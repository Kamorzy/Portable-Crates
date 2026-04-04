package kamorzy.portablecrates.registry;

import kamorzy.portablecrates.PortableCrates;
import kamorzy.portablecrates.screen.CrateScreenHandler;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;

public final class ModScreens {
    public static final MenuType<CrateScreenHandler> CRATE_SCREEN_HANDLER =
            Registry.register(
                    BuiltInRegistries.MENU,
                    Identifier.fromNamespaceAndPath(PortableCrates.MOD_ID, "crate_screen_handler"),
                    new MenuType<>(CrateScreenHandler::new, FeatureFlags.DEFAULT_FLAGS)
            );

    public static void initialize() {
    }

    private ModScreens() {
    }
}