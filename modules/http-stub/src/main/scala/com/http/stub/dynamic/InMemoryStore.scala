package com.http.stub.dynamic

import org.slf4j.LoggerFactory

import java.util.concurrent.ConcurrentHashMap
import scala.jdk.CollectionConverters.IterableHasAsScala

trait HasIdentity[T] {
  val id: T
}

class TypedInMemoryStore[I, D <: HasIdentity[I]] {

  val cache = new SimpleInMemoryStore[I, D]()

  def reset() = cache.reset()

  def allValues = cache.allValues

  def get(id: I): Option[D] = cache.get(id)

  def store(data: D) = cache.store(data.id, data)

  def delete(id: I): Option[D] = cache.delete(id)
}

class SimpleInMemoryStore[I, D] {

  private val logger = LoggerFactory.getLogger(getClass)

  val cache = new ConcurrentHashMap[I, D]()

  def reset() = cache.clear()

  def allValues = cache.values().asScala.toList

  def get(id: I): Option[D] = {
    val data = cache.get(id)
    if (data == null) None else Some(data)
  }

  def store(id: I, data: D) = {
    cache.put(id, data)
    logStoreInteraction(id, s"Stored $data")
  }

  def delete(id: I): Option[D] = {
    get(id) match {
      case Some(d) =>
        val rd = cache.remove(id)
        logStoreInteraction(id, s"Removed $rd")
        Some(d)
      case _ => None
    }
  }

  private def logStoreInteraction(id: I, operation: String) = {
    logger.info("{}. ID[{}].", operation, id)
    logger.info("Cache contains {}", cache)
  }
}
