package me.mical.serverguider.command

import com.mcstarrysky.starrysky.command.CommandExecutor
import com.mcstarrysky.starrysky.command.CommandHandler
import com.mcstarrysky.starrysky.i18n.sendRaw
import me.mical.serverguider.ConfigReader
import me.mical.serverguider.guide.GuideReader
import me.mical.serverguider.ui.GuideMenu
import org.bukkit.entity.Player
import org.serverct.parrot.parrotx.mechanism.Reloadables
import taboolib.common.platform.ProxyCommandSender
import taboolib.common.platform.command.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.system.measureTimeMillis

/**
 * ServerGuider
 * me.mical.serverguider.command.ServerGuiderCommand
 *
 * @author xiaomu
 * @since 2023/2/14 10:53 PM
 */
@CommandHeader(name = "serverguider", aliases = ["sg", "guide"], permission = "guider.use")
object ServerGuiderCommand : CommandHandler {

    override val sub: ConcurrentHashMap<String, CommandExecutor> = ConcurrentHashMap()

    @CommandBody(permission = "guider.use")
    val open = object : CommandExecutor {

        override val name: String
            get() = "open"

        override val command: SimpleCommandBody
            get() = subCommand {
                execute<Player> { sender, _, _ ->
                    GuideMenu.open(sender)
                }
            }

        init {
            sub[name] = this
        }
    }.command

    @CommandBody(permission = "guider.admin", hidden = true)
    val reload = object : CommandExecutor {

        override val name: String
            get() = "reload"

        override val command: SimpleCommandBody
            get() = subCommand {
                execute<ProxyCommandSender> { sender, _, _ ->
                    measureTimeMillis {
                        ConfigReader.config.reload()
                        GuideReader.load()
                        Reloadables.execute()
                    }.let { time ->
                        sender.sendRaw("插件已成功重载, 耗时 §6${time}ms")
                    }
                }
            }

        init {
            sub[name] = this
        }
    }.command
}