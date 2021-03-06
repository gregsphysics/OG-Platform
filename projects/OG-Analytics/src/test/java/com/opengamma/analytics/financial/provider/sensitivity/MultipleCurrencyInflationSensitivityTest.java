/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.provider.sensitivity;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.opengamma.analytics.financial.forex.method.FXMatrix;
import com.opengamma.analytics.financial.interestrate.InterestRateCurveSensitivityUtils;
import com.opengamma.analytics.financial.provider.sensitivity.inflation.InflationSensitivity;
import com.opengamma.analytics.financial.provider.sensitivity.inflation.MultipleCurrencyInflationSensitivity;
import com.opengamma.analytics.financial.provider.sensitivity.multicurve.ForwardSensitivity;
import com.opengamma.analytics.financial.util.AssertSensivityObjects;
import com.opengamma.util.money.Currency;
import com.opengamma.util.tuple.DoublesPair;

/**
 * Tests the MultipleCurrencyCurveSensitivityMarket class.
 */
public class MultipleCurrencyInflationSensitivityTest {

  private static final List<DoublesPair> SENSI_DATA_1 = Arrays.asList(new DoublesPair[] {new DoublesPair(1, 10), new DoublesPair(2, 20), new DoublesPair(3, 30), new DoublesPair(4, 40)});
  private static final List<DoublesPair> SENSI_DATA_2 = Arrays.asList(new DoublesPair[] {new DoublesPair(1, 40), new DoublesPair(2, 30), new DoublesPair(3, 20), new DoublesPair(4, 10)});
  private static final List<DoublesPair> SENSI_DATA_3 = Arrays.asList(new DoublesPair[] {new DoublesPair(11, 40), new DoublesPair(12, 30), new DoublesPair(13, 20), new DoublesPair(14, 10)});
  private static final List<ForwardSensitivity> SENSI_FWD_1 = new ArrayList<ForwardSensitivity>();
  static {
    SENSI_FWD_1.add(new ForwardSensitivity(0.5, 0.75, 0.26, 11));
    SENSI_FWD_1.add(new ForwardSensitivity(0.75, 1.00, 0.26, 12));
    SENSI_FWD_1.add(new ForwardSensitivity(1.00, 1.25, 0.24, 13));
  }
  private static final String CURVE_NAME_1 = "A";
  private static final String CURVE_NAME_2 = "B";
  private static final String CURVE_NAME_3 = "C";

  private static final Map<String, List<DoublesPair>> SENSI_11 = new HashMap<String, List<DoublesPair>>();
  private static final Map<String, List<DoublesPair>> SENSI_12 = new HashMap<String, List<DoublesPair>>();
  private static final Map<String, List<DoublesPair>> SENSI_22 = new HashMap<String, List<DoublesPair>>();
  private static final Map<String, List<DoublesPair>> SENSI_33 = new HashMap<String, List<DoublesPair>>();
  private static final Map<String, List<ForwardSensitivity>> SENSI_FWD_11 = new HashMap<String, List<ForwardSensitivity>>();
  static {
    SENSI_11.put(CURVE_NAME_1, SENSI_DATA_1);
    SENSI_22.put(CURVE_NAME_2, SENSI_DATA_2);
    SENSI_12.put(CURVE_NAME_1, SENSI_DATA_2);
    SENSI_33.put(CURVE_NAME_3, SENSI_DATA_3);
    SENSI_FWD_11.put(CURVE_NAME_2, SENSI_FWD_1);
  }

