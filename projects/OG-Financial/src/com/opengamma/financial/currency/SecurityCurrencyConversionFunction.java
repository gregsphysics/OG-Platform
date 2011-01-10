/**
 * Copyright (C) 2009 - Present by OpenGamma Inc.
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.currency;

import com.opengamma.engine.ComputationTargetType;

/**
 * Converts a value from one currency to another, acting on a security.
 */
public class SecurityCurrencyConversionFunction extends CurrencyConversionFunction {

  public SecurityCurrencyConversionFunction(final String valueName) {
    super(ComputationTargetType.SECURITY, valueName);
  }

  public SecurityCurrencyConversionFunction(final String... valueNames) {
    super(ComputationTargetType.SECURITY, valueNames);
  }

}
