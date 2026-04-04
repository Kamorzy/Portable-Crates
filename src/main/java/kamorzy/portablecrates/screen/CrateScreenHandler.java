package kamorzy.portablecrates.screen;

import kamorzy.portablecrates.PortableCrates;
//import kamorzy.portablecrates.block.SealedCrateBlock;
import kamorzy.portablecrates.registry.ModScreens;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;

public class CrateScreenHandler extends AbstractContainerMenu {
    private static final int CONTAINER_SIZE = 9;
    private final Container container;

    public CrateScreenHandler(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(CONTAINER_SIZE));
    }

    public CrateScreenHandler(int syncId, Inventory playerInventory, Container container) {
        super(ModScreens.CRATE_SCREEN_HANDLER, syncId);
        checkContainerSize(container, CONTAINER_SIZE);
        this.container = container;
        container.startOpen(playerInventory.player);

        int row;
        int column;

        // Crate inventory: 3x3
        for (row = 0; row < 3; ++row) {
            for (column = 0; column < 3; ++column) {
                this.addSlot(new Slot(container, column + row * 3, 62 + column * 18, 17 + row * 18) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        if (stack.getItem() instanceof BlockItem blockItem) {
                            Block block = blockItem.getBlock();
                            return !(block instanceof ShulkerBoxBlock);
                            //return !(block instanceof ShulkerBoxBlock) && !(block instanceof SealedCrateBlock);
                        }

                        return true;
                    }
                });
            }
        }

        // Player inventory
        for (row = 0; row < 3; ++row) {
            for (column = 0; column < 9; ++column) {
                this.addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }

        // Hotbar
        for (column = 0; column < 9; ++column) {
            this.addSlot(new Slot(playerInventory, column, 8 + column * 18, 142));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    public Container getInventory() {
        return this.container;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            result = stackInSlot.copy();

            if (slotIndex < CONTAINER_SIZE) {
                if (!this.moveItemStackTo(stackInSlot, CONTAINER_SIZE, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(stackInSlot, 0, CONTAINER_SIZE, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stackInSlot.getCount() == result.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stackInSlot);
        }

        return result;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }
}