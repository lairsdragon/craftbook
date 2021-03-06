package com.sk89q.craftbook.gates.world.items;

import com.sk89q.craftbook.ChangedSign;
import com.sk89q.craftbook.ic.ChipState;
import com.sk89q.craftbook.ic.IC;
import com.sk89q.craftbook.ic.ICFactory;
import com.sk89q.craftbook.ic.SelfTriggeredIC;
import org.bukkit.Server;

public class SorterST extends Sorter implements SelfTriggeredIC {

    public SorterST(Server server, ChangedSign sign, ICFactory factory) {

        super(server, sign, factory);
    }

    @Override
    public String getTitle() {

        return "Self-Triggered Sorter";
    }

    @Override
    public String getSignTitle() {

        return "SORTER ST";
    }

    @Override
    public boolean isActive() {

        return true;
    }

    @Override
    public void think(ChipState state) {

        state.setOutput(0, sort());
    }

    public static class Factory extends Sorter.Factory {

        public Factory(Server server) {

            super(server);
        }

        @Override
        public IC create(ChangedSign sign) {

            return new SorterST(getServer(), sign, this);
        }
    }
}
