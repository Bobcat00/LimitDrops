// LimitDrops
// Copyright 2022 Bobcat00
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

package com.bobcat00.limitdrops;

import org.bukkit.ChatColor;
import org.bukkit.block.Container;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.InventoryHolder;

public final class Listeners implements Listener
{
    private LimitDrops plugin;
    
    public Listeners(LimitDrops plugin)
    {
        this.plugin = plugin;
    }
    
    // -------------------------------------------------------------------------
    
    // Prevent players from dropping items.
    
    // We use priority HIGH so WorldGuard has first crack at this event.
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        if (plugin.getConfig().getStringList("worlds").contains(event.getPlayer().getLocation().getWorld().getName()))
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("drop-message")));
        }
    }
    
    // -------------------------------------------------------------------------
    
    // Prevent containers from dropping their inventories. The event is not
    // canceled, so the block itself is still dropped normally.
    
    // We use priority HIGHEST so another plugin hopefully won't cancel the
    // event after we've cleared the inventory.
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event)
    {
        if (event.getBlock().getState() instanceof Container)
        {
            if (plugin.getConfig().getStringList("worlds").contains(event.getBlock().getLocation().getWorld().getName()))
            {
                Container container = (Container) event.getBlock().getState();
                if (!container.getInventory().isEmpty())
                {
                    container.getInventory().clear();
                    event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("drop-message")));
                }
            }
        }
    }
    
    // -------------------------------------------------------------------------
    
    // Prevent minecarts with inventories from dropping their inventories. The
    // event is not canceled, so the minecart itself is still dropped normally.
    
    // We use priority HIGHEST so another plugin hopefully won't cancel the
    // event after we've cleared the inventory.
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGHEST)
    public void onVehicleDestroy(VehicleDestroyEvent event)
    {
        if (event.getVehicle() instanceof Minecart && event.getVehicle() instanceof InventoryHolder)
        {
            if (plugin.getConfig().getStringList("worlds").contains(event.getVehicle().getLocation().getWorld().getName()))
            {
                InventoryHolder invHolder = (InventoryHolder) event.getVehicle();
                if (!invHolder.getInventory().isEmpty())
                {
                    invHolder.getInventory().clear();
                    Entity attacker = event.getAttacker();
                    if (attacker != null && attacker instanceof Player)
                    {
                        attacker.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("drop-message")));
                    }
                }
            }
        }
    }
    
    // -------------------------------------------------------------------------
    
    // Prevent dispensers/droppers from dispensing anything.
    
    // We use priority HIGH so WorldGuard has first crack at this event.
    @EventHandler(ignoreCancelled=true, priority=EventPriority.HIGH)
    public void onBlockDispense(BlockDispenseEvent event)
    {
        if (plugin.getConfig().getStringList("worlds").contains(event.getBlock().getLocation().getWorld().getName()))
        {
            event.setCancelled(true);
        }
    }
    
//    @EventHandler
//    public void onItemSpawn(ItemSpawnEvent event)
//    {
//        if (event.getEntity().getLocation().getWorld().getName().equals("plotworld"))
//        {
//            //plugin.getLogger().info(event.getEventName() + ", " + event.getEntity().getLocation().getWorld().getName() + ": " + event.getEntity().getItemStack().toString());
//            event.setCancelled(true);
//            
//            UUID thrower = event.getEntity().getThrower();
//            if (thrower != null)
//            {
//                Player player = Bukkit.getPlayer(thrower);
//                if (player != null && player.isOnline())
//                {
//                    player.sendMessage("Drops are not allowed.");
//                }
//            }
//        }
//    }

}
