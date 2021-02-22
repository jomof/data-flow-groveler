package com.github.jomof.dataflowgroveler

interface Info

interface IClassHeader {
    val classVersion: Int
    val classAccess: Int
    val className: String?
    val classSignature: String?
    val classSuperName: String?
    val classInterfaces: Array<out String>?
    fun toClassHeader() : ClassHeader =
        ClassHeader(classVersion, classAccess, className, classSignature, classSuperName, classInterfaces)
}

data class ClassHeader(
    override val classVersion: Int,
    override val classAccess: Int,
    override val className: String?,
    override val classSignature: String?,
    override val classSuperName: String?,
    override val classInterfaces: Array<out String>?
) : IClassHeader, Info {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ClassHeader

        if (classVersion != other.classVersion) return false
        if (classAccess != other.classAccess) return false
        if (className != other.className) return false
        if (classSignature != other.classSignature) return false
        if (classSuperName != other.classSuperName) return false
        if (classInterfaces != null) {
            if (other.classInterfaces == null) return false
            if (!classInterfaces.contentEquals(other.classInterfaces)) return false
        } else if (other.classInterfaces != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = classVersion
        result = 31 * result + classAccess
        result = 31 * result + (className?.hashCode() ?: 0)
        result = 31 * result + (classSignature?.hashCode() ?: 0)
        result = 31 * result + (classSuperName?.hashCode() ?: 0)
        result = 31 * result + (classInterfaces?.contentHashCode() ?: 0)
        return result
    }
}

interface IVisitMethod {
    val methodAccess: Int
    val methodName: String?
    val methodDesc: String?
    val methodSignature: String?
    val methodExceptions: Array<out String>?
    val methodClass : IClassHeader
    fun toVisitMethod() : VisitMethod = VisitMethod(methodAccess, methodName, methodDesc,
        methodSignature, methodExceptions, methodClass.toClassHeader())
}

data class VisitMethod(
    override val methodAccess: Int,
    override val methodName: String?,
    override val methodDesc: String?,
    override val methodSignature: String?,
    override val methodExceptions: Array<out String>?,
    override val methodClass: ClassHeader
) : IVisitMethod, Info {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VisitMethod

        if (methodAccess != other.methodAccess) return false
        if (methodName != other.methodName) return false
        if (methodDesc != other.methodDesc) return false
        if (methodSignature != other.methodSignature) return false
        if (methodExceptions != null) {
            if (other.methodExceptions == null) return false
            if (!methodExceptions.contentEquals(other.methodExceptions)) return false
        } else if (other.methodExceptions != null) return false
        if (methodClass != other.methodClass) return false

        return true
    }

    override fun hashCode(): Int {
        var result = methodAccess
        result = 31 * result + (methodName?.hashCode() ?: 0)
        result = 31 * result + (methodDesc?.hashCode() ?: 0)
        result = 31 * result + (methodSignature?.hashCode() ?: 0)
        result = 31 * result + (methodExceptions?.contentHashCode() ?: 0)
        result = 31 * result + methodClass.hashCode()
        return result
    }
}

interface IVisitMethodInsn {
    val insnOpcode: Int
    val insnOwner: String?
    val insnName: String?
    val insnDescriptor: String?
    val insnIsInterface: Boolean
    val insnCallerMethod: IVisitMethod
    fun toVisitMethodInsn() : VisitMethodInsn =
        VisitMethodInsn(insnOpcode, insnOwner, insnName, insnDescriptor, insnIsInterface, insnCallerMethod.toVisitMethod())
}

data class VisitMethodInsn(
    override val insnOpcode: Int,
    override val insnOwner: String?,
    override val insnName: String?,
    override val insnDescriptor: String?,
    override val insnIsInterface: Boolean,
    override val insnCallerMethod: VisitMethod) : IVisitMethodInsn, Info

