package com.github.jomof.dataflowgroveler

import com.github.jomof.dataflowgroveler.MultipleAnnotationVisitor.Companion.multipleAnnotationVisitor
import com.github.jomof.dataflowgroveler.MultipleFieldVisitor.Companion.multipleFieldVisitor
import com.github.jomof.dataflowgroveler.MultipleMethodVisitor.Companion.multipleMethodVisitor
import com.github.jomof.dataflowgroveler.MultipleModuleVisitor.Companion.multipleModuleVisitor
import com.github.jomof.dataflowgroveler.MultipleRecordComponentVisitor.Companion.multipleRecordComponentVisitor
import org.objectweb.asm.*

class MultipleClassVisitor private constructor (
    private val visitors : List<ClassVisitor>,
    api : Int) : ClassVisitor(api) {

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<String?>?
    ) {
        for(visitor in visitors){
            visitor.visit(version, access, name, signature, superName, interfaces)
        }
    }

    override fun visitSource(source: String?, debug: String?) {
        for(visitor in visitors){
            visitor.visitSource(source, debug)
        }
    }

    override fun visitModule(name: String?, access: Int, version: String?): ModuleVisitor? {
        return multipleModuleVisitor(visitors.map{ it.visitModule(name, access, version) }, api)
    }

    override fun visitNestHost(nestHost: String?) {
        for(visitor in visitors){
            visitor.visitNestHost(nestHost)
        }
    }

    override fun visitOuterClass(owner: String?, name: String?, descriptor: String?) {
        for(visitor in visitors){
            visitor.visitOuterClass(owner, name, descriptor)
        }
    }

    override fun visitAnnotation(descriptor: String?, visible: Boolean): AnnotationVisitor? {
        return multipleAnnotationVisitor(visitors.map{ it.visitAnnotation(descriptor, visible) }, api)
    }

    override fun visitTypeAnnotation(
        typeRef: Int, typePath: TypePath?, descriptor: String?, visible: Boolean
    ): AnnotationVisitor? {
        return multipleAnnotationVisitor(visitors.map{ it.visitTypeAnnotation(typeRef, typePath, descriptor, visible) }, api)
    }

    override fun visitAttribute(attribute: Attribute?) {
        for(visitor in visitors){
            visitor.visitAttribute(attribute)
        }
    }

    override fun visitNestMember(nestMember: String?) {
        for(visitor in visitors){
            visitor.visitNestMember(nestMember)
        }
    }

    override fun visitPermittedSubclass(permittedSubclass: String?) {
        for(visitor in visitors){
            visitor.visitPermittedSubclass(permittedSubclass)
        }
    }

    override fun visitInnerClass(
        name: String?, outerName: String?, innerName: String?, access: Int
    ) {
        for(visitor in visitors){
            visitor.visitInnerClass(name, outerName, innerName, access)
        }
    }

    override fun visitRecordComponent(
        name: String?, descriptor: String?, signature: String?
    ): RecordComponentVisitor? {
        return multipleRecordComponentVisitor(visitors.map { it.visitRecordComponent(name, descriptor, signature) }, api)
    }

    override fun visitField(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        value: Any?
    ): FieldVisitor? {
        return multipleFieldVisitor(visitors.map{ it.visitField(access, name, descriptor, signature, value) }, api)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<String?>?
    ): MethodVisitor? {
        return multipleMethodVisitor(visitors.map{ it.visitMethod(access, name, descriptor, signature, exceptions) }, api)
    }

    override fun visitEnd() {
        for(visitor in visitors){
            visitor.visitEnd()
        }
    }

    companion object {
        fun multipleClassVisitor(
            visitors: List<ClassVisitor?>,
            api: Int = Opcodes.ASM9
        ): ClassVisitor? {
            val filtered = visitors.filterNotNull()
            return if (filtered.isEmpty()) null
            else MultipleClassVisitor(filtered, api)
        }
    }
}