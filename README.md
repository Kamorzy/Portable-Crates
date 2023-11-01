# Portable Crates
## Purpose
With the growing number of unique blocks in the game, the current inventory is insufficient. There is more emphasis on exploring in the recent updates, but it ends up being heavily discouraged by the inventory filling up so quickly.

Storage systems also easily get very large, both due to all the players' storage needs and to handle high outputs of farms. Moving bases, even in the early-game, is usually not very practical. Because of that, it can feel like the game can't really start until you have shulker boxes.

Crates solve those issues - encouraging players to explore further, move their base more freely, and compact their storage systems. However, it does it in a way that doesn't upset the overall vanilla balance nor diminish the value of vanilla features. The vanilla containers are still best in their niche, and the player still needs to think about how they optimize their usual 27 slot inventory.

----------
## Details

#### Storage
Adds 2 blocks to the game:
- Crate
- Sealed Crate

Both have 9 slots of inventory space. Craft a crate with 4 iron ingots and 4 planks:

<img src=>

The crate functions similar to a piggy bank or a wrapped package.

Place a crate down and put items in it like any container. When you break the crate block, it will drop as a sealed crate with those items. Sealed crates can't be accessed by players, and hoppers/droppers can't interact with it. When you place and break a sealed crate, the sealed crate will be destroyed and it will drop its contents.

- If you break an empty crate, it will just drop the crate again
- If you break a sealed crate with silk touch, it will drop the sealed crate again
- Sealed crates and shulker boxes can't be stored in crates
- Both crates can be stored in shulker boxes
- Crates stack to 64, sealed crates are unstackable

#### Redstone
Pistons extending into crate blocks will break them, causing them to drop whatever they're expected to drop, similar to if a player did it.

Comparators read the fullness of the crates, like other containers.

Hoppers/droppers work as expected with crates, but do no interact with sealed crates.

#### Decoration
Crates are directional, so placing at different orientations makes the diagonal go one way or the other.

<img src=>