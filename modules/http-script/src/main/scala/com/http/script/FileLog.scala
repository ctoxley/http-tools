package com.http.script

object FileLog {

  private val errorLog = os.pwd / "target" / "error.log"
  private val resultLog = os.pwd / "target" / "result.log"
  private val lineSeparator = sys.props("line.separator")

  def truncateLogs(): Unit = {
    if (os.exists(errorLog)) {
      os.truncate(errorLog, 0)
    }
    if (os.exists(resultLog)) {
      os.truncate(resultLog, 0)
    }
  }

  def error(message: String): Unit = os.write.append(errorLog, s"$message$lineSeparator")

  def info(message: String): Unit = os.write.append(resultLog, s"$message$lineSeparator")
}
