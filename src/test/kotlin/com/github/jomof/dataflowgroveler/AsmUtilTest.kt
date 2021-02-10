package com.github.jomof.dataflowgroveler

import com.github.jomof.dataflowgroveler.examples.RenderUserImpl

import org.junit.Test

class AsmUtilTest {
    @Test
    fun `test getClassNode`() {
        val node = getClassNode(RenderUserImpl::class.java)
        val method = node.methods.toList().filter { it.name == "renderUser" }.single()
        method.name.assertString("renderUser")
    }
}