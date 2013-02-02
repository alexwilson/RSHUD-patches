package net.minecraft.src;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import net.minecraft.client.Minecraft;

public class GRV_GuiRSHUD_ACV extends GRH_GuiRSHUDConfigure
{
    private static final int ma_weapon	= 0;
    private static final int ma_ammo	= 1;
    private static final int ma_armor	= 2;
    private static final int ma_armor1	= 3;
    private static final int ma_armor2	= 4;
    private static final int ma_armor3	= 5;
    private static final int ma_armor4	= 6;
    private static final int ma_speed	= 7;
    private static final int ma_height	= 8;
    private float[] messageAlpha;
    private int[] messageLastValue;

    private long counterMessage;
    private long counterMessage2;
    private long lastTime;
    private String valMes;
    private int colMes;
    private double centerX;
    private double centerY;
    public float textSize;
    float zoomx;
    float zoomy;

    private int lastDamageDealt;

    public boolean isRouteView;
    private int counterCheckWay;
    protected List<double[]> wayPoints;
    protected double[] lastWayPoint;
    protected double[] nowWayPoint;
    protected GRV_EntityMARKER entityRoute;

    //
    public int ColorInt_Maker;
    protected GRH_GuiRSHUDSlider[] colormarker;
    protected GRH_GuiRSHUDSlider colorlowgain;
    public float lowGain;
    public boolean isStatus;

    // RECON
    protected static List viewRECONEntitys;
    public static boolean isRECONUpdate;

    public GRV_GuiRSHUD_ACV(BaseMod basemod)
    {
        super(basemod);
        ColorInt_Normal	 = 0xb199ffb2;
        ColorInt_Warning = 0xb1ffff3f;
        ColorInt_Alert	 = 0xb1ff3f3f;
        ColorInt_Maker	 = 0xcc667fff;
        messageAlpha = new float[10];
        messageLastValue = new int[10];
        counterMessage = 0;
        counterMessage2 = 0;
        valMes = null;
        colMes = 0;
        textSize = 1.0F;//0.5F;
        isRouteView = false;
        counterCheckWay = 0;
        entityRoute = null;
        wayPoints = new ArrayList<double[]>();
        colormarker = new GRH_GuiRSHUDSlider[4];
        lowGain = 0.3F;
        isStatus = true;
        viewRECONEntitys = new ArrayList();
    }

    @Override
    public String getHUDName()
    {
        return "Migrant";
    }

    @Override
    public void initGui()
    {
        //
        super.initGui();
        String[] str = new String[] {"\2474R", "\2472G", "\2471B", "A"};
        float f[];
        f = getRGBA(ColorInt_Normal);

        for (int i = 0; i < 4; i++)
        {
            colornormal[i] = new GRH_GuiRSHUDSlider(10 + i, hwSize - 160, hhSize + 18 + (20 * i) , str[i], f[i]).setDisplayString();
            controlList.add(colornormal[i]);
        }

        colorlowgain = new GRH_GuiRSHUDSlider(14, hwSize - 160, hhSize + 18 + 80 , "low Gain", lowGain).setDisplayString();
        controlList.add(colorlowgain);
        f = getRGBA(ColorInt_Warning);

        for (int i = 0; i < 4; i++)
        {
            colorwarning[i] = new GRH_GuiRSHUDSlider(20 + i, hwSize - 50, hhSize + 18 + (20 * i) , str[i], f[i]).setDisplayString();
            controlList.add(colorwarning[i]);
        }

        f = getRGBA(ColorInt_Alert);

        for (int i = 0; i < 4; i++)
        {
            coloralert[i] = new GRH_GuiRSHUDSlider(30 + i, hwSize + 60, hhSize + 18 + (20 * i) , str[i], f[i]).setDisplayString();
            controlList.add(coloralert[i]);
        }

        f = getRGBA(ColorInt_Maker);

        for (int i = 0; i < 4; i++)
        {
            colormarker[i] = new GRH_GuiRSHUDSlider(30 + i, hwSize + 60, hhSize - 98 + (20 * i) , str[i], f[i]).setDisplayString();
            controlList.add(colormarker[i]);
        }

        controlList.add(new GuiButton(300, hwSize - 50, hhSize - 106, 100, 20, String.format("Value Size x%.1f", textSize)));
        controlList.add(new GuiButton(310, hwSize - 160, hhSize - 38, 100, 20, isStatus ? "Status ON" : "Status OFF"));
    }

    @Override
    public void drawDefaultBackground()
    {
        drawGradientRect(hwSize + 50, hhSize - 116, hwSize + 170, hhSize - 96, 0xd0101010, 0xc0101010);
        drawGradientRect(hwSize - 170, hhSize, hwSize + 170, hhSize + 20, 0xc0101010, 0xd0101010);
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        super.actionPerformed(guibutton);

        if (guibutton.id == 300)
        {
            if (textSize == 1.0F)
            {
                textSize = 0.5F;
            }
            else
            {
                textSize = 1.0F;
            }

            guibutton.displayString = String.format("Value Size x%.1f", textSize);
            mod_GRV_RSHUD_ACV.TextSize = textSize;
        }

        if (guibutton.id == 310)
        {
            isStatus = !isStatus;
            guibutton.displayString = isStatus ? "Status ON" : "Status OFF";
            mod_GRV_RSHUD_ACV.Show_Status = isStatus;
        }
    }

