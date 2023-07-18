package net.notcoded.namefabric.command;


import com.mojang.brigadier.Command;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.*;

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
                                    ctx.getSource().sendError(new TranslatableText("command.all.error"));
                                    return Command.SINGLE_SUCCESS;
                                }
                            } else {
                                try {
                                    return getUuidName(ctx.getSource(), getString(ctx, "player/uuid"));
                                } catch (Exception e) {
                                    ctx.getSource().sendError(new TranslatableText("command.all.error"));
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
                                    ctx.getSource().sendError(new TranslatableText("command.all.error"));
                                    return Command.SINGLE_SUCCESS;
                                }
                            } else {
                                try {
                                    return getUuidName(ctx.getSource(), getString(ctx, "player/uuid"));
                                } catch (Exception e) {
                                    ctx.getSource().sendError(new TranslatableText("command.all.error"));
                                    return Command.SINGLE_SUCCESS;
                                }
                            }
                        })));
    }


    private static int getNamesUUID(FabricClientCommandSource source, @NotNull String uuid) {
        String name = MinecraftAPI.getName(uuid);
        if (name != null && name.trim().length() != 0) {
            try {
                Text uuidText = new LiteralText(uuid).styled(style -> style
                        .withUnderline(true)
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("click.copy.uuid")))
                        .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, uuid))
                );
                source.sendFeedback(new TranslatableText("command.getuuid.uuid.success", uuidText, name));
            } catch (Exception e) {
                source.sendError(new TranslatableText("command.all.error"));
            }
        } else {
            source.sendError(new TranslatableText("command.all.error"));
        }
        return Command.SINGLE_SUCCESS;
    }

    public static int getUuidName(FabricClientCommandSource source, String name) {
        String uuid = MinecraftAPI.getUUID(name);
        if (uuid != null && uuid.trim().length() != 0) {
            try {
                Text uuidText = new LiteralText(uuid).styled(style -> style
                        .withUnderline(true)
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("click.copy.uuid")))
                        .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, uuid))
                );
                source.sendFeedback(new TranslatableText("command.getuuid.name.success", name, uuidText));

            } catch (Exception e) {
                source.sendError(new TranslatableText("command.all.error"));
            }
        } else {
            source.sendError(new TranslatableText("command.all.invalid.name"));
        }
        return Command.SINGLE_SUCCESS;
    }
}
