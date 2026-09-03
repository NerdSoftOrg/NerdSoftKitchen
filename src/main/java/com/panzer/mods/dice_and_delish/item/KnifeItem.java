package com.panzer.mods.dice_and_delish.item;

import net.minecraft.world.item.SwordItem;

//? if <1.21.2 {
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.Tier;
//?} else {
/*import net.minecraft.world.item.ToolMaterial;
 *///?}

public class KnifeItem extends SwordItem {

    //? if <1.21.2 {
    private static final ResourceLocation BASE_ATTACK_DAMAGE_ID =
            ResourceLocation.withDefaultNamespace("base_attack_damage");
    private static final ResourceLocation BASE_ATTACK_SPEED_ID =
            ResourceLocation.withDefaultNamespace("base_attack_speed");

    public KnifeItem(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, properties.attributes(createPreciseAttributes(tier, attackDamage, attackSpeed)));
    }

    private static ItemAttributeModifiers createPreciseAttributes(Tier tier, float attackDamage, float attackSpeed) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, tier.getAttackDamageBonus() + attackDamage, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, attackSpeed, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }
    //?} else {
    /*public KnifeItem(ToolMaterial material, float attackDamage, float attackSpeed, Properties properties) {
        super(material, attackDamage, attackSpeed, properties);
    }
    *///?}
}