    @Override
    public void drawScreen(int i, int j, float f)
    {
        super.drawScreen(i, j, f);
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
        ColorInt_Maker = setRGBA(
                colormarker[0].getSliderValue(),
                colormarker[1].getSliderValue(),
                colormarker[2].getSliderValue(),
                colormarker[3].getSliderValue()
                );
        lowGain = colorlowgain.getSliderValue();
        mod_GRV_RSHUD_ACV.Color_Normal	= String.format("%08x", ColorInt_Normal);
        mod_GRV_RSHUD_ACV.Color_Warning	= String.format("%08x", ColorInt_Warning);
        mod_GRV_RSHUD_ACV.Color_Alert	= String.format("%08x", ColorInt_Alert);
        mod_GRV_RSHUD_ACV.Color_Marker	= String.format("%08x", ColorInt_Maker);
        mod_GRV_RSHUD_ACV.LowGain = lowGain;
        String s = "NORMAL";
        drawString(fontRenderer, s, hwSize - 110 - fontRenderer.getStringWidth(s) / 2, hhSize + 6, ColorInt_Normal);
        s = "WARNING";
        drawString(fontRenderer, s, hwSize - fontRenderer.getStringWidth(s) / 2, hhSize + 6, ColorInt_Warning);
        s = "ALERT";
        drawString(fontRenderer, s, hwSize + 110 - fontRenderer.getStringWidth(s) / 2, hhSize + 6, ColorInt_Alert);
        s = "MARKER";
        drawString(fontRenderer, s, hwSize + 110 - fontRenderer.getStringWidth(s) / 2, hhSize - 110, ColorInt_Maker);
        GL11.glEnable(GL11.GL_BLEND);
        s = "MARKER";
        fontRenderer.drawString(s, hwSize - fontRenderer.getStringWidth(s) / 2, hhSize - 75, ColorInt_Maker);
        s = "NORMAL";
        fontRenderer.drawString(s, hwSize - fontRenderer.getStringWidth(s) / 2, hhSize - 45, ColorInt_Normal);
        s = "LOW GAIN";
        fontRenderer.drawString(s, hwSize - fontRenderer.getStringWidth(s) / 2, hhSize - 35, getColorAmp(ColorInt_Normal, lowGain));
        s = "WARNING";
        fontRenderer.drawString(s, hwSize - fontRenderer.getStringWidth(s) / 2, hhSize - 25, ColorInt_Warning);
        s = "ALERT";
        fontRenderer.drawString(s, hwSize - fontRenderer.getStringWidth(s) / 2, hhSize - 15, ColorInt_Alert);
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    public void renderRSHUD(Minecraft mc, int i, int j)
    {
        // ACV
        if (mc.theWorld == null || mc.thePlayer == null)
        {
            counterMessage = 0L;
            counterMessage2 = 0L;
            return;
        }

        EntityPlayer mcpl = mc.thePlayer;

        if (mcpl == null || mcpl.isDead)
        {
            return;
        }

        // Route
        if (entityRoute == null || entityRoute.isDead || entityRoute.worldObj != mc.theWorld)
        {
            entityRoute = new GRV_EntityMARKER(mc.theWorld);
            setStartRoute(mcpl);
            entityRoute.setPosition(mcpl.posX, mcpl.posY, mcpl.posZ);
            mc.theWorld.spawnEntityInWorld(entityRoute);
        }

        if (isRouteView)
        {
            checkRoutePoint(mc.theWorld, mcpl);
            checkReRoute(mc.theWorld, mcpl);
        }

        Tessellator tessellator = Tessellator.instance;
        centerX = i / 2D;
        centerY = j / 2D;
        zoomx = (float)mc.displayWidth / (float)i;
        zoomy = (float)mc.displayHeight / (float)j;
        boolean iblink = (mc.theWorld.getWorldTime() & 2) == 2;
        int deltaTime = (int)(mc.theWorld.getWorldTime() - lastTime);
        double ldx;
        double ldy;
        double r0 = 60D;
        double r1;
        double r2;
        int d1 = (int)(12F * textSize);
        int d2 = (int)(16F * textSize);
        float valFoodExh = 0F;
        int valFoodTimer = 0;

        try
        {
            valFoodExh = (Float)(ModLoader.getPrivateValue(FoodStats.class, mcpl.foodStats, 2)); //4
            valFoodTimer = (Integer)(ModLoader.getPrivateValue(FoodStats.class, mcpl.foodStats, 3)); //80
        }
        catch (Exception e)
        {
        }

        //RECON
//		if (RenderManager.instance.renderEngine != null && !viewRECONEntitys.isEmpty()) {
        if (entityRoute != null && !entityRoute.isDead && !viewRECONEntitys.isEmpty())
        {
            RenderManager lrm = RenderManager.instance;

            if (lrm.options != null)
            {
                GL11.glMatrixMode(GL11.GL_PROJECTION);
                GL11.glPushMatrix();
                GL11.glLoadMatrix(GRV_RenderMARKER.matProjection);
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glPushMatrix();
                GL11.glLoadMatrix(GRV_RenderMARKER.matModel);
                Iterator literator1 = viewRECONEntitys.iterator();

                while (literator1.hasNext())
                {
                    Entity lentity1 = (Entity)literator1.next();

                    if (lentity1 != null && !(lentity1 == mcpl))
                    {
                        GL11.glPushMatrix();
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glDisable(GL11.GL_ALPHA_TEST);
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                        GL11.glDisable(GL11.GL_LIGHTING);
                        double lw = lentity1.width + 0.2D;
                        double lh = lentity1.height / 2D + 0.1D;
                        GL11.glPushMatrix();
                        GL11.glTranslated(lentity1.lastTickPosX - lrm.renderPosX, lentity1.lastTickPosY - lrm.renderPosY, lentity1.lastTickPosZ - lrm.renderPosZ);
                        GL11.glRotatef(-mcpl.prevRotationYaw, 0F, 1F, 0F);
                        GL11.glTranslated(0D, lh, 0D);
                        GL11.glRotatef(mcpl.prevRotationPitch, 1F, 0F, 0F);
                        GL11.glDisable(GL11.GL_TEXTURE_2D);
                        GL11.glColorMask(true, true, true, true);
                        GL11.glLineWidth(1.0F);
                        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
                        setGLTextColor(ColorInt_Maker, 1.0F);
                        tessellator.startDrawing(GL11.GL_LINE_LOOP);
                        tessellator.addVertex(lw, lh, 0D);
                        tessellator.addVertex(-lw, lh, 0D);
                        tessellator.addVertex(-lw, -lh, 0D);
                        tessellator.addVertex(lw, -lh, 0D);
                        tessellator.draw();
                        GL11.glPopMatrix();
                        GL11.glEnable(GL11.GL_TEXTURE_2D);

                        if (lentity1 instanceof IMob || lentity1 instanceof IRangedAttackMob || lentity1 instanceof EntityEnderman || lentity1 instanceof EntityBat || (lentity1 instanceof EntityWolf && ((EntityWolf)lentity1).isAngry()))
                        {
                            GL11.glColorMask(true, false, false, true);
                        }
                        else
                        {
                            GL11.glColorMask(false, true, false, true);
                        }

                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        lrm.renderEntityWithPosYaw(lentity1, lentity1.lastTickPosX - lrm.renderPosX, lentity1.lastTickPosY - lrm.renderPosY, lentity1.lastTickPosZ - lrm.renderPosZ, lentity1.prevRotationYaw, 0F);
//						RenderManager.instance.renderEntity(lentity1, 0F);
                        GL11.glPopMatrix();
                    }
                }

                GL11.glPopMatrix();
                GL11.glMatrixMode(GL11.GL_PROJECTION);
                GL11.glPopMatrix();
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glColorMask(true, true, true, true);
                isRECONUpdate = false;
            }
        }

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_LIGHTING);
        float deg;
        float valEN = ((float)mcpl.foodStats.getFoodLevel() + mcpl.foodStats.getSaturationLevel() - (valFoodExh / 4F)) / 40F;
        valEN = Math.max(valEN, 0F);
        int colEN = getTextColor(valEN, 0.4F, 0.1F);
        GL11.glLineWidth(1F);
        setGLTextColor(colEN, 0.5F);
        drawMeter(tessellator, r0 + 2.5D, r0 + 5.0D, 88F, 0F, valEN);
        r1 = r0 + 2.0D;
        GL11.glLineWidth(1.5F);
        setGLTextColor(ColorInt_Normal, 1.0F);
        tessellator.startDrawing(GL11.GL_LINE_STRIP);
        deg = 0 / (180F / (float)Math.PI);
        ldx = centerX + r1 + 5D;
        ldy = centerY;
        tessellator.addVertex(ldx, ldy, 0.0D);

        for (int li = 0; li <= 88; li += 2)
        {
            deg = li / (180F / (float)Math.PI);
            ldx = MathHelper.cos(deg) * r1 + centerX;
            ldy = MathHelper.sin(deg) * r1 + centerY;
            tessellator.addVertex(ldx, ldy, 0.0D);
        }

        tessellator.draw();
        tessellator.startDrawing(GL11.GL_LINE_STRIP);
        tessellator.addVertex(ldx, ldy - 2D, 0.0D);
        tessellator.addVertex(ldx, ldy, 0.0D);
        tessellator.addVertex(ldx, ldy + 3.0D, 0.0D);
        tessellator.draw();
        float valAP = Math.min((float)mcpl.health + (float)valFoodTimer / 80F, (float)mcpl.getMaxHealth()) / (float)mcpl.getMaxHealth();
        int colAP = getTextColor(valAP, 0.4F, 0.2F);
        GL11.glLineWidth(1F);
        setGLTextColor(colAP, 0.5F);
        drawMeter(tessellator, r0 + 2.5D, r0 + 5.0D, 92F, 180F, valAP);
        r1 = r0 + 2.0D;
        GL11.glLineWidth(1.5F);
        setGLTextColor(ColorInt_Normal, 1.0F);
        tessellator.startDrawing(GL11.GL_LINE_STRIP);
        deg = 180 / (180F / (float)Math.PI);
        ldx = MathHelper.cos(deg) * r1 + centerX - 5D;
        ldy = MathHelper.sin(deg) * r1 + centerY;
        tessellator.addVertex(ldx, ldy, 0.0D);

        for (int li = 180; li >= 92; li -= 2)
        {
            deg = li / (180F / (float)Math.PI);
            ldx = MathHelper.cos(deg) * r1 + centerX;
            ldy = MathHelper.sin(deg) * r1 + centerY;
            tessellator.addVertex(ldx, ldy, 0.0D);
        }

        tessellator.draw();
        tessellator.startDrawing(GL11.GL_LINE_STRIP);
        tessellator.addVertex(ldx, ldy - 2D, 0.0D);
        tessellator.addVertex(ldx, ldy, 0.0D);
        tessellator.addVertex(ldx, ldy + 3.0D, 0.0D);
        tessellator.draw();
        // WEAPON
        ItemStack itemstack = mcpl.getCurrentEquippedItem();
        int valWep1 = 0;
        float valWep2 = 0F;
        int colWep = 0;

        if (itemstack != null)
        {
            if (itemstack.isItemStackDamageable())
            {
                valWep1 = itemstack.getMaxDamage() - itemstack.getItemDamage();
                valWep2 = (float)valWep1 / (float)itemstack.getMaxDamage();
            }
            else
            {
                valWep1 = itemstack.stackSize;
                valWep2 = (float)valWep1 / (float)itemstack.getMaxStackSize();
            }

            GL11.glLineWidth(1.0F);
            colWep = getTextColor(valWep2, 0.3F, 0.1F);
            setGLTextColor(colWep, 0.5F);
            drawMeter(tessellator, r0 - 3D, r0, 272, 360 - d1, valWep2);
        }

        int valAmmo = -1;
        int colAmmo = 0;

        if (containsAmmo(itemstack))
        {
            valAmmo = countAmmo(itemstack, mcpl);
            float maxammo = (float)itemstack.getMaxDamage();
            maxammo = maxammo > 0 ? maxammo : (float)Item.itemsList[projectorList.get(itemstack.getItem().itemID).get(0)].maxStackSize;
            maxammo = (float)valAmmo / maxammo;
            colAmmo = getTextColor(maxammo, 0.3F, 0.1F);
            setGLTextColor(colAmmo, 0.5F);
            drawMeter(tessellator, r0 + 2D, r0 + 3D, 272, 360 - d1, Math.min(1.0F, maxammo));
            r1 = r0 + 1.0D;
            setGLTextColor(ColorInt_Normal, 0.8F);
            tessellator.startDrawing(GL11.GL_LINE_STRIP);
            deg = -88F / (180F / (float)Math.PI);
            ldx = MathHelper.cos(deg) * r1 + centerX;
            ldy = MathHelper.sin(deg) * r1 + centerY - 3D;
            tessellator.addVertex(ldx, ldy, 0.0D);

            for (int li = 2; li <= 90 - d1; li += 2)
            {
                deg = (-90 + li) / (180F / (float)Math.PI);
                ldx = MathHelper.cos(deg) * r1 + centerX;
                ldy = MathHelper.sin(deg) * r1 + centerY;
                tessellator.addVertex(ldx, ldy, 0.0D);
            }

            tessellator.addVertex(ldx + 3D, ldy, 0.0D);
            tessellator.draw();
        }

        GL11.glLineWidth(1.0F);
        // SPEED
        double lvx = mcpl.lastTickPosX - mcpl.posX;
        double lvy = mcpl.lastTickPosY - mcpl.posY;
        double lvz = mcpl.lastTickPosZ - mcpl.posZ;
        float valSpeed = MathHelper.sqrt_double(lvx * lvx + lvy * lvy + lvz * lvz) * 1000F;
        int colSpeed = ColorInt_Normal;

        if (valSpeed >= 1000F)
        {
            colSpeed = ColorInt_Alert;
        }
        else if (valSpeed >= 600F)
        {
            colSpeed = ColorInt_Warning;
        }

        setGLTextColor(colSpeed, 0.5F);
        drawMeter(tessellator, r0 - 3D, r0, 88, d1, valSpeed / 1000F);
        // HEIGHT
//        float valHeight = (float)(mcpl.posY / (double)mc.theWorld.worldMaxY);
        float valHeight = (float)(mcpl.posY / mc.theWorld.getHeight());
        int colHeight = ColorInt_Normal;

        if (valHeight > 1.0F || valHeight < 0.0F)
        {
            colHeight = ColorInt_Alert;
        }

        setGLTextColor(colHeight, 0.5F);
        drawMeter(tessellator, r0 - 3D, r0, 92, 180 - d1, valHeight);
        // AC
        float valArmor[] = new float[] { -1F, -1F, -1F, -1F};
        int colArmor[] = new int[4];
        float valAC = 0F;
        int valACAll = 0;

        for (int li = 0; li < 4; li++)
        {
            ItemStack itemarmor = mcpl.inventory.armorInventory[3 - li];

            if (itemarmor != null)
            {
                valArmor[li] = 1F - (float)itemarmor.getItemDamage() / (float)itemarmor.getMaxDamage();
                valAC += valArmor[li];
                int ll = itemarmor.getMaxDamage() - itemarmor.getItemDamage();

                if (messageLastValue[ma_armor1 + li] == ll)
                {
                    messageAlpha[ma_armor1 + li] -= 0.1F * (float)deltaTime;
                }
                else
                {
                    messageLastValue[ma_armor1 + li] = ll;
                    messageAlpha[ma_armor1 + li] = 5F;
                }

                valACAll += ll;
                colArmor[li] = getColorAmp(getArmorColor(itemarmor), Math.max(lowGain, Math.min(1.0F, messageAlpha[ma_armor1 + li])));
            }
            else
            {
                messageLastValue[ma_armor1 + li] = -1;
            }
        }

        valAC /= 4F;
        int colAC = getTextColor(valAC, 0.4F, 0.2F);
        setGLTextColor(colAC, 0.5F);
        drawMeter(tessellator, r0 - 3D, r0, 268, 180 + d1, valAC);
        float lf[] = getRGBA(ColorInt_Normal);
        // DIR
        GL11.glPushMatrix();
        GL11.glTranslatef((float)centerX, (float)centerY, 0F);
        GL11.glRotatef(180 - mcpl.rotationYaw, 0F, 0F, 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, lf[3] * 0.8F);
        ldy = -r0 - 9D;
        tessellator.startDrawing(GL11.GL_TRIANGLES);
        tessellator.addVertex(0D, ldy, 0.0D);
        tessellator.addVertex(2.5D, ldy - 2D, 0.0D);
        tessellator.addVertex(-2.5D, ldy - 2D, 0.0D);
        tessellator.draw();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, lf[3] * 0.4F);
        ldy = r0 + 9D;
        tessellator.startDrawing(GL11.GL_TRIANGLES);
        tessellator.addVertex(0D, ldy, 0.0D);
        tessellator.addVertex(-2.5D, ldy + 2D, 0.0D);
        tessellator.addVertex(2.5D, ldy + 2D, 0.0D);
        tessellator.draw();
        GL11.glPopMatrix();
        GL11.glLineWidth(1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, lf[3] * 0.6F);
        r1 = r0;
        tessellator.startDrawing(GL11.GL_LINE_STRIP);

