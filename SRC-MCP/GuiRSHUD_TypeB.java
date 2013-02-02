//V1.24

package net.minecraft.src;

import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiRSHUD_TypeB extends GRH_GuiRSHUDConfigure
{
    private GRH_GuiRSHUDSlider colornumber[];
    public int ColorInt_Number;
    public int ColorInt_Iron;
    public int ColorInt_Gold;
    public int ColorInt_Diamond;
    public int ColorInt_Lava;

    public GuiRSHUD_TypeB(BaseMod basemod)
    {
        super(basemod);
        colornumber = new GRH_GuiRSHUDSlider[4];
    }

    @Override
    public String getHUDName()
    {
        return "TypeB";
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
      */
    @Override
    public void initGui()
    {
        super.initGui();
        deg = (new GRH_GuiRSHUDSlider(50, hwSize + 60, (hhSize - 106) + 0, "Deg+", (float)mod_RSHUD_TypeB.DegOffset / 36F, 36F, 0.0F)).setStrFormat("%s : %.0f").setDisplayString();
        controlList.add(deg);
        linea = (new GRH_GuiRSHUDSlider(51, hwSize + 60, (hhSize - 106) + 24, "Line A", mod_RSHUD_TypeB.LineAlpha)).setDisplayString();
        controlList.add(linea);
        linew = (new GRH_GuiRSHUDSlider(52, hwSize + 60, (hhSize - 106) + 48, "LineWidth", (mod_RSHUD_TypeB.LineWidth - 0.5F) / 5F, 5F, 0.5F)).setDisplayString();
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
        mod_RSHUD_TypeB.DegOffset = (int)deg.getSliderValue();
        mod_RSHUD_TypeB.LineAlpha = linea.getSliderValue();
        mod_RSHUD_TypeB.LineWidth = linew.getSliderValue();
        LineR = colornormal[0].getSliderValue();
        LineG = colornormal[1].getSliderValue();
        LineB = colornormal[2].getSliderValue();
        ColorInt_Normal = setRGBA(colornormal[0].getSliderValue(), colornormal[1].getSliderValue(), colornormal[2].getSliderValue(), colornormal[3].getSliderValue());
        ColorInt_Warning = setRGBA(colorwarning[0].getSliderValue(), colorwarning[1].getSliderValue(), colorwarning[2].getSliderValue(), colorwarning[3].getSliderValue());
        ColorInt_Alert = setRGBA(coloralert[0].getSliderValue(), coloralert[1].getSliderValue(), coloralert[2].getSliderValue(), coloralert[3].getSliderValue());
        ColorInt_Number = setRGBA(colornumber[0].getSliderValue(), colornumber[1].getSliderValue(), colornumber[2].getSliderValue(), colornumber[3].getSliderValue());
        mod_RSHUD_TypeB.Color_Normal = String.format("%08x", new Object[]
                {
                    Integer.valueOf(ColorInt_Normal)
                });
        mod_RSHUD_TypeB.Color_Warning = String.format("%08x", new Object[]
                {
                    Integer.valueOf(ColorInt_Warning)
                });
        mod_RSHUD_TypeB.Color_Alert = String.format("%08x", new Object[]
                {
                    Integer.valueOf(ColorInt_Alert)
                });
        mod_RSHUD_TypeB.Color_Number = String.format("%08x", new Object[]
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
        GL11.glColor4f(LineR, LineG, LineB, mod_RSHUD_TypeB.LineAlpha);
        GL11.glLineWidth(mod_RSHUD_TypeB.LineWidth);
        double d2 = 0.0D - (double)((EntityPlayer)(entityplayersp)).rotationYaw % 10D;
        int k = (int)(((EntityPlayer)(entityplayersp)).rotationYaw / 10F) * 2;
        tessellator.startDrawing(1);

        for (int l = -i / 10; l < i / 10; l++)
        {
            double d4 = d2 - d;
            d4 = d1 - 90D - d4 * d4 * 0.00080000000000000004D;

            if ((k + l) % 18 == 0)
            {
                tessellator.addVertex(d2 + 2D, d4 - 7D, 0.0D);
            }
            else
            {
                tessellator.addVertex(d2 + 2D, d4 - ((l & 1) != 1 ? 5D : 3D), 0.0D);
            }

            tessellator.addVertex(d2 + 2D, d4, 0.0D);
            d2 += 5D;
        }

        tessellator.draw();
        double d3 = d1 - 90D;
        tessellator.startDrawing(2);
        tessellator.addVertex(d, d3, 0.0D);
        tessellator.addVertex(d + 2D, d3 + 3D, 0.0D);
        tessellator.addVertex(d - 2D, d3 + 3D, 0.0D);
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

                if (k1 % 18 == 0)
                {
                    tessellator.addVertex(d2 - 90D, d3, 0.0D);
                    tessellator.addVertex(d2 - 50D, d3, 0.0D);
                    tessellator.addVertex(d2 + 90D, d3, 0.0D);
                    tessellator.addVertex(d2 + 50D, d3, 0.0D);
                }
                else
                {
                    byte byte0 = (byte)((j1 & 1) != 0 ? 6 : 8);

                    for (int l1 = byte0 != 8 ? 1 : 0; l1 < byte0; l1++)
                    {
                        double d7 = l1 * 4;
                        tessellator.addVertex((d2 - 90D) + d7, d3, 0.0D);
                        tessellator.addVertex((d2 - 88D) + d7, d3, 0.0D);
                        tessellator.addVertex((d2 + 90D) - d7, d3, 0.0D);
                        tessellator.addVertex((d2 + 88D) - d7, d3, 0.0D);
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
        d3 = 0.0D + (d9 * 10D) % 10D;

        for (int i2 = 0; i2 < j / 5; i2++)
        {
            if (d3 < d1 - 4D || d3 > d1 + 7D)
            {
                d2 = d3 - d1;
                d2 = (d - 160D) + d2 * d2 * 0.0030000000000000001D;

                if (d3 > d1 - 100D && d3 < (double)j - 80D)
                {
                    tessellator.addVertex(d2, d3, 0.0D);
                    tessellator.addVertex(d2 - ((i2 & 1) != 1 ? 5D : 3D), d3, 0.0D);
                }
            }

            d3 += 5D;
        }

        tessellator.draw();
        tessellator.startDrawing(2);
        d2 = d - 160D;
        d3 = d1 - 4D;
        tessellator.addVertex(d2, d3, 0.0D);
        tessellator.addVertex(d2 + 45D, d3, 0.0D);
        tessellator.addVertex(d2 + 45D, d3 + 11D, 0.0D);
        tessellator.addVertex(d2, d3 + 11D, 0.0D);
        tessellator.draw();
        tessellator.startDrawing(1);
        d3 = 0.0D + (minecraft.thePlayer.posY * 10D) % 10D;

        for (int j2 = 0; j2 < j / 5; j2++)
        {
            if (d3 < d1 - 4D || d3 > d1 + 7D)
            {
                d2 = d3 - d1;
                d2 = (d + 160D) - d2 * d2 * 0.0030000000000000001D;

                if (d3 > d1 - 100D && d3 < (double)j - 60D)
                {
                    tessellator.addVertex(d2, d3, 0.0D);
                    tessellator.addVertex(d2 + ((j2 & 1) != 1 ? 5D : 3D), d3, 0.0D);
                }
            }

            d3 += 5D;
        }

        tessellator.draw();
        tessellator.startDrawing(2);
        d2 = d + 115D;
        d3 = d1 - 4D;
        tessellator.addVertex(d2, d3, 0.0D);
        tessellator.addVertex(d2 + 45D, d3, 0.0D);
        tessellator.addVertex(d2 + 45D, d3 + 11D, 0.0D);
        tessellator.addVertex(d2, d3 + 11D, 0.0D);
        tessellator.draw();
        tessellator.startDrawing(2);
        d2 = d - 160D;
        d3 = (double)j - 70D;
        tessellator.addVertex(d2, d3, 0.0D);
        tessellator.addVertex(d2 + 45D, d3, 0.0D);
        tessellator.addVertex(d2 + 45D, d3 + 60D, 0.0D);
        tessellator.addVertex(d2, d3 + 60D, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        int k2 = ColorInt_Number;
        int i3 = (int)(((EntityPlayer)(entityplayersp)).rotationYaw / 10F) * 2;
        d2 = 0.0D - (double)((EntityPlayer)(entityplayersp)).rotationYaw % 10D;

        for (int j3 = -i / 10; j3 < i / 10; j3++)
        {
            d3 = d2 - d;
            d3 = d1 - 105D - d3 * d3 * 0.00080000000000000004D;

            if ((k + j3) % 18 == 0)
            {
                int l3 = ((i3 + j3) / 2 + mod_RSHUD_TypeB.DegOffset) % 36;

                if (l3 < 0)
                {
                    l3 += 36;
                }

                if (l3 == 0)
                {
                    String s2 = "North";
                    minecraft.fontRenderer.drawString(s2, ((int)d2 + 2) - minecraft.fontRenderer.getStringWidth(s2) / 2, (int)d3, k2);
                }

                if (l3 == 9)
                {
                    String s3 = "East";
                    minecraft.fontRenderer.drawString(s3, ((int)d2 + 2) - minecraft.fontRenderer.getStringWidth(s3) / 2, (int)d3, k2);
                }

                if (l3 == 18)
                {
                    String s4 = "South";
                    minecraft.fontRenderer.drawString(s4, ((int)d2 + 2) - minecraft.fontRenderer.getStringWidth(s4) / 2, (int)d3, k2);
                }

                if (l3 == 27)
                {
                    String s5 = "West";
                    minecraft.fontRenderer.drawString(s5, ((int)d2 + 2) - minecraft.fontRenderer.getStringWidth(s5) / 2, (int)d3, k2);
                }
            }

            d2 += 5D;
        }

        d3 = 0.0D + (d9 * 10D) % 10D;

        for (int k3 = -j / 20; k3 < j / 20; k3++)
        {
            int i4 = (int)d9 - k3;

            if ((d3 < d1 - 9D || d3 > d1 + 12D) && i4 % 5 == 0)
            {
                d2 = d3 - d1;
                d2 = (d - 200D) + d2 * d2 * 0.0030000000000000001D;
                String s6 = String.format("%4d", new Object[]
                        {
                            Integer.valueOf(i4)
                        });

                if (d3 > d1 - 100D && d3 < (double)j - 80D)
                {
                    minecraft.fontRenderer.drawString(s6, (int)d2 + 10, (int)d3 - 2, k2);
                }
            }

            d3 += 10D;
        }

        String s = String.format("%.1f", new Object[]
                {
                    Double.valueOf(d9)
                });
        minecraft.fontRenderer.drawString(s, (int)d - 120 - minecraft.fontRenderer.getStringWidth(s), j / 2 - 2, k2);
        String s1 = "Speed";
        minecraft.fontRenderer.drawString(s1, (int)d - minecraft.fontRenderer.getStringWidth(s1) / 2 - 140, (int)d1 - 15, k2);
        d3 = 0.0D + (minecraft.thePlayer.posY * 10D) % 10D;

        for (int j4 = -j / 20; j4 < j / 20; j4++)
        {
            int k4 = (int)minecraft.thePlayer.posY - j4;

            if ((d3 < d1 - 9D || d3 > d1 + 12D) && k4 % 5 == 0)
            {
                d2 = d3 - d1;
                d2 = (d + 155D) - d2 * d2 * 0.0030000000000000001D;
                s = String.format("%4d", new Object[]
                        {
                            Integer.valueOf(k4)
                        });

                if (d3 > d1 - 100D && d3 < (double)j - 60D)
                {
                    minecraft.fontRenderer.drawString(s, (int)d2 + 10, (int)d3 - 2, k2);
                }
            }

            d3 += 10D;
        }

        s = String.format("%.1f", new Object[]
                {
                    Double.valueOf(minecraft.thePlayer.posY)
                });
        minecraft.fontRenderer.drawString(s, ((int)d + 155) - minecraft.fontRenderer.getStringWidth(s), j / 2 - 2, k2);
        String s7 = "Height";
        minecraft.fontRenderer.drawString(s7, ((int)d - minecraft.fontRenderer.getStringWidth(s7) / 2) + 135, (int)d1 - 15, k2);
        d2 = d;
        d3 = (double)j / 2D - 105D - ((double)((EntityPlayer)(entityplayersp)).rotationPitch % 10D) * 3D - 3D;

        for (int l4 = -7; l4 < 7; l4++)
        {
            if (d3 > d1 - 63D && d3 < d1 + 60D && (l4 & 1) == 0)
            {
                int j5 = Math.abs(i1 + l4) / 2;

                if (j5 > 9)
                {
                    j5 = 18 - j5;
                }

                minecraft.fontRenderer.drawString(String.format("%d0", new Object[]
                        {
                            Integer.valueOf(j5)
                        }), (int)d2 - 104, (int)d3, k2);
                minecraft.fontRenderer.drawString(String.format("%d0", new Object[]
                        {
                            Integer.valueOf(j5)
                        }), (int)d2 + 94, (int)d3, k2);
            }

            d3 += 15D;
        }

        int i5 = ColorInt_Normal / 0x1000000;
        int k5 = i5 * 0x1000000 + ColorInt_Iron;
        int l5 = i5 * 0x1000000 + ColorInt_Gold;
        int i6 = i5 * 0x1000000 + ColorInt_Diamond;
        int j6 = i5 * 0x1000000 + ColorInt_Lava;

        if (minecraft.thePlayer.posY < 64D)
        {
            String s8 = "Iron";
            minecraft.fontRenderer.drawString(s8, i - minecraft.fontRenderer.getStringWidth(s8) - 6, (int)d1 - 20, k5);
        }

        if (minecraft.thePlayer.posY < 30D)
        {
            String s9 = "Gold";
            minecraft.fontRenderer.drawString(s9, i - minecraft.fontRenderer.getStringWidth(s9) - 6, (int)d1 - 10, l5);
        }

        if (minecraft.thePlayer.posY < 16D)
        {
            String s10 = "Diamond";
            minecraft.fontRenderer.drawString(s10, i - minecraft.fontRenderer.getStringWidth(s10) - 6, (int)d1, i6);
        }

        if (minecraft.thePlayer.posY < 12D)
        {
            String s11 = "Lava";
            minecraft.fontRenderer.drawString(s11, i - minecraft.fontRenderer.getStringWidth(s11) - 6, (int)d1 + 15, j6);
        }

        int k6 = (int)(minecraft.theWorld.getWorldTime() % 24000L) / 10;
        int l6 = k6 / 100 + 6;

        if (l6 >= 24)
        {
            l6 -= 24;
        }

        String s12 = String.format("%02d", new Object[]
                {
                    Integer.valueOf(l6)
                });
        minecraft.fontRenderer.drawString(s12, 15, j - 18, k2);
        int i7 = (int)((double)(k6 % 100) / 1.66666665D);
        String s13 = String.format("%02d", new Object[]
                {
                    Integer.valueOf(i7)
                });
        minecraft.fontRenderer.drawString(s13, 31, j - 18, k2);
        String s14 = ":";
        minecraft.fontRenderer.drawString(s14, 28, j - 18, k2);
        k2 = ColorInt_Number;
        int j7 = 0;

        if (minecraft.renderViewEntity.isPotionActive(Potion.fireResistance))
        {
            String s15 = "FireResistance";
            j7++;
            minecraft.fontRenderer.drawString(s15, 5, 5 + j7 * 11, k2);
        }

        if (minecraft.renderViewEntity.isPotionActive(Potion.regeneration))
        {
            String s16 = "Regeneration";
            j7++;
            minecraft.fontRenderer.drawString(s16, 5, 5 + j7 * 11, k2);
        }

        if (minecraft.renderViewEntity.isPotionActive(Potion.damageBoost))
        {
            String s17 = "Strength";
            j7++;
            minecraft.fontRenderer.drawString(s17, 5, 5 + j7 * 11, k2);
        }

        if (minecraft.renderViewEntity.isPotionActive(Potion.moveSpeed))
        {
            String s18 = "Speed";
            j7++;
            minecraft.fontRenderer.drawString(s18, 5, 5 + j7 * 11, k2);
        }

        if (minecraft.renderViewEntity.isPotionActive(Potion.hunger))
        {
            String s19 = "Hunger";
            j7++;
            minecraft.fontRenderer.drawString(s19, 5, 5 + j7 * 11, ColorInt_Warning);
        }

        if (k6 % 3 != 1)
        {
            if (minecraft.renderViewEntity.isPotionActive(Potion.poison))
            {
                String s20 = "ALERT POISON!";
                minecraft.fontRenderer.drawString(s20, (int)d - minecraft.fontRenderer.getStringWidth(s20) / 2, (int)d1 - 60, ColorInt_Alert);
            }

            int l2 = ColorInt_Normal;

            if (((EntityPlayer)(entityplayersp)).health < 9)
            {
                String s21 = "ALERT HEALTH";
                int k7 = ((EntityPlayer)(entityplayersp)).health >= 5 ? ColorInt_Warning : ColorInt_Alert;
                minecraft.fontRenderer.drawString(s21, (int)d - minecraft.fontRenderer.getStringWidth(s21) / 2, (int)d1 - 80, k7);
            }

            if (((EntityPlayer)(entityplayersp)).foodStats.getFoodLevel() < 7)
            {
                String s22 = "ALERT FOOD";
                int l7 = ((EntityPlayer)(entityplayersp)).health >= 5 ? ColorInt_Warning : ColorInt_Alert;
                minecraft.fontRenderer.drawString(s22, (int)d - minecraft.fontRenderer.getStringWidth(s22) / 2, (int)d1 - 70, l7);
            }
        }

        ItemStack itemstack = ((EntityPlayer)(entityplayersp)).inventory.getCurrentItem();

        if (itemstack != null && itemstack.isItemStackDamageable())
        {
            int i8 = itemstack.getMaxDamage() - itemstack.getItemDamage();
            int l8 = ColorInt_Number;

            if ((float)itemstack.getItemDamage() / (float)itemstack.getMaxDamage() > 0.9F)
            {
                l8 = ColorInt_Alert;
            }

            String s23 = (new StringBuilder()).append(itemstack.getItem().getStatName()).append(" /").toString();
            minecraft.fontRenderer.drawString(s23, ((int)d - minecraft.fontRenderer.getStringWidth(s23)) + 25, (int)d1 + 63, l8);
            s23 = String.format("%7d", new Object[]
                    {
                        Integer.valueOf(i8)
                    });
            minecraft.fontRenderer.drawString(s23, ((int)d - minecraft.fontRenderer.getStringWidth(s23)) + 60, (int)d1 + 63, l8);
        }

        if (itemstack != null && projectorList.containsKey(Integer.valueOf(itemstack.getItem().itemID)))
        {
            int j8 = 0;
            List list = (List)projectorList.get(Integer.valueOf(itemstack.getItem().itemID));

            for (int j9 = 0; j9 < ((EntityPlayer)(entityplayersp)).inventory.mainInventory.length; j9++)
            {
                itemstack = ((EntityPlayer)(entityplayersp)).inventory.mainInventory[j9];

                if (itemstack != null && list.contains(Integer.valueOf(itemstack.getItem().itemID)))
                {
                    j8 += itemstack.stackSize;
                }
            }

            int k9 = ColorInt_Number;

            if (j8 <= 10)
            {
                k9 = ColorInt_Alert;
            }

            String s24 = (new StringBuilder()).append(Item.itemsList[((Integer)list.get(0)).intValue()].getStatName()).append(" /").toString();
            minecraft.fontRenderer.drawString(s24, ((int)d - minecraft.fontRenderer.getStringWidth(s24)) + 15, (int)d1 + 54, k9);

            if (j8 == 0)
            {
                s24 = "EMPTY";
            }
            else
            {
                s24 = String.format("%7d", new Object[]
                        {
                            Integer.valueOf(j8)
                        });
            }

            minecraft.fontRenderer.drawString(s24, ((int)d - minecraft.fontRenderer.getStringWidth(s24)) + 50, (int)d1 + 54, k9);
        }

        int k8 = (int)d - 143;
        int i9 = j - 62;
        itemstack = ((EntityPlayer)(entityplayersp)).inventory.armorInventory[3];

        if (itemstack != null)
        {
            int l9 = getArmorColor(itemstack);
            drawRectL(k8, i9, k8 + 11, i9 + 11, l9);
        }

        i9 += 12;
        itemstack = ((EntityPlayer)(entityplayersp)).inventory.armorInventory[2];

        if (((EntityPlayer)(entityplayersp)).inventory.armorInventory[2] != null)
        {
            int i10 = getArmorColor(itemstack);
            drawRectL(k8, i9, k8 + 11, i9 + 10, i10);
            drawRectL(k8 -= 6, i9, k8 + 5, i9 + 16, i10);
            drawRectL(k8 += 18, i9, k8 + 5, i9 + 16, i10);
            k8 -= 12;
        }

        i9 += 11;
        itemstack = ((EntityPlayer)(entityplayersp)).inventory.armorInventory[1];

        if (((EntityPlayer)(entityplayersp)).inventory.armorInventory[1] != null)
        {
            int j10 = getArmorColor(itemstack);
            drawRectL(k8, i9, k8 + 11, i9 + 5, j10);
            i9 += 5;
            drawRectL(k8, i9, k8 + 5, i9 + 8, j10);
            drawRectL(k8 += 6, i9, k8 + 5, i9 + 8, j10);
            k8 -= 6;
        }
        else
        {
            i9 += 5;
        }

        i9 += 9;
        itemstack = ((EntityPlayer)(entityplayersp)).inventory.armorInventory[0];

        if (((EntityPlayer)(entityplayersp)).inventory.armorInventory[0] != null)
        {
            int k10 = getArmorColor(itemstack);
            drawRectL(k8, i9, k8 + 5, i9 + 7, k10);
            drawRectL(k8 += 6, i9, k8 + 5, i9 + 7, k10);
        }

        GL11.glDisable(GL11.GL_BLEND);
    }
}
