//V1.24

package net.minecraft.src;

import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiRSHUD_AC extends GRH_GuiRSHUDConfigure
{
    private GRH_GuiRSHUDSlider colornumber[];
    public int ColorInt_Number;
    public int ColorInt_Iron;
    public int ColorInt_Gold;
    public int ColorInt_Diamond;
    public int ColorInt_Lava;

    public GuiRSHUD_AC(BaseMod basemod)
    {
        super(basemod);
        colornumber = new GRH_GuiRSHUDSlider[4];
    }

    @Override
    public String getHUDName()
    {
        return "Raven";
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui()
    {
        super.initGui();
        deg = (new GRH_GuiRSHUDSlider(50, hwSize + 60, (hhSize - 106) + 0, "Deg+", (float)mod_RSHUD_AC.DegOffset / 36F, 36F, 0.0F)).setStrFormat("%s : %.0f").setDisplayString();
        controlList.add(deg);
        linea = (new GRH_GuiRSHUDSlider(51, hwSize + 60, (hhSize - 106) + 24, "Line A", mod_RSHUD_AC.LineAlpha)).setDisplayString();
        controlList.add(linea);
        linew = (new GRH_GuiRSHUDSlider(52, hwSize + 60, (hhSize - 106) + 48, "LineWidth", (mod_RSHUD_AC.LineWidth - 0.5F) / 5F, 5F, 0.5F)).setDisplayString();
        controlList.add(linew);
        String as[] =
        {
            "\2474R", "\2472G", "\2471B", "A"
        };
        float af[] = getRGBA(ColorInt_Normal);

        for (int i = 0; i < 4; i++)
        {
            colornormal[i] = (new GRH_GuiRSHUDSlider(10 + i, hwSize - 205, hhSize + 14 + 24 * i, as[i], af[i])).setDisplayString();
            controlList.add(colornormal[i]);
        }

        af = getRGBA(ColorInt_Warning);

        for (int j = 0; j < 4; j++)
        {
            colorwarning[j] = (new GRH_GuiRSHUDSlider(20 + j, hwSize - 100, hhSize + 14 + 24 * j, as[j], af[j])).setDisplayString();
            controlList.add(colorwarning[j]);
        }

        af = getRGBA(ColorInt_Alert);

        for (int k = 0; k < 4; k++)
        {
            coloralert[k] = (new GRH_GuiRSHUDSlider(30 + k, hwSize + 5, hhSize + 14 + 24 * k, as[k], af[k])).setDisplayString();
            controlList.add(coloralert[k]);
        }

        af = getRGBA(ColorInt_Number);

        for (int l = 0; l < 4; l++)
        {
            colornumber[l] = (new GRH_GuiRSHUDSlider(40 + l, hwSize + 110, hhSize + 14 + 24 * l, as[l], af[l])).setDisplayString();
            controlList.add(colornumber[l]);
        }
    }

    /**
     * Draws either a gradient over the background screen (when it exists) or a flat gradient over background.png
      */
    @Override
    public void drawDefaultBackground()
    {
        drawGradientRect(hwSize + 50, hhSize - 116, hwSize + 170, hhSize - 4, 0xd0101010, 0xc0101010);
        drawGradientRect(hwSize - 210, hhSize - 16, hwSize + 210, hhSize + 116, 0xc0101010, 0xd0101010);
    }

    /**
     * Draws the screen and all the components in it.
      */
    @Override
    public void drawScreen(int i, int j, float f)
    {
        super.drawScreen(i, j, f);
        mod_RSHUD_AC.DegOffset = (int)deg.getSliderValue();
        mod_RSHUD_AC.LineAlpha = linea.getSliderValue();
        mod_RSHUD_AC.LineWidth = linew.getSliderValue();
        LineR = colornormal[0].getSliderValue();
        LineG = colornormal[1].getSliderValue();
        LineB = colornormal[2].getSliderValue();
        ColorInt_Normal = setRGBA(colornormal[0].getSliderValue(), colornormal[1].getSliderValue(), colornormal[2].getSliderValue(), colornormal[3].getSliderValue());
        ColorInt_Warning = setRGBA(colorwarning[0].getSliderValue(), colorwarning[1].getSliderValue(), colorwarning[2].getSliderValue(), colorwarning[3].getSliderValue());
        ColorInt_Alert = setRGBA(coloralert[0].getSliderValue(), coloralert[1].getSliderValue(), coloralert[2].getSliderValue(), coloralert[3].getSliderValue());
        ColorInt_Number = setRGBA(colornumber[0].getSliderValue(), colornumber[1].getSliderValue(), colornumber[2].getSliderValue(), colornumber[3].getSliderValue());
        mod_RSHUD_AC.Color_Normal = String.format("%08x", new Object[]
                {
                    Integer.valueOf(ColorInt_Normal)
                });
        mod_RSHUD_AC.Color_Warning = String.format("%08x", new Object[]
                {
                    Integer.valueOf(ColorInt_Warning)
                });
        mod_RSHUD_AC.Color_Alert = String.format("%08x", new Object[]
                {
                    Integer.valueOf(ColorInt_Alert)
                });
        mod_RSHUD_AC.Color_Number = String.format("%08x", new Object[]
                {
                    Integer.valueOf(ColorInt_Number)
                });
        String s = "LINE Color";
        drawString(fontRenderer, s, hwSize - 155 - fontRenderer.getStringWidth(s) / 2, hhSize - 12, ColorInt_Normal);
        s = "ORE Alpha";
        int k = ColorInt_Normal / 0x1000000;
        int l = k * 0x1000000 + 0x807050;
        drawString(fontRenderer, s, hwSize - 155 - fontRenderer.getStringWidth(s) / 2, hhSize + 2, l);
        s = "WARNING";
        drawString(fontRenderer, s, hwSize - 55 - fontRenderer.getStringWidth(s) / 2, hhSize + 2, ColorInt_Warning);
        s = "ALERT";
        drawString(fontRenderer, s, (hwSize + 50) - fontRenderer.getStringWidth(s) / 2, hhSize + 2, ColorInt_Alert);
        s = "NUMBER";
        drawString(fontRenderer, s, (hwSize + 155) - fontRenderer.getStringWidth(s) / 2, hhSize + 2, ColorInt_Number);
        GL11.glEnable(GL11.GL_BLEND);
        s = "WARNING";
        fontRenderer.drawString(s, hwSize - fontRenderer.getStringWidth(s) / 2, hhSize - 50, ColorInt_Warning);
        s = "ALERT";
        fontRenderer.drawString(s, hwSize - fontRenderer.getStringWidth(s) / 2, hhSize - 70, ColorInt_Alert);
        s = "NUMBER";
        fontRenderer.drawString(s, hwSize - fontRenderer.getStringWidth(s) / 2, hhSize - 90, ColorInt_Number);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public void renderRSHUD(Minecraft minecraft, int i, int j)
    {
        double d = (double)i / 2D;
        double d1 = (double)j / 2D;
        EntityPlayerSP entityplayersp = minecraft.thePlayer;
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(mod_RSHUD_AC.LineWidth);
        GL11.glColor4f(LineR * 0.1F, LineG * 0.1F, LineB * 0.1F, 0.3F);
        tessellator.startDrawing(6);
        double d2 = 15D;
        double d3 = (double)j - 30D;
        tessellator.addVertex(d2, d3, 0.0D);
        tessellator.addVertex(d2, d3 + 29D, 0.0D);
        tessellator.addVertex(d2 + 50D, d3 + 29D, 0.0D);
        tessellator.addVertex(d2 + 50D, d3 + 15D, 0.0D);
        tessellator.addVertex(d2 + 35D, d3, 0.0D);
        tessellator.draw();
        tessellator.startDrawing(6);
        d2 = i - 55;
        d3 = 130D;
        tessellator.addVertex(d2, d3, 0.0D);
        tessellator.addVertex(d2, d3 + 12D, 0.0D);
        tessellator.addVertex(d2 + 13D, d3 + 25D, 0.0D);
        tessellator.addVertex(d2 + 45D, d3 + 25D, 0.0D);
        tessellator.addVertex(d2 + 45D, d3, 0.0D);
        tessellator.draw();
        tessellator.startDrawing(6);
        d2 = 15D;
        d3 = 2D;
        tessellator.addVertex(d2, d3, 0.0D);
        tessellator.addVertex(d2, d3 + 25D, 0.0D);
        tessellator.addVertex(d2 + 70D, d3 + 25D, 0.0D);
        tessellator.addVertex(d2 + 70D, d3 + 20D, 0.0D);
        tessellator.addVertex(d2 + 50D, d3, 0.0D);
        tessellator.draw();
        tessellator.startDrawing(6);
        d2 = 3D;
        d3 = 2D;
        tessellator.addVertex(d2, d3, 0.0D);
        tessellator.addVertex(d2 + 10D, d3, 0.0D);
        tessellator.addVertex(d2 + 10D, (d3 + (double)j) - 2D, 0.0D);
        tessellator.addVertex(d2, (d3 + (double)j) - 2D, 0.0D);
        tessellator.draw();
        GL11.glColor4f(LineR, LineG, LineB, mod_RSHUD_AC.LineAlpha);
        d2 = 0.0D - (double)((EntityPlayer)(entityplayersp)).rotationYaw % 10D;
        int k = (int)(((EntityPlayer)(entityplayersp)).rotationYaw / 10F) * 2;
        tessellator.startDrawing(1);

        for (int l = -i / 20; l < i / 20; l++)
        {
            double d4 = d2 - d;
            d4 = 12D;

            if ((k + l) % 18 == 0)
            {
                tessellator.addVertex(d / 2D + d2 + 2D, d4 + 7D, 0.0D);
            }
            else
            {
                tessellator.addVertex(d / 2D + d2 + 2D, d4 + ((l & 1) == 1 ? 3D : 5D), 0.0D);
            }

            tessellator.addVertex(d / 2D + d2 + 2D, d4, 0.0D);
            d2 += 5D;
        }

        tessellator.draw();
        tessellator.startDrawing(1);
        tessellator.addVertex(d - (double)(i / 4), 12D, 0.0D);
        tessellator.addVertex(d + (double)(i / 4), 12D, 0.0D);
        tessellator.draw();
        d3 = 15D;
        tessellator.startDrawing(2);
        tessellator.addVertex(d, d3, 0.0D);
        tessellator.addVertex(d + 4D, d3 + 6D, 0.0D);
        tessellator.addVertex(d - 4D, d3 + 6D, 0.0D);
        tessellator.draw();
        d2 = d;
        d3 = d1 - 105D - ((double)((EntityPlayer)(entityplayersp)).rotationPitch % 10D) * 3D;
        int i1 = (int)(((EntityPlayer)(entityplayersp)).rotationPitch / 10F) * 2;
        tessellator.startDrawing(1);

        for (int j1 = -7; j1 < 7; j1++)
        {
            if (d3 > d1 - 60D && d3 < d1 + 61D)
            {
                int k1 = i1 + j1;

                if (k1 % 9 == 0)
                {
                    tessellator.addVertex(d2 - 90D, d3, 0.0D);
                    tessellator.addVertex(d2 - 55D, d3, 0.0D);
                    tessellator.addVertex(d2 + 90D, d3, 0.0D);
                    tessellator.addVertex(d2 + 55D, d3, 0.0D);
                }
                else
                {
                    byte byte0 = (byte)((j1 & 1) == 0 ? 8 : 6);

                    for (int l1 = byte0 == 8 ? 0 : 1; l1 < byte0; l1++)
                    {
                        double d7 = l1 * 2;
                        tessellator.addVertex((d2 - 90D) + d7, d3, 0.0D);
                        tessellator.addVertex((d2 - 89D) + d7, d3, 0.0D);
                        tessellator.addVertex((d2 + 90D) - d7, d3, 0.0D);
                        tessellator.addVertex((d2 + 89D) - d7, d3, 0.0D);
                    }

                    if (byte0 == 8)
                    {
                        if (k1 < 0 && k1 > -18 || k1 > 18)
                        {
                            tessellator.addVertex(d2 - 90D, d3, 0.0D);
                            tessellator.addVertex(d2 - 90D, d3 + 3D, 0.0D);
                            tessellator.addVertex(d2 + 90D, d3, 0.0D);
                            tessellator.addVertex(d2 + 90D, d3 + 3D, 0.0D);
                        }
                        else
                        {
                            tessellator.addVertex(d2 - 90D, d3, 0.0D);
                            tessellator.addVertex(d2 - 90D, d3 - 3D, 0.0D);
                            tessellator.addVertex(d2 + 90D, d3, 0.0D);
                            tessellator.addVertex(d2 + 90D, d3 - 3D, 0.0D);
                        }
                    }
                }
            }

            d3 += 15D;
        }

        tessellator.addVertex(d2 - 40D, d1, 0.0D);
        tessellator.addVertex(d2 - 15D, d1, 0.0D);
        tessellator.addVertex(d2 + 40D, d1, 0.0D);
        tessellator.addVertex(d2 + 15D, d1, 0.0D);
        tessellator.draw();
        double d5 = ((EntityPlayer)(entityplayersp)).lastTickPosX - ((EntityPlayer)(entityplayersp)).posX;
        double d6 = ((EntityPlayer)(entityplayersp)).lastTickPosY - ((EntityPlayer)(entityplayersp)).posY;
        double d8 = ((EntityPlayer)(entityplayersp)).lastTickPosZ - ((EntityPlayer)(entityplayersp)).posZ;
        double d9 = Math.sqrt(d5 * d5 + d6 * d6 + d8 * d8) * 100D;
        tessellator.startDrawing(1);
        tessellator.addVertex(i - 3, 2D, 0.0D);
        tessellator.addVertex(i - 3, j - 2, 0.0D);
        tessellator.draw();
        tessellator.startDrawing(1);
        d3 = ((minecraft.thePlayer.posY - 1.6000000000000001D) * 10D) % 10D;
        int i2 = j - 5;

        for (int j2 = 0; j2 < 257; j2++)
        {
            d3 = j - 2 - (j2 * i2) / 256;
            tessellator.addVertex(i - 3, d3, 0.0D);

            if (j2 % 32 == 0)
            {
                tessellator.addVertex(i - 10, d3, 0.0D);
            }
            else
            {
                tessellator.addVertex((double)(i - 3) - (j2 % 8 == 0 ? 3D : 0.0D), d3, 0.0D);
            }
        }

        tessellator.draw();
        double d10 = (double)j - (minecraft.thePlayer.posY * (double)i2) / 256D;
        tessellator.startDrawing(4);
        tessellator.addVertex(i - 4, d10, 0.0D);
        tessellator.addVertex(i - 10, d10 - 3D, 0.0D);
        tessellator.addVertex(i - 10, d10 + 3D, 0.0D);
        tessellator.draw();
        tessellator.startDrawing(2);
        d2 = 15D;
        d3 = (double)j - 30D;
        tessellator.addVertex(d2, d3, 0.0D);
        tessellator.addVertex(d2 + 35D, d3, 0.0D);
        tessellator.addVertex(d2 + 50D, d3 + 15D, 0.0D);
        tessellator.addVertex(d2 + 50D, d3 + 29D, 0.0D);
        tessellator.addVertex(d2, d3 + 29D, 0.0D);
        tessellator.draw();
        tessellator.startDrawing(2);
        d2 = i - 55;
        d3 = 130D;
        tessellator.addVertex(d2, d3, 0.0D);
        tessellator.addVertex(d2 + 45D, d3, 0.0D);
        tessellator.addVertex(d2 + 45D, d3 + 25D, 0.0D);
        tessellator.addVertex(d2 + 13D, d3 + 25D, 0.0D);
        tessellator.addVertex(d2, d3 + 12D, 0.0D);
        tessellator.draw();
        tessellator.startDrawing(2);
        d2 = 15D;
        d3 = 2D;
        tessellator.addVertex(d2, d3, 0.0D);
        tessellator.addVertex(d2, d3 + 25D, 0.0D);
        tessellator.addVertex(d2 + 70D, d3 + 25D, 0.0D);
        tessellator.addVertex(d2 + 70D, d3 + 20D, 0.0D);
        tessellator.addVertex(d2 + 50D, d3, 0.0D);
        tessellator.draw();
        tessellator.startDrawing(2);
        d2 = 3D;
        d3 = 2D;
        tessellator.addVertex(d2, d3, 0.0D);
        tessellator.addVertex(d2 + 10D, d3, 0.0D);
        tessellator.addVertex(d2 + 10D, (d3 + (double)j) - 3D, 0.0D);
        tessellator.addVertex(d2, (d3 + (double)j) - 3D, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        int k2 = ColorInt_Number;
        int l2 = (int)(((EntityPlayer)(entityplayersp)).rotationYaw / 10F) * 2;
        d2 = 0.0D - (double)((EntityPlayer)(entityplayersp)).rotationYaw % 10D;

        for (int i3 = -i / 20; i3 < i / 20; i3++)
        {
            d3 = 2D;

            if ((k + i3) % 18 == 0)
            {
                int j3 = ((l2 + i3) / 2 + mod_RSHUD_AC.DegOffset) % 36;

                if (j3 < 0)
                {
                    j3 += 36;
                }

                if (j3 == 0)
                {
                    String s2 = "N";
                    minecraft.fontRenderer.drawString(s2, (int)(d / 2D + d2), (int)d3, k2);
                }

                if (j3 == 9)
                {
                    String s3 = "E";
                    minecraft.fontRenderer.drawString(s3, (int)(d / 2D + d2), (int)d3, k2);
                }

                if (j3 == 18)
                {
                    String s4 = "S";
                    minecraft.fontRenderer.drawString(s4, (int)(d / 2D + d2), (int)d3, k2);
                }

                if (j3 == 27)
                {
                    String s5 = "W";
                    minecraft.fontRenderer.drawString(s5, (int)(d / 2D + d2), (int)d3, k2);
                }
            }

            d2 += 5D;
        }

        d3 = 0.0D + (d9 * 10D) % 10D;
        String s = String.format("%.1f", new Object[]
                {
                    Double.valueOf(d9)
                });
        minecraft.fontRenderer.drawString(s, 17, j - 12, k2);
        String s1 = "Speed";
        minecraft.fontRenderer.drawString(s1, 17, j - 27, k2);
        s = String.format("%.1f", new Object[]
                {
                    Double.valueOf(minecraft.thePlayer.posY - 1.6000000000000001D)
                });
        minecraft.fontRenderer.drawString(s, i - minecraft.fontRenderer.getStringWidth(s) - 15, 145, k2);
        String s6 = "Height";
        minecraft.fontRenderer.drawString(s6, i - minecraft.fontRenderer.getStringWidth(s6) / 2 - 30, 132, k2);
        d2 = d;
        d3 = (double)j / 2D - 105D - ((double)((EntityPlayer)(entityplayersp)).rotationPitch % 10D) * 3D - 3D;

        for (int k3 = -7; k3 < 7; k3++)
        {
            if (d3 > d1 - 63D && d3 < d1 + 60D && (k3 & 1) == 0)
            {
                int i4 = Math.abs(i1 + k3) / 2;

                if (i4 > 9)
                {
                    i4 = 18 - i4;
                }

                minecraft.fontRenderer.drawString(String.format("%d0", new Object[]
                        {
                            Integer.valueOf(i4)
                        }), (int)d2 - 104, (int)d3, k2);
                minecraft.fontRenderer.drawString(String.format("%d0", new Object[]
                        {
                            Integer.valueOf(i4)
                        }), (int)d2 + 94, (int)d3, k2);
            }

            d3 += 15D;
        }

        int l3 = ColorInt_Normal / 0x1000000;
        int j4 = l3 * 0x1000000 + ColorInt_Iron;
        int k4 = l3 * 0x1000000 + ColorInt_Gold;
        int l4 = l3 * 0x1000000 + ColorInt_Diamond;
        int i5 = l3 * 0x1000000 + ColorInt_Lava;

        if (minecraft.thePlayer.posY < 64D)
        {
            String s7 = "Iron";
            minecraft.fontRenderer.drawString(s7, i - minecraft.fontRenderer.getStringWidth(s7) - 10, 90, j4);
        }

        if (minecraft.thePlayer.posY < 30D)
        {
            String s8 = "Gold";
            minecraft.fontRenderer.drawString(s8, i - minecraft.fontRenderer.getStringWidth(s8) - 10, 100, k4);
        }

        if (minecraft.thePlayer.posY < 16D)
        {
            String s9 = "Diamond";
            minecraft.fontRenderer.drawString(s9, i - minecraft.fontRenderer.getStringWidth(s9) - 10, 110, l4);
        }

        if (minecraft.thePlayer.posY < 12D)
        {
            String s10 = "Lava";
            minecraft.fontRenderer.drawString(s10, i - minecraft.fontRenderer.getStringWidth(s10) - 10, 120, i5);
        }

        int j5 = (int)(minecraft.theWorld.getWorldTime() % 24000L) / 10;
        int k5 = j5 / 100 + 6;

        if (k5 >= 24)
        {
            k5 -= 24;
        }

        String s11 = String.format("%02d", new Object[]
                {
                    Integer.valueOf(k5)
                });
        minecraft.fontRenderer.drawString(s11, 70, j - 12, k2);
        int l5 = (int)((double)(j5 % 100) / 1.66666665D);
        String s12 = String.format("%02d", new Object[]
                {
                    Integer.valueOf(l5)
                });
        minecraft.fontRenderer.drawString(s12, 86, j - 12, k2);
        String s13 = ":";
        minecraft.fontRenderer.drawString(s13, 83, j - 12, k2);
        int i6 = 0;

        if (minecraft.renderViewEntity.isPotionActive(Potion.fireResistance))
        {
            String s14 = "ANTI-Flame";
            i6++;
            minecraft.fontRenderer.drawString(s14, 20, 22 + i6 * 11, k2);
        }

        if (minecraft.renderViewEntity.isPotionActive(Potion.moveSpeed))
        {
            String s15 = "SPD-Boost";
            i6++;
            minecraft.fontRenderer.drawString(s15, 20, 22 + i6 * 11, k2);
        }

        if (minecraft.renderViewEntity.isPotionActive(Potion.damageBoost))
        {
            String s16 = "ATK-Boost";
            i6++;
            minecraft.fontRenderer.drawString(s16, 20, 22 + i6 * 11, k2);
        }

        if (minecraft.renderViewEntity.isPotionActive(Potion.regeneration))
        {
            String s17 = "AUTO-Repair";
            i6++;
            minecraft.fontRenderer.drawString(s17, 20, 22 + i6 * 11, k2);
        }

        if (minecraft.renderViewEntity.isPotionActive(Potion.hunger))
        {
            String s18 = "FUEL-Leakage";
            i6++;
            minecraft.fontRenderer.drawString(s18, 20, 22 + i6 * 11, ColorInt_Warning);
        }

        if (j5 % 3 != 1)
        {
            if (minecraft.renderViewEntity.isPotionActive(Potion.poison))
            {
                String s19 = "ALART CORROSION!";
                minecraft.fontRenderer.drawString(s19, (int)d - minecraft.fontRenderer.getStringWidth(s19) / 2, (int)d1 - 60, ColorInt_Alert);
            }

            if (((EntityPlayer)(entityplayersp)).health < 9)
            {
                String s20 = "ALART GCS DOWN!";
                int j6 = ((EntityPlayer)(entityplayersp)).health < 5 ? ColorInt_Alert : ColorInt_Warning;
                minecraft.fontRenderer.drawString(s20, (int)d - minecraft.fontRenderer.getStringWidth(s20) / 2, (int)d1 - 80, j6);
            }

            if (((EntityPlayer)(entityplayersp)).foodStats.getFoodLevel() < 7)
            {
                String s21 = "ALART LESS FUEL";
                int k6 = ((EntityPlayer)(entityplayersp)).health < 5 ? ColorInt_Alert : ColorInt_Warning;
                minecraft.fontRenderer.drawString(s21, (int)d - minecraft.fontRenderer.getStringWidth(s21) / 2, (int)d1 - 70, k6);
            }
        }

        ItemStack itemstack = ((EntityPlayer)(entityplayersp)).inventory.getCurrentItem();

        if (itemstack != null && itemstack.isItemStackDamageable())
        {
            int l6 = itemstack.getMaxDamage() - itemstack.getItemDamage();
            int k7 = ColorInt_Number;

            if ((float)itemstack.getItemDamage() / (float)itemstack.getMaxDamage() > 0.9F)
            {
                k7 = ColorInt_Alert;
            }

            String s22 = (new StringBuilder()).append(itemstack.getItem().getStatName()).append(" /").toString();
            minecraft.fontRenderer.drawString(s22, ((int)d - minecraft.fontRenderer.getStringWidth(s22)) + 25, j - 58, k7);
            s22 = String.format("%7d", new Object[]
                    {
                        Integer.valueOf(l6)
                    });
            minecraft.fontRenderer.drawString(s22, ((int)d - minecraft.fontRenderer.getStringWidth(s22)) + 60, j - 58, k7);
        }

        if (itemstack != null && projectorList.containsKey(Integer.valueOf(itemstack.getItem().shiftedIndex)))
        {
            int i7 = 0;
            List list = (List)projectorList.get(Integer.valueOf(itemstack.getItem().shiftedIndex));

            for (int i8 = 0; i8 < ((EntityPlayer)(entityplayersp)).inventory.mainInventory.length; i8++)
            {
                itemstack = ((EntityPlayer)(entityplayersp)).inventory.mainInventory[i8];

                if (itemstack != null && list.contains(Integer.valueOf(itemstack.getItem().shiftedIndex)))
                {
                    i7 += itemstack.stackSize;
                }
            }

            int j8 = ColorInt_Number;

            if (i7 <= 10)
            {
                j8 = ColorInt_Alert;
            }

            String s23 = (new StringBuilder()).append(Item.itemsList[((Integer)list.get(0)).intValue()].getStatName()).append(" /").toString();
            minecraft.fontRenderer.drawString(s23, ((int)d - minecraft.fontRenderer.getStringWidth(s23)) + 25, j - 68, j8);

            if (i7 == 0)
            {
                s23 = "EMPTY";
            }
            else
            {
                s23 = String.format("%7d", new Object[]
                        {
                            Integer.valueOf(i7)
                        });
            }

            minecraft.fontRenderer.drawString(s23, ((int)d - minecraft.fontRenderer.getStringWidth(s23)) + 60, j - 68, j8);
        }

        int j7 = 17;
        int l7 = 5;
        int k8 = 0;
        itemstack = ((EntityPlayer)(entityplayersp)).inventory.armorInventory[3];

        if (itemstack != null)
        {
            k8 += itemstack.getMaxDamage() - itemstack.getItemDamage();
            int l8 = getArmorColor(itemstack);
            drawRectL(j7, l7, j7 + 11, l7 + 5, l8);
            drawRectL(j7, l7 + 5, j7 + 3, l7 + 11, l8);
            drawRectL(j7 + 8, l7 + 5, j7 + 11, l7 + 11, l8);
        }

        j7 += 13;
        itemstack = ((EntityPlayer)(entityplayersp)).inventory.armorInventory[2];

        if (((EntityPlayer)(entityplayersp)).inventory.armorInventory[2] != null)
        {
            k8 += itemstack.getMaxDamage() - itemstack.getItemDamage();
            int i9 = getArmorColor(itemstack);
            drawRectL(j7, l7, j7 + 11, l7 + 5, i9);
            drawRectL(j7 + 2, l7 + 5, j7 + 9, l7 + 11, i9);
        }

        j7 += 13;
        itemstack = ((EntityPlayer)(entityplayersp)).inventory.armorInventory[1];

        if (((EntityPlayer)(entityplayersp)).inventory.armorInventory[1] != null)
        {
            k8 += itemstack.getMaxDamage() - itemstack.getItemDamage();
            int j9 = getArmorColor(itemstack);
            drawRectL(j7, l7, j7 + 11, l7 + 4, j9);
            drawRectL(j7, l7 + 4, j7 + 5, l7 + 11, j9);
            drawRectL(j7 + 6, l7 + 4, j7 + 11, l7 + 11, j9);
        }

        j7 += 13;
        l7 += 4;
        itemstack = ((EntityPlayer)(entityplayersp)).inventory.armorInventory[0];

        if (((EntityPlayer)(entityplayersp)).inventory.armorInventory[0] != null)
        {
            k8 += itemstack.getMaxDamage() - itemstack.getItemDamage();
            int k9 = getArmorColor(itemstack);
            drawRectL(j7 + 1, l7, j7 + 5, l7 + 7, k9);
            drawRectL(j7, l7 + 4, j7 + 1, l7 + 7, k9);
            drawRectL(j7 + 6, l7, j7 + 10, l7 + 7, k9);
            drawRectL(j7 + 10, l7 + 4, j7 + 11, l7 + 7, k9);
        }

        int l9 = ColorInt_Number;
        int i10 = ColorInt_Alert;
        double d11 = j;
        double d12 = (double)j * 0.80000000000000004D;
        double d13 = ((double)(((EntityPlayer)(entityplayersp)).foodStats.getFoodLevel() * 12) - d9) * ((double)j / 240D);

        if (d13 >= 0.0D)
        {
            if (d13 >= (double)(j - 4) * 0.20000000000000001D)
            {
                drawRectL(10, (int)d12, 5, (int)d11 - 2, i10);
                drawRectL(10, ((int)d11 - (int)d13) + 2, 5, (int)d12, l9);
            }
            else
            {
                drawRectL(10, ((int)d11 - (int)d13) + 2, 5, (int)d11 - 2, i10);
            }
        }

        if (k8 != 0)
        {
            double d14 = d - 146D;
            double d15 = (double)j - 80D;
            String s24 = "AP ";
            minecraft.fontRenderer.drawString(s24, 20, 18, k2);
            minecraft.fontRenderer.drawString(String.format("%d0", new Object[]
                    {
                        Integer.valueOf(k8)
                    }), 40, 18, k2);
        }

        GL11.glDisable(GL11.GL_BLEND);
    }
}
