package kamorzy.portablecrates;

import kamorzy.portablecrates.block.CrateBlock;
import kamorzy.portablecrates.block.SealedCrateBlock;
import kamorzy.portablecrates.blockentity.CrateBlockEntity;
import kamorzy.portablecrates.blockentity.SealedCrateBlockEntity;
import kamorzy.portablecrates.screen.CrateScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.BlockPlacementDispenserBehavior;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.*;
import net.minecraft.util.Identifier;

public class PortableCrates implements ModInitializer {

    public static final Block CRATE_BLOCK;
    public static final BlockItem CRATE_BLOCK_ITEM;
    public static final BlockEntityType<CrateBlockEntity> CRATE_BLOCK_ENTITY;

    public static final Block SEALED_CRATE_BLOCK;
    public static final BlockItem SEALED_CRATE_BLOCK_ITEM;
    public static final BlockEntityType<SealedCrateBlockEntity> SEALED_CRATE_BLOCK_ENTITY;

    public static final ScreenHandlerType<CrateScreenHandler> CRATE_SCREEN_HANDLER;

    public static final String MOD_ID = "portablecrates";
    public static final Identifier CRATE = new Identifier(MOD_ID, "spruce_crate");
    public static final Identifier SEALED_CRATE = new Identifier(MOD_ID, "spruce_sealed_crate");
    public static final Identifier CRATE_SCREEN = new Identifier(MOD_ID, "crate_screen");

    static {
        CRATE_BLOCK = Registry.register(Registries.BLOCK, CRATE, new CrateBlock(WoodType.SPRUCE, FabricBlockSettings.copyOf(Blocks.BARREL).pistonBehavior(PistonBehavior.DESTROY).hardness(2.0f).mapColor(MapColor.SPRUCE_BROWN)));
        CRATE_BLOCK_ITEM = Registry.register(Registries.ITEM, CRATE, new BlockItem(CRATE_BLOCK, new FabricItemSettings()));
        CRATE_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, CRATE, FabricBlockEntityTypeBuilder.create(CrateBlockEntity::new, CRATE_BLOCK).build(null));

        SEALED_CRATE_BLOCK = Registry.register(Registries.BLOCK, SEALED_CRATE, new SealedCrateBlock(WoodType.SPRUCE, FabricBlockSettings.copyOf(Blocks.BARREL).pistonBehavior(PistonBehavior.DESTROY).hardness(2.0f).mapColor(MapColor.SPRUCE_BROWN)));
        SEALED_CRATE_BLOCK_ITEM = Registry.register(Registries.ITEM, SEALED_CRATE, new BlockItem(SEALED_CRATE_BLOCK, new FabricItemSettings().maxCount(1)));
        SEALED_CRATE_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, SEALED_CRATE, FabricBlockEntityTypeBuilder.create(SealedCrateBlockEntity::new, SEALED_CRATE_BLOCK).build(null));

        CRATE_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, CRATE_SCREEN, new ScreenHandlerType<>(CrateScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
    }

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> {
            content.addAfter(Items.BARREL, CRATE_BLOCK_ITEM);
            content.addAfter(CRATE_BLOCK_ITEM, SEALED_CRATE_BLOCK_ITEM);
        });
        FuelRegistry.INSTANCE.add(CRATE_BLOCK_ITEM, 300);
        DispenserBlock.registerBehavior(CRATE_BLOCK_ITEM, new BlockPlacementDispenserBehavior());
        DispenserBlock.registerBehavior(SEALED_CRATE_BLOCK_ITEM, new BlockPlacementDispenserBehavior());
    }
}
