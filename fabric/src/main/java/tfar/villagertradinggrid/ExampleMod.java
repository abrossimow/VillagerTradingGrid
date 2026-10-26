package tfar.villagertradinggrid;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;

public class ExampleMod implements ModInitializer {

    @Override
    public void onInitialize() {
        Registry.register(
                Registries.MENU,
                VillagerTradingGrid.id("villager_trading_grid"),
                Init.VILLAGER_TRADING_GRID
        );

        VillagerTradingGrid.init();
    }
}
