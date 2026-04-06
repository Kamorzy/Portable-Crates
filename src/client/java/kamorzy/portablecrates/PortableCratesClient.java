package kamorzy.portablecrates;

import kamorzy.portablecrates.registry.ModScreens;
import net.fabricmc.api.ClientModInitializer;

public class PortableCratesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ModScreens.initialize();
	}
}