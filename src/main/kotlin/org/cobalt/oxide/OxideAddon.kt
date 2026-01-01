package org.cobalt.oxide

import org.cobalt.oxide.command.OxideCommand
import org.cobalt.oxide.module.OxideModule
import org.cobalt.api.addon.Addon
import org.cobalt.api.command.CommandManager
import org.cobalt.api.module.Module

object Oxide : Addon() {

  override fun onLoad() {
    CommandManager.register(OxideCommand)
    println("Oxide loaded!")
  }

  override fun onUnload() {
    println("Oxide unloaded!")
  }

  override fun getModules(): List<Module> {
    return listOf(OxideModule)
  }

}
