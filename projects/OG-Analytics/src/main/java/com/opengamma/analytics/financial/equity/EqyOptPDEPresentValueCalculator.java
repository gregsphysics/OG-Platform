/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.equity;

import com.opengamma.analytics.financial.ExerciseDecisionType;
import com.opengamma.analytics.financial.equity.option.EquityIndexOption;
import com.opengamma.analytics.financial.equity.option.EquityOption;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivativeVisitorAdapter;
import com.opengamma.analytics.financial.model.finitedifference.applications.BlackScholesMertonPDEPricer;
import com.opengamma.util.ArgumentChecker;

/**
 * Calculates the present value of equity options using the Black method.
 */
public final class EqyOptPDEPresentValueCalculator extends InstrumentDerivativeVisitorAdapter<StaticReplicationDataBundle, Double> {
  /** A static instance */
  private static final EqyOptPDEPresentValueCalculator INSTANCE = new EqyOptPDEPresentValueCalculator();
  /** The present value calculator */
  private static final BlackScholesMertonPDEPricer MODEL = new BlackScholesMertonPDEPricer();

  /**
   * Gets the static instance
   * @return The static instance
   */
  public static EqyOptPDEPresentValueCalculator getInstance() {
    return INSTANCE;
  }

  private EqyOptPDEPresentValueCalculator() {
  }

  @Override
  public Double visitEquityIndexOption(final EquityIndexOption option, final StaticReplicationDataBundle data) {
    ArgumentChecker.notNull(option, "option");
    ArgumentChecker.notNull(data, "data");
    final double spot = data.getForwardCurve().getSpot();
    final double strike = option.getStrike();
    final double time = option.getTimeToExpiry();
    final double sigma = data.getVolatilitySurface().getVolatility(time, strike);
    final boolean isCall = option.isCall();
    final boolean isAmerican;
    final ExerciseDecisionType exercise = option.getExerciseType();
    if (exercise == ExerciseDecisionType.AMERICAN) {
      isAmerican = true;
    } else if (exercise == ExerciseDecisionType.EUROPEAN) {
      isAmerican = false;
    } else {
      throw new IllegalArgumentException("Can only price American or European expiry options");
    }
    final double interestRate = data.getDiscountCurve().getInterestRate(time);
    final double costOfCarry = interestRate; //TODO
    return option.getUnitAmount() * MODEL.price(spot, strike, interestRate, costOfCarry, time, sigma, isCall, isAmerican, 10, 500);
  }

  @Override
  public Double visitEquityOption(final EquityOption option, final StaticReplicationDataBundle data) {
    ArgumentChecker.notNull(option, "option");
    ArgumentChecker.notNull(data, "data");
    final double spot = data.getForwardCurve().getSpot();
    final double strike = option.getStrike();
    final double time = option.getTimeToExpiry();
    final double sigma = data.getVolatilitySurface().getVolatility(time, strike);
    final boolean isCall = option.isCall();
    final boolean isAmerican;
    final ExerciseDecisionType exercise = option.getExerciseType();
    if (exercise == ExerciseDecisionType.AMERICAN) {
      isAmerican = true;
    } else if (exercise == ExerciseDecisionType.EUROPEAN) {
      isAmerican = false;
    } else {
      throw new IllegalArgumentException("Can only price American or European expiry options");
    }
    final double interestRate = data.getDiscountCurve().getInterestRate(time);
    final double costOfCarry = interestRate; //TODO
    return option.getUnitAmount() * MODEL.price(spot, strike, interestRate, costOfCarry, time, sigma, isCall, isAmerican, 10, 500);
  }

}