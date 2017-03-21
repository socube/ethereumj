/*
 * Copyright (c) [2016] [ <ether.camp> ]
 * This file is part of the ethereumJ library.
 *
 * The ethereumJ library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ethereumJ library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the ethereumJ library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.ethereum.datasource;

/**
 * Abstract cache implementation which tracks the cache size with
 * supplied key and value MemSizeEstimator's
 *
 * Created by Anton Nashatyrev on 01.12.2016.
 */
public abstract class AbstractCachedSource <Key, Value>
        extends AbstractChainedSource<Key, Value, Key, Value>
        implements CachedSource<Key, Value> {

    private MemSizeEstimator<Key> keySizeEstimator;
    private MemSizeEstimator<Value> valueSizeEstimator;
    private int size = 0;

    public AbstractCachedSource(Source<Key, Value> source) {
        super(source);
    }

    /**
     * Needs to be called by the implementation when cache entry is added
     * Only new entries should be accounted for accurate size tracking
     * If the value for the key is changed the {@link #cacheRemoved}
     * needs to be called first
     */
    protected void cacheAdded(Key key, Value value) {
        if (keySizeEstimator != null) {
            size += keySizeEstimator.estimateSize(key);
        }
        if (valueSizeEstimator != null) {
            size += valueSizeEstimator.estimateSize(value);
        }
    }

    /**
     * Needs to be called by the implementation when cache entry is removed
     */
    protected void cacheRemoved(Key key, Value value) {
        if (keySizeEstimator != null) {
            size -= keySizeEstimator.estimateSize(key);
        }
        if (valueSizeEstimator != null) {
            size -= valueSizeEstimator.estimateSize(value);
        }
    }

    /**
     * Needs to be called by the implementation when cache is cleared
     */
    protected void cacheCleared() {
        size = 0;
    }

    /**
     * Sets the key/value size estimators
     */
    public AbstractCachedSource <Key, Value> withSizeEstimators(MemSizeEstimator<Key> keySizeEstimator, MemSizeEstimator<Value> valueSizeEstimator) {
        this.keySizeEstimator = keySizeEstimator;
        this.valueSizeEstimator = valueSizeEstimator;
        return this;
    }

    @Override
    public long estimateCacheSize() {
        return size;
    }
}