/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.ircurve;

import java.util.Set;

import javax.time.calendar.LocalDate;

import com.opengamma.engine.ComputationTarget;
import com.opengamma.engine.ComputationTargetType;
import com.opengamma.engine.function.AbstractFunction;
import com.opengamma.engine.function.FunctionCompilationContext;
import com.opengamma.engine.function.FunctionExecutionContext;
import com.opengamma.engine.function.FunctionInputs;
import com.opengamma.engine.function.FunctionInvoker;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.id.UniqueIdentifier;

/**
 * 
 */
public class MarketInstrumentImpliedYieldCurveFunction extends AbstractFunction implements FunctionInvoker {
  private static final String SPOT_TICKER = "US00O/N Index"; //TODO shouldn't be hard-coded
  private static final String FLOAT_REFERENCE_TICKER = "US0006M Index"; //TODO shouldn't be hard-coded
  private static final String CURVE_NAME = "USD Yield Curve"; //TODO should'nt be hard-coded
  private YieldCurveDefinition _definition;
  private UniqueIdentifier _referenceRateIdentifier;
  private ValueRequirement _referenceRateRequirement;
  private ValueRequirement _spotRateRequirement;
  private Set<ValueRequirement> _requirements;
  private ValueSpecification _curveResult;
  private ValueSpecification _jacobianResult;
  private Set<ValueSpecification> _results;

