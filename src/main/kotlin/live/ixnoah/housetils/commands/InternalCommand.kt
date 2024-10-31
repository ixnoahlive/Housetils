package live.ixnoah.housetils.commands

import live.ixnoah.housetils.mixin.ChatScreenMixin
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ChatScreen
import net.silkmc.silk.commands.clientCommand


object InternalCommand {
    val command = clientCommand("ht-internal") {
        literal("placesymbol") {
            argument<String>("symbol") { symbol ->
                runs {
                    val mcInstance = MinecraftClient.getInstance()
                    val currentScreen = mcInstance.currentScreen

                    if (mcInstance.inGameHud.chatHud.isChatFocused && currentScreen is ChatScreen) {
                        val chatField = (currentScreen as ChatScreenMixin).chatField
                        chatField.text = chatField.text.replaceRange(chatField.cursor, chatField.cursor, symbol())

                        (currentScreen as ChatScreenMixin).chatField = chatField
                    }
                }
            }
        }
    }
}