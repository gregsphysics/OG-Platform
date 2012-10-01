/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.volatility.surface;

import javax.time.calendar.DayOfWeek;
import javax.time.calendar.LocalDate;
import javax.time.calendar.format.DateTimeFormatter;
import javax.time.calendar.format.DateTimeFormatters;

import org.apache.commons.lang.Validate;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.core.id.ExternalSchemes;
import com.opengamma.financial.analytics.ircurve.NextExpiryAdjuster;
import com.opengamma.financial.analytics.model.FutureOptionExpiries;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalScheme;

/**
 * Provides ExternalId's for EquityFutureOptions used to build the Volatility Surface
 */
public class BloombergEquityFutureOptionVolatilitySurfaceInstrumentProvider extends BloombergFutureOptionVolatilitySurfaceInstrumentProvider {

  private static final ExternalScheme SCHEME = ExternalSchemes.BLOOMBERG_TICKER_WEAK;
  private static final DateTimeFormatter FORMAT = DateTimeFormatters.pattern("MM/dd/yy");
  private static final FutureOptionExpiries EXPIRY_UTILS = FutureOptionExpiries.of(new NextExpiryAdjuster(3, DayOfWeek.FRIDAY, 1));

  private static final BiMap<String, FutureOptionExpiries> EXPIRY_RULES;
  static {
    EXPIRY_RULES = HashBiMap.create();
    EXPIRY_RULES.put("NKY", FutureOptionExpiries.of(new NextExpiryAdjuster(2, DayOfWeek.FRIDAY)));
    EXPIRY_RULES.put("DJX", FutureOptionExpiries.of(new NextExpiryAdjuster(3, DayOfWeek.FRIDAY, 1)));
    EXPIRY_RULES.put("SPX", FutureOptionExpiries.of(new NextExpiryAdjuster(3, DayOfWeek.FRIDAY, 1)));
    EXPIRY_RULES.put("UKX", FutureOptionExpiries.of(new NextExpiryAdjuster(3, DayOfWeek.FRIDAY)));
    //TODO DAX, EUROSTOXX 50 (SX5E)
  }

  /**
   * @param futureOptionPrefix the prefix to the resulting code (eg DJX)
   * @param postfix the postfix to the resulting code (eg Index)
   * @param dataFieldName the name of the data field. Expecting MarketDataRequirementNames.IMPLIED_VOLATILITY or OPT_IMPLIED_VOLATILITY_MID
   * @param useCallAboveStrike the strike above which to use calls rather than puts
   * @param exchangeIdName the exchange id
   */
  public BloombergEquityFutureOptionVolatilitySurfaceInstrumentProvider(final String futureOptionPrefix, final String postfix, final String dataFieldName, final Double useCallAboveStrike,
      final String exchangeIdName) {
    super(futureOptionPrefix, postfix, dataFieldName, useCallAboveStrike, exchangeIdName);
  }

  /**
   * Provides ExternalID for Bloomberg ticker,
   * given a reference date and an integer offset, the n'th subsequent option <p>
   * The format is prefix + date + callPutFlag + strike + postfix <p>
   * e.g. DJX 12/21/13 C145.0 Index
   * <p>
   * @param futureOptionNumber n'th future following curve date
   * @param strike option's strike, expressed as price in %, e.g. 98.750
   * @param surfaceDate date of curve validity; valuation date
   * @return the id of the Bloomberg ticker
   */
  @Override
  public ExternalId getInstrument(final Number futureOptionNumber, final Double strike, final LocalDate surfaceDate) {
    Validate.notNull(futureOptionNumber, "futureOptionNumber");
    final String prefix = getFutureOptionPrefix();
    final StringBuffer ticker = new StringBuffer();
    ticker.append(prefix);
    ticker.append(" ");
    FutureOptionExpiries expiryRule = EXPIRY_RULES.get(prefix);
    if (expiryRule == null) {
      throw new OpenGammaRuntimeException("No expiry rule has been setup for " + prefix + ". Determine week and day pattern and add to EXPIRY_RULES.");
    }
    final LocalDate expiry = expiryRule.getFutureOptionExpiry(futureOptionNumber.intValue(), surfaceDate);
    ticker.append(FORMAT.print(expiry));
    ticker.append(" ");
    ticker.append(strike > useCallAboveStrike() ? "C" : "P");
    ticker.append(strike);
    ticker.append(" ");
    ticker.append(getPostfix());
    return ExternalId.of(SCHEME, ticker.toString());
  }

}
