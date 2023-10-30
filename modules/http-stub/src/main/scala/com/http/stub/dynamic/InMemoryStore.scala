package com.http.stub.dynamic

import java.util.concurrent.ConcurrentHashMap

trait HasIdentity[T] {
  val id: T
}

class InMemoryStore[I, D <: HasIdentity[I]] {

  val cache = new ConcurrentHashMap[I, D]()

  def get(id: I): Option[D] = {
    val data = cache.get(id)
    if (data == null) None else Some(data)
  }

  def store(data: D) = cache.put(data.id, data)

  def delete(id: I): Option[D] = {
    get(id) match {
      case Some(d) =>
        cache.remove(id)
        Some(d)
      case _ => None
    }
  }
}
