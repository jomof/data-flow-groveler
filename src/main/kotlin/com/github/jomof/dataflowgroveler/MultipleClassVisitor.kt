package com.github.jomof.dataflowgroveler

import org.objectweb.asm.*

class MultipleClassVisitor(
    visitContext : VisitContext) :
    ClassVisitor(visitContext.api),
    IVisitMethod,
    IClassHeader {
    private val methodVisitor = MultipleMethodVisitor(this, visitContext)

    override var classVersion: Int = 0
    override var classAccess: Int = 0
    override var className: String? = null
    override var classSignature: String? = null
    override var classSuperName: String? = null
    override var classInterfaces: Array<out String>? = null

    override var methodAccess: Int = 0
    override var methodName: String? = null
    override var methodDesc: String? = null
    override var methodSignature: String? = null
    override var methodExceptions: Array<out String>? = null
    override val methodClass: IClassHeader = this

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        classVersion = version
        classAccess = access
        className = name
        classSignature = signature
        classSuperName = superName
        classInterfaces = interfaces
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        desc: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        this.methodAccess = access
        this.methodName = name
        this.methodDesc = desc
        this.methodSignature = signature
        this.methodExceptions = exceptions
        return methodVisitor
    }
}