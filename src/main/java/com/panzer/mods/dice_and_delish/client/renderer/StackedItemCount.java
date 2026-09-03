package com.panzer.mods.dice_and_delish.client.renderer;

import net.minecraft.world.item.ItemStack;

public final class StackedItemCount {

    static final int FIRST_TIER_ITEMS = 6;
    private static final int MIN_STACK_SIZE_FOR_PERCENT = 17;
    private static final float EXTRA_ITEM_PERCENT = 0.20F;

    private StackedItemCount() {
    }

    public static int countFor(ItemStack stack) {
        int maxStackSize = stack.getMaxStackSize();
        int count = stack.getCount();

        if (maxStackSize <= 1 || count <= FIRST_TIER_ITEMS) {
            return Math.clamp(count, 1, FIRST_TIER_ITEMS);
        }

        if (maxStackSize < MIN_STACK_SIZE_FOR_PERCENT) {
            return FIRST_TIER_ITEMS;
        }

        int remainder = count - FIRST_TIER_ITEMS;
        int stepSize = Math.max(1, Math.round(maxStackSize * EXTRA_ITEM_PERCENT));
        int extra = remainder / stepSize;

        return FIRST_TIER_ITEMS + extra;
    }
}
