package tfar.villagertradinggrid;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class VillagerTradingGridFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Registry.register(
                BuiltInRegistries.MENU,
                VillagerTradingGrid.id("villager_trading_grid"),
                Init.VILLAGER_TRADING_GRID
        );

        VillagerTradingGrid.init();
    }
}