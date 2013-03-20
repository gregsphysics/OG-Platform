/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.conversion;

import org.threeten.bp.ZonedDateTime;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.analytics.financial.credit.BuySellProtection;
import com.opengamma.analytics.financial.credit.DebtSeniority;
import com.opengamma.analytics.financial.credit.RestructuringClause;
import com.opengamma.analytics.financial.credit.StubType;
import com.opengamma.analytics.financial.credit.creditdefaultswap.StandardCDSCoupon;
import com.opengamma.analytics.financial.credit.creditdefaultswap.definition.legacy.LegacyVanillaCreditDefaultSwapDefinition;
import com.opengamma.analytics.financial.credit.obligor.CreditRating;
import com.opengamma.analytics.financial.credit.obligor.CreditRatingFitch;
import com.opengamma.analytics.financial.credit.obligor.CreditRatingMoodys;
import com.opengamma.analytics.financial.credit.obligor.CreditRatingStandardAndPoors;
import com.opengamma.analytics.financial.credit.obligor.Region;
import com.opengamma.analytics.financial.credit.obligor.Sector;
import com.opengamma.analytics.financial.credit.obligor.definition.Obligor;
import com.opengamma.core.holiday.HolidaySource;
import com.opengamma.core.organization.Organization;
import com.opengamma.core.organization.OrganizationSource;
import com.opengamma.core.region.RegionSource;
import com.opengamma.financial.convention.HolidaySourceCalendarAdapter;
import com.opengamma.financial.convention.businessday.BusinessDayConvention;
import com.opengamma.financial.convention.businessday.BusinessDayConventionFactory;
import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.financial.convention.daycount.DayCount;
import com.opengamma.financial.convention.frequency.Frequency;
import com.opengamma.financial.convention.frequency.PeriodFrequency;
import com.opengamma.financial.convention.frequency.SimpleFrequency;
import com.opengamma.financial.security.cds.LegacyVanillaCDSSecurity;
import com.opengamma.financial.security.cds.StandardVanillaCDSSecurity;
import com.opengamma.financial.security.swap.InterestRateNotional;
import com.opengamma.id.ExternalId;
import com.opengamma.id.UniqueId;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.money.Currency;

/**
 *
 */
public class CreditDefaultSwapSecurityConverter {
  private static final Obligor DUMMY_OBLIGOR_A = new Obligor(
      "Dummy_A",
      "Dummy_A",
      "Dummy_A",
      CreditRating.A,
      CreditRating.A,
      CreditRatingMoodys.A,
      CreditRatingStandardAndPoors.A,
      CreditRatingFitch.A,
      false,
      Sector.BASICMATERIALS,
      Region.EUROPE,
      "CA");
  private static final Obligor DUMMY_OBLIGOR_B = new Obligor(
      "Dummy_B",
      "Dummy_B",
      "Dummy_B",
      CreditRating.A,
      CreditRating.A,
      CreditRatingMoodys.A,
      CreditRatingStandardAndPoors.A,
      CreditRatingFitch.A,
      false,
      Sector.BASICMATERIALS,
      Region.ASIA,
      "NY");
  private static final Obligor DUMMY_OBLIGOR_C = new Obligor(
      "Dummy_C",
      "Dummy_C",
      "Dummy_C",
      CreditRating.A,
      CreditRating.A,
      CreditRatingMoodys.A,
      CreditRatingStandardAndPoors.A,
      CreditRatingFitch.A,
      false,
      Sector.BASICMATERIALS,
      Region.NORTHAMERICA,
      "NJ");

  private static final BusinessDayConvention FOLLOWING = BusinessDayConventionFactory.INSTANCE.getBusinessDayConvention("Following");
  private final HolidaySource _holidaySource;
  private final RegionSource _regionSource;
  private final OrganizationSource _organizationSource;

