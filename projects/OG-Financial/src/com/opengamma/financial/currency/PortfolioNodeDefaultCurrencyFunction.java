/**
 * Copyright (C) 2009 - Present by OpenGamma Inc.
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.currency;

import com.opengamma.engine.ComputationTargetType;

/**
 * Injects a default currency requirement into the graph at a portfolio node.
 */
public class PortfolioNodeDefaultCurrencyFunction extends DefaultCurrencyFunction {

  public PortfolioNodeDefaultCurrencyFunction(final String valueName) {
    super(ComputationTargetType.PORTFOLIO_NODE, valueName);
  }

  public PortfolioNodeDefaultCurrencyFunction(final String... valueNames) {
    super(ComputationTargetType.PORTFOLIO_NODE, valueNames);
  }

}
