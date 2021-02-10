package com.github.jomof.dataflowgroveler

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

data class MethodDescription(
    val classHeader : ClassHeader,
    val access: Int,
    val name: String?,
    val desc: String?,
    val signature: String?,
    val exceptions: List<String>
)

data class ClassHeader(
    val version: Int,
    val access: Int,
    val name: String?,
    val signature: String?,
    val superName: String?,
    val interfaces: List<String>
)

data class MethodInsnInfo(
    val opcode: Int,
    val owner: String?,
    val name: String?,
    val desc: String?,
    val itf: Boolean
)

data class CallSiteInfo(
    val callerClassMethod : MethodDescription,
    val callInstruction : MethodInsnInfo
)

private class InspectingClassVisitor(private val callback:(info : CallSiteInfo) -> Unit) : ClassVisitor(Opcodes.ASM7) {
    private var header : ClassHeader? = null

    class InspectingMethodVisitor(
        private val callback:(info : CallSiteInfo) -> Unit,
        private val method : MethodDescription
    ) : MethodVisitor(Opcodes.ASM7) {

        override fun visitMethodInsn(opcode: Int, owner: String?, name: String?, desc: String?, itf: Boolean) {
            val callPair = CallSiteInfo(
                callerClassMethod = method,
                callInstruction = MethodInsnInfo(opcode, owner, name, desc, itf))
            callback(callPair)
            super.visitMethodInsn(opcode, owner, name, desc, itf)
        }
    }

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        header = ClassHeader(
            version,
            access,
            name,
            signature,
            superName,
            interfaces?.toList()?:listOf()
        )
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        desc: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        return InspectingMethodVisitor(callback,
            MethodDescription(
                header!!,
                access,
                name,
                desc,
                signature,
                exceptions?.toList()?:listOf()
            ))
    }
}

fun visitMethodCallSite(callback:(info : CallSiteInfo) -> Unit) : ClassVisitor = InspectingClassVisitor(callback)
