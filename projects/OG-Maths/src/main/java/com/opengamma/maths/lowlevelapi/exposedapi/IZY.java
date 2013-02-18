/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.maths.lowlevelapi.exposedapi;

import com.opengamma.maths.lowlevelapi.exposedapi.IZYBacking.IZYAPIInterface;
import com.opengamma.maths.lowlevelapi.exposedapi.IZYBacking.IZYAbstractSuper;
import com.opengamma.maths.lowlevelapi.exposedapi.IZYBacking.IZYNativeBacked;
import com.opengamma.maths.lowlevelapi.exposedapi.IZYBacking.IZYReferenceJavaBacked;

/**
 * Provides a unified interface to IZY
 */
public class IZY implements IZYAPIInterface {

  private IZYAbstractSuper _localIZY;

  /**
   * Backing type
   */
  public enum backing {
    /**
     * Reference Java backed BLAS
     */
    Referencejava,
    /**
     * OG Native backed BLAS
     */
    OGnative,
  }

  public IZY() {
    _localIZY = new IZYNativeBacked();
  }

  public IZY(backing backedby) {
    if (backedby == backing.Referencejava) {
      _localIZY = new IZYReferenceJavaBacked();
    } else if (backedby == backing.OGnative) {
      _localIZY = new IZYNativeBacked();
    }

  }

