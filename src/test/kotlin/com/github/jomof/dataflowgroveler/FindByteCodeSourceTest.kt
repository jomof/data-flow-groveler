package com.github.jomof.dataflowgroveler

import com.github.jomof.dataflowgroveler.examples.ExamplesRoot
import org.junit.Test

class FindByteCodeSourceTest {
    @Test
    fun basic() {
        val path = findByteCodeSource(ExamplesRoot::class.java)
        path.name.assertString("ExamplesRoot.class")
    }
}