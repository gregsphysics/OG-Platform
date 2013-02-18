/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.convention;

import java.util.ArrayList;
import java.util.Collection;

import com.opengamma.id.UniqueId;

/**
 * 
 */
public class InMemoryConventionMaster implements ConventionMaster {
  private final ExternalIdBundleMapper<Convention> _mapper = new ExternalIdBundleMapper<>(InMemoryConventionBundleMaster.IN_MEMORY_UNIQUE_SCHEME.getName());

  /**
   * 
   */
  public InMemoryConventionMaster() {
  }

  @Override
  public ConventionSearchResult searchConvention(final ConventionSearchRequest searchRequest) {
    final Collection<Convention> collection = _mapper.get(searchRequest.getIdentifiers());
    return new ConventionSearchResult(wrapConventionsWithDocuments(collection));
  }

  @Override
  public ConventionSearchResult searchHistoricalConvention(final ConventionSearchHistoricRequest searchRequest) {
    final Collection<Convention> collection = _mapper.get(searchRequest.getIdentifiers());
    return new ConventionSearchResult(wrapConventionsWithDocuments(collection));
  }

  @Override
  public ConventionDocument getConvention(final UniqueId uniqueId) {
    return new ConventionDocument(_mapper.get(uniqueId));
  }

  @Override
  public UniqueId add(final Convention convention) {
    final UniqueId uid = _mapper.add(convention.getExternalIdBundle(), convention);
    convention.setUniqueId(uid);
    return uid;
  }

  private Collection<ConventionDocument> wrapConventionsWithDocuments(final Collection<Convention> collection) {
    final Collection<ConventionDocument> results = new ArrayList<>(collection.size());
    for (final Convention convention : collection) {
      results.add(new ConventionDocument(convention));
    }
    return results;
  }
}