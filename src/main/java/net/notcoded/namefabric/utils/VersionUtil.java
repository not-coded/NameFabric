package net.notcoded.namefabric.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
//? if <1.19 {
/*import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
*///?}

import java.net.URI;

//? if >=26.1 {
/*import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
*///?} elif >=1.19 {
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
//?} elif <1.19 {
/*import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
*///?}

public class VersionUtil {
    public static LiteralArgumentBuilder<FabricClientCommandSource> literal(String name) {
        //? if >=26.1 {
        /*return ClientCommands.literal(name);
        *///?} else {
        return ClientCommandManager.literal(name);
        //?}
    }

    public static <T> RequiredArgumentBuilder<FabricClientCommandSource, T> argument(String name, ArgumentType<T> type) {
        //? if >=26.1 {
        /*return ClientCommands.argument(name, type);
        *///?} else {
        return ClientCommandManager.argument(name, type);
        //?}
    }

    public static JsonElement parseString(String response) {
        //? if >=1.19 {
        return JsonParser.parseString(response);
        //?} elif <1.19 {
        /*return new JsonParser().parse(response);        *///?}
    }

    public static void sendError(FabricClientCommandSource source, String translatable, Object... args) {
        //? if >=1.19 {
        source.sendError(Component.translatable(translatable, args));
        //?} elif <1.19 {
        /*source.sendError(new TranslatableComponent(translatable, args));        *///?}
    }

    public static Object webLinkText(String link) {
        //? if >=1.21.5 {
        /*return Component.literal(link).withStyle(style -> style
                .withUnderlined(true)
                .withHoverEvent(new HoverEvent.ShowText(Component.translatable("click.open.link")))
                .withClickEvent(new ClickEvent.OpenUrl(URI.create(link)))
        );
        *///?} elif >=1.19 {
        return Component.literal(link).withStyle(style -> style
                .withUnderlined(true)
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("click.open.link")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link))
        );
        //?} elif <1.19 {
        /*return new TextComponent(link).withStyle(style -> style
                .withUnderlined(true)
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("click.open.link")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link))
        );
        *///?}
    }

    public static Object copyUUIDText(String uuid) {
        //? if >=1.21.5 {
        /*return Component.literal(uuid).withStyle(style -> style
                .withUnderlined(true)
                .withHoverEvent(new HoverEvent.ShowText(Component.translatable("click.copy.uuid")))
                .withClickEvent(new ClickEvent.CopyToClipboard(uuid))
        );
        *///?} elif >=1.19 {
        return Component.literal(uuid).withStyle(style -> style
                .withUnderlined(true)
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("click.copy.uuid")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, uuid))
        );
        //?} elif <1.19 {
        /*return new TextComponent(uuid).withStyle(style -> style
                .withUnderlined(true)
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("click.copy.uuid")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, uuid))
        );
        *///?}
    }

    public static Object copyNameText(String name) {
        //? if >=1.21.5 {
        /*return Component.literal(name).withStyle(style -> style
                .withUnderlined(true)
                .withHoverEvent(new HoverEvent.ShowText(Component.translatable("click.copy.name")))
                .withClickEvent(new ClickEvent.CopyToClipboard(name))
        );
        *///?} elif >=1.19 {
        return Component.literal(name).withStyle(style -> style
                .withUnderlined(true)
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("click.copy.name")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, name))
        );
        //?} elif <1.19 {
        /*return new TextComponent(name).withStyle(style -> style
                .withUnderlined(true)
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableComponent("click.copy.name")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, name))
        );
        *///?}
    }

    public static void sendFeedback(FabricClientCommandSource source, String translatable, Object... args) {
        //? if >=1.19 {
        source.sendFeedback(Component.translatable(translatable, args));
        //?} elif <1.19 {
        /*source.sendFeedback(new TranslatableComponent(translatable, args));        *///?}
    }
}
