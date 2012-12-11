/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.web.spring.defaults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.opengamma.financial.analytics.model.curve.forward.ForwardCurveValuePropertyNames;

/**
 * 
 */
public abstract class EquityBlackVolatilitySurfaceDefaultValues {
  private static final Map<String, String> TICKERS = new HashMap<String, String>(); 
  private static final Map<String, String> DISCOUNTING_CURVE_NAMES = new HashMap<String, String>();
  private static final Map<String, String> FORWARD_CURVE_NAMES = new HashMap<String, String>();
  private static final Map<String, String> DISCOUNTING_CURVE_CALCULATION_CONFIG_NAMES = new HashMap<String, String>();
  private static final Map<String, String> FORWARD_CURVE_CALCULATION_CONFIG_NAMES = new HashMap<String, String>();
  private static final Map<String, String> FORWARD_CURVE_CALCULATION_METHOD_NAMES = new HashMap<String, String>();
  private static final Map<String, String> VOLATILITY_SURFACE_NAMES = new HashMap<String, String>(); 
  
  static {
    TICKERS.put("DJX Index", "USD");
    TICKERS.put("SPX Index", "USD");
    TICKERS.put("NDX Index", "USD");
    TICKERS.put("RUY Index", "USD");
    TICKERS.put("NKY Index", "JPY");
    DISCOUNTING_CURVE_NAMES.put("USD", "Discounting");
    DISCOUNTING_CURVE_NAMES.put("JPY", "Discounting");
    FORWARD_CURVE_NAMES.put("USD", "Discounting");
    FORWARD_CURVE_NAMES.put("JPY", "Discounting");
    DISCOUNTING_CURVE_CALCULATION_CONFIG_NAMES.put("USD", "DefaultTwoCurveUSDConfig");
    DISCOUNTING_CURVE_CALCULATION_CONFIG_NAMES.put("JPY", "DefaultTwoCurveJPYConfig");
    FORWARD_CURVE_CALCULATION_CONFIG_NAMES.put("USD", "DefaultTwoCurveUSDConfig");
    FORWARD_CURVE_CALCULATION_CONFIG_NAMES.put("JPY", "DefaultTwoCurveJPYConfig");
    FORWARD_CURVE_CALCULATION_METHOD_NAMES.put("USD", ForwardCurveValuePropertyNames.PROPERTY_YIELD_CURVE_IMPLIED_METHOD);
    FORWARD_CURVE_CALCULATION_METHOD_NAMES.put("JPY", ForwardCurveValuePropertyNames.PROPERTY_YIELD_CURVE_IMPLIED_METHOD);
    VOLATILITY_SURFACE_NAMES.put("DJX Index", "BBG");
    VOLATILITY_SURFACE_NAMES.put("SPX Index", "BBG");
    VOLATILITY_SURFACE_NAMES.put("NDX Index", "BBG");
    VOLATILITY_SURFACE_NAMES.put("RUY Index", "BBG");
    VOLATILITY_SURFACE_NAMES.put("NKY Index", "BBG");
  }

  /**
   * Builder interface
   */
  public interface Builder {
    
    Builder useTickers();
    
    Builder useDiscountingCurveNames();
    
    Builder useDiscountingCurveCurrency();
    
    Builder useDiscountingCurveCalculationConfigNames();
    
    Builder useForwardCurveNames();
    
    Builder useForwardCurveCalculationMethodNames();
    
    Builder useForwardCurveCalculationConfigNames();
    
    Builder useVolatilitySurfaceNames();
    
    List<String> createDefaults();
  }
  
  private EquityBlackVolatilitySurfaceDefaultValues() {
  }

  public static Builder builder() {
    return new MyBuilder();
  }
  
  private static final class MyBuilder implements Builder {
    private final boolean _useTickers;
    private final boolean _useDiscountingCurveNames;
    private final boolean _useDiscountingCurveCurrency;
    private final boolean _useDiscountingCurveCalculationConfigNames;
    private final boolean _useForwardCurveNames;
    private final boolean _useForwardCurveCalculationMethodNames;
    private final boolean _useForwardCurveCalculationConfigNames;
    private final boolean _useVolatilitySurfaceNames;
    private final List<Integer> _order;
    
    /* package */ MyBuilder() {
      this(false, false, false, false, false, false, false, false, new ArrayList<Integer>());
    }
    
    /* package */ MyBuilder(final boolean useTickers, final boolean useDiscountingCurveNames, final boolean useDiscountingCurveCurrency,
        final boolean useDiscountingCurveCalculationConfigNames, final boolean useForwardCurveNames, final boolean useForwardCurveCalculationNames, 
        final boolean useForwardCurveCalculationConfigNames, final boolean useVolatilitySurfaceNames, final List<Integer> order) {
      _useTickers = useTickers;
      _useDiscountingCurveNames = useDiscountingCurveNames;
      _useDiscountingCurveCurrency = useDiscountingCurveCurrency;
      _useDiscountingCurveCalculationConfigNames = useDiscountingCurveCalculationConfigNames;
      _useForwardCurveNames = useForwardCurveNames;
      _useForwardCurveCalculationMethodNames = useForwardCurveCalculationNames;
      _useForwardCurveCalculationConfigNames = useForwardCurveCalculationConfigNames;
      _useVolatilitySurfaceNames = useVolatilitySurfaceNames;
      _order = order;
    }
    
