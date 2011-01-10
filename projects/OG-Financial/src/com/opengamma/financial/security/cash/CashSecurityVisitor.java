/**
 * Copyright (C) 2009 - Present by OpenGamma Inc.
 *
 * Please see distribution for license.
 */

package com.opengamma.financial.security.cash;

/**
 * Visitor for the {@code CashSecurity} subclasses.
 * 
 * @param <T> visitor method return type
 */
public interface CashSecurityVisitor<T> {

  T visitCashSecurity(CashSecurity security);

}
