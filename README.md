# LimitDrops
Stop players from dropping items and lagging the server. There are four cases, each which can be enabled/disabled individually:

1. Prevent players from dropping items (PlayerDropItemEvent). This can also be done with WorldGuard.

2. Prevent containers from dropping their inventories (BlockBreakEvent). The event itself is not canceled, so the block itself is still dropped normally.

3. Prevent minecarts with inventories from dropping their inventories (VehicleDestroyEvent). The event itself is not canceled, so the minecart itself is still dropped normally. The reasoning is that if you break a regular minecart, you get the minecart, so if you break a hopper minecart you should get a hopper and a minecart.

4. Prevent dispensers/droppers from dispensing anything (BlockDispenseEvent). WorldGuard can do this, but only for items on the blacklist.
