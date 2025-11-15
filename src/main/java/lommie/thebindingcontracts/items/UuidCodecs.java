package lommie.thebindingcontracts.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;

import java.util.UUID;

public class UuidCodecs {
    public static final Codec<UUID> CODEC =
            RecordCodecBuilder.create((instance -> instance.group(
                    Codec.STRING.fieldOf("id").forGetter(UUID::toString)
            ).apply(instance, UUID::fromString)));
    public static final PacketCodec<? super PacketByteBuf, UUID> PACKET_CODEC =
            PacketCodec.of((uuid,buf) -> buf.writeUuid(uuid), (buf) -> buf.readUuid());
}
