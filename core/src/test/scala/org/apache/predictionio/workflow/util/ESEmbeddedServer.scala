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

package org.apache.predictionio.workflow.util

import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.node.Node
import org.elasticsearch.node.NodeBuilder.nodeBuilder

object ESEmbeddedServer {

  val DEFAULT_DATA_DIR = "target/es-data"

  var node: Option[Node] = Option.empty[Node]

  def start: Unit = {

    val esSettings: ImmutableSettings.Builder =
      ImmutableSettings
        .settingsBuilder
        .put("http.enabled", "true")
        .put("http.port", "9301")
        .put("path.data", DEFAULT_DATA_DIR)

    node = Some(
      nodeBuilder
        .local(false)
        .data(true)
        .settings(esSettings.build)
        .clusterName("elasticsearch")
        .node
    )
  }

  def shutdown: Unit = {
    if(node.isDefined) node.get.close
  }
}
