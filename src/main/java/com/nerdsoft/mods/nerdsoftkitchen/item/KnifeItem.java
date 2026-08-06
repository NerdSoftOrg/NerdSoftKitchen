package com.nerdsoft.mods.nerdsoftkitchen.item;

import net.minecraft.world.item.SwordItem;

//? if <1.21.2 {
import net.minecraft.world.item.Tier;
//?} else {
/*import net.minecraft.world.item.ToolMaterial;
 *///?}

public class KnifeItem extends SwordItem {

    //? if <1.21.2 {
    public KnifeItem(Tier tier, float attackDamage, float attackSpeed, Properties properties) {
        super(tier, properties.attributes(SwordItem.createAttributes(tier, (int) attackDamage, attackSpeed)));
    }
    //?} else {
    /*public KnifeItem(ToolMaterial material, float attackDamage, float attackSpeed, Properties properties) {
        super(material, attackDamage, attackSpeed, properties);
    }
    *///?}
}