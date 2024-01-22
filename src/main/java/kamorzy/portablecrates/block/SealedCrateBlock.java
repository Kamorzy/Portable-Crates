package kamorzy.portablecrates.block;

import com.mojang.serialization.MapCodec;
import kamorzy.portablecrates.blockentity.SealedCrateBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;

public class SealedCrateBlock extends BlockWithEntity {

    // Properties
    public static final Identifier CONTENTS = new Identifier("contents");
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    private final WoodType wood;

    public MapCodec<SealedCrateBlock> getCodec() {
        return null;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public SealedCrateBlock(WoodType wood, Settings settings) {
        super(settings);
        this.wood = wood;
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate((Direction)state.get(FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.get(FACING)));
    }

    // Init
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SealedCrateBlockEntity(pos, state);
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof SealedCrateBlockEntity) {
            ((SealedCrateBlockEntity)blockEntity).tick();
        }
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    // Player Interactions
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.isCreative()) {
            if (world.isClient) {
                return ActionResult.SUCCESS;
            } else {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof SealedCrateBlockEntity) {
                    player.openHandledScreen((SealedCrateBlockEntity)blockEntity);
                    PiglinBrain.onGuardedBlockInteracted(player, true);
                }
                return ActionResult.CONSUME;
            }
        } else {
            return ActionResult.PASS;
        }
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof SealedCrateBlockEntity) {
                ((SealedCrateBlockEntity)blockEntity).setCustomName(itemStack.getName());
            }
        }
    }

    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof SealedCrateBlockEntity) {
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        BlockEntity blockEntity = (BlockEntity)builder.getOptional(LootContextParameters.BLOCK_ENTITY);
        if (blockEntity instanceof SealedCrateBlockEntity sealedCrateBlockEntity) {
            builder = builder.addDynamicDrop(CONTENTS, (lootConsumer) -> {
                for(int i = 0; i < sealedCrateBlockEntity.size(); ++i) {
                    lootConsumer.accept(sealedCrateBlockEntity.getStack(i));
                }
            });
        }
        return super.getDroppedStacks(state, builder);
    }

    // Redstone
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }
}
