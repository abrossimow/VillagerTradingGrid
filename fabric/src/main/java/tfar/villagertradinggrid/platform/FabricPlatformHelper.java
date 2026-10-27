package tfar.villagertradinggrid.platform;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import tfar.villagertradinggrid.network.PacketHandler;
import tfar.villagertradinggrid.network.client.S2CModPacket;
import tfar.villagertradinggrid.network.server.C2SModPacket;
import tfar.villagertradinggrid.platform.services.IPlatformHelper;

import java.util.function.Function;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance()
                .getEnvironmentType() == EnvType.CLIENT
                && FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public <MSG extends S2CModPacket> void registerClientPacket(
            Class<MSG> packetClass,
            Function<FriendlyByteBuf, MSG> reader) {

        ResourceLocation id = PacketHandler.packet(packetClass);

        ClientPlayNetworking.registerGlobalReceiver(
                id,
                (client, handler, buf, responseSender) -> {
                    MSG packet = reader.apply(buf);
                    client.execute(packet::handleClient);
                }
        );
    }

    @Override
    public <MSG extends C2SModPacket> void registerServerPacket(
            Class<MSG> packetClass,
            Function<FriendlyByteBuf, MSG> reader) {

        ResourceLocation id = PacketHandler.packet(packetClass);

        ServerPlayNetworking.registerGlobalReceiver(
                id,
                (server, player, handler, buf, responseSender) -> {
                    MSG packet = reader.apply(buf);
                    server.execute(() -> packet.handleServer(player));
                }
        );
    }

    @Override
    public void sendToClient(S2CModPacket packet, ServerPlayer player) {
        ServerPlayNetworking.send(
                player,
                PacketHandler.packet(packet.getClass()),
                encode(packet)
        );
    }

    @Override
    public void sendToServer(C2SModPacket packet) {
        ClientPlayNetworking.send(
                PacketHandler.packet(packet.getClass()),
                encode(packet)
        );
    }

    private static FriendlyByteBuf encode(
            tfar.villagertradinggrid.network.ModPacket packet) {

        FriendlyByteBuf buf = new FriendlyByteBuf(
                io.netty.buffer.Unpooled.buffer()
        );

        packet.write(buf);
        return buf;
    }

    @Override
    public boolean canEquip(
            ItemStack stack,
            EquipmentSlot slot,
            Player player) {
        return !stack.isEmpty()
                && LivingEntity.getEquipmentSlotForItem(stack) == slot;
    }
}