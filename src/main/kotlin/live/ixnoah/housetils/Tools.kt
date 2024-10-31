package live.ixnoah.housetils

import net.minecraft.client.MinecraftClient
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

object Tools {
    fun chatCommand(command: String) {
        MinecraftClient.getInstance().player?.networkHandler?.sendChatCommand(command)
    }

    fun chat(message: Text) {
        MinecraftClient.getInstance().player?.sendMessage(message)
    }

    fun getHeldItemStack(): ItemStack? {
        return MinecraftClient.getInstance().player?.inventory?.mainHandStack
    }
}