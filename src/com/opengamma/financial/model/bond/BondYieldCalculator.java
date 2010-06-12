/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.model.bond;

import com.opengamma.financial.model.cashflow.PresentValueCalculator;
import com.opengamma.financial.model.interestrate.InterestRateModel;
import com.opengamma.financial.model.interestrate.curve.ConstantInterestRateModel;
import com.opengamma.math.function.Function1D;
import com.opengamma.math.rootfinding.RealSingleRootFinder;
import com.opengamma.math.rootfinding.VanWijngaardenDekkerBrentSingleRootFinder;
import com.opengamma.util.timeseries.DoubleTimeSeries;

/**
 * @author emcleod
 * 
 */
public class BondYieldCalculator {
  private final RealSingleRootFinder _root = new VanWijngaardenDekkerBrentSingleRootFinder();

  public double calculate(final DoubleTimeSeries<Long> cashFlows, final Double price, final Long date, final PresentValueCalculator pvCalculator) {
    if (cashFlows == null) {
      throw new IllegalArgumentException("Cash flow time series was null");
    }
    if (cashFlows.isEmpty()) {
      throw new IllegalArgumentException("Cash flow time series was empty");
    }
    if (price <= 0) {
      throw new IllegalArgumentException("Price must be positive");
    }
    if (date == null) {
      throw new IllegalArgumentException("Date was null");
    }
    if (pvCalculator == null) {
      throw new IllegalArgumentException("Present value calculator was null");
    }
    final Function1D<Double, Double> f = new Function1D<Double, Double>() {

      @Override
      public Double evaluate(final Double y) {
        final InterestRateModel<Double> rate = new ConstantInterestRateModel(y);
        return pvCalculator.calculate(cashFlows, rate, date) - price;
      }

    };
    return _root.getRoot(f, 0., 100000.);
  }
}
