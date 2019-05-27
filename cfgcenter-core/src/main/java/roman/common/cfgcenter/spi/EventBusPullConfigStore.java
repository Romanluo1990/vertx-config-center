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
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 *
 */
public class EventBusPullConfigStore implements ConfigStore {

  private Vertx vertx;

  private String address;

  private String domain;

  public EventBusPullConfigStore(Vertx vertx, String address, String domain) {
    this.vertx = vertx;
    this.address = address;
    this.domain = domain;
  }

  @Override
  public void get(Handler<AsyncResult<Buffer>> completionHandler) {
    Handler<AsyncResult<Message<JsonObject>>> replyHandler = as -> {
      if(as.succeeded()){
        completionHandler.handle(Future.succeededFuture(as.result().body().toBuffer()));
      }else{
        completionHandler.handle(Future.failedFuture(as.cause()));
      }
    };
    vertx.eventBus().send(address, domain, replyHandler);
  }
}
