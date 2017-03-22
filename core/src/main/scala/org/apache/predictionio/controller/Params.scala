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


<<<<<<< c4f6b19b59eab99bd02be1bf38f7c2fd7c9d2b3a:core/src/main/scala/org/apache/predictionio/controller/Params.scala
package org.apache.predictionio.controller

/** Base trait for all kinds of parameters that will be passed to constructors
  * of different controller classes.
  *
  * @group Helper
  */
trait Params extends Serializable {}

/** A concrete implementation of [[Params]] representing empty parameters.
  *
  * @group Helper
  */
case class EmptyParams() extends Params {
  override def toString(): String = "Empty"
=======
object PIOBuild extends Build {
  val elasticsearchVersion = SettingKey[String](
    "elasticsearch-version",
    "The version of Elasticsearch used for building.")
  val elasticsearchForTestsVersion = SettingKey[String](
    "elasticsearch-test-version",
    "The version of embedded Elasticsearch used for testing core."
  )
  val json4sVersion = SettingKey[String](
    "json4s-version",
    "The version of JSON4S used for building.")
  val sparkVersion = SettingKey[String](
    "spark-version",
    "The version of Apache Spark used for building.")
  val childrenPomExtra = SettingKey[scala.xml.NodeSeq](
    "children-pom-extra",
    "Extra POM data for children projects.")
>>>>>>> Testing merge:project/Build.scala
}