  //TODO all interpolators should be passed in
//  private final Interpolator1D<Interpolator1DCubicSplineDataBundle, InterpolationResult> _interpolator; // TODO this should not be hard-coded
  // TODO this should depend on the type of _fundingInterpolator
//  private final Interpolator1D<Interpolator1DCubicSplineWithSensitivitiesDataBundle, InterpolationResultWithSensitivities> _interpolatorWithSensitivity;
  private LocalDate _curveDate;
  private String _curveName;
//  private final Interpolator1D<? extends Interpolator1DDataBundle> _interpolator;
//  private final Interpolator1DNodeSensitivityCalculator<? extends Interpolator1DDataBundle> _sensitivityCalculator;
//
//  public MarketInstrumentImpliedYieldCurveFunction(final Currency currency, String interpolatorName, String leftExtrapolatorName, String rightExtrapolatorName, 
//      boolean useFiniteDifferenceAsDefaultForSensitivities) {
//    Validate.notNull(currency);
//    _currency = currency;
//    _interpolator = CombinedInterpolatorExtrapolatorFactory.getInterpolator(interpolatorName, leftExtrapolatorName, rightExtrapolatorName);
//    _sensitivityCalculator = CombinedInterpolatorExtrapolatorNodeSensitivityCalculatorFactory.getSensitivityCalculator(interpolatorName, leftExtrapolatorName, 
//        rightExtrapolatorName, useFiniteDifferenceAsDefaultForSensitivities);
//  }
//
//  @Override
//  public Set<ComputedValue> execute(final FunctionExecutionContext executionContext, final FunctionInputs inputs, final ComputationTarget target, final Set<ValueRequirement> desiredValues) {
//    final LocalDate now = executionContext.getSnapshotClock().today();
//    final HolidayRepository holidayRepository = OpenGammaExecutionContext.getHolidayRepository(executionContext);
//    if (holidayRepository == null) {
//      throw new IllegalStateException("Must have a holiday repository in the execution context");
//    }
//    final Calendar calendar = new HolidayRepositoryCalendarAdapter(holidayRepository, _currency);
//    final Region region = OpenGammaExecutionContext.getRegionRepository(executionContext)
//        .getHierarchyNodes(now.toLocalDate(), InMemoryRegionRepository.POLITICAL_HIERARCHY_NAME, InMemoryRegionRepository.ISO_CURRENCY_3, _currency.getISOCode()).iterator().next();
//    final List<InterestRateDerivative> derivatives = new ArrayList<InterestRateDerivative>();
//    final Set<FixedIncomeStrip> strips = _definition.getStrips();
//    final int n = strips.size();
//    final double[] marketRates = new double[n];
//    final double[] initialRatesGuess = new double[n];
//    final double[] nodeTimes = new double[n];
//    marketRates[0] = 0.01;
//    nodeTimes[0] = 0;
//    InterestRateDerivative derivative;
//    ValueRequirement stripRequirement;
//    Double rate = (Double) inputs.getValue(_spotRateRequirement);
//    if (rate == null) {
//      throw new NullPointerException("Could not get spot rate for " + _currency);
//    }
//    rate = (Double) inputs.getValue(_referenceRateRequirement);
//    if (rate == null) {
//      throw new NullPointerException("Could not get first floating rate for " + _currency);
//    }
//    
//    int i = 0;
//    for (final FixedIncomeStrip strip : strips) {
//      stripRequirement = new ValueRequirement(MarketDataRequirementNames.MARKET_VALUE, strip.getMarketDataSpecification());
//      rate = (Double) inputs.getValue(stripRequirement);
//      if (rate == null) {
//        throw new NullPointerException("Could not get market data for " + strip);
//      }
//     
//      if (strip.getInstrumentType() != StripInstrument.FUTURE) {
//        rate /= 100;
//      }
//      derivative = getInterestRateDerivative(strip, calendar, region, now, rate);
//      if (derivative == null) {
//        throw new NullPointerException("Had a null InterestRateDefinition for " + strip);
//      }
//      derivatives.add(derivative);
//      initialRatesGuess[i] = 0.01;
//      nodeTimes[i] = getLastTime(derivative);
//      i++;
//    }
//    LinkedHashMap<String, double[]> curveNodes = new LinkedHashMap<String, double[]>();
//    LinkedHashMap<String, Interpolator1D<? extends Interpolator1DDataBundle>> interpolators = new LinkedHashMap<String, Interpolator1D<? extends Interpolator1DDataBundle>>();
//    LinkedHashMap<String, Interpolator1DNodeSensitivityCalculator<? extends Interpolator1DDataBundle>> sensitivityCalculators = 
//      new LinkedHashMap<String, Interpolator1DNodeSensitivityCalculator<? extends Interpolator1DDataBundle>>();
//    curveNodes.put(CURVE_NAME, nodeTimes);
//    interpolators.put(CURVE_NAME, _interpolator);
//    sensitivityCalculators.put(CURVE_NAME, _sensitivityCalculator);
//    MultipleYieldCurveFinderDataBundle data = new MultipleYieldCurveFinderDataBundle(derivatives, null, curveNodes, interpolators, sensitivityCalculators);
//    final JacobianCalculator jacobian = new MultipleYieldCurveFinderJacobian(data, ParRateCurveSensitivityCalculator.getInstance()); //TODO have the calculator as an input
//    final Function1D<DoubleMatrix1D, DoubleMatrix1D> curveFinder = new MultipleYieldCurveFinderFunction(data, ParRateDifferenceCalculator.getInstance()); //TODO have the calculator as an input
//    NewtonVectorRootFinder rootFinder;
//    double[] yields = null;
//    try {
//      rootFinder = new BroydenVectorRootFinder(1e-7, 1e-7, 100, jacobian, DecompositionFactory.getDecomposition(DecompositionFactory.LU_COMMONS_NAME));
//      yields = rootFinder.getRoot(curveFinder, new DoubleMatrix1D(initialRatesGuess)).getData();
//    } catch (final IllegalArgumentException e) {
//      rootFinder = new BroydenVectorRootFinder(1e-7, 1e-7, 100, jacobian, DecompositionFactory.getDecomposition(DecompositionFactory.SV_COMMONS_NAME));
//      yields = rootFinder.getRoot(curveFinder, new DoubleMatrix1D(initialRatesGuess)).getData();
//    }
//    final YieldAndDiscountCurve curve = new InterpolatedYieldCurve(nodeTimes, yields, _interpolator);
//    final DoubleMatrix2D jacobianMatrix = jacobian.evaluate(new DoubleMatrix1D(yields), (Function1D<DoubleMatrix1D, DoubleMatrix1D>[]) null);
//    return Sets.newHashSet(new ComputedValue(_curveResult, curve), new ComputedValue(_jacobianResult, jacobianMatrix.getData()));
//  }
//
//  @Override
//  public void init(final FunctionCompilationContext context) {
//    final InterpolatedYieldAndDiscountCurveSource curveSource = OpenGammaCompilationContext.getDiscountCurveSource(context);
//    _definition = curveSource.getDefinition(_currency, "ForwardAndFunding");
//    _requirements = Collections.unmodifiableSet(buildRequirements(_definition));
//    _curveResult = new ValueSpecification(
//        new ValueRequirement(ValueRequirementNames.FUNDING_CURVE, _currency),
//        getUniqueIdentifier());
//    _jacobianResult = new ValueSpecification(
//        new ValueRequirement(ValueRequirementNames.FUNDING_AND_FORWARD_JACOBIAN, _currency),
//        getUniqueIdentifier());
//    _results = Sets.newHashSet(_curveResult, _jacobianResult);
//  }
//
//  public Set<ValueRequirement> buildRequirements(final InterpolatedYieldAndDiscountCurveDefinition definition) {
//    final Set<ValueRequirement> result = new HashSet<ValueRequirement>();
//    for (final FixedIncomeStrip strip : definition.getStrips()) {
//      final ValueRequirement requirement = new ValueRequirement(MarketDataRequirementNames.MARKET_VALUE, strip.getMarketDataSpecification());
//      result.add(requirement);
//    }
//
//    //TODO all of this section will need to be removed
//    final String scheme = IdentificationScheme.BLOOMBERG_TICKER.getName();
//    _referenceRateIdentifier = UniqueIdentifier.of(scheme, FLOAT_REFERENCE_TICKER);
//    final FixedIncomeStrip referenceRate = new FixedIncomeStrip(Period.ofMonths(6), _referenceRateIdentifier, StripInstrument.LIBOR);
//    final FixedIncomeStrip spotRate = new FixedIncomeStrip(Period.ofDays(1), UniqueIdentifier.of(scheme, SPOT_TICKER), StripInstrument.CASH);
//    _referenceRateRequirement = new ValueRequirement(MarketDataRequirementNames.MARKET_VALUE, referenceRate.getMarketDataSpecification());
//    _spotRateRequirement = new ValueRequirement(MarketDataRequirementNames.MARKET_VALUE, spotRate.getMarketDataSpecification());
//    result.add(_referenceRateRequirement);
//    result.add(_spotRateRequirement);
//    return result;
//  }

