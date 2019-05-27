package quickcarpet.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import quickcarpet.utils.CarpetRegistry;

import java.util.Map;

@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin extends BlockWithEntity {
    @Shadow @Final private static Map<Item, DispenserBehavior> BEHAVIORS;

    protected DispenserBlockMixin(Settings block$Settings_1) {
        super(block$Settings_1);
    }

    @Overwrite
    public DispenserBehavior getBehaviorForItem(ItemStack stack) {
        Item item = stack.getItem();
        if (quickcarpet.settings.Settings.dispensersPlaceBlocks && !BEHAVIORS.containsKey(item) && item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();
            if (CarpetRegistry.DISPENSER_PLACEABLE.contains(block)) return CarpetRegistry.PLACE_BLOCK_DISPENSER_BEHAVIOR;
        }
        return BEHAVIORS.get(stack.getItem());
    }
}