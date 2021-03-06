/**
 * Copyright 2017 Pivotal Software, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micrometer.core.instrument.binder.cache;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import io.micrometer.core.instrument.Statistic;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class HazelcastCacheMetricsTest {
    @Test
    void cacheMetrics() {
        Config config = new Config();
        HazelcastInstance h = Hazelcast.newHazelcastInstance(config);
        IMap<String, String> map = h.getMap("my-distributed-map");

        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        HazelcastCacheMetrics.monitor(registry, map, "cache",emptyList());

        map.put("key", "value");
        map.get("key");

        assertThat(registry.find("cache.gets").value(Statistic.Count, 1.0).meter()).isPresent();
        assertThat(registry.find("cache.puts").value(Statistic.Count, 1.0).meter()).isPresent();
    }
}