package net.notcoded.namefabric.utils;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

//? if >=1.19 {
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
//?} elif <1.19 {
/*import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
*///?}

public class CommandUtil {
    public static final String PLAYER_OR_UUID_ARGUMENT = "player/uuid";

    public static Collection<String> getPlayerNames(FabricClientCommandSource source) {
        return source.getOnlinePlayerNames();
    }

    public static CompletableFuture<Suggestions> suggestPlayers(Collection<String> playerNames, SuggestionsBuilder builder) {
        for (String playerName : playerNames) {
            builder.suggest(playerName);
        }

        return builder.buildFuture();
    }

    public static RequiredArgumentBuilder<FabricClientCommandSource, String> playerOrUuidArgument() {
        return VersionUtil.argument(PLAYER_OR_UUID_ARGUMENT, StringArgumentType.string())
                .suggests((context, builder) -> suggestPlayers(getPlayerNames(context.getSource()), builder));
    }

    public static void registerPlayerOrUuid(
            CommandDispatcher<FabricClientCommandSource> dispatcher,
            String command,
            BiFunction<FabricClientCommandSource, String, Integer> uuidExecutor,
            BiFunction<FabricClientCommandSource, String, Integer> nameExecutor
    ) {
        dispatcher.register(VersionUtil.literal(command)
                .then(playerOrUuidArgument().executes(ctx -> executePlayerOrUuid(ctx, uuidExecutor, nameExecutor))));
    }

    private static int executePlayerOrUuid(
            CommandContext<FabricClientCommandSource> ctx,
            BiFunction<FabricClientCommandSource, String, Integer> uuidExecutor,
            BiFunction<FabricClientCommandSource, String, Integer> nameExecutor
    ) {
        String playerOrUUID = StringArgumentType.getString(ctx, PLAYER_OR_UUID_ARGUMENT);
        if (playerOrUUID.length() == 32 || playerOrUUID.length() == 36) {
            return uuidExecutor.apply(ctx.getSource(), playerOrUUID);
        }
        return nameExecutor.apply(ctx.getSource(), playerOrUUID);
    }
}
