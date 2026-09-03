package com.panzer.mods.dice_and_delish.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class CreativeOnlyBlockItem extends BlockItem {

    public CreativeOnlyBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null || !player.getAbilities().instabuild) {
            return InteractionResult.PASS;
        }
        return super.useOn(context);
    }
}
