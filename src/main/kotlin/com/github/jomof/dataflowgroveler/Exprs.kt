package com.github.jomof.dataflowgroveler

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor

interface Expr

data class CallersOfMethod(
    val className : String,
    val method : String,
    val whichCalls : CallersOfMethod? = null
) : Expr {
    override fun toString() : String {
        var result = "$className:$method"
        if (whichCalls != null) result += "\n$whichCalls"
        return result
    }
}

class SinglePass {
    private val results = mutableMapOf<Expr, MutableList<Info>>()
    private var totalResults = 0
    private var totalClasses = 0
    private val visitContext = VisitContext()
    fun add(expr : Expr) {
        when (expr) {
            is CallersOfMethod -> {
                val list = mutableListOf<Info>()
                results[expr] = list
                visitContext.visitMethodInsn { insn : IVisitMethodInsn ->
                    if (insn.insnOwner == expr.className && insn.insnName == expr.method) {
                        list += insn.toVisitMethodInsn()
                        ++totalResults
                    }
                }
            }
            else -> error("")
        }
    }

    fun readFrom(classReader : ClassReader) {
        val multi = MultipleClassVisitor(visitContext)
        ++totalClasses
        classReader.accept(multi, 0)
    }

    override fun toString(): String {
        return "${results.size} result categories, $totalClasses classes read, $totalResults results"
    }

    fun getResults() : Map<Expr, List<Info>> = results
}