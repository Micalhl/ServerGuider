package me.mical.serverguider.listener

import me.mical.serverguider.guide.GuideReader
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import taboolib.common.platform.event.SubscribeEvent

object EventListener {
    @SubscribeEvent
    fun e(e: PlayerCommandPreprocessEvent){
        GuideReader.closeOpenedBook(e.player)
    }
}