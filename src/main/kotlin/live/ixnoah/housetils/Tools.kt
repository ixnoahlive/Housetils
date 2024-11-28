package live.ixnoah.housetils

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.mojang.serialization.Codec
import eu.pb4.placeholders.api.parsers.LegacyFormattingParser
import eu.pb4.placeholders.api.parsers.NodeParser
import net.minecraft.client.MinecraftClient
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtIo
import net.minecraft.nbt.NbtSizeTracker
import net.minecraft.nbt.StringNbtReader
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket
import net.minecraft.text.MutableText
import net.minecraft.text.Style
import net.minecraft.text.Text
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.util.Base64

object Tools {
    fun formatText(text: String): MutableText {
        val parser = NodeParser.merge(LegacyFormattingParser.ALL)
        return parser.parseNode(text).toText().copy()
    }

    fun formatUserText(text: String): MutableText {
        val parser = NodeParser.merge(LegacyFormattingParser.ALL)
        return parser.parseNode(text).toText().copy()
    }

    fun chatCommand(command: String) {
        MinecraftClient.getInstance().player?.networkHandler?.sendChatCommand(command)
    }

    fun chat(message: Text) {
        MinecraftClient.getInstance().player?.sendMessage(message)
    }

    fun chat(text: String) {
        MinecraftClient.getInstance().player?.sendMessage(formatText(text))
    }

    fun chatError(message: String) {
        this.chat(formatText(message).setStyle(Style.EMPTY.withColor(0xFF2244)))
    }

    fun chatSuccess(message: String) {
        this.chat(formatText(message).setStyle(Style.EMPTY.withColor(0x22FF88)))
    }

    object Item {
        fun getHeldItemStack(): ItemStack? {
            var mainHandStack = MinecraftClient.getInstance().player?.inventory?.mainHandStack
            if (mainHandStack == ItemStack.EMPTY) mainHandStack = null

            return mainHandStack
        }

        fun giveItemStack(stack: ItemStack, slot: Int? = null) {
            val player = MinecraftClient.getInstance().player ?: return
            if (!player.isCreative) return

            val usedSlot = slot ?: player.inventory.selectedSlot

            player.networkHandler.sendPacket( CreativeInventoryActionC2SPacket(usedSlot, stack) )
        }

        fun readActions(stack: ItemStack): NbtCompound? {
            val encodedData = stack.get(DataComponentTypes.CUSTOM_DATA)?.get(Codec.STRING.fieldOf("interact_data"))
                ?: return null

            val encodedDataSplit = encodedData.result()?.toString()?.split(".") ?: return null
            val decodedPayload   = String( Base64.getDecoder().decode(encodedDataSplit[1]) )

            val jsonPayload = Gson().fromJson(decodedPayload, JsonObject::class.java) ?: return null

            val encodedActionNBT = jsonPayload.get("data").asString ?: return null
            val decodedActionNBT = Base64.getDecoder().decode(encodedActionNBT) ?: return null

            val inputStream = DataInputStream(ByteArrayInputStream(decodedActionNBT))

            return NbtIo.readCompressed(inputStream, NbtSizeTracker(2091752, 8))
        }

    }
}