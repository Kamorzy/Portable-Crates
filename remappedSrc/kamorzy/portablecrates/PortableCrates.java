package kamorzy.portablecrates;

import kamorzy.portablecrates.block.CrateBlock;
import kamorzy.portablecrates.block.SealedCrateBlock;
import kamorzy.portablecrates.blockentity.CrateBlockEntity;
import kamorzy.portablecrates.blockentity.SealedCrateBlockEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class PortableCrates implements ModInitializer {

    public static final Block CRATE_BLOCK;
    public static final BlockItem CRATE_BLOCK_ITEM;
    public static final BlockEntityType<CrateBlockEntity> CRATE_BLOCK_ENTITY;

    public static final Block SEALED_CRATE_BLOCK;
    public static final BlockItem SEALED_CRATE_BLOCK_ITEM;
    public static final BlockEntityType<SealedCrateBlockEntity> SEALED_CRATE_BLOCK_ENTITY;

    // Commonly used identifiers
    public static final String MOD_ID = "portablecrates";
    public static final Identifier CRATE = new Identifier(MOD_ID, "crate");
    public static final Identifier SEALED_CRATE = new Identifier(MOD_ID, "sealed_crate");

    // Registry of banned items json
    public static final TagKey<Item> BANNED_ITEMS = TagKey.of(Registries.ITEM.getKey(), new Identifier(MOD_ID, "banned_items"));

    static {
        CRATE_BLOCK = Registry.register(Registries.BLOCK, CRATE, new CrateBlock(FabricBlockSettings.copyOf(Blocks.BARREL)));
        CRATE_BLOCK_ITEM = Registry.register(Registries.ITEM, CRATE, new BlockItem(CRATE_BLOCK, new FabricItemSettings()));
        CRATE_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, CRATE, FabricBlockEntityTypeBuilder.create(CrateBlockEntity::new, CRATE_BLOCK).build(null));

        SEALED_CRATE_BLOCK = Registry.register(Registries.BLOCK, SEALED_CRATE, new SealedCrateBlock(FabricBlockSettings.copyOf(Blocks.BARREL)));
        SEALED_CRATE_BLOCK_ITEM = Registry.register(Registries.ITEM, SEALED_CRATE, new BlockItem(SEALED_CRATE_BLOCK, new FabricItemSettings()));
        SEALED_CRATE_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, SEALED_CRATE, FabricBlockEntityTypeBuilder.create(SealedCrateBlockEntity::new, SEALED_CRATE_BLOCK).build(null));
    }

    @Override
    public void onInitialize() {

    }
}
