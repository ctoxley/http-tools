package com.http.dynamic.stub.model

import com.http.dynamic.stub.model.PathParts.placeholder

final case class PathParts(head: String, tail: Seq[String]) {

  private val parts: Seq[String] = head +: tail

  lazy val length: Int = parts.length
  lazy val first: String = parts.head
  lazy val second: Option[String] = parts.lift(1)
  lazy val third: Option[String] = parts.lift(2)

  override def toString: String = parts.mkString("/", "/", "")

  def matches(pathParts: PathParts): Boolean =
    if (length != pathParts.length)
      false
    else
      parts.zip(pathParts.parts).foldRight(true) {
        case ((p1, p2), acc) if p1 == placeholder => acc
        case ((p1, p2), acc) if p2 == placeholder => acc
        case ((p1, p2), acc) => p1 == p2 && acc
      }
}

object PathParts {

  val placeholder: String = "*"

  def from(path: String): PathParts =
    path.split("/") match
      case Array("", head, tail*) => PathParts(head, tail)
      case _ => throw new IllegalArgumentException(s"Unable to parse path[$path]. Paths must start with a `/` and have least one part.")
}