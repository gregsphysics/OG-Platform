/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang.StringUtils;
import org.joda.beans.Bean;
import org.joda.beans.MetaProperty;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;
import org.threeten.bp.ZoneId;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.OpenGammaClock;
import com.opengamma.util.PlatformConfigUtils;
import com.opengamma.util.ResourceUtils;

/**
 * Manages the process of loading and starting OpenGamma components.
 * <p>
 * The OpenGamma logical architecture consists of a set of components.
 * This class loads and starts the components based on configuration.
 * The end result is a populated {@link ComponentRepository}.
 * <p>
 * Two types of config file format are recognized - properties and INI.
 * The INI file is the primary file for loading the components, see {@link ComponentConfigIniLoader}.
 * The behavior of an INI file can be controlled using properties.
 * <p>
 * The properties can either be specified manually before {@link #start(Resource))}
 * is called or loaded by specifying a properties file instead of an INI file.
 * The properties file must contain the key "MANAGER.NEXT.FILE" which is used to load the next file.
 * The next file is normally the INI file, but could be another properties file.
 * As such, the properties files can be chained.
 * <p>
 * Properties are never overwritten, thus manual properties have priority over file-based, and
 * earlier file-based have priority over later file-based.
 * <p>
 * It is not intended that the manager is retained for the lifetime of
 * the application, the repository is intended for that purpose.
 */
public class ComponentManager {

  /**
   * The server name property.
   */
  private static final String OPENGAMMA_SERVER_NAME = "og.server.name";
  /**
   * The key identifying the next config file in a properties file.
   */
  static final String MANAGER_NEXT_FILE = "MANAGER.NEXT.FILE";
  /**
   * The key identifying the entire combined set of active properties.
   */
  static final String MANAGER_PROPERTIES = "MANAGER.PROPERTIES";
  /**
   * The key identifying the the inclusion of another file.
   */
  static final String MANAGER_INCLUDE = "MANAGER.INCLUDE";

  /**
   * The component repository.
   */
  private final ComponentRepository _repo;
  /**
   * The component logger.
   */
  private final ComponentLogger _logger;
  /**
   * The component properties.
   */
  private final ConcurrentMap<String, String> _properties = new ConcurrentHashMap<String, String>();

  /**
   * Creates an instance that does not log.
   * 
   * @param serverName  the server name, not null
   */
  public ComponentManager(String serverName) {
    this(serverName, ComponentLogger.Sink.INSTANCE);
  }

  /**
   * Creates an instance.
   * 
   * @param serverName  the server name, not null
   * @param logger  the logger, not null
   */
  public ComponentManager(String serverName, ComponentLogger logger) {
    this(serverName, new ComponentRepository(logger));
  }

