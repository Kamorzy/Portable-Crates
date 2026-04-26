package kamorzy.portablecrates.blockentity;

import java.util.stream.IntStream;

import kamorzy.portablecrates.block.BaseCrateBlock;
import kamorzy.portablecrates.block.SealedCrateBlock;
import kamorzy.portablecrates.component.CrateContentsComponent;
import kamorzy.portablecrates.registry.ModBlockEntities;
import kamorzy.portablecrates.registry.ModDataComponents;
import kamorzy.portablecrates.screen.CrateMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class CrateBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {

    // PROPERTIES

    public static final int SLOT_COUNT = 9;
    private static final int[] SLOTS = IntStream.range(0, SLOT_COUNT).toArray();
    private static final int[] NO_SLOTS = new int[0];

    private static final Component DEFAULT_NAME =
            Component.translatable("container.portablecrates.crate");

    private NonNullList<ItemStack> itemStacks =
            NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    // CONSTRUCTOR

    public CrateBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRATE_BLOCK_ENTITY, pos, state);
    }

    // GET-SET PROPERTIES

    private boolean isSealedCrate() {
        return this.getBlockState().getBlock() instanceof SealedCrateBlock;
    }

    @Override
    protected @NonNull Component getDefaultName() {
        return DEFAULT_NAME;
    }

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    protected void setItems(@NonNull NonNullList<ItemStack> items) {
        this.itemStacks = items;
    }

    @Override
    protected @NonNull NonNullList<ItemStack> getItems() {
        return this.itemStacks;
    }

    // CHECKS FOR INPUTS

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return this.canAcceptStack(stack);
    }

    private boolean canAcceptStack(ItemStack stack) {
        Block block = Block.byItem(stack.getItem());
        return !(block instanceof ShulkerBoxBlock)
                && !(block instanceof BaseCrateBlock);
    }

    // VARIABLE HOPPER INTERACTION LOGIC

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return this.isSealedCrate() ? NO_SLOTS : SLOTS;

    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction direction) {
        return this.canAcceptStack(stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return true;
    }

    // UPDATE REDSTONE OUTPUTS WHEN INVENTORY CHANGES

    @Override
    public void setChanged() {
        super.setChanged();

        if (this.level != null && !this.level.isClientSide()) {
            this.level.updateNeighbourForOutputSignal(
                    this.worldPosition,
                    this.getBlockState().getBlock()
            );

            this.level.updateNeighborsAt(
                    this.worldPosition,
                    this.getBlockState().getBlock()
            );
        }
    }

    // CALL/SAVE INVENTORY AND OPEN MENUS

    @Override
    protected @NonNull AbstractContainerMenu createMenu(int syncId, @NonNull Inventory playerInventory) {
        return new CrateMenu(syncId, playerInventory, this);
    }

    @Override
    protected void loadAdditional(@NonNull ValueInput input) {
        super.loadAdditional(input);

        this.itemStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);

        if (!this.tryLoadLootTable(input)) {
            ContainerHelper.loadAllItems(input, this.itemStacks);
        }
    }

    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        if (!this.trySaveLootTable(output)) {
            ContainerHelper.saveAllItems(output, this.itemStacks);
        }

        super.saveAdditional(output);
    }

    @Override
    protected void applyImplicitComponents(@NonNull DataComponentGetter components) {
        super.applyImplicitComponents(components);

        CrateContentsComponent crateContents = components.get(ModDataComponents.CRATE_CONTENTS);

        if (crateContents != null) {
            this.lootTable = null;
            this.lootTableSeed = 0L;
            this.itemStacks = crateContents.toNonNullList();
            this.setChanged();
        }
    }

    @Override
    public @NonNull CompoundTag getUpdateTag(HolderLookup.@NonNull Provider registryLookup) {
        return this.saveWithoutMetadata(registryLookup);
    }

    @Override
    public void preRemoveSideEffects(@NonNull BlockPos pos, @NonNull BlockState state) {
    }

    public @NonNull NonNullList<ItemStack> copyStoredItems() {
        this.unpackLootTable(null);

        NonNullList<ItemStack> copiedItems = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);

        for (int i = 0; i < this.getContainerSize(); i++) {
            copiedItems.set(i, this.getItem(i).copy());
        }

        return copiedItems;
    }

    public void loadStoredItems(@NonNull NonNullList<ItemStack> newItems) {
        NonNullList<ItemStack> copiedItems = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);

        for (int i = 0; i < this.getContainerSize(); i++) {
            if (i < newItems.size()) {
                copiedItems.set(i, newItems.get(i).copy());
            }
        }

        this.itemStacks = copiedItems;
        this.lootTable = null;
        this.lootTableSeed = 0L;
        this.setChanged();
    }
}