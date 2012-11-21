// Autogenerated, do not edit!
/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.maths.dogma;
import com.opengamma.maths.commonapi.numbers.ComplexType;
import com.opengamma.maths.dogma.engine.language.InfixOperator;
import com.opengamma.maths.dogma.engine.language.UnaryFunction;
import com.opengamma.maths.dogma.engine.language.Function;
import com.opengamma.maths.dogma.engine.operationstack.InfixOpChain;
import com.opengamma.maths.dogma.engine.operationstack.MethodScraperForInfixOperators;
import com.opengamma.maths.dogma.engine.operationstack.MethodScraperForUnaryFunctions;
import com.opengamma.maths.dogma.engine.operationstack.OperatorDictionaryPopulator;
import com.opengamma.maths.dogma.engine.operationstack.RunInfixOpChain;
import com.opengamma.maths.dogma.engine.operationstack.RunUnaryFunctionChain;
import com.opengamma.maths.dogma.engine.operationstack.UnaryFunctionChain;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGArray;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGComplexScalar;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGMatrix;
import com.opengamma.maths.lowlevelapi.functions.checkers.Catchers;import com.opengamma.maths.dogma.engine.matrixinfo.ConversionCostAdjacencyMatrixStore;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGRealScalar;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGMatrix;
import com.opengamma.maths.dogma.engine.matrixinfo.MatrixTypeToIndexMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.opengamma.maths.dogma.engine.methodhookinstances.Mtimes;
import com.opengamma.maths.dogma.engine.methodhookinstances.Times;
import com.opengamma.maths.dogma.engine.methodhookinstances.Transpose;
import com.opengamma.maths.dogma.engine.methodhookinstances.Ctranspose;
import com.opengamma.maths.dogma.engine.methodhookinstances.Minus;
import com.opengamma.maths.dogma.engine.methodhookinstances.Sin;
import com.opengamma.maths.dogma.engine.methodhookinstances.Rdivide;
import com.opengamma.maths.dogma.engine.methodhookinstances.Plus;
/**
 * Provides the DOGMA Language
 */
