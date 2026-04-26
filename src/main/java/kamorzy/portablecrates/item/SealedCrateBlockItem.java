package kamorzy.portablecrates.item;

import kamorzy.portablecrates.blockentity.CrateBlockEntity;
import kamorzy.portablecrates.component.CrateContentsComponent;
import kamorzy.portablecrates.registry.ModDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

public class SealedCrateBlockItem extends BlockItem {
    public SealedCrateBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public @NonNull InteractionResult place(BlockPlaceContext context) {
        InteractionResult result = super.place(context);

        if (!result.consumesAction()) {
            return result;
        }

        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();

        if (level.getBlockEntity(pos) instanceof CrateBlockEntity crateBlockEntity) {
            CrateContentsComponent contents = stack.get(ModDataComponents.CRATE_CONTENTS);

            if (contents != null) {
                crateBlockEntity.loadStoredItems(contents.toNonNullList());
            }
        }

        return result;
    }
}