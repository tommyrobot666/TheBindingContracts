package lommie.thebindingcontracts.contract;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

public class TermsAndConditions {
    public static final Codec<TermsAndConditions> CODEC = new CustomCodec();
    public final NbtCompound savedData;
    public final RegistryKey<TermsAndConditions> key;

    public TermsAndConditions(RegistryKey<TermsAndConditions> key) {
        this(key, new NbtCompound());
    }

    public TermsAndConditions(RegistryKey<TermsAndConditions> key, NbtCompound savedData) {
        this.key = key;
        this.savedData = savedData;
    }

    public void onUseWhenOtherIsOnline(ServerWorld world, ServerPlayerEntity user, ServerPlayerEntity other, Hand hand, ItemStack stack, ItemStack stackInOtherHand, Contract contract) {

    }

    public void onTickForEachPlayer(ServerWorld world, ServerPlayerEntity player){

    }

    public Codec<TermsAndConditions> getCodec() {
        return RecordCodecBuilder.create((instance) ->
                instance.group(RegistryKey.createCodec(key.getRegistryRef()).fieldOf("id")
                        .forGetter((t) -> t.key),
                        NbtCompound.CODEC.fieldOf("savedData").forGetter((t) -> t.savedData))
                        .apply(instance, TermsAndConditions::new)
        );
    }

    private static class CustomCodec implements Codec<TermsAndConditions> {
        @Override
        public <T> DataResult<T> encode(TermsAndConditions termsAndConditions, DynamicOps<T> ops, T t) {
            return termsAndConditions.getCodec().encode(termsAndConditions, ops, t);
        }

        @Override
        public <T> DataResult<Pair<TermsAndConditions, T>> decode(DynamicOps<T> ops, T t) {
            return null;
        }
    }
}
