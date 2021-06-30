# PunchTree-Minigames
This repository holds common code and libraries underlying a suite of PvP minigames implemented in Minecraft. 
The only additionally extracted library is the [movement library](https://github.com/Cxom/Movement-), due to inclusion of some aspects of it in non-minigame modules.

Current minigames are:

* **Melee** - A free-for-all minigame, first to 7 (or X) kills.
* **Rabbit** - Capture-The-Flag meets King-of-the-Hill. 
Holding the flag grants double movement stamina over other players; first to hold the flag for a cumulative 2 minutes (X seconds) wins.
* **Jailbreak** - Like team deathmatch, except when you die you go to jail. Alive teammates can free you from jail. If your whole team is dead, you lose.
* **Battle** - Capture all the enemy's poles to win, but only do so when it's your turn! Deny enemies from capturing any of your team's poles for 15 seconds to turn the tide of battle!
* **Siege** - One side gets no respawns, lots of health, and some better equipment, and they fight their way through a large, multi-chapter map to capture the kingdom/city/etc. The other side respawns with each captured section of the map, and tries to stop the attackers from sieging their territory!
* **BeachWars** - An experimental turf-wars type gamemode. As your team gains kills, the sand under your feet starts to turn your team's color! Die to the enemy team and the opposite happens. Capture the whole arena to win!

Repositories under the PunchTree-Minigames umbrella currently are:

* [PunchTree-Minigames](https://github.com/Cxom/PunchTree-Minigames) - The core minigames libraries (You are here)
* [Movement++](https://github.com/Cxom/Movement-) - The component-based movement augmentation system and libraries
* [Melee](https://github.com/Cxom/Melee2) - Contains Melee and Rabbit code. The oldest codebase and has a lot of refactoring still left to do.
* [Jailbreak](https://github.com/Cxom/Jailbreak3) 
* [Battle](https://github.com/Cxom/Battle) 
* [Siege](https://github.com/Cxom/Siege) - (Still under development - not currently playable)
* [BeachWars](https://github.com/Cxom/BeachWars) - (Developed to experimentally playable - alpha, not currently being worked on)
