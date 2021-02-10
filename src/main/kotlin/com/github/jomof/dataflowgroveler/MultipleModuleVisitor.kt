package com.github.jomof.dataflowgroveler

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ModuleVisitor
import org.objectweb.asm.Opcodes

class MultipleModuleVisitor private constructor (
    private val visitors : List<ModuleVisitor>,
    api : Int = Opcodes.ASM7
) : ModuleVisitor(api) {

    override fun visitMainClass(mainClass: String?) {
        for(visitor in visitors){
            visitor.visitMainClass(mainClass)
        }
    }

    override fun visitPackage(packaze: String?) {
        for(visitor in visitors){
            visitor.visitPackage(packaze)
        }
    }

    override fun visitRequire(module: String?, access: Int, version: String?) {
        for(visitor in visitors){
            visitor.visitRequire(module, access, version)
        }
    }

    override fun visitExport(packaze: String?, access: Int, vararg modules: String?) {
        for(visitor in visitors){
            visitor.visitExport(packaze, access, *modules)
        }
    }

    override fun visitOpen(packaze: String?, access: Int, vararg modules: String?) {
        for(visitor in visitors){
            visitor.visitOpen(packaze, access, *modules)
        }
    }

    override fun visitUse(service: String?) {
        for(visitor in visitors){
            visitor.visitUse(service)
        }
    }

    override fun visitProvide(service: String?, vararg providers: String?) {
        for(visitor in visitors){
            visitor.visitProvide(service, *providers)
        }
    }

    override fun visitEnd() {
        for(visitor in visitors){
            visitor.visitEnd()
        }
    }

    companion object {
        fun multipleModuleVisitor(
            visitors: List<ModuleVisitor?>,
            api: Int
        ): ModuleVisitor? {
            val filtered = visitors.filterNotNull()
            return if (filtered.isEmpty()) null
            else MultipleModuleVisitor(filtered, api)
        }
    }
}