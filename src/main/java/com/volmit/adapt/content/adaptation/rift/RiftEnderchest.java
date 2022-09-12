/*------------------------------------------------------------------------------
 -   Adapt is a Skill/Integration plugin  for Minecraft Bukkit Servers
 -   Copyright (c) 2022 Arcane Arts (Volmit Software)
 -
 -   This program is free software: you can redistribute it and/or modify
 -   it under the terms of the GNU General Public License as published by
 -   the Free Software Foundation, either version 3 of the License, or
 -   (at your option) any later version.
 -
 -   This program is distributed in the hope that it will be useful,
 -   but WITHOUT ANY WARRANTY; without even the implied warranty of
 -   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 -   GNU General Public License for more details.
 -
 -   You should have received a copy of the GNU General Public License
 -   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 -----------------------------------------------------------------------------*/

package com.volmit.adapt.content.adaptation.rift;

import com.volmit.adapt.Adapt;
import com.volmit.adapt.api.adaptation.SimpleAdaptation;
import com.volmit.adapt.nms.NMS;
import com.volmit.adapt.util.C;
import com.volmit.adapt.util.Element;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public class RiftEnderchest extends SimpleAdaptation<RiftEnderchest.Config> {
    public RiftEnderchest() {
        super("rift-enderchest");
        setDescription(Adapt.dLocalize("Rift", "RiftEnderchest", "Description"));
        setDisplayName(Adapt.dLocalize("Rift", "RiftEnderchest", "Name"));
        setIcon(Material.ENDER_CHEST);
        setBaseCost(0);
        setCostFactor(0);
        setMaxLevel(1);
        setInitialCost(10);
        setInterval(9248);
        registerConfiguration(Config.class);
    }

    @Override
    public void addStats(int level, Element v) {
        v.addLore(C.ITALIC + Adapt.dLocalize("Rift", "RiftEnderchest", "Lore1"));
    }

    @EventHandler
    public void on(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!hasAdaptation(p)) {
            return;
        }
        ItemStack hand = p.getInventory().getItemInMainHand();
        if (hand.getType() != Material.ENDER_CHEST) {
            return;
        }

        if (p.hasCooldown(hand.getType())) {
            e.setCancelled(true);
            return;
        } else {
            NMS.get().sendCooldown(p, Material.ENDER_PEARL, 100);
            p.setCooldown(Material.ENDER_PEARL, 100);
        }
        if (p.getInventory().getItemInMainHand().getType().equals(Material.ENDER_CHEST)
                && (e.getAction().equals(Action.RIGHT_CLICK_AIR)
                || e.getAction().equals(Action.LEFT_CLICK_AIR)
                || e.getAction().equals(Action.LEFT_CLICK_BLOCK))) {

            if (getPlayer(p).getData().getSkillLines().get("rift").getAdaptations().get("rift-resist") != null
                    && getPlayer(p).getData().getSkillLines().get("rift").getAdaptations().get("rift-resist").getLevel() > 0) {
                RiftResist.riftResistStackAdd(p, 10, 2);
            }
            p.openInventory(p.getEnderChest());
        }
    }

    @Override
    public void onTick() {

    }

    @Override
    public boolean isEnabled() {
        return getConfig().enabled;
    }

    @NoArgsConstructor
    protected static class Config {
        boolean enabled = true;
    }
}