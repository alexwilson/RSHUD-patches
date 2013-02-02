package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class GRH_ContainerRSHUD extends ContainerCreative
{
    public List<List<ItemStack>> weaponList;
    public int weaponOffset;

    public GRH_ContainerRSHUD(EntityPlayer entityplayer)
    {
        super(entityplayer);
        inventorySlots.clear();

        for (int l2 = 0; l2 < 5; l2++)
        {
            for (int j3 = 0; j3 < 8; j3++)
            {
                addSlotToContainer(new Slot(GRH_GuiContainerRSHUD.getInventory1(), j3 + l2 * 8, 8 + j3 * 18, 18 + l2 * 18));
            }
        }

        for (int l2 = 0; l2 < 5; l2++)
        {
            addSlotToContainer(new Slot(GRH_GuiContainerRSHUD.getInventory2(), l2 * 7, 8 , 121 + l2 * 18));

            for (int j3 = 1; j3 < 7; j3++)
            {
                addSlotToContainer(new Slot(GRH_GuiContainerRSHUD.getInventory2(), j3 + l2 * 7, 26 + j3 * 18, 121 + l2 * 18));
            }
        }

        weaponList = new ArrayList<List<ItemStack>>();

        for (Entry<Integer, List<Integer>> me: GRH_GuiRSHUDConfigure.projectorList.entrySet())
        {
            if (Item.itemsList[me.getKey()] == null)
            {
                continue;
            }

            int ls = me.getValue().size();

            for (; ls > 0;)
            {
                List<ItemStack> ti = new ArrayList<ItemStack>();
                ti.add(new ItemStack(Item.itemsList[me.getKey()]));

                for (int i = 0; i < 6 && ls-- > 0; i++)
                {
                    if (Item.itemsList[me.getValue().get(i)] == null)
                    {
                        continue;
                    }

                    ti.add(new ItemStack(Item.itemsList[me.getValue().get(i)]));
                }

                weaponList.add(ti);
            }
        }

        initAllSelections();
        scrollTo(0.0F);
        setWeaponlist(0.0F);
    }

    private void initAllSelections()
    {
        this.itemList.clear();
        Item[] var2 = Item.itemsList;
        int var3 = var2.length;

        for (int var4 = 0; var4 < var3; ++var4)
        {
            Item var5 = var2[var4];

            if (var5 != null && var5.getCreativeTab() != null)
            {
                var5.getSubItems(var5.itemID, (CreativeTabs)null, this.itemList);
            }
        }
    }

    @Override public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override public void scrollTo(float f)
    {
        int i = (itemList.size() / 8 - 5) + 1;
        int j = (int)((double)(f * (float)i) + 0.5D);

        if (j < 0)
        {
            j = 0;
        }

        for (int k = 0; k < 5; k++)
        {
            for (int l = 0; l < 8; l++)
            {
                int i1 = l + (k + j) * 8;

                if (i1 >= 0 && i1 < itemList.size())
                {
                    GRH_GuiContainerRSHUD.getInventory1().setInventorySlotContents(l + k * 8, (ItemStack)itemList.get(i1));
                }
                else
                {
                    GRH_GuiContainerRSHUD.getInventory1().setInventorySlotContents(l + k * 8, null);
                }
            }
        }
    }

//	@Override
    public ItemStack slotClick(int i, int j, boolean flag,
            EntityPlayer entityplayer)
    {
        if (i >= 40)
        {
            int lk = (i - 40) / 7 + weaponOffset;

            for (; weaponList.size() <= lk;)
            {
                weaponList.add(new ArrayList<ItemStack>());
            }

            List list1 = weaponList.get(lk);

            for (; list1.size() < 7;)
            {
                list1.add(null);
            }

            list1.set((i - 40) % 7, entityplayer.inventory.getItemStack());
        }

        if (i == -999)
        {
            entityplayer.inventory.setItemStack(null);
        }

        int iflag = flag ? 1 : 0;
        ItemStack is = super.slotClick(i, j, iflag, entityplayer);
        return is;
    }

    public void setWeaponlist(float f)
    {
        //
        int i = (weaponList.size() - 5) + 1;
        weaponOffset = (int)((double)(f * (float)i) + 0.5D);

        if (weaponOffset < 0)
        {
            weaponOffset = 0;
        }

        for (int k = 0; k < 5; k++)
        {
            int i1 = k + weaponOffset;

            for (int l = 0; l < 7; l++)
            {
                if (i1 >= 0 && i1 < weaponList.size() && l < weaponList.get(i1).size())
                {
                    GRH_GuiContainerRSHUD.getInventory2().setInventorySlotContents(k * 7 + l, weaponList.get(i1).get(l));
                }
                else
                {
                    GRH_GuiContainerRSHUD.getInventory2().setInventorySlotContents(k * 7 + l, null);
                }
            }
        }
    }
}
