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

package de.markusbordihn.minecraft.dynamicplayerprogressionplayerdifficulty.data;

import java.util.Set;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.Item;

public class PlayerData {

  private ServerPlayer player;
  private ServerStatsCounter stats;

  private String username;

  // General stats
  private float damageAbsorbed = 0;
  private float damageBlockedByShield = 0;
  private float damageDealt = 0;
  private float damageDealtAbsorbed = 0;
  private float damageDealtResisted = 0;
  private float damageResisted = 0;
  private float damageTaken = 0;
  private float sneakTime = 0;
  private int mobKills = 0;
  private int numberOfDeaths = 0;
  private int playerKills = 0;
  private int raidsTriggered = 0;
  private int raidsWon = 0;
  private int targetsHit = 0;

  // Specific stats experience
  private int damageExperienceMob = 0;
  private int damageExperiencePlayer = 0;
  private int itemExperienceAxe = 0;
  private int itemExperienceBow = 0;
  private int itemExperienceCrossbow = 0;
  private int itemExperiencePickaxe = 0;
  private int itemExperienceShield = 0;
  private int itemExperienceSword = 0;

  // Specific stats level
  private int damageLevelMob = 1;
  private int damageLevelPlayer = 1;
  private int itemLevelAxe = 1;
  private int itemLevelBow = 1;
  private int itemLevelCrossbow = 1;
  private int itemLevelPickaxe = 1;
  private int itemLevelShield = 1;
  private int itemLevelSword = 1;

  // Adjustments
  private float dealtDamageAdjustmentMob = 0;
  private float dealtDamageAdjustmentPlayer = 0;
  private float hurtDamageAdjustmentMob = 0;
  private float hurtDamageAdjustmentPlayer = 0;
  private float itemDamageAdjustmentAxe = 0;
  private float itemDamageAdjustmentBow = 0;
  private float itemDamageAdjustmentCrossbow = 0;
  private float itemDamageAdjustmentPickaxe = 0;
  private float itemDamageAdjustmentShield = 0;
  private float itemDamageAdjustmentSword = 0;

  public PlayerData(ServerPlayer player) {
    if (player == null) {
      return;
    }
    this.player = player;
    this.stats = player.getStats();
    updateStats();
  }

  public void updateStats() {
    if (this.stats == null && this.player != null) {
      this.stats = player.getStats();
    }
    updateStats(this.stats);
  }

