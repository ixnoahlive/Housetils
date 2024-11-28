package live.ixnoah.housetils

import live.ixnoah.housetils.command.CommandManager
import net.fabricmc.api.ModInitializer

import org.slf4j.LoggerFactory

object Housetils : ModInitializer {
    private val logger = LoggerFactory.getLogger("housetils")


	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		CommandManager()
	}
}