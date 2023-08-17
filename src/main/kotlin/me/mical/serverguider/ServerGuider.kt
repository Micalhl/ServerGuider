package me.mical.serverguider

import com.mcstarrysky.starrysky.i18n.I18n
import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info

object ServerGuider : Plugin() {

    override fun onEnable() {
		I18n.initialize()
        info("Successfully running ServerGuider! Author: Mical")
    }
}