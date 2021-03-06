/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.provider.calculator.generic;

import com.opengamma.analytics.financial.commodity.derivative.AgricultureFutureOption;
import com.opengamma.analytics.financial.commodity.derivative.EnergyFutureOption;
import com.opengamma.analytics.financial.commodity.derivative.MetalFutureOption;
import com.opengamma.analytics.financial.equity.option.EquityIndexFutureOption;
import com.opengamma.analytics.financial.equity.option.EquityIndexOption;
import com.opengamma.analytics.financial.equity.option.EquityOption;
import com.opengamma.analytics.financial.forex.derivative.Forex;
import com.opengamma.analytics.financial.forex.derivative.ForexSwap;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivativeVisitorAdapter;
import com.opengamma.analytics.financial.interestrate.annuity.derivative.Annuity;
import com.opengamma.analytics.financial.interestrate.annuity.derivative.AnnuityCouponFixed;
import com.opengamma.analytics.financial.interestrate.bond.definition.BillTransaction;
import com.opengamma.analytics.financial.interestrate.bond.definition.BondFixedSecurity;
import com.opengamma.analytics.financial.interestrate.bond.definition.BondFixedTransaction;
import com.opengamma.analytics.financial.interestrate.bond.definition.BondIborSecurity;
import com.opengamma.analytics.financial.interestrate.bond.definition.BondIborTransaction;
import com.opengamma.analytics.financial.interestrate.cash.derivative.Cash;
import com.opengamma.analytics.financial.interestrate.cash.derivative.DepositCounterpart;
import com.opengamma.analytics.financial.interestrate.cash.derivative.DepositIbor;
import com.opengamma.analytics.financial.interestrate.cash.derivative.DepositZero;
import com.opengamma.analytics.financial.interestrate.fra.derivative.ForwardRateAgreement;
import com.opengamma.analytics.financial.interestrate.future.derivative.BondFutureOptionPremiumSecurity;
import com.opengamma.analytics.financial.interestrate.future.derivative.BondFutureOptionPremiumTransaction;
import com.opengamma.analytics.financial.interestrate.future.derivative.InterestRateFutureOptionMarginSecurity;
import com.opengamma.analytics.financial.interestrate.future.derivative.InterestRateFutureOptionMarginTransaction;
import com.opengamma.analytics.financial.interestrate.future.derivative.InterestRateFutureOptionPremiumSecurity;
import com.opengamma.analytics.financial.interestrate.future.derivative.InterestRateFutureOptionPremiumTransaction;
import com.opengamma.analytics.financial.interestrate.future.derivative.InterestRateFutureSecurity;
import com.opengamma.analytics.financial.interestrate.future.derivative.InterestRateFutureTransaction;
import com.opengamma.analytics.financial.interestrate.future.derivative.SwapFuturesPriceDeliverableTransaction;
import com.opengamma.analytics.financial.interestrate.inflation.derivative.CouponInflationYearOnYearInterpolation;
import com.opengamma.analytics.financial.interestrate.inflation.derivative.CouponInflationYearOnYearMonthly;
import com.opengamma.analytics.financial.interestrate.inflation.derivative.CouponInflationZeroCouponInterpolation;
import com.opengamma.analytics.financial.interestrate.inflation.derivative.CouponInflationZeroCouponInterpolationGearing;
import com.opengamma.analytics.financial.interestrate.inflation.derivative.CouponInflationZeroCouponMonthly;
import com.opengamma.analytics.financial.interestrate.inflation.derivative.CouponInflationZeroCouponMonthlyGearing;
import com.opengamma.analytics.financial.interestrate.payments.derivative.CouponCMS;
import com.opengamma.analytics.financial.interestrate.payments.derivative.CouponFixed;
import com.opengamma.analytics.financial.interestrate.payments.derivative.CouponFixedCompounding;
import com.opengamma.analytics.financial.interestrate.payments.derivative.CouponIbor;
import com.opengamma.analytics.financial.interestrate.payments.derivative.CouponIborSpread;
import com.opengamma.analytics.financial.interestrate.payments.derivative.CouponOIS;
import com.opengamma.analytics.financial.interestrate.payments.derivative.Payment;
import com.opengamma.analytics.financial.interestrate.payments.derivative.PaymentFixed;
import com.opengamma.analytics.financial.interestrate.swap.derivative.Swap;
import com.opengamma.analytics.financial.interestrate.swap.derivative.SwapFixedCoupon;
import com.opengamma.analytics.financial.interestrate.swaption.derivative.SwaptionCashFixedIbor;