public class DogmaLanguage {
private static DogmaLanguage s_instance;
DogmaLanguage() {
}
public static DogmaLanguage getInstance() {
return s_instance;
}
private static Logger s_log = LoggerFactory.getLogger(DogmaLanguage.class);// switch for chatty start up
private static boolean s_verbose;
public DogmaLanguage(boolean verbose) {
s_verbose = verbose;
};
private static RunInfixOpChain s_infixOpChainRunner = new RunInfixOpChain();
private static RunUnaryFunctionChain s_unaryFunctionChainRunner = new RunUnaryFunctionChain();
private static InfixOpChain[][] s_mtimesInstructions; //CSOFF
private static InfixOpChain[][] s_timesInstructions; //CSOFF
private static UnaryFunctionChain[] s_transposeInstructions; //CSOFF
private static UnaryFunctionChain[] s_ctransposeInstructions; //CSOFF
private static InfixOpChain[][] s_minusInstructions; //CSOFF
private static UnaryFunctionChain[] s_sinInstructions; //CSOFF
private static InfixOpChain[][] s_rdivideInstructions; //CSOFF
private static InfixOpChain[][] s_plusInstructions; //CSOFF
static {
if(s_verbose){
  s_log.info("Welcome to DOGMA");  s_log.info("Building instructions...");}
final double[][] DefaultInfixFunctionEvalCosts = new double[][] {
{1.00, 1.00, 1.00, 1.00, 0.00, 1.00, 1.00, 1.00, 1.00, 1.00 },//
{1.00, 1.00, 0.00, 1.00, 0.00, 0.00, 0.00, 1.00, 0.00, 1.00 },//
{1.00, 0.00, 1.00, 1.00, 0.00, 0.00, 1.00, 1.00, 1.00, 1.00 },//
{1.00, 1.00, 1.00, 1.00, 0.00, 0.00, 0.00, 1.00, 0.00, 1.00 },//
{0.00, 0.00, 0.00, 0.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00 },//
{1.00, 0.00, 0.00, 0.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00 },//
{1.00, 0.00, 1.00, 0.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00 },//
{1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 0.00, 1.00 },//
{1.00, 0.00, 1.00, 0.00, 1.00, 1.00, 1.00, 0.00, 1.00, 1.00 },//
{1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00 } };
OGMatrix defaultInfixFunctionEvalCostsMatrix = new OGMatrix(DefaultInfixFunctionEvalCosts);
final double[][] DefaultUnaryFunctionEvalCosts = new double[][] {//
{1 },//
{1 },//
{2 },//
{3 },//
{3 },//
{5 },//
{5 },//
{5 },//
{10 },//
{20 } };
OGMatrix defaultUnaryFunctionEvalCostsMatrix = new OGMatrix(DefaultUnaryFunctionEvalCosts);
// Build instructions sets
 OperatorDictionaryPopulator<InfixOperator<OGArray<? extends Number>, OGArray<? extends Number>, OGArray<? extends Number>>> operatorDictInfix = new OperatorDictionaryPopulator<InfixOperator<OGArray<? extends Number>, OGArray<? extends Number>, OGArray<? extends Number>>>();
OperatorDictionaryPopulator<UnaryFunction<OGArray<? extends Number>, OGArray<? extends Number>>> operatorDictUnary = new OperatorDictionaryPopulator<UnaryFunction<OGArray<? extends Number>, OGArray<? extends Number>>>();
InfixOperator<OGArray<? extends Number>, OGArray<? extends Number>, OGArray<? extends Number>>[][] MtimesFunctionTable = MethodScraperForInfixOperators.availableMethodsForInfixOp(operatorDictInfix.getOperationsMap(),Mtimes.class, s_verbose);
s_mtimesInstructions = MethodScraperForInfixOperators.computeFunctions(ConversionCostAdjacencyMatrixStore.getWeightedAdjacencyMatrix(),MtimesFunctionTable, defaultInfixFunctionEvalCostsMatrix);

InfixOperator<OGArray<? extends Number>, OGArray<? extends Number>, OGArray<? extends Number>>[][] TimesFunctionTable = MethodScraperForInfixOperators.availableMethodsForInfixOp(operatorDictInfix.getOperationsMap(),Times.class, s_verbose);
s_timesInstructions = MethodScraperForInfixOperators.computeFunctions(ConversionCostAdjacencyMatrixStore.getWeightedAdjacencyMatrix(),TimesFunctionTable, defaultInfixFunctionEvalCostsMatrix);

UnaryFunction<OGArray<? extends Number>, OGArray<? extends Number>>[] TransposeFunctionTable = MethodScraperForUnaryFunctions.availableMethodsForUnaryFunctions(operatorDictUnary.getOperationsMap(),Transpose.class);
s_transposeInstructions = MethodScraperForUnaryFunctions.computeFunctions(ConversionCostAdjacencyMatrixStore.getWeightedAdjacencyMatrix(),TransposeFunctionTable, defaultUnaryFunctionEvalCostsMatrix);

UnaryFunction<OGArray<? extends Number>, OGArray<? extends Number>>[] CtransposeFunctionTable = MethodScraperForUnaryFunctions.availableMethodsForUnaryFunctions(operatorDictUnary.getOperationsMap(),Ctranspose.class);
s_ctransposeInstructions = MethodScraperForUnaryFunctions.computeFunctions(ConversionCostAdjacencyMatrixStore.getWeightedAdjacencyMatrix(),CtransposeFunctionTable, defaultUnaryFunctionEvalCostsMatrix);

InfixOperator<OGArray<? extends Number>, OGArray<? extends Number>, OGArray<? extends Number>>[][] MinusFunctionTable = MethodScraperForInfixOperators.availableMethodsForInfixOp(operatorDictInfix.getOperationsMap(),Minus.class, s_verbose);
s_minusInstructions = MethodScraperForInfixOperators.computeFunctions(ConversionCostAdjacencyMatrixStore.getWeightedAdjacencyMatrix(),MinusFunctionTable, defaultInfixFunctionEvalCostsMatrix);

UnaryFunction<OGArray<? extends Number>, OGArray<? extends Number>>[] SinFunctionTable = MethodScraperForUnaryFunctions.availableMethodsForUnaryFunctions(operatorDictUnary.getOperationsMap(),Sin.class);
s_sinInstructions = MethodScraperForUnaryFunctions.computeFunctions(ConversionCostAdjacencyMatrixStore.getWeightedAdjacencyMatrix(),SinFunctionTable, defaultUnaryFunctionEvalCostsMatrix);

InfixOperator<OGArray<? extends Number>, OGArray<? extends Number>, OGArray<? extends Number>>[][] RdivideFunctionTable = MethodScraperForInfixOperators.availableMethodsForInfixOp(operatorDictInfix.getOperationsMap(),Rdivide.class, s_verbose);
s_rdivideInstructions = MethodScraperForInfixOperators.computeFunctions(ConversionCostAdjacencyMatrixStore.getWeightedAdjacencyMatrix(),RdivideFunctionTable, defaultInfixFunctionEvalCostsMatrix);

InfixOperator<OGArray<? extends Number>, OGArray<? extends Number>, OGArray<? extends Number>>[][] PlusFunctionTable = MethodScraperForInfixOperators.availableMethodsForInfixOp(operatorDictInfix.getOperationsMap(),Plus.class, s_verbose);
s_plusInstructions = MethodScraperForInfixOperators.computeFunctions(ConversionCostAdjacencyMatrixStore.getWeightedAdjacencyMatrix(),PlusFunctionTable, defaultInfixFunctionEvalCostsMatrix);

if(s_verbose){
  s_log.info("DOGMA built.");}

}

public static OGArray<? extends Number>mtimes(OGArray<? extends Number> arg1, OGArray<? extends Number> arg2) {
  Catchers.catchNullFromArgList(arg1, 1);
  Catchers.catchNullFromArgList(arg2, 2);
  int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1.getClass());
  int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg2.getClass());
  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_mtimesInstructions[type1][type2], arg1, arg2);
  return tmp;
}

