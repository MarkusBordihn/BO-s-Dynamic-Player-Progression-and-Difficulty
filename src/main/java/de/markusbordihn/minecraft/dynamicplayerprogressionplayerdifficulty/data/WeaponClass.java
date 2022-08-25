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

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.Set;

import de.markusbordihn.minecraft.dynamicplayerprogressionplayerdifficulty.Constants;
import de.markusbordihn.minecraft.dynamicplayerprogressionplayerdifficulty.config.CommonConfig;

public enum WeaponClass {

  //@formatter:off
  AXE("🪓", "axe", null),
  BOW("🏹", "bow", null),
  CROSSBOW("🏹", "crossbow", null),
  DAGGER("🗡", "dagger", null),
  GREAT_SWORD("⚔", "great sword", null),
  GUN("▝▜", "gun", null),
  HAMMER("⚒", "hammer", null),
  HAND_TO_HAND("╽", "hand to hand", null),
  HOE("↿", "hoe", null),
  KATANA("⚔", "katana", null),
  KEYBLADE("⚷", "keyblade", null),
  PICKAXE("⛏", "pickaxe", null),
  POLEARM("🔱", "polearm", null),
  SCYTHE("⚳", "scythe", null),
  SHIELD("🛡", "shield", null),
  SHOVEL("⚒", "shovel", null),
  STAFF("╲", "staff", null),
  SWORD("⚔", "sword", null),
  TACHI("⚔", "tachi", null),
  WAND("⚚", "wand", null);
  //@formatter:on

  private static final CommonConfig.Config COMMON = CommonConfig.COMMON;

  public final ResourceLocation icon;
  public final String textIcon;
  public final String textName;
  public final String translationId;
  public final TranslatableComponent text;

  private WeaponClass(String textIcon, String textName, ResourceLocation icon) {
    this.icon = icon;
    this.textIcon = textIcon;
    this.translationId = Constants.CLASS_TEXT_PREFIX + this.name().toLowerCase();
    this.text = new TranslatableComponent(this.translationId);
    this.textName = textName;
  }

  public float getDamageAdjustment() {
    switch (this) {
      case AXE:
        return COMMON.axeItemDamageIncrease.get();
      case BOW:
        return COMMON.bowItemDamageIncrease.get();
      case CROSSBOW:
        return COMMON.crossbowItemDamageIncrease.get();
      case DAGGER:
        return COMMON.daggerItemDamageIncrease.get();
      case GREAT_SWORD:
        return COMMON.greatSwordItemDamageIncrease.get();
      case GUN:
        return COMMON.gunItemDamageIncrease.get();
      case HAMMER:
        return COMMON.hammerItemDamageIncrease.get();
      case KATANA:
        return COMMON.katanaItemDamageIncrease.get();
      case HAND_TO_HAND:
        return COMMON.handToHandItemDamageIncrease.get();
      case HOE:
        return COMMON.hoeItemDamageIncrease.get();
      case KEYBLADE:
        return COMMON.keybladeItemDamageIncrease.get();
      case PICKAXE:
        return COMMON.pickaxeItemDamageIncrease.get();
      case POLEARM:
        return COMMON.polearmItemDamageIncrease.get();
      case SCYTHE:
        return COMMON.scytheItemDamageIncrease.get();
      case SHIELD:
        return COMMON.shieldItemDamageIncrease.get();
      case SHOVEL:
        return COMMON.shovelItemDamageIncrease.get();
      case STAFF:
        return COMMON.staffItemDamageIncrease.get();
      case SWORD:
        return COMMON.swordItemDamageIncrease.get();
      case TACHI:
        return COMMON.tachiItemDamageIncrease.get();
      case WAND:
        return COMMON.wandItemDamageIncrease.get();
    }
    return 0.0f;
  }

  public float getDamageAdjustment(int level, int maxLevel) {
    float adjustment = getDamageAdjustment();
    return adjustment == 0 || level == 1 ? 0.0f
        : 1.0f + ((((float) level / maxLevel) * adjustment) / 100f);
  }

  public int getDurabilityAdjustment() {
    switch (this) {
      case AXE:
        return COMMON.axeItemDurabilityIncrease.get();
      case BOW:
        return COMMON.bowItemDurabilityIncrease.get();
      case CROSSBOW:
        return COMMON.crossbowItemDurabilityIncrease.get();
      case DAGGER:
        return COMMON.daggerItemDurabilityIncrease.get();
      case GREAT_SWORD:
        return COMMON.greatSwordItemDurabilityIncrease.get();
      case GUN:
        return COMMON.gunItemDurabilityIncrease.get();
      case HAMMER:
        return COMMON.hammerItemDurabilityIncrease.get();
      case HOE:
        return COMMON.hoeItemDurabilityIncrease.get();
      case KATANA:
        return COMMON.katanaItemDurabilityIncrease.get();
      case HAND_TO_HAND:
        return COMMON.handToHandItemDurabilityIncrease.get();
      case KEYBLADE:
        return COMMON.keybladeItemDurabilityIncrease.get();
      case PICKAXE:
        return COMMON.pickaxeItemDurabilityIncrease.get();
      case POLEARM:
        return COMMON.polearmItemDurabilityIncrease.get();
      case SCYTHE:
        return COMMON.scytheItemDurabilityIncrease.get();
      case SHIELD:
        return COMMON.shieldItemDurabilityIncrease.get();
      case SHOVEL:
        return COMMON.shovelItemDurabilityIncrease.get();
      case STAFF:
        return COMMON.staffItemDurabilityIncrease.get();
      case SWORD:
        return COMMON.swordItemDurabilityIncrease.get();
      case TACHI:
        return COMMON.tachiItemDurabilityIncrease.get();
      case WAND:
        return COMMON.wandItemDurabilityIncrease.get();
    }
    return 0;
  }

  public float getDurabilityAdjustment(int level, int maxLevel) {
    int adjustment = getDurabilityAdjustment();
    return adjustment == 0 || level == 1 ? 0.0f
        : 1.0f + ((((float) level / maxLevel) * adjustment) / 100f);
  }

  public Set<Item> getItems() {
    return WeaponClassData.getItems(this);
  }

  public String getTextName() {
    return this.textName;
  }
}