/**
 * Get the last time (in years from now) referenced in the instrument description.
 */
public final class LastTimeCalculator extends InstrumentDerivativeVisitorAdapter<Object, Double> {
  /** Static instance of this class */
  private static final LastTimeCalculator CALCULATOR = new LastTimeCalculator();

  /**
   * @return A static instance of this class
   */
  public static LastTimeCalculator getInstance() {
    return CALCULATOR;
  }

  private LastTimeCalculator() {
  }

  @Override
  public Double visitCash(final Cash cash) {
    return cash.getEndTime();
  }

  @Override
  public Double visitDepositIbor(final DepositIbor deposit) {
    return deposit.getEndTime();
  }

  @Override
  public Double visitDepositCounterpart(final DepositCounterpart deposit) {
    return deposit.getEndTime();
  }

  @Override
  public Double visitFixedCouponSwap(final SwapFixedCoupon<?> swap) {
    return visitSwap(swap);
  }

  @Override
  public Double visitForwardRateAgreement(final ForwardRateAgreement fra) {
    return fra.getFixingPeriodEndTime();
  }

  @Override
  public Double visitInterestRateFutureTransaction(final InterestRateFutureTransaction future) {
    return future.getFixingPeriodEndTime();
  }

  @Override
  public Double visitInterestRateFutureSecurity(final InterestRateFutureSecurity future) {
    return future.getFixingPeriodEndTime();
  }

  @Override
  public Double visitSwapFuturesDeliverableTransaction(final SwapFuturesPriceDeliverableTransaction future) {
    return visitSwap(future.getUnderlying().getUnderlyingSwap());
  }

  @Override
  public Double visitFixedPayment(final PaymentFixed payment) {
    return payment.getPaymentTime();
  }

  @Override
  public Double visitCouponIbor(final CouponIbor payment) {
    return Math.max(payment.getFixingPeriodEndTime(), payment.getPaymentTime());
  }

  @Override
  public Double visitCouponIborSpread(final CouponIborSpread payment) {
    return Math.max(payment.getFixingPeriodEndTime(), payment.getPaymentTime());
  }

  @Override
  public Double visitGenericAnnuity(final Annuity<? extends Payment> annuity) {
    return annuity.getNthPayment(annuity.getNumberOfPayments() - 1).accept(this);
  }

  @Override
  public Double visitSwap(final Swap<?, ?> swap) {
    final double a = swap.getFirstLeg().accept(this);
    final double b = swap.getSecondLeg().accept(this);
    return Math.max(a, b);
  }

  @Override
  public Double visitFixedCouponAnnuity(final AnnuityCouponFixed annuity) {
    return visitGenericAnnuity(annuity);
  }

  @Override
  public Double visitCouponFixed(final CouponFixed payment) {
    return payment.getPaymentTime();
  }

  @Override
  public Double visitCouponFixedCompounding(final CouponFixedCompounding payment) {
    return payment.getPaymentTime();
  }

  @Override
  public Double visitSwaptionCashFixedIbor(final SwaptionCashFixedIbor swaption) {
    return visitSwap(swaption.getUnderlyingSwap());
  }

  @Override
  public Double visitCouponCMS(final CouponCMS payment) {
    final double swapLastTime = payment.getUnderlyingSwap().accept(this);
    final double paymentTime = payment.getPaymentTime();
    return Math.max(swapLastTime, paymentTime);
  }

  @Override
  public Double visitCouponOIS(final CouponOIS payment) {
    return payment.getPaymentTime();
  }

  @Override
  public Double visitDepositZero(final DepositZero deposit) {
    return deposit.getEndTime();
  }