  public void updateStats(ServerStatsCounter stats) {
    if (stats == null) {
      return;
    }

    // Stats which needs conversions
    this.damageAbsorbed = getStatsFloatValue(stats, Stats.CUSTOM, Stats.DAMAGE_ABSORBED);
    this.damageBlockedByShield =
        getStatsFloatValue(stats, Stats.CUSTOM, Stats.DAMAGE_BLOCKED_BY_SHIELD);
    this.damageDealt = getStatsFloatValue(stats, Stats.CUSTOM, Stats.DAMAGE_DEALT) / 10;
    this.damageDealtAbsorbed = getStatsFloatValue(stats, Stats.CUSTOM, Stats.DAMAGE_DEALT_ABSORBED);
    this.damageDealtResisted = getStatsFloatValue(stats, Stats.CUSTOM, Stats.DAMAGE_DEALT_RESISTED);
    this.damageResisted = getStatsFloatValue(stats, Stats.CUSTOM, Stats.DAMAGE_RESISTED);
    this.damageTaken = getStatsFloatValue(stats, Stats.CUSTOM, Stats.DAMAGE_TAKEN);
    this.sneakTime = getStatsFloatValue(stats, Stats.CUSTOM, Stats.CROUCH_TIME);

    // Stats without conversion
    this.mobKills = stats.getValue(Stats.CUSTOM, Stats.MOB_KILLS);
    this.numberOfDeaths = stats.getValue(Stats.CUSTOM, Stats.DEATHS);
    this.playerKills = stats.getValue(Stats.CUSTOM, Stats.PLAYER_KILLS);
    this.raidsTriggered = stats.getValue(Stats.CUSTOM, Stats.RAID_TRIGGER);
    this.raidsWon = stats.getValue(Stats.CUSTOM, Stats.RAID_WIN);
    this.targetsHit = stats.getValue(Stats.CUSTOM, Stats.TARGET_HIT);

    // Calculate damage experience
    double damageBase = this.playerKills + (double) this.mobKills;
    this.damageExperienceMob = (int) Math.round(this.damageDealt * (this.mobKills / damageBase));
    this.damageExperiencePlayer =
        (int) Math.round(this.damageDealt * (this.playerKills / damageBase));

    // Calculate item experience
    float itemExperienceFactor = Experience.getExperienceFactorItems();
    this.itemExperienceAxe =
        Math.round(getItemsUsage(PlayerDataManager.getAxeItems()) * itemExperienceFactor);
    this.itemExperienceBow =
        Math.round(getItemsUsage(PlayerDataManager.getBowItems()) * itemExperienceFactor);
    this.itemExperienceCrossbow =
        Math.round(getItemsUsage(PlayerDataManager.getCrossbowItems()) * itemExperienceFactor);
    this.itemExperiencePickaxe =
        Math.round(getItemsUsage(PlayerDataManager.getPickaxeItems()) * itemExperienceFactor);
    this.itemExperienceShield =
        Math.round(getItemsUsage(PlayerDataManager.getShieldItems()) * itemExperienceFactor);
    this.itemExperienceSword =
        Math.round(getItemsUsage(PlayerDataManager.getSwordItems()) * itemExperienceFactor);

    // Calculate levels with death penalty
    int experienceDeathPenalty = this.numberOfDeaths * Experience.getExperienceDeathPenalty();
    int experienceDeathPenaltyItems =
        this.numberOfDeaths * Experience.getExperienceDeathPenaltyItems();
    this.damageLevelMob =
        Experience.getLevelFromExperience(this.damageExperienceMob, experienceDeathPenalty);
    this.damageLevelPlayer =
        Experience.getLevelFromExperience(this.damageExperiencePlayer, experienceDeathPenalty);
    this.itemLevelAxe =
        Experience.getLevelFromExperience(this.itemExperienceAxe, experienceDeathPenaltyItems);
    this.itemLevelBow =
        Experience.getLevelFromExperience(this.itemExperienceBow, experienceDeathPenaltyItems);
    this.itemLevelCrossbow =
        Experience.getLevelFromExperience(this.itemExperienceCrossbow, experienceDeathPenaltyItems);
    this.itemLevelPickaxe =
        Experience.getLevelFromExperience(this.itemExperiencePickaxe, experienceDeathPenaltyItems);
    this.itemLevelShield =
        Experience.getLevelFromExperience(this.itemExperienceShield, experienceDeathPenaltyItems);
    this.itemLevelSword =
        Experience.getLevelFromExperience(this.itemExperienceSword, experienceDeathPenaltyItems);

    // Calculate adjustments
    this.dealtDamageAdjustmentMob = Experience.getDealtDamageAdjustment(this.damageLevelMob);
    this.dealtDamageAdjustmentPlayer = Experience.getDealtDamageAdjustment(this.damageLevelPlayer);
    this.hurtDamageAdjustmentMob = Experience.getHurtDamageAdjustment(this.damageLevelMob);
    this.hurtDamageAdjustmentPlayer = Experience.getHurtDamageAdjustment(this.damageLevelPlayer);
    this.itemDamageAdjustmentAxe = Experience.getItemDamageAdjustmentAxe(this.itemLevelAxe);
    this.itemDamageAdjustmentBow = Experience.getItemDamageAdjustmentBow(this.itemLevelBow);
    this.itemDamageAdjustmentCrossbow =
        Experience.getItemDamageAdjustmentCrossbow(this.itemLevelCrossbow);
    this.itemDamageAdjustmentPickaxe =
        Experience.getItemDamageAdjustmentPickaxe(this.itemLevelPickaxe);
    this.itemDamageAdjustmentShield =
        Experience.getItemDamageAdjustmentShield(this.itemLevelShield);
    this.itemDamageAdjustmentSword = Experience.getItemDamageAdjustmentSword(this.itemLevelSword);
  }

  public String getUsername() {
    return username;
  }

