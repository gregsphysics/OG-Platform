/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.engine.marketdata;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Sets;
import com.opengamma.engine.ComputationTargetSpecification;
import com.opengamma.engine.marketdata.availability.MarketDataAvailabilityFilter;
import com.opengamma.engine.marketdata.availability.MarketDataAvailabilityProvider;
import com.opengamma.engine.marketdata.availability.MarketDataNotSatisfiableException;
import com.opengamma.engine.marketdata.availability.UnionMarketDataAvailability;
import com.opengamma.engine.marketdata.spec.CombinedMarketDataSpecification;
import com.opengamma.engine.marketdata.spec.MarketDataSpecification;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValuePropertyNames;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.livedata.UserPrincipal;
import com.opengamma.util.tuple.Pair;

/**
 * Implementation of {@link MarketDataProvider} which sources its data from one of two {@link MarketDataProvider}s, choosing based on the availability of data.
 */
public class CombinedMarketDataProvider extends AbstractMarketDataProvider {

  private static final String PREFERRED_PROVIDER = "Preferred";
  private static final String FALLBACK_PROVIDER = "Fallback";

  private final MarketDataProvider _preferred;
  private final MarketDataProvider _fallBack;

  private final MarketDataListener _listener = new MarketDataListener() {

    @Override
    public void subscriptionSucceeded(final ValueSpecification specification) {
      CombinedMarketDataProvider.this.subscriptionSucceeded(specification);
    }

    @Override
    public void subscriptionFailed(final ValueSpecification specification, final String msg) {
      CombinedMarketDataProvider.this.subscriptionFailed(specification, msg);
    }

    @Override
    public void subscriptionStopped(final ValueSpecification specification) {
      CombinedMarketDataProvider.this.subscriptionStopped(specification);
    }

    @Override
    public void valuesChanged(final Collection<ValueSpecification> specifications) {
      CombinedMarketDataProvider.this.valuesChanged(specifications);
    }

  };

  private final MarketDataAvailabilityProvider _availabilityProvider;

  private boolean _listenerAttached;

  public CombinedMarketDataProvider(final MarketDataProvider preferred, final MarketDataProvider fallBack) {
    _preferred = preferred;
    _fallBack = fallBack;
    _availabilityProvider = buildAvailabilityProvider();
  }

  @Override
  public void addListener(final MarketDataListener listener) {
    super.addListener(listener);
    checkListenerAttach();
  }

  @Override
  public void removeListener(final MarketDataListener listener) {
    super.removeListener(listener);
    checkListenerAttach();
  }

  private void checkListenerAttach() {
    //TODO: dedupe with CombinedMarketDataProvider
    synchronized (_listener) {
      final boolean anyListeners = getListeners().size() > 0;
      if (anyListeners && !_listenerAttached) {
        _preferred.addListener(_listener);
        _fallBack.addListener(_listener);
        _listenerAttached = true;
      } else if (!anyListeners && _listenerAttached) {
        _preferred.removeListener(_listener);
        _fallBack.removeListener(_listener);
        _listenerAttached = false;
      }
    }
  }

  @Override
  public MarketDataAvailabilityProvider getAvailabilityProvider() {
    return _availabilityProvider;
  }

  private ValueSpecification createValueSpecification(final ValueSpecification underlying, final String provider) {
    final ValueProperties.Builder properties = underlying.getProperties().copy();
    final String dataProvider = underlying.getProperty(ValuePropertyNames.DATA_PROVIDER);
    if (dataProvider != null) {
      properties.withoutAny(ValuePropertyNames.DATA_PROVIDER).with(ValuePropertyNames.DATA_PROVIDER, dataProvider + "/" + provider);
    } else {
      properties.with(ValuePropertyNames.DATA_PROVIDER, provider);
    }
    return new ValueSpecification(underlying.getValueName(), underlying.getTargetSpecification(), properties.get());
  }

  /**
   * Creates an availability provider that combines results from the two providers. The returned specification is either:
   * <ul>
   * <li>The value specification from the preferred provider
   * <li>The value specification from the non-preferred provider
   * <li>Null if either provider returned null
   * <li>{@link MarketDataNotSatisfiableException} being thrown if both providers do so
   * </ul>
   */
  private MarketDataAvailabilityProvider buildAvailabilityProvider() {
    return new MarketDataAvailabilityProvider() {

      private final MarketDataAvailabilityProvider _preferredProvider = _preferred.getAvailabilityProvider();
      private final MarketDataAvailabilityProvider _fallbackProvider = _fallBack.getAvailabilityProvider();

      @Override
      public ValueSpecification getAvailability(final ComputationTargetSpecification targetSpec, final Object target, final ValueRequirement desiredValue) {
        MarketDataNotSatisfiableException preferredMissing = null;
        try {
          final ValueSpecification preferred = _preferredProvider.getAvailability(targetSpec, target, desiredValue);
          if (preferred != null) {
            // preferred is available
            return createValueSpecification(preferred, PREFERRED_PROVIDER);
          }
        } catch (final MarketDataNotSatisfiableException e) {
          preferredMissing = e;
        }
        try {
          final ValueSpecification fallback = _fallbackProvider.getAvailability(targetSpec, target, desiredValue);
          if (fallback != null) {
            // fallback is available
            return createValueSpecification(fallback, FALLBACK_PROVIDER);
          }
        } catch (final MarketDataNotSatisfiableException e) {
          if (preferredMissing != null) {
            // both are not available - use preferred
            throw preferredMissing;
          } else {
            // fallback is not available
            throw e;
          }
        }
        // preferred is either not available or missing
        if (preferredMissing != null) {
          throw preferredMissing;
        } else {
          return null;
        }
      }

      @Override
      public MarketDataAvailabilityFilter getAvailabilityFilter() {
        return new UnionMarketDataAvailability.Filter(Arrays.asList(_preferredProvider.getAvailabilityFilter(), _fallbackProvider.getAvailabilityFilter()));
      }

    };
  }

