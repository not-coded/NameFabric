package net.notcoded.namefabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.notcoded.namefabric.loaders.CommandLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
	@Override
	public void onInitialize() {
		ClientCommandRegistrationCallback.EVENT.register(CommandLoader::registerCommands);
	}
}
