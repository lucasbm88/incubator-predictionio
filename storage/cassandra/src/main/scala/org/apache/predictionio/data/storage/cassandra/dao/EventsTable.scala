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

package org.apache.predictionio.data.storage.cassandra.dao

import java.util.UUID

import com.outworkers.phantom.dsl._

abstract class EventsTable extends Table[EventsTable, EventsType] {

  object id extends StringColumn with PartitionKey
  object event extends StringColumn
  object entityType extends StringColumn
  object entityId extends StringColumn
  object targetEntityType extends OptionalStringColumn
  object targetEntityId extends OptionalStringColumn
  object properties extends OptionalStringColumn
  object eventTime extends DateTimeColumn
  object eventTimeZone extends StringColumn
  object tags extends OptionalStringColumn
  object prId extends OptionalStringColumn
  object creationTime extends DateTimeColumn
  object creationTimeZone extends StringColumn
}

case class EventsType(
   id: String,
   event: String,
   entityType: String,
   entityId: String,
   targetEntityType: Option[String],
   targetEntityId: Option[String],
   properties: Option[String],
   eventTime: DateTime,
   eventTimeZone: String,
   tags: Option[String],
   prId: Option[String],
   creationTime: DateTime,
   creationTimeZone: String
 )
