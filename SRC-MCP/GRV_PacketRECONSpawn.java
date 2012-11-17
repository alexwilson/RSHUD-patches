package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class GRV_PacketRECONSpawn extends Packet23VehicleSpawn
{
    public GRV_PacketRECONSpawn(Entity par1Entity, int par2, int par3)
    {
        super(par1Entity, par2, par3);
    }

    @Override
    public void processPacket(NetHandler par1NetHandler)
    {
        if (par1NetHandler instanceof NetClientHandler)
        {
            Minecraft mc = ModLoader.getMinecraftInstance();
            WorldClient lworld = mc.theWorld;
            double lx = (double)this.xPosition / 32.0D;
            double ly = (double)this.yPosition / 32.0D;
            double lz = (double)this.zPosition / 32.0D;
            Entity le = (mc.thePlayer.entityId == throwerEntityId) ? mc.thePlayer : lworld.getEntityByID(throwerEntityId);

            if (le instanceof EntityLiving)
            {
                GRV_EntityRECON lentity = new GRV_EntityRECON(lworld, (EntityLiving)le);
                lentity.serverPosX = this.xPosition;
                lentity.serverPosY = this.yPosition;
                lentity.serverPosZ = this.zPosition;
                lentity.entityId = this.entityId;
                lworld.addEntityToWorld(this.entityId, lentity);
                lentity.setVelocity((double)this.speedX / 8000.0D, (double)this.speedY / 8000.0D, (double)this.speedZ / 8000.0D);
            }
        }
    }
}
