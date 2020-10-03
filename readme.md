# Visible Barriers - Fabric
This fabric mod adds the ability to toggle the visiblity of barriers, structure voids and invisible armor stands. It also features other misc tools to help with technical aspects.  

## Info
Mod designed for Fabric 1.15 or 1.16.  
The default toggle keybinds are:
* `B` - Toggle Barriers and Structure Blocks
* `N` - Open Configuration Screen
* `M` - Open device menu
* `Not Set` - Toggle Structure Name Renderer (On Target/Always)

## Installation
1. Download and install Fabric: https://fabricmc.net/
2. Download the Fabric API: https://www.curseforge.com/minecraft/mc-mods/fabric-api
2. Download the mod file from [here (1.15.2)](../1.15/VisibleBarriersMod-1.4.0.jar) or [here (1.16.2)](../1.16a/VisibleBarriersMod-1.4.0.jar)
3. Place both files in the mods folder for your profile (default: %appdata%/.minecraft/mods)


## Chest Helper
1. Download the 'chests.yml' file from github
2. Either place it at the default location (%appdata%/.minecraft/config/chests.yml) or change the dirrectory in the settings menu (ensure the full file path is used eg. C:/Users/aws404/Desktop/chests.yml)
3. Change the 'Highlight Loot Chests' option to your prefered option:
 - NONE: No highlights at all
 - CUBE_ALL: Places a cube around where each chest would be
 - BEAM_BROKEN: Places a cube around where each chest would be and creates a beacon beam if:
   - The block is non-replaceable (not air, tall grass, ect.)
   - The block above is not air
   - The block below is air
 - BEAM_ALL: Places a cube and a beacon beam around where each chest would be
4. You can use the '/reloadchests' command at any time to reload data from the file
