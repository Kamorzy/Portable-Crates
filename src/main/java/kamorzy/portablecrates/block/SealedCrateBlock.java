package kamorzy.portablecrates.block;

import com.mojang.serialization.MapCodec;
import kamorzy.portablecrates.blockentity.CrateBlockEntity;
import kamorzy.portablecrates.component.CrateContentsComponent;
import kamorzy.portablecrates.registry.ModBlocks;
import kamorzy.portablecrates.registry.ModDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;

public class SealedCrateBlock extends BaseCrateBlock {
    public static final MapCodec<SealedCrateBlock> CODEC = simpleCodec(SealedCrateBlock::new);

    public SealedCrateBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public SealedCrateBlock(WoodType wood, BlockBehaviour.Properties properties) {
        super(wood, properties);
    }

    @Override
    public @NonNull MapCodec<? extends SealedCrateBlock> codec() {
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
        return InteractionResult.PASS;
    }

    @Override
    protected java.util.@NonNull List<ItemStack> getDrops(
            BlockState state,
            LootParams.Builder params
    ) {
        BlockEntity blockEntity = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (!(blockEntity instanceof CrateBlockEntity crateBlockEntity)) {
            return java.util.Collections.emptyList();
        }

        ItemInstance tool = params.getOptionalParameter(LootContextParams.TOOL);

        boolean hasSilkTouch = false;
        if (tool != null) {
            hasSilkTouch = EnchantmentHelper.getItemEnchantmentLevel(
                    params.getLevel()
                            .registryAccess()
                            .lookupOrThrow(Registries.ENCHANTMENT)
                            .getOrThrow(Enchantments.SILK_TOUCH),
                    tool
            ) > 0;
        }

        CrateContentsComponent contents =
                CrateContentsComponent.fromItems(crateBlockEntity.copyStoredItems());

        if (hasSilkTouch) {
            ItemStack sealedCrateStack = new ItemStack(ModBlocks.SPRUCE_SEALED_CRATE.asItem());

            if (!contents.isEmpty()) {
                sealedCrateStack.set(ModDataComponents.CRATE_CONTENTS, contents);
            }

            return java.util.Collections.singletonList(sealedCrateStack);
        }

        java.util.ArrayList<ItemStack> drops = new java.util.ArrayList<>();
        for (ItemStack stack : crateBlockEntity.copyStoredItems()) {
            if (!stack.isEmpty()) {
                drops.add(stack.copy());
            }
        }

        return drops;
    }
}