package com.github.jomof.dataflowgroveler

import java.io.File
import java.net.URLDecoder
import java.nio.charset.Charset

fun findByteCodeSource(context: Class<*>): File {
    val rawName = context.name
    val idxOfDot: Int = rawName.lastIndexOf('.')
    val classFileName = (if (idxOfDot == -1) rawName else rawName.substring(idxOfDot + 1)) + ".class"
    val uri = context.getResource(classFileName).toString()
    if (uri.startsWith("file:")) return File(uri.substring(5)).absoluteFile
    if (!uri.startsWith("jar:file:")) {
        val idx = uri.indexOf(':')
        error("class loaded remotely")
    }
    val idx = uri.indexOf('!')
    if (idx == -1) error("weird url")
    val fileName =
        URLDecoder.decode(
            uri.substring("jar:file:".length, idx),
            Charset.defaultCharset().name()
        )
    return File(fileName).absoluteFile

}