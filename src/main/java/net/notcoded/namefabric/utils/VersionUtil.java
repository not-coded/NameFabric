package net.notcoded.namefabric.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;

//? if >=1.19 {
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
//?} elif <1.19 {
/*import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.TranslatableText;
import net.minecraft.text.LiteralText;
*///?}

public class VersionUtil {

    public static JsonElement parseString(String response) {
        //? if >=1.19 {
        return JsonParser.parseString(response);
        //?} elif <1.19 {
         /*return new JsonParser().parse(response);        *///?}
    }

    public static void sendError(FabricClientCommandSource source, String translatable, Object... args) {

        //? if >=1.19 {
        source.sendError(Text.translatable(translatable, args));
        //?} elif <1.19 {
         /*source.sendError(new TranslatableText(translatable, args));        *///?}
    }


    public static Text webLinkText(String link) {
        //? if >=1.19 {
        return Text.literal(link).styled(style -> style
                .withUnderline(true)
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("click.open.link")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link))
        );
        //?} elif <1.19 {
        /*return new LiteralText(link).styled(style -> style
                .withUnderline(true)
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("click.open.link")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link))
        );
        *///?}
    }

    public static Text copyUUIDText(String uuid) {
        //? if >=1.19 {
        return Text.literal(uuid).styled(style -> style
                .withUnderline(true)
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("click.copy.uuid")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, uuid))
        );
        //?} elif <1.19 {
        /*return new LiteralText(uuid).styled(style -> style
                .withUnderline(true)
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TranslatableText("click.copy.uuid")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, uuid))
        );
        *///?}
    }


    public static void sendFeedback(FabricClientCommandSource source, String translatable, Object... args) {

        //? if >=1.19 {
        source.sendFeedback(Text.translatable(translatable, args));
        //?} elif <1.19 {
         /*source.sendFeedback(new TranslatableText(translatable, args));        *///?}
    }

}
