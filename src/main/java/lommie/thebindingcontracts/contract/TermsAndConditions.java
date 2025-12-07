package lommie.thebindingcontracts.contract;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lommie.thebindingcontracts.TheBindingContracts;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

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

    @SuppressWarnings({"unused", "EmptyMethod"})
    public void onUse(ServerWorld world, ServerPlayerEntity user, Hand hand, ItemStack stack, ItemStack stackInOtherHand, Contract contract) {

    }

    @SuppressWarnings("unused")
    public void onTickForEachPlayer(ServerWorld world, ServerPlayerEntity player, Contract contract){

    }

    @SuppressWarnings("unused")
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

    public static List<Text> listToDisplayText(List<TermsAndConditionsType> terms){
        ArrayList<Text> texts = new ArrayList<>(terms.size());
        for (TermsAndConditionsType term : terms) {
            texts.add(term.typeGetDisplayName());
        }
        return texts;
    }

    public static List<TermsAndConditionsType> getTypesFromIds(List<Identifier> termIds){
        ArrayList<TermsAndConditionsType> texts = new ArrayList<>(termIds.size());
        for (Identifier termId : termIds) {
            texts.add(TheBindingContracts.TERM_TYPE_REGISTRY.get(termId));
        }
        return texts;
    }

    public static Map<Integer,Identifier> getTermsWithActionsAndIndex(List<TermsAndConditionsType> terms) {
        HashMap<Integer,Identifier> termsWithActions = new HashMap<>(terms.size());
        for (int i = 0; i < terms.size(); i++) {
            TermsAndConditionsType term = terms.get(i);
            if (term.typeHasAction()){
                termsWithActions.put(i,term.typeGetId());
            }
        }
        return termsWithActions;
    }
}
