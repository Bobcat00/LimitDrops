// LimitDrops - Limit drops in specified worlds
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
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.EventExecutor;

public final class Listeners implements Listener
{
    private LimitDrops plugin;
    
    // Constructor
    
    public Listeners(LimitDrops plugin)
    {
        this.plugin = plugin;
        
        // Register listeners
        
        if (plugin.getConfig().getBoolean("limit-drops.player"))
        {
            // Use priority HIGH so WorldGuard has first crack at this event
            plugin.getServer().getPluginManager().registerEvent(PlayerDropItemEvent.class, this, EventPriority.HIGH,
                    new EventExecutor() { public void execute(Listener l, Event e) { if (e instanceof PlayerDropItemEvent) onPlayerDropItem((PlayerDropItemEvent)e); }},
                    plugin, true); // ignoreCancelled=true
        }

        if (plugin.getConfig().getBoolean("limit-drops.container-inventory"))
        {
            // Use priority HIGHEST so another plugin hopefully won't cancel the event after we've cleared the inventory
            plugin.getServer().getPluginManager().registerEvent(BlockBreakEvent.class, this, EventPriority.HIGHEST,
                    new EventExecutor() { public void execute(Listener l, Event e) { if (e instanceof BlockBreakEvent) onBlockBreak((BlockBreakEvent)e); }},
                    plugin, true); // ignoreCancelled=true
        }

        if (plugin.getConfig().getBoolean("limit-drops.minecart-inventory"))
        {
            // Use priority HIGHEST so another plugin hopefully won't cancel the event after we've cleared the inventory
            plugin.getServer().getPluginManager().registerEvent(VehicleDestroyEvent.class, this, EventPriority.HIGHEST,
                    new EventExecutor() { public void execute(Listener l, Event e) { if (e instanceof VehicleDestroyEvent) onVehicleDestroy((VehicleDestroyEvent)e); }},
                    plugin, true); // ignoreCancelled=true
        }

        if (plugin.getConfig().getBoolean("limit-drops.dispenser"))
        {
            // Use priority HIGH so WorldGuard has first crack at this event
            plugin.getServer().getPluginManager().registerEvent(BlockDispenseEvent.class, this, EventPriority.HIGH,
                    new EventExecutor() { public void execute(Listener l, Event e) { if (e instanceof BlockDispenseEvent) onBlockDispense((BlockDispenseEvent)e); }},
                    plugin, true); // ignoreCancelled=true
        }
    }
    
    // -------------------------------------------------------------------------
    
    // Prevent players from dropping items.
    
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
    
    public void onBlockDispense(BlockDispenseEvent event)
    {
        if (plugin.getConfig().getStringList("worlds").contains(event.getBlock().getLocation().getWorld().getName()))
        {
            event.setCancelled(true);
        }
    }

}
