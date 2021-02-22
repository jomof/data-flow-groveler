package com.github.jomof.dataflowgroveler

import org.objectweb.asm.*

class MultipleMethodVisitor(
    override val insnCallerMethod: IVisitMethod,
    private val visitContext : VisitContext
) :
    MethodVisitor(visitContext.api),
    IVisitMethodInsn {

    override var insnOpcode: Int = 0
    override var insnOwner: String? = null
    override var insnName: String? = null
    override var insnDescriptor: String? = null
    override var insnIsInterface: Boolean = false

    override fun visitMethodInsn(
        opcode: Int,
        owner: String?,
        name: String?,
        descriptor: String?,
        isInterface: Boolean
    ) {
        this.insnOpcode = opcode
        this.insnOwner = owner
        this.insnName = name
        this.insnDescriptor = descriptor
        this.insnIsInterface = isInterface
        if (visitContext.methodInsnVisitor.isNotEmpty()) {
            visitContext.methodInsnVisitor.forEach { it(this) }
        }
    }
}