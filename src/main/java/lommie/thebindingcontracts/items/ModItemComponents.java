package lommie.thebindingcontracts.items;

import com.mojang.serialization.Codec;
import lommie.thebindingcontracts.TheBindingContracts;
import net.minecraft.component.ComponentType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ModItemComponents {
    public static <T> ComponentType<T> register(String id, Codec<T> codec, PacketCodec<? super RegistryByteBuf, T> byteCodec){
         return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(TheBindingContracts.MOD_ID, id),
                 new ComponentType<T>() {
                     @Override
                     public @Nullable Codec<T> getCodec() {
                         return codec;
                     }

                     @Override
                     public PacketCodec<? super RegistryByteBuf, T> getPacketCodec() {
                         return byteCodec;
                     }
                 });
    }

    public static final ComponentType<UUID> CONTRACT_SIGNATURE = register("first_signature",
            Uuids.CODEC,
            Uuids.PACKET_CODEC);

    public static final ComponentType<UUID> OTHER_CONTRACT_SIGNATURE = register("other_signature",
            Uuids.CODEC,
            Uuids.PACKET_CODEC);

    public static final ComponentType<Boolean> BROKEN = register("broken",
            Codec.BOOL,
            PacketCodec.of((b, buf) -> buf.writeBoolean(b),RegistryByteBuf::readBoolean));

    public static void register(){}
}
