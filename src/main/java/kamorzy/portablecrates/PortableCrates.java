package kamorzy.portablecrates;

import kamorzy.portablecrates.registry.ModBlockEntities;
import kamorzy.portablecrates.registry.ModBlocks;
import kamorzy.portablecrates.registry.ModDataComponents;
import kamorzy.portablecrates.registry.ModMenuTypes;
import net.fabricmc.api.ModInitializer;

public class PortableCrates implements ModInitializer {
	public static final String MOD_ID = "portablecrates";

	@Override
	public void onInitialize() {
		ModBlocks.initialize();
		ModBlockEntities.initialize();
		ModMenuTypes.initialize();
		ModDataComponents.initialize();
		ModBlocks.registerDispenserBehaviors();
	}
}