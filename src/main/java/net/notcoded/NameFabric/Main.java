package net.notcoded.namefabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.notcoded.namefabric.loaders.CommandLoader;

public class Main implements ModInitializer {
	public static String prefix = "§8[§7NameFabric§8] §f";

	@Override
	public void onInitialize() {
		ClientCommandRegistrationCallback.EVENT.register(CommandLoader::registerCommands);
	}
}