  public CreditDefaultSwapSecurityConverter(final HolidaySource holidaySource, final RegionSource regionSource, final OrganizationSource organizationSource) {
    ArgumentChecker.notNull(holidaySource, "holiday source");
    ArgumentChecker.notNull(regionSource, "region source");
    //ArgumentChecker.notNull(organizationSource, "organization source");
    _holidaySource = holidaySource;
    _regionSource = regionSource;
    _organizationSource = organizationSource;
  }

  public LegacyVanillaCreditDefaultSwapDefinition visitStandardVanillaCDSSecurity(final StandardVanillaCDSSecurity security, final ZonedDateTime valuationDate) {
    ArgumentChecker.notNull(security, "security");
    final BuySellProtection buySellProtection = security.isBuy() ? BuySellProtection.BUY : BuySellProtection.SELL;
    //final ExternalId regionId = security.getRegionId();
    //final Calendar calendar = new HolidaySourceCalendarAdapter(_holidaySource, _regionSource.getHighestLevelRegion(regionId));
    final Calendar calendar = new HolidaySourceCalendarAdapter(_holidaySource, security.getNotional().getCurrency());
    final ZonedDateTime startDate = security.getStartDate();
    final ZonedDateTime effectiveDate = FOLLOWING.adjustDate(calendar, valuationDate.withHour(0).withMinute(0).withSecond(0).withNano(0).plusDays(1));
    final ZonedDateTime maturityDate = security.getMaturityDate();
    final PeriodFrequency couponFrequency = getPeriodFrequency(security.getCouponFrequency());
    final DayCount dayCount = security.getDayCount();
    final BusinessDayConvention businessDayConvention = security.getBusinessDayConvention();
    final boolean immAdjustMaturityDate = security.isImmAdjustMaturityDate();
    final boolean adjustEffectiveDate = security.isAdjustEffectiveDate();
    final boolean adjustMaturityDate = security.isAdjustMaturityDate();
    final InterestRateNotional notional = security.getNotional();
    final Currency currency = notional.getCurrency();
    final DebtSeniority debtSeniority = security.getDebtSeniority();
    final RestructuringClause restructuringClause = security.getRestructuringClause();
    final double amount = notional.getAmount();
    final double recoveryRate = security.getRecoveryRate();
    final boolean includeAccruedPremium = security.isIncludeAccruedPremium();
    final boolean protectionStart = security.isProtectionStart();
    final double quotedSpread = security.getQuotedSpread();
    final StandardCDSCoupon premiumLegCoupon = getCoupon(security.getCoupon());
    final double upFrontAmount = security.getUpfrontAmount().getAmount();
    final StubType stubType = security.getStubType().toAnalyticsType();
    final ZonedDateTime cashSettlementDate = security.getCashSettlementDate();
    final boolean adjustCashSettlementDate = security.isAdjustCashSettlementDate();
    final double coupon = security.getCoupon();
    if (_organizationSource != null) {
      final com.opengamma.analytics.financial.credit.obligor.definition.Obligor protectionBuyer = getObligor(security.getProtectionBuyer());
      final com.opengamma.analytics.financial.credit.obligor.definition.Obligor protectionSeller = getObligor(security.getProtectionSeller());
      final com.opengamma.analytics.financial.credit.obligor.definition.Obligor referenceEntity = getObligor(security.getReferenceEntity());
      return new LegacyVanillaCreditDefaultSwapDefinition(buySellProtection, protectionBuyer, protectionSeller, referenceEntity, currency,
          debtSeniority, restructuringClause, calendar, startDate, effectiveDate, maturityDate, stubType,
          couponFrequency, dayCount, businessDayConvention, immAdjustMaturityDate, adjustEffectiveDate, adjustMaturityDate,
          amount, recoveryRate, includeAccruedPremium, protectionStart, coupon);
    }
    return new LegacyVanillaCreditDefaultSwapDefinition(buySellProtection, DUMMY_OBLIGOR_A, DUMMY_OBLIGOR_B, DUMMY_OBLIGOR_C, currency,
        debtSeniority, restructuringClause, calendar, startDate, effectiveDate, maturityDate, stubType,
        couponFrequency, dayCount, businessDayConvention, immAdjustMaturityDate, adjustEffectiveDate, adjustMaturityDate,
        amount, recoveryRate, includeAccruedPremium, protectionStart, coupon);
  }

