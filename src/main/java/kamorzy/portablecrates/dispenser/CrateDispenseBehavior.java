package kamorzy.portablecrates.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class CrateDispenseBehavior extends OptionalDispenseItemBehavior {
    @Override
    protected ItemStack execute(BlockSource source, ItemStack stack) {
        Direction direction = source.state().getValue(DispenserBlock.FACING);
        Level level = source.level();
        BlockPos placePos = source.pos().relative(direction);

        this.setSuccess(false);

        if (!(stack.getItem() instanceof BlockItem blockItem)) {
            return stack;
        }

        DirectionalPlaceContext context = new DirectionalPlaceContext(
                level,
                placePos,
                direction,
                stack,
                direction
        );

        InteractionResult result = blockItem.place(context);

        if (result.consumesAction()) {
            this.setSuccess(true);
        }

        return stack;
    }
}