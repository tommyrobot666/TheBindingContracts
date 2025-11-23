package lommie.thebindingcontracts.contract;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lommie.thebindingcontracts.items.ContractItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Uuids;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        UUID otherId = ContractItem.getOtherPlayer(this, user.getUuid());
        PlayerEntity other = world.getPlayerAnyDimension(otherId);


        TermsAndConditions term = terms.get(selectedTerm);

        // bunch of other onUse stuff

        if (other == null) return;
        
        term.onUseWhenOtherIsOnline((ServerWorld) world, (ServerPlayerEntity) user, (ServerPlayerEntity) other,hand,stack,stackInOtherHand,this);
    }

    private static ItemStack inOtherHand(PlayerEntity player, Hand hand){
        if (hand == Hand.MAIN_HAND){
            return player.getStackInHand(Hand.OFF_HAND);
        } else {
            return player.getStackInHand(Hand.MAIN_HAND);
        }
    }
    
    public void onTick(World world){
        if (world.isClient()) return;
        for (UUID uuid : signers){
            PlayerEntity player = world.getPlayerAnyDimension(uuid);
            if (player == null){
                continue;
            }
            for (TermsAndConditions term : terms) {
                term.onTickForEachPlayer((ServerWorld) world, (ServerPlayerEntity) player);
            }
        }
    }

    public void onTermsJustBroken(){}

    /// @return is onBroken done?
    public boolean onTermsBrokenTick(){
        return true;
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

    public boolean isSigned(){
        return signed;
    }

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
}