  public LegacyVanillaCreditDefaultSwapDefinition visitLegacyVanillaCDSSecurity(final LegacyVanillaCDSSecurity security, final ZonedDateTime valuationDate) {
    ArgumentChecker.notNull(security, "security");
    final BuySellProtection buySellProtection = security.isBuy() ? BuySellProtection.BUY : BuySellProtection.SELL;
    //    final ExternalId regionId = security.getRegionId();
    //    final Calendar calendar = new HolidaySourceCalendarAdapter(_holidaySource, _regionSource.getHighestLevelRegion(regionId));
    final Calendar calendar = new HolidaySourceCalendarAdapter(_holidaySource, security.getNotional().getCurrency());
    final ZonedDateTime startDate = security.getStartDate();
    final ZonedDateTime effectiveDate = FOLLOWING.adjustDate(calendar, valuationDate.withHour(0).withMinute(0).withSecond(0).withNano(0).plusDays(1));
    final ZonedDateTime maturityDate = security.getMaturityDate();
    final PeriodFrequency couponFrequency = getPeriodFrequency(security.getCouponFrequency());
    final DayCount dayCount = security.getDayCount();
    final BusinessDayConvention businessDayConvention = security.getBusinessDayConvention();
    final boolean immAdjustMaturityDate = security.isImmAdjustMaturityDate();
    final boolean adjustEffectiveDate = security.isAdjustEffectiveDate();
    final boolean adjustMaturityDate = security.isAdjustMaturityDate();
    final InterestRateNotional notional = security.getNotional();
    final Currency currency = notional.getCurrency();
    final DebtSeniority debtSeniority = security.getDebtSeniority();
    final RestructuringClause restructuringClause = security.getRestructuringClause();
    final double amount = notional.getAmount();
    final double recoveryRate = security.getRecoveryRate();
    final boolean includeAccruedPremium = security.isIncludeAccruedPremium();
    final boolean protectionStart = security.isProtectionStart();
    final StubType stubType = security.getStubType().toAnalyticsType();
    final double parSpread = security.getParSpread();
    if (_organizationSource != null) {
      final com.opengamma.analytics.financial.credit.obligor.definition.Obligor protectionBuyer = getObligor(security.getProtectionBuyer());
      final com.opengamma.analytics.financial.credit.obligor.definition.Obligor protectionSeller = getObligor(security.getProtectionSeller());
      final com.opengamma.analytics.financial.credit.obligor.definition.Obligor referenceEntity = getObligor(security.getReferenceEntity());
      return new LegacyVanillaCreditDefaultSwapDefinition(buySellProtection, protectionBuyer, protectionSeller, referenceEntity, currency,
          debtSeniority, restructuringClause, calendar, startDate, effectiveDate, maturityDate, stubType,
          couponFrequency, dayCount, businessDayConvention, immAdjustMaturityDate, adjustEffectiveDate, adjustMaturityDate,
          amount, recoveryRate, includeAccruedPremium, protectionStart, parSpread);
    }
    return new LegacyVanillaCreditDefaultSwapDefinition(buySellProtection, DUMMY_OBLIGOR_A, DUMMY_OBLIGOR_B, DUMMY_OBLIGOR_C, currency,
        debtSeniority, restructuringClause, calendar, startDate, effectiveDate, maturityDate, stubType,
        couponFrequency, dayCount, businessDayConvention, immAdjustMaturityDate, adjustEffectiveDate, adjustMaturityDate,
        amount, recoveryRate, includeAccruedPremium, protectionStart, parSpread);
  }

