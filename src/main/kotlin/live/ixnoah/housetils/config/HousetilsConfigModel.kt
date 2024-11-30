package live.ixnoah.housetils.config

import io.wispforest.owo.config.annotation.Config
import io.wispforest.owo.config.annotation.Modmenu
import io.wispforest.owo.config.annotation.SectionHeader
import live.ixnoah.housetils.Housetils

@Modmenu(modId = Housetils.MOD_ID)
@Config(name = Housetils.MOD_ID, wrapperName="ModConfig")
class HousetilsConfigModel {
    @JvmField
    var actionsAlphabetised = true

    @SectionHeader("Tweaks")
    @JvmField
    var tweaksAsteriskHidden = false

    @JvmField
    var tweaksAsteriskOverride = "&7*&r %message%"
}