        for (int li = d1; li <= 180 - d1; li += 2)
        {
            deg = li / (180F / (float)Math.PI);
            ldx = MathHelper.cos(deg) * r1 + centerX;
            ldy = MathHelper.sin(deg) * r1 + centerY;
            tessellator.addVertex(ldx, ldy, 0.0D);
        }

        tessellator.draw();
        tessellator.startDrawing(GL11.GL_LINE_STRIP);

        for (int li = 180 + d1; li <= 360 - d1; li += 2)
        {
            deg = li / (180F / (float)Math.PI);
            ldx = MathHelper.cos(deg) * r1 + centerX;
            ldy = MathHelper.sin(deg) * r1 + centerY;
            tessellator.addVertex(ldx, ldy, 0.0D);
        }

        tessellator.draw();
        r2 = r1 - 3D;
        tessellator.startDrawing(GL11.GL_LINES);
        deg = d1 / (180F / (float)Math.PI);
        ldx = MathHelper.cos(deg);
        ldy = MathHelper.sin(deg);
        float deg2 = (float)(Math.atan2(ldy * r2, ldx * r2));
        ldx *= r1;
        ldy *= r1;
        tessellator.addVertex(centerX + ldx, centerY + ldy, 0D);
        tessellator.addVertex(centerX + ldx - 8D, centerY + ldy, 0D);
        tessellator.addVertex(centerX - ldx, centerY + ldy, 0D);
        tessellator.addVertex(centerX - ldx + 8D, centerY + ldy, 0D);
        tessellator.addVertex(centerX + ldx, centerY - ldy, 0D);
        tessellator.addVertex(centerX + ldx - 8D, centerY - ldy, 0D);
        tessellator.addVertex(centerX - ldx, centerY - ldy, 0D);
        tessellator.addVertex(centerX - ldx + 8D, centerY - ldy, 0D);
        deg = 88 / (180F / (float)Math.PI);
        ldx = MathHelper.cos(deg);
        ldy = MathHelper.sin(deg);
        float deg3 = (float)(Math.atan2(ldy * r2, ldx * r2));
        ldx *= r1;
        ldy *= r1;
        tessellator.addVertex(centerX + ldx, centerY + ldy, 0D);
        tessellator.addVertex(centerX + ldx, centerY + ldy - 8D, 0D);
        tessellator.addVertex(centerX - ldx, centerY + ldy, 0D);
        tessellator.addVertex(centerX - ldx, centerY + ldy - 8D, 0D);
        tessellator.addVertex(centerX + ldx, centerY - ldy, 0D);
        tessellator.addVertex(centerX + ldx, centerY - ldy + 8D, 0D);
        tessellator.addVertex(centerX - ldx, centerY - ldy, 0D);
        tessellator.addVertex(centerX - ldx, centerY - ldy + 8D, 0D);
        tessellator.draw();
        tessellator.startDrawing(GL11.GL_LINE_STRIP);
        ldx = centerX + MathHelper.cos(deg2) * r2;
        ldy = centerY + MathHelper.sin(deg2) * r2;
        tessellator.addVertex(ldx, ldy, 0.0D);

