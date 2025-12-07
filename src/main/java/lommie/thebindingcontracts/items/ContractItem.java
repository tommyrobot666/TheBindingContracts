package lommie.thebindingcontracts.items;

import lommie.thebindingcontracts.contract.Contract;
import lommie.thebindingcontracts.contract.TermsAndConditions;
import lommie.thebindingcontracts.contract.TermsAndConditionsType;
import lommie.thebindingcontracts.data.ContractsPersistentState;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class ContractItem extends Item {
    public static final int SIGNATURES_TOOLTIP_MAX_CHAR_PER_LINE = 150;

    public ContractItem(Settings settings) {
        super(settings);
    }

    @SuppressWarnings("unused")
    public ContractItem(Settings settings, List<TermsAndConditions> startingTerms) {
        super(settings.component(ModItemComponents.TERMS_TO_ADD_ON_NEXT_TICK, startingTerms));
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) return ActionResult.PASS;
        ItemStack stack = user.getStackInHand(hand);

        // change selected term
        int selectedTerm = stack.getOrDefault(ModItemComponents.SELECTED_TERM,0);
        if (user.isSneaking()) {
            List<Identifier> termIds = stack.getOrDefault(ModItemComponents.TERMS,List.of());
            if (!termIds.isEmpty()) {
                List<TermsAndConditionsType> termTypes = TermsAndConditions.getTypesFromIds(termIds);
                List<Integer> termsWithActions = TermsAndConditions.getTermsWithActionsAndIndex(termTypes).keySet().stream().toList();
                int i = termsWithActions.indexOf(selectedTerm);
                i = i > termsWithActions.size() ? 0 : ++i;
                selectedTerm = termsWithActions.get(i);
                stack.set(ModItemComponents.SELECTED_TERM,selectedTerm);
            }
        }

        // call contract functions
        Contract contract = getContract(stack, (ServerWorld) world);
        if (contract.isValidAndSigned()) {
            contract.onUseItem(selectedTerm, world, user, hand);
        }

        // add self to contract when used
        if (hand == Hand.OFF_HAND) return ActionResult.PASS;
        ItemStack stackInOtherHand = user.getStackInHand(Hand.OFF_HAND);
        if (canAddPlayerToContract(contract,user.getUuid())){
            addPlayerToContract(stack,contract,user,world);
            return ActionResult.SUCCESS;
        }
        // seal contract
        if (stackInOtherHand.isOf(ModItems.WAX_SEAL) && contract.isValid()){
            stackInOtherHand.decrement(1);
            contract.sign();
            playSoundToAllSigners(world,contract,SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    private void playSoundToAllSigners(World world, Contract contract, SoundEvent sound) {
        for (UUID signer : contract.getSigners()) {
            PlayerEntity player = world.getPlayerAnyDimension(signer);
            if (player == null) {continue;}
            world.playSound(null, player.getBlockPos(), sound, SoundCategory.PLAYERS);
        }
    }

    protected void addPlayerToContract(ItemStack stack, Contract contract, PlayerEntity player, World world) {
        contract.addSigner(player.getUuid());
        ArrayList<String> signatures = new ArrayList<>(stack.getOrDefault(ModItemComponents.SIGNATURES,List.of()));
        signatures.add(player.getStringifiedName());
        stack.set(ModItemComponents.SIGNATURES,signatures);
        playSoundToAllSigners(world,contract,SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE);
    }

    private boolean canAddPlayerToContract(Contract contract, UUID player){
        return !contract.getSigners().contains(player)&&contract.isUnfinished();
    }

    @SuppressWarnings({"deprecation", "ExtractMethodRecommender"})
    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        if (stack.getOrDefault(ModItemComponents.BROKEN, false)) {
            textConsumer.accept(Text.literal("BROKEN").setStyle(Style.EMPTY.withColor(Formatting.DARK_RED)));
            if (type.isAdvanced()){
                textConsumer.accept(Text.literal("Contract Id: "+stack.getOrDefault(ModItemComponents.CONTRACT_ID,UUID.randomUUID())).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
            }
            return;
        }

        textConsumer.accept(Text.literal("Terms:").formatted(Formatting.GRAY));
        int selectedTerm = stack.getOrDefault(ModItemComponents.SELECTED_TERM,0);
        List<Identifier> termIds = stack.getOrDefault(ModItemComponents.TERMS,List.of());
        List<TermsAndConditionsType> termTypes = TermsAndConditions.getTypesFromIds(termIds);
        for (int i = 0; i < termTypes.size(); i++) {
            TermsAndConditionsType termType = termTypes.get(i);
            MutableText prefix = !termType.typeHasAction() ? Text.empty() :
                    selectedTerm == i ? Text.literal("@> ").formatted(Formatting.BOLD,Formatting.GOLD)
                            : Text.literal("@ ");
            prefix.append(termType.typeGetDisplayName());
            textConsumer.accept(prefix);
        }

        textConsumer.accept(Text.empty());
        List<String> signatures = stack.getOrDefault(ModItemComponents.SIGNATURES,List.of());
        if (signatures.isEmpty()) {
            textConsumer.accept(Text.literal("No signatures").formatted(Formatting.RED));
        } else {
            textConsumer.accept(Text.literal("Signed by:").formatted(Formatting.GRAY));
            StringBuilder stringBuilder = new StringBuilder();
            int lineLength = 0;
            for (String name : signatures) {
                lineLength += name.length();
                if (lineLength > SIGNATURES_TOOLTIP_MAX_CHAR_PER_LINE){
                    stringBuilder.append("\n");
                    lineLength = 0;
                }
                stringBuilder.append(name);
                stringBuilder.append(", ");
            }
            stringBuilder.delete(stringBuilder.length()-1,stringBuilder.length());
            textConsumer.accept(Text.literal(stringBuilder.toString()));
        }

        if (type.isAdvanced()){
            textConsumer.accept(Text.literal("Contract Id: "+stack.getOrDefault(ModItemComponents.CONTRACT_ID,UUID.randomUUID())).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        }
    }

    public static Contract getContract(ItemStack stack, ServerWorld world){
        return Objects.requireNonNull(ContractsPersistentState.getAContractInWorldAndDirty(world,
                getContractIdOrCreate(stack, world)));
    }

    public static Contract getContractNoDirty(ItemStack stack, ServerWorld world){
        return Objects.requireNonNull(ContractsPersistentState.getAContractInWorld(world,
                getContractIdOrCreate(stack, world)));
    }

    public static UUID getContractIdOrCreate(ItemStack stack, ServerWorld world){
        if (stack.hasChangedComponent(ModItemComponents.CONTRACT_ID)) {
            return Objects.requireNonNull(stack.get(ModItemComponents.CONTRACT_ID));
        } else {
            HashMap<UUID,Contract> contracts = ContractsPersistentState.getContractsInWorld(world);
            UUID newContractId = UUID.randomUUID();
            contracts.put(newContractId, new Contract());
            stack.set(ModItemComponents.CONTRACT_ID, newContractId);
            return newContractId;
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
        if (stack.hasChangedComponent(ModItemComponents.TERMS_TO_ADD_ON_NEXT_TICK)) {
            List<TermsAndConditions> termsToAddOnNextTick = stack.get(ModItemComponents.TERMS_TO_ADD_ON_NEXT_TICK);
            Contract contract = getContract(stack, world);
            assert termsToAddOnNextTick != null;
            for (TermsAndConditions term : termsToAddOnNextTick) {
                contract.addTerm(term);
            }
            stack.remove(ModItemComponents.TERMS_TO_ADD_ON_NEXT_TICK);

            playSoundToAllSigners(world,contract,SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT);
        }

        updateItemComponents(stack, world);
    }

    private static void updateItemComponents(ItemStack stack, ServerWorld world) {
        if (!stack.hasChangedComponent(ModItemComponents.CONTRACT_ID)) return;
        // check contract state
        Contract contract = getContractNoDirty(stack, world);

        if (contract.isBroken() && !stack.hasChangedComponent(ModItemComponents.BROKEN)){
            stack.set(ModItemComponents.BROKEN,true);
            return;
        }

        if (contract.isValid() && !stack.hasChangedComponent(ModItemComponents.VALID)){
            stack.set(ModItemComponents.VALID,true);
        }

        if (contract.isValidAndSigned() && !stack.hasChangedComponent(ModItemComponents.SIGNED)){
            stack.set(ModItemComponents.SIGNED,true);
        }

        if (contract.getSigners().size() != stack.getOrDefault(ModItemComponents.SIGNATURES,List.of()).size()){
            ArrayList<String> signatures = new ArrayList<>();
            for (UUID signer : contract.getSigners()) {
                Optional<PlayerEntity> player = Optional.ofNullable(world.getPlayerAnyDimension(signer));
                if (player.isPresent()) {
                    signatures.add(player.orElseThrow().getStringifiedName());
                } else {
                    signatures.add(signer.toString());
                }
            }
            stack.set(ModItemComponents.SIGNATURES,signatures);
        }

        if (contract.getTermsSize() != stack.getOrDefault(ModItemComponents.TERMS,List.of()).size()){
            stack.set(ModItemComponents.TERMS,contract.getTermIds());
        }
    }
}
