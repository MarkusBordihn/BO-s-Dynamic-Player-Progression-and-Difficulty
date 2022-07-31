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

package de.markusbordihn.minecraft.dynamicplayerprogressionplayerdifficulty.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import de.markusbordihn.minecraft.dynamicplayerprogressionplayerdifficulty.data.Experience;
import de.markusbordihn.minecraft.dynamicplayerprogressionplayerdifficulty.data.PlayerData;
import de.markusbordihn.minecraft.dynamicplayerprogressionplayerdifficulty.data.PlayerDataManager;

public class StatsCommand extends CustomCommand {
  private static final StatsCommand command = new StatsCommand();

  public static ArgumentBuilder<CommandSourceStack, ?> register() {
    return Commands.literal("stats").requires(cs -> cs.hasPermission(0)).executes(command);
  }

  @Override
  public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
    ServerPlayer player = context.getSource().getPlayerOrException();
    PlayerData playerData = PlayerDataManager.getPlayer(player);

    // Update stats to make sure we should current values
    playerData.updateStats();

    sendFeedback(context, "⚔ Stats\n=============");

    // Levels
    sendFeedback(context, String.format("⧫ Damage Level (Mob): %s (%s exp)",
        playerData.getDamageLevelMob(), playerData.getDamageExperienceMob()));
    sendFeedback(context, String.format("⧫ Damage Level (Player): %s (%s exp)",
        playerData.getDamageLevelPlayer(), playerData.getDamageExperiencePlayer()));

    // Adjustments
    sendFeedback(context, String.format("⚔ Dealt Damage adjustments to Mob %s / to Player %s",
        playerData.getDealtDamageAdjustmentMob(), playerData.getDealtDamageAdjustmentPlayer()));
    sendFeedback(context, String.format("🛡 Hurt Damage adjustments by Mob %s / by Player %s",
        playerData.getHurtDamageAdjustmentMob(), playerData.getHurtDamageAdjustmentPlayer()));

    // Misc
    sendFeedback(context,
        String.format("☠ Number of Deaths: %s / Death penalty: %s xp", playerData.getNumberOfDeaths(),
            playerData.getNumberOfDeaths() * Experience.getExperienceForMinLevel() * 0.75));

    return 0;
  }

}
