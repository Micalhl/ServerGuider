package me.mical.serverguider.ui

import me.mical.serverguider.database.PluginDatabase
import me.mical.serverguider.guide.GuideReader
import org.bukkit.Material
import org.bukkit.entity.Player
import org.serverct.parrot.parrotx.function.singletons
import org.serverct.parrot.parrotx.mechanism.Reloadable
import org.serverct.parrot.parrotx.ui.MenuComponent
import org.serverct.parrot.parrotx.ui.config.MenuConfiguration
import org.serverct.parrot.parrotx.ui.feature.util.MenuFunctionBuilder
import taboolib.common5.cbool
import taboolib.module.chat.colored
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.ui.openMenu
import taboolib.module.ui.type.Linked

/**
 * ServerGuider
 * me.mical.serverguider.ui.GuideMenu
 *
 * @author xiaomu
 * @since 2023/2/14 10:10 PM
 */
@MenuComponent("Guide")
object GuideMenu {

    @Config("guide.yml")
    private lateinit var source: Configuration
    private lateinit var config: MenuConfiguration

    @Reloadable
    fun reload() {
        source.reload()
        config = MenuConfiguration(source)
    }

    fun open(player: Player) {
        if (!::config.isInitialized) {
            config = MenuConfiguration(source)
        }
        player.openMenu<Linked<Pair<String, List<String>>>>(config.title().colored()) {
            val (shape, templates) = config
            rows(shape.rows)
            val slots = shape["Guide\$information"].toList()
            slots(slots)
            elements { GuideReader.guides.values.toList() }

            onBuild { _, it ->
                shape.all("Guide\$information", "Previous", "Next", "Close") { slot, index, item, _ ->
                    it.setItem(slot, item(slot, index))
                }
            }

            val template = templates.require("Guide\$information")
            onGenerate { _, member, index, slot ->
                template(slot, index, PluginDatabase.read(player.uniqueId.toString(), member.first), member.first)
            }

            onClick { event, member ->
                event.isCancelled = true
                template.handle(event, member.first)
            }

            shape["Previous"].first().let { slot ->
                setPreviousPage(slot) { it, _ ->
                    templates("Previous", slot, it)
                }
            }

            shape["Next"].first().let { slot ->
                setNextPage(slot) { it, _ ->
                    templates("Next", slot, it)
                }
            }

            onClick { event ->
                event.isCancelled = true
                if (event.rawSlot in shape && event.rawSlot !in slots) {
                    templates[event.rawSlot]?.handle(event)
                }
            }
        }
    }

    @MenuComponent
    private val information = MenuFunctionBuilder {
        onBuild { (_, extra, _, _, icon, args) ->
            val state = args[0].cbool
            icon.singletons {
                when (it) {
                    "name" -> args[1].toString()
                    "option" -> if (state) extra["read"].toString().colored() else extra["or"].toString().colored()
                    else -> null
                }
            }.also {
                if (state) {
                    it.type = Material.WRITTEN_BOOK
                }
            }
        }

        onClick { (_, _, event, args) ->
            if (event.clickEvent().isLeftClick) {
                event.clicker.closeInventory()
                GuideReader.open(event.clicker, args[0].toString())
            }
        }
    }
}