  private PeriodFrequency getPeriodFrequency(final Frequency frequency) {
    if (frequency instanceof PeriodFrequency) {
      return (PeriodFrequency) frequency;
    }
    if (frequency instanceof SimpleFrequency) {
      return ((SimpleFrequency) frequency).toPeriodFrequency();
    }
    throw new OpenGammaRuntimeException("Can only handle PeriodFrequency and SimpleFrequency");
  }

  private StandardCDSCoupon getCoupon(final double coupon) {
    if (Double.compare(coupon, 25) == 0) {
      return StandardCDSCoupon._25bps;
    }
    if (Double.compare(coupon, 100) == 0) {
      return StandardCDSCoupon._100bps;
    }
    if (Double.compare(coupon, 500) == 0) {
      return StandardCDSCoupon._500bps;
    }
    if (Double.compare(coupon, 750) == 0) {
      return StandardCDSCoupon._750bps;
    }
    if (Double.compare(coupon, 1000) == 0) {
      return StandardCDSCoupon._1000bps;
    }
    throw new OpenGammaRuntimeException("Could not identify coupon with value " + coupon);
  }

  private com.opengamma.analytics.financial.credit.obligor.definition.Obligor getObligor(final ExternalId obligorId) {
    final Organization organization = _organizationSource.get(UniqueId.of(obligorId.getScheme().getName(), obligorId.getValue()));
    final com.opengamma.core.obligor.definition.Obligor obligorDb = organization.getObligor();
    final com.opengamma.analytics.financial.credit.obligor.definition.Obligor obligor = new com.opengamma.analytics.financial.credit.obligor.definition.Obligor(
        obligorDb.getObligorTicker(),
        obligorDb.getObligorShortName(),
        obligorDb.getObligorREDCode(),
        getCreditRating(obligorDb.getCompositeRating()),
        getCreditRating(obligorDb.getImpliedRating()),
        getCreditRating(obligorDb.getMoodysCreditRating()),
        getCreditRating(obligorDb.getStandardAndPoorsCreditRating()),
        getCreditRating(obligorDb.getFitchCreditRating()),
        obligorDb.hasDefaulted().get(),
        getSector(obligorDb.getSector()),
        getRegion(obligorDb.getRegion()),
        obligorDb.getCountry());
    return obligor;
  }

  private com.opengamma.analytics.financial.credit.obligor.CreditRating getCreditRating(final com.opengamma.core.obligor.CreditRating ratingDb) {
    return com.opengamma.analytics.financial.credit.obligor.CreditRating.valueOf(ratingDb.name());
  }

  private com.opengamma.analytics.financial.credit.obligor.CreditRatingMoodys getCreditRating(final com.opengamma.core.obligor.CreditRatingMoodys ratingDb) {
    return com.opengamma.analytics.financial.credit.obligor.CreditRatingMoodys.valueOf(ratingDb.name());
  }

  private com.opengamma.analytics.financial.credit.obligor.CreditRatingStandardAndPoors getCreditRating(final com.opengamma.core.obligor.CreditRatingStandardAndPoors ratingDb) {
    return com.opengamma.analytics.financial.credit.obligor.CreditRatingStandardAndPoors.valueOf(ratingDb.name());
  }

  private com.opengamma.analytics.financial.credit.obligor.CreditRatingFitch getCreditRating(final com.opengamma.core.obligor.CreditRatingFitch ratingDb) {
    return com.opengamma.analytics.financial.credit.obligor.CreditRatingFitch.valueOf(ratingDb.name());
  }

  private com.opengamma.analytics.financial.credit.obligor.Region getRegion(final com.opengamma.core.obligor.Region regionDb) {
    return com.opengamma.analytics.financial.credit.obligor.Region.valueOf(regionDb.name());
  }

  private com.opengamma.analytics.financial.credit.obligor.Sector getSector(final com.opengamma.core.obligor.Sector sectorDb) {
    return com.opengamma.analytics.financial.credit.obligor.Sector.valueOf(sectorDb.name());
  }
}
