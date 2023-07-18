package net.notcoded.namefabric.command;


import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.notcoded.namefabric.Main;
import net.notcoded.namefabric.utils.MinecraftAPI;
import org.jetbrains.annotations.NotNull;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.command.CommandSource.suggestMatching;

public class GetUuidCommand {


    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("getname")
                .then(ClientCommandManager.argument("player/uuid", string())
                        .suggests((context, builder) -> suggestMatching(context.getSource().getPlayerNames(), builder))
                        .executes(ctx -> {
                            if (getString(ctx, "player/uuid").length() == 32 || getString(ctx, "player/uuid").length() == 36) {
                                try {
                                    return getNamesUUID(ctx.getSource(), getString(ctx, "player/uuid"));
                                } catch (Exception e) {
                                    ctx.getSource().getPlayer().sendMessage(Text.literal(Main.prefix + Text.translatable("command.all.error").getString()));
                                    return Command.SINGLE_SUCCESS;
                                }
                            } else {
                                try {
                                    return getUuidName(ctx.getSource(), getString(ctx, "player/uuid"));
                                } catch (Exception e) {
                                    ctx.getSource().getPlayer().sendMessage(Text.literal(Main.prefix + Text.translatable("command.all.error").getString()));
                                    return Command.SINGLE_SUCCESS;
                                }
                            }
                        })));
        dispatcher.register(ClientCommandManager.literal("getuuid")
                .then(ClientCommandManager.argument("player/uuid", string())
                        .suggests((context, builder) -> suggestMatching(context.getSource().getPlayerNames(), builder))
                        .executes(ctx -> {
                            if (getString(ctx, "player/uuid").length() == 32 || getString(ctx, "player/uuid").length() == 36) {
                                try {
                                    return getNamesUUID(ctx.getSource(), getString(ctx, "player/uuid"));
                                } catch (Exception e) {
                                    ctx.getSource().sendFeedback(Text.literal(Main.prefix + Text.translatable("command.all.error").getString()));
                                    return Command.SINGLE_SUCCESS;
                                }
                            } else {
                                try {
                                    return getUuidName(ctx.getSource(), getString(ctx, "player/uuid"));
                                } catch (Exception e) {
                                    ctx.getSource().sendFeedback(Text.literal(Main.prefix + Text.translatable("command.all.error").getString()));
                                    return Command.SINGLE_SUCCESS;
                                }
                            }
                        })));
    }


    private static int getNamesUUID(FabricClientCommandSource source, @NotNull String uuid) {
        String name = MinecraftAPI.getName(uuid);
        if (name != null && name.trim().length() != 0) {
            try {
                Text uuidText = Text.literal(uuid).styled(style -> style
                        .withUnderline(true)
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("Click to copy the uuid!")))
                        .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, uuid))
                );
                source.sendFeedback(Text.translatable("command.getuuid.uuid.success", uuidText, name));
            } catch (Exception e) {
                source.sendError(Text.literal(Main.prefix + Text.translatable("command.all.error").getString()));
            }
        } else {
            source.sendError(Text.literal(Main.prefix + Text.translatable("command.all.error").getString()));
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int getUuidName(FabricClientCommandSource source, String name) {
        String uuid = MinecraftAPI.getUUID(name);
        if (uuid != null && uuid.trim().length() != 0) {
            try {
                Text uuidText = Text.literal(uuid).styled(style -> style
                        .withUnderline(true)
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("Click to copy the uuid!")))
                        .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, uuid))
                );
                source.sendFeedback(Text.translatable("command.getuuid.name.success", name, uuidText));

            } catch (Exception e) {
                source.sendError(Text.literal(Main.prefix + Text.translatable("command.all.error").getString()));
            }
        } else {
            source.sendError(Text.literal(Main.prefix + Text.translatable( "command.all.invalid.name").getString()));
        }
        return Command.SINGLE_SUCCESS;
    }
}
