package com.github.jomof.dataflowgroveler.examples

import java.io.InputStream
import java.io.ObjectInputStream
import java.io.Serializable

open class User : Serializable {
    val name : String? = null
    open fun render() = name
}

interface RenderUser {
    fun renderUser(x : Int, inputStream: InputStream?): String?
}

class RenderUserImpl : RenderUser {
    /*
        empty()V
        00000 R  :  :    L0
        00001 R  :  :     LINENUMBER 17 L0
        00002 R  :  :     RETURN
        00003 ?  :    L1
     */
    fun empty(input : Boolean) : String {
        if (input) {
            return "hello"
        }
        return "goodbye"
    }
    override fun renderUser(x : Int, inputStream: InputStream?): String? {
        val ois = ObjectInputStream(inputStream)
        val user = ois.readObject() as User
        return user.render()
    }
}

class ClassThatInvokeRenderUserVirtually {
    private fun createRenderUser() : RenderUser = RenderUserImpl()
    fun functionThatInvokesRenderUserVirtually() {
        val userRenderer = createRenderUser()
        userRenderer.renderUser(0, null)
    }
    fun readObject() {
        val userRenderer = createRenderUser()
        userRenderer.renderUser(0, null)
    }
}

