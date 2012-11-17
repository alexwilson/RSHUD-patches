package net.minecraft.src;

import java.nio.FloatBuffer;

import net.minecraft.client.Minecraft;

import org.lwjgl.BufferUtils;

public class GRV_EntityMARKER extends Entity {

	public double[][] routePos = new double[3][];
	public FloatBuffer[] markerPos = new FloatBuffer[3];
	public FloatBuffer playerPos = BufferUtils.createFloatBuffer(3);
	public EntityPlayer playerEntity;
	
	
	public GRV_EntityMARKER(World world) {
		super(world);
		setSize(0.1F, 0.1F);		
	}

	@Override
	protected void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
	}
	
	@Override
	public void onUpdate() {
		Minecraft mc = ModLoader.getMinecraftInstance();
		if (worldObj != mc.theWorld) {
			setDead();
		}
		playerEntity = mc.thePlayer;
		setLocationAndAngles(playerEntity.posX, playerEntity.posY, playerEntity.posZ,
				playerEntity.rotationYaw, playerEntity.rotationPitch);
//		super.onUpdate();
		if (!isDead) {
			if (mod_GRV_RSHUD_ACV.rshud.entityRoute != this || mc.theWorld != worldObj) {
				setDead();
			}
			else if (mod_GRV_RSHUD_ACV.rshud.entityRoute == null) {
				mod_GRV_RSHUD_ACV.rshud.entityRoute = this;
			}
		}
		
		GRV_GuiRSHUD_ACV.clearRECONSensing();
	}
	
}
