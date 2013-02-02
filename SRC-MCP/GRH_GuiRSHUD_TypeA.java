package net.minecraft.src;

import java.util.List;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

public class GRH_GuiRSHUD_TypeA extends GRH_GuiRSHUDConfigure
{
    private GRH_GuiRSHUDSlider deg;
    private GRH_GuiRSHUDSlider linea;
    private GRH_GuiRSHUDSlider linew;
    private GRH_GuiRSHUDSlider[] colornormal = new GRH_GuiRSHUDSlider[4];
    private GRH_GuiRSHUDSlider[] colorwarning = new GRH_GuiRSHUDSlider[4];
    private GRH_GuiRSHUDSlider[] coloralert = new GRH_GuiRSHUDSlider[4];

    public GRH_GuiRSHUD_TypeA(BaseMod basemod)
    {
        super(basemod);
    }

    @Override
    public String getHUDName()
    {
        return "TypeA";
    }

    @Override
    public void initGui()
    {
        //
        super.initGui();
//		hwSize = width / 2;
//		hhSize = height / 2;
        deg = new GRH_GuiRSHUDSlider(50, hwSize + 60, hhSize - 106 + 0 , "Deg+", mod_GRH_RSHUD.DegOffset / 36F, 36F, 0.0F).setStrFormat("%s : %.0f").setDisplayString();
        controlList.add(deg);
        linea = new GRH_GuiRSHUDSlider(51, hwSize + 60, hhSize - 106 + 24, "Line A", mod_GRH_RSHUD.LineAlpha).setDisplayString();
        controlList.add(linea);
        linew = new GRH_GuiRSHUDSlider(52, hwSize + 60, hhSize - 106 + 48, "LineWidth", (mod_GRH_RSHUD.LineWidth - 0.5F) / 5F, 5F, 0.5F).setDisplayString();
        controlList.add(linew);
//		controlList.add(new GuiButton(100, hwSize + 60, hhSize -106 + 72, 100, 20, "Weapon Set"));
//		controlList.add(new GuiButton(200, hwSize - 160, hhSize -106 + 0, 100, 20, mod_RSHUD.HUDEnable ? "HUD ON" : "HUD OFF"));
        String[] str = new String[] {"\2474R", "\2472G", "\2471B", "A"};
        float f[];
        f = getRGBA(ColorInt_Normal);

        for (int i = 0; i < 4; i++)
        {
            colornormal[i] = new GRH_GuiRSHUDSlider(10 + i, hwSize - 160, hhSize + 14 + (24 * i) , str[i], f[i]).setDisplayString();
            controlList.add(colornormal[i]);
        }

        f = getRGBA(ColorInt_Warning);

        for (int i = 0; i < 4; i++)
        {
            colorwarning[i] = new GRH_GuiRSHUDSlider(20 + i, hwSize - 50, hhSize + 14 + (24 * i) , str[i], f[i]).setDisplayString();
            controlList.add(colorwarning[i]);
        }

        f = getRGBA(ColorInt_Alert);

        for (int i = 0; i < 4; i++)
        {
            coloralert[i] = new GRH_GuiRSHUDSlider(30 + i, hwSize + 60, hhSize + 14 + (24 * i) , str[i], f[i]).setDisplayString();
            controlList.add(coloralert[i]);
        }
    }

    @Override
    public void drawDefaultBackground()
    {
        drawGradientRect(hwSize + 50, hhSize - 116, hwSize + 170, hhSize - 4, 0xd0101010, 0xc0101010);
        drawGradientRect(hwSize - 170, hhSize - 4, hwSize + 170, hhSize + 116, 0xc0101010, 0xd0101010);
    }

