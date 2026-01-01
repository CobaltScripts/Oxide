package org.cobalt.oxide.module

import java.awt.Color
import org.cobalt.api.module.Module
import org.cobalt.api.module.setting.impl.*
import org.lwjgl.glfw.GLFW

object OxideModule : Module(
  name = "Oxide Module",
) {

  val checkbox by CheckboxSetting(
    name = "Checkbox",
    description = "Oxide checkbox setting",
    defaultValue = true
  )

  val color by ColorSetting(
    name = "Color",
    description = "Oxide color setting",
    defaultValue = Color.WHITE.rgb
  )

  val keyBind by KeyBindSetting(
    name = "KeyBind",
    description = "Oxide keybind setting",
    defaultValue = GLFW.GLFW_KEY_ESCAPE
  )

  val mode by ModeSetting(
    name = "Mode",
    description = "Oxide mode setting",
    defaultValue = 0,
    options = arrayOf("Mode1", "Mode2", "Mode3")
  )

  val range by RangeSetting(
    name = "Range",
    description = "Oxide range setting",
    defaultValue = Pair(3.0, 5.0),
    min = 0.0,
    max = 10.0
  )

  val slider by SliderSetting(
    name = "Slider",
    description = "Oxide slider setting",
    defaultValue = 3.0,
    min = 1.0,
    max = 10.0
  )

  val text by TextSetting(
    name = "Text",
    description = "Oxide text setting",
    defaultValue = "Hello"
  )

}
