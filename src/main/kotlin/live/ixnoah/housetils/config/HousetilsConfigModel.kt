package live.ixnoah.housetils.config

import io.wispforest.owo.config.annotation.Config

@Config(name = "housetils", wrapperName="HousetilsConfig")
class HousetilsConfigModel {
    var intOption = 16
    var boolToggle = false
}