package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

import javax.transaction.xa.Xid;

import net.minecraft.client.Minecraft;

public class GRV_EntityRECON extends EntityThrowable
{
    private int xTile;
    private int yTile;
    private int zTile;
    private int inTile;
    //
    public boolean enable;
    public int batteryCount;
    //
    public Entity targetEntity;
    public int index;
    private int countEnemy;
    private int countOther;
    private Minecraft mc;
    protected int field_85053_h;

    public GRV_EntityRECON(World par1World)
    {
        super(par1World);
    }

    public GRV_EntityRECON(World par1World, EntityLiving par2EntityLiving)
    {
        super(par1World, par2EntityLiving);
    }

    public GRV_EntityRECON(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        targetEntity = null;
        enable = false;
        batteryCount = 600;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound)
    {
        super.writeEntityToNBT(par1nbtTagCompound);
        par1nbtTagCompound.setBoolean("Enable", enable);
        par1nbtTagCompound.setInteger("Battery", batteryCount);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound)
    {
        super.readEntityFromNBT(par1nbtTagCompound);
        enable = par1nbtTagCompound.getBoolean("Enable");
        batteryCount = par1nbtTagCompound.getInteger("Battery");
    }

    @Override
    protected void onImpact(MovingObjectPosition movingobjectposition)
    {
/*    	if (!worldObj.multiplayerWorld) {
    		setDead();
    	}
*/    	
        if (movingobjectposition.entityHit != null)
        {
            movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, func_85052_h()), 6);
//          targetEntity = movingobjectposition.entityHit;
            for (int i = 0; i < 8; ++i) {
	            worldObj.spawnParticle("explosion", this.posX, this.posY, this.posZ + 0.5D, 0.0D, 0.0D, 0.0D);
	        }
            setDead();
        }
        else
        {
            xTile = movingobjectposition.blockX;
            yTile = movingobjectposition.blockY;
            zTile = movingobjectposition.blockZ;
            inTile = worldObj.getBlockId(xTile, yTile, zTile);
            motionX = (float)(movingobjectposition.hitVec.xCoord - posX);
            motionY = (float)(movingobjectposition.hitVec.yCoord - posY);
            motionZ = (float)(movingobjectposition.hitVec.zCoord - posZ);
            float f2 = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
            posX -= (motionX / (double)f2) * 0.05000000074505806D;
            posY -= (motionY / (double)f2) * 0.05000000074505806D;
            posZ -= (motionZ / (double)f2) * 0.05000000074505806D;
            posX += motionX;
            posY += motionY;
            posZ += motionZ;
            inGround = true;
            setPrivates();
        }

        if (func_85052_h() instanceof EntityPlayer)
        {
            int lwakeCount = ((EntityPlayer)func_85052_h()).experienceLevel / 10;

            List llist = worldObj.getLoadedEntityList();

            for (int li = 0; li < llist.size(); li++)
            {
                Entity lentity = (Entity)llist.get(li);

                if (lentity instanceof GRV_EntityRECON)
                {
                    GRV_EntityRECON lrecon = (GRV_EntityRECON)lentity;
                    lrecon.index++;

                    if ((!((EntityPlayer)func_85052_h()).capabilities.isCreativeMode) && lrecon.enable && lrecon.index > lwakeCount)
                    {
                        worldObj.spawnParticle("smoke", this.posX + 0.5D, this.posY + 1D, this.posZ + 0.5D, 0.0D, 0.0D, 0.0D);
                        lrecon.setDead();
                    }
                }
            }

            enable = true;
            index = 0;
        }
    }

    @Override
    public void onUpdate()
    {	
        getPrivates();

        if (targetEntity != null)
        {
            xTile = (int)(posX - 0.5D);
            yTile = (int)posY;
            zTile = (int)(posZ - 0.5D);
            inTile = worldObj.getBlockId(xTile, yTile, zTile);
        }

        setPrivates();
        super.onUpdate();

        if (enable)
        {
            if (batteryCount-- < 0)
            {
                worldObj.spawnParticle("smoke", this.posX + 0.5D, this.posY + 1D, this.posZ + 0.5D, 0.0D, 0.0D, 0.0D);
                setDead();
            }

            if (worldObj.isRemote)
            {
                countOther = 0;
                countEnemy = 0;
                List<Entity> llist1 = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(8D, 8D, 8D));

                if (llist1 != null)
                {
                    for (Entity entity : llist1)
                    {
                        if (!(entity instanceof EntityLiving))
                        {
                            continue;
                        }

                        if (entity instanceof EntityPlayer)
                        {
                            continue;
                        }

                        GRV_GuiRSHUD_ACV.addRECONSensing(entity);

                        if (entity instanceof IMob || entity instanceof IRangedAttackMob || entity instanceof EntityEnderman || entity instanceof EntityBat || (entity instanceof EntityWolf && ((EntityWolf)entity).isAngry()))
                        {
                            countEnemy++;
                        }
                        else
                        {
                            countOther++;
                        }
                    }
                }
            }
        }

        if (targetEntity != null)
        {
            if (targetEntity.isDead)
            {
                targetEntity = null;
            }
            else
            {
                posX = targetEntity.posX;
                posY = targetEntity.posY + targetEntity.getEyeHeight();
                posZ = targetEntity.posZ;
            }
        }
    }

    public boolean getPrivates()
    {
        try
        {
            xTile = (Integer)ModLoader.getPrivateValue(EntityThrowable.class, this, 0);
            yTile = (Integer)ModLoader.getPrivateValue(EntityThrowable.class, this, 1);
            zTile = (Integer)ModLoader.getPrivateValue(EntityThrowable.class, this, 2);
            inTile = (Integer)ModLoader.getPrivateValue(EntityThrowable.class, this, 3);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setPrivates()
    {
        try
        {
            ModLoader.setPrivateValue(EntityThrowable.class, this, 0, xTile);
            ModLoader.setPrivateValue(EntityThrowable.class, this, 1, yTile);
            ModLoader.setPrivateValue(EntityThrowable.class, this, 2, zTile);
            ModLoader.setPrivateValue(EntityThrowable.class, this, 3, inTile);
            ModLoader.setPrivateValue(EntityThrowable.class, this, 7, null);
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public int getCountOther()
    {
        return countOther > 99 ? 99 : countOther;
    }

    public int getCountEnemy()
    {
        return countEnemy > 99 ? 99 : countEnemy;
    }
}