    @Override
    public void drawScreen(int i, int j, float f)
    {
        super.drawScreen(i, j, f);
        mod_GRH_RSHUD.DegOffset = (int)deg.getSliderValue();
        mod_GRH_RSHUD.LineAlpha = linea.getSliderValue();
        mod_GRH_RSHUD.LineWidth = linew.getSliderValue();
        LineR = colornormal[0].getSliderValue();
        LineG = colornormal[1].getSliderValue();
        LineB = colornormal[2].getSliderValue();
        ColorInt_Normal = setRGBA(
                colornormal[0].getSliderValue(),
                colornormal[1].getSliderValue(),
                colornormal[2].getSliderValue(),
                colornormal[3].getSliderValue()
                );
        ColorInt_Warning = setRGBA(
                colorwarning[0].getSliderValue(),
                colorwarning[1].getSliderValue(),
                colorwarning[2].getSliderValue(),
                colorwarning[3].getSliderValue()
                );
        ColorInt_Alert = setRGBA(
                coloralert[0].getSliderValue(),
                coloralert[1].getSliderValue(),
                coloralert[2].getSliderValue(),
                coloralert[3].getSliderValue()
                );
        mod_GRH_RSHUD.Color_Normal	= String.format("%08x", ColorInt_Normal);
        mod_GRH_RSHUD.Color_Warning	= String.format("%08x", ColorInt_Warning);
        mod_GRH_RSHUD.Color_Alert	= String.format("%08x", ColorInt_Alert);
        String s = "NORMAL";
        drawString(fontRenderer, s, hwSize - 110 - fontRenderer.getStringWidth(s) / 2, hhSize + 2, ColorInt_Normal);
        s = "WARNING";
        drawString(fontRenderer, s, hwSize - fontRenderer.getStringWidth(s) / 2, hhSize + 2, ColorInt_Warning);
        s = "ALERT";
        drawString(fontRenderer, s, hwSize + 110 - fontRenderer.getStringWidth(s) / 2, hhSize + 2, ColorInt_Alert);
        GL11.glEnable(GL11.GL_BLEND);
        s = "WARNING";
        fontRenderer.drawString(s, hwSize - fontRenderer.getStringWidth(s) / 2, hhSize - 50, ColorInt_Warning);
        s = "ALERT";
        fontRenderer.drawString(s, hwSize - fontRenderer.getStringWidth(s) / 2, hhSize - 70, ColorInt_Alert);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void renderRSHUD(Minecraft mc, int i, int j)
    {
//		if (!mod_RSHUD.HUDEnable) return;
        double xcenter = i / 2D;
        double ycenter = j / 2D;
        EntityPlayer mcpl = mc.thePlayer;
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
//        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//        GL11.glColor4f(0.4F, 1.0F, 0.6F, 0.6F);
        GL11.glColor4f(LineR, LineG, LineB, mod_GRH_RSHUD.LineAlpha);
        GL11.glLineWidth(mod_GRH_RSHUD.LineWidth);
        double ldx;
        double ldy;
        ldx = 0D - mcpl.rotationYaw % 10D;
        int liy = (int)(mcpl.rotationYaw / 10F) * 2;
//        System.out.println(String.format("%f", mcpl.rotationYaw));
        tessellator.startDrawing(GL11.GL_LINES);

        for (int li1 = -i / 10; li1 < i / 10; li1++)
        {
            ldy = (ldx - xcenter);
            ldy = ycenter - 90D - (ldy * ldy * 0.0008D);

            if ((liy + li1) % 18 == 0)
            {
                tessellator.addVertex(ldx, ldy - 7D, 0.0D);
            }
            else
            {
                tessellator.addVertex(ldx, ldy - ((li1 & 1) == 1 ? 3D : 5D), 0.0D);
            }

            tessellator.addVertex(ldx, ldy, 0.0D);
            ldx += 5D;
        }

        tessellator.draw();
        ldy = ycenter - 90D;
        tessellator.startDrawing(GL11.GL_LINE_LOOP);
        tessellator.addVertex(xcenter, ldy, 0.0D);
        tessellator.addVertex(xcenter + 2D, ldy + 3D, 0.0D);
        tessellator.addVertex(xcenter - 2D, ldy + 3D, 0.0D);
        tessellator.draw();
        ldx = xcenter;
        ldy = ycenter - 105D - (mcpl.rotationPitch % 10D * 3D);
        int lip = (int)(mcpl.rotationPitch / 10F) * 2;
//        System.out.println(String.format("%f, %d", mc.thePlayer.rotationPitch, (int)(mc.thePlayer.rotationPitch / 5F)));
        tessellator.startDrawing(GL11.GL_LINES);

        for (int li1 = -7; li1 < 7; li1++)
        {
            if (ldy > (ycenter - 70D) && ldy < (ycenter + 70D))
            {
                int lps = lip + li1;

                if (lps % 18 == 0)
                {
                    tessellator.addVertex(ldx - 90D, ldy, 0.0D);
                    tessellator.addVertex(ldx - 50D, ldy, 0.0D);
                    tessellator.addVertex(ldx + 90D, ldy, 0.0D);
                    tessellator.addVertex(ldx + 50D, ldy, 0.0D);
                }
                else
                {
                    int li3 = ((li1 & 1) == 0 ? 8 : 6);

                    for (int li2 = li3 == 8 ? 0 : 1; li2 < li3; li2++)
                    {
                        double ld1 = li2 * 4;
                        tessellator.addVertex(ldx - 90D + ld1, ldy, 0.0D);
                        tessellator.addVertex(ldx - 88D + ld1, ldy, 0.0D);
                        tessellator.addVertex(ldx + 90D - ld1, ldy, 0.0D);
                        tessellator.addVertex(ldx + 88D - ld1, ldy, 0.0D);
                    }

                    if (li3 == 8)
                    {
                        if (lps < 0 && lps > -18 || lps > 18)
                        {
                            tessellator.addVertex(ldx - 90D, ldy, 0.0D);
                            tessellator.addVertex(ldx - 90D, ldy + 3D, 0.0D);
                            tessellator.addVertex(ldx + 90D, ldy, 0.0D);
                            tessellator.addVertex(ldx + 90D, ldy + 3D, 0.0D);
                        }
                        else
                        {
                            tessellator.addVertex(ldx - 90D, ldy, 0.0D);
                            tessellator.addVertex(ldx - 90D, ldy - 3D, 0.0D);
                            tessellator.addVertex(ldx + 90D, ldy, 0.0D);
                            tessellator.addVertex(ldx + 90D, ldy - 3D, 0.0D);
                        }
                    }
                }
            }

            ldy += 15D;
        }

        tessellator.addVertex(ldx - 40D, ycenter, 0.0D);
        tessellator.addVertex(ldx - 15D, ycenter, 0.0D);
        tessellator.addVertex(ldx + 40D, ycenter, 0.0D);
        tessellator.addVertex(ldx + 15D, ycenter, 0.0D);
        tessellator.draw();
        double lvx = mcpl.lastTickPosX - mcpl.posX;
        double lvy = mcpl.lastTickPosY - mcpl.posY;
        double lvz = mcpl.lastTickPosZ - mcpl.posZ;
        double vmove = Math.sqrt(lvx * lvx + lvy * lvy + lvz * lvz) * 100D;
        tessellator.startDrawing(GL11.GL_LINES);
        ldy = 0D + (vmove * 10 % 10);

        for (int li1 = 0; li1 < j / 5; li1++)
        {
            if ((ldy < ycenter - 4D) || ((ldy > ycenter + 7D)))
            {
                ldx = ldy - ycenter;
                ldx = xcenter - 130D - (ldx * ldx * 0.003D);
                tessellator.addVertex(ldx, ldy, 0.0D);
                tessellator.addVertex(ldx - ((li1 & 1) == 1 ? 3D : 5D), ldy, 0.0D);
            }

            ldy += 5D;
        }

        tessellator.draw();
        tessellator.startDrawing(GL11.GL_LINE_LOOP);
        ldx = xcenter - 160D;;
        ldy = ycenter - 4D;
        tessellator.addVertex(ldx, ldy, 0.0D);
        tessellator.addVertex(ldx + 45D, ldy, 0.0D);
        tessellator.addVertex(ldx + 45D, ldy + 11D, 0.0D);
        tessellator.addVertex(ldx, ldy + 11D, 0.0D);
        tessellator.draw();
        tessellator.startDrawing(GL11.GL_LINES);
        ldy = 0D + ((mc.thePlayer.posY * 10) % 10);

        for (int li1 = 0; li1 < j / 5; li1++)
        {
            if ((ldy < ycenter - 4D) || (ldy > ycenter + 7D))
            {
                ldx = ldy - ycenter;
                ldx = xcenter + 130D + (ldx * ldx * 0.003D);
                tessellator.addVertex(ldx, ldy, 0.0D);
                tessellator.addVertex(ldx + ((li1 & 1) == 1 ? 3D : 5D), ldy, 0.0D);
            }

            ldy += 5D;
        }

        tessellator.draw();
        tessellator.startDrawing(GL11.GL_LINE_LOOP);
        ldx = xcenter + 115D;;
        ldy = ycenter - 4D;
        tessellator.addVertex(ldx, ldy, 0.0D);
        tessellator.addVertex(ldx + 45D, ldy, 0.0D);
        tessellator.addVertex(ldx + 45D, ldy + 11D, 0.0D);
        tessellator.addVertex(ldx, ldy + 11D, 0.0D);
        tessellator.draw();
        tessellator.startDrawing(GL11.GL_LINE_LOOP);
        ldx = xcenter - 160D;;
        ldy = j - 70D;
        tessellator.addVertex(ldx, ldy, 0.0D);
        tessellator.addVertex(ldx + 45D, ldy, 0.0D);
        tessellator.addVertex(ldx + 45D, ldy + 60D, 0.0D);
        tessellator.addVertex(ldx, ldy + 60D, 0.0D);
        tessellator.draw();
        /*
                ChunkCoordinates chunkcoordinates = null;
                ChunkCoordinates chunkcoordinates1 = null;
                boolean flag2 = true;
                if(mcpl != null) {
                    chunkcoordinates = mcpl.getPlayerSpawnCoordinate();
                    if(chunkcoordinates != null) {
                        chunkcoordinates1 = EntityPlayer.verifyRespawnCoordinates(mc.theWorld, chunkcoordinates);
                    }
                }
                if(chunkcoordinates1 == null) {
                    chunkcoordinates1 = mc.theWorld.getSpawnPoint();
                    flag2 = false;
                }
                GL11.glPushMatrix();
                GL11.glTranslated(xcenter, ycenter, 0D);
        //        GL11.glTranslated((double)chunkcoordinates1.posX, (double)chunkcoordinates1.posY, (double)chunkcoordinates1.posZ);
                GL11.glRotatef(-mcpl.rotationPitch, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(mcpl.rotationYaw, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(40F, 40F, 40F);
                GL11.glTranslated(mcpl.posX - (double)chunkcoordinates1.posX + 0.5D, mcpl.posY - (double)chunkcoordinates1.posY + 0.5D, mcpl.posZ - (double)chunkcoordinates1.posZ + 0.5D);
                GL11.glRotatef(-mcpl.rotationYaw, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(mcpl.rotationPitch, 1.0F, 0.0F, 0.0F);
        //        GLU.gluPerspective(getFOVModifier(f, true), (float)mc.displayWidth / (float)mc.displayHeight, 0.05F, (256 >> mc.gameSettings.renderDistance) * 2.0F);

                tessellator.startDrawing(GL11.GL_LINE_LOOP);
        //        tessellator.startDrawingQuads();
                tessellator.addVertex(0D, 0.3D, 0.0D);
                tessellator.addVertex(0.3D, 0D, 0.0D);
                tessellator.addVertex(0D, -0.3D, 0.0D);
                tessellator.addVertex(-0.3D, 0D, 0.0D);
                tessellator.draw();
                GL11.glPopMatrix();
        */
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        String s;
        int txtcolor = ColorInt_Normal;
        int deg = (int)(mcpl.rotationYaw / 10F) * 2;
        ldx = 0D - mcpl.rotationYaw % 10D;

        for (int li1 = -i / 10; li1 < i / 10; li1++)
        {
            ldy = (ldx - xcenter);
            ldy = ycenter - 105D - (ldy * ldy * 0.0008D);

            if ((liy + li1) % 18 == 0)
            {
                int deg2 = ((deg + li1) / 2 + mod_GRH_RSHUD.DegOffset) % 36;

                if (deg2 < 0)
                {
                    deg2 += 36;
                }

                s = String.format("%02d", deg2);
                mc.fontRenderer.drawString(s, (int)ldx - mc.fontRenderer.getStringWidth(s) / 2, (int)ldy, txtcolor);
            }

            ldx += 5D;
        }

        ldy = 0D + (vmove * 10D % 10D);

        for (int li1 = -j / 20; li1 < j / 20; li1++)
        {
            int li2 = (int)(vmove) - li1;

            if (((ldy < ycenter - 9D) || (ldy > ycenter + 12D)) && (li2 % 5) == 0)
            {
                ldx = ldy - ycenter;
                ldx = xcenter - 170D - (ldx * ldx * 0.003D);
                s = String.format("%4d", li2);
                mc.fontRenderer.drawString(s, (int)ldx + 10, (int)ldy - 2, txtcolor);
            }

            ldy += 10D;
        }

        s = String.format("%.1f", vmove);
        mc.fontRenderer.drawString(s, (int)xcenter - 120 - mc.fontRenderer.getStringWidth(s), j / 2 - 2, txtcolor);
        ldy = 0D + ((mc.thePlayer.posY * 10) % 10);

        for (int li1 = -j / 20; li1 < j / 20; li1++)
        {
            int li2 = (int)mc.thePlayer.posY - li1;

            if (((ldy < ycenter - 9D) || (ldy > ycenter + 12D)) && (li2 % 5) == 0)
            {
                ldx = ldy - ycenter;
                ldx = xcenter + 130D + (ldx * ldx * 0.003D);
                s = String.format("%4d", li2);
                mc.fontRenderer.drawString(s, (int)ldx + 10, (int)ldy - 2, txtcolor);
            }

            ldy += 10D;
        }

//        s = String.format("%5.1f", mc.thePlayer.posY);
        s = String.format("%.1f", mc.thePlayer.posY);
        mc.fontRenderer.drawString(s, (int)xcenter + 155 - mc.fontRenderer.getStringWidth(s), j / 2 - 2, txtcolor);
        ldx = xcenter;
        ldy = (double)j / 2D - 105D - (mcpl.rotationPitch % 10D * 3D) - 3D;

        for (int li1 = -7; li1 < 7; li1++)
        {
            if (ldy > ycenter - 75D && ldy < ycenter + 65D && ((li1 & 1) == 0))
            {
                int lps = Math.abs(lip + li1) / 2;

                if (lps > 9)
                {
                    lps = 18 - lps;
                }

                mc.fontRenderer.drawString(String.format("%d0", lps), (int)ldx - 104, (int)ldy, txtcolor);
                mc.fontRenderer.drawString(String.format("%d0", lps), (int)ldx + 94, (int)ldy, txtcolor);
            }

            ldy += 15D;
        }

        txtcolor = ColorInt_Normal;

        if (mcpl.health < 9)
        {
            // HP Low
            s = "WARN HEALTH";
            txtcolor = mcpl.health < 5 ? ColorInt_Alert : ColorInt_Warning;
            mc.fontRenderer.drawString(s, (int)xcenter - mc.fontRenderer.getStringWidth(s) / 2, (int)ycenter - 80, txtcolor);
        }

        if (mcpl.foodStats.getFoodLevel() < 7)
        {
            // FOOD Low
            s = "ALERT FOOD";
            txtcolor = mcpl.health < 5 ? ColorInt_Alert : ColorInt_Warning;
            mc.fontRenderer.drawString(s, (int)xcenter - mc.fontRenderer.getStringWidth(s) / 2, (int)ycenter - 70, txtcolor);
        }

        ItemStack is = mcpl.inventory.getCurrentItem();

        if (is != null && is.isItemStackDamageable())
        {
            int cdamage = is.getMaxDamage() - is.getItemDamage();

            if (((float)is.getItemDamage() / (float)is.getMaxDamage()) > 0.9F)
            {
                txtcolor = ColorInt_Alert;
            }
            else
            {
                txtcolor = ColorInt_Normal;
            }

            s = (new StringBuilder()).append(is.getItem().getStatName()).append(" /").toString();
            mc.fontRenderer.drawString(s, (int)xcenter - mc.fontRenderer.getStringWidth(s) + 15, (int)ycenter + 62, txtcolor);
            s = String.format("%7d", cdamage);
            mc.fontRenderer.drawString(s, (int)xcenter - mc.fontRenderer.getStringWidth(s) + 50, (int)ycenter + 62, txtcolor);
        }

        if (containsAmmo(is))
        {
            // AMMO
            int cammo = 0;
            List<Integer> clist = projectorList.get(is.getItem().itemID);

            for (int li1 = 0; li1 < mcpl.inventory.mainInventory.length; li1++)
            {
                is = mcpl.inventory.mainInventory[li1];

                if (is != null && clist.contains(is.getItem().itemID))
                {
                    cammo += is.stackSize;
                }
            }

            txtcolor = (cammo <= 10) ? ColorInt_Alert : ColorInt_Normal;
            s = (new StringBuilder()).append(Item.itemsList[clist.get(0)].getStatName()).append(" /").toString();
            mc.fontRenderer.drawString(s, (int)xcenter - mc.fontRenderer.getStringWidth(s) + 15, (int)ycenter + 54, txtcolor);

            if (cammo == 0)
            {
                s = "EMPTY";
            }
            else
            {
                s = String.format("%7d", cammo);
            }

            mc.fontRenderer.drawString(s, (int)xcenter - mc.fontRenderer.getStringWidth(s) + 50, (int)ycenter + 54, txtcolor);
        }

        int lx = (int)xcenter - 143;
        int ly = j - 62;
        float f;
        is = mcpl.inventory.armorInventory[3];

        if (is != null)
        {
            txtcolor = getArmorColor(is);
            drawRectL(lx, ly, lx + 11, ly + 11, txtcolor);
        }

        ly += 12;
        is = mcpl.inventory.armorInventory[2];

        if (mcpl.inventory.armorInventory[2] != null)
        {
            txtcolor = getArmorColor(is);
            drawRectL(lx, ly, lx + 11, ly + 10, txtcolor);
            lx -= 6;
            drawRectL(lx, ly, lx + 5, ly + 16, txtcolor);
            lx += 18;
            drawRectL(lx, ly, lx + 5, ly + 16, txtcolor);
            lx -= 12;
        }

        ly += 11;
        is = mcpl.inventory.armorInventory[1];

        if (mcpl.inventory.armorInventory[1] != null)
        {
            txtcolor = getArmorColor(is);
            drawRectL(lx, ly, lx + 11, ly + 5, txtcolor);
            ly += 5;
            drawRectL(lx, ly, lx + 5, ly + 8, txtcolor);
            lx += 6;
            drawRectL(lx, ly, lx + 5, ly + 8, txtcolor);
            lx -= 6;
        }
        else
        {
            ly += 5;
        }

        ly += 9;
        is = mcpl.inventory.armorInventory[0];

        if (mcpl.inventory.armorInventory[0] != null)
        {
            txtcolor = getArmorColor(is);
            drawRectL(lx, ly, lx + 5, ly + 7, txtcolor);
            lx += 6;
            drawRectL(lx, ly, lx + 5, ly + 7, txtcolor);
        }

        GL11.glDisable(GL11.GL_BLEND);
    }
}
