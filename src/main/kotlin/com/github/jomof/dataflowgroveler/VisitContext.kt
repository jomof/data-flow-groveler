package com.github.jomof.dataflowgroveler

import org.objectweb.asm.Opcodes

data class VisitContext(
    val api: Int = Opcodes.ASM9,
    val methodInsnVisitor: MutableList<(insn : IVisitMethodInsn)->Unit> = mutableListOf()) {

    fun visitMethodInsn(visit: (insn: IVisitMethodInsn) -> Unit) {
        methodInsnVisitor += visit
    }
}