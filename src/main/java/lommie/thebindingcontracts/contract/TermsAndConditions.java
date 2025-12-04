package lommie.thebindingcontracts.contract;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lommie.thebindingcontracts.TheBindingContracts;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Optional;

public abstract class TermsAndConditions implements TermsAndConditionsType{
    public static final Codec<TermsAndConditions> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(Identifier.CODEC.fieldOf("id")
                                    .forGetter(TermsAndConditions::typeGetId),
                            NbtCompound.CODEC.optionalFieldOf("savedData").forGetter((t) -> Optional.ofNullable(t.savedData))
            ).apply(instance, (id,optSavedData) -> {
                if (optSavedData.isPresent()){
                    return TermsAndConditions.createNew(id,optSavedData.orElseThrow());
                } else {
                    return TermsAndConditions.createNew(id);
                }
            })
    );
    public static final PacketCodec<RegistryByteBuf, TermsAndConditions> PACKET_CODEC = PacketCodec.ofStatic(
            (buf, term) -> {
                buf.writeIdentifier(term.typeGetId());
                buf.writeNbt(term.savedData);
            },
            (buf -> TermsAndConditions.createNew(buf.readIdentifier(),buf.readNbt())));

    public final NbtCompound savedData;

    public TermsAndConditions() {
        this(new NbtCompound());
    }

    public TermsAndConditions(NbtCompound savedData) {
        this.savedData = savedData;
    }

    @SuppressWarnings("unused")
    public void onUse(ServerWorld world, ServerPlayerEntity user, Hand hand, ItemStack stack, ItemStack stackInOtherHand, Contract contract) {

    }

    public void onTickForEachPlayer(ServerWorld world, ServerPlayerEntity player, Contract contract){

    }

    public void onTermsJustBroken(MinecraftServer server, Contract contract) {

    }

    @SuppressWarnings("unused")
    public boolean onTermsBrokenTick(MinecraftServer server, Contract contract) {
        return true;
    }

    public static TermsAndConditions createNew(Identifier id){
        return Objects.requireNonNull(TheBindingContracts.TERM_TYPE_REGISTRY.get(id)).typeCreateNew();
    }

    public static TermsAndConditions createNew(Identifier id, NbtCompound savedData){
        return Objects.requireNonNull(TheBindingContracts.TERM_TYPE_REGISTRY.get(id)).typeCreateNew(savedData);
    }

    @SuppressWarnings("unused")
    public TermsAndConditions typeCreateNewExceptions(NbtCompound savedData) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return this.getClass().getConstructor(NbtCompound.class).newInstance(savedData);
    }

    @SuppressWarnings("unused")
    public TermsAndConditions typeCreateNewExceptions() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return this.getClass().getConstructor().newInstance();
    }
}
