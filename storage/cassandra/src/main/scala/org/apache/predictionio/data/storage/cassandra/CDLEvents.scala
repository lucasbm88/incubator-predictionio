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

import grizzled.slf4j.Logging
import org.apache.predictionio.data.storage.Event
import org.apache.predictionio.data.storage.LEvents
import org.apache.predictionio.data.storage.StorageClientConfig
import com.outworkers.phantom.dsl._
import org.apache.predictionio.annotation.DeveloperApi
import org.apache.predictionio.data.storage.cassandra.dao.EventsDatabase

import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class CDLEvents(val client: CassandraConnection, config: StorageClientConfig, val namespace: String)
  extends LEvents with Logging {

  override
  def init(appId: Int, channelId: Option[Int] = None): Boolean = {

    val tablePrefix = CDUtil.tablesPrefix(namespace, appId, channelId)
    val database = new EventsDatabase(client, tablePrefix)

    true
  }

  @DeveloperApi
  override def futureInsert(event: Event, appId: Int, channelId: Option[Int])
                           (implicit ec: ExecutionContext): Future[String] = ???


  /* override
  def remove(appId: Int, channelId: Option[Int] = None): Boolean = {
    val tableName = TableName.valueOf(HBEventsUtil.tableName(namespace, appId, channelId))
    try {
      if (client.admin.tableExists(tableName)) {
        info(s"Removing table ${tableName.getNameAsString()}...")
        client.admin.disableTable(tableName)
        client.admin.deleteTable(tableName)
      } else {
        info(s"Table ${tableName.getNameAsString()} doesn't exist." +
          s" Nothing is deleted.")
      }
      true
    } catch {
      case e: Exception => {
        error(s"Fail to remove table for appId ${appId}. Exception: ${e}")
        false
      }
    }
  } */

  /* override
  def close(): Unit = {
     client.admin.close()
    client.connection.close()
  } */

  /* override
  def futureInsert(
    event: Event, appId: Int, channelId: Option[Int])(implicit ec: ExecutionContext):
    Future[String] = {
    Future {
      val table = getTable(appId, channelId)
      val (put, rowKey) = HBEventsUtil.eventToPut(event, appId)
      table.put(put)
      table.flushCommits()
      table.close()
      rowKey.toString
    }
  }
 */
  /* override
  def futureInsertBatch(
    events: Seq[Event], appId: Int, channelId: Option[Int])(implicit ec: ExecutionContext):
    Future[Seq[String]] = {
    Future {
      val table = getTable(appId, channelId)
      val (puts, rowKeys) = events.map { event => HBEventsUtil.eventToPut(event, appId) }.unzip
      table.put(puts)
      table.flushCommits()
      table.close()
      rowKeys.map(_.toString)
    }
  }

  override
  def futureGet(
    eventId: String, appId: Int, channelId: Option[Int])(implicit ec: ExecutionContext):
    Future[Option[Event]] = {
      Future {
        val table = getTable(appId, channelId)
        val rowKey = RowKey(eventId)
        val get = new Get(rowKey.toBytes)

        val result = table.get(get)
        table.close()

        if (!result.isEmpty()) {
          val event = resultToEvent(result, appId)
          Some(event)
        } else {
          None
        }
      }
    }

  override
  def futureDelete(
    eventId: String, appId: Int, channelId: Option[Int])(implicit ec: ExecutionContext):
    Future[Boolean] = {
    Future {
      val table = getTable(appId, channelId)
      val rowKey = RowKey(eventId)
      val exists = table.exists(new Get(rowKey.toBytes))
      table.delete(new Delete(rowKey.toBytes))
      table.close()
      exists
    }
  }

  override
  def futureFind(
    appId: Int,
    channelId: Option[Int] = None,
    startTime: Option[DateTime] = None,
    untilTime: Option[DateTime] = None,
    entityType: Option[String] = None,
    entityId: Option[String] = None,
    eventNames: Option[Seq[String]] = None,
    targetEntityType: Option[Option[String]] = None,
    targetEntityId: Option[Option[String]] = None,
    limit: Option[Int] = None,
    reversed: Option[Boolean] = None)(implicit ec: ExecutionContext):
    Future[Iterator[Event]] = {
      Future {

        require(!((reversed == Some(true)) && (entityType.isEmpty || entityId.isEmpty)),
          "the parameter reversed can only be used with both entityType and entityId specified.")

        val table = getTable(appId, channelId)

        val scan = HBEventsUtil.createScan(
          startTime = startTime,
          untilTime = untilTime,
          entityType = entityType,
          entityId = entityId,
          eventNames = eventNames,
          targetEntityType = targetEntityType,
          targetEntityId = targetEntityId,
          reversed = reversed)
        val scanner = table.getScanner(scan)
        table.close()

        val eventsIter = scanner.iterator()

        // Get all events if None or Some(-1)
        val results: Iterator[Result] = limit match {
          case Some(-1) => eventsIter
          case None => eventsIter
          case Some(x) => eventsIter.take(x)
        }

        val eventsIt = results.map { resultToEvent(_, appId) }

        eventsIt
      }
  } */
  @DeveloperApi
  override def remove(appId: Int, channelId: Option[Int]): Boolean = ???

  @DeveloperApi
  override def close(): Unit = ???

  @DeveloperApi
  override def futureGet(eventId: String, appId: Int, channelId: Option[Int])
                        (implicit ec: ExecutionContext): Future[Option[Event]] = ???

  @DeveloperApi
  override def futureDelete(eventId: String, appId: Int, channelId: Option[Int])
                           (implicit ec: ExecutionContext): Future[Boolean] = ???

  @DeveloperApi
  override def futureFind(appId: Int, channelId: Option[Int],
      startTime: Option[DateTime], untilTime: Option[DateTime], entityType: Option[String],
      entityId: Option[String], eventNames: Option[Seq[String]],
      targetEntityType: Option[Option[String]], targetEntityId: Option[Option[String]],
      limit: Option[Int], reversed: Option[Boolean])
     (implicit ec: ExecutionContext): Future[Iterator[Event]] = ???
}
