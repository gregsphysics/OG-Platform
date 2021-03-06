/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.analytics.util.amount;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

import org.testng.annotations.Test;

import com.opengamma.util.money.Currency;
import com.opengamma.util.tuple.ObjectsPair;
import com.opengamma.util.tuple.Pair;

public class ReferenceAmountTest {

  private static final double TOLERANCE = 1.0E-10;

  private static final String STR1 = "Name 1";
  private static final String STR2 = "Name 2";
  private static final Currency USD = Currency.USD;
  private static final Currency EUR = Currency.EUR;
  private static final Pair<String, Currency> STR1_USD = new ObjectsPair<String, Currency>(STR1, USD);
  private static final Pair<String, Currency> STR1_EUR = new ObjectsPair<String, Currency>(STR1, EUR);
  private static final Pair<String, Currency> STR2_USD = new ObjectsPair<String, Currency>(STR2, USD);
  private static final Pair<String, Currency> STR2_EUR = new ObjectsPair<String, Currency>(STR2, EUR);

  @Test
  public void constructor() {
    ReferenceAmount<Pair<String, Currency>> surf0 = new ReferenceAmount<Pair<String, Currency>>();
    assertEquals("ReferenceAmount - constructor", 0, surf0.getMap().size());
  }

  @Test
  public void plusAdd() {
    double value1 = 2345.678;
    ReferenceAmount<Pair<String, Currency>> surf1 = new ReferenceAmount<Pair<String, Currency>>();
    surf1.add(STR1_USD, value1);
    assertEquals("ReferenceAmount - add", 1, surf1.getMap().size());
    double value2 = 10 * Math.E;
    ReferenceAmount<Pair<String, Currency>> surf2 = new ReferenceAmount<Pair<String, Currency>>();
    surf2.add(STR2_EUR, value2);
    ReferenceAmount<Pair<String, Currency>> surf3 = surf1.plus(surf2);
    assertEquals("ReferenceAmount - plus", 2, surf3.getMap().size());
    assertTrue("ReferenceAmount - plus", surf3.getMap().containsKey(STR1_USD));
    assertTrue("ReferenceAmount - plus", surf3.getMap().containsKey(STR2_EUR));
    assertEquals("ReferenceAmount - plus", value1, surf3.getMap().get(STR1_USD), TOLERANCE);
    assertEquals("ReferenceAmount - plus", value2, surf3.getMap().get(STR2_EUR), TOLERANCE);
    ReferenceAmount<Pair<String, Currency>> surf4 = surf2.plus(surf1);
    assertEquals("ReferenceAmount - plus", surf3, surf4);
    ReferenceAmount<Pair<String, Currency>> surf5 = new ReferenceAmount<Pair<String, Currency>>();
    double value3 = 10.01;
    surf5.add(STR2_EUR, value3);
    assertEquals("ReferenceAmount - plus", value2 + value3, surf3.plus(surf5).getMap().get(STR2_EUR), TOLERANCE);
  }

  @Test
  public void multipliedBy() {
    double value1 = 2345.678;
    ReferenceAmount<Pair<String, Currency>> surf1 = new ReferenceAmount<Pair<String, Currency>>();
    surf1.add(STR1_EUR, value1);
    surf1.add(STR2_USD, value1);
    double factor = 3;
    ReferenceAmount<Pair<String, Currency>> surf2 = surf1.multiplyBy(factor);
    ReferenceAmount<Pair<String, Currency>> surf3 = surf1.plus(surf1).plus(surf1);
    assertEquals("ReferenceAmount - multipliedBy", surf2, surf3);
  }

}
