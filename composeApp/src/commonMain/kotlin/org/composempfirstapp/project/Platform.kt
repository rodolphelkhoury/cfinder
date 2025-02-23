package org.composempfirstapp.project


interface Platform {
    val name: String
}

expect fun getPlatform(): Platform