package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class GRV_ItemRECON extends ItemSnowball
{
    public GRV_ItemRECON(int par1)
    {
        super(par1);
        setMaxStackSize(64);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (!par3EntityPlayer.capabilities.isCreativeMode)
        {
            par1ItemStack.stackSize--;
        }

        par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!par2World.isRemote)
        {
            par2World.spawnEntityInWorld(new GRV_EntityRECON(par2World, par3EntityPlayer));
        }

        return par1ItemStack;
    }

    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        return 0xff888888;
    }

    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return EnumRarity.rare;
    }
}