  @Override
  public void vd_acos(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_acos(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_asin(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_asin(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_atan(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_atan(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_atan2(int[] count, double[] arg0, int[] offsetarg0, double[] arg1, int[] offsetarg1, double[] out0, int[] offsetout0) {
    _localIZY.vd_atan2(count, arg0, offsetarg0, arg1, offsetarg1, out0, offsetout0);
  }

  @Override
  public void vd_cos(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_cos(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_sin(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_sin(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_sincos(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0, double[] out1, int[] offsetout1) {
    _localIZY.vd_sincos(count, arg0, offsetarg0, out0, offsetout0, out1, offsetout1);
  }

  @Override
  public void vd_tan(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_tan(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_acosh(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_acosh(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_asinh(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_asinh(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_atanh(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_atanh(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_cosh(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_cosh(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_sinh(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_sinh(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_tanh(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_tanh(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_cbrt(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_cbrt(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_hypot(int[] count, double[] arg0, int[] offsetarg0, double[] arg1, int[] offsetarg1, double[] out0, int[] offsetout0) {
    _localIZY.vd_hypot(count, arg0, offsetarg0, arg1, offsetarg1, out0, offsetout0);
  }

  @Override
  public void vd_inv(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_inv(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_invcbrt(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_invcbrt(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_invsqrt(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_invsqrt(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_pow(int[] count, double[] arg0, int[] offsetarg0, double[] arg1, int[] offsetarg1, double[] out0, int[] offsetout0) {
    _localIZY.vd_pow(count, arg0, offsetarg0, arg1, offsetarg1, out0, offsetout0);
  }

  @Override
  public void vd_powx(int[] count, double[] arg0, int[] offsetarg0, double[] arg1, int[] offsetarg1, double[] out0, int[] offsetout0) {
    _localIZY.vd_powx(count, arg0, offsetarg0, arg1, offsetarg1, out0, offsetout0);
  }

  @Override
  public void vd_pow2o3(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_pow2o3(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_pow3o2(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_pow3o2(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_sqrt(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_sqrt(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_exp(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_exp(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_expm1(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_expm1(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_ln(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_ln(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_log10(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_log10(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_log1p(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_log1p(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_abs(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_abs(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_add(int[] count, double[] arg0, int[] offsetarg0, double[] arg1, int[] offsetarg1, double[] out0, int[] offsetout0) {
    _localIZY.vd_add(count, arg0, offsetarg0, arg1, offsetarg1, out0, offsetout0);
  }

  @Override
  public void vd_addx(int[] count, double[] arg0, int[] offsetarg0, double[] arg1, int[] offsetarg1, double[] out0, int[] offsetout0) {
    _localIZY.vd_addx(count, arg0, offsetarg0, arg1, offsetarg1, out0, offsetout0);
  }

  @Override
  public void vd_div(int[] count, double[] arg0, int[] offsetarg0, double[] arg1, int[] offsetarg1, double[] out0, int[] offsetout0) {
    _localIZY.vd_div(count, arg0, offsetarg0, arg1, offsetarg1, out0, offsetout0);
  }

  @Override
  public void vd_divx(int[] count, double[] arg0, int[] offsetarg0, double[] arg1, int[] offsetarg1, double[] out0, int[] offsetout0) {
    _localIZY.vd_divx(count, arg0, offsetarg0, arg1, offsetarg1, out0, offsetout0);
  }

  @Override
  public void vd_mul(int[] count, double[] arg0, int[] offsetarg0, double[] arg1, int[] offsetarg1, double[] out0, int[] offsetout0) {
    _localIZY.vd_mul(count, arg0, offsetarg0, arg1, offsetarg1, out0, offsetout0);
  }

  @Override
  public void vd_mulx(int[] count, double[] arg0, int[] offsetarg0, double[] arg1, int[] offsetarg1, double[] out0, int[] offsetout0) {
    _localIZY.vd_mulx(count, arg0, offsetarg0, arg1, offsetarg1, out0, offsetout0);
  }

  @Override
  public void vd_sqr(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_sqr(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_sub(int[] count, double[] arg0, int[] offsetarg0, double[] arg1, int[] offsetarg1, double[] out0, int[] offsetout0) {
    _localIZY.vd_sub(count, arg0, offsetarg0, arg1, offsetarg1, out0, offsetout0);
  }

  @Override
  public void vd_subx(int[] count, double[] arg0, int[] offsetarg0, double[] arg1, int[] offsetarg1, double[] out0, int[] offsetout0) {
    _localIZY.vd_subx(count, arg0, offsetarg0, arg1, offsetarg1, out0, offsetout0);
  }

  @Override
  public void vd_ceil(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_ceil(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_floor(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_floor(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_modf(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0, double[] out1, int[] offsetout1) {
    _localIZY.vd_modf(count, arg0, offsetarg0, out0, offsetout0, out1, offsetout1);
  }

  @Override
  public void vd_nearbyint(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_nearbyint(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_rint(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_rint(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_round(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_round(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_trunc(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_trunc(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_cdfnorm(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_cdfnorm(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_cdfnorminv(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_cdfnorminv(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_erf(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_erf(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_erfc(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_erfc(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_erfinv(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_erfinv(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_erfcinv(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_erfcinv(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_lgamma(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_lgamma(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vd_tgamma(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vd_tgamma(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vz_abs(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vz_abs(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vz_acos(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vz_acos(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vz_acosh(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vz_acosh(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vz_add(int[] count, double[] arg0, int[] offsetarg0, double[] arg1, int[] offsetarg1, double[] out0, int[] offsetout0) {
    _localIZY.vz_add(count, arg0, offsetarg0, arg1, offsetarg1, out0, offsetout0);
  }

  @Override
  public void vz_addx(int[] count, double[] arg0, int[] offsetarg0, double[] arg1, int[] offsetarg1, double[] out0, int[] offsetout0) {
    _localIZY.vz_addx(count, arg0, offsetarg0, arg1, offsetarg1, out0, offsetout0);
  }

  @Override
  public void vz_arg(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vz_arg(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vz_asin(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vz_asin(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vz_asinh(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vz_asinh(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vz_atan(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vz_atan(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vz_atanh(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vz_atanh(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vz_conj(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vz_conj(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vz_cos(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vz_cos(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vz_cosh(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vz_cosh(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vz_div(int[] count, double[] arg0, int[] offsetarg0, double[] arg1, int[] offsetarg1, double[] out0, int[] offsetout0) {
    _localIZY.vz_div(count, arg0, offsetarg0, arg1, offsetarg1, out0, offsetout0);
  }

  @Override
  public void vz_divx(int[] count, double[] arg0, int[] offsetarg0, double[] arg1, int[] offsetarg1, double[] out0, int[] offsetout0) {
    _localIZY.vz_divx(count, arg0, offsetarg0, arg1, offsetarg1, out0, offsetout0);
  }

  @Override
  public void vz_exp(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vz_exp(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vz_ln(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vz_ln(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vz_log10(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vz_log10(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vz_mul(int[] count, double[] arg0, int[] offsetarg0, double[] arg1, int[] offsetarg1, double[] out0, int[] offsetout0) {
    _localIZY.vz_mul(count, arg0, offsetarg0, arg1, offsetarg1, out0, offsetout0);
  }

  @Override
  public void vz_mulx(int[] count, double[] arg0, int[] offsetarg0, double[] arg1, int[] offsetarg1, double[] out0, int[] offsetout0) {
    _localIZY.vz_mulx(count, arg0, offsetarg0, arg1, offsetarg1, out0, offsetout0);
  }

  @Override
  public void vz_mulbyconj(int[] count, double[] arg0, int[] offsetarg0, double[] arg1, int[] offsetarg1, double[] out0, int[] offsetout0) {
    _localIZY.vz_mulbyconj(count, arg0, offsetarg0, arg1, offsetarg1, out0, offsetout0);
  }

  @Override
  public void vz_pow(int[] count, double[] arg0, int[] offsetarg0, double[] arg1, int[] offsetarg1, double[] out0, int[] offsetout0) {
    _localIZY.vz_pow(count, arg0, offsetarg0, arg1, offsetarg1, out0, offsetout0);
  }

  @Override
  public void vz_powx(int[] count, double[] arg0, int[] offsetarg0, double[] arg1, int[] offsetarg1, double[] out0, int[] offsetout0) {
    _localIZY.vz_powx(count, arg0, offsetarg0, arg1, offsetarg1, out0, offsetout0);
  }

  @Override
  public void vz_sin(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vz_sin(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vz_sinh(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vz_sinh(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vz_sqrt(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vz_sqrt(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vz_sub(int[] count, double[] arg0, int[] offsetarg0, double[] arg1, int[] offsetarg1, double[] out0, int[] offsetout0) {
    _localIZY.vz_sub(count, arg0, offsetarg0, arg1, offsetarg1, out0, offsetout0);
  }

  @Override
  public void vz_subx(int[] count, double[] arg0, int[] offsetarg0, double[] arg1, int[] offsetarg1, double[] out0, int[] offsetout0) {
    _localIZY.vz_subx(count, arg0, offsetarg0, arg1, offsetarg1, out0, offsetout0);
  }

  @Override
  public void vz_tan(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vz_tan(count, arg0, offsetarg0, out0, offsetout0);
  }

  @Override
  public void vz_tanh(int[] count, double[] arg0, int[] offsetarg0, double[] out0, int[] offsetout0) {
    _localIZY.vz_tanh(count, arg0, offsetarg0, out0, offsetout0);
  }
}