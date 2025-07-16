package com.http.dynamic.stub.store

import org.slf4j.LoggerFactory

import java.util.concurrent.ConcurrentHashMap
import scala.jdk.CollectionConverters.IterableHasAsScala

class SimpleInMemoryStore[I, D] {

  private val logger = LoggerFactory.getLogger(getClass)
  private val cache = new ConcurrentHashMap[I, D]()

  def reset(): Unit = cache.clear()

  def allValues: Seq[D] = cache.values().asScala.toList

  def get(id: I): Option[D] = Option(cache.get(id))

  def store(id: I, data: D): Unit =
    cache.put(id, data)
    logStoreInteraction(id, s"Stored $data")

  def delete(id: I): Option[D] =
    get(id) match {
      case Some(d) =>
        val rd = cache.remove(id)
        logStoreInteraction(id, s"Removed $rd")
        Some(d)
      case _ => None
    }

  private def logStoreInteraction(id: I, operation: String): Unit = {
    logger.info("{}. ID[{}].", operation, id)
    logger.info("Cache contains {}", cache)
  }
}