        for (int li = d2; li <= 86; li += 2)
        {
            deg = li / (180F / (float)Math.PI);
            ldx = centerX + MathHelper.cos(deg) * r2;
            ldy = centerY + MathHelper.sin(deg) * r2;
            tessellator.addVertex(ldx, ldy, 0.0D);
        }

        ldx = centerX + MathHelper.cos(deg3) * r2;
        ldy = centerY + MathHelper.sin(deg3) * r2;
        tessellator.addVertex(ldx, ldy, 0.0D);
        tessellator.draw();
        tessellator.startDrawing(GL11.GL_LINE_STRIP);
        ldx = centerX - MathHelper.cos(deg3) * r2;
        ldy = centerY + MathHelper.sin(deg3) * r2;
        tessellator.addVertex(ldx, ldy, 0.0D);

        for (int li = 94; li <= 180 - d2; li += 2)
        {
            deg = li / (180F / (float)Math.PI);
            ldx = centerX + MathHelper.cos(deg) * r2;
            ldy = centerY + MathHelper.sin(deg) * r2;
            tessellator.addVertex(ldx, ldy, 0.0D);
        }

        ldx = centerX - MathHelper.cos(deg2) * r2;
        ldy = centerY + MathHelper.sin(deg2) * r2;
        tessellator.addVertex(ldx, ldy, 0.0D);
        tessellator.draw();
        tessellator.startDrawing(GL11.GL_LINE_STRIP);
        ldx = centerX - MathHelper.cos(deg2) * r2;
        ldy = centerY - MathHelper.sin(deg2) * r2;
        tessellator.addVertex(ldx, ldy, 0.0D);

        for (int li = 180 + d2; li <= 266; li += 2)
        {
            deg = li / (180F / (float)Math.PI);
            ldx = centerX + MathHelper.cos(deg) * r2;
            ldy = centerY + MathHelper.sin(deg) * r2;
            tessellator.addVertex(ldx, ldy, 0.0D);
        }

        ldx = centerX - MathHelper.cos(deg3) * r2;
        ldy = centerY - MathHelper.sin(deg3) * r2;
        tessellator.addVertex(ldx, ldy, 0.0D);
        tessellator.draw();
        tessellator.startDrawing(GL11.GL_LINE_STRIP);
        ldx = centerX + MathHelper.cos(deg3) * r2;
        ldy = centerY - MathHelper.sin(deg3) * r2;
        tessellator.addVertex(ldx, ldy, 0.0D);

        for (int li = 274; li <= 360 - d2; li += 2)
        {
            deg = li / (180F / (float)Math.PI);
            ldx = centerX + MathHelper.cos(deg) * r2;
            ldy = centerY + MathHelper.sin(deg) * r2;
            tessellator.addVertex(ldx, ldy, 0.0D);
        }

        ldx = centerX + MathHelper.cos(deg2) * r2;
        ldy = centerY - MathHelper.sin(deg2) * r2;
        tessellator.addVertex(ldx, ldy, 0.0D);
        tessellator.draw();
        GL11.glLineWidth(1.0F);
        setGLTextColor(ColorInt_Alert, 0.5F);
        double r3 = r0 * 0.70710678D;
        drawXross(tessellator, centerX, centerY, r3 * 0.1D, 5D, 0.2D);
        drawXross(tessellator, centerX, centerY, r3 - 11D, 4D, 0.1D);
        drawXross(tessellator, centerX, centerY, r3 - 7D, 5D, 0.6D);

