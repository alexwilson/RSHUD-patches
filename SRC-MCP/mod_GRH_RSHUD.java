package net.minecraft.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.logging.Logger;

import net.minecraft.client.Minecraft;

public class mod_GRH_RSHUD extends BaseMod
{
    @MLProp(info = "Normal Color.")
    public static String Color_Normal = "cc4cff7f";
    @MLProp(info = "Warning Message Color.")
    public static String Color_Warning = "e5ffff00";
    @MLProp(info = "Alert Message Color.")
    public static String Color_Alert = "e5ff0000";
    @MLProp(info = "Line Alpha Value.", min = 0.0F, max = 1.0F)
    public static float LineAlpha = 0.8F;
    @MLProp(info = "Line Width.", min = 0.5F)
    public static float LineWidth = 1.0F;
    @MLProp(info = "Degree Offset.", min = 0, max = 36)
    public static int DegOffset = 18;

    @MLProp(info = "Weapon & Ammo List(Bow:Arrow[,Arrow;Bow:Arrow,Arrow];)")
    public static String projectionArms = "261:262;";
    @MLProp(info = "Replace GuiIngame.")
    public static boolean replaceGuiIngame = false;
    @MLProp(info = "GUI Enable(dont't use GUI is false)")
    public static boolean guiEnable = true;
    @MLProp(info = "Default HUD")
    public static String HUDName = "TypeA";

    public static KeyBinding guiKey;
    public static GRH_GuiRSHUDConfigure selectHUD;
    public static GRH_GuiRSHUDConfigure rshud;
    public static Map<String, GRH_GuiRSHUDConfigure> HUDList = new TreeMap<String, GRH_GuiRSHUDConfigure>();
    public static mod_GRH_RSHUD instance;

    @Override
    public String getVersion()
    {
        return "1.4.5";
    }

    @Override
    public String getPriorities()
    {
        return "before:*";
    }

    @Override
    public void load()
    {
        if (replaceGuiIngame)
        {
            ModLoader.setInGUIHook(this, true, false);
        }
        else
        {
            ModLoader.setInGameHook(this, true, false);
        }

        if (guiEnable)
        {
            String s = "key.RSHUD";
            guiKey = new KeyBinding(s, 25);
            ModLoader.registerKey(this, guiKey, false);
            ModLoader.addLocalization(
                    (new StringBuilder()).append(s).toString(),
                    (new StringBuilder()).append("RSHUD Gui").toString()
            );
        }

        selectHUD = null;
        addHUD(new GRH_GuiRSHUDConfigure(this));
        addHUD(rshud = new GRH_GuiRSHUD_TypeA(this));
        Color_Normal = "00000000".concat(Color_Normal);
        Color_Normal = Color_Normal.substring(Color_Normal.length() - 8);
        Color_Warning = "00000000".concat(Color_Warning);
        Color_Warning = Color_Warning.substring(Color_Warning.length() - 8);
        Color_Alert = "00000000".concat(Color_Alert);
        Color_Alert = Color_Alert.substring(Color_Alert.length() - 8);
        rshud.ColorInt_Normal	= (Integer.parseInt(Color_Normal.substring(0, 4), 16) << 16) | Integer.parseInt(Color_Normal.substring(4, 8), 16);
        rshud.ColorInt_Warning	= (Integer.parseInt(Color_Warning.substring(0, 4), 16) << 16) | Integer.parseInt(Color_Warning.substring(4, 8), 16);
        rshud.ColorInt_Alert	= (Integer.parseInt(Color_Alert.substring(0, 4), 16) << 16) | Integer.parseInt(Color_Alert.substring(4, 8), 16);
        rshud.LineR = (float)((rshud.ColorInt_Normal >> 16) & 0xff) / 255F;
        rshud.LineG = (float)((rshud.ColorInt_Normal >> 8) & 0xff) / 255F;
        rshud.LineB = (float)(rshud.ColorInt_Normal & 0xff) / 255F;
        instance = this;
    }

    @Override
    public void modsLoaded()
    {
        String[] w1 = projectionArms.split(";");

        for (int i = 0; i < w1.length; i++)
        {
            String[] w2 = w1[i].split(":");

            if (w2.length < 2)
            {
                continue;
            }

            for (int j = 0; j < w2.length; j++)
            {
                String[] w3 = w2[1].split(",");
                setAmmoIndex(w2[0], w3);
            }
        }
    }

    @Override
    public void keyboardEvent(KeyBinding keybinding)
    {
        Minecraft mcGame = ModLoader.getMinecraftInstance();

        if (mcGame.theWorld != null && mcGame.currentScreen == null)
        {
            ModLoader.openGUI(mcGame.thePlayer, selectHUD);
        }
    }

    @Override
    public boolean onTickInGUI(float f, Minecraft minecraft, GuiScreen guiscreen)
    {
        minecraft.ingameGUI = new GRH_GuiIngameRSHUD(minecraft);
        return false;
    }

    @Override
    public boolean onTickInGame(float f, Minecraft minecraft)
    {
        if (minecraft.currentScreen == null || minecraft.currentScreen instanceof GuiChat)
        {
            ScaledResolution scaledresolution = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
            int k = scaledresolution.getScaledWidth();
            int l = scaledresolution.getScaledHeight();
            selectHUD.renderRSHUD(minecraft, k, l, f);
        }

        return true;
    }

    private boolean setAmmoIndex(String weaponIndex, String[] ammoIndex)
    {
        try
        {
            int projectionWeapon = Integer.valueOf(weaponIndex);
            List<Integer> weaponAmmos = new ArrayList<Integer>();

            for (int j = 0; j < ammoIndex.length; j++)
            {
                int k = Integer.valueOf(ammoIndex[j]);
                weaponAmmos.add(k);
            }

            if (weaponAmmos.isEmpty())
            {
                return false;
            }

            GRH_GuiRSHUDConfigure.projectorList.put(projectionWeapon, weaponAmmos);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public static void setupProperties(BaseMod basemod)
    throws IllegalArgumentException, IllegalAccessException, IOException, SecurityException, NoSuchFieldException
    {
//    	System.out.println("write property.");
        HUDName = selectHUD.getHUDName();
        Logger logger = ModLoader.getLogger();
        Class class1 = basemod.getClass();
        Properties properties = new Properties();
        File cfgdir = new File(Minecraft.getMinecraftDir(), "/config/");
        File file = new File(cfgdir, (new StringBuilder(String.valueOf(class1.getSimpleName()))).append(".cfg").toString());

        if (file.exists() && file.canRead())
        {
            properties.load(new FileInputStream(file));
        }

        StringBuilder stringbuilder = new StringBuilder();
        Field afield[];
        int j = (afield = class1.getFields()).length;

        for (int i = 0; i < j; i++)
        {
            Field field = afield[i];

            if ((field.getModifiers() & 8) == 0 || !field.isAnnotationPresent(net.minecraft.src.MLProp.class))
            {
                continue;
            }

            Class class2 = field.getType();
            MLProp mlprop = (MLProp)field.getAnnotation(net.minecraft.src.MLProp.class);
            String s = mlprop.name().length() != 0 ? mlprop.name() : field.getName();
            Object obj = field.get(null);
            StringBuilder stringbuilder1 = new StringBuilder();

            if (mlprop.min() != (-1.0D / 0.0D))
            {
                stringbuilder1.append(String.format(",>=%.1f", new Object[]
                        {
                            Double.valueOf(mlprop.min())
                        }));
            }

            if (mlprop.max() != (1.0D / 0.0D))
            {
                stringbuilder1.append(String.format(",<=%.1f", new Object[]
                        {
                            Double.valueOf(mlprop.max())
                        }));
            }

            StringBuilder stringbuilder2 = new StringBuilder();

            if (mlprop.info().length() > 0)
            {
                stringbuilder2.append(" -- ");
                stringbuilder2.append(mlprop.info());
            }

            stringbuilder.append(String.format("%s (%s:%s%s)%s\n", new Object[]
                    {
                        s, class2.getName(), obj, stringbuilder1, stringbuilder2
                    }));
            logger.finer((new StringBuilder(String.valueOf(s))).append(" set to ").append(obj).toString());
            properties.setProperty(s, obj.toString());
        }

        if (!properties.isEmpty() && (file.exists() || file.createNewFile()) && file.canWrite())
        {
            properties.store(new FileOutputStream(file), stringbuilder.toString());
        }
    }

    public static void addHUD(GRH_GuiRSHUDConfigure guirshud)
    {
        HUDList.put(guirshud.getHUDName(), guirshud);

        if (HUDName.equals(guirshud.getHUDName()))
        {
            selectHUD = guirshud;
        }
    }

    public static GRH_GuiRSHUDConfigure getNextHUD(String name)
    {
        boolean flag = false;

        for (Map.Entry<String, GRH_GuiRSHUDConfigure> et : HUDList.entrySet())
        {
            if (flag)
            {
                return et.getValue();
            }

            flag = name.equals(et.getKey());
        }

        for (Map.Entry<String, GRH_GuiRSHUDConfigure> et : HUDList.entrySet())
        {
            return et.getValue();
        }

        return null;
    }
}
