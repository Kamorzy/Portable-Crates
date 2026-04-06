package kamorzy.portablecrates.screen;

import kamorzy.portablecrates.registry.ModBlocks;
import kamorzy.portablecrates.registry.ModMenuTypes;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ShulkerBoxBlock;

public class CrateMenu extends AbstractContainerMenu {
    public static final int CRATE_SIZE = 9;

    private static final int CRATE_SLOT_START = 0;
    private static final int PLAYER_INV_START = CRATE_SLOT_START + CRATE_SIZE;   // 9
    private static final int HOTBAR_START = PLAYER_INV_START + 27;               // 36
    private static final int HOTBAR_END = HOTBAR_START + 9;                      // 45

    private final Container crateInventory;

    public CrateMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(CRATE_SIZE));
    }

    public CrateMenu(int containerId, Inventory playerInventory, Container crateInventory) {
        super(ModMenuTypes.CRATE_MENU, containerId);
        checkContainerSize(crateInventory, CRATE_SIZE);
        this.crateInventory = crateInventory;
        this.crateInventory.startOpen(playerInventory.player);

        // Crate inventory: 3x3
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int slotIndex = col + row * 3;
                int x = 62 + col * 18;
                int y = 17 + row * 18;

                this.addSlot(new Slot(crateInventory, slotIndex, x, y) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        if (stack.getItem() instanceof BlockItem blockItem
                                && blockItem.getBlock() instanceof ShulkerBoxBlock) {
                            return false;
                        }

                        return !stack.is(ModBlocks.SPRUCE_CRATE.asItem());
                    }
                });
            }
        }

        // Player inventory: 3 rows
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(
                        playerInventory,
                        col + row * 9 + 9,
                        8 + col * 18,
                        84 + row * 18
                ));
            }
        }

        // Hotbar
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    public Container getCrateInventory() {
        return this.crateInventory;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.crateInventory.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack original = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            original = stackInSlot.copy();

            if (slotIndex < CRATE_SIZE) {
                if (!this.moveItemStackTo(stackInSlot, PLAYER_INV_START, HOTBAR_END, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(stackInSlot, CRATE_SLOT_START, CRATE_SIZE, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stackInSlot.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stackInSlot.getCount() == original.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stackInSlot);
        }

        return original;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.crateInventory.stopOpen(player);
    }
}