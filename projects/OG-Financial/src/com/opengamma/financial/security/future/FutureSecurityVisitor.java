/**
 * Copyright (C) 2009 - Present by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.security.future;

/**
 * Visitor for the {@code FutureSecurity} subclasses.
 * 
 * @param <T> visitor method return type
 */
public interface FutureSecurityVisitor<T> {

  T visitAgricultureFutureSecurity(AgricultureFutureSecurity security);

  T visitBondFutureSecurity(BondFutureSecurity security);

  T visitEnergyFutureSecurity(EnergyFutureSecurity security);

  T visitFXFutureSecurity(FXFutureSecurity security);

  T visitIndexFutureSecurity(IndexFutureSecurity security);

  T visitInterestRateFutureSecurity(InterestRateFutureSecurity security);

  T visitMetalFutureSecurity(MetalFutureSecurity security);

  T visitStockFutureSecurity(StockFutureSecurity security);

}
