/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.apache.predictionio.data.storage.cassandra

import com.datastax.driver.core.SocketOptions
import org.apache.predictionio.data.storage.BaseStorageClient
import org.apache.predictionio.data.storage.StorageClientConfig
import grizzled.slf4j.Logging
import com.outworkers.phantom.connectors
import com.outworkers.phantom.connectors.KeySpace
import com.outworkers.phantom.dsl._

class StorageClient(val config: StorageClientConfig)
  extends BaseStorageClient with Logging {

  val connectTimeout = config.properties.get("CONNECT_TIMEOUT").getOrElse("1000")
  val readTimeout = config.properties.get("READ_TIMEOUT").getOrElse("10000")
  val hosts = config.properties.get("HOSTS")
    .map(_.split(",").toSeq).getOrElse(Seq("localhost"))
  val port = config.properties.get("PORT").getOrElse("9042")

  val client: CassandraConnection = connectors.ContactPoints(hosts, port.toInt)
    .withClusterBuilder(_.withSocketOptions(
      new SocketOptions()
        .setConnectTimeoutMillis(connectTimeout.toInt)
        .setReadTimeoutMillis(readTimeout.toInt)
    )).noHeartbeat()
    .keySpace(KeySpace("pio").ifNotExists().`with`(
      replication eqs SimpleStrategy.replication_factor(1)
    ))

  override
  val prefix = "CD"
}