  /**
   * Creates an instance.
   * 
   * @param serverName  the server name, not null
   * @param repo  the repository to use, not null
   */
  protected ComponentManager(String serverName, ComponentRepository repo) {
    ArgumentChecker.notNull(serverName, "serverName");
    ArgumentChecker.notNull(repo, "repo");
    _repo = repo;
    _logger = repo.getLogger();
    setServerName(serverName);
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the repository of components.
   * 
   * @return the repository, not null
   */
  public ComponentRepository getRepository() {
    return _repo;
  }

  /**
   * Gets the properties used while loading the manager.
   * <p>
   * This may be populated before calling {@link #start()} if desired.
   * This is an alternative to using a separate properties file.
   * 
   * @return the map of key-value properties, not null
   */
  public ConcurrentMap<String, String> getProperties() {
    return _properties;
  }

  //-------------------------------------------------------------------------
  /**
   * Sets the server name property.
   * <p>
   * This can be used as a general purpose name for the server.
   * 
   * @return the server name, null if name not set
   */
  public String getServerName() {
    return getProperties().get(OPENGAMMA_SERVER_NAME);
  }

  /**
   * Sets the server name property.
   * <p>
   * This can be used as a general purpose name for the server.
   * 
   * @param serverName  the server name, not null
   */
  public void setServerName(String serverName) {
    getProperties().put(OPENGAMMA_SERVER_NAME, serverName);
    System.setProperty(OPENGAMMA_SERVER_NAME, serverName);
  }

  //-------------------------------------------------------------------------
  /**
   * Initializes the components based on the specified resource.
   * <p>
   * See {@link #createResource(String)} for the valid resource location formats.
   * 
   * @param resourceLocation  the resource location, not null
   * @return the created repository, not null
   */
  public ComponentRepository start(String resourceLocation) {
    Resource resource = ResourceUtils.createResource(resourceLocation);
    return start(resource);
  }

  /**
   * Initializes the components based on the specified resource.
   * 
   * @param resource  the config resource to load, not null
   * @return the created repository, not null
   */
  public ComponentRepository start(Resource resource) {
    _logger.logInfo("  Using file: " + resource.getDescription());
    
    if (resource.getFilename().endsWith(".properties")) {
      String nextConfig = loadProperties(resource);
      if (nextConfig == null) {
        throw new OpenGammaRuntimeException("The properties file must contain the key '" + MANAGER_NEXT_FILE + "' to specify the next file to load: " + resource);
      }
      return start(nextConfig);
    }
    if (resource.getFilename().endsWith(".ini")) {
      loadIni(resource);
      start();
      return getRepository();
    }
    throw new OpenGammaRuntimeException("Unknown file format: " + resource);
  }

  //-------------------------------------------------------------------------
  /**
   * Loads a properties file into the replacements map.
   * <p>
   * The properties file must be in the standard format defined by {@link Properties}.
   * The file must contain a key "component.ini"
   * 
   * @param resource  the properties resource location, not null
   * @return the next configuration file to load, null if not specified
   */
  protected String loadProperties(Resource resource) {
    ComponentConfigPropertiesLoader loader = new ComponentConfigPropertiesLoader(_logger, getProperties());
    return loader.load(resource, 0);
  }

  /**
   * Loads the INI file and initializes the components based on the contents.
   * 
   * @param resource  the INI resource location, not null
   */
  protected void loadIni(Resource resource) {
    ComponentConfigIniLoader loader = new ComponentConfigIniLoader(_logger, getProperties());
    ComponentConfig config = loader.load(resource, 0);
    
    logProperties();
    getRepository().pushThreadLocal();
    initGlobal(config);
    init(config);
  }

  private void logProperties() {
    _logger.logDebug("--- Using merged properties ---");
    Map<String, String> properties = new TreeMap<String, String>(getProperties());
    for (String key : properties.keySet()) {
      if (key.contains("password")) {
        _logger.logDebug(" " + key + " = " + StringUtils.repeat("*", properties.get(key).length()));
      } else {
        _logger.logDebug(" " + key + " = " + properties.get(key));
      }
    }
  }

  //-------------------------------------------------------------------------
  protected void initGlobal(ComponentConfig config) {
    LinkedHashMap<String, String> global = config.getGroup("global");
    if (global != null) {
      PlatformConfigUtils.configureSystemProperties();
      String zoneId = global.get("time.zone");
      if (zoneId != null) {
        OpenGammaClock.setZone(ZoneId.of(zoneId));
      }
    }
  }

  /**
   * Initializes the repository from the config.
   * 
   * @param config  the loaded config, not null
   */
  protected void init(ComponentConfig config) {
    for (String groupName : config.getGroups()) {
      LinkedHashMap<String, String> groupData = config.getGroup(groupName);
      if (groupData.containsKey("factory")) {
        initComponent(groupName, groupData);
      }
    }
  }

  //-------------------------------------------------------------------------
  /**
   * Starts the components.
   */
  protected void start() {
    _logger.logInfo("--- Starting Lifecycle ---");
    long startInstant = System.nanoTime();
    
    getRepository().start();
    
    long endInstant = System.nanoTime();
    _logger.logInfo("--- Started Lifecycle in " + ((endInstant - startInstant) / 1000000L) + "ms ---");
  }

  //-------------------------------------------------------------------------
  /**
   * Initialize the component.
   * 
   * @param groupName  the group name, not null
   * @param groupConfig  the config data, not null
   */
  protected void initComponent(String groupName, LinkedHashMap<String, String> groupConfig) {
    _logger.logInfo("--- Initializing " + groupName + " ---");
    long startInstant = System.nanoTime();
    
    LinkedHashMap<String, String> remainingConfig = new LinkedHashMap<String, String>(groupConfig);
    String typeStr = remainingConfig.remove("factory");
    _logger.logDebug(" Initializing factory '" + typeStr);
    _logger.logDebug(" Using properties " + remainingConfig);
    
    // load factory
    ComponentFactory factory = loadFactory(typeStr);
    
    // set properties
    try {
      setFactoryProperties(factory, remainingConfig);
    } catch (Exception ex) {
      throw new OpenGammaRuntimeException("Failed to set component factory properties: '" + groupName + "' with " + groupConfig, ex);
    }
    
    // init
    try {
      initFactory(factory, remainingConfig);
    } catch (Exception ex) {
      throw new OpenGammaRuntimeException("Failed to init component factory: '" + groupName + "' with " + groupConfig, ex);
    }
    
    long endInstant = System.nanoTime();
    _logger.logInfo("--- Initialized " + groupName + " in " + ((endInstant - startInstant) / 1000000L) + "ms ---");
  }

  //-------------------------------------------------------------------------
  /**
   * Loads the factory.
   * A factory should perform minimal work in the constructor.
   * 
   * @param typeStr  the factory type class name, not null
   * @return the factory, not null
   */
  protected ComponentFactory loadFactory(String typeStr) {
    ComponentFactory factory;
    try {
      Class<? extends ComponentFactory> cls = getClass().getClassLoader().loadClass(typeStr).asSubclass(ComponentFactory.class);
      factory = cls.newInstance();
    } catch (ClassNotFoundException ex) {
      throw new OpenGammaRuntimeException("Unknown component factory: " + typeStr, ex);
    } catch (InstantiationException ex) {
      throw new OpenGammaRuntimeException("Unable to create component factory: " + typeStr, ex);
    } catch (IllegalAccessException ex) {
      throw new OpenGammaRuntimeException("Unable to access component factory: " + typeStr, ex);
    }
    return factory;
  }

  //-------------------------------------------------------------------------
  /**
   * Sets the properties on the factory.
   * 
   * @param factory  the factory, not null
   * @param remainingConfig  the config data, not null
   * @throws Exception allowing throwing of a checked exception
   */
  protected void setFactoryProperties(ComponentFactory factory, LinkedHashMap<String, String> remainingConfig) throws Exception {
    if (factory instanceof Bean) {
      Bean bean = (Bean) factory;
      for (MetaProperty<?> mp : bean.metaBean().metaPropertyIterable()) {
        String value = remainingConfig.remove(mp.name());
        setProperty(bean, mp, value);
      }
    }
  }

  /**
   * Sets an individual property.
   * <p>
   * This method handles the main special case formats of the value.
   * 
   * @param bean  the bean, not null
   * @param mp  the property, not null
   * @param value  the configured value, not null
   * @throws Exception allowing throwing of a checked exception
   */
  protected void setProperty(Bean bean, MetaProperty<?> mp, String value) throws Exception {
    if (ComponentRepository.class.equals(mp.propertyType())) {
      // set the repo
      mp.set(bean, getRepository());
      
    } else if (value == null) {
      // set to ensure validated by factory
      mp.set(bean, mp.get(bean));
      
    } else if ("null".equals(value)) {
      // forcibly set to null
      mp.set(bean, null);
      
    } else if (value.contains("::")) {
      // double colon used for component references
      setPropertyComponentRef(bean, mp, value);
      
    } else if (MANAGER_PROPERTIES.equals(value) && Resource.class.equals(mp.propertyType())) {
      // set to the combined set of properties
      setPropertyMergedProperties(bean, mp);
      
    } else {
      // set value
      setPropertyInferType(bean, mp, value);
    }
  }

  /**
   * Intelligently sets the property to the merged set of properties.
   * <p>
   * The key "MANAGER.PROPERTIES" can be used in a properties file to refer to
   * the entire set of merged properties. This is normally what you want to pass
   * into other systems (such as Spring) that need a set of properties.
   * 
   * @param bean  the bean, not null
   * @param mp  the property, not null
   * @throws Exception allowing throwing of a checked exception
   */
  protected void setPropertyMergedProperties(Bean bean, MetaProperty<?> mp) throws Exception {
    final String desc = MANAGER_PROPERTIES + " for " + mp;
    final ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
    Properties props = new Properties();
    props.putAll(getProperties());
    props.store(out, desc);
    out.close();
    Resource resource = new AbstractResource() {
      @Override
      public String getDescription() {
        return MANAGER_PROPERTIES;
      }
      @Override
      public String getFilename() throws IllegalStateException {
        return MANAGER_PROPERTIES + ".properties";
      }
      @Override
      public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(out.toByteArray());
      }
      @Override
      public String toString() {
        return desc;
      }
    };
    mp.set(bean, resource);
  }

