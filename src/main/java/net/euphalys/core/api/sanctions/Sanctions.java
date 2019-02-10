package net.euphalys.core.api.sanctions;

import net.euphalys.api.sanctions.ISanctions;
import net.euphalys.api.sanctions.SanctionsType;

/**
 * @author Dinnerwolph
 */

public class Sanctions implements ISanctions {

    int sanctionsId, duration;
    SanctionsType type;
    String server, message;

    public Sanctions(int sanctionsId, SanctionsType type, int duration, String server, String message) {
        this.sanctionsId = sanctionsId;
        this.type = type;
        this.duration = duration;
        this.server = server;
        this.message = message;
    }

    public int getSanctionsId() {
        return sanctionsId;
    }

    public int getDuration() {
        return duration;
    }

    public SanctionsType getType() {
        return type;
    }

    public String getServer() {
        return server;
    }

    public String getMessage() {
        return message;
    }
}
