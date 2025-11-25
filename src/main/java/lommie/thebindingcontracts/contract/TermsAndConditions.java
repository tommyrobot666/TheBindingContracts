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

public abstract class TermsAndConditions implements TermsAndConditionsType{
    public static final Codec<TermsAndConditions> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(Identifier.CODEC.fieldOf("id")
                                    .forGetter(TermsAndConditions::typeGetId),
                            NbtCompound.CODEC.fieldOf("savedData").forGetter((t) -> t.savedData)
            ).apply(instance, TermsAndConditions::createNew)
    );

    public final NbtCompound savedData;

    public TermsAndConditions() {
        this(new NbtCompound());
    }

    public TermsAndConditions(NbtCompound savedData) {
        this.savedData = savedData;
    }

    public void onUse(ServerWorld world, ServerPlayerEntity user, Hand hand, ItemStack stack, ItemStack stackInOtherHand, Contract contract) {

    }

    public void onTickForEachPlayer(ServerWorld world, ServerPlayerEntity player){

    }

    public static TermsAndConditions createNew(Identifier id){
        return Objects.requireNonNull(TheBindingContracts.TERM_TYPE_REGISTRY.get(id)).typeCreateNew();
    }

    public static TermsAndConditions createNew(Identifier id, NbtCompound savedData){
        return Objects.requireNonNull(TheBindingContracts.TERM_TYPE_REGISTRY.get(id)).typeCreateNew(savedData);
    }

    public TermsAndConditions typeCreateNewExceptions(NbtCompound savedData) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return this.getClass().getConstructor(NbtCompound.class).newInstance(savedData);
    }

    public TermsAndConditions typeCreateNewExceptions() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return this.getClass().getConstructor().newInstance();
    }
}
