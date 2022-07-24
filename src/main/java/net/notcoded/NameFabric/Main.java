package net.notcoded.namefabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.notcoded.namefabric.loaders.CommandLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("NameFabric");
	public static final String PREFIX = "§8[§7NameFabric§8] ";

	@Override
	public void onInitialize() {
		ClientCommandRegistrationCallback.EVENT.register(CommandLoader::registerCommands);
	}
}
