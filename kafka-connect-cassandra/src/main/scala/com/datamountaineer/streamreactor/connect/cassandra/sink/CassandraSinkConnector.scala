/**
  * Copyright 2016 Datamountaineer.
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  **/

package com.datamountaineer.streamreactor.connect.cassandra.sink

import java.util

import com.datamountaineer.streamreactor.connect.cassandra.config.CassandraConfigSink
import com.typesafe.scalalogging.slf4j.StrictLogging
import org.apache.kafka.common.config.ConfigDef
import org.apache.kafka.connect.connector.Task
import org.apache.kafka.connect.sink.SinkConnector

import scala.collection.JavaConverters.seqAsJavaListConverter

/**
  * <h1>CassandraSinkConnector</h1>
  * Kafka connect Cassandra Sink connector
  *
  * Sets up CassandraSinkTask and configurations for the tasks.
  * */
class CassandraSinkConnector extends SinkConnector with StrictLogging with CassandraConfigSink {
  private var configProps : Option[util.Map[String, String]] = None
  private var connConfigDef : Option[ConfigDef] = None

  /**
    * States which SinkTask class to use
    * */
  override def taskClass(): Class[_ <: Task] = classOf[CassandraSinkTask]

  /**
    * Set the configuration for each work and determine the split
    *
    * @param maxTasks The max number of task workers be can spawn
    * @return a List of configuration properties per worker
    * */
  override def taskConfigs(maxTasks: Int): util.List[util.Map[String, String]] = {
    logger.info(s"Setting task configurations for $maxTasks workers.")
    (1 to maxTasks).map(c => configProps.get).toList.asJava
  }

  /**
    * Start the sink and set to configuration
    *
    * @param props A map of properties for the connector and worker
    * */
  override def start(props: util.Map[String, String]): Unit = {
    logger.info(s"Starting Cassandra sink task with ${props.toString}.")
    configProps = Some(props)
    connConfigDef = Some(sinkConfig)
  }

  override def stop(): Unit = {}
  override def version(): String = getClass.getPackage.getImplementationVersion

  //override def config(): ConfigDef = connConfigDef.get
}