public static OGArray<? extends Number> mtimes(Number arg1, OGArray<? extends Number> arg2) {
  Catchers.catchNullFromArgList(arg1, 1);
  Catchers.catchNullFromArgList(arg2, 2);
OGArray<? extends Number> arg1rewrite;
if (arg1.getClass() == ComplexType.class) {
arg1rewrite = new OGComplexScalar(arg1);
} else {
arg1rewrite = new OGRealScalar(arg1.doubleValue());
}
int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1rewrite.getClass());
int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg2.getClass());
  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_mtimesInstructions[type1][type2], arg1rewrite, arg2);
  return tmp;
}

public static OGArray<? extends Number>mtimes(OGArray<? extends Number> arg1, Number arg2) {
  Catchers.catchNullFromArgList(arg1, 1);
  Catchers.catchNullFromArgList(arg2, 2);
OGArray<? extends Number> arg2rewrite;
if (arg2.getClass() == ComplexType.class) {
arg2rewrite = new OGComplexScalar(arg2);
} else {
 arg2rewrite = new OGRealScalar(arg2.doubleValue());
 }
 int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1.getClass());
 int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg2rewrite.getClass());
  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_mtimesInstructions[type1][type2], arg1, arg2rewrite);
  return tmp;
}


public static OGArray<? extends Number>times(OGArray<? extends Number> arg1, OGArray<? extends Number> arg2) {
  Catchers.catchNullFromArgList(arg1, 1);
  Catchers.catchNullFromArgList(arg2, 2);
  int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1.getClass());
  int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg2.getClass());
  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_timesInstructions[type1][type2], arg1, arg2);
  return tmp;
}

public static OGArray<? extends Number> times(Number arg1, OGArray<? extends Number> arg2) {
  Catchers.catchNullFromArgList(arg1, 1);
  Catchers.catchNullFromArgList(arg2, 2);
OGArray<? extends Number> arg1rewrite;
if (arg1.getClass() == ComplexType.class) {
arg1rewrite = new OGComplexScalar(arg1);
} else {
arg1rewrite = new OGRealScalar(arg1.doubleValue());
}
int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1rewrite.getClass());
int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg2.getClass());
  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_timesInstructions[type1][type2], arg1rewrite, arg2);
  return tmp;
}

