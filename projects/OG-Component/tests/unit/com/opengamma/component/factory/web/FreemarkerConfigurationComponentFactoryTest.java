/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.component.factory.web;

import static org.mockito.Mockito.mock;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import javax.servlet.ServletContext;

import org.testng.annotations.Test;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;

/**
 *
 */
public class FreemarkerConfigurationComponentFactoryTest {

  @Test
  public void createLoaders() {
    FreemarkerConfigurationComponentFactory factory = new FreemarkerConfigurationComponentFactory();
    factory.setTemplateLocations("servlet-context:WEB-INF/pages, file:" + System.getProperty("java.io.tmpdir"));
    TemplateLoader[] loaders = factory.createLoaders(mock(ServletContext.class));
    assertNotNull(loaders);
    assertEquals(2, loaders.length);
    assertTrue(loaders[0] instanceof WebappTemplateLoader);
    assertTrue(loaders[1] instanceof FileTemplateLoader);
  }
}
