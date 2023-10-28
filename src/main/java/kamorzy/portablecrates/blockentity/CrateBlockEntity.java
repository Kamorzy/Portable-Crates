package kamorzy.portablecrates.blockentity;

import kamorzy.portablecrates.PortableCrates;
import kamorzy.portablecrates.block.CrateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class CrateBlockEntity extends LootableContainerBlockEntity {

    // Properties
    private DefaultedList<ItemStack> inventory;
    private final ViewerCountManager stateManager;
    private static final int slotSize = 9;

    public int size() {
        return slotSize;
    }

    protected DefaultedList<ItemStack> getInvStackList() {
        return this.inventory;
    }

    protected void setInvStackList(DefaultedList<ItemStack> list) {
        this.inventory = list;
    }

    protected Text getContainerName() {
        return Text.translatable("container.crate");
    }

    // Init
    public CrateBlockEntity(BlockPos pos, BlockState state) {
        super(PortableCrates.CRATE_BLOCK_ENTITY, pos, state);
        this.inventory = DefaultedList.ofSize(slotSize, ItemStack.EMPTY);
        this.stateManager = new ViewerCountManager() {
            protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
                CrateBlockEntity.this.playSound(state, SoundEvents.BLOCK_BARREL_OPEN);
            }

            protected void onContainerClose(World world, BlockPos pos, BlockState state) {
                CrateBlockEntity.this.playSound(state, SoundEvents.BLOCK_BARREL_CLOSE);
            }

            protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
            }

            protected boolean isPlayerViewing(PlayerEntity player) {
                if (player.currentScreenHandler instanceof GenericContainerScreenHandler) {
                    Inventory inventory = ((GenericContainerScreenHandler)player.currentScreenHandler).getInventory();
                    return inventory == CrateBlockEntity.this;
                } else {
                    return false;
                }
            }
        };
    }

    // Events
    public void tick() {
        if (!this.removed) {
            this.stateManager.updateViewerCount(this.getWorld(), this.getPos(), this.getCachedState());
        }
    }

    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new Generic3x3ContainerScreenHandler(syncId, playerInventory, this);
    }

    void playSound(BlockState state, SoundEvent soundEvent) {
        Vec3i vec3i = ((Direction)state.get(CrateBlock.FACING)).getVector();
        double d = (double)this.pos.getX() + 0.5 + (double)vec3i.getX() / 2.0;
        double e = (double)this.pos.getY() + 0.5 + (double)vec3i.getY() / 2.0;
        double f = (double)this.pos.getZ() + 0.5 + (double)vec3i.getZ() / 2.0;
        this.world.playSound((PlayerEntity)null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
    }

    // Player Interactions
    public void onOpen(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.stateManager.openContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
        }
    }

    public void onClose(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.stateManager.closeContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
        }
    }

    // NBT
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (!this.serializeLootTable(nbt)) {
            Inventories.writeNbt(nbt, this.inventory, false);
        }
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.readInventoryNbt(nbt);
    }

    public void readInventoryNbt(NbtCompound nbt) {
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.deserializeLootTable(nbt) && nbt.contains("Items", 9)) {
            Inventories.readNbt(nbt, this.inventory);
        }
    }

    // Blockstate Update
    public void markDirty(){
        if (this.world != null) {
            updateEmptyState(this.world, this.pos);
        }
        super.markDirty();
    }

    public void updateEmptyState(World world, BlockPos pos){
        if (!world.isClient) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof CrateBlock) {
                if (this.isEmpty()) {
                    world.setBlockState(pos, state.with(CrateBlock.EMPTY, true));
                } else {
                    world.setBlockState(pos, state.with(CrateBlock.EMPTY, false));
                }
            }
        }
    }
}
