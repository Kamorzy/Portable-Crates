package kamorzy.portablecrates.registry;

import kamorzy.portablecrates.PortableCrates;
import kamorzy.portablecrates.blockentity.CrateBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class ModBlockEntities {
    public static final BlockEntityType<CrateBlockEntity> CRATE_BLOCK_ENTITY =
            Registry.register(
                    BuiltInRegistries.BLOCK_ENTITY_TYPE,
                    Identifier.fromNamespaceAndPath(PortableCrates.MOD_ID, "crate_block_entity"),
                    FabricBlockEntityTypeBuilder.create(CrateBlockEntity::new, ModBlocks.CRATE_BLOCK).build()
            );

    public static void initialize() {
    }

    private ModBlockEntities() {
    }
}