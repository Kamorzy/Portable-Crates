package kamorzy.portablecrates.registry;

import kamorzy.portablecrates.screen.CrateScreen;
import net.minecraft.client.gui.screens.MenuScreens;

public final class ModScreens {
    private ModScreens() {
    }

    public static void initialize() {
        MenuScreens.register(ModMenuTypes.CRATE_MENU, CrateScreen::new);
    }
}