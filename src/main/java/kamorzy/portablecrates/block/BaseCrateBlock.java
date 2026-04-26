package kamorzy.portablecrates.block;

import kamorzy.portablecrates.blockentity.CrateBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.WoodType;
import com.mojang.serialization.MapCodec;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public abstract class BaseCrateBlock extends BaseEntityBlock {
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    private final WoodType wood;

    public WoodType getWoodType() {
        return wood;
    }

    // SERIALIZATION

    @Override
    public abstract @NonNull MapCodec<? extends BaseCrateBlock> codec();

    // CONSTRUCTORS

    protected BaseCrateBlock(BlockBehaviour.Properties properties) {
        this(WoodType.SPRUCE, properties);
    }

    protected BaseCrateBlock(WoodType wood, BlockBehaviour.Properties properties) {
        super(properties);
        this.wood = wood;

        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrateBlockEntity(pos, state);
    }

    // STATE and DIRECTION for placement

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    // REDSTONE and TICK UPDATES

    @Override
    protected void affectNeighborsAfterRemoval(
            BlockState state,
            ServerLevel level,
            BlockPos pos,
            boolean movedByPiston
    ) {
        Containers.updateNeighboursAfterDestroy(state, level, pos);
    }

    @Override
    protected boolean hasAnalogOutputSignal(final BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(
            BlockState state,
            Level level,
            BlockPos pos,
            Direction direction
    ) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(
                level.getBlockEntity(pos)
        );
    }
}