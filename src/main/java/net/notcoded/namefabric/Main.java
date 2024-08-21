package net.notcoded.namefabric;

import net.fabricmc.api.ClientModInitializer;

//? if >=1.19 {
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
//?} elif <1.19 {
 /*import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;*///?}

import net.fabricmc.loader.api.FabricLoader;
import net.notcoded.namefabric.loaders.CommandLoader;

public class Main implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		//? if >=1.19 {
		if(FabricLoader.getInstance().isModLoaded("fabric-command-api-v2"))
			ClientCommandRegistrationCallback.EVENT.register(CommandLoader::registerCommands);
		//?} elif <1.19 {
		/*if(FabricLoader.getInstance().isModLoaded("fabric-command-api-v1"))
			CommandLoader.registerCommands(ClientCommandManager.DISPATCHER);
		*///?}


	}
}
