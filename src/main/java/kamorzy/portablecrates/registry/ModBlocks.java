package kamorzy.portablecrates.registry;

import java.util.function.Function;
import kamorzy.portablecrates.PortableCrates;

import kamorzy.portablecrates.block.CrateBlock;
import kamorzy.portablecrates.block.SealedCrateBlock;
import kamorzy.portablecrates.item.SealedCrateBlockItem;
import kamorzy.portablecrates.dispenser.CrateDispenseBehavior;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.PushReaction;

public final class ModBlocks {
    // ADDING TO CREATIVE TAB
    public static void initialize() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
            entries.accept(SPRUCE_CRATE.asItem());
            entries.accept(SPRUCE_SEALED_CRATE.asItem());
        });
    }

    // REGISTER ACTION
    public static final Block SPRUCE_CRATE = register(
            "spruce_crate",
            props -> new CrateBlock(WoodType.SPRUCE, props),
            BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)
                    .pushReaction(PushReaction.DESTROY),
            true
    );

    public static final Block SPRUCE_SEALED_CRATE = registerSealedCrate(
            "spruce_sealed_crate",
            props -> new SealedCrateBlock(WoodType.SPRUCE, props),
            BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)
                    .pushReaction(PushReaction.DESTROY)
    );

    // REGISTER DEFINITION
    public static void registerDispenserBehaviors() {
        CrateDispenseBehavior crateDispenseBehavior = new CrateDispenseBehavior();

        DispenserBlock.registerBehavior(SPRUCE_CRATE.asItem(), crateDispenseBehavior);
        DispenserBlock.registerBehavior(SPRUCE_SEALED_CRATE.asItem(), crateDispenseBehavior);
    }

    private static Block register(
            String name,
            Function<BlockBehaviour.Properties, Block> blockFactory,
            BlockBehaviour.Properties settings,
            boolean shouldRegisterItem
    ) {
        ResourceKey<Block> blockKey = ResourceKey.create(
                Registries.BLOCK,
                Identifier.fromNamespaceAndPath(PortableCrates.MOD_ID, name)
        );

        Block block = blockFactory.apply(settings.setId(blockKey));

        if (shouldRegisterItem) {
            ResourceKey<Item> itemKey = ResourceKey.create(
                    Registries.ITEM,
                    Identifier.fromNamespaceAndPath(PortableCrates.MOD_ID, name)
            );

            BlockItem blockItem = new BlockItem(
                    block,
                    new Item.Properties().setId(itemKey).useBlockDescriptionPrefix()
            );

            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    private static Block registerSealedCrate(
            String name,
            Function<BlockBehaviour.Properties, Block> blockFactory,
            BlockBehaviour.Properties settings
    ) {
        ResourceKey<Block> blockKey = ResourceKey.create(
                Registries.BLOCK,
                Identifier.fromNamespaceAndPath(PortableCrates.MOD_ID, name)
        );

        Block block = blockFactory.apply(settings.setId(blockKey));

        ResourceKey<Item> itemKey = ResourceKey.create(
                Registries.ITEM,
                Identifier.fromNamespaceAndPath(PortableCrates.MOD_ID, name)
        );

        SealedCrateBlockItem blockItem = new SealedCrateBlockItem(
                block,
                new Item.Properties().setId(itemKey).useBlockDescriptionPrefix()
        );

        Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    // BEST PRACTICE
    private ModBlocks() {
    }
}