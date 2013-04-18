package com.opengamma.analytics.financial.instrument.payment;

import static org.testng.AssertJUnit.assertEquals;

import org.testng.annotations.Test;
import org.threeten.bp.Period;
import org.threeten.bp.ZonedDateTime;

import com.opengamma.analytics.financial.instrument.index.IborIndex;
import com.opengamma.analytics.financial.instrument.index.IndexIborMaster;
import com.opengamma.analytics.financial.instrument.index.IndexON;
import com.opengamma.analytics.financial.instrument.index.IndexONMaster;
import com.opengamma.analytics.financial.schedule.ScheduleCalculator;
import com.opengamma.financial.convention.businessday.BusinessDayConvention;
import com.opengamma.financial.convention.businessday.BusinessDayConventionFactory;
import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.financial.convention.calendar.MondayToFridayCalendar;
import com.opengamma.util.money.Currency;
import com.opengamma.util.time.DateUtils;

public class CouponArithmeticAverageONSpreadDefinitionTest {

  private static final BusinessDayConvention MOD_FOL = BusinessDayConventionFactory.INSTANCE.getBusinessDayConvention("Modified Following");
  private static final Calendar NYC = new MondayToFridayCalendar("NYC");
  private static final IndexON FEDFUND = IndexONMaster.getInstance().getIndex("FED FUND", NYC);
  private static final IborIndex USDLIBOR3M = IndexIborMaster.getInstance().getIndex("USDLIBOR3M", NYC);

  private static final ZonedDateTime EFFECTIVE_DATE = DateUtils.getUTCDate(2011, 5, 23);
  private static final Period TENOR_3M = Period.ofMonths(3);
  private static final double NOTIONAL = 100000000; // 100m
  private static final double SPREAD = 0.0010; // 10 bps
  private static final ZonedDateTime ACCRUAL_END_DATE = ScheduleCalculator.getAdjustedDate(EFFECTIVE_DATE, TENOR_3M, USDLIBOR3M);
  private static final double ACCURAL_FACTOR = USDLIBOR3M.getDayCount().getDayCountFraction(EFFECTIVE_DATE, ACCRUAL_END_DATE);
  private static final CouponArithmeticAverageONSpreadDefinition FEDFUND_CPN_3M_DEF = new CouponArithmeticAverageONSpreadDefinition(Currency.USD, ACCRUAL_END_DATE, EFFECTIVE_DATE, ACCRUAL_END_DATE,
      ACCURAL_FACTOR, NOTIONAL, FEDFUND, EFFECTIVE_DATE, ACCRUAL_END_DATE, SPREAD);

  private static final CouponArithmeticAverageONSpreadDefinition FEDFUND_CPN_3M_FROM_DEF = CouponArithmeticAverageONSpreadDefinition.from(FEDFUND, EFFECTIVE_DATE, TENOR_3M, NOTIONAL, 0,
      MOD_FOL, true, SPREAD);

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void nullCurrency() {
    new CouponArithmeticAverageONSpreadDefinition(null, EFFECTIVE_DATE, EFFECTIVE_DATE, ACCRUAL_END_DATE, ACCURAL_FACTOR,
        NOTIONAL, FEDFUND, EFFECTIVE_DATE, ACCRUAL_END_DATE, SPREAD);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void nullEffective() {
    new CouponArithmeticAverageONSpreadDefinition(Currency.USD, null, EFFECTIVE_DATE, ACCRUAL_END_DATE, ACCURAL_FACTOR,
        NOTIONAL, FEDFUND, EFFECTIVE_DATE, ACCRUAL_END_DATE, SPREAD);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void nullStartAccural() {
    new CouponArithmeticAverageONSpreadDefinition(Currency.USD, EFFECTIVE_DATE, null, ACCRUAL_END_DATE, ACCURAL_FACTOR,
        NOTIONAL, FEDFUND, EFFECTIVE_DATE, ACCRUAL_END_DATE, SPREAD);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void nullEndAccural() {
    new CouponArithmeticAverageONSpreadDefinition(Currency.USD, EFFECTIVE_DATE, EFFECTIVE_DATE, null, ACCURAL_FACTOR,
        NOTIONAL, FEDFUND, EFFECTIVE_DATE, ACCRUAL_END_DATE, SPREAD);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void nullIndex() {
    new CouponArithmeticAverageONSpreadDefinition(Currency.USD, EFFECTIVE_DATE, EFFECTIVE_DATE, ACCRUAL_END_DATE, ACCURAL_FACTOR,
        NOTIONAL, null, EFFECTIVE_DATE, ACCRUAL_END_DATE, SPREAD);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void nullStartFixing() {
    new CouponArithmeticAverageONSpreadDefinition(Currency.USD, EFFECTIVE_DATE, EFFECTIVE_DATE, ACCRUAL_END_DATE, ACCURAL_FACTOR,
        NOTIONAL, FEDFUND, null, ACCRUAL_END_DATE, SPREAD);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void nullEndFixing() {
    new CouponArithmeticAverageONSpreadDefinition(Currency.USD, EFFECTIVE_DATE, EFFECTIVE_DATE, ACCRUAL_END_DATE, ACCURAL_FACTOR,
        NOTIONAL, FEDFUND, EFFECTIVE_DATE, null, SPREAD);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void wrongCurrency() {
    new CouponArithmeticAverageONSpreadDefinition(Currency.AUD, EFFECTIVE_DATE, EFFECTIVE_DATE, ACCRUAL_END_DATE, ACCURAL_FACTOR,
        NOTIONAL, FEDFUND, EFFECTIVE_DATE, ACCRUAL_END_DATE, SPREAD);
  }

  @Test
  public void getter() {
    assertEquals("CouponArithmeticAverageONSpreadDefinition: getter", FEDFUND_CPN_3M_DEF.getPaymentDate(), ACCRUAL_END_DATE);
    assertEquals("CouponArithmeticAverageONSpreadDefinition: getter", FEDFUND_CPN_3M_DEF.getAccrualStartDate(), EFFECTIVE_DATE);
    assertEquals("CouponArithmeticAverageONSpreadDefinition: getter", FEDFUND_CPN_3M_DEF.getAccrualEndDate(), ACCRUAL_END_DATE);
    assertEquals("CouponArithmeticAverageONSpreadDefinition: getter", FEDFUND_CPN_3M_DEF.getFixingPeriodDate()[0], EFFECTIVE_DATE);
    assertEquals("CouponArithmeticAverageONSpreadDefinition: getter", FEDFUND_CPN_3M_DEF.getFixingPeriodDate()[FEDFUND_CPN_3M_DEF.getFixingPeriodDate().length - 1], ACCRUAL_END_DATE);
    assertEquals("CouponArithmeticAverageONSpreadDefinition: getter", FEDFUND_CPN_3M_DEF.getCurrency(), FEDFUND.getCurrency());
    assertEquals("CouponArithmeticAverageONSpreadDefinition: getter", FEDFUND_CPN_3M_DEF.getIndex(), FEDFUND);
    assertEquals("CouponArithmeticAverageONSpreadDefinition: getter", FEDFUND_CPN_3M_DEF.getSpread(), SPREAD);
    assertEquals("CouponArithmeticAverageONSpreadDefinition: getter", FEDFUND_CPN_3M_DEF.getSpreadAmount(), SPREAD * ACCURAL_FACTOR * NOTIONAL);
  }

  @Test
  public void from() {
    assertEquals("CouponArithmeticAverageONSpreadDefinition: from", FEDFUND_CPN_3M_DEF, FEDFUND_CPN_3M_FROM_DEF);
  }

  //TODO: toDerivatives

}