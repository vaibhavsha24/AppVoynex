package com.voynex.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform