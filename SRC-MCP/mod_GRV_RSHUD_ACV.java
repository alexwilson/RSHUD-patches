package net.minecraft.src;

import java.util.Map;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;

public class mod_GRV_RSHUD_ACV extends BaseMod {
	
	@MLProp(info="Normal Color.")
	public static String Color_Normal = "b199ffb2";
	@MLProp(info="Warning Message Color.")
	public static String Color_Warning = "b1ffff3f";
	@MLProp(info="Alert Messaege Color.")
	public static String Color_Alert = "b1ff3f3f";
	@MLProp(info="Marker Color.")
	public static String Color_Marker = "e6667fff";
	@MLProp(info="Marker Color.")
	public static float LowGain = 0.3F;
	@MLProp(info="Marker Color.")
	public static float TextSize = 0.5F;
	@MLProp(info="RECON's ItemID.(ShiftIndex = ItemID - 256, 0 is not use.)", max = 31999)
	public static int itemIDRECON = 22250;
	@MLProp(info="Show Status Massage")
	public static boolean Show_Status = true;

	
	public static GRV_GuiRSHUD_ACV rshud;
	public static Item itemRECON;
	public static int uniqueRECON;
	

	
	@Override
	public String getVersion() {
		return "1.3.2-2";
	}

	@Override
	public String getPriorities() {
		return "after:mod_GRH_RSHUD";
	}
	
	@Override
	public void load() {
		try {
			rshud = new GRV_GuiRSHUD_ACV(this);
			mod_GRH_RSHUD.addHUD(rshud);
			
			// èâä˙ílÇÃì«Ç›çûÇ›
	        Color_Normal = "00000000".concat(Color_Normal);
	        Color_Normal = Color_Normal.substring(Color_Normal.length() - 8);
	        Color_Warning = "00000000".concat(Color_Warning);
	        Color_Warning = Color_Warning.substring(Color_Warning.length() - 8);
	        Color_Alert = "00000000".concat(Color_Alert);
	        Color_Alert = Color_Alert.substring(Color_Alert.length() - 8);
	        Color_Marker = "00000000".concat(Color_Marker);
	        Color_Marker = Color_Marker.substring(Color_Marker.length() - 8);
	        rshud.ColorInt_Normal = Integer.parseInt(Color_Normal.substring(0, 4), 16) << 16 | Integer.parseInt(Color_Normal.substring(4, 8), 16);
	        rshud.ColorInt_Warning = Integer.parseInt(Color_Warning.substring(0, 4), 16) << 16 | Integer.parseInt(Color_Warning.substring(4, 8), 16);
	        rshud.ColorInt_Alert = Integer.parseInt(Color_Alert.substring(0, 4), 16) << 16 | Integer.parseInt(Color_Alert.substring(4, 8), 16);
	        rshud.ColorInt_Maker = Integer.parseInt(Color_Marker.substring(0, 4), 16) << 16 | Integer.parseInt(Color_Marker.substring(4, 8), 16);
			rshud.lowGain = LowGain;
			rshud.textSize = TextSize;
			rshud.isStatus = Show_Status;
	        
			String s = "key.RSHUD.ACV.Route";
			ModLoader.registerKey(this, new KeyBinding(s, 49), false);
	        ModLoader.addLocalization(
	        		(new StringBuilder()).append(s).toString(),
	        		(new StringBuilder()).append("RouteView").toString()
	        		);
	        
	        // RECON
	        if (itemIDRECON > 0) {
				itemRECON = (new GRV_ItemRECON(itemIDRECON - 256)).setIconCoord(14, 0).setItemName("recon");
		        ModLoader.addName(itemRECON, "RECON");
//		        ModLoader.addName(itemRECON, "ja_JP", "íTç∏ã@");
		        ModLoader.addRecipe(new ItemStack(itemRECON, 8), new Object[] {
					"E", 
					"R", 
					"I", 
					Character.valueOf('E'), Item.spiderEye,
					Character.valueOf('I'), Item.ingotIron,
					Character.valueOf('R'), Item.redstone
		        });
		        uniqueRECON = ModLoader.getUniqueEntityId();
		        ModLoader.registerEntityID(GRV_EntityRECON.class, "RECON", ModLoader.getUniqueEntityId());
		        ModLoader.addEntityTracker(this, GRV_EntityRECON.class, uniqueRECON, 80, 10, true);
	        }
	        
		}
		catch (NoClassDefFoundError e) {
			System.out.print("not Found RSHUD.");
		}
	}
	
	@Override
	public void keyboardEvent(KeyBinding keybinding) {
    	Minecraft mcGame = ModLoader.getMinecraftInstance();
    	if (mcGame.theWorld != null && mcGame.currentScreen == null && mod_GRH_RSHUD.selectHUD == rshud) {
    		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
        		rshud.setStartRoute(mcGame.thePlayer);
        		System.out.println("Route Reset.");
    		} else {
        		rshud.toggleRouteView(mcGame.thePlayer);
        		System.out.println("Route.");
    		}
    	}
	}

	@Override
	public void addRenderer(Map map) {
		map.put(GRV_EntityMARKER.class, new GRV_RenderMARKER());
		map.put(GRV_EntityRECON.class, new RenderSnowball(Item.snowball.getIconFromDamage(0)));
	}
	
	@Override
	public Packet23VehicleSpawn getSpawnPacket(Entity var1, int var2) {
		EntityLiving lthrower = ((GRV_EntityRECON)var1).thrower;
		return new GRV_PacketRECONSpawn(var1, 0, lthrower == null ? 0 : lthrower.entityId);
	}
}
