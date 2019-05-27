/*
 * Copyright (c) 2014 Red Hat, Inc. and others
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 */

package roman.common.cfgcenter.spi;

import io.vertx.config.spi.ConfigStore;
import io.vertx.config.spi.ConfigStoreFactory;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

/**
 *
 */
public class EventBusPullConfigtoreFactory implements ConfigStoreFactory {

  public static final String NAME = "event-bus-pull";

  @Override
  public String name() {
    return NAME;
  }

  @Override
  public ConfigStore create(Vertx vertx, JsonObject configuration) {
    String address = configuration.getString("address");
    if (address == null) {
      address = name();
    }
    String domain = configuration.getString("domain");
    Objects.requireNonNull(domain);
    return new EventBusPullConfigStore(vertx, address, domain);
  }
}