public static OGArray<? extends Number>times(OGArray<? extends Number> arg1, Number arg2) {
  Catchers.catchNullFromArgList(arg1, 1);
  Catchers.catchNullFromArgList(arg2, 2);
OGArray<? extends Number> arg2rewrite;
if (arg2.getClass() == ComplexType.class) {
arg2rewrite = new OGComplexScalar(arg2);
} else {
 arg2rewrite = new OGRealScalar(arg2.doubleValue());
 }
 int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1.getClass());
 int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg2rewrite.getClass());
  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_timesInstructions[type1][type2], arg1, arg2rewrite);
  return tmp;
}


public static OGArray<? extends Number>transpose(OGArray<? extends Number> arg1) {
Catchers.catchNullFromArgList(arg1, 1);
int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1.getClass());
OGArray<? extends Number> tmp = s_unaryFunctionChainRunner.dispatch(s_transposeInstructions[type1], arg1);
return tmp;
}

public static OGArray<? extends Number>transpose(Number arg1) {Catchers.catchNullFromArgList(arg1, 1);
OGArray<? extends Number> arg1rewrite;
if (arg1.getClass() == ComplexType.class) {
arg1rewrite = new OGComplexScalar(arg1);
} else {
arg1rewrite = new OGRealScalar(arg1.doubleValue());
}
int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1rewrite.getClass());
OGArray<? extends Number> tmp = s_unaryFunctionChainRunner.dispatch(s_transposeInstructions[type1], arg1rewrite);
return tmp;
}


public static OGArray<? extends Number>ctranspose(OGArray<? extends Number> arg1) {
Catchers.catchNullFromArgList(arg1, 1);
int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1.getClass());
OGArray<? extends Number> tmp = s_unaryFunctionChainRunner.dispatch(s_ctransposeInstructions[type1], arg1);
return tmp;
}

public static OGArray<? extends Number>ctranspose(Number arg1) {Catchers.catchNullFromArgList(arg1, 1);
OGArray<? extends Number> arg1rewrite;
if (arg1.getClass() == ComplexType.class) {
arg1rewrite = new OGComplexScalar(arg1);
} else {
arg1rewrite = new OGRealScalar(arg1.doubleValue());
}
int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1rewrite.getClass());
OGArray<? extends Number> tmp = s_unaryFunctionChainRunner.dispatch(s_ctransposeInstructions[type1], arg1rewrite);
return tmp;
}


public static OGArray<? extends Number>minus(OGArray<? extends Number> arg1, OGArray<? extends Number> arg2) {
  Catchers.catchNullFromArgList(arg1, 1);
  Catchers.catchNullFromArgList(arg2, 2);
  int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1.getClass());
  int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg2.getClass());
  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_minusInstructions[type1][type2], arg1, arg2);
  return tmp;
}

public static OGArray<? extends Number> minus(Number arg1, OGArray<? extends Number> arg2) {
  Catchers.catchNullFromArgList(arg1, 1);
  Catchers.catchNullFromArgList(arg2, 2);
OGArray<? extends Number> arg1rewrite;
if (arg1.getClass() == ComplexType.class) {
arg1rewrite = new OGComplexScalar(arg1);
} else {
arg1rewrite = new OGRealScalar(arg1.doubleValue());
}
int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1rewrite.getClass());
int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg2.getClass());
  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_minusInstructions[type1][type2], arg1rewrite, arg2);
  return tmp;
}

public static OGArray<? extends Number>minus(OGArray<? extends Number> arg1, Number arg2) {
  Catchers.catchNullFromArgList(arg1, 1);
  Catchers.catchNullFromArgList(arg2, 2);
OGArray<? extends Number> arg2rewrite;
if (arg2.getClass() == ComplexType.class) {
arg2rewrite = new OGComplexScalar(arg2);
} else {
 arg2rewrite = new OGRealScalar(arg2.doubleValue());
 }
 int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1.getClass());
 int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg2rewrite.getClass());
  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_minusInstructions[type1][type2], arg1, arg2rewrite);
  return tmp;
}