    @Override
    public Builder useTickers() {
      if (_useTickers == true) {
        return this;
      }
      List<Integer> order = new ArrayList<Integer>(_order);
      order.add(0);
      return new MyBuilder(true, _useDiscountingCurveNames, _useDiscountingCurveCurrency, _useDiscountingCurveCalculationConfigNames, 
          _useForwardCurveNames, _useForwardCurveCalculationMethodNames, _useForwardCurveCalculationConfigNames, 
          _useVolatilitySurfaceNames, order);
    }
    
    @Override
    public Builder useDiscountingCurveNames() {
      if (_useDiscountingCurveNames == true) {
        return this;
      }
      List<Integer> order = new ArrayList<Integer>(_order);
      order.add(1);
      return new MyBuilder(_useTickers, true, _useDiscountingCurveCurrency, _useDiscountingCurveCalculationConfigNames, 
          _useForwardCurveNames, _useForwardCurveCalculationMethodNames, _useForwardCurveCalculationConfigNames, 
          _useVolatilitySurfaceNames, order);
    }

    @Override
    public Builder useDiscountingCurveCurrency() {
      if (_useDiscountingCurveCurrency == true) {
        return this;
      }
      List<Integer> order = new ArrayList<Integer>(_order);
      order.add(2);
      return new MyBuilder(_useTickers, _useDiscountingCurveNames, true, _useDiscountingCurveCalculationConfigNames, 
          _useForwardCurveNames, _useForwardCurveCalculationMethodNames, _useForwardCurveCalculationConfigNames, 
          _useVolatilitySurfaceNames, order);
    }

    @Override
    public Builder useDiscountingCurveCalculationConfigNames() {
      if (_useDiscountingCurveCalculationConfigNames == true) {
        return this;
      }
      List<Integer> order = new ArrayList<Integer>(_order);
      order.add(3);
      return new MyBuilder(_useTickers, _useDiscountingCurveNames, _useDiscountingCurveCurrency, true, 
          _useForwardCurveNames, _useForwardCurveCalculationMethodNames, _useForwardCurveCalculationConfigNames, 
          _useVolatilitySurfaceNames, order);
    }

    @Override
    public Builder useForwardCurveNames() {
      if (_useForwardCurveNames == true) {
        return this;
      }
      List<Integer> order = new ArrayList<Integer>(_order);
      order.add(4);
      return new MyBuilder(_useTickers, _useDiscountingCurveNames, _useDiscountingCurveCurrency, _useDiscountingCurveCalculationConfigNames, 
          true, _useForwardCurveCalculationMethodNames, _useForwardCurveCalculationConfigNames, 
          _useVolatilitySurfaceNames, order);
    }

    @Override
    public Builder useForwardCurveCalculationMethodNames() {
      if (_useForwardCurveCalculationMethodNames == true) {
        return this;
      }
      List<Integer> order = new ArrayList<Integer>(_order);
      order.add(5);
      return new MyBuilder(_useTickers, _useDiscountingCurveNames, _useDiscountingCurveCurrency, _useDiscountingCurveCalculationConfigNames, 
          _useForwardCurveNames, true, _useForwardCurveCalculationConfigNames, 
          _useVolatilitySurfaceNames, order);
    }

    @Override
    public Builder useForwardCurveCalculationConfigNames() {
      if (_useForwardCurveCalculationConfigNames == true) {
        return this;
      }
      List<Integer> order = new ArrayList<Integer>(_order);
      order.add(6);
      return new MyBuilder(_useTickers, _useDiscountingCurveNames, _useDiscountingCurveCurrency, _useDiscountingCurveCalculationConfigNames, 
          _useForwardCurveNames, _useForwardCurveCalculationMethodNames, true, 
          _useVolatilitySurfaceNames, order);
    }

    @Override
    public Builder useVolatilitySurfaceNames() {
      if (_useVolatilitySurfaceNames == true) {
        return this;
      }
      List<Integer> order = new ArrayList<Integer>(_order);
      order.add(7);
      return new MyBuilder(_useTickers, _useDiscountingCurveNames, _useDiscountingCurveCurrency, _useDiscountingCurveCalculationConfigNames, 
          _useForwardCurveNames, _useForwardCurveCalculationMethodNames, _useForwardCurveCalculationConfigNames, 
          true, order);
    }
    
    @SuppressWarnings("synthetic-access")
    @Override
    public List<String> createDefaults() {
      List<String> result = new ArrayList<String>();
      Iterator<Map.Entry<String, String>> iterator = TICKERS.entrySet().iterator();
      for (int i = 0; i < TICKERS.size(); i++) {
        Map.Entry<String, String> entry = iterator.next();
        String ticker = entry.getKey();
        String id = entry.getValue();
        for (Integer field : _order) {
          switch (field) {
            case 0: 
              result.add(ticker);
              break;
            case 1:
              result.add(DISCOUNTING_CURVE_NAMES.get(id));
              break;
            case 2: 
              result.add(id);
              break;
            case 3:
              result.add(DISCOUNTING_CURVE_CALCULATION_CONFIG_NAMES.get(id));
              break;
            case 4:
              result.add(FORWARD_CURVE_NAMES.get(id));
              break;
            case 5:
              result.add(FORWARD_CURVE_CALCULATION_METHOD_NAMES.get(id));
              break;
            case 6:
              result.add(FORWARD_CURVE_CALCULATION_CONFIG_NAMES.get(id));
              break;
            case 7:
              result.add(VOLATILITY_SURFACE_NAMES.get(ticker));
              break;
            default:
              throw new IllegalStateException();
          }
        }
      }
      return result;
    }
  }
  
}
