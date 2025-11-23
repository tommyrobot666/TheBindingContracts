package lommie.thebindingcontracts.items;

import com.mojang.serialization.Codec;
import lommie.thebindingcontracts.TheBindingContracts;
import net.minecraft.component.ComponentType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModItemComponents {
    public static <T> ComponentType<T> register(String id, ComponentType<T> type){
         return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(TheBindingContracts.MOD_ID, id), type);
    }

    public static final ComponentType<UUID> CONTRACT_ID = register("contract_id",
            new ComponentType.Builder<UUID>().codec(Uuids.CODEC).packetCodec(Uuids.PACKET_CODEC).build());

    public static final ComponentType<Boolean> BROKEN = register("broken",
            new ComponentType.Builder<Boolean>().codec(Codec.BOOL).packetCodec(PacketCodec.of((b, buf) -> buf.writeBoolean(b),RegistryByteBuf::readBoolean)).build());

    public static final ComponentType<List<String>> SIGNATURES = register("signatures",
            new ComponentType.Builder<List<String>>().codec(Codec.STRING.listOf())
                    .packetCodec(PacketCodec.of((sl, buf) -> buf.writeCollection(sl, PacketByteBuf::writeString),
                            (buf) -> buf.readCollection(ArrayList::new, PacketByteBuf::readString))).build());

    public static final ComponentType<Boolean> SIGNED = register("signed",
            new ComponentType.Builder<Boolean>().codec(Codec.BOOL).packetCodec(PacketCodec.of((b, buf) -> buf.writeBoolean(b),RegistryByteBuf::readBoolean)).build());

    public static void register(){}
}
