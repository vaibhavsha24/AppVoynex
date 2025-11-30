package com.voynex.app.data.remote.pexels

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js

actual fun httpClientEngine(): HttpClientEngine = Js.create()
