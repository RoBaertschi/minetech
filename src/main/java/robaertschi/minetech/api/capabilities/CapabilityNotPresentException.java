package robaertschi.minetech.api.capabilities;

/*
 * CREDIT: https://github.com/Choonster-Minecraft-Mods/TestMod3/blob/1.19.x/src/main/java/choonster/testmod3/util/CapabilityNotPresentException.java
 * Under MIT-License: https://github.com/Choonster-Minecraft-Mods/TestMod3/blob/1.19.x/LICENSE.txt
 */

public class CapabilityNotPresentException extends RuntimeException {
    private static final String MESSAGE = "Required Capability not present";

    public CapabilityNotPresentException() {
        this(MESSAGE);
    }

    public CapabilityNotPresentException(final String message) {
        super(message);
    }

    public CapabilityNotPresentException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CapabilityNotPresentException(final Throwable cause) {
        this(MESSAGE, cause);
    }

    public CapabilityNotPresentException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