  // -----     Bond     -----

  @Override
  public Double visitBondFixedSecurity(final BondFixedSecurity bond) {
    return Math.max(bond.getCoupon().accept(this), bond.getNominal().accept(this));
  }

  @Override
  public Double visitBondFixedTransaction(final BondFixedTransaction bond) {
    return bond.getBondStandard().accept(this);
  }

  @Override
  public Double visitBondIborSecurity(final BondIborSecurity bond) {
    return Math.max(bond.getCoupon().accept(this), bond.getNominal().accept(this));
  }

  @Override
  public Double visitBondIborTransaction(final BondIborTransaction bond) {
    return bond.getBondStandard().accept(this);
  }

  // -----     Bond     -----

  @Override
  public Double visitBillTransaction(final BillTransaction bill) {
    return bill.getBillStandard().getEndTime();
  }

  // -----     Forex     -----

  @Override
  public Double visitForexSwap(final ForexSwap fx) {
    return fx.getFarLeg().getPaymentTime();
  }

  @Override
  public Double visitForex(final Forex fx) {
    return fx.getPaymentTime();
  }

  @Override
  public Double visitAgricultureFutureOption(final AgricultureFutureOption option) {
    return option.getExpiry();
  }

  @Override
  public Double visitEnergyFutureOption(final EnergyFutureOption option) {
    return option.getExpiry();
  }

  @Override
  public Double visitMetalFutureOption(final MetalFutureOption option) {
    return option.getExpiry();
  }

  @Override
  public Double visitBondFutureOptionPremiumSecurity(final BondFutureOptionPremiumSecurity option) {
    return option.getExpirationTime();
  }

  @Override
  public Double visitBondFutureOptionPremiumTransaction(final BondFutureOptionPremiumTransaction option) {
    return option.getUnderlyingOption().getExpirationTime();
  }

  @Override
  public Double visitInterestRateFutureOptionMarginSecurity(final InterestRateFutureOptionMarginSecurity option) {
    return option.getExpirationTime();
  }

  @Override
  public Double visitInterestRateFutureOptionMarginTransaction(final InterestRateFutureOptionMarginTransaction option) {
    return option.getUnderlyingOption().getExpirationTime();
  }

  @Override
  public Double visitInterestRateFutureOptionPremiumSecurity(final InterestRateFutureOptionPremiumSecurity option) {
    return option.getExpirationTime();
  }

  @Override
  public Double visitInterestRateFutureOptionPremiumTransaction(final InterestRateFutureOptionPremiumTransaction option) {
    return option.getUnderlyingOption().getExpirationTime();
  }

  @Override
  public Double visitEquityIndexOption(final EquityIndexOption option) {
    return option.getTimeToExpiry();
  }

  @Override
  public Double visitEquityOption(final EquityOption option) {
    return option.getTimeToExpiry();
  }

  @Override
  public Double visitEquityIndexFutureOption(final EquityIndexFutureOption option) {
    return option.getExpiry();
  }

  // -----     Inflation     -----

  @Override
  public Double visitCouponInflationZeroCouponMonthly(final CouponInflationZeroCouponMonthly coupon) {
    return coupon.getPaymentTime();
  }

  @Override
  public Double visitCouponInflationZeroCouponMonthlyGearing(final CouponInflationZeroCouponMonthlyGearing coupon) {
    return coupon.getPaymentTime();
  }

  @Override
  public Double visitCouponInflationZeroCouponInterpolation(final CouponInflationZeroCouponInterpolation coupon) {
    return coupon.getPaymentTime();
  }

  @Override
  public Double visitCouponInflationZeroCouponInterpolationGearing(final CouponInflationZeroCouponInterpolationGearing coupon) {
    return coupon.getPaymentTime();
  }

  @Override
  public Double visitCouponInflationYearOnYearMonthly(final CouponInflationYearOnYearMonthly coupon) {
    return coupon.getPaymentTime();
  }

  @Override
  public Double visitCouponInflationYearOnYearInterpolation(final CouponInflationYearOnYearInterpolation coupon) {
    return coupon.getPaymentTime();
  }

}
