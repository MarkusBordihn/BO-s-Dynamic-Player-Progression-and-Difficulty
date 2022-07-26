/**
 * Copyright 2022 Markus Bordihn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.markusbordihn.minecraft.dynamicplayerprogressionplayerdifficulty.player;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;

import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import de.markusbordihn.minecraft.dynamicplayerprogressionplayerdifficulty.Constants;
import de.markusbordihn.minecraft.dynamicplayerprogressionplayerdifficulty.data.PlayerData;
import de.markusbordihn.minecraft.dynamicplayerprogressionplayerdifficulty.data.PlayerDataManager;

@EventBusSubscriber
public class PlayerDamageManager {

  protected static final Logger log = LogManager.getLogger(Constants.LOG_NAME);

  protected PlayerDamageManager() {}

  @SubscribeEvent(priority = EventPriority.LOW)
  public static void handleLivingDamageEvent(LivingDamageEvent event) {
    DamageSource damageSource = event.getSource();

    // We only care about damage caused by server players for now.
    if (damageSource.getEntity() instanceof ServerPlayer serverPlayer) {
      PlayerData playerData = PlayerDataManager.getPlayer(serverPlayer);
      if (playerData != null) {
        float lastAttackDamage = event.getAmount();
        float attackDamage = lastAttackDamage;
        if (event.getEntity() instanceof ServerPlayer) {
          if (playerData.getDealtDamageAdjustmentPlayer() > 0.0f) {
            attackDamage = attackDamage * playerData.getDealtDamageAdjustmentPlayer();
            log.debug("[Player Dealt Damage] {} from {} by factor {} to {}", serverPlayer,
                lastAttackDamage, playerData.getDealtDamageAdjustmentPlayer(), attackDamage);
          }
        } else {
          if (playerData.getDealtDamageAdjustmentMob() > 0.0f) {
            attackDamage = attackDamage * playerData.getDealtDamageAdjustmentMob();
            log.debug("[Dealt Damage] {} from {} by factor {} to {}", serverPlayer,
                lastAttackDamage, playerData.getDealtDamageAdjustmentMob(), attackDamage);
          }
        }

        // Adjust attack damage, if there is any different
        if (event.getAmount() != attackDamage) {
          event.setAmount(attackDamage);
        }
      }
    }
  }

}
