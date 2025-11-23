package lommie.thebindingcontracts.contract;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lommie.thebindingcontracts.TheBindingContracts;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public abstract class TermsAndConditions {
    public static final Codec<TermsAndConditions> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(Identifier.CODEC.fieldOf("id")
                                    .forGetter((t) -> t.key),
                            NbtCompound.CODEC.fieldOf("savedData").forGetter((t) -> t.savedData))
                    .apply(instance, (id,savedData) ->
                        Objects.requireNonNull(TheBindingContracts.TERM_TYPE_REGISTRY.get(id)).newWithData(savedData)
                    )
    );

    private TermsAndConditions newWithData(NbtCompound savedData) {
        try {
            return this.getClass().getConstructor(Identifier.class, NbtCompound.class).newInstance(this.key, savedData);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public final NbtCompound savedData;
    public final Identifier key;

    public TermsAndConditions(Identifier key) {
        this(key, new NbtCompound());
    }

    public TermsAndConditions(Identifier key, NbtCompound savedData) {
        this.key = key;
        this.savedData = savedData;
    }

    public void onUse(ServerWorld world, ServerPlayerEntity user, Hand hand, ItemStack stack, ItemStack stackInOtherHand, Contract contract) {

    }

    public void onTickForEachPlayer(ServerWorld world, ServerPlayerEntity player){

    }
}
