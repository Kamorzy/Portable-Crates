package kamorzy.portablecrates.registry;

import kamorzy.portablecrates.PortableCrates;
import kamorzy.portablecrates.component.CrateContentsComponent;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public final class ModDataComponents {

    // REGISTRY ACTION
    public static final DataComponentType<CrateContentsComponent> CRATE_CONTENTS =
            Registry.register(
                    BuiltInRegistries.DATA_COMPONENT_TYPE,
                    Identifier.fromNamespaceAndPath(PortableCrates.MOD_ID, "crate_contents"),
                    DataComponentType.<CrateContentsComponent>builder()
                            .persistent(CrateContentsComponent.CODEC)
                            .build()
            );

    // BEST PRACTICE
    public static void initialize() {
    }

    private ModDataComponents() {
    }
}