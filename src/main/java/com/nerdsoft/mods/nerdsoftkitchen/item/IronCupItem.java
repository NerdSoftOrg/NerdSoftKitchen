package com.nerdsoft.mods.nerdsoftkitchen.item;

import com.nerdsoft.mods.nerdsoftkitchen.item.component.IronCupContent;
import com.nerdsoft.mods.nerdsoftkitchen.registry.data.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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

//? if <1.21.2 {
/*import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.UseAnim;
*///?}

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
        return stack;
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack) {
        return isEmpty(stack) ? super.getMaxStackSize(stack) : 4;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        IronCupContent content = contentOf(stack);
        if (content == null) return Component.translatable(this.getDescriptionId());
        return Component.translatable("nerdsoftkitchen.iron_cup.filled_name",
                Component.translatable("nerdsoftkitchen.iron_cup_content." + content.getSerializedName()));
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player player,
                                                           @NotNull LivingEntity target,
                                                           @NotNull InteractionHand hand) {
        if (!isEmpty(stack)) return InteractionResult.PASS;
        if (!(target instanceof Cow) || target instanceof MushroomCow) {
            return InteractionResult.PASS;
        }

        @SuppressWarnings("resource") //! IGNORE RESOURCE WARNING
        Level level = player.level();

        player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);

        if (player.getAbilities().instabuild) {
            if (!level.isClientSide) {
                ItemStack filledCup = filled(this, IronCupContent.MILK);
                if (!player.getInventory().contains(filledCup)) {
                    player.getInventory().add(filledCup);
                }
            }
        } else if (stack.getCount() == 1) {
            stack.set(ModDataComponents.IRON_CUP_CONTENT.get(), IronCupContent.MILK);
            stack.set(DataComponents.FOOD, IronCupContent.MILK.food());
        } else {
            stack.shrink(1);
            if (!level.isClientSide) {
                ItemStack filledCup = filled(this, IronCupContent.MILK);
                if (!player.getInventory().add(filledCup)) {
                    player.drop(filledCup, false);
                }
            }
        }

        //? if <1.21.2 {
        /*return InteractionResult.sidedSuccess(level.isClientSide);
         *///?} else {
        return InteractionResult.SUCCESS;
        //?}
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level,
                                              @NotNull LivingEntity entity) {
        IronCupContent content = contentOf(stack);
        if (content == null) return stack;

        if (content == IronCupContent.MILK && entity instanceof Player player) {
            removePartialEffects(player);
        }
        if (entity instanceof Player player) {
            return giveEmptyCup(stack, player);
        }
        return stack;
    }

    private ItemStack giveEmptyCup(ItemStack stack, Player player) {
        if (player.getAbilities().instabuild) return stack;

        ItemStack emptyCup = new ItemStack(this);
        if (stack.getCount() == 1) {
            return emptyCup;
        }
        stack.shrink(1);
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

            int currentDuration = effect.getDuration();
            int reductionStep = Math.max(currentDuration / 3, 1);
            int newDuration = currentDuration - reductionStep;

            toRemove.add(effect.getEffect());

            if (newDuration > 0) {
                //? if <1.21.2 {
                /*MobEffectInstance newEffect = new MobEffectInstance(
                        effect.getEffect(),
                        newDuration,
                        effect.getAmplifier(),
                        effect.isAmbient(),
                        effect.isVisible(),
                        effect.showIcon()
                );
                newEffect.getCures().clear();
                newEffect.getCures().addAll(effect.getCures());
                toReapply.add(newEffect);
                *///?} else {
                MobEffectInstance newEffect = new MobEffectInstance(
                        effect.getEffect(),
                        newDuration,
                        effect.getAmplifier(),
                        effect.isAmbient(),
                        effect.isVisible(),
                        effect.showIcon()
                );
                toReapply.add(newEffect);
                //?}
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
            //? if <1.21.2 {
    /*public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return isEmpty(stack) ? UseAnim.NONE : UseAnim.DRINK;
    }
    *///?} else {
    public @NotNull ItemUseAnimation getUseAnimation(@NotNull ItemStack stack) {
        return isEmpty(stack) ? ItemUseAnimation.NONE : ItemUseAnimation.DRINK;
    }
    //?}

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return isEmpty(stack) ? 0 : 32;
    }

    @Override
            //? if <1.21.2 {
    /*public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player,
                                                           @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (isEmpty(stack)) {
            return InteractionResultHolder.pass(stack);
        }
        return ItemUtils.startUsingInstantly(level, player, hand);
    }
    *///?} else {
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player,
                                          @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (isEmpty(stack)) {
            return InteractionResult.PASS;
        }
        return ItemUtils.startUsingInstantly(level, player, hand);
    }
    //?}
}