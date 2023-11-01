package kamorzy.portablecrates;

import kamorzy.portablecrates.screen.CrateScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

@Environment(EnvType.CLIENT)
public class PortableCratesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(PortableCrates.CRATE_SCREEN_HANDLER, CrateScreen::new);
    }
}
