package com.github.jomof.dataflowgroveler

import com.github.jomof.dataflowgroveler.examples.ExamplesRoot
import org.junit.Test
import java.io.File

class StagesTest {
    private val intellijClassFolder = File("/Users/jomof/Library/Application Support/JetBrains/Toolbox/apps/AndroidStudio/ch-0/202.7033425")
    private val testClassFolder = findByteCodeSource(ExamplesRoot::class.java).parentFile
    private val classFolder = testClassFolder
    init {
        println(intellijClassFolder)
        println(testClassFolder)
    }

    private val roots = listOf(
        CallersOfMethod(className = "java/io/ObjectInputStream", method = "readObject")
    )

    private val stage1 by lazy {
        val stage = SinglePass()
        roots.forEach { root -> stage.add(root) }
        getClassesFromPath(classFolder).forEach { classReader ->
            stage.readFrom(classReader)
        }
        println("stage1 done")
        stage
    }

    private val stage2 by lazy {
        val stage = SinglePass()

        for((input, outputs) in stage1.getResults()) {
            when(input) {
                is CallersOfMethod -> {
                    for(insn in outputs) {
                        insn as VisitMethodInsn
                        val newInput = CallersOfMethod(
                            className = insn.insnCallerMethod.methodClass.className!!,
                            method = insn.insnCallerMethod.methodName!!,
                            whichCalls = input
                        )
                        stage.add(newInput)
                    }
                }
                else -> error("${input.javaClass}")
            }
        }

        getClassesFromPath(classFolder).forEach { classReader ->
            stage.readFrom(classReader)
        }
        println("stage2 done")
        stage
    }

    /**
     * The first stage is to gather all of the call sites of the questionable
     * method call. These are, for example, the class and methods that directly
     * call readObject().
     */
    @Test
    fun `stage 1, collect initial call sites`() {
        println(stage1)
    }


    @Test
    fun `stage 2, collect next stage`() {
        println(stage2)
    }
}