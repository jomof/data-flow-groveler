package com.github.jomof.dataflowgroveler

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.tree.ClassNode
import java.io.FileInputStream

fun getClassFileInputStream(clazz: Class<*>) = FileInputStream(findByteCodeSource(clazz))
fun getClassReader(clazz: Class<*>) = ClassReader(getClassFileInputStream(clazz))
fun getClassNode(clazz: Class<*>) : ClassNode {
    val classReader = getClassReader(clazz)
    val classNode = ClassNode()
    classReader.accept(classNode as ClassVisitor, 0)
    return classNode
}