        // Marker
        if (entityRoute != null && isRouteView)
        {
            boolean flag[] = new boolean[] {false, false, false};
            setGLTextColor(ColorInt_Maker, 1.0F);

            for (int li = 0; li < 3 && entityRoute.routePos[li] != null && entityRoute.markerPos[li] != null; li++)
            {
                float lfz = entityRoute.markerPos[li].get(2);

                if (lfz >= 0.0F && lfz <= 1.0F)
                {
                    flag[li] = true;
                    GL11.glPushMatrix();
                    GL11.glTranslatef(entityRoute.markerPos[li].get(0) / zoomx, j - entityRoute.markerPos[li].get(1) / zoomy, 0.0F);
//            		GL11.glScalef(0.5F, 0.5F, 0.5F);
                    GL11.glLineWidth(1.5F);
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    tessellator.startDrawing(GL11.GL_LINE_LOOP);
                    tessellator.addVertex(-7D, -7D, 0D);
                    tessellator.addVertex(7D, -7D, 0D);
                    tessellator.addVertex(7D, 7D, 0D);
                    tessellator.addVertex(-7D, 7D, 0D);
                    tessellator.draw();

                    if (li < 2 && entityRoute.markerPos[li + 1] != null)
                    {
                        GL11.glLineWidth(1.0F);
                        tessellator.startDrawing(GL11.GL_LINE_LOOP);
                        tessellator.addVertex(-6D, -6D, 0D);
                        tessellator.addVertex(6D, -6D, 0D);
                        tessellator.addVertex(6D, 6D, 0D);
                        tessellator.addVertex(-6D, 6D, 0D);
                        tessellator.draw();
                        tessellator.startDrawing(GL11.GL_LINES);
                        tessellator.addVertex(-6D, -6D, 0D);
                        tessellator.addVertex(6D, 6D, 0D);
                        tessellator.addVertex(6D, -6D, 0D);
                        tessellator.addVertex(-6D, 6D, 0D);
                        tessellator.draw();
                        drawOutline(tessellator, -8D, -8D, 8D, 8D, 1D);
                    }

                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glScalef(textSize, textSize, textSize);
                    double ld0 = entityRoute.routePos[li][0] - mcpl.posX;
                    double ld1 = entityRoute.routePos[li][1] - mcpl.posY;
                    double ld2 = entityRoute.routePos[li][2] - mcpl.posZ;
                    String sdis = String.format("%.2f", MathHelper.sqrt_double(ld0 * ld0 + ld1 * ld1 + ld2 * ld2));
                    mc.fontRenderer.drawString(sdis, mc.fontRenderer.getStringWidth("0.00") - mc.fontRenderer.getStringWidth(sdis), (int)(10F / textSize), ColorInt_Maker);
                    sdis = "ROUTE";
                    mc.fontRenderer.drawString(sdis, -mc.fontRenderer.getStringWidth(sdis) / 2, (int)(-10.5F / textSize - mc.fontRenderer.FONT_HEIGHT * textSize), ColorInt_Maker);
                    GL11.glPopMatrix();
                }
            }

            GL11.glLineWidth(5.0F);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            setGLTextColor(ColorInt_Maker, 0.5F);
            tessellator.startDrawing(GL11.GL_LINES);

            if (flag[0])
            {
                float f = entityRoute.playerPos.get(2);
                f = (f > 1.0F) ? -1 : 1;
                tessellator.addVertex(entityRoute.playerPos.get(0) / zoomx, j - entityRoute.playerPos.get(1) * f / zoomy, 0D);
                tessellator.addVertex(entityRoute.markerPos[0].get(0) / zoomx, j - entityRoute.markerPos[0].get(1) / zoomy, 0D);
            }

            if (flag[0] && flag[1])
            {
                tessellator.addVertex(entityRoute.markerPos[0].get(0) / zoomx, j - entityRoute.markerPos[0].get(1) / zoomy, 0D);
                tessellator.addVertex(entityRoute.markerPos[1].get(0) / zoomx, j - entityRoute.markerPos[1].get(1) / zoomy, 0D);
            }

            if (flag[1] && flag[2])
            {
                tessellator.addVertex(entityRoute.markerPos[1].get(0) / zoomx, j - entityRoute.markerPos[1].get(1) / zoomy, 0D);
                tessellator.addVertex(entityRoute.markerPos[2].get(0) / zoomx, j - entityRoute.markerPos[2].get(1) / zoomy, 0D);
            }

            tessellator.draw();
        }

        // RECON
        List llist1 = mc.theWorld.getLoadedEntityList();

        for (int li = 0; li < llist1.size(); li++)
        {
            Entity lentity = (Entity)llist1.get(li);

            if (lentity instanceof GRV_EntityRECON)
            {
                drawRECON(tessellator, mc, (GRV_EntityRECON)lentity);
            }
        }

        int txtcolor;
        String s;
        GL11.glPushMatrix();
        GL11.glScalef(textSize, textSize, textSize);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        // EN
        s = String.format("%06.2f", valEN * 100F);
        ldx = centerX + r0 + 3D;
        ldy = centerY - 1D;
        mc.fontRenderer.drawString(s, (int)(ldx / textSize), (int)(ldy / textSize) - mc.fontRenderer.FONT_HEIGHT, colEN);
        // AP
        s = String.format("%06.2f", valAP * 100F);
        ldx = centerX - r0 - 1D;
        ldy = centerY - 1D;
        mc.fontRenderer.drawString(s, (int)(ldx / textSize) - mc.fontRenderer.getStringWidth(s), (int)(ldy / textSize) - mc.fontRenderer.FONT_HEIGHT, colAP);

        // WEAPON
        if (itemstack != null)
        {
//        if (itemstack != null && itemstack.getMaxDamage() > 0) {
            s = String.valueOf(valWep1);

            if (messageLastValue[ma_weapon] == valWep1)
            {
                messageAlpha[ma_weapon] -= 0.1F * (float)deltaTime;
            }
            else
            {
                messageLastValue[ma_weapon] = valWep1;
                messageAlpha[ma_weapon] = 5F;
            }

            ldx = centerX + r0 - 4D;
            ldy = centerY;
            txtcolor = getColorAmp(colWep, Math.max(lowGain, Math.min(1.0F, messageAlpha[ma_weapon])));
            mc.fontRenderer.drawString(s, (int)(ldx / textSize - 6D) - mc.fontRenderer.getStringWidth(s), (int)(ldy / textSize) - mc.fontRenderer.FONT_HEIGHT * 3, txtcolor);
        }
        else
        {
            messageLastValue[ma_weapon] = 0;
        }

        if (valAmmo > 0)
        {
            s = String.valueOf(valAmmo);

            if (messageLastValue[ma_ammo] == valAmmo)
            {
                messageAlpha[ma_ammo] -= 0.1F * (float)deltaTime;
            }
            else
            {
                messageLastValue[ma_ammo] = valAmmo;
                messageAlpha[ma_ammo] = 5F;
            }

            ldx = centerX + r0 + 5D;
            ldy = centerY;
            txtcolor = getColorAmp(colAmmo, Math.max(lowGain, Math.min(1.0F, messageAlpha[ma_ammo])));
            mc.fontRenderer.drawString(s, (int)(ldx / textSize), (int)(ldy / textSize) - mc.fontRenderer.FONT_HEIGHT * 3, txtcolor);
        }
        else
        {
            messageLastValue[ma_ammo] = -1;
        }

        // SPEED
        s = String.format("%.2f", valSpeed);

        if (messageLastValue[ma_speed] == (int)valSpeed)
        {
            messageAlpha[ma_speed] -= 0.1F * (float)deltaTime;
        }
        else
        {
            messageLastValue[ma_speed] = (int)valSpeed;
            messageAlpha[ma_speed] = 5F;
        }

