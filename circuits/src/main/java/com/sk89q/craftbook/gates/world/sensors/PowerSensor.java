package com.sk89q.craftbook.gates.world.sensors;

import com.sk89q.craftbook.ChangedSign;
import com.sk89q.craftbook.ic.*;
import org.bukkit.Server;
import org.bukkit.block.Block;

/**
 * @author Silthus
 */
public class PowerSensor extends AbstractIC {

    private Block center;

    public PowerSensor(Server server, ChangedSign block, ICFactory factory) {

        super(server, block, factory);
    }

    @Override
    public void load() {

        center = ICUtil.parseBlockLocation(getSign());
    }

    @Override
    public String getTitle() {

        return "Power Sensor";
    }

    @Override
    public String getSignTitle() {

        return "POWER SENSOR";
    }

    @Override
    public void trigger(ChipState chip) {

        if (chip.getInput(0)) {
            chip.setOutput(0, isPowered());
        }
    }

    protected boolean isPowered() {

        return center.isBlockPowered() || center.isBlockIndirectlyPowered();
    }

    public static class Factory extends AbstractICFactory {

        public Factory(Server server) {

            super(server);
        }

        @Override
        public IC create(ChangedSign sign) {

            return new PowerSensor(getServer(), sign, this);
        }

        @Override
        public void verify(ChangedSign sign) throws ICVerificationException {

            ICUtil.verifySignSyntax(sign);
        }

        @Override
        public String getDescription() {

            return "Detects if offset block is powered.";
        }

        @Override
        public String[] getLineHelp() {

            String[] lines = new String[] {"x:y:z offset", null};
            return lines;
        }
    }
}
