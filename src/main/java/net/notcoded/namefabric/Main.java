package net.notcoded.namefabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.loader.api.FabricLoader;
import net.notcoded.namefabric.loaders.CommandLoader;

public class Main implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		if(FabricLoader.getInstance().isModLoaded("fabric-command-api-v1"))
			CommandLoader.registerCommands(ClientCommandManager.DISPATCHER);
	}
}
