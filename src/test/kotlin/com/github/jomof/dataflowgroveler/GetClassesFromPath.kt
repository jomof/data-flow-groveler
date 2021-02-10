package com.github.jomof.dataflowgroveler

import com.github.jomof.dataflowgroveler.MultipleClassVisitor.Companion.multipleClassVisitor
import com.github.jomof.dataflowgroveler.examples.ExamplesRoot
import org.junit.Test

class GetClassesFromPath {
    @Test
    fun basic() {
        val callsites = mutableListOf<CallSiteInfo>()
        val inspector = visitMethodCallSite { callsite ->
            if (callsite.callInstruction.owner=="java/io/ObjectInputStream"
                && callsite.callInstruction.name == "readObject") {
                callsites += callsite
            }
        }
        val multi = multipleClassVisitor(listOf(inspector))

        getClassesFromPath(findByteCodeSource(ExamplesRoot::class.java).parentFile).forEach { classReader ->
            classReader.accept(inspector, 0)
        }
        callsites.size.assertString("1")
    }

    @Test
    fun `multiple visitors`() {
        val callsites = mutableListOf<CallSiteInfo>()
        val inspector = visitMethodCallSite { callsite ->
            if (callsite.callInstruction.owner=="java/io/ObjectInputStream"
                && callsite.callInstruction.name == "readObject") {
                callsites += callsite
            }
        }
        val multi = multipleClassVisitor(listOf(inspector, inspector))

        getClassesFromPath(findByteCodeSource(ExamplesRoot::class.java).parentFile).forEach { classReader ->
            classReader.accept(multi, 0)
        }
        callsites.size.assertString("2")
    }
}