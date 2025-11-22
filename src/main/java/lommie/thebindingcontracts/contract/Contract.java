package lommie.thebindingcontracts.contract;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lommie.thebindingcontracts.items.ContractItem;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.UUID;

public class Contract {
    public static final Codec<Contract> CODEC = RecordCodecBuilder.create((instance) ->
        instance.group(Codec)
    );

    private final ArrayList<TermsAndConditions> terms = new ArrayList<>();
    private final ArrayList<NbtComponent> termsSavedData = new ArrayList<>();
    private final ArrayList<UUID> signers = new ArrayList<>();
    public boolean signed = false;

    public Contract(){}

    private Contract(ArrayList<TermsAndConditions> terms, ArrayList<UUID> signers, boolean signed ){
        this.terms.addAll(terms);
        this.signers.addAll(signers);
        this.signed = signed;
    }

    public void onUseItem(int selectedTerm, World world, PlayerEntity user, Hand hand) {
        if(!signers.contains(user.getUuid())) return;
        if(world.isClient()) return;
        ItemStack stack = user.getStackInHand(hand);
        ItemStack stackInOtherHand = inOtherHand(user, hand);
        UUID otherId = ContractItem.getOtherPlayer(stack, user.getUuid());
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

    public void onTermsBroken(){}

    public void addTerm(TermsAndConditions term){
        terms.add(term);
    }

    public void addSigner(UUID uuid){
        signers.add(uuid);
    }

    public void sign(){
        signed = true;
    }
}