  /**
   * Intelligently sets the property which is a component reference.
   * <p>
   * The double colon is used in the format {@code Type::Classifier}.
   * If the type is omitted, this method will try to infer it.
   * 
   * @param bean  the bean, not null
   * @param mp  the property, not null
   * @param value  the configured value containing double colon, not null
   */
  protected void setPropertyComponentRef(Bean bean, MetaProperty<?> mp, String value) {
    Class<?> propertyType = mp.propertyType();
    String type = StringUtils.substringBefore(value, "::");
    String classifier = StringUtils.substringAfter(value, "::");
    if (type.length() == 0) {
      try {
        // infer type
        mp.set(bean, getRepository().getInstance(propertyType, classifier));
        return;
      } catch (RuntimeException ex) {
        throw new OpenGammaRuntimeException("Unable to set property " + mp + " of type " + propertyType.getName(), ex);
      }
    }
    ComponentInfo info = getRepository().findInfo(type, classifier);
    if (info == null) {
      throw new OpenGammaRuntimeException("Unable to find component reference '" + value + "' while setting property " + mp);
    }
    if (ComponentInfo.class.isAssignableFrom(propertyType)) {
      mp.set(bean, info);
    } else {
      mp.set(bean, getRepository().getInstance(info));
    }
  }

  /**
   * Intelligently sets the property.
   * <p>
   * This uses the repository to link properties declared with classifiers to the instance.
   * 
   * @param bean  the bean, not null
   * @param mp  the property, not null
   * @param value  the configured value, not null
   */
  protected void setPropertyInferType(Bean bean, MetaProperty<?> mp, String value) {
    Class<?> propertyType = mp.propertyType();
    if (propertyType == Resource.class) {
      mp.set(bean, ResourceUtils.createResource(value));
      
    } else {
      // set property by value type conversion from String
      try {
        mp.setString(bean, value);
        
      } catch (RuntimeException ex) {
        throw new OpenGammaRuntimeException("Unable to set property " + mp, ex);
      }
    }
  }

  //-------------------------------------------------------------------------
  /**
   * Initializes the factory.
   * <p>
   * The real work of creating the component and registering it should be done here.
   * The factory may also publish a RESTful view and/or a life-cycle method.
   * 
   * @param factory  the factory to initialize, not null
   * @param remainingConfig  the remaining configuration data, not null
   * @throws Exception to allow components to throw checked exceptions
   */
  protected void initFactory(ComponentFactory factory, LinkedHashMap<String, String> remainingConfig) throws Exception {
    factory.init(getRepository(), remainingConfig);
    if (remainingConfig.size() > 0) {
      throw new IllegalStateException("Configuration was specified but not used: " + remainingConfig);
    }
  }

}
