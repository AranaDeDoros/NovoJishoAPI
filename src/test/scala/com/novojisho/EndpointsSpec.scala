package com.novojisho

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import sttp.client4.*
import sttp.client4.testing.SyncBackendStub
import sttp.model.StatusCode
import sttp.tapir.server.stub4.TapirSyncStubInterpreter

class EndpointsSpec extends AnyFlatSpec with Matchers:

  private val backendStub = TapirSyncStubInterpreter(SyncBackendStub)
    .whenServerEndpointsRunLogic(Endpoints.all)
    .backend()

  it should "search Jisho for a term" in {
    val response = basicRequest
      .get(uri"http://localhost/jisho?term=cat")
      .send(backendStub)

    response.code shouldBe StatusCode.Ok
  }

  it should "return 404 for an unmatched route" in {
    val response = basicRequest
      .get(uri"http://localhost/not-found")
      .send(backendStub)

    response.code shouldBe StatusCode.NotFound
  }
