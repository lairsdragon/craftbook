package com.sk89q.craftbook.gates.world.miscellaneous;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import com.sk89q.craftbook.ChangedSign;
import com.sk89q.craftbook.bukkit.BukkitUtil;
import com.sk89q.craftbook.ic.AbstractIC;
import com.sk89q.craftbook.ic.AbstractICFactory;
import com.sk89q.craftbook.ic.ChipState;
import com.sk89q.craftbook.ic.IC;
import com.sk89q.craftbook.ic.ICFactory;
import com.sk89q.craftbook.jinglenote.JingleNoteManager;
import com.sk89q.craftbook.jinglenote.StringJingleSequencer;
import com.sk89q.craftbook.util.LocationUtil;
import com.sk89q.craftbook.util.RegexUtil;

public class Tune extends AbstractIC {

    StringJingleSequencer sequencer;
    JingleNoteManager jNote = new JingleNoteManager();

    public Tune (Server server, ChangedSign sign, ICFactory factory) {
        super(server, sign, factory);
    }

    @Override
    public String getTitle () {

        return "Tune Player";
    }

    @Override
    public String getSignTitle () {

        return "TUNE";
    }

    @Override
    public void trigger (ChipState chip) {

        if (chip.getInput(0)) {

            if (sequencer != null || jNote != null) {
                for (Player player : getServer().getOnlinePlayers()) {
                    jNote.stop(player);
                }
                jNote.stopAll();
            }
            sequencer = new StringJingleSequencer(tune,delay);
            for (Player player : getServer().getOnlinePlayers()) {
                if (player == null) {
                    continue;
                }
                if (radius > 0 && !LocationUtil.isWithinRadius(BukkitUtil.toSign(getSign()).getLocation(),
                        player.getLocation(), radius)) {
                    continue;
                }
                jNote.play(player, sequencer);
            }
        } else if (!chip.getInput(0) && sequencer != null) {
            sequencer.stop();
            for (Player player : getServer().getOnlinePlayers()) {
                jNote.stop(player);
            }
            jNote.stopAll();
        }
    }

    int radius;
    int delay;
    String tune;

    @Override
    public void load() {

        try {
            radius = Integer.parseInt(getSign().getLine(3));
        } catch (Exception ignored) {
            radius = -1;
        }

        if(getLine(2).contains(":")) {

            String[] split = RegexUtil.COLON_PATTERN.split(getLine(2), 2);
            try {
                delay = Integer.parseInt(split[0]);
            }
            catch(Exception e){
                delay = 2;
            }
            tune = split[1];
        }
        else {

            tune = getSign().getLine(2);
            delay = 2;
        }
    }

    @Override
    public void unload() {

        try {
            sequencer.stop();
            for (Player player : getServer().getOnlinePlayers()) {
                jNote.stop(player);
            }
            jNote.stopAll();
        } catch (Exception ignored) {
        }
    }

    public static class Factory extends AbstractICFactory {

        public Factory(Server server) {

            super(server);
        }

        @Override
        public String getDescription() {

            return "Plays a tune.";
        }

        @Override
        public String[] getLineHelp() {

            String[] lines = new String[] {"Delay:Tune", "Radius"};
            return lines;
        }

        @Override
        public IC create (ChangedSign sign) {
            return new Tune(getServer(), sign, this);
        }
    }
}