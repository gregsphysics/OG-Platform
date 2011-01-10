/**
 * Copyright (C) 2009 - Present by OpenGamma Inc.
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.currency;

import com.opengamma.engine.ComputationTargetType;

/**
 * Injects a default currency requirement into the graph at a security.
 */
public class SecurityDefaultCurrencyFunction extends DefaultCurrencyFunction {

  public SecurityDefaultCurrencyFunction(final String valueName) {
    super(ComputationTargetType.SECURITY, valueName);
  }

  public SecurityDefaultCurrencyFunction(final String... valueNames) {
    super(ComputationTargetType.SECURITY, valueNames);
  }

}
