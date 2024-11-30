package live.ixnoah.housetils.command.core

import live.ixnoah.housetils.Tools
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.NbtComponent
import net.minecraft.component.type.UnbreakableComponent
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Unit
import net.silkmc.silk.commands.clientCommand
import net.silkmc.silk.commands.player
import net.silkmc.silk.core.item.setLore
import net.silkmc.silk.nbt.set

object ItemEditorCommand {
    var actionClipboard : String? = null

    private fun checksumAndGetItem(player: PlayerEntity, creativeRequired: Boolean = true): ItemStack? {
        if (!player.isCreative && creativeRequired) {
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

                heldItem[DataComponentTypes.CUSTOM_NAME] = Tools.formatUserText(strArg())

                Tools.chatSuccess("Updated item name to \"${strArg()}\"!")
            }}
        }
        /** Lore Command - Changes lore of item */
        literal("lore") {
            // add new line
            literal("add") { argument<String>("line") { strArg -> runs {
                val heldItem = checksumAndGetItem(source.player) ?: return@runs

                val lines : MutableList<Text> = heldItem[DataComponentTypes.LORE]?.lines?.toMutableList() ?: mutableListOf()
                lines.add(Tools.formatUserText("&7" + strArg()))

                heldItem.setLore(lines)

                Tools.chatSuccess("Added line \"${strArg()}\" to item lore!")
            }}}

            // remove line by number (1-index)
            literal("remove") { argument<Int>("line") { intArg -> runs {
                val heldItem = checksumAndGetItem(source.player) ?: return@runs

                val lines : MutableList<Text> = heldItem[DataComponentTypes.LORE]?.lines?.toMutableList() ?: mutableListOf()

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

            val unbreakableValue = heldItem[DataComponentTypes.UNBREAKABLE]

            if (unbreakableValue == null) {
                Tools.chatSuccess("Updated item to be unbreakable! &#e6e6eb(Tooltip automatically hidden!)")
                heldItem[DataComponentTypes.UNBREAKABLE] = UnbreakableComponent(true)
            } else {
                Tools.chatSuccess("Updated item to be breakable!")
                heldItem.remove(DataComponentTypes.UNBREAKABLE)
            }
        }

        literal("tooltip") runs {
            val heldItem = checksumAndGetItem(source.player) ?: return@runs
            val hideTooltipValue = heldItem[DataComponentTypes.HIDE_TOOLTIP]

            if (hideTooltipValue == null) {
                Tools.chatSuccess("Updated item to hide tooltip!")
                heldItem[DataComponentTypes.HIDE_TOOLTIP] = Unit.INSTANCE
            } else {
                Tools.chatSuccess("Updated item to show tooltip!")
                heldItem.remove(DataComponentTypes.HIDE_TOOLTIP)
            }
        }

        literal("actions") {
            literal("print") runs {
                val heldItem = checksumAndGetItem(source.player, false) ?: return@runs
                val actionData = Tools.Item.readActions(heldItem)
                    ?: return@runs Tools.chatError("Could not read this item's actions! Does it have any?")

                val actionList = actionData.get("actions") as NbtList

                Tools.chatSuccess("Successfully decompiled ${actionList.size} actions!")
                Tools.chat(Text.literal("$actionList").setStyle(Style.EMPTY.withColor(0x7777AA)))
            }

            literal("copy") runs {
                val heldItem = checksumAndGetItem(source.player, false) ?: return@runs
                val actionData = Tools.Item.readActions(heldItem)
                    ?: return@runs Tools.chatError("Could not read this item's actions! Does it have any?")

                actionClipboard = actionData.get("source")!!.asString()
                Tools.chatSuccess("Successfully copied item actions! (Note: They are constrained to this house!)")
            }

            literal("paste") runs {
                val heldItem = checksumAndGetItem(source.player) ?: return@runs

                if (actionClipboard == null)
                    return@runs Tools.chatError("Your clipboard is empty!")

                val actionData = Tools.Item.readActions(heldItem)
                println(actionClipboard)

                if (actionData == null) {
                    val newCustomData = NbtCompound()
                    newCustomData["interact_data"] = actionClipboard!!

                    heldItem.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(newCustomData))
                } else {
                    val newCustomData = heldItem.get(DataComponentTypes.CUSTOM_DATA)!!.copyNbt()
                    newCustomData["interact_data"] = actionClipboard!!

                    heldItem.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(newCustomData))
                }

                Tools.chatSuccess("Successfully pasted item actions onto held item!")
            }
        }

        alias("it")
    }
}