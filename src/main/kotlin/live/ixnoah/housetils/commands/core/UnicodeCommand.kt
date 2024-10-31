package live.ixnoah.housetils.commands.core

import net.minecraft.text.*
import net.silkmc.silk.commands.clientCommand
import net.silkmc.silk.commands.player

object UnicodeCommand {
    val symbols = mapOf(
        "Stars" to listOf("★","☆","✡","✦","✧","✩","✪","✫","✬","✭","✮","✯","✰","✢","✣","✤","✥","✱","✲","✳","✴","✵","✶","✷","✸","✹","✺","✻","✼","✽","✾","✿","❀","❁","❂","❃","❇","❈","❉","❊","❋","❄","❆","❅","⋆","۞","⭒","⍟","⭐"),
        "SkyBlock" to listOf("❁","❤","❈","❂","✦","✎","☣","☠","⚔","⫽","✯","♣","α","๑","⸕","✧","☘","⸎","ʬ","♨","᠅","≈","❣","✆","✪","☀","☽","⏣","✌","♲","☀","⚠","✿","♪","♫","⓪","ⓩ","▲","⁍","⚚","✖","✔","➜","﴾","﴿","☬","☄","⚑","Ⓑ","☺","♞","✷","⦾"),
        "Brackets" to listOf("【","】","《","》","『","』","〖","〗〈","〉","﴾","﴿"),
        "Arrows" to listOf("➤","▶","➸","➠","➦","➔","◀","⮜"),
        "Emoji" to listOf("✌","✍","❤","✔","✘","✖","☀","☂","☁","☺","☻","☹"),
        "Circles" to listOf("⓪","Ⓑ","ⓩ","⊙","㊣","☯","☢","㊚","㊛"),
        "Lines" to listOf("╝","╚","╔","╗","╬","═","╓","╩","┠","┨","┯","┷","┏","┓","┗","┛","┳","⊥","﹃","﹄","┌","┐","└","┘","∟","「","」"),
        "Squares" to listOf("▁","▂","▃","▄","▅","▆","▇","█","▀","▄","█","▐","░","▒","▬","▆","▇","█","█","■","▓")
    )

    val placeSymbol = clientCommand("housetils-internal")

    val command = clientCommand("symbols") {
        runs {
            var text = Text.literal("Copyable Unicode").setStyle(Style.EMPTY.withBold(true).withColor(0x11FF11))

            symbols.forEach { entry ->
                text = text.append(Text.literal("\n${entry.key}: ").setStyle(Style.EMPTY.withColor(0xFFF111).withBold(false)))

                entry.value.forEach { symbol ->
                    text = text.append(Text.literal(" $symbol ")
                        .setStyle(Style.EMPTY
                            .withColor(0x22ddFF)
                            .withBold(false)
                            .withClickEvent(
                                ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, symbol)
                            ).withHoverEvent(
                                HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click to copy $symbol").setStyle(Style.EMPTY.withColor(0x1188FF)))
                            )
                        )
                    )
                }
            }

            source.player.sendMessage(text)
        }

        alias("symbol")
        alias("unicode")
        alias("uni")
    }
}