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

import org.apache.predictionio.data.storage.{DataMap, Event}
import org.joda.time.DateTime

trait TestEvents {

  val aEventTime = new DateTime(12345)

  val completeEvent = Event(
    eventId = Some("1"),
    event = "eventtest",
    entityId = "2",
    entityType = "typetest",
    targetEntityId = Some("3"),
    targetEntityType = Some("targettypetest"),
    properties = DataMap(
      """{
        "a" : 1,
        "b" : "value2",
        "d" : [1, 2, 3],
      }"""),
    eventTime = aEventTime,
    tags = Seq("testTag", "testTag2"),
    prId = Some("prIdTest"),
    creationTime = aEventTime
  )

  val eventNoOptions = Event(
    event = "neventtest",
    entityId = "4",
    entityType = "ntypetest",
    properties = DataMap(),
    eventTime = aEventTime,
    tags = Seq(),
    creationTime = aEventTime
  )


}
