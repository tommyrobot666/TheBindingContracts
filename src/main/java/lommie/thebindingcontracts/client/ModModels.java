package lommie.thebindingcontracts.client;

import lommie.thebindingcontracts.TheBindingContracts;
import net.minecraft.client.data.Model;
import net.minecraft.client.data.TextureKey;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ModModels {
    public static final Model CONTRACT = new Model(
            Optional.of(Identifier.of(TheBindingContracts.MOD_ID,"item/contract")),
            Optional.empty(), TextureKey.LAYER0);
}
