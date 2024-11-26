package com.izcode.law

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform