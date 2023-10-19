package com.http.script

object Log {

  val errorLog = os.pwd / "target" / "error.log"
  val resultLog = os.pwd / "target" / "result.log"
  val lineSeparator = sys.props("line.separator")

  def truncate = {
    if (os.exists(errorLog)) {
      os.truncate(errorLog, 0)
    }
    if (os.exists(resultLog)) {
      os.truncate(resultLog, 0)
    }
  }

  private def error(message: String) = os.write.append(errorLog, s"$message$lineSeparator")
  private def info(message: String) = os.write.append(resultLog, s"$message$lineSeparator")
}
