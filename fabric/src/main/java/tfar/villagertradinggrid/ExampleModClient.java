package tfar.villagertradinggrid;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import tfar.villagertradinggrid.client.VillagerTradingGridScreen;

public class ExampleModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MenuScreens.register(
                Init.VILLAGER_TRADING_GRID,
                VillagerTradingGridScreen::new
        );
    }
}
