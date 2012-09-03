/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.credit.creditdefaultswap.pricing;

import javax.time.calendar.ZonedDateTime;

import com.opengamma.analytics.financial.credit.BuySellProtection;
import com.opengamma.analytics.financial.credit.creditdefaultswap.definition.CreditDefaultSwapDefinition;
import com.opengamma.analytics.financial.model.interestrate.curve.YieldCurve;

/**
 *  Class containing methods for the valuation of a legacy vanilla CDS (and its constituent legs)
 */
public class PresentValueCreditDefaultSwap {

  // -------------------------------------------------------------------------------------------------

  // Method for computing the PV of a CDS based on an input CDS contract
  public double getPresentValueCreditDefaultSwap(CreditDefaultSwapDefinition cds) {

    // Construct a cashflow schedule object
    final GenerateCreditDefaultSwapPremiumLegSchedule cashflowSchedule = new GenerateCreditDefaultSwapPremiumLegSchedule();

    // Build the premium leg cashflow schedule
    ZonedDateTime[][] premiumLegSchedule = cashflowSchedule.constructCreditDefaultSwapPremiumLegSchedule(cds);

    // Calculate the value of the premium leg
    double presentValuePremiumLeg = calculatePremiumLeg(cds, premiumLegSchedule);

    // Calculate the value of the contingent leg
    double presentValueContingentLeg = 0.0; //calculateContingentLeg(cds);

    // Calculate the PV of the CDS (assumes we are buying protection i.e. paying the premium leg, receiving the contingent leg)
    double presentValue = -presentValuePremiumLeg + presentValueContingentLeg;

    // If we are selling protection, then reverse the direction of the premium and contingent leg cashflows
    if (cds.getBuySellProtection() == BuySellProtection.SELL) {
      presentValue = -1 * presentValue;
    }

    return presentValue;
  }

  // -------------------------------------------------------------------------------------------------

  public double getParSpreadCreditDefaultSwap(CreditDefaultSwapDefinition cds, ZonedDateTime[] cashflowSchedule) {

    double parSpread = 0.0;

    return parSpread;
  }

  //-------------------------------------------------------------------------------------------------

  // TODO : Seperate out the accrued premium calc out into another method (so users can see contribution of this directly)
  // TODO : Add a method to calc both the legs in one go (is this useful or not? Might be useful from a speed perspective - remember can have O(10^5) positions in a book)

  // We assume the discount factors have been computed externally and are passed in with the CDS object
  // We assume the 'calibrated' survival probabilities have been computed externally and are passed in with the CDS object
  // Will replace these three dummy 'objects' with suitably computed objects in due course
  // For now, assuming we are on a cashflow date (for testing purposes) - will need to add the corrections for a seasoned trade

  // -------------------------------------------------------------------------------------------------

