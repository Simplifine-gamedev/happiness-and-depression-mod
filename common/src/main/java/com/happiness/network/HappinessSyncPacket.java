package com.happiness.network;

import com.happiness.HappinessMod;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * Packet sent from server to client to sync happiness level
 */
public record HappinessSyncPacket(float happiness) implements CustomPayload {
    
    public static final Identifier PACKET_ID = Identifier.of(HappinessMod.MOD_ID, "happiness_sync");
    public static final Id<HappinessSyncPacket> ID = new Id<>(PACKET_ID);
    
    public static final PacketCodec<RegistryByteBuf, HappinessSyncPacket> CODEC = PacketCodec.tuple(
            PacketCodecs.FLOAT, HappinessSyncPacket::happiness,
            HappinessSyncPacket::new
    );
    
    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
