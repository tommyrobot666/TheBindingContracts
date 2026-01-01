package lommie.thebindingcontracts.client;

import net.minecraft.client.data.Model;
import net.minecraft.client.data.TextureKey;
import net.minecraft.util.Identifier;

import java.util.Optional;

/***/
public class ModModels {
    public static final Model CONTRACT = new Model(
            Optional.of(Identifier.ofVanilla("item/generated")),
            Optional.empty(), TextureKey.LAYER0);
}