  // Method to calculate the value of the premium leg of a CDS 
  double calculatePremiumLeg(CreditDefaultSwapDefinition cds, ZonedDateTime[][] cashflowSchedule) {

    // -------------------------------------------------------------

    double dcf = 360.0;
    double presentValuePremiumLeg = 0.0;
    double presentValueAccruedPremium = 0.0;

    // -------------------------------------------------------------

    // Get the relevant contract date needed to value the premium leg

    //String daycountConvention = cds.getDayCountFractionConvention();

    // Get the notional amount to multiply the premium leg by
    double notional = cds.getNotional();

    // Get the CDS par spread (remember this is supplied in bps, therefore needs to be divided by 10,000)
    double parSpread = cds.getParSpread() / 10000.0;

    // get the yield curve
    YieldCurve yieldCurve = cds.getYieldCurve();

    // Get the survival curve (this will be retreived based on the CDS credit key to uniquely identify a particular spread curve)
    YieldCurve survivalCurve = cds.getSurvivalCurve();

    // Do we need to calculate the accrued premium as well
    boolean includeAccruedPremium = cds.getIncludeAccruedPremium();

    // -------------------------------------------------------------

    // TODO : Rework this code when add the enum
    /*
    switch (daycountConvention) {
      case "ACT/360":
        dcf = 360.0;
        break;

      default:
        dcf = 360.0;
        break;
    }
    */

    // -------------------------------------------------------------

    // Loop through all the elements in the cashflow schedule (note limits of loop)
    for (int i = 1; i < cashflowSchedule.length; i++) {

      System.out.println("Cashflow i = " + i + ", on adj date " + cashflowSchedule[i][0]);

      // TODO : Is there a better way of doing this?
      //long t = cashflowSchedule[i].toEpochSeconds();

      // TODO : Is there a better way of doing this?
      //long dcf = (((cashflowSchedule[i].toEpochSeconds() - cashflowSchedule[i - 1].toEpochSeconds())) / (long) (24.0 * 60.0 * 60.0)) / (long) 360.0;

      // TODO : This is getting silly now
      //long discountFactor = (long) yieldCurve.getDiscountFactor(t);
      //long survivalProbability = (long) survivalCurve.getDiscountFactor(t);

      //presentValuePremiumLeg += dcf * discountFactor * survivalProbability;

      // If required, calculate the accrued premium contribution to the overall premium leg
      if (includeAccruedPremium) {

        //long tPrevious = cashflowSchedule[i - 1].toEpochSeconds();
        //long survivalProbabilityPrevious = (long) survivalCurve.getDiscountFactor(tPrevious);

        //presentValueAccruedPremium += 0.5 * dcf * discountFactor * (survivalProbabilityPrevious - survivalProbability);
      }
    }

    // -------------------------------------------------------------

    return parSpread * notional * (presentValuePremiumLeg + presentValueAccruedPremium);

    // -------------------------------------------------------------
  }

  // -------------------------------------------------------------------------------------------------

  // Method to calculate the accrued premium of a CDS premium leg (this method is just to allow a user to calculate the accrued on its own)
  double calculateAccruedPremium(CreditDefaultSwapDefinition cds) {

    // TODO : Add this code

    double presentValueAccruedPremium = 0.0;

    return presentValueAccruedPremium;
  }

  // -------------------------------------------------------------------------------------------------

  // TODO: Need to fix this code up so it is not just hacked together

  // Method to calculate the value of the contingent leg of a CDS
  double calculateContingentLeg(CreditDefaultSwapDefinition cds) {

    double presentValueContingentLeg = 0.0;

    // Get the notional amount to multiply the contingent leg by
    double notional = cds.getNotional();

    // get the yield curve
    YieldCurve yieldCurve = cds.getYieldCurve();

    // Get the survival curve
    YieldCurve survivalCurve = cds.getSurvivalCurve();

    // Hardcoded hack for now - will remove when implement the schedule generator
    int tVal = 0;
    int maturity = 5;

    //ZonedDateTime maturityDate = cds.getMaturityDate();
    //ZonedDateTime valuationDate = cds.getValuationDate();

    //int numDays = DateUtils.getDaysBetween(valuationDate, maturityDate);

    int numberOfIntegrationSteps = cds.getNumberOfIntegrationSteps();

    // Check this calculation more carefully - is the proper way to do it
    //int numberOfPartitions = (int) (numberOfIntegrationSteps * numDays / 365.25 + 0.5); 

    int numberOfPartitions = (int) (numberOfIntegrationSteps * (maturity - tVal) + 0.5);

    double epsilon = (double) (maturity - tVal) / (double) numberOfPartitions;

    double valuationRecoveryRate = cds.getValuationRecoveryRate();

    // Calculate the integral for the contingent leg
    for (int k = 1; k < numberOfPartitions; k++) {

      double t = k * epsilon;
      double tPrevious = (k - 1) * epsilon;

      double discountFactor = yieldCurve.getDiscountFactor(t);
      double survivalProbability = survivalCurve.getDiscountFactor(t);
      double survivalProbabilityPrevious = survivalCurve.getDiscountFactor(tPrevious);

      presentValueContingentLeg += discountFactor * (survivalProbabilityPrevious - survivalProbability);
    }

    return notional * (1 - valuationRecoveryRate) * presentValueContingentLeg;
  }

  // -------------------------------------------------------------------------------------------------
}
