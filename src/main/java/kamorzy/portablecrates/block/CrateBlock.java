package kamorzy.portablecrates.block;

import com.mojang.serialization.MapCodec;
import kamorzy.portablecrates.PortableCrates;
import kamorzy.portablecrates.blockentity.CrateBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

public class CrateBlock extends BaseEntityBlock {
    // Properties
    public static final Identifier CONTENTS = Identifier.fromNamespaceAndPath(PortableCrates.MOD_ID, "contents");;
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    private final WoodType wood;

    public CrateBlock(BlockBehaviour.Properties properties) {
        this(WoodType.SPRUCE, properties);
    }

    public CrateBlock(WoodType wood, BlockBehaviour.Properties properties) {
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, net.minecraft.world.level.block.state.BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(CrateBlock::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrateBlockEntity(pos, state);
    }

    public WoodType getWoodType() {
        return wood;
    }
}
