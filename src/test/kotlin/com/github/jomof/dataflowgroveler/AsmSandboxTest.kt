package com.github.jomof.dataflowgroveler

import com.github.jomof.dataflowgroveler.examples.RenderUserImpl
import org.junit.Test
import org.objectweb.asm.tree.*
import org.objectweb.asm.tree.analysis.*
import org.objectweb.asm.util.*
import java.io.PrintWriter
import java.io.StringWriter


class AsmSandboxTest {
    private fun getUnqualifiedName(name: String): String {
        val lastSlashIndex = name.lastIndexOf('/')
        return if (lastSlashIndex == -1) {
            name
        } else {
            var endIndex = name.length
            if (name[endIndex - 1] == ';') {
                endIndex--
            }
            name.substring(lastSlashIndex + 1, endIndex)
        }
    }

    fun printAnalyzerResult(
        method: MethodNode, analyzer: Analyzer<BasicValue?>, printWriter: PrintWriter
    ) {
        val textifier = Textifier()
        val traceMethodVisitor = TraceMethodVisitor(textifier)
        printWriter.println(method.name + method.desc)
        for (i in 0 until method.instructions.size()) {
            method.instructions.get(i).accept(traceMethodVisitor)
            val stringBuilder = StringBuilder()
            val frame = analyzer.frames[i]
            if (frame == null) {
                stringBuilder.append('?')
            } else {
                for (j in 0 until frame.locals) {
                    stringBuilder.append(getUnqualifiedName(frame.getLocal(j).toString())).append(' ')
                }
                stringBuilder.append(" | ")
                for (j in 0 until frame.stackSize) {
                    stringBuilder.append(getUnqualifiedName(frame.getStack(j).toString())).append(' ')
                }
            }
            while (stringBuilder.length < method.maxStack + method.maxLocals + 1) {
                stringBuilder.append(' ')
            }
            printWriter.print((i + 100000).toString().substring(1))
            printWriter.print(
                " " + stringBuilder + " | " + textifier.text[textifier.text.size - 1]
            )
        }
        for (tryCatchBlock in method.tryCatchBlocks) {
            tryCatchBlock.accept(traceMethodVisitor)
            printWriter.print(" " + textifier.text[textifier.text.size - 1])
        }
        printWriter.println()
    }
    @Test
    fun test() {
        val analyzer = Analyzer(BasicInterpreter())
        val node = getClassNode(RenderUserImpl::class.java)
        val method = node.methods.toList().single { it.name == "renderUser" }
        val result = analyzer.analyze("owner", method)
        val sw = StringWriter()
        printAnalyzerResult(method, analyzer, PrintWriter(sw))
       println(sw.toString())
    }
}