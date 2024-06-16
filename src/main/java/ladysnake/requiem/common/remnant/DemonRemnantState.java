/*
 * Requiem
 * Copyright (C) 2017-2024 Ladysnake
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses>.
 *
 * Linking this mod statically or dynamically with other
 * modules is making a combined work based on this mod.
 * Thus, the terms and conditions of the GNU General Public License cover the whole combination.
 *
 * In addition, as a special exception, the copyright holders of
 * this mod give you permission to combine this mod
 * with free software programs or libraries that are released under the GNU LGPL
 * and with code included in the standard release of Minecraft under All Rights Reserved (or
 * modified versions of such code, with unchanged license).
 * You may copy and distribute such a system following the terms of the GNU GPL for this mod
 * and the licenses of the other code concerned.
 *
 * Note that people who make modified versions of this mod are not obligated to grant
 * this special exception for their modified versions; it is their choice whether to do so.
 * The GNU General Public License gives permission to release a modified version without this exception;
 * this exception also makes it possible to release a modified version which carries forward this exception.
 */
package ladysnake.requiem.common.remnant;

import com.google.common.base.Preconditions;
import ladysnake.requiem.api.v1.remnant.RemnantComponent;
import ladysnake.requiem.common.advancement.criterion.RequiemCriteria;
import ladysnake.requiem.common.entity.effect.AttritionStatusEffect;
import ladysnake.requiem.common.entity.effect.RequiemStatusEffects;
import ladysnake.requiem.common.network.RequiemNetworking;
import ladysnake.requiem.core.remnant.MutableRemnantState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.WorldEvents;

public class DemonRemnantState extends MutableRemnantState {
    public DemonRemnantState(PlayerEntity player) {
        super(player);
    }

    @Override
    protected void regenerateBody(LivingEntity body) {
        Preconditions.checkState(!this.player.getWorld().isClient);
        RequiemNetworking.sendBodyCureMessage(player);
        RemnantComponent.get(player).setVagrant(false);
        RequiemCriteria.TRANSFORMED_POSSESSED_ENTITY.handle((ServerPlayerEntity) player, body, player, true);
        body.remove(Entity.RemovalReason.DISCARDED);
        player.removeStatusEffect(RequiemStatusEffects.ATTRITION);
        player.setHealth(body.getHealth());
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0));
        player.getWorld().syncWorldEvent(null, WorldEvents.ZOMBIE_VILLAGER_CURED, player.getBlockPos(), 0);
    }

    @Override
    public void curePossessed(LivingEntity body) {
        super.curePossessed(body);
        AttritionStatusEffect.reduce(this.player, Integer.MAX_VALUE);
    }

    @Override
    protected MobEntity cureMob(LivingEntity body) {
        MobEntity cured = super.cureMob(body);
        if (cured != null) {
            RequiemCriteria.TRANSFORMED_POSSESSED_ENTITY.handle((ServerPlayerEntity) this.player, body, cured, true);
        }
        return cured;
    }

    @Override
    public boolean canSplit(boolean forced) {
        return forced || this.player.hasStatusEffect(RequiemStatusEffects.EMANCIPATION);
    }

    @Override
    public boolean canDissociateFrom(MobEntity possessed) {
        return super.canDissociateFrom(possessed)
            || this.player.hasStatusEffect(RequiemStatusEffects.EMANCIPATION);
    }

    @Override
    protected void onRespawnAfterDeath() {
        AttritionStatusEffect.apply(player);
    }
}
