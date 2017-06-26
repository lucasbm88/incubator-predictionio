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

import org.scalatest.{Matchers, WordSpec}

class CDUtilSpec extends WordSpec with Matchers with TestEvents{

  "tablesPrefix method" should {
    "generate a valid table prefix with channel" in {
      val result = CDUtil.tablesPrefix("nstest", 1, Some(2))
      result shouldEqual "nstest_1_2"
    }

    "generate a valid table prefix without channel" in {
      val resultNoChannelId = CDUtil.tablesPrefix("nstestnochan", 3, Option.empty)
      resultNoChannelId shouldEqual "nstestnochan_3"
    }
  }

  "generateUniqueUUID method" should {
    "not generate the same ID over time" in {
      val generatedId = CDUtil.generateUniqueID("13", "test")
      // ensure they will not be executed at the same ms
      Thread.sleep(100)
      val lateGeneratedId = CDUtil.generateUniqueID("13", "test")
      generatedId shouldNot be(lateGeneratedId)
    }
  }

  "convertToEventsType method" should {
    "convert a Event to EventsType" in {
      val result = CDUtil.convertToEventsType(completeEvent)
      result.id shouldEqual "1"
      result.event shouldEqual "eventtest"
      result.entityId shouldEqual "2"
      result.entityType shouldEqual "typetest"
      result.targetEntityId.get shouldEqual "3"
      result.targetEntityType.get shouldEqual "targettypetest"
      result.properties.get shouldEqual """{"a":1,"b":"value2","d":[1,2,3]}"""
      result.eventTime shouldEqual aEventTime
      result.eventTimeZone shouldEqual aEventTime.getZone.getID
      result.tags.get shouldEqual "testTag,testTag2"
      result.prId.get shouldEqual "prIdTest"
      result.creationTime shouldEqual aEventTime
      result.creationTimeZone shouldEqual aEventTime.getZone.getID
    }

    "convert a Event to EventType with only required fields" in {
      val result = CDUtil.convertToEventsType(eventNoOptions)
      result.id.length shouldEqual 36
      result.event shouldEqual "neventtest"
      result.entityId shouldEqual "4"
      result.entityType shouldEqual "ntypetest"
      result.eventTime shouldEqual aEventTime
      result.eventTimeZone shouldEqual aEventTime.getZone.getID
      result.creationTime shouldEqual aEventTime
      result.creationTimeZone shouldEqual aEventTime.getZone.getID
    }
  }

}
