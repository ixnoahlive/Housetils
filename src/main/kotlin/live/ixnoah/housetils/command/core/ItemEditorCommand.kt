package live.ixnoah.housetils.command.core

import com.mojang.serialization.Codec
import live.ixnoah.housetils.Tools
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.UnbreakableComponent
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.silkmc.silk.commands.clientCommand
import net.silkmc.silk.commands.player
import net.silkmc.silk.core.item.setLore

object ItemEditorCommand {
    fun checksumAndGetItem(player: PlayerEntity): ItemStack? {
        if (!player.isCreative) {
            Tools.chatError("You must be in Creative Mode to use this command!")
            return null
        }

        val heldItem = Tools.Item.getHeldItemStack()
        if (heldItem == null) {
            Tools.chatError("You must be in Creative Mode to use this command!")
            return null
        }

        return heldItem
    }

    val command = clientCommand("itemeditor") {


        /** Name Command - Changes custom name of item */
        literal("name") {
            argument<String>("name") { strArg -> runs {
                val heldItem = checksumAndGetItem(source.player) ?: return@runs

                heldItem.set(DataComponentTypes.CUSTOM_NAME, Tools.formatUserText(strArg()))

                Tools.chatSuccess("Updated item name to \"${strArg()}\"!")
            }}
        }
        /** Lore Command - Changes lore of item */
        literal("lore") {
            // add new line
            literal("add") { argument<String>("line") { strArg -> runs {
                val heldItem = checksumAndGetItem(source.player) ?: return@runs

                val lines : MutableList<Text> = heldItem.get(DataComponentTypes.LORE)?.lines?.toMutableList() ?: mutableListOf()
                lines.add(Tools.formatUserText("&7" + strArg()))

                heldItem.setLore(lines)

                Tools.chatSuccess("Added line \"${strArg()}\" to item lore!")
            }}}

            // remove line by number (1-index)
            literal("remove") { argument<Int>("line") { intArg -> runs {
                val heldItem = checksumAndGetItem(source.player) ?: return@runs

                val lines : MutableList<Text> = heldItem.get(DataComponentTypes.LORE)?.lines?.toMutableList() ?: mutableListOf()

                if (intArg() < 1 || intArg() > lines.size)
                    return@runs Tools.chatError("This line doesn't exist! (There are ${lines.size} lines!)")

                lines.removeAt(intArg())
                heldItem.setLore(lines)

                Tools.chatSuccess("Removed line #${intArg()} from the item lore!")
            }}}

            // clear all lines
            literal("clear") runs {
                val heldItem = checksumAndGetItem(source.player) ?: return@runs

                heldItem.setLore(listOf())

                Tools.chatSuccess("Removed all item lore!")
            }
        }
        /** Data Value Command - Adds data value lore to item */
        literal("datavalue") {
            argument<Int>("id") { intArg ->
                runs {
                    val heldItem = checksumAndGetItem(source.player) ?: return@runs

                    heldItem.setLore(listOf(
                        Tools.formatText("&7Data Value: ${intArg()}")
                    ))

                    Tools.chatSuccess("Updated Data Value to ${intArg()}!")
                }
            }
        }

        literal("unbreakable") runs {
            val heldItem = checksumAndGetItem(source.player) ?: return@runs

            val unbreakableValue = heldItem.get(DataComponentTypes.UNBREAKABLE)

            if (unbreakableValue == null) {
                Tools.chatSuccess("Updated item to be unbreakable! &#e6e6eb(Tooltip automatically hidden!)")
                heldItem.set(DataComponentTypes.UNBREAKABLE, UnbreakableComponent(true))
            } else {
                Tools.chatSuccess("Updated item to be breakable!")
                heldItem.remove(DataComponentTypes.UNBREAKABLE)
            }
        }

        literal("tooltip") runs {
            val heldItem = checksumAndGetItem(source.player) ?: return@runs
            val hideTooltipValue = heldItem.get(DataComponentTypes.HIDE_TOOLTIP)

            if (hideTooltipValue == null) {
                Tools.chatSuccess("Updated item to hide tooltip!")
                heldItem.set(DataComponentTypes.HIDE_TOOLTIP, net.minecraft.util.Unit.INSTANCE)
            } else {
                Tools.chatSuccess("Updated item to show tooltip!")
                heldItem.remove(DataComponentTypes.HIDE_TOOLTIP)
            }
        }

        literal("actions") {
            literal("print") runs {
                val heldItem = checksumAndGetItem(source.player) ?: return@runs
                println(Tools.Item.readActions(heldItem).toString())
            }
        }

        alias("it")
    }
}