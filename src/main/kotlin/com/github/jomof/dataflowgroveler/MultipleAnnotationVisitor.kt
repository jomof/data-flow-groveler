package com.github.jomof.dataflowgroveler

import org.objectweb.asm.*

class MultipleAnnotationVisitor private constructor(
    private val visitors : List<AnnotationVisitor>,
    api : Int) : AnnotationVisitor(api) {


    override fun visit(name: String?, value: Any?) {
        for(visitor in visitors){
            visitor.visit(name, value)
        }
    }

    override fun visitEnum(name: String?, descriptor: String?, value: String?) {
        for(visitor in visitors){
            visitor.visitEnum(name, descriptor, value)
        }
    }

    override fun visitAnnotation(name: String?, descriptor: String?): AnnotationVisitor? {
        return MultipleAnnotationVisitor(visitors.map{ it.visitAnnotation(descriptor, descriptor) }, api)
    }

    override fun visitArray(name: String?): AnnotationVisitor? {
        return MultipleAnnotationVisitor(visitors.map{ it.visitArray(name) }, api)
    }

    override fun visitEnd() {
        for(visitor in visitors) {
            visitor.visitEnd()
        }
    }
    companion object {
        fun multipleAnnotationVisitor(
            visitors : List<AnnotationVisitor?>,
            api : Int) : MultipleAnnotationVisitor? {
            val filtered = visitors.filterNotNull()
            return if (filtered.isEmpty()) null
            else MultipleAnnotationVisitor(filtered, api)
        }
    }
}