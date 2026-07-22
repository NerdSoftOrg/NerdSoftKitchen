package com.nerdsoft.mods.nerdsoftkitchen.item;

import com.nerdsoft.mods.nerdsoftkitchen.item.component.IronCupContent;
import com.nerdsoft.mods.nerdsoftkitchen.registry.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class IronCupItem extends Item {

    public IronCupItem(Properties properties) {
        super(properties);
    }

    @Nullable
    public static IronCupContent contentOf(ItemStack stack) {
        return stack.get(ModDataComponents.IRON_CUP_CONTENT.get());
    }

    public static boolean isEmpty(ItemStack stack) {
        return contentOf(stack) == null;
    }

    public static ItemStack filled(Item cupItem, IronCupContent content) {
        ItemStack stack = new ItemStack(cupItem);
        stack.set(ModDataComponents.IRON_CUP_CONTENT.get(), content);
        stack.set(DataComponents.FOOD, content.food());
        stack.set(DataComponents.MAX_STACK_SIZE, 1);
        return stack;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        IronCupContent content = contentOf(stack);
        if (content == null) return Component.translatable(this.getDescriptionId(stack));
        return Component.translatable("nerdsoftkitchen.iron_cup.filled_name",
                Component.translatable("nerdsoftkitchen.iron_cup_content." + content.getSerializedName()));
    }

    @SuppressWarnings("resource")
    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player player,
                                                             @NotNull LivingEntity target,
                                                             @NotNull InteractionHand hand) {
        if (!isEmpty(stack)) return InteractionResult.PASS;
        if (!(target instanceof Cow) || target instanceof MushroomCow) {
            return InteractionResult.PASS;
        }
        if (player.level().isClientSide) return InteractionResult.CONSUME;
        ItemStack filledCup = filled(this, IronCupContent.MILK);
        ItemStack result = ItemUtils.createFilledResult(stack, player, filledCup);
        player.setItemInHand(hand, result);
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level,
                                               @NotNull LivingEntity entity) {
        IronCupContent content = contentOf(stack);
        if (content == null) return stack;
        if (entity instanceof Player player && !player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        if (content == IronCupContent.MILK && entity instanceof Player player) {
            removePartialEffects(player);
        }
        if (entity instanceof Player player) {
            return giveEmptyCup(stack, player);
        }
        return stack;
    }

    private ItemStack giveEmptyCup(ItemStack stack, Player player) {
        ItemStack emptyCup = new ItemStack(this);
        if (stack.getCount() <= 1) {
            return player.getAbilities().instabuild ? stack : emptyCup;
        }
        if (!player.getInventory().add(emptyCup)) {
            player.drop(emptyCup, false);
        }
        return stack;
    }

    private void removePartialEffects(Player player) {
        List<MobEffectInstance> toReapply = new ArrayList<>();
        List<Holder<MobEffect>> toRemove = new ArrayList<>();
        for (MobEffectInstance effect : player.getActiveEffectsMap().values()) {
            if (effect.getEffect().value().isInstantenous()) continue;
            int reducedDuration = effect.getDuration() - Math.max(effect.getDuration() / 3, 1);
            toRemove.add(effect.getEffect());
            if (reducedDuration > 0) {
                toReapply.add(new MobEffectInstance(effect.getEffect(), reducedDuration, effect.getAmplifier(),
                        effect.isAmbient(), effect.isVisible(), effect.showIcon()));
            }
        }
        toRemove.forEach(player::removeEffect);
        toReapply.forEach(player::addEffect);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context,
                                 @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        if (!flag.isAdvanced()) return;
        IronCupContent content = contentOf(stack);
        if (content != null) {
            tooltip.add(Component.translatable("nerdsoftkitchen.iron_cup_content.contains",
                    Component.translatable("nerdsoftkitchen.iron_cup_content." + content.getSerializedName()))
                    .withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return isEmpty(stack) ? UseAnim.NONE : UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return isEmpty(stack) ? 0 : 32;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player,
                                                             @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (isEmpty(stack)) {
            return InteractionResultHolder.pass(stack);
        }
        return ItemUtils.startUsingInstantly(level, player, hand);
    }
}