  @Override
  public boolean canApplyTo(final FunctionCompilationContext context, final ComputationTarget target) {
    if (target.getType() != ComputationTargetType.PRIMITIVE) {
      return false;
    }
    return false;//ObjectUtils.equals(target.getUniqueIdentifier(), _currency.getUniqueIdentifier());
  }

  @Override
  public Set<ValueRequirement> getRequirements(final FunctionCompilationContext context, final ComputationTarget target) {
    if (canApplyTo(context, target)) {
      return _requirements;
    }
    return null;
  }

  @Override
  public boolean buildsOwnSubGraph() {
    return false;
  }

  @Override
  public Set<ValueSpecification> getResults(final FunctionCompilationContext context, final ComputationTarget target) {
    if (canApplyTo(context, target)) {
      return _results;
    }
    return null;
  }

  @Override
  public String getShortName() {
    return "JointFundingAndForwardYieldCurveFunction";
  }

  @Override
  public ComputationTargetType getTargetType() {
    return ComputationTargetType.PRIMITIVE;
  }
//
//  //TODO everything from here down is rubbish
//  private InterestRateDerivative getInterestRateDerivative(final FixedIncomeStrip strip, final Calendar calendar, final Region region, final LocalDate now, final double rateOrPrice) {
//    if (strip.getInstrumentType() == StripInstrument.SWAP) {
//      return getSwap(strip, calendar, region, now, rateOrPrice);
//    } else if (strip.getInstrumentType() == StripInstrument.CASH) {
//      return getCash(strip, calendar, now, rateOrPrice);
//    } else if (strip.getInstrumentType() == StripInstrument.FRA) {
//      return getFRA(strip, calendar, now, rateOrPrice);
//    } else if (strip.getInstrumentType() == StripInstrument.FUTURE) {
//      return getIRFuture(strip, calendar, now, rateOrPrice);
//    } else if (strip.getInstrumentType() == StripInstrument.LIBOR) {
//      return getLibor(strip, calendar, now, rateOrPrice);
//    }
//    return null;
//  }
//
//  private Cash getCash(final FixedIncomeStrip cashStrip, final Calendar calendar, final LocalDate now, final double cashRate) {
//    final DayCount dayCount = cashStrip.getDayCount();
//    final BusinessDayConvention convention = cashStrip.getBusinessDayConvention();
//    final ZonedDateTime start = cashStrip.getStartDate().atStartOfDayInZone(TimeZone.UTC);
//    final ZonedDateTime end = cashStrip.getEndDate().atStartOfDayInZone(TimeZone.UTC);
//    final ZonedDateTime startAdjusted = convention.adjustDate(calendar, start);
//    final ZonedDateTime endAdjusted = convention.adjustDate(calendar, end);
//    final double t = dayCount.getDayCountFraction(startAdjusted, endAdjusted);
//
//    return new Cash(t, cashRate, CURVE_NAME);
//
//  }
//
//  private ForwardRateAgreement getFRA(final FixedIncomeStrip fraStrip, final Calendar calendar, final LocalDate now, final double strike) {
//    final DayCount dayCount = fraStrip.getDayCount();
//    final BusinessDayConvention convention = fraStrip.getBusinessDayConvention();
//    final ZonedDateTime start = fraStrip.getStartDate().atStartOfDayInZone(TimeZone.UTC);
//    final ZonedDateTime end = fraStrip.getEndDate().atStartOfDayInZone(TimeZone.UTC);
//    final ZonedDateTime startAdjusted = convention.adjustDate(calendar, start);
//    final ZonedDateTime endAdjusted = convention.adjustDate(calendar, end);
//    final ZonedDateTime nowWithTime = now.atStartOfDayInZone(TimeZone.UTC);
//    final double startTime = dayCount.getDayCountFraction(nowWithTime, startAdjusted);
//    final double endTime = dayCount.getDayCountFraction(nowWithTime, endAdjusted);
//
//    return new ForwardRateAgreement(startTime, endTime, strike, CURVE_NAME, CURVE_NAME);
//
//  }
//
//  private InterestRateFuture getIRFuture(final FixedIncomeStrip irFutureStrip, final Calendar calendar, final LocalDate now, final double price) {
//    final DayCount dayCount = irFutureStrip.getDayCount();
//    final BusinessDayConvention convention = irFutureStrip.getBusinessDayConvention();
//    final ZonedDateTime start = irFutureStrip.getStartDate().atStartOfDayInZone(TimeZone.UTC);
//    final ZonedDateTime end = irFutureStrip.getEndDate().atStartOfDayInZone(TimeZone.UTC);
//    final ZonedDateTime startAdjusted = convention.adjustDate(calendar, start);
//    final ZonedDateTime endAdjusted = convention.adjustDate(calendar, end);
//    final ZonedDateTime nowWithTime = now.atStartOfDayInZone(TimeZone.UTC);
//    final double startTime = dayCount.getDayCountFraction(nowWithTime, startAdjusted);
//    final double endTime = dayCount.getDayCountFraction(nowWithTime, endAdjusted);
//
//    return new InterestRateFuture(startTime,endTime, endTime - startTime, price, CURVE_NAME);
//
//  }
//
//  private Libor getLibor(final FixedIncomeStrip liborStrip, final Calendar calendar, final LocalDate now, final double liborRate) {
//    final DayCount dayCount = liborStrip.getDayCount();
//    final BusinessDayConvention convention = liborStrip.getBusinessDayConvention();
//    final ZonedDateTime start = liborStrip.getStartDate().atStartOfDayInZone(TimeZone.UTC);
//    final ZonedDateTime end = liborStrip.getEndDate().atStartOfDayInZone(TimeZone.UTC);
//    final ZonedDateTime startAdjusted = convention.adjustDate(calendar, start);
//    final ZonedDateTime endAdjusted = convention.adjustDate(calendar, end);
//    final double t = dayCount.getDayCountFraction(startAdjusted, endAdjusted);
//
//    return new Libor(t, liborRate, CURVE_NAME);
//
//  }
//
//  private FixedFloatSwap getSwap(final FixedIncomeStrip swapStrip, final Calendar calendar, final Region region, final LocalDate now, final double swapRate) {
//    final BusinessDayConvention convention = swapStrip.getBusinessDayConvention();
//    final ZonedDateTime effectiveDate = swapStrip.getStartDate().atStartOfDayInZone(TimeZone.UTC); //TODO change this
//    final ZonedDateTime maturityDate = swapStrip.getEndDate().atStartOfDayInZone(TimeZone.UTC); //TODO change this
//    final Frequency frequency = PeriodFrequency.SEMI_ANNUAL; //TODO change this
//    final DayCount dayCount = swapStrip.getDayCount();
//    final ZonedDateTime[] unadjustedDates = ScheduleCalculator.getUnadjustedDateSchedule(effectiveDate, maturityDate, frequency);
//    final ZonedDateTime[] adjustedDates = ScheduleCalculator.getAdjustedDateSchedule(unadjustedDates, convention, calendar);
//    final double[] swapPaymentDates = ScheduleCalculator.getTimes(adjustedDates, dayCount, now.atStartOfDayInZone(TimeZone.UTC));
//    final int n = swapPaymentDates.length;
//    final double[] delta = new double[n];
//    for (int i = 0; i < n; i++) {
//      delta[i] = 0;
//    }
//    return new FixedFloatSwap(swapPaymentDates,  swapPaymentDates, swapRate, delta, delta, CURVE_NAME, CURVE_NAME);
//
//  }
//  private double getLastTime(final InterestRateDerivative derivative) {
//    if (derivative instanceof FixedFloatSwap) {
//      return getLastSwapTime((FixedFloatSwap) derivative);
//    } else if (derivative instanceof Cash) {
//      return getLastCashTime((Cash) derivative);
//    } else if (derivative instanceof ForwardRateAgreement) {
//      return getLastFRATime((ForwardRateAgreement) derivative);
//    } else if (derivative instanceof InterestRateFuture) {
//      return getLastIRFutureTime((InterestRateFuture) derivative);
//    } else if (derivative instanceof Libor) {
//      return getLastLiborTime((Libor) derivative);
//    }
//    throw new IllegalArgumentException("This should never happen");
//  }
//
//  private double getLastSwapTime(final FixedFloatSwap swap) {
//    final int nFix = swap.getFixedLeg().getNumberOfPayments() - 1;
//    final int nFloat = swap.getFloatingLeg().getNumberOfPayments() - 1;
//    return Math.max(swap.getFixedLeg().getPaymentTimes()[nFix], swap.getFloatingLeg().getPaymentTimes()[nFloat] + swap.getFloatingLeg().getDeltaEnd()[nFloat]);
//  }
//
//  private double getLastCashTime(final Cash cash) {
//    return cash.getPaymentTime();
//  }
//
//  private double getLastLiborTime(final Libor libor) {
//    return libor.getMaturity();
//  }
//
//  private double getLastFRATime(final ForwardRateAgreement fra) {
//    return fra.getMaturity();
//  }
//
//  private double getLastIRFutureTime(final InterestRateFuture irFuture) {
//    return irFuture.getMaturity();
//  }

  @Override
  public Set<ComputedValue> execute(FunctionExecutionContext executionContext, FunctionInputs inputs, ComputationTarget target, Set<ValueRequirement> desiredValues) {
    return null;
  }
}
