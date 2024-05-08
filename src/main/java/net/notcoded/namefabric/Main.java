package net.notcoded.namefabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.notcoded.namefabric.loaders.CommandLoader;

public class Main implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		if(FabricLoader.getInstance().isModLoaded("fabric-command-api-v2"))
			ClientCommandRegistrationCallback.EVENT.register(CommandLoader::registerCommands);
	}
}
