package com.github.jomof.dataflowgroveler

import org.objectweb.asm.ClassReader
import java.io.File
import java.io.FileInputStream

fun getClassesFromPath(classFolder : File) : Sequence<ClassReader> = sequence {
    for(jar in classFolder.walkTopDown().filter { it.isFile && it.extension == "jar"}) {
        val zip = java.util.zip.ZipFile(jar)
        for(entry in zip.entries()) {
            if (entry.isDirectory) continue
            if (!entry.name.endsWith(".class")) continue
            zip.getInputStream(entry).use { stream ->
                yield(ClassReader(stream))
            }
        }
    }
    for(clasz in classFolder.walkTopDown().filter { it.isFile && it.extension == "class"}) {
        yield(ClassReader(FileInputStream(clasz)))
    }
}