package kamorzy.portablecrates.blockentity;

import kamorzy.portablecrates.block.CrateBlock;
import kamorzy.portablecrates.registry.ModBlockEntities;
import kamorzy.portablecrates.screen.CrateScreenHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

import static net.minecraft.world.inventory.AbstractFurnaceMenu.SLOT_COUNT;

public class CrateBlockEntity extends RandomizableContainerBlockEntity{
    public static final int SLOT_COUNT = 9;
    private NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    public CrateBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRATE_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected @NonNull Component getDefaultName() {
        return Component.translatable("container.portablecrates.crate");
    }

    @Override
    protected @NonNull NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(@NonNull NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (stack.getItem() instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            return !(block instanceof ShulkerBoxBlock);
        }
        return true;
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new CrateScreenHandler(syncId, playerInventory, this);
    }
}
