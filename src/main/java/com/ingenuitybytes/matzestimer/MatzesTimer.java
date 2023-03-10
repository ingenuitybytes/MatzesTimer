package com.ingenuitybytes.matzestimer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.common.MinecraftForge;

import java.util.Timer;
import java.util.TimerTask;


@Mod(modid = MatzesTimer.MODID, version = MatzesTimer.VERSION)
public class MatzesTimer {
    public static final String MODID = "matzestimer";
    public static final String VERSION = "1.0.0";

    private static final KeyBinding KEY_TIMER = new KeyBinding("Starte 2 Minuten Timer", Keyboard.KEY_N, "key.categories.gameplay");


    private static Timer timer;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientRegistry.registerKeyBinding(KEY_TIMER);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onKeyInputEvent(InputEvent.KeyInputEvent event) {
        if (KEY_TIMER.isPressed()) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Der Timer wurde gestartet!"));
            startTimer();
        }
    }

    private static void startTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("2 Minuten sind um!"));
            }
        }, 2 * 60 * 1000); // 2 Minuten in Millisekunden
    }
}
