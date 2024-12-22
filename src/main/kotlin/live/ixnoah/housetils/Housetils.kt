package live.ixnoah.housetils

import live.ixnoah.housetils.command.CommandManager
import live.ixnoah.housetils.config.ModConfig
import live.ixnoah.housetils.feature.FeatureManager
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object Housetils : ModInitializer {
	const val MOD_ID = "housetils"
	private val logger = LoggerFactory.getLogger(MOD_ID)
	val config = ModConfig.createAndLoad()
	override fun onInitialize() {
		CommandManager()
		FeatureManager()
	}
}