  @Override
  public MarketDataPermissionProvider getPermissionProvider() {
    return new MarketDataPermissionProvider() {

      @Override
      public Set<ValueSpecification> checkMarketDataPermissions(final UserPrincipal user, final Set<ValueSpecification> specifications) {
        final Map<MarketDataProvider, Set<ValueSpecification>> specsByProvider = getProviders(specifications);
        final Set<ValueSpecification> failures = Sets.newHashSet();
        for (final Entry<MarketDataProvider, Set<ValueSpecification>> entry : specsByProvider.entrySet()) {
          final MarketDataPermissionProvider permissionProvider = entry.getKey().getPermissionProvider();
          final Set<ValueSpecification> failed = permissionProvider.checkMarketDataPermissions(user, entry.getValue());
          failures.addAll(failed);
        }
        return failures;
      }
    };
  }

  @Override
  public void subscribe(final ValueSpecification valueSpecification) {
    subscribe(Collections.singleton(valueSpecification));
  }

  @Override
  public void subscribe(final Set<ValueSpecification> valueSpecification) {
    final Map<MarketDataProvider, Set<ValueSpecification>> specificationsByProvider = getProviders(valueSpecification);
    for (final Entry<MarketDataProvider, Set<ValueSpecification>> entry : specificationsByProvider.entrySet()) {
      entry.getKey().subscribe(entry.getValue());
    }
  }

  @Override
  public void unsubscribe(final ValueSpecification valueSpecification) {
    unsubscribe(Collections.singleton(valueSpecification));
  }

  @Override
  public void unsubscribe(final Set<ValueSpecification> valueSpecifications) {
    final Map<MarketDataProvider, Set<ValueSpecification>> specificationsByProvider = getProviders(valueSpecifications);
    for (final Entry<MarketDataProvider, Set<ValueSpecification>> entry : specificationsByProvider.entrySet()) {
      entry.getKey().unsubscribe(entry.getValue());
    }
  }

  @Override
  public MarketDataSnapshot snapshot(final MarketDataSpecification marketDataSpec) {
    final CombinedMarketDataSpecification combinedSpec = (CombinedMarketDataSpecification) marketDataSpec;
    final Map<MarketDataProvider, MarketDataSnapshot> snapByProvider = new HashMap<MarketDataProvider, MarketDataSnapshot>();
    snapByProvider.put(_preferred, _preferred.snapshot(combinedSpec.getPreferredSpecification()));
    snapByProvider.put(_fallBack, _fallBack.snapshot(combinedSpec.getFallbackSpecification()));
    final MarketDataSnapshot preferredSnap = snapByProvider.get(_preferred);
    return new CombinedMarketDataSnapshot(preferredSnap, snapByProvider, this);
  }

  @Override
  public boolean isCompatible(final MarketDataSpecification marketDataSpec) {
    if (!(marketDataSpec instanceof CombinedMarketDataSpecification)) {
      return false;
    }
    final CombinedMarketDataSpecification combinedMarketDataSpec = (CombinedMarketDataSpecification) marketDataSpec;
    return _preferred.isCompatible(combinedMarketDataSpec.getPreferredSpecification())
        && _fallBack.isCompatible(combinedMarketDataSpec.getFallbackSpecification());
  }

  private MarketDataProvider getDataProvider(final ValueSpecification specification, final ValueProperties.Builder underlyingProperties) {
    String dataProvider = specification.getProperty(ValuePropertyNames.DATA_PROVIDER);
    if (dataProvider == null) {
      throw new IllegalArgumentException("Don't know how to provide " + specification);
    }
    underlyingProperties.withoutAny(ValuePropertyNames.DATA_PROVIDER);
    final int slash = dataProvider.lastIndexOf('/');
    if (slash > 0) {
      underlyingProperties.with(ValuePropertyNames.DATA_PROVIDER, dataProvider.substring(0, slash));
      dataProvider = dataProvider.substring(slash + 1);
    }
    if (PREFERRED_PROVIDER.equals(dataProvider)) {
      return _preferred;
    } else if (FALLBACK_PROVIDER.equals(dataProvider)) {
      return _fallBack;
    } else {
      throw new IllegalArgumentException("Don't know how to provide " + specification);
    }
  }

  public Map<MarketDataProvider, Set<ValueSpecification>> getProviders(final Collection<ValueSpecification> specifications) {
    final Map<MarketDataProvider, Set<ValueSpecification>> result = new HashMap<MarketDataProvider, Set<ValueSpecification>>();
    for (final ValueSpecification specification : specifications) {
      final ValueProperties.Builder underlyingProperties = specification.getProperties().copy();
      final MarketDataProvider provider = getDataProvider(specification, underlyingProperties);
      Set<ValueSpecification> set = result.get(provider);
      if (set == null) {
        set = new HashSet<ValueSpecification>();
        result.put(provider, set);
      }
      set.add(new ValueSpecification(specification.getValueName(), specification.getTargetSpecification(), underlyingProperties.get()));
    }
    return result;
  }

  protected Pair<MarketDataProvider, ValueSpecification> getProvider(final ValueSpecification specification) {
    final ValueProperties.Builder underlyingProperties = specification.getProperties().copy();
    final MarketDataProvider provider = getDataProvider(specification, underlyingProperties);
    return Pair.of(provider, new ValueSpecification(specification.getValueName(), specification.getTargetSpecification(), underlyingProperties.get()));
  }

}
