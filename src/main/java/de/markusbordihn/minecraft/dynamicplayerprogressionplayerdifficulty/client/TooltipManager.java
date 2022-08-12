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

package de.markusbordihn.minecraft.dynamicplayerprogressionplayerdifficulty.client;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import de.markusbordihn.minecraft.dynamicplayerprogressionplayerdifficulty.Constants;
import de.markusbordihn.minecraft.dynamicplayerprogressionplayerdifficulty.data.Experience;
import de.markusbordihn.minecraft.dynamicplayerprogressionplayerdifficulty.data.PlayerData;
import de.markusbordihn.minecraft.dynamicplayerprogressionplayerdifficulty.data.PlayerDataManager;

@EventBusSubscriber(value = Dist.CLIENT)
public class TooltipManager {

  protected static final Logger log = LogManager.getLogger(Constants.LOG_NAME);

  protected TooltipManager() {}

  @SubscribeEvent
  public static void onItemTooltipEvent(ItemTooltipEvent event) {

    ItemStack itemStack = event.getItemStack();
    if (itemStack.isEmpty()) {
      return;
    }

    Item item = itemStack.getItem();
    List<Component> tooltip = event.getToolTip();
    PlayerData playerData = PlayerDataManager.getLocalPlayer();

    // Sword item damage
    if (PlayerDataManager.isSwordItem(item)) {
      if (Experience.getItemDamageIncreaseSword() > 0.0f) {
        tooltip.add(1, formatWeaponClass("sword"));
        if (playerData != null) {
          tooltip.add(2, formatLevel(playerData.getItemLevelSword()));
          if (playerData.getItemDamageAdjustmentSword() > 0.0f) {
            tooltip.add(3, formatAttackDamage(playerData.getItemDamageAdjustmentSword()));
          }
        }
      }
    }

    // Axe item damage
    else if (PlayerDataManager.isAxeItem(item)) {
      if (Experience.getItemDamageIncreaseAxe() > 0.0f) {
        tooltip.add(1, formatWeaponClass("axe"));
        if (playerData != null) {
          tooltip.add(2, formatLevel(playerData.getItemLevelAxe()));
          if (playerData.getItemDamageAdjustmentAxe() > 0.0f) {
            tooltip.add(3, formatAttackDamage(playerData.getItemDamageAdjustmentAxe()));
          }
        }
      }
    }

    // Bow item damage
    else if (PlayerDataManager.isBowItem(item)) {
      if (Experience.getItemDamageIncreaseBow() > 0.0f) {
        tooltip.add(1, formatWeaponClass("bow"));
        if (playerData != null) {
          tooltip.add(2, formatLevel(playerData.getItemLevelBow()));
          if (playerData.getItemDamageAdjustmentBow() > 0.0f) {
            tooltip.add(3, formatAttackDamage(playerData.getItemDamageAdjustmentBow()));
          }
        }
      }
    }

    // Crossbow item damage
    else if (PlayerDataManager.isCrossbowItem(item)) {
      if (Experience.getItemDamageIncreaseCrossbow() > 0.0f) {
        tooltip.add(1, formatWeaponClass("crossbow"));
        if (playerData != null) {
          tooltip.add(2, formatLevel(playerData.getItemLevelCrossbow()));
          if (playerData.getItemDamageAdjustmentCrossbow() > 0.0f) {
            tooltip.add(3, formatAttackDamage(playerData.getItemDamageAdjustmentCrossbow()));
          }
        }
      }
    }

    // Pickaxe item damage
    else if (PlayerDataManager.isPickaxeItem(item)) {
      if (Experience.getItemDamageIncreasePickaxe() > 0.0f) {
        tooltip.add(1, formatWeaponClass("pickaxe"));
        if (playerData != null) {
          tooltip.add(2, formatLevel(playerData.getItemLevelPickaxe()));
          if (playerData.getItemDamageAdjustmentPickaxe() > 0.0f) {
            tooltip.add(3, formatAttackDamage(playerData.getItemDamageAdjustmentPickaxe()));
          }
        }
      }
    }

    // Shield item damage
    else if (PlayerDataManager.isShieldItem(item)) {
      if (Experience.getItemDamageIncreaseShield() > 0.0f) {
        tooltip.add(1, formatWeaponClass("shield"));
        if (playerData != null) {
          tooltip.add(2, formatLevel(playerData.getItemLevelShield()));
          if (playerData.getItemDamageAdjustmentShield() > 0.0f) {
            tooltip.add(3, formatAttackDamage(playerData.getItemDamageAdjustmentShield()));
          }
        }
      }
    }

    else {
      // Do nothing.
    }

  }

  private static Component formatWeaponClass(String weaponClass) {
    return Component
        .translatable(Constants.CLASS_TEXT_PREFIX, Component
            .translatable(Constants.CLASS_TEXT_PREFIX + weaponClass).withStyle(ChatFormatting.BLUE))
        .withStyle(ChatFormatting.GRAY);
  }

  private static Component formatLevel(int level) {
    return Component
        .translatable(Constants.TOOLTIP_TEXT_PREFIX + "level", level, Experience.getMaxLevel())
        .withStyle(ChatFormatting.YELLOW);
  }

  private static Component formatAttackDamage(float attackDamage) {
    return Component.translatable(Constants.TOOLTIP_TEXT_PREFIX + "attack_damage", Component
        .literal(
            String.format("+%.2f%%", attackDamage > 1 ? (attackDamage - 1) * 100 : attackDamage))
        .withStyle(ChatFormatting.GREEN)).withStyle(ChatFormatting.GRAY);
  }

}
