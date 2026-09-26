package com.novojisho

import ox.*
import sttp.tapir.server.netty.sync.NettySyncServer

object Main extends OxApp.Simple:

  def run(using Ox): Unit =
    val port = sys.env.get("JISHO_HTTP_PORT").flatMap(_.toIntOption).getOrElse(8080)
    val host = sys.env.getOrElse("JISHO_API_HOST", "http://localhost")
    val binding = useInScope(NettySyncServer().port(port).addEndpoints(Endpoints.all).start())(_.stop())
    println(s"Go to ${host}:${binding.port}/docs to open SwaggerUI. ")
    never
