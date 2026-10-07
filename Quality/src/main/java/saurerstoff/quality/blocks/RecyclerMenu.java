package saurerstoff.quality.blocks;

import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import saurerstoff.quality.ModMenuTypes;

public class RecyclerMenu extends AbstractContainerMenu{
    private final Container container;
    private final ContainerData data;

    //Client-side constructor
    public RecyclerMenu(final int containerId, final Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(RecyclerBlockEntity.CONTAINER_SIZE), new SimpleContainerData(RecyclerBlockEntity.DATA_COUNT));
    }

    //Server-side constructor
    public RecyclerMenu(final int containerId, final Inventory inventory, final Container container, final ContainerData containerData){
        super(ModMenuTypes.RECYCLER, containerId);
        checkContainerSize(container, RecyclerBlockEntity.CONTAINER_SIZE);
        this.container = container;
        this.data = containerData;

        container.startOpen(inventory.player);

        int paddingX = 16;
        int paddingY = 16;
        int slotSpacing = 18;
        int spacing = 8;

        //input slot
        this.addSlot(new Slot(container, 0, paddingX, paddingY));

        //output slots
        //row 1
        this.addSlot(new Slot(container, 1, paddingX, paddingY + spacing + 1 * slotSpacing));
        this.addSlot(new Slot(container, 2, paddingX + 1 * slotSpacing, paddingY + spacing + 1 * slotSpacing));
        this.addSlot(new Slot(container, 3, paddingX + 2 * slotSpacing, paddingY + spacing + 1 * slotSpacing));
        this.addSlot(new Slot(container, 4, paddingX + 3 * slotSpacing, paddingY + spacing + 1 * slotSpacing));
        this.addSlot(new Slot(container, 5, paddingX + 4 * slotSpacing, paddingY + spacing + 1 * slotSpacing));
        //row 2
        this.addSlot(new Slot(container, 6, paddingX, paddingY + spacing + 2 * slotSpacing));
        this.addSlot(new Slot(container, 7, paddingX + 1 * slotSpacing, paddingY + spacing + 2 * slotSpacing));
        this.addSlot(new Slot(container, 8, paddingX + 2 * slotSpacing, paddingY + spacing + 2 * slotSpacing));
        this.addSlot(new Slot(container, 9, paddingX + 3 * slotSpacing, paddingY + spacing + 2 * slotSpacing));
        this.addSlot(new Slot(container, 10, paddingX + 4 * slotSpacing, paddingY + spacing + 2 * slotSpacing));

        this.addStandardInventorySlots(inventory, 8, 92);
        this.addDataSlots(containerData);
    }

    private void addStandardInventorySlots(Inventory playerInventory, int leftX, int topY) {
    // Add the standard 3x9 player inventory
    for (int row = 0; row < 3; ++row) {
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col + row * 9 + 9, leftX + col * 18, topY + row * 18));
        }
    }

    // Add the 1x9 player hotbar
    for (int col = 0; col < 9; ++col) {
        this.addSlot(new Slot(playerInventory, col, leftX + col * 18, topY + 58));
    }
}

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        Slot slot = this.slots.get(slotIndex);

		if (!slot.hasItem()) {
			return ItemStack.EMPTY;
		}

		ItemStack stack = slot.getItem();
		ItemStack clicked = stack.copy();

		if (slotIndex < container.getContainerSize()) {
			if (!this.moveItemStackTo(stack, container.getContainerSize(), this.slots.size(), true)) {
				return ItemStack.EMPTY;
			}
		} else if (!this.moveItemStackTo(stack, 0, container.getContainerSize(), false)) {
			return ItemStack.EMPTY;
		}

		if (stack.isEmpty()) {
			slot.setByPlayer(ItemStack.EMPTY);
		} else {
			slot.setChanged();
		}

		return clicked;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    public float getProgress(){
        int recycleProgress = this.data.get(0);
		int recycleTotalTime = this.data.get(1);
		return 
            recycleTotalTime != 0 
            && recycleProgress != 0 
                ? Mth.clamp((float)recycleProgress / recycleTotalTime, 0.0F, 1.0F) 
                : 0.0F;
    }
}
