package com.sk89q.craftbook.gates.world.blocks;

import com.sk89q.craftbook.ChangedSign;
import com.sk89q.craftbook.ic.ChipState;
import com.sk89q.craftbook.ic.IC;
import com.sk89q.craftbook.ic.ICFactory;
import com.sk89q.craftbook.ic.SelfTriggeredIC;
import org.bukkit.Server;

public class CultivatorST extends Cultivator implements SelfTriggeredIC {

    public CultivatorST(Server server, ChangedSign sign, ICFactory factory) {

        super(server, sign, factory);
    }

    @Override
    public String getTitle() {

        return "Self-Triggered Cultivator";
    }

    @Override
    public String getSignTitle() {

        return "CULTIVATOR ST";
    }

    @Override
    public boolean isActive() {

        return true;
    }

    @Override
    public void think(ChipState state) {

        state.setOutput(0, cultivate());
    }

    public static class Factory extends Cultivator.Factory {

        public Factory(Server server) {

            super(server);
        }

        @Override
        public IC create(ChangedSign sign) {

            return new CultivatorST(getServer(), sign, this);
        }
    }
}