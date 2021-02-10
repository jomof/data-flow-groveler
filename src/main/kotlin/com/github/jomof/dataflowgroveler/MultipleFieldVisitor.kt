package com.github.jomof.dataflowgroveler

import com.github.jomof.dataflowgroveler.MultipleAnnotationVisitor.Companion.multipleAnnotationVisitor
import org.objectweb.asm.*

class MultipleFieldVisitor private constructor (
    private val visitors : List<FieldVisitor>,
    api : Int
) : FieldVisitor(api) {

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

    override fun visitEnd() {
        for(visitor in visitors){
            visitor.visitEnd()
        }
    }

    companion object {
        fun multipleFieldVisitor(
            visitors : List<FieldVisitor?>,
            api : Int) : FieldVisitor? {
            val filtered = visitors.filterNotNull()
            return if (filtered.isEmpty()) null
            else MultipleFieldVisitor(filtered, api)
        }
    }
}