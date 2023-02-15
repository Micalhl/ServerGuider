package me.mical.serverguider.guide

import me.mical.serverguider.ConfigReader
import me.mical.serverguider.database.PluginDatabase
import net.kyori.adventure.inventory.Book
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.io.newFolder
import taboolib.common.platform.Awake
import taboolib.common.platform.function.getDataFolder
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.module.configuration.Configuration
import taboolib.platform.compat.replacePlaceholder
import taboolib.platform.util.sendLang

/**
 * ServerGuider
 * me.mical.serverguider.guide.GuideReader
 *
 * @author xiaomu
 * @since 2023/2/14 10:07 PM
 */
object GuideReader {

    val guides = sortedMapOf<Int, Pair<String, List<String>>>()

    @Awake(LifeCycle.ENABLE)
    fun load() {
        guides.clear()
        val folder = newFolder(getDataFolder(), "guides")
        val files = folder.listFiles { _, name -> name.lowercase().endsWith(".yml") } ?: return
        for (file in files) {
            val config = Configuration.loadFromFile(file)
            val number = config.getInt("number")
            val name = config.getString("name") ?: ""
            val content = config.getStringList("content")
            guides[number] = name to content
        }
    }

    fun open(player: Player, name: String) {
        val content = guides.values.first { it.first == name }.second
        val book = Book.book(Component.text(ConfigReader.config.getString("name") ?: "helper"), Component.text(ConfigReader.config.getString("author") ?: "administrator"), content.map { MiniMessage.miniMessage().deserialize(it.replacePlaceholder(player)) })
        try {
            player.invokeMethod<Any>("openBook", book)
            player.sendLang("open-guide", name)
            if (!PluginDatabase.read(player.uniqueId.toString(), name)) {
                PluginDatabase.write(player.uniqueId.toString(), name)
            }
        } catch (ex: Throwable) {
            throw IllegalStateException("Please use Paper!")
        }
    }
}