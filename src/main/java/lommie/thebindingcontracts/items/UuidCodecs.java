package lommie.thebindingcontracts.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;

import java.util.UUID;

public class UuidCodecs {
    public static final Codec<UUID> CODEC =
            RecordCodecBuilder.create((instance -> instance.group(
                    Codec.LONG.fieldOf("id_msb").forGetter((uuid) -> uuid.getMostSignificantBits()),
                    Codec.LONG.fieldOf("id_lsb").forGetter((uuid) -> uuid.getLeastSignificantBits())
            ).apply(instance, (lsb,msb) -> new UUID(msb,lsb))));
    public static final PacketCodec<? super PacketByteBuf, UUID> PACKET_CODEC =
            PacketCodec.of((uuid,buf) -> buf.writeUuid(uuid), (buf) -> buf.readUuid());
}
