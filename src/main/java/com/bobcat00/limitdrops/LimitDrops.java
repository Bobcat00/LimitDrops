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

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.plugin.java.JavaPlugin;

public final class LimitDrops extends JavaPlugin
{
    Listeners listeners;

    @Override
    public void onEnable()
    {
        saveDefaultConfig();
        
        listeners = new Listeners(this);
        
        // Metrics
        int pluginId = 21059;
        Metrics metrics = new Metrics(this, pluginId);
        
        metrics.addCustomChart(new SimplePie("player",              () -> getConfig().getBoolean("limit-drops.player")                ? "Yes" : "No"));
        metrics.addCustomChart(new SimplePie("container_inventory", () -> getConfig().getBoolean("limit-drops.container-inventory")   ? "Yes" : "No"));
        metrics.addCustomChart(new SimplePie("minecart_inventory",  () -> getConfig().getBoolean("limit-drops.minecart-inventory")    ? "Yes" : "No"));
        metrics.addCustomChart(new SimplePie("dispenser",           () -> getConfig().getBoolean("limit-drops.dispenser")             ? "Yes" : "No"));
        metrics.addCustomChart(new SimplePie("shulker_box",         () -> getConfig().getBoolean("prevent-shulkerbox-creative-drops") ? "Yes" : "No"));
        
        getLogger().info("Metrics enabled if allowed by plugins/bStats/config.yml");
    }
 
    @Override
    public void onDisable()
    {
        // HandlerList.unregisterAll(listeners);
    }

}
