# NameFabric

NameFabric, the mod that shows you public player data like past usernames, current skin, and more using the Mojang's API.

We have a discord now! **[Discord](https://discord.gg/bChCtfB9eS)**

**Disclaimer**: This **REQUIRES** [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api).

## Features
- /namehistory | Gets the past names of the specified player
- /getuuid | Gets the uuid of the specified player
- /getskin | Gets the current skin of the specified player
- /getcape | Gets the current cape of the specified player

## Known Issues:
- If you put anything with the same length as 32 or 36 it will cause an error in the commands.
- Commands sometimes say "Invalid name/uuid", even though it is valid, and "An error occurred".
- Commands sometimes cause random errors that aren't shown in chat.

## Credits:
- [NameMC](https://namemc.com/capes) - Capes List
- EarthComputer, xpple and haykam821 (PlayerInfoCommand - Variables, fetchNameHistory function and the HTTP request) - https://github.com/Earthcomputer/clientcommands/
