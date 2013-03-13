/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.engine.target;

import java.io.ObjectStreamException;
import java.io.Serializable;

import com.opengamma.core.position.Portfolio;
import com.opengamma.core.position.PortfolioNode;
import com.opengamma.core.position.Position;
import com.opengamma.core.position.PositionOrTrade;
import com.opengamma.core.position.Trade;
import com.opengamma.core.security.Security;
import com.opengamma.engine.fudgemsg.ComputationTargetTypeFudgeBuilder;
import com.opengamma.engine.target.resolver.CurrencyResolver;
import com.opengamma.engine.target.resolver.ObjectResolver;
import com.opengamma.engine.target.resolver.PrimitiveResolver;
import com.opengamma.engine.target.resolver.UnorderedCurrencyPairResolver;
import com.opengamma.id.UniqueIdentifiable;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.PublicAPI;
import com.opengamma.util.WeakInstanceCache;
import com.opengamma.util.money.Currency;
import com.opengamma.util.money.UnorderedCurrencyPair;

/**
 * The type of the target a computation step will act on or a data sourcing function will provide.
 */
@PublicAPI
public abstract class ComputationTargetType implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final WeakInstanceCache<ComputationTargetType> s_computationTargetTypes = new WeakInstanceCache<ComputationTargetType>();

  /**
   * A full portfolio structure. This will seldom be needed for calculations - the root node is usually more important from an aggregation perspective.
   */
  public static final ObjectComputationTargetType<Portfolio> PORTFOLIO = defaultObject(Portfolio.class, "PORTFOLIO");
  /**
   * An ordered list of positions and other portfolio nodes.
   */
  public static final ObjectComputationTargetType<PortfolioNode> PORTFOLIO_NODE = defaultObject(PortfolioNode.class, "PORTFOLIO_NODE");

  /**
   * A position.
   */
  public static final ObjectComputationTargetType<Position> POSITION = defaultObject(Position.class, "POSITION");

  /**
   * A security.
   */
  public static final ObjectComputationTargetType<Security> SECURITY = defaultObject(Security.class, "SECURITY");

  /**
   * A trade.
   */
  public static final ObjectComputationTargetType<Trade> TRADE = defaultObject(Trade.class, "TRADE");

  /**
   * A simple type, for trivial items for which a unique ID (which can just be an arbitrary string triple if scheme, value and version used) that does not need resolving is sufficient.
   */
  public static final PrimitiveComputationTargetType<Primitive> PRIMITIVE = defaultPrimitive(Primitive.class, "PRIMITIVE", new PrimitiveResolver());

  /**
   * A currency.
   */
  public static final PrimitiveComputationTargetType<Currency> CURRENCY = defaultPrimitive(Currency.class, "CURRENCY", new CurrencyResolver());

  /**
   * An unordered currency pair.
   */
  public static final PrimitiveComputationTargetType<UnorderedCurrencyPair> UNORDERED_CURRENCY_PAIR = defaultPrimitive(UnorderedCurrencyPair.class, "UNORDERED_CURRENCY_PAIR",
      new UnorderedCurrencyPairResolver());

  /**
   * A wildcard type. This may be used when declaring the target type of a function. It should not be used as part of a target reference as the lack of specific type details will prevent a resolver
   * from producing the concrete target object.
   */
  public static final ObjectComputationTargetType<UniqueIdentifiable> ANYTHING = defaultObject(UniqueIdentifiable.class, "ANYTHING");

  /**
   * A position or a trade object. This is a union type for an object that is either an instance of {@link Position}, {@link Trade}, or both. This may be used when declaring the target type of a
   * function. It should not normally be used as part of a target reference as the resolver will have to try multiple resolution strategies to determine the concrete instance.
   */
  public static final ObjectComputationTargetType<PositionOrTrade> POSITION_OR_TRADE = ObjectComputationTargetType.of(POSITION.or(TRADE), PositionOrTrade.class);

  /**
   * An explicit null, for the anonymous target.
   */
  public static final ComputationTargetType NULL = new NullComputationTargetType();

  /**
   * An equivalent to the previous use of {@code PRIMITIVE}. It means primitives in their new sense, plus currencies and unordered currency pairs that now have explicit types.
   * 
   * @deprecated This is not particularly efficient to use and may not be correct, but is better than using {@link #ANYTHING}. It will be removed at the first opportunity.
   */
  @Deprecated
  public static final ComputationTargetType LEGACY_PRIMITIVE = PRIMITIVE.or(CURRENCY).or(UNORDERED_CURRENCY_PAIR);

  /* package */ComputationTargetType() {
  }

  private static <T extends UniqueIdentifiable> ComputationTargetType defaultType(final Class<T> clazz, final String name) {
    return of(clazz, name, true);
  }

  private static <T extends UniqueIdentifiable> ObjectComputationTargetType<T> defaultObject(final Class<T> clazz, final String name) {
    return ObjectComputationTargetType.of(defaultType(clazz, name), clazz);
  }

  private static <T extends UniqueIdentifiable> PrimitiveComputationTargetType<T> defaultPrimitive(final Class<T> clazz, final String name, final ObjectResolver<T> resolver) {
    return PrimitiveComputationTargetType.of(defaultType(clazz, name), clazz, resolver);
  }

  private static ComputationTargetType of(final Class<? extends UniqueIdentifiable> clazz, final String name, final boolean nameWellKnown) {
    return s_computationTargetTypes.get(new ClassComputationTargetType(clazz, name, nameWellKnown));
  }

  public static <T extends UniqueIdentifiable> ComputationTargetType of(final Class<T> clazz) {
    ArgumentChecker.notNull(clazz, "clazz");
    return of(clazz, clazz.getSimpleName(), false);
  }

  public ComputationTargetType containing(final Class<? extends UniqueIdentifiable> clazz) {
    return containing(of(clazz));
  }

  public ComputationTargetType containing(final ComputationTargetType inner) {
    ArgumentChecker.notNull(inner, "inner");
    return s_computationTargetTypes.get(new NestedComputationTargetType(this, inner));
  }

  public ComputationTargetType or(final Class<? extends UniqueIdentifiable> clazz) {
    return or(of(clazz));
  }

  public ComputationTargetType or(final ComputationTargetType alternative) {
    ArgumentChecker.notNull(alternative, "alternative");
    return s_computationTargetTypes.get(new MultipleComputationTargetType(this, alternative));
  }

  /**
   * Tests if the given target object is valid for this type descriptor.
   * 
   * @param target the object to test
   * @return true if the object is compatible, false if the object is not of the descriptor's type
   */
  public abstract boolean isCompatible(UniqueIdentifiable target);

  /**
   * Tests if the given type descriptor is compatible with this type descriptor. The target class must be the same class or a subclass at each nesting level.
   * 
   * @param type the type to test
   * @return true if the type is compatible, false otherwise
   */
  public abstract boolean isCompatible(ComputationTargetType type);

  /**
   * Tests if the given target class is valid for this type descriptor.
   * 
   * @param clazz the object class to test
   * @return true if the class is compatible, false if it is not of the descriptor's type
   */
  public abstract boolean isCompatible(Class<? extends UniqueIdentifiable> clazz);

  private static final class Parser {

    private final String _buffer;
    private int _index;

    public Parser(final String str) {
      _buffer = str;
    }

    private static boolean isIdentifier(final char c) {
      return (c != '/') && (c != '|') && (c != '(') && (c != ')');
    }

    public ComputationTargetType parse() {
      ComputationTargetType type = null;
      do {
        switch (_buffer.charAt(_index)) {
          case '/':
            if (type == null) {
              throw new IllegalArgumentException("Unexpected / at index " + _index + " of " + _buffer);
            } else {
              _index++;
              type = new NestedComputationTargetType(type, parse());
              break;
            }
          case '|':
            if (type == null) {
              throw new IllegalArgumentException("Unexpected | at index " + _index + " of " + _buffer);
            } else {
              _index++;
              type = new MultipleComputationTargetType(type, parse());
              break;
            }
          case '(':
            _index++;
            if (type == null) {
              type = parse();
            } else {
              throw new IllegalArgumentException("Unexpected ( at index " + _index + " of " + _buffer);
            }
            break;
          case ')':
            if (type == null) {
              throw new IllegalArgumentException("Unexpected ) at index " + _index + " of " + _buffer);
            } else {
              _index++;
              return type;
            }
          default:
            if (type != null) {
              throw new IllegalArgumentException("Unexpected identifier at index " + _index + " of " + _buffer);
            }
            final StringBuilder sb = new StringBuilder();
            do {
              sb.append(_buffer.charAt(_index++));
            } while ((_index < _buffer.length()) && isIdentifier(_buffer.charAt(_index)));
            type = ComputationTargetTypeFudgeBuilder.fromString(sb.toString());
            break;
        }
      } while (_index < _buffer.length());
      return type;
    }

  }

  /**
   * Parses a string produced by {@link #toString}.
   * 
   * @param str the string to parse, not null
   * @return the computation target type, not null
   */
  public static ComputationTargetType parse(final String str) {
    return new Parser(str).parse();
  }

  /**
   * Apply the visitor operation to this target type.
   * 
   * @param <T> the return type of the visitor
   * @param <D> the parameter data type of the visitor
   * @param visitor the visitor to apply, not null
   * @param data the data value to pass to the visitor
   * @return the result of the visitor operation
   */
  public abstract <D, T> T accept(ComputationTargetTypeVisitor<D, T> visitor, D data);

  /**
   * Produces a string representation of the type that includes outer brackets if necessary to maintain the structure of composite types for handling by {@link #parse}.
   * 
   * @param sb the string builder to append the string representation to
   */
  protected abstract void toStringNested(StringBuilder sb);

  /**
   * Produces a string representation of the type that can be parsed by {@link #parse} and is vaguely human readable.
   * 
   * @return the string representation
   */
  @Override
  public abstract String toString();

  /**
   * Produces a string representation of the type that includes outer brackets if necessary to maintain the structure of composite types for viewing by a user.
   * 
   * @param sb the string builder to append the string representation to
   */
  protected abstract void getNameNested(StringBuilder sb);

  /**
   * Produces a string representation of the type that can be displayed to the user.
   * 
   * @return the string representation
   */
  public abstract String getName();

  @Override
  public abstract boolean equals(Object o);

  @Override
  public abstract int hashCode();

  /**
   * Tests if the leaf target type(s) matches the given type. {@code x.isTargetType(y) == y.isCompatible(x) }
   * 
   * @param type the type to test, not null
   * @return true if the leaf target type (or one of the types if there are multiple choices) matches the given type, false otherwise
   */
  public abstract boolean isTargetType(ComputationTargetType type);

  /**
   * Tests if the leaf target type(s) matches the given type.
   * 
   * @param type the type to test, not null
   * @return true if the leaf target type (or one of the types if there are multiple choices) matches the given type, false otherwise
   */
  public abstract boolean isTargetType(Class<? extends UniqueIdentifiable> type);

  protected Object readResolve() throws ObjectStreamException {
    return s_computationTargetTypes.get(this);
  }

}
