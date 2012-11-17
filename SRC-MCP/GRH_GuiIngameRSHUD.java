package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import net.minecraft.client.Minecraft;

public class GRH_GuiIngameRSHUD extends GuiIngame
{
    private Minecraft mc;

    public GRH_GuiIngameRSHUD(Minecraft minecraft)
    {
        super(minecraft);
        mc = minecraft;
    }

    @Override
    public void renderGameOverlay(float f, boolean flag, int i, int j)
    {
        super.renderGameOverlay(f, flag, i, j);
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int k = scaledresolution.getScaledWidth();
        int l = scaledresolution.getScaledHeight();
        ItemStack itemstack = mc.thePlayer.inventory.armorItemInSlot(3);
//        if(mc.gameSettings.thirdPersonView == 0 && itemstack != null && itemstack.itemID == Block.pumpkin.blockID)
        {
            mod_GRH_RSHUD.selectHUD.renderRSHUD(mc, k, l);
        }
    }
}
