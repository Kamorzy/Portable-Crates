package kamorzy.portablecrates.registry;

import kamorzy.portablecrates.PortableCrates;
import kamorzy.portablecrates.screen.CrateMenu;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public final class ModMenuTypes {
    public static final MenuType<CrateMenu> CRATE_MENU =
            Registry.register(
                    BuiltInRegistries.MENU,
                    Identifier.fromNamespaceAndPath(PortableCrates.MOD_ID, "crate"),
                    new MenuType<>(CrateMenu::new, FeatureFlags.DEFAULT_FLAGS)
            );

    public static void initialize() {
    }

    private ModMenuTypes() {
    }
}