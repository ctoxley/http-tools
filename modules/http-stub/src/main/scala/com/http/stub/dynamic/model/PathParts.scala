package com.http.stub.dynamic.model

import com.http.stub.dynamic.model.PathParts.Param

sealed trait PathParts {

  def length: Int = this match {
    case _: PathPartsThree => 3
    case _: PathPartsTwo => 2
    case _: PathPartsOne => 1
  }

  def matchesFirst(pathPart: String): Boolean = paramOrExpected(pathPart, Some(first))

  def matchesSecond(pathPart: String): Boolean = paramOrExpected(pathPart, second)

  def matchesThird(pathPart: String): Boolean = paramOrExpected(pathPart, third)

  def first: String = this match {
    case p3: PathPartsThree => p3.p1
    case p2: PathPartsTwo => p2.p1
    case p1: PathPartsOne => p1.p1
  }

  def second: Option[String] = this match {
    case p3: PathPartsThree => Some(p3.p2)
    case p2: PathPartsTwo => Some(p2.p2)
    case _ => None
  }

  def third: Option[String] = this match {
    case p3: PathPartsThree => Some(p3.p3)
    case _ => None
  }

  private def paramOrExpected(actual: String, expected: Option[String]) = expected match {
    case Some(p) => p == Param || actual == p
    case _ => false
  }
}
case class PathPartsOne(p1: String) extends PathParts
case class PathPartsTwo(p1: String, p2: String) extends PathParts
case class PathPartsThree(p1: String, p2: String, p3: String) extends PathParts

object PathParts {

  val Param = "*"

  def from(path: String): PathParts = path.split("/") match {
      case Array(_, p1, p2, p3) => PathPartsThree(p1, p2, p3)
      case Array(_, p1, p2) => PathPartsTwo(p1, p2)
      case Array(_, p1) => PathPartsOne(p1)
      case _ => throw new IllegalArgumentException(s"Unable to parse path[$path]. Paths must start with a `/`.")
    }
}