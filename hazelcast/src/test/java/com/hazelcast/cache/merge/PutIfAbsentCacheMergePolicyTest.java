/*
 * Copyright (c) 2008-2018, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.cache.merge;

import com.hazelcast.cache.CacheEntryView;
import com.hazelcast.cache.CacheMergePolicy;
import com.hazelcast.test.HazelcastParallelClassRunner;
import com.hazelcast.test.annotation.ParallelTest;
import com.hazelcast.test.annotation.QuickTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(HazelcastParallelClassRunner.class)
@Category({QuickTest.class, ParallelTest.class})
public class PutIfAbsentCacheMergePolicyTest {

    private static final String EXISTING = "EXISTING";
    private static final String MERGING = "MERGING";

    protected CacheMergePolicy policy;

    @Before
    public void setup() {
        policy = new PutIfAbsentCacheMergePolicy();
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void merge_existingValueAbsent() {
        CacheEntryView existing = null;
        CacheEntryView merging = entryWithGivenValue(MERGING);

        assertEquals(MERGING, policy.merge("map", merging, existing));
    }

    @Test
    public void merge_existingValuePresent() {
        CacheEntryView existing = entryWithGivenValue(EXISTING);
        CacheEntryView merging = entryWithGivenValue(MERGING);

        assertEquals(EXISTING, policy.merge("map", merging, existing));
    }

    @Test
    public void merge_bothValuesNull() {
        CacheEntryView existing = entryWithGivenValue(null);
        CacheEntryView merging = entryWithGivenValue(null);

        assertNull(policy.merge("map", merging, existing));
    }

    private CacheEntryView entryWithGivenValue(String value) {
        CacheEntryView entryView = mock(CacheEntryView.class);
        try {
            when(entryView.getValue()).thenReturn(value);
            return entryView;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
