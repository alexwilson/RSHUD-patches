package net.minecraft.src;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

public class GRH_GuiRSHUDConfigure extends GuiScreen {

	public BaseMod modbase;
	public static Map<Integer, List<Integer>> projectorList = new HashMap<Integer, List<Integer>>();

	protected GRH_GuiRSHUDSlider deg;
	protected GRH_GuiRSHUDSlider linea;
	protected GRH_GuiRSHUDSlider linew;
	protected GRH_GuiRSHUDSlider[] colornormal = new GRH_GuiRSHUDSlider[4];
	protected GRH_GuiRSHUDSlider[] colorwarning = new GRH_GuiRSHUDSlider[4];
	protected GRH_GuiRSHUDSlider[] coloralert = new GRH_GuiRSHUDSlider[4];
	protected int hwSize;
	protected int hhSize;

	public float LineR;
	public float LineG;
	public float LineB;
	public int ColorInt_Normal;
	public int ColorInt_Warning;
	public int ColorInt_Alert;

	
	public GRH_GuiRSHUDConfigure(BaseMod basemod) {
		modbase = basemod;
	}
	
	@Override
	public void initGui() {
		//
		hwSize = width / 2;
		hhSize = height / 2;
		controlList.add(new GuiButton(100, hwSize - 160, hhSize -106 + 24, 100, 20, "Weapon Set"));
		controlList.add(new GuiButton(200, hwSize - 160, hhSize -106 + 0, 100, 20, getHUDName()));
	}
	
	@Override
	public void onGuiClosed() {
		try {
			mod_GRH_RSHUD.setupProperties(modbase);
			if (modbase != mod_GRH_RSHUD.instance) {
				mod_GRH_RSHUD.setupProperties(mod_GRH_RSHUD.instance);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
    protected void keyTyped(char c, int i) {
		super.keyTyped(c, i);
        if(i == 1 || i == mod_GRH_RSHUD.guiKey.keyCode) {
//        	mc.displayGuiScreen(null);
        }
    }

	@Override
	protected void actionPerformed(GuiButton guibutton) {
		if (guibutton.id == 100) {
			mc.displayGuiScreen(new GRH_GuiContainerRSHUD(mc.thePlayer, this));
		}
		if (guibutton.id == 200) {
			nextGui();
		}
	}
	
	@Override
	public void drawScreen(int i, int j, float f) {
        if(!mod_GRH_RSHUD.replaceGuiIngame) {
            renderRSHUD(mc, width, height);
        }
        drawDefaultBackground();
		super.drawScreen(i, j, f);
	}
	
	@Override
	public void drawDefaultBackground() {
	}
	
	protected float[] getRGBA(int color) {
		float f[] = new float[4];
		f[0] = (float)((color >> 16) & 0xff) / 255F;
		f[1] = (float)((color >> 8) & 0xff) / 255F;
		f[2] = (float)(color & 0xff) / 255F;
		f[3] = (float)((color >> 24) & 0xff) / 255F;
		return f;
	}
	
	protected int setRGBA(float r, float g, float b, float a) {
		return (((int)(255F * a) & 0xff) << 24) | (((int)(255F * r) & 0xff) << 16) | (((int)(255F * g) & 0xff) << 8) | ((int)(255F * b) & 0xff);
	}
	
	public String getHUDName() {
		return "HUD OFF";
	}
	
	public void renderRSHUD(Minecraft mc, int i, int j) {
	}

	public void renderRSHUD(Minecraft mc, int i, int j, float f) {
		renderRSHUD(mc, i, j);
	}

	public boolean containsAmmo(ItemStack itemstack) {
		return itemstack != null && projectorList.containsKey(itemstack.getItem().shiftedIndex);
	}
	
	public int countAmmo(ItemStack itemstack, EntityPlayer entityplayer) {
        int count = 0;
        List<Integer> clist = projectorList.get(itemstack.getItem().shiftedIndex);
        for (int li1 = 0; li1 < entityplayer.inventory.mainInventory.length; li1++) {
        	ItemStack itemstack2 = entityplayer.inventory.mainInventory[li1];
        	if (itemstack2 != null && clist.contains(itemstack2.getItem().shiftedIndex)) {
        		count += itemstack2.stackSize;
        	}
        }
        return count;
	}
	
	protected int getArmorColor(ItemStack itemstack) {
        float f = (float)itemstack.getItemDamage() / (float)itemstack.getMaxDamage();
        if (f >= 0.8F) return ColorInt_Alert;
        if (f >= 0.6F) return ColorInt_Warning;
    	return ColorInt_Normal;
	}
	
	protected void drawRectL(int i, int j, int k, int l, int i1)
    {
        if(i < k)
        {
            int j1 = i;
            i = k;
            k = j1;
        }
        if(j < l)
        {
            int k1 = j;
            j = l;
            l = k1;
        }
        float f = (float)(i1 >> 24 & 0xff) / 255F;
        float f1 = (float)(i1 >> 16 & 0xff) / 255F;
        float f2 = (float)(i1 >> 8 & 0xff) / 255F;
        float f3 = (float)(i1 & 0xff) / 255F;
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(f1, f2, f3, f);
        tessellator.startDrawingQuads();
        tessellator.addVertex(i, l, 0.0D);
        tessellator.addVertex(k, l, 0.0D);
        tessellator.addVertex(k, j, 0.0D);
        tessellator.addVertex(i, j, 0.0D);
        tessellator.draw();
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
        GL11.glDisable(3042 /*GL_BLEND*/);
    }

	public void nextGui() {
		mod_GRH_RSHUD.selectHUD = mod_GRH_RSHUD.getNextHUD(getHUDName());
		mc.displayGuiScreen(mod_GRH_RSHUD.selectHUD);
	}
	
	
	public int getCurrentEquipItemLoad() {
		return 0;
	}
	
	public int getCurrentEquipItemMaxLoad() {
		return 0;
	}
	
	public int getCurrentEquipItemAmmmoCount() {
		return 0;
	}

}