  public float getDamageAbsorbed() {
    return damageAbsorbed;
  }

  public float getDamageBlockedByShield() {
    return damageBlockedByShield;
  }

  public float getDamageDealt() {
    return damageDealt;
  }

  public float getDamageDealtAbsorbed() {
    return damageDealtAbsorbed;
  }

  public float getDamageDealtResisted() {
    return damageDealtResisted;
  }

  public int getDamageExperienceMob() {
    return damageExperienceMob;
  }

  public int getDamageExperiencePlayer() {
    return damageExperiencePlayer;
  }

  public float getDamageResisted() {
    return damageResisted;
  }

  public float getDamageTaken() {
    return damageTaken;
  }

  public float getSneakTime() {
    return sneakTime;
  }

  public int getMobKills() {
    return mobKills;
  }

  public int getNumberOfDeaths() {
    return numberOfDeaths;
  }

  public int getPlayerKills() {
    return playerKills;
  }

  public int getRaidsTriggered() {
    return raidsTriggered;
  }

  public int getRaidsWon() {
    return raidsWon;
  }

  public int getTargetsHit() {
    return targetsHit;
  }

  public int getDamageLevelMob() {
    return damageLevelMob;
  }

  public int getDamageLevelPlayer() {
    return damageLevelPlayer;
  }

  public float getDealtDamageAdjustmentMob() {
    return dealtDamageAdjustmentMob;
  }

  public float getDealtDamageAdjustmentPlayer() {
    return dealtDamageAdjustmentPlayer;
  }

  public float getHurtDamageAdjustmentMob() {
    return hurtDamageAdjustmentMob;
  }

  public float getHurtDamageAdjustmentPlayer() {
    return hurtDamageAdjustmentPlayer;
  }

  public int getItemExperienceAxe() {
    return itemExperienceAxe;
  }

  public int getItemExperienceBow() {
    return itemExperienceBow;
  }

  public int getItemExperienceCrossbow() {
    return itemExperienceCrossbow;
  }

  public int getItemExperiencePickaxe() {
    return itemExperiencePickaxe;
  }

  public int getItemExperienceShield() {
    return itemExperienceShield;
  }

  public int getItemExperienceSword() {
    return itemExperienceSword;
  }

  public int getItemLevelAxe() {
    return itemLevelAxe;
  }

  public int getItemLevelBow() {
    return itemLevelBow;
  }

  public int getItemLevelCrossbow() {
    return itemLevelCrossbow;
  }

  public int getItemLevelPickaxe() {
    return itemLevelPickaxe;
  }

  public int getItemLevelShield() {
    return itemLevelShield;
  }

  public int getItemLevelSword() {
    return itemLevelSword;
  }

  public float getItemDamageAdjustmentAxe() {
    return itemDamageAdjustmentAxe;
  }

  public float getItemDamageAdjustmentBow() {
    return itemDamageAdjustmentBow;
  }

  public float getItemDamageAdjustmentCrossbow() {
    return itemDamageAdjustmentCrossbow;
  }

  public float getItemDamageAdjustmentPickaxe() {
    return itemDamageAdjustmentPickaxe;
  }

  public float getItemDamageAdjustmentShield() {
    return itemDamageAdjustmentShield;
  }

  public float getItemDamageAdjustmentSword() {
    return itemDamageAdjustmentSword;
  }

  @Override
  public String toString() {
    return "PlayerData['" + player.getDisplayName().getString() + "', damage dealt="
        + this.damageDealt + "]";
  }

  private int getItemUsage(Item item) {
    return stats.getValue(Stats.ITEM_USED, item);
  }

  private int getItemsUsage(Set<Item> items) {
    int usage = 0;
    for (Item item : items) {
      int itemUsage = getItemUsage(item);
      if (itemUsage > 0) {
        usage += itemUsage;
      }
    }
    return usage;
  }

  private static float getStatsFloatValue(ServerStatsCounter stats,
      StatType<ResourceLocation> statType, ResourceLocation state) {
    if (stats != null) {
      int value = stats.getValue(statType, state);
      if (value != 0) {
        return value / 1.0f;
      }
    }
    return 0.0f;
  }

}