package com.github.jomof.dataflowgroveler

import com.github.jomof.dataflowgroveler.MultipleAnnotationVisitor.Companion.multipleAnnotationVisitor
import org.objectweb.asm.*

class MultipleMethodVisitor private constructor (
    private val visitors : List<MethodVisitor>,
    api : Int) : MethodVisitor(api) {

    override fun visitParameter(name: String?, access: Int) {
        for(visitor in visitors){
            visitor.visitParameter(name, access)
        }
    }

    override fun visitAnnotationDefault(): AnnotationVisitor? {
        return multipleAnnotationVisitor(visitors.map{ it.visitAnnotationDefault() }, api)
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor? {
        return multipleAnnotationVisitor(visitors.map{ it.visitAnnotation(descriptor, visible) }, api)
    }

    override fun visitTypeAnnotation(
        typeRef: Int, typePath: TypePath?, descriptor: String?, visible: Boolean
    ): AnnotationVisitor? {
        return multipleAnnotationVisitor(visitors.map{ it.visitTypeAnnotation(typeRef, typePath, descriptor, visible) }, api)
    }

    override fun visitAnnotableParameterCount(parameterCount: Int, visible: Boolean) {
        for(visitor in visitors){
            visitor.visitAnnotableParameterCount(parameterCount, visible)
        }
    }

    override fun visitParameterAnnotation(
        parameter: Int, descriptor: String?, visible: Boolean
    ): AnnotationVisitor? {
        return multipleAnnotationVisitor(visitors.map{ it.visitParameterAnnotation(parameter, descriptor, visible) }, api)
    }

    override fun visitAttribute(attribute: Attribute?) {
        for(visitor in visitors){
            visitor.visitAttribute(attribute)
        }
    }

    override fun visitCode() {
        for(visitor in visitors){
            visitor.visitCode()
        }
    }

    override fun visitFrame(
        type: Int,
        numLocal: Int,
        local: Array<Any?>?,
        numStack: Int,
        stack: Array<Any?>?
    ) {
        for(visitor in visitors){
            visitor.visitFrame(type, numLocal, local, numStack, stack)
        }
    }

    override fun visitInsn(opcode: Int) {
        for(visitor in visitors){
            visitor.visitInsn(opcode)
        }
    }

    override fun visitIntInsn(opcode: Int, operand: Int) {
        for(visitor in visitors) {
            visitor.visitIntInsn(opcode, operand)
        }
    }

    override fun visitVarInsn(opcode: Int, `var`: Int) {
        for(visitor in visitors){
            visitor.visitVarInsn(opcode, `var`)
        }
    }

    override fun visitTypeInsn(opcode: Int, type: String?) {
        for(visitor in visitors){
            visitor.visitTypeInsn(opcode, type)
        }
    }

    override fun visitFieldInsn(
        opcode: Int, owner: String?, name: String?, descriptor: String?
    ) {
        for(visitor in visitors){
            visitor.visitFieldInsn(opcode, owner, name, descriptor)
        }
    }

    override fun visitMethodInsn(
        opcode: Int,
        owner: String?,
        name: String?,
        descriptor: String?,
        isInterface: Boolean
    ) {
        for(visitor in visitors){
            visitor.visitMethodInsn(opcode and Opcodes.SOURCE_MASK.inv(), owner, name, descriptor, isInterface)
        }
    }

    override fun visitInvokeDynamicInsn(
        name: String?,
        descriptor: String?,
        bootstrapMethodHandle: Handle?,
        vararg bootstrapMethodArguments: Any?
    ) {
        for(visitor in visitors){
            visitor.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, *bootstrapMethodArguments)
        }
    }

    override fun visitJumpInsn(opcode: Int, label: Label?) {
        for(visitor in visitors){
            visitor.visitJumpInsn(opcode, label)
        }
    }

    override fun visitLabel(label: Label?) {
        for(visitor in visitors){
            visitor.visitLabel(label)
        }
    }

    override fun visitLdcInsn(value: Any?) {
        for(visitor in visitors){
            visitor.visitLdcInsn(value)
        }
    }

    override fun visitIincInsn(`var`: Int, increment: Int) {
        for(visitor in visitors){
            visitor.visitIincInsn(`var`, increment)
        }
    }

    override fun visitTableSwitchInsn(
        min: Int, max: Int, dflt: Label?, vararg labels: Label?
    ) {
        for(visitor in visitors){
            visitor.visitTableSwitchInsn(min, max, dflt, *labels)
        }
    }

    override fun visitLookupSwitchInsn(dflt: Label?, keys: IntArray?, labels: Array<Label?>?) {
        for(visitor in visitors){
            visitor.visitLookupSwitchInsn(dflt, keys, labels)
        }
    }

    override fun visitMultiANewArrayInsn(descriptor: String?, numDimensions: Int) {
        for(visitor in visitors){
            visitor.visitMultiANewArrayInsn(descriptor, numDimensions)
        }
    }

    override fun visitInsnAnnotation(
        typeRef: Int, typePath: TypePath?, descriptor: String?, visible: Boolean
    ): AnnotationVisitor? {
        return multipleAnnotationVisitor(visitors.map{ it.visitInsnAnnotation(typeRef, typePath, descriptor, visible) }, api)
    }

    override fun visitTryCatchBlock(
        start: Label?, end: Label?, handler: Label?, type: String?
    ) {
        for(visitor in visitors){
            visitor.visitTryCatchBlock(start, end, handler, type)
        }
    }

    override fun visitTryCatchAnnotation(
        typeRef: Int, typePath: TypePath?, descriptor: String?, visible: Boolean
    ): AnnotationVisitor? {
        return multipleAnnotationVisitor(visitors.map{ it.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible)}, api)
    }

    override fun visitLocalVariable(
        name: String?,
        descriptor: String?,
        signature: String?,
        start: Label?,
        end: Label?,
        index: Int
    ) {
        for(visitor in visitors){
            visitor.visitLocalVariable(name, descriptor, signature, start, end, index)
        }
    }

    override fun visitLocalVariableAnnotation(
        typeRef: Int,
        typePath: TypePath?,
        start: Array<Label?>?,
        end: Array<Label?>?,
        index: IntArray?,
        descriptor: String?,
        visible: Boolean
    ): AnnotationVisitor? {
        return multipleAnnotationVisitor(visitors.map{ it.visitLocalVariableAnnotation(
            typeRef, typePath, start, end, index, descriptor, visible
        ) }, api)
    }


    override fun visitLineNumber(line: Int, start: Label?) {
        for(visitor in visitors){
            visitor.visitLineNumber(line, start)
        }
    }

    override fun visitMaxs(maxStack: Int, maxLocals: Int) {
        for(visitor in visitors){
            visitor.visitMaxs(maxStack, maxLocals)
        }
    }

    override fun visitEnd() {
        for(visitor in visitors){
            visitor.visitEnd()
        }
    }

    companion object {
        fun multipleMethodVisitor(
            visitors : List<MethodVisitor?>,
            api : Int) : MethodVisitor? {
            val filtered = visitors.filterNotNull()
            return if (filtered.isEmpty()) null
            else MultipleMethodVisitor(filtered, api)
        }
    }
}