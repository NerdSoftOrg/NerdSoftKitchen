package com.nerdsoft.mods.nerdsoftkitchen.item;

import com.nerdsoft.mods.nerdsoftkitchen.blockentity.SkilletBlockEntity;
import com.nerdsoft.mods.nerdsoftkitchen.item.component.SkilletHotState;
import com.nerdsoft.mods.nerdsoftkitchen.registry.data.ModDataComponents;
import com.nerdsoft.mods.nerdsoftkitchen.registry.sound.ModSounds;
import com.nerdsoft.mods.nerdsoftkitchen.util.RandomUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class SkilletBlockItem extends BlockItem {

    private static final ResourceLocation BASE_ATTACK_DAMAGE_ID =
            ResourceLocation.withDefaultNamespace("base_attack_damage");
    private static final ResourceLocation BASE_ATTACK_SPEED_ID =
            ResourceLocation.withDefaultNamespace("base_attack_speed");

    private static final float ATTACK_DAMAGE_DELTA = 6.0F - 1.0F;
    private static final float ATTACK_SPEED_DELTA = 1.2F - 4.0F;

    private static final double HOT_SKILLET_KNOCKBACK = 0.7;
    private static final float HOT_SKILLET_FIRE_SECONDS = 4.0F;
    private static final float CLANG_VOLUME = 1.0F;
    private static final float CLANG_BASE_PITCH = 0.9F;
    private static final float CLANG_PITCH_VARIANCE = 0.15F;

    private static final ItemAttributeModifiers ATTRIBUTES = ItemAttributeModifiers.builder()
            .add(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(BASE_ATTACK_DAMAGE_ID, ATTACK_DAMAGE_DELTA, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND)
            .add(Attributes.ATTACK_SPEED,
                    new AttributeModifier(BASE_ATTACK_SPEED_ID, ATTACK_SPEED_DELTA, AttributeModifier.Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND)
            .build();

    public SkilletBlockItem(Block block, Properties properties) {
        super(block, properties.attributes(ATTRIBUTES));
    }

    public static boolean isHot(ItemStack stack, Level level) {
        SkilletHotState hotState = stack.get(ModDataComponents.SKILLET_HOT_STATE.get());
        return hotState != null && hotState.isActiveAt(level.getGameTime());
    }

    public static void pickupHotState(ItemStack stack, SkilletBlockEntity entity, Level level) {
        if (!entity.isHotEligible()) {
            return;
        }
        stack.set(ModDataComponents.SKILLET_HOT_STATE.get(), new SkilletHotState(entity.computeHotUntilTick(level)));
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity attacker, @NotNull LivingEntity target) {
        Level level = attacker.level();

        level.playSound(null, target.blockPosition(), ModSounds.SKILLET_CLANG.get(), SoundSource.PLAYERS,
                CLANG_VOLUME, RandomUtil.jitteredPitch(level.getRandom(), CLANG_BASE_PITCH, CLANG_PITCH_VARIANCE));

        double dx = target.getX() - attacker.getX();
        double dz = target.getZ() - attacker.getZ();
        target.knockback(HOT_SKILLET_KNOCKBACK, -dx, -dz);

        if (!level.isClientSide && isHot(stack, level)) {
            target.igniteForSeconds(HOT_SKILLET_FIRE_SECONDS);
        }

        return true;
    }

    @Override
    public void postHurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        stack.hurtAndBreak(1, target, EquipmentSlot.MAINHAND);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(@NotNull BlockPos pos, @NotNull Level level,
                                                 @Nullable Player player, @NotNull ItemStack stack,
                                                 @NotNull BlockState state) {
        boolean superResult = super.updateCustomBlockEntityTag(pos, level, player, stack, state);

        if (!level.isClientSide && stack.isDamaged()) {
            if (level.getBlockEntity(pos) instanceof SkilletBlockEntity skilletEntity) {
                skilletEntity.setDamage(stack.getDamageValue());
            }
        }

        return superResult;
    }
}