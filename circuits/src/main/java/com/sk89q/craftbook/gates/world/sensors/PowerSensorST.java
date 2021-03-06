package com.sk89q.craftbook.gates.world.sensors;

import com.sk89q.craftbook.ChangedSign;
import com.sk89q.craftbook.ic.*;
import org.bukkit.Server;

/**
 * @author Silthus
 */
public class PowerSensorST extends PowerSensor implements SelfTriggeredIC {

    public PowerSensorST(Server server, ChangedSign block, ICFactory factory) {

        super(server, block, factory);
    }

    @Override
    public String getTitle() {

        return "Self Triggered Power Sensor";
    }

    @Override
    public String getSignTitle() {

        return "ST POWER SENSOR";
    }

    @Override
    public void think(ChipState state) {

        state.setOutput(0, isPowered());
    }

    @Override
    public boolean isActive() {

        return true;
    }

    public static class Factory extends PowerSensor.Factory implements RestrictedIC {

        public Factory(Server server) {

            super(server);
        }

        @Override
        public IC create(ChangedSign sign) {

            try {
                if (sign.getLine(1).equalsIgnoreCase("[MC0270]")) {
                    sign.setLine(1, "[MC0266]");
                    sign.update(false);
                }
            } catch (Exception ignored) {
            }
            return new PowerSensorST(getServer(), sign, this);
        }

        @Override
        public void verify(ChangedSign sign) throws ICVerificationException {

            ICUtil.verifySignSyntax(sign);
        }
    }
}
