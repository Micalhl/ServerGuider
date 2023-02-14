package me.mical.serverguider.command

import me.mical.serverguider.ui.GuideMenu
import org.bukkit.entity.Player
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand

/**
 * ServerGuider
 * me.mical.serverguider.command.ServerGuiderCommand
 *
 * @author xiaomu
 * @since 2023/2/14 10:53 PM
 */
@CommandHeader(name = "serverguider", aliases = ["sg", "guider"])
object ServerGuiderCommand {

    @CommandBody
    val main = mainCommand {
        execute<Player> { sender, _, _ ->
            GuideMenu.open(sender)
        }
    }
}