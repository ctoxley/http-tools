package com.http.stub.service
import com.http.stub.WireMockCommonMapping.{post, postMatchingOnBody}

class Post extends Service {

  private val devNull = post(uri = "/dev/null")
  private val bodyMatchThis = postMatchingOnBody(uri = "/post/body/match/true", requestBodyMatch = "true", responseBody = """{"matched":true}""")

  override def allMappings = Seq(devNull, bodyMatchThis)
}
