package com.nerdsoft.mods.nerdsoftkitchen.item;

import com.nerdsoft.mods.nerdsoftkitchen.blockentity.SkilletBlockEntity;
import com.nerdsoft.mods.nerdsoftkitchen.item.component.SkilletHotState;
import com.nerdsoft.mods.nerdsoftkitchen.registry.data.ModDataComponents;
import com.nerdsoft.mods.nerdsoftkitchen.registry.sound.ModSounds;
import com.nerdsoft.mods.nerdsoftkitchen.util.RandomUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

/**
 * Item form of the Skillet: places {@link com.nerdsoft.mods.nerdsoftkitchen.block.SkilletBlock}
 * like any other {@link BlockItem}, but also functions as a heavy melee weapon.
 * <p>
 * Combat profile: base attack damage/speed sit between a stone axe and an iron sword (heavier
 * hit, slower swing than a sword), and every successful hit applies extra knockback plus a
 * metallic clang sound instead of the default weapon-hit sound. If the stack carries an active
 * {@link SkilletHotState} (acquired by picking the block up while it was lit/cooking - see
 * {@link #pickupHotState}), attacks also ignite the target for a few seconds, simulating
 * branding the target with a red-hot pan.
 * <p>
 * Placing the block back down always starts cold: {@code SkilletBlock#getStateForPlacement}
 * re-derives {@code LIT} from whatever is beneath the placement position, independent of
 * whatever hot state the item stack itself carried.
 */
public class SkilletBlockItem extends BlockItem {

    private static final ResourceLocation BASE_ATTACK_DAMAGE_ID =
            ResourceLocation.withDefaultNamespace("base_attack_damage");
    private static final ResourceLocation BASE_ATTACK_SPEED_ID =
            ResourceLocation.withDefaultNamespace("base_attack_speed");

    // Between a stone axe (9 dmg / 0.8 speed) and an iron sword (6 dmg / 1.6 speed): a heavy,
    // slower-swinging blunt weapon, per the "club/axe style" requirement. Vanilla's base
    // ATTACK_DAMAGE/ATTACK_SPEED attribute values are 1.0/4.0 respectively; these constants are
    // the ADD_VALUE deltas on top of that base (mirroring KnifeItem's convention), chosen so the
    // final resolved values equal 6.0 damage / 1.2 speed as specified.
    private static final float ATTACK_DAMAGE_DELTA = 6.0F - 1.0F;
    private static final float ATTACK_SPEED_DELTA = 1.2F - 4.0F;

    // Knockback strength above SwordItem's default melee push (vanilla base ~0.4).
    private static final double HOT_SKILLET_KNOCKBACK = 0.7;
    private static final float HOT_SKILLET_FIRE_SECONDS = 4.0F;
    private static final float CLANG_VOLUME = 1.0F;
    private static final float CLANG_BASE_PITCH = 0.9F;
    private static final float CLANG_PITCH_VARIANCE = 0.15F;

    // Stateless and identical for every SkilletBlockItem instance (there is only ever one, but
    // this also keeps the attribute set from being rebuilt if the item is ever re-constructed,
    // e.g. by a datapack override) - computed once rather than per-instance.
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

    /**
     * @return {@code true} if this stack currently carries an unexpired "Hot Skillet" state.
     */
    public static boolean isHot(ItemStack stack, Level level) {
        SkilletHotState hotState = stack.get(ModDataComponents.SKILLET_HOT_STATE.get());
        return hotState != null && hotState.isActiveAt(level.getGameTime());
    }

    /**
     * Called from {@link com.nerdsoft.mods.nerdsoftkitchen.block.SkilletBlock} when a lit/
     * cooking skillet is mined, stamping the "Hot Skillet" component onto the resulting stack
     * so subsequent attacks apply burn damage until the state expires.
     */
    public static void pickupHotState(ItemStack stack, SkilletBlockEntity entity, Level level) {
        if (!entity.isHotEligible()) {
            return;
        }
        stack.set(ModDataComponents.SKILLET_HOT_STATE.get(), new SkilletHotState(entity.computeHotUntilTick(level)));
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity attacker, @NotNull LivingEntity target) {
        Level level = attacker.level();

        // Distinct metallic clang instead of the default weapon-hit sound - played on every hit
        // regardless of hot state, since it represents the pan's physical material. hurtEnemy
        // only ever runs server-side in vanilla's attack flow, so no isClientSide guard is
        // needed here (broadcasting from a null source player is itself the standard pattern).
        level.playSound(null, target.blockPosition(), ModSounds.SKILLET_CLANG.get(), SoundSource.PLAYERS,
                CLANG_VOLUME, RandomUtil.jitteredPitch(level.getRandom(), CLANG_BASE_PITCH, CLANG_PITCH_VARIANCE));

        // Heavier knockback than a standard sword, independent of hot state - the pan's mass
        // pushes harder on every hit.
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
}