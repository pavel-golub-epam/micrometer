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
package io.micrometer.core.samples;

import cern.jet.random.Normal;
import cern.jet.random.engine.MersenneTwister64;
import cern.jet.random.engine.RandomEngine;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.samples.utils.SampleRegistries;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class CounterSample {
    public static void main(String[] args) {
        Counter counter = SampleRegistries.prometheus().counter("counter");

        RandomEngine r = new MersenneTwister64(0);
        Normal dist = new Normal(0, 1, r);

        Flux.interval(Duration.ofMillis(10))
                .doOnEach(d -> {
                    if (dist.nextDouble() + 0.1 > 0) {
                        counter.increment();
                    }
                })
                .blockLast();
    }
}