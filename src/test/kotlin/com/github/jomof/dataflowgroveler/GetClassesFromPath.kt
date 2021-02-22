package com.github.jomof.dataflowgroveler

import com.github.jomof.dataflowgroveler.examples.ExamplesRoot
import org.junit.Test

class GetClassesFromPath {
    @Test
    fun basic() {
        val callsites = mutableListOf<VisitMethodInsn>()
        val context = VisitContext()
        context.visitMethodInsn { insn ->
            if (insn.insnOwner=="java/io/ObjectInputStream"
                && insn.insnName == "readObject") {
                callsites += insn.toVisitMethodInsn()
            }
        }
        val multi = MultipleClassVisitor(context)

        getClassesFromPath(findByteCodeSource(ExamplesRoot::class.java).parentFile).forEach { classReader ->
            classReader.accept(multi, 0)
        }
        callsites.size.assertString("1")
    }

    @Test
    fun `multiple visitors`() {
        val callsites = mutableListOf<VisitMethodInsn>()
        val context = VisitContext()
        context.visitMethodInsn { insn ->
            if (insn.insnOwner=="java/io/ObjectInputStream"
                && insn.insnName == "readObject") {
                callsites += insn.toVisitMethodInsn()
            }
        }
        context.visitMethodInsn { insn ->
            if (insn.insnOwner=="java/io/ObjectInputStream"
                && insn.insnName == "readObject") {
                callsites += insn.toVisitMethodInsn()
            }
        }
        val multi = MultipleClassVisitor(context)

        getClassesFromPath(findByteCodeSource(ExamplesRoot::class.java).parentFile).forEach { classReader ->
            classReader.accept(multi, 0)
        }
        callsites.size.assertString("2")
    }
}