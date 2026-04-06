package kamorzy.portablecrates.block;

import kamorzy.portablecrates.blockentity.CrateBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
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

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrateBlockEntity(pos, state);
    }

    @Override
    protected void affectNeighborsAfterRemoval(
            BlockState state,
            ServerLevel level,
            BlockPos pos,
            boolean movedByPiston
    ) {
        level.updateNeighbourForOutputSignal(pos, this);
    }

    @Override
    public abstract @NonNull MapCodec<? extends BaseCrateBlock> codec();

    public WoodType getWoodType() {
        return wood;
    }
}