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

/**
 *
 */
public class ClassPathConfigtoreFactory implements ConfigStoreFactory {

  @Override
  public String name() {
    return "classpath";
  }

  @Override
  public ConfigStore create(Vertx vertx, JsonObject configuration) {
    String path = configuration.getString("path");
    if (path == null) {
      throw new IllegalArgumentException("The `path` configuration is required.");
    }
    return new ClassPathConfigStore(vertx, path);
  }
}
