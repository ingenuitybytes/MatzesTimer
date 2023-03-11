package com.ingenuitybytes.matzestimer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import net.minecraftforge.fml.client.config.GuiConfig;

@Mod(modid = MatzesTimer.MODID, version = MatzesTimer.VERSION)
public class MatzesTimer {
    public static final String MODID = "matzestimer";
    public static final String VERSION = "1.1.0";
    public static final String CATEGORY_MYMOD = "Matzes Timer";

    private static final KeyBinding KEY_TIMER = new KeyBinding("Starte den Timer", Keyboard.KEY_N,
            CATEGORY_MYMOD);
    private static final KeyBinding KEY_CONFIG = new KeyBinding("Öffne das Config Menü", Keyboard.KEY_C,
            CATEGORY_MYMOD);

    private static int TIMER_DURATION = 120; // 2 minutes in seconds

    public static Configuration config;
    private static Timer timer;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientRegistry.registerKeyBinding(KEY_TIMER);
        ClientRegistry.registerKeyBinding(KEY_CONFIG);
        MinecraftForge.EVENT_BUS.register(this);

        // Load and save the configuration file
        File configFile = new File(Minecraft.getMinecraft().mcDataDir, "config/" + MODID + ".cfg");
        config = new Configuration(configFile);
        config.load();
        int defaultDuration = 120; // 2 minutes in seconds
        int duration = config.getInt("Timer Zeit", Configuration.CATEGORY_GENERAL, defaultDuration, 10, 3600,
                "The duration of the timer in seconds (between 10 and 3600)");
        config.addCustomCategoryComment(CATEGORY_MYMOD, "Keybinds für Matzes Timer");
        config.save();

        // Update the timer duration
        TIMER_DURATION = duration;
    }

    @SubscribeEvent
    public void onKeyInputEvent(InputEvent.KeyInputEvent event) {
        if (KEY_TIMER.isPressed()) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Der Timer wurde gestartet!"));
            startTimer();
        } else if (KEY_CONFIG.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new MatzesTimerConfigGui(Minecraft.getMinecraft().currentScreen));
        }
    }

    public static class MatzesTimerConfigGui extends GuiConfig {
        public MatzesTimerConfigGui(GuiScreen parentScreen) {
            super(parentScreen,
                    new ConfigElement(config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
                    MODID,
                    false,
                    false,
                    GuiConfig.getAbridgedConfigPath(config.toString()));
        }
    }

    private static void startTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            int count = TIMER_DURATION;

            @Override
            public void run() {
                if (count > 10) {
                    count--;
                } else if (count > 0) {
                    count--;
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(
                            EnumChatFormatting.RED + String.format("Noch %d Sekunden übrig!", count)));
                } else {
                    timer.cancel();
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Der Timer ist um!"));
                    config.save(); // Speichert die Konfigurationsdatei
                }
            }
        }, 0, 1000); // 1 second delay between each count
    }
}