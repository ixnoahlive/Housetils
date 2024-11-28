package live.ixnoah.housetils.config

import io.wispforest.owo.config.annotation.Config
import io.wispforest.owo.config.annotation.Modmenu
import live.ixnoah.housetils.Housetils

@Modmenu(modId = Housetils.MOD_ID)
@Config(name = Housetils.MOD_ID, wrapperName="ModConfig")
class HousetilsConfigModel {
    @JvmField
    var intOption = 16
    @JvmField
    var boolToggle = false
}