/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.convention;

import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;

/**
 * 
 */
@BeanDefinition
public class InterestRateFutureConvention extends ExchangeTradedFutureAndOptionConvention {

  /** Serialization version. */
  private static final long serialVersionUID = 1L;

  /**
   * The index convention.
   */
  @PropertyDefinition(validate = "notNull")
  private String _indexConvention;

  /**
   * For the builder.
   */
  public InterestRateFutureConvention() {
  }

  public InterestRateFutureConvention(final String name, final ExternalIdBundle externalIdBundle, final String expiryConvention,
      final ExternalId exchangeCalendar, final String indexConvention) {
    super(name, externalIdBundle, expiryConvention, exchangeCalendar);
    setIndexConvention(indexConvention);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code InterestRateFutureConvention}.
   * @return the meta-bean, not null
   */
  public static InterestRateFutureConvention.Meta meta() {
    return InterestRateFutureConvention.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(InterestRateFutureConvention.Meta.INSTANCE);
  }

  @Override
  public InterestRateFutureConvention.Meta metaBean() {
    return InterestRateFutureConvention.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -668532253:  // indexConvention
        return getIndexConvention();
    }
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -668532253:  // indexConvention
        setIndexConvention((String) newValue);
        return;
    }
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  protected void validate() {
    JodaBeanUtils.notNull(_indexConvention, "indexConvention");
    super.validate();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      InterestRateFutureConvention other = (InterestRateFutureConvention) obj;
      return JodaBeanUtils.equal(getIndexConvention(), other.getIndexConvention()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(getIndexConvention());
    return hash ^ super.hashCode();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the index convention.
   * @return the value of the property, not null
   */
  public String getIndexConvention() {
    return _indexConvention;
  }

  /**
   * Sets the index convention.
   * @param indexConvention  the new value of the property, not null
   */
  public void setIndexConvention(String indexConvention) {
    JodaBeanUtils.notNull(indexConvention, "indexConvention");
    this._indexConvention = indexConvention;
  }

  /**
   * Gets the the {@code indexConvention} property.
   * @return the property, not null
   */
  public final Property<String> indexConvention() {
    return metaBean().indexConvention().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code InterestRateFutureConvention}.
   */
  public static class Meta extends ExchangeTradedFutureAndOptionConvention.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code indexConvention} property.
     */
    private final MetaProperty<String> _indexConvention = DirectMetaProperty.ofReadWrite(
        this, "indexConvention", InterestRateFutureConvention.class, String.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
      this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "indexConvention");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -668532253:  // indexConvention
          return _indexConvention;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends InterestRateFutureConvention> builder() {
      return new DirectBeanBuilder<InterestRateFutureConvention>(new InterestRateFutureConvention());
    }

    @Override
    public Class<? extends InterestRateFutureConvention> beanType() {
      return InterestRateFutureConvention.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code indexConvention} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> indexConvention() {
      return _indexConvention;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}