        ldx = centerX + r0 - 4D;
        ldy = centerY;
        txtcolor = getColorAmp(colSpeed, Math.max(lowGain, Math.min(1.0F, messageAlpha[ma_speed])));
        mc.fontRenderer.drawString(s, (int)(ldx / textSize - 6D) - mc.fontRenderer.getStringWidth(s), (int)(ldy / textSize) + mc.fontRenderer.FONT_HEIGHT * 2, txtcolor);
        // HEIGHT
        s = String.format("%06.2f", mcpl.posY);

        if (messageLastValue[ma_height] == (int)mcpl.posY)
        {
            messageAlpha[ma_height] -= 0.1F * (float)deltaTime;
        }
        else
        {
            messageLastValue[ma_height] = (int)mcpl.posY;
            messageAlpha[ma_height] = 5F;
        }

        ldx = centerX - r0 + 4D;
        ldy = centerY;
        txtcolor = getColorAmp(colHeight, Math.max(lowGain, Math.min(1.0F, messageAlpha[ma_height])));
        mc.fontRenderer.drawString(s, (int)(ldx / textSize + 6D), (int)(ldy / textSize) + mc.fontRenderer.FONT_HEIGHT * 2, txtcolor);

        // AC
        if (valAC > 0F)
        {
            s = String.format("%06.2f", valAC * 100F);

            if (messageLastValue[ma_armor] == valACAll)
            {
                messageAlpha[ma_armor] -= 0.1F * (float)deltaTime;
            }
            else
            {
                messageLastValue[ma_armor] = valACAll;
                messageAlpha[ma_armor] = 5F;
            }

            ldx = centerX - r0 + 4D;
            ldy = centerY;
            txtcolor = getColorAmp(colAC, Math.max(lowGain, Math.min(1.0F, messageAlpha[ma_armor])));
            mc.fontRenderer.drawString(s, (int)(ldx / textSize + 6D), (int)(ldy / textSize) - mc.fontRenderer.FONT_HEIGHT * 3, txtcolor);
        }

        if (valArmor[0] >= 0)
        {
            s = String.valueOf(messageLastValue[ma_armor1]);
            ldx = centerX - r0 - 15D;
            ldy = centerY;
            mc.fontRenderer.drawString(s, (int)(ldx / textSize + 26D) - mc.fontRenderer.getStringWidth(s), (int)(ldy / textSize) - mc.fontRenderer.FONT_HEIGHT * 6, colArmor[0]);
        }

        if (valArmor[1] >= 0)
        {
            s = String.valueOf(messageLastValue[ma_armor2]);
            ldx = centerX - r0 - 11D;
            ldy = centerY;
            mc.fontRenderer.drawString(s, (int)(ldx / textSize + 16D) - mc.fontRenderer.getStringWidth(s), (int)(ldy / textSize) - mc.fontRenderer.FONT_HEIGHT * 5, colArmor[1]);
        }

        if (valArmor[2] >= 0)
        {
            s = String.valueOf(messageLastValue[ma_armor3]);
            ldx = centerX - r0 - 8D;
            ldy = centerY;
            mc.fontRenderer.drawString(s, (int)(ldx / textSize + 7D) - mc.fontRenderer.getStringWidth(s), (int)(ldy / textSize) - mc.fontRenderer.FONT_HEIGHT * 4, colArmor[2]);
        }

        if (valArmor[3] >= 0)
        {
            s = String.valueOf(messageLastValue[ma_armor4]);
            ldx = centerX - r0 - 5D;
            ldy = centerY;
            mc.fontRenderer.drawString(s, (int)(ldx / textSize) - mc.fontRenderer.getStringWidth(s), (int)(ldy / textSize) - mc.fontRenderer.FONT_HEIGHT * 3, colArmor[3]);
        }

        //
        GL11.glPopMatrix();

        // MESSAGE
        if (mcpl.hurtTime > 0)
        {
            valMes = "DANGER";
            colMes = ColorInt_Alert;
            counterMessage = mc.theWorld.getWorldTime() + 20L;
        }
        else if (mcpl.foodStats.getFoodLevel() == 0)
        {
            valMes = "CHARGE";
            colMes = ColorInt_Alert;
            counterMessage = mc.theWorld.getWorldTime() + 5L;
        }
        else if (mcpl.foodStats.getFoodLevel() < 5)
        {
            valMes = "EN LOW";
            colMes = ColorInt_Warning;
            counterMessage = mc.theWorld.getWorldTime() + 5L;
        }

        if (counterMessage < mc.theWorld.getWorldTime())
        {
            valMes = null;
        }

        if (valMes != null)
        {
            int widMes = mc.fontRenderer.getStringWidth(valMes) / 2;
            ldx = centerX;
            ldy = centerY + 25D;
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            mc.fontRenderer.drawString(valMes, (int)ldx - widMes + 1, (int)ldy, getColorAmp(colMes, iblink ? 0.3F : 1.0F));
            widMes += 1;
            GL11.glLineWidth(1.0F);
            setGLTextColor(ColorInt_Normal, iblink ? 0.3F : 1.0F);
            drawOutline(tessellator, ldy - 1D, ldx - widMes, ldy + mc.fontRenderer.FONT_HEIGHT, ldx + widMes, 2D);
        }

        // BOOST
        if (mcpl.isSprinting() || mcpl.isJumping)
        {
            s = "BOOSTER";
            ldx = centerX;
            ldy = centerY + r0 - 20D;
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            int widMes = mc.fontRenderer.getStringWidth(s) / 2;
            mc.fontRenderer.drawString(s, (int)ldx + 1 - widMes, (int)ldy, getColorAmp(ColorInt_Alert, 0.5F));
            setGLTextColor(ColorInt_Normal, 1.0F);
            widMes += 1;
            GL11.glLineWidth(1.0F);
            drawOutline(tessellator, ldy - 1D, ldx - widMes, ldy + mc.fontRenderer.FONT_HEIGHT, ldx + widMes, 2D);
        }

        // HIT
        int dds = mc.statFileWriter.writeStat(StatList.damageDealtStat);

        if (lastDamageDealt != dds)
        {
            counterMessage2 = mc.theWorld.getWorldTime() + 20L;
            lastDamageDealt = dds;
        }

        if (counterMessage2 >= mc.theWorld.getWorldTime())
        {
            s = "HIT";
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glPushMatrix();
            GL11.glTranslated(centerX, centerY - r0 / 2D, 0D);
            GL11.glScalef(2F, 2F, 2F);
            int widMes = mc.fontRenderer.getStringWidth(s) / 2;
            mc.fontRenderer.drawString(s, (int)(1 - widMes), 0, getColorAmp(ColorInt_Normal, iblink ? 1.0F : 0.3F));
            widMes += 1;
            GL11.glLineWidth(1.0F);
            drawOutline(tessellator, -1D, -widMes, mc.fontRenderer.FONT_HEIGHT, widMes, 1D);
            GL11.glPopMatrix();
        }

