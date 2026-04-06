package kamorzy.portablecrates.block;

import com.mojang.serialization.MapCodec;
import kamorzy.portablecrates.blockentity.CrateBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;

public class CrateBlock extends BaseCrateBlock {

    // ✅ REQUIRED IN 26.1
    public static final MapCodec<CrateBlock> CODEC =
            simpleCodec(CrateBlock::new);

    public CrateBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public CrateBlock(WoodType wood, BlockBehaviour.Properties properties) {
        super(wood, properties);
    }

    // ✅ REQUIRED IN 26.1
    @Override
    public @NonNull MapCodec<? extends CrateBlock> codec() {
        return CODEC;
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hitResult
    ) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (level.getBlockEntity(pos) instanceof CrateBlockEntity crateBlockEntity) {
            player.openMenu(crateBlockEntity);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }
}