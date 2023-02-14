package me.mical.serverguider

import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration

/**
 * ServerGuider
 * me.mical.serverguider.ConfigReader
 *
 * @author xiaomu
 * @since 2023/2/14 10:18 PM
 */
object ConfigReader {

    @Config(autoReload = true)
    lateinit var config: Configuration
        private set
}