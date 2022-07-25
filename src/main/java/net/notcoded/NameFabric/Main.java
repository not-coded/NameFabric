package net.notcoded.namefabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.notcoded.namefabric.loaders.CommandLoader;

public class Main implements ModInitializer {
	public static final String PREFIX = "§8[§7NameFabric§8] ";

	@Override
	public void onInitialize() {
		CommandLoader.registerCommands(ClientCommandManager.DISPATCHER);
	}
}
