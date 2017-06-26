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

import java.util.UUID

import org.apache.predictionio.data.storage.Event
import org.apache.predictionio.data.storage.cassandra.dao.EventsType
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.{read, write}


object CDUtil {

  implicit val formats = DefaultFormats

  def tablesPrefix(namespace: String, appId: Int, channelId: Option[Int]): String =
    s"${namespace}_${appId}${channelId.map("_" + _).getOrElse("")}"

  def generateUniqueID(entityId: String, entityType: String): String = {
    val leastRandomUUID = UUID.randomUUID.getLeastSignificantBits
    val now = System.currentTimeMillis()
    val id = s"$entityId$entityType$now$leastRandomUUID"
    UUID.nameUUIDFromBytes(id.getBytes).toString
  }

  def convertToEventsType(event: Event): EventsType = {
    val id = event.eventId.map{id =>
      id
    }.getOrElse(generateUniqueID(event.entityId, event.entityType))

    EventsType(
      id = id,
      event = event.event,
      entityId = event.entityId,
      entityType = event.entityType,
      targetEntityId = event.targetEntityId,
      targetEntityType = event.targetEntityType,
      properties = Some(write(event.properties.toJObject)),
      eventTime = event.eventTime,
      eventTimeZone = event.eventTime.getZone.getID,
      tags = Some(event.tags.mkString(",")),
      prId = event.prId,
      creationTime = event.creationTime,
      creationTimeZone = event.creationTime.getZone.getID
    )
  }
}