  private static final double TOLERANCE = 1.0E-10;

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void nullCcy() {
    MultipleCurrencyInflationSensitivity.of(null, new InflationSensitivity());
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void nullSensi() {
    MultipleCurrencyInflationSensitivity.of(Currency.AUD, null);
  }

  @Test
  public void of() {
    InflationSensitivity cs = InflationSensitivity.of(SENSI_11, SENSI_FWD_11, SENSI_33);
    Currency ccy1 = Currency.AUD;
    MultipleCurrencyInflationSensitivity mcs = MultipleCurrencyInflationSensitivity.of(ccy1, cs);
    assertEquals("MultipleCurrencyCurveSensitivityMarket: of", cs, mcs.getSensitivity(ccy1));
    MultipleCurrencyInflationSensitivity constructor = new MultipleCurrencyInflationSensitivity();
    constructor = constructor.plus(ccy1, cs);
    AssertSensivityObjects.assertEquals("MultipleCurrencyCurveSensitivityMarket: of", mcs.cleaned(), constructor.cleaned(), TOLERANCE);
    AssertSensivityObjects.assertEquals("MultipleCurrencyCurveSensitivityMarket: getSensitivity", new InflationSensitivity(), mcs.getSensitivity(Currency.CAD), TOLERANCE);
  }

  @Test
  public void plusMultipliedBy() {
    Currency ccy1 = Currency.AUD;
    Currency ccy2 = Currency.CAD;
    InflationSensitivity cs = InflationSensitivity.of(SENSI_11, SENSI_FWD_11, SENSI_33);
    MultipleCurrencyInflationSensitivity mcs = MultipleCurrencyInflationSensitivity.of(ccy1, cs);
    InflationSensitivity cs2 = InflationSensitivity.ofYieldDiscounting(SENSI_22);
    MultipleCurrencyInflationSensitivity mcs2 = MultipleCurrencyInflationSensitivity.of(ccy1, cs2);
    MultipleCurrencyInflationSensitivity mcs3 = mcs.plus(mcs2);
    Map<String, List<DoublesPair>> sum = InterestRateCurveSensitivityUtils.addSensitivity(SENSI_11, SENSI_22);
    MultipleCurrencyInflationSensitivity mcs3Expected = MultipleCurrencyInflationSensitivity.of(ccy1, InflationSensitivity.of(sum, SENSI_FWD_11, SENSI_33));
    AssertSensivityObjects.assertEquals("", mcs3Expected.cleaned(), mcs3.cleaned(), TOLERANCE);
    mcs = mcs.plus(ccy2, cs);
    assertEquals("MultipleCurrencyCurveSensitivityMarket: plusMultipliedBy", cs, mcs.getSensitivity(ccy1));
    assertEquals("MultipleCurrencyCurveSensitivityMarket: plusMultipliedBy", cs, mcs.getSensitivity(ccy2));
    AssertSensivityObjects.assertEquals("", mcs.plus(mcs).cleaned(), mcs.multipliedBy(2.0).cleaned(), TOLERANCE);
  }

  @Test
  public void cleaned() {
    Currency ccy1 = Currency.AUD;
    Currency ccy2 = Currency.CAD;
    InflationSensitivity cs1 = InflationSensitivity.of(SENSI_11, SENSI_FWD_11, SENSI_33);
    InflationSensitivity cs2 = InflationSensitivity.of(SENSI_22, SENSI_FWD_11, SENSI_33);
    MultipleCurrencyInflationSensitivity mcs1 = MultipleCurrencyInflationSensitivity.of(ccy1, cs1);
    mcs1 = mcs1.plus(ccy2, cs2);
    MultipleCurrencyInflationSensitivity mcs2 = MultipleCurrencyInflationSensitivity.of(ccy2, cs2);
    mcs2 = mcs2.plus(ccy1, cs1);
    AssertSensivityObjects.assertEquals("MultipleCurrencyCurveSensitivityMarket: cleaned", mcs1.cleaned(), mcs2.cleaned(), TOLERANCE);
  }

  @Test
  public void converted() {
    Currency ccy1 = Currency.EUR;
    Currency ccy2 = Currency.USD;
    final FXMatrix fxMatrix = new FXMatrix(ccy1, ccy2, 1.25);
    InflationSensitivity cs = InflationSensitivity.of(SENSI_11, SENSI_FWD_11, SENSI_33);
    MultipleCurrencyInflationSensitivity mcs = MultipleCurrencyInflationSensitivity.of(ccy1, cs);
    MultipleCurrencyInflationSensitivity mcsConverted = mcs.converted(ccy2, fxMatrix);
    MultipleCurrencyInflationSensitivity mcsExpected = MultipleCurrencyInflationSensitivity.of(ccy2, cs.multipliedBy(fxMatrix.getFxRate(ccy1, ccy2)));
    AssertSensivityObjects.assertEquals("MultipleCurrencyCurveSensitivityMarket: converted", mcsExpected.cleaned(), mcsConverted.cleaned(), TOLERANCE);
  }

  @Test
  public void equalHash() {
    Currency ccy1 = Currency.EUR;
    Currency ccy2 = Currency.USD;
    InflationSensitivity cs = InflationSensitivity.of(SENSI_11, SENSI_FWD_11, SENSI_33);
    MultipleCurrencyInflationSensitivity mcs = MultipleCurrencyInflationSensitivity.of(ccy1, cs);
    assertEquals("MultipleCurrencyCurveSensitivityMarket: equalHash", mcs, mcs);
    assertEquals("MultipleCurrencyCurveSensitivityMarket: equalHash", mcs.hashCode(), mcs.hashCode());
    assertFalse("MultipleCurrencyCurveSensitivityMarket: equalHash", mcs.equals(null));
    assertFalse("MultipleCurrencyCurveSensitivityMarket: equalHash", mcs.equals(SENSI_11));
    MultipleCurrencyInflationSensitivity other = MultipleCurrencyInflationSensitivity.of(ccy1, cs);
    assertEquals("MultipleCurrencyCurveSensitivityMarket: equalHash", mcs, other);
    MultipleCurrencyInflationSensitivity modified;
    modified = MultipleCurrencyInflationSensitivity.of(ccy2, cs);
    assertFalse("MultipleCurrencyCurveSensitivityMarket: equalHash", mcs.equals(modified));
  }

}