public static OGArray<? extends Number>sin(OGArray<? extends Number> arg1) {
Catchers.catchNullFromArgList(arg1, 1);
int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1.getClass());
OGArray<? extends Number> tmp = s_unaryFunctionChainRunner.dispatch(s_sinInstructions[type1], arg1);
return tmp;
}

public static OGArray<? extends Number>sin(Number arg1) {Catchers.catchNullFromArgList(arg1, 1);
OGArray<? extends Number> arg1rewrite;
if (arg1.getClass() == ComplexType.class) {
arg1rewrite = new OGComplexScalar(arg1);
} else {
arg1rewrite = new OGRealScalar(arg1.doubleValue());
}
int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1rewrite.getClass());
OGArray<? extends Number> tmp = s_unaryFunctionChainRunner.dispatch(s_sinInstructions[type1], arg1rewrite);
return tmp;
}


public static OGArray<? extends Number>rdivide(OGArray<? extends Number> arg1, OGArray<? extends Number> arg2) {
  Catchers.catchNullFromArgList(arg1, 1);
  Catchers.catchNullFromArgList(arg2, 2);
  int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1.getClass());
  int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg2.getClass());
  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_rdivideInstructions[type1][type2], arg1, arg2);
  return tmp;
}

public static OGArray<? extends Number> rdivide(Number arg1, OGArray<? extends Number> arg2) {
  Catchers.catchNullFromArgList(arg1, 1);
  Catchers.catchNullFromArgList(arg2, 2);
OGArray<? extends Number> arg1rewrite;
if (arg1.getClass() == ComplexType.class) {
arg1rewrite = new OGComplexScalar(arg1);
} else {
arg1rewrite = new OGRealScalar(arg1.doubleValue());
}
int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1rewrite.getClass());
int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg2.getClass());
  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_rdivideInstructions[type1][type2], arg1rewrite, arg2);
  return tmp;
}

public static OGArray<? extends Number>rdivide(OGArray<? extends Number> arg1, Number arg2) {
  Catchers.catchNullFromArgList(arg1, 1);
  Catchers.catchNullFromArgList(arg2, 2);
OGArray<? extends Number> arg2rewrite;
if (arg2.getClass() == ComplexType.class) {
arg2rewrite = new OGComplexScalar(arg2);
} else {
 arg2rewrite = new OGRealScalar(arg2.doubleValue());
 }
 int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1.getClass());
 int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg2rewrite.getClass());
  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_rdivideInstructions[type1][type2], arg1, arg2rewrite);
  return tmp;
}


public static OGArray<? extends Number>plus(OGArray<? extends Number> arg1, OGArray<? extends Number> arg2) {
  Catchers.catchNullFromArgList(arg1, 1);
  Catchers.catchNullFromArgList(arg2, 2);
  int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1.getClass());
  int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg2.getClass());
  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_plusInstructions[type1][type2], arg1, arg2);
  return tmp;
}

public static OGArray<? extends Number> plus(Number arg1, OGArray<? extends Number> arg2) {
  Catchers.catchNullFromArgList(arg1, 1);
  Catchers.catchNullFromArgList(arg2, 2);
OGArray<? extends Number> arg1rewrite;
if (arg1.getClass() == ComplexType.class) {
arg1rewrite = new OGComplexScalar(arg1);
} else {
arg1rewrite = new OGRealScalar(arg1.doubleValue());
}
int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1rewrite.getClass());
int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg2.getClass());
  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_plusInstructions[type1][type2], arg1rewrite, arg2);
  return tmp;
}

public static OGArray<? extends Number>plus(OGArray<? extends Number> arg1, Number arg2) {
  Catchers.catchNullFromArgList(arg1, 1);
  Catchers.catchNullFromArgList(arg2, 2);
OGArray<? extends Number> arg2rewrite;
if (arg2.getClass() == ComplexType.class) {
arg2rewrite = new OGComplexScalar(arg2);
} else {
 arg2rewrite = new OGRealScalar(arg2.doubleValue());
 }
 int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1.getClass());
 int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg2rewrite.getClass());
  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_plusInstructions[type1][type2], arg1, arg2rewrite);
  return tmp;
}


}
