package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GRH_GuiContainerRSHUD extends GuiContainer {
	
	protected float scrolleWeaponset;
	protected float scrolleContainer;
    private static InventoryBasic inventory1 = new InventoryBasic("tmpsel", 40);
    private static InventoryBasic inventory2 = new InventoryBasic("tmpwep", 35);
    private int lastX;
    private int lastY;
    private boolean ismousePress;
    private int isScrolled;
    public GRH_GuiRSHUDConfigure owner;
    
	

	public GRH_GuiContainerRSHUD(EntityPlayer entityplayer, GRH_GuiRSHUDConfigure guirshudconfigure) {
		super(new GRH_ContainerRSHUD(entityplayer));
        ySize = 216;
        owner = guirshudconfigure;
//        entityplayer.craftingInventory = this.inventorySlots;
//        entityplayer.craftingInventory.windowId = 120;
	}

	@Override
    protected void keyTyped(char c, int i) {
        if(i == 1 || i == mod_GRH_RSHUD.guiKey.keyCode) {
        	mc.displayGuiScreen(owner);
        }
    }
	
	@Override
	public void onGuiClosed() {
		// 設定値のデコード
		GRH_GuiRSHUDConfigure.projectorList.clear();
		for (int i = 0; i < ((GRH_ContainerRSHUD)inventorySlots).weaponList.size(); i++) {
			List<ItemStack> list1 = ((GRH_ContainerRSHUD)inventorySlots).weaponList.get(i);
			if (list1.get(0) != null) {
				List list2 = new ArrayList<Integer>();
				for (int j = 1; j < list1.size(); j++) {
					if (list1.get(j) != null) {
						list2.add(list1.get(j).getItem().shiftedIndex);
					}
				}
				if (list2.isEmpty()) continue;
				if (GRH_GuiRSHUDConfigure.projectorList.containsKey(list1.get(0).getItem().shiftedIndex)) {
					// キーがある
					GRH_GuiRSHUDConfigure.projectorList.get(list1.get(0).getItem().shiftedIndex).addAll(list2);
				} else {
					GRH_GuiRSHUDConfigure.projectorList.put(list1.get(0).getItem().shiftedIndex, list2);
				}
			}
		}
		StringBuilder sb = new StringBuilder();
		for (Entry<Integer, List<Integer>> me : GRH_GuiRSHUDConfigure.projectorList.entrySet()) {
			sb.append(me.getKey().toString()).append(":").append(me.getValue().get(0).toString());
			for (int i = 1; i < me.getValue().size(); i++) {
				sb.append(",").append(me.getValue().get(i).toString());
			}
			sb.append(";");
		}
		mod_GRH_RSHUD.projectionArms = sb.toString();
		
		
		super.onGuiClosed();
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	
	@Override
	protected void handleMouseClick(Slot slot, int i, int j, boolean flag) {
        if(slot != null)
        {
            if(slot.inventory == inventory1)
            {
                InventoryPlayer inventoryplayer = mc.thePlayer.inventory;
                ItemStack itemstack1 = inventoryplayer.getItemStack();
                ItemStack itemstack4 = slot.getStack();
                // 二つは同じID
                if(itemstack1 != null && itemstack4 != null && itemstack1.itemID == itemstack4.itemID)
                {
                    if(j == 0)
                    {
                        if(flag)
                        {
                            itemstack1.stackSize = itemstack1.getMaxStackSize();
                        } else
                        if(itemstack1.stackSize < itemstack1.getMaxStackSize())
                        {
                            itemstack1.stackSize++;
                        }
                    } else
                    if(itemstack1.stackSize <= 1)
                    {
                        inventoryplayer.setItemStack(null);
                    } else
                    {
                        itemstack1.stackSize--;
                    }
                } else
                if(itemstack1 != null)
                {
                    inventoryplayer.setItemStack(null);
                } else
                if(itemstack4 == null)
                {
                    inventoryplayer.setItemStack(null);
                } else
                if(itemstack1 == null || itemstack1.itemID != itemstack4.itemID)
                {
                    inventoryplayer.setItemStack(ItemStack.copyItemStack(itemstack4));
                    ItemStack itemstack2 = inventoryplayer.getItemStack();
                    if(flag)
                    {
                        itemstack2.stackSize = itemstack2.getMaxStackSize();
                    }
                }
            } else
            {
                inventorySlots.slotClick(slot.slotNumber, j, flag, mc.thePlayer);
                ItemStack itemstack = inventorySlots.getSlot(slot.slotNumber).getStack();
                mc.playerController.sendSlotPacket(itemstack, (slot.slotNumber - inventorySlots.inventorySlots.size()) + 9 + 36);
            }
        } else
        {
        	// Slot以外なら捨てる
            InventoryPlayer inventoryplayer1 = mc.thePlayer.inventory;
            inventoryplayer1.setItemStack(null);
        }
    }

	@Override
    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Item selection", 8, 6, 0x404040);
        fontRenderer.drawString("Weapon and Ammo", 8, 110, 0x404040);
    }

	@Override
    public void handleMouseInput()
    {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if(i != 0) {
        	if (lastY < height / 2) {
                int j = (((GRH_ContainerRSHUD)inventorySlots).itemList.size() / 8 - 5) + 1;
                if(i > 0)
                {
                    i = 1;
                }
                if(i < 0)
                {
                    i = -1;
                }
                scrolleContainer -= (double)i / (double)j;
                if(scrolleContainer < 0.0F)
                {
                	scrolleContainer = 0.0F;
                }
                if(scrolleContainer > 1.0F)
                {
                	scrolleContainer = 1.0F;
                }
                ((GRH_ContainerRSHUD)inventorySlots).scrollTo(scrolleContainer);
        	} else {
                int j = (((GRH_ContainerRSHUD)inventorySlots).weaponList.size() - 5) + 1;
                if(i > 0)
                {
                    i = 1;
                }
                if(i < 0)
                {
                    i = -1;
                }
                scrolleWeaponset -= (double)i / (double)j;
                if(scrolleWeaponset < 0.0F)
                {
                	scrolleWeaponset = 0.0F;
                }
                if(scrolleWeaponset > 1.0F)
                {
                	scrolleWeaponset = 1.0F;
                }
                ((GRH_ContainerRSHUD)inventorySlots).setWeaponlist(scrolleWeaponset);
        	}
        }
    }

	@Override
    public void drawScreen(int i, int j, float f)
    {
    	lastX = i;
    	lastY = j;
        boolean flag = Mouse.isButtonDown(0);
        int k = guiLeft;
        int l = guiTop;
        int i1 = k + 155;
        int j1 = l + 17;
        int k1 = i1 + 14;
        int l1 = j1 + 90;
        if(!flag)
        {
            isScrolled = 0;
        }
        if(!ismousePress && flag && i >= i1 && j >= j1 && i < k1 && j < l1)
        {
            isScrolled = 1;
        }
        if(isScrolled == 1)
        {
            scrolleContainer = (float)(j - (j1 + 8)) / ((float)(l1 - j1) - 16F);
            if(scrolleContainer < 0.0F)
            {
                scrolleContainer = 0.0F;
            }
            if(scrolleContainer > 1.0F)
            {
                scrolleContainer = 1.0F;
            }
            ((GRH_ContainerRSHUD)inventorySlots).scrollTo(scrolleContainer);
        }
        j1 = l + 120;
        l1 = j1 + 90;
        if(!ismousePress && flag && i >= i1 && j >= j1 && i < k1 && j < l1)
        {
            isScrolled = 2;
        }
        if(isScrolled == 2)
        {
        	scrolleWeaponset= (float)(j - (j1 + 8)) / ((float)(l1 - j1) - 16F);
            if(scrolleWeaponset < 0.0F)
            {
            	scrolleWeaponset = 0.0F;
            }
            if(scrolleWeaponset > 1.0F)
            {
            	scrolleWeaponset = 1.0F;
            }
            ((GRH_ContainerRSHUD)inventorySlots).setWeaponlist(scrolleWeaponset);
        }
        ismousePress = flag;
        super.drawScreen(i, j, f);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(2896 /*GL_LIGHTING*/);
    }

	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int k = mc.renderEngine.getTexture("/gui/rshudcontainer.png");
        mc.renderEngine.bindTexture(k);
        int l = guiLeft;
        int i1 = guiTop;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
        
        int j1 = l + 155;
        int k1 = i1 + 17;
        int l1 = k1 + 88 + 2;
//        scrolleWeaponset = 1.0F;
//        scrolleContainer = 0.5F;
        drawTexturedModalRect(l + 154, i1 + 17 + (int)((float)(l1 - k1 - 17) * scrolleContainer), 176, 0, 16, 16);
        drawTexturedModalRect(l + 154, i1 + 120 + (int)((float)(l1 - k1 - 17) * scrolleWeaponset), 176, 0, 16, 16);
	}

    public static InventoryBasic getInventory1()
    {
        return inventory1;
    }

    public static InventoryBasic getInventory2()
    {
        return inventory2;
    }

}
