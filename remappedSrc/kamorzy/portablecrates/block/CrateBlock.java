package kamorzy.portablecrates.block;

import kamorzy.portablecrates.PortableCrates;
import kamorzy.portablecrates.blockentity.CrateBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.loot.context.*;
import net.minecraft.entity.ItemEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CrateBlock extends BlockWithEntity {

    public static final Identifier CONTENTS = new Identifier("contents");

    public CrateBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CrateBlockEntity(pos, state);
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CrateBlockEntity) {
                player.openHandledScreen((CrateBlockEntity)blockEntity);
                PiglinBrain.onGuardedBlockInteracted(player, true);
            }

            return ActionResult.CONSUME;
        }
    }

    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CrateBlockEntity CrateBlockEntity) {
            // If creative mode, drop the item again with its inventory (think by default blocks drop nothing in creative)
            if (!world.isClient && player.isCreative() && !CrateBlockEntity.isEmpty()) {
                ItemStack itemStack = new ItemStack(PortableCrates.CRATE_BLOCK);
                blockEntity.setStackNbt(itemStack);
                if (CrateBlockEntity.hasCustomName()) {
                    itemStack.setCustomName(CrateBlockEntity.getCustomName());
                }
                ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, itemStack);
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
            // No idea what this does, loot drops according to loot table regardless of this statement
            // Found in ShulkerboxBlock class tho so keeping it.
            } else {
                CrateBlockEntity.checkLootInteraction(player);
            }
        }
        super.onBreak(world, pos, state, player);
    }

    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        BlockEntity blockEntity = (BlockEntity)builder.getNullable(LootContextParameters.BLOCK_ENTITY);
        if (blockEntity instanceof CrateBlockEntity crateBlockEntity) {
            builder = builder.putDrop(CONTENTS, (context, consumer) -> {
                for(int i = 0; i < crateBlockEntity.size(); ++i) {
                    consumer.accept(crateBlockEntity.getStack(i));
                }

            });
        }
        return super.getDroppedStacks(state, builder);
    }

    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        ItemStack itemStack = super.getPickStack(world, pos, state);
        world.getBlockEntity(pos, PortableCrates.CRATE_BLOCK_ENTITY).ifPresent((blockEntity) -> {
            blockEntity.setStackNbt(itemStack);
        });
        return itemStack;
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CrateBlockEntity) {
            ((CrateBlockEntity) blockEntity).tick();
        }
    }

    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }
}
