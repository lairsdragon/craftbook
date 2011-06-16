// $Id$
/*
* Copyright (C) 2010, 2011 sk89q <http://www.sk89q.com>
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/

package com.sk89q.craftbook.gates.world;

import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import com.sk89q.craftbook.ic.AbstractIC;
import com.sk89q.craftbook.ic.AbstractICFactory;
import com.sk89q.craftbook.ic.ChipState;
import com.sk89q.craftbook.ic.IC;
import com.sk89q.craftbook.ic.RestrictedIC;

public class FlexibleSetBlock extends AbstractIC {

    public FlexibleSetBlock(Server server, Sign block) {
        super(server, block);
    }

    @Override
    public String getTitle() {
        return "Flexible Set Block";
    }

    @Override
    public String getSignTitle() {
        return "FLEX SET";
    }

    @Override
    public void trigger(ChipState chip) {
        // Clock trigger!
        FlexiBlockDescription desc = getDescription(chip);
        //Invalid description
        if (desc == null) {
            return;
        }
        Block setBlock = getSign().getBlock().getRelative(desc.xOff, desc.yOff, desc.zOff);

        boolean clock = chip.get(0);
        if (clock) {
            if(desc.blockType == -1) {
                setBlock.setTypeId(desc.blockId);
            } else {
                setBlock.setTypeIdAndData(desc.blockId, desc.blockType, true);
            }
        } else if (desc.hold) {
            if (desc.toggleBlockType == -1) {
                setBlock.setTypeId(desc.toggleBlockId);
            } else {
                setBlock.setTypeIdAndData(desc.toggleBlockId, desc.toggleBlockType, true);
            }
        }
    }

    private FlexiBlockDescription getDescription(ChipState chip) {
        FlexiBlockDescription d = new FlexiBlockDescription();
        try {
            Sign s = getSign();
            /* Parse position */
            String[] posAndBlock = s.getLine(2).split(":", 2);
            switch (posAndBlock[0].toLowerCase().charAt(0)) {
            case 'y':
                d.yOff = Integer.parseInt(posAndBlock[0].substring(2));
                break;
            case 'x':
                d.xOff = Integer.parseInt(posAndBlock[0].substring(2));
                break;
            case 'z':
                d.zOff = Integer.parseInt(posAndBlock[0].substring(2));
                break;
            }
            /* Parse delta */
            if (!(posAndBlock[0].charAt(1) == '+' | posAndBlock[0].charAt(1) == '-')) {
                return null;
            } else if (posAndBlock[0].charAt(1) == '-') {
                d.xOff *= -1;
                d.yOff *= -1;
                d.zOff *= -1;
            }
            /* Parse block information */
            if (posAndBlock[1].contains(":")) {
                String[] blockAndType = posAndBlock[1].split(":", 2);
                d.blockId = Integer.parseInt(blockAndType[0]);
                d.blockType = Byte.parseByte(blockAndType[1]);
            } else {
                d.blockId = Integer.parseInt(posAndBlock[1]);
                d.blockType = -1;
            }
            /* Parse optional hold and toggle settings */
            String[] holdAndToggle = s.getLine(3).trim().split(":", 2);
            if (holdAndToggle.length == 1) {
                d.hold = holdAndToggle[0].equalsIgnoreCase("h");
                d.toggleBlockId = 0;
                d.toggleBlockType = -1;
            } else if(holdAndToggle.length == 2) {
                d.hold = holdAndToggle[0].equalsIgnoreCase("h");
                if (holdAndToggle[1].contains(":")) {
                    String[] blockAndType = holdAndToggle[1].split(":", 2);
                    d.toggleBlockId = Integer.parseInt(blockAndType[0]);
                    d.toggleBlockType = Byte.parseByte(blockAndType[1]);
                } else {
                    d.toggleBlockId = Integer.parseInt(holdAndToggle[1]);
                    d.toggleBlockType = -1;
                }
            } else {
                d.hold = false;
            }
        } catch (Exception e) {
            return null;
        }
        return d;
    }

    private class FlexiBlockDescription {
        int xOff, yOff, zOff;
        boolean hold;
        int blockId;
        byte blockType;
        int toggleBlockId;
        byte toggleBlockType;
    }

    public static class Factory extends AbstractICFactory {
        public Factory(Server server) {
            super(server);
        }

        @Override
        public IC create(Sign sign) {
            return new FlexibleSetBlock(getServer(), sign);
        }
    }

}