        if (isStatus)
        {
            // STATUS
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glPushMatrix();
            GL11.glScalef(0.5F, 0.5F, 0.5F);

//            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            for (Iterator iterator = mcpl.getActivePotionEffects().iterator(); iterator.hasNext();)
            {
                PotionEffect potioneffect = (PotionEffect)iterator.next();
                int lpid = potioneffect.getPotionID();
//            for (int lpid = 0; lpid < 32; lpid++) {
                Potion potion = Potion.potionTypes[lpid];

                if (potion == null)
                {
                    continue;
                }

                s = StatCollector.translateToLocal(potion.getName());
                float lx = (float)(lpid & 0x03);
                GL11.glPushMatrix();
                GL11.glTranslatef(20F + lx * 30F, (float)mc.displayHeight - 80F - lx * 40F - (lpid >>> 2) * 50F, 0F);
                int widMes = mc.fontRenderer.getStringWidth(s) / 2;
                mc.fontRenderer.drawString(s, 64 - widMes, 19, getColorAmp(potion.isBadEffect() ? ColorInt_Alert : ColorInt_Normal, 0.8F));
                int lk = mc.renderEngine.getTexture("/gui/rshud_acvstatus.png");
                mc.renderEngine.bindTexture(lk);
                drawTexturedModalRect(0, 0, 0, potion.isBadEffect() ? 0 : 48, 128, 45);
                GL11.glPopMatrix();
            }

            if (itemstack != null && itemstack.hasTagCompound())
            {
                NBTTagList nbttaglist = itemstack.getEnchantmentTagList();

                if (nbttaglist != null)
                {
//                    for (short word0 = 0; word0 < 256; word0++) {
                    for (int li = 0; li < nbttaglist.tagCount(); li++)
                    {
                        short word0 = ((NBTTagCompound)nbttaglist.tagAt(li)).getShort("id");
                        short word1 = ((NBTTagCompound)nbttaglist.tagAt(li)).getShort("lvl");

//                    	short word1 = 1;
                        if (Enchantment.enchantmentsList[word0] != null)
                        {
                            s = Enchantment.enchantmentsList[word0].getTranslatedName(word1);
                            float lx = (float)(word0 & 0x07);
                            float ly = (float)(word0 >>> 3);
                            GL11.glPushMatrix();
                            GL11.glTranslatef((float)mc.displayWidth - 148F - ly * 16F, (float)mc.displayHeight - 80F - ly * 40F - lx * 50F, 0F);
                            int widMes = mc.fontRenderer.getStringWidth(s) / 2;
                            mc.fontRenderer.drawString(s, 64 - widMes, 19, getColorAmp(ColorInt_Normal, 0.8F));
                            int lk = mc.renderEngine.getTexture("/gui/rshud_acvstatus.png");
                            mc.renderEngine.bindTexture(lk);
                            drawTexturedModalRect(0, 0, 0, 48, 128, 45);
                            GL11.glPopMatrix();
                        }
                    }
                }
            }

            GL11.glPopMatrix();
        }

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        lastTime = mc.theWorld.getWorldTime();
    }

    private void drawXross(Tessellator tessellator, double ldx, double ldy, double r1, double r2, double wid)
    {
        r2 += r1;
        tessellator.startDrawing(GL11.GL_QUADS);
        //
        tessellator.addVertex(ldx + r2 - wid, ldy + r2 + wid, 0.0D);
        tessellator.addVertex(ldx + r2 + wid, ldy + r2 - wid, 0.0D);
        tessellator.addVertex(ldx + r1 + wid, ldy + r1 - wid, 0.0D);
        tessellator.addVertex(ldx + r1 - wid, ldy + r1 + wid, 0.0D);
        //
        tessellator.addVertex(ldx - r2 - wid, ldy + r2 - wid, 0.0D);
        tessellator.addVertex(ldx - r2 + wid, ldy + r2 + wid, 0.0D);
        tessellator.addVertex(ldx - r1 + wid, ldy + r1 + wid, 0.0D);
        tessellator.addVertex(ldx - r1 - wid, ldy + r1 - wid, 0.0D);
        //
        tessellator.addVertex(ldx - r2 + wid, ldy - r2 - wid, 0.0D);
        tessellator.addVertex(ldx - r2 - wid, ldy - r2 + wid, 0.0D);
        tessellator.addVertex(ldx - r1 - wid, ldy - r1 + wid, 0.0D);
        tessellator.addVertex(ldx - r1 + wid, ldy - r1 - wid, 0.0D);
        //
        tessellator.addVertex(ldx + r2 + wid, ldy - r2 + wid, 0.0D);
        tessellator.addVertex(ldx + r2 - wid, ldy - r2 - wid, 0.0D);
        tessellator.addVertex(ldx + r1 - wid, ldy - r1 - wid, 0.0D);
        tessellator.addVertex(ldx + r1 + wid, ldy - r1 + wid, 0.0D);
        tessellator.draw();
    }

    private void drawOutline(Tessellator tessellator, double top, double left, double bottom, double right, double length)
    {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        tessellator.startDrawing(GL11.GL_LINES);
        tessellator.addVertex(left, top, 0D);
        tessellator.addVertex(left + length, top, 0D);
        tessellator.addVertex(left, top, 0D);
        tessellator.addVertex(left, top + length, 0D);
        tessellator.addVertex(left, bottom, 0D);
        tessellator.addVertex(left + length, bottom, 0D);
        tessellator.addVertex(left, bottom, 0D);
        tessellator.addVertex(left, bottom - length, 0D);
        tessellator.addVertex(right, bottom, 0D);
        tessellator.addVertex(right - length, bottom, 0D);
        tessellator.addVertex(right, bottom, 0D);
        tessellator.addVertex(right, bottom - length, 0D);
        tessellator.addVertex(right, top, 0D);
        tessellator.addVertex(right - length, top, 0D);
        tessellator.addVertex(right, top, 0D);
        tessellator.addVertex(right, top + length, 0D);
        tessellator.draw();
    }

    private void drawMeter(Tessellator tessellator, double r1, double r2, float degStart, float degEnd, float range)
    {
        if (range <= 0.0F)
        {
            return;
        }

        float vec = (degEnd - degStart) * Math.min(1.0F, range);
        int li;
        float deg;
        double ldx;
        double ldy;
        tessellator.startDrawing(GL11.GL_QUAD_STRIP);

        if (vec > 0)
        {
            for (li = 0; li < vec; li += 2)
            {
                deg = (degStart + li) / (180F / (float)Math.PI);
                ldx = MathHelper.cos(deg);
                ldy = MathHelper.sin(deg);
                tessellator.addVertex(ldx * r2 + centerX, ldy * r2 + centerY, 0.0D);
                tessellator.addVertex(ldx * r1 + centerX, ldy * r1 + centerY, 0.0D);
            }

            deg = (degStart + vec) / (180F / (float)Math.PI);
            ldx = MathHelper.cos(deg);
            ldy = MathHelper.sin(deg);
            tessellator.addVertex(ldx * r2 + centerX, ldy * r2 + centerY, 0.0D);
            tessellator.addVertex(ldx * r1 + centerX, ldy * r1 + centerY, 0.0D);

            if (li < vec)
            {
            }
        }
        else
        {
            for (li = 0; li < -vec; li += 2)
            {
                deg = (degStart - li) / (180F / (float)Math.PI);
                ldx = MathHelper.cos(deg);
                ldy = MathHelper.sin(deg);
                tessellator.addVertex(ldx * r1 + centerX, ldy * r1 + centerY, 0.0D);
                tessellator.addVertex(ldx * r2 + centerX, ldy * r2 + centerY, 0.0D);
            }

            deg = (degStart + vec) / (180F / (float)Math.PI);
            ldx = MathHelper.cos(deg);
            ldy = MathHelper.sin(deg);
            tessellator.addVertex(ldx * r1 + centerX, ldy * r1 + centerY, 0.0D);
            tessellator.addVertex(ldx * r2 + centerX, ldy * r2 + centerY, 0.0D);

            if (li < vec)
            {
            }
        }

        tessellator.draw();
    }

    public void toggleRouteView(EntityPlayer entityplayer)
    {
        isRouteView = !isRouteView;

        if (entityplayer != null)
        {
            if (isRouteView)
            {
                entityplayer.addChatMessage("Route Visible.");

                if (entityRoute.routePos[0] == null)
                {
                    setStartRoute(entityplayer);
                }
            }
            else
            {
                entityplayer.addChatMessage("Route Hide.");
            }
        }
    }

    public void setStartRoute(EntityPlayer entityplayer)
    {
        System.out.println("start Route.");
        lastWayPoint = new double[]
        {
            entityplayer.posX,
            entityplayer.posY - entityplayer.yOffset + 0.7D,
            entityplayer.posZ
        };
        nowWayPoint = new double[]
        {
            entityplayer.posX,
            entityplayer.posY - entityplayer.yOffset + 0.7D,
            entityplayer.posZ
        };
        wayPoints.clear();
        wayPoints.add(lastWayPoint);

        if (entityRoute != null)
        {
            entityRoute.routePos[0] = lastWayPoint;
            entityRoute.routePos[1] = null;
            entityRoute.routePos[2] = null;
        }

        counterCheckWay = 0;
        entityplayer.addChatMessage("Route Reset.");
    }

    public boolean checkRoutePoint(World world, EntityPlayer entityplayer)
    {
        boolean flag = false;

        if (world == null || lastWayPoint == null)
        {
            return false;
        }

        Vec3 vec1 = entityplayer.getPosition(1.0F);
        vec1.yCoord -= entityplayer.yOffset - 0.7D;
        MovingObjectPosition mo = world.rayTraceBlocks_do_do(Vec3.createVectorHelper(lastWayPoint[0], lastWayPoint[1], lastWayPoint[2]), vec1, false, true);

        if (mo != null)
        {
            wayPoints.add(nowWayPoint);
            lastWayPoint = nowWayPoint;
            System.out.println(String.format("Add Route(%d).", wayPoints.size()));
            entityRoute.routePos[2] = entityRoute.routePos[1];
            entityRoute.routePos[1] = entityRoute.routePos[0];
            entityRoute.routePos[0] = lastWayPoint;
            flag = true;
        }

//		System.out.println("Ray Enable.");
        nowWayPoint = new double[]
        {
            entityplayer.posX,
            entityplayer.posY - entityplayer.yOffset + 0.7D,
            entityplayer.posZ
        };
        return flag;
    }

    public void checkReRoute(World world, EntityPlayer entityplayer)
    {
        if (wayPoints.size() <= 1)
        {
            return;
        }

        if (counterCheckWay < 0)
        {
            counterCheckWay = 0;
        }

        double[] ldd = wayPoints.get(counterCheckWay);

        if (ldd != lastWayPoint)
        {
            Vec3 vec1 = entityplayer.getPosition(1.0F);
            vec1.yCoord -= entityplayer.yOffset - 0.7D;
            MovingObjectPosition mo = world.rayTraceBlocks_do_do(Vec3.createVectorHelper(ldd[0], ldd[1], ldd[2]), vec1, false, true);

            if (mo == null)
            {
                while (wayPoints.size() > counterCheckWay + 1)
                {
                    wayPoints.remove(wayPoints.size() - 1);
                }

                entityRoute.routePos[0] = null;
                entityRoute.routePos[1] = null;
                entityRoute.routePos[2] = null;

                for (int li = 0; li < 3 && (wayPoints.size() >= li + 1); li++)
                {
                    entityRoute.routePos[li] = wayPoints.get(counterCheckWay - li);
                }

                lastWayPoint = entityRoute.routePos[0];
                System.out.println(String.format("reRoute %d Check points", wayPoints.size()));
            }
        }

        if (--counterCheckWay < 0)
        {
            counterCheckWay = wayPoints.size() - 1;
        }
    }

    private void drawRECON(Tessellator tessellator, Minecraft lmc, GRV_EntityRECON pEntityRECON)
    {
        if (entityRoute == null || !pEntityRECON.enable)
        {
            return;
        }

        FloatBuffer posMarker = BufferUtils.createFloatBuffer(3);
        GL11.glPushMatrix();
        GLU.gluProject((float)(pEntityRECON.posX - entityRoute.posX),
                (float)(pEntityRECON.posY - entityRoute.posY),
                (float)(pEntityRECON.posZ - entityRoute.posZ),
                GRV_RenderMARKER.matModel, GRV_RenderMARKER.matProjection, GRV_RenderMARKER.matViewport, posMarker);

        if (posMarker.get(2) >= 0.0F && posMarker.get(2) <= 1.0F)
        {
            int lcolor = ColorInt_Maker;

            if (pEntityRECON.getCountEnemy() > 0)
            {
                lcolor = ColorInt_Alert;
            }

            GL11.glLineWidth(1.5F);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glTranslatef(posMarker.get(0) / zoomx, (float)centerY * 2F - posMarker.get(1) / zoomy - 10F, 0.0F);
            setGLTextColor(lcolor, 1.0F);
            tessellator.startDrawing(GL11.GL_LINE_LOOP);
            tessellator.addVertex(0D, 0D, 0.0D);
            tessellator.addVertex(2D, -8D, 0.0D);
            tessellator.addVertex(-2D, -8D, 0.0D);
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glScalef(textSize, textSize, textSize);
            String lstr;
            float ly = -15F / textSize;
            lstr = String.format("OTHER x%02d", pEntityRECON.getCountOther());
            int lw = -lmc.fontRenderer.getStringWidth(lstr) / 2;
            lmc.fontRenderer.drawString(lstr, lw, (int)(ly - lmc.fontRenderer.FONT_HEIGHT * 1), lcolor);
            lstr = String.format("ENEMY x%02d", pEntityRECON.getCountEnemy());
            lmc.fontRenderer.drawString(lstr, lw, (int)(ly - lmc.fontRenderer.FONT_HEIGHT * 2), lcolor);
            lstr = String.format("RECON %3d", pEntityRECON.index + 1);
            lmc.fontRenderer.drawString(lstr, lw, (int)(ly - lmc.fontRenderer.FONT_HEIGHT * 3), lcolor);
        }
        else
        {
        }

        GL11.glPopMatrix();
    }

    private int getTextColor(float percent, float levelwarning, float levelalart)
    {
        if (levelalart >= percent)
        {
            return ColorInt_Alert;
        }
        else if (levelwarning >= percent)
        {
            return ColorInt_Warning;
        }

        return ColorInt_Normal;
    }

    private int getColorAmp(int col, float rate)
    {
        int a = col >> 24 & 0xff;
        a = (int)((float)a * rate) & 0xff;
        return col & 0x00ffffff | a << 24;
    }

    private void setGLTextColor(int textColor, float alphaRate)
    {
        float[] colf = getRGBA(textColor);
        GL11.glColor4f(colf[0],	colf[1], colf[2], colf[3] * alphaRate);
    }

    public static void addRECONSensing(Entity pentity)
    {
        if (!viewRECONEntitys.contains(pentity))
        {
            viewRECONEntitys.add(pentity);
        }
    }

    public static void clearRECONSensing()
    {
        if (!isRECONUpdate)
        {
            viewRECONEntitys.clear();
        }

        isRECONUpdate = true;
    }
}