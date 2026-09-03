package com.panzer.mods.dice_and_delish.registry.world.damagesource;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class BlockDamageSource extends DamageSource {

    private final Block block;

    public BlockDamageSource(Holder<DamageType> type, Block block) {
        super(type);
        this.block = block;
    }

    @Override
    public @NotNull Component getLocalizedDeathMessage(@NotNull LivingEntity victim) {
        String msgId = this.typeHolder().unwrapKey()
                .map(key -> key.location().getNamespace() + "." + key.location().getPath())
                .orElse("generic");

        String baseKey = "death.attack." + msgId;
        LivingEntity killer = victim.getKillCredit();

        if (killer != null) {
            String playerKey = baseKey + ".player";
            return Component.translatable(playerKey, victim.getDisplayName(), this.block.getName(), killer.getDisplayName());
        }

        return Component.translatable(baseKey, victim.getDisplayName(), this.block.getName());
    }
}
