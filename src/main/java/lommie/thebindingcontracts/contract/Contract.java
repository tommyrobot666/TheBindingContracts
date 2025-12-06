package lommie.thebindingcontracts.contract;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lommie.thebindingcontracts.items.TwoPlayerContractItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import net.minecraft.world.World;

import java.util.*;

public class Contract {
    public static final Codec<Contract> CODEC = RecordCodecBuilder.create((instance) ->
        instance.group(TermsAndConditions.CODEC.listOf().fieldOf("terms")
                .forGetter((c) -> c.terms),
                Uuids.CODEC.listOf().fieldOf("signers").forGetter((c) -> c.signers),
                Codec.BOOL.fieldOf("signed").forGetter((c) -> c.signed))
                .apply(instance, Contract::new)
    );

    private final ArrayList<TermsAndConditions> terms = new ArrayList<>();
    private final ArrayList<UUID> signers = new ArrayList<>();
    private boolean signed = false;
    private boolean broken = false;

    public Contract(){}

    private Contract(List<TermsAndConditions> terms, List<UUID> signers, boolean signed ){
        this.terms.addAll(terms);
        this.signers.addAll(signers);
        this.signed = signed;
    }

    public void onUseItem(int selectedTerm, World world, PlayerEntity user, Hand hand) {
        if(!signers.contains(user.getUuid())) return;
        if(world.isClient()) return;
        ItemStack stack = user.getStackInHand(hand);
        ItemStack stackInOtherHand = inOtherHand(user, hand);
        ServerPlayerEntity other = TwoPlayerContractItem.getOtherPlayer(this, (ServerWorld) world,user.getUuid());


        TermsAndConditions term = terms.get(selectedTerm);
        if (term instanceof TwoPlayerTermsAndConditions<?> twoPlayerTerm && other != null) {
            twoPlayerTerm.onUseWhenOtherIsOnline((ServerWorld) world, (ServerPlayerEntity) user, other, hand, stack, stackInOtherHand, this);
        } else {
            term.onUse((ServerWorld) world, (ServerPlayerEntity) user, hand, stack, stackInOtherHand, this);
        }
    }

    private static ItemStack inOtherHand(PlayerEntity player, Hand hand){
        if (hand == Hand.MAIN_HAND){
            return player.getStackInHand(Hand.OFF_HAND);
        } else {
            return player.getStackInHand(Hand.MAIN_HAND);
        }
    }
    
    @SuppressWarnings("unused")
    public void onTick(MinecraftServer server){
        for (UUID uuid : signers){
            ServerPlayerEntity player = server.getPlayerManager().getPlayer(uuid);
            if (player == null){
                continue;
            }
            for (TermsAndConditions term : terms) {
                term.onTickForEachPlayer(player.getEntityWorld(), player, this);
            }
        }
    }

    @SuppressWarnings({"unused", "EmptyMethod"})
    public void onTermsJustBroken(MinecraftServer server){
        for (TermsAndConditions term : terms) {
            term.onTermsJustBroken(server, this);
        }
    }

    /// @return is onBroken done?
    @SuppressWarnings({"unused", "SameReturnValue"})
    public boolean onTermsBrokenTick(MinecraftServer server){
        boolean done = true;
        for (TermsAndConditions term : terms){
            boolean termDone = term.onTermsBrokenTick(server, this);
            done = done && termDone;
        }
        return done;
    }

    public void addTerm(TermsAndConditions term){
        terms.add(term);
    }

    public void addSigner(UUID uuid){
        signers.add(uuid);
    }

    public void sign(){
        signed = true;
    }

    @SuppressWarnings("unused")
    public boolean isSigned(){
        return signed;
    }

    @SuppressWarnings("unused")
    public void break2(){
        broken = true;
    }

    public boolean isBroken(){
        return broken;
    }

    public boolean isValidAndSigned(){
        return isValid()&&signed;
    }

    public boolean isValid(){
        return signers.size()>1&&!broken&&!terms.isEmpty();
    }

    public boolean isUnfinished(){
        return !signed&&!broken;
    }

    public List<UUID> getSigners(){
        return signers;
    }

    public String listTermIds() {
        StringBuilder builder = new StringBuilder();
        for (TermsAndConditions term : terms) {
            builder.append(term.typeGetId().toString());
            builder.append(", ");
        }
        return builder.toString();
    }

    public String listSignerIds() {
        StringBuilder builder = new StringBuilder();
        for (UUID signer : signers) {
            builder.append(signer.toString());
            builder.append(", ");
        }
        return builder.toString();
    }

    public void forceSetBroken(boolean val) {
        broken = val;
    }

    public void forceSetSigned(boolean val) {
        signed = val;
    }

    public Map<Integer,Identifier> getTermsWithActionsAndIndex() {
        HashMap<Integer,Identifier> termsWithActions = new HashMap<>(terms.size());
        for (int i = 0; i < terms.size(); i++) {
            TermsAndConditions term = terms.get(i);
            if (term.typeHasAction()){
                termsWithActions.put(i,term.typeGetId());
            }
        }
        return termsWithActions;
    }
}
