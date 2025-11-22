package lommie.thebindingcontracts.contract;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

public class TermsAndConditions {
    public final NbtCompound savedData;
    public final RegistryKey<? extends Registry<? extends TermsAndConditions>> key;

    public TermsAndConditions(RegistryKey<? extends Registry<? extends TermsAndConditions>> key) {
        this(key, new NbtCompound());
    }

    public TermsAndConditions(RegistryKey<? extends Registry<? extends TermsAndConditions>> key, NbtCompound savedData) {
        this.key = key;
        this.savedData = savedData;
    }

    public void onUseWhenOtherIsOnline(ServerWorld world, ServerPlayerEntity user, ServerPlayerEntity other, Hand hand, ItemStack stack, ItemStack stackInOtherHand, Contract contract) {

    }

    public void onTickForEachPlayer(ServerWorld world, ServerPlayerEntity player){

    }

    public Codec<TermsAndConditions> getCodec() {
        return RecordCodecBuilder.create((instance) ->
                instance.group(RegistryKey.createCodec(key).fieldOf("id")
                        .forGetter((t) -> ((TermsAndConditions) t).key))
        );
    }
}
