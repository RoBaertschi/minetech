package robaertschi.minetech.api.util;

/*
 * CREDIT:  https://github.com/Tutorials-By-Kaupenjoe/Forge-Tutorial-1.19 by Kaupenjoe
 * Under MIT-License: https://github.com/Tutorials-By-Kaupenjoe/Forge-Tutorial-1.19/blob/main/LICENSE
 */
/**
 * An Basic Screen. It adds an Energy and Fluid Info area if needed.
 * @author Kaupenjoe
 * @since 0.1.0-1.19.4
 * @version 1
 */
public class MouseUtil {
    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y) {
        return isMouseOver(mouseX, mouseY, x, y, 16);
    }

    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int size) {
        return isMouseOver(mouseX, mouseY, x, y, size, size);
    }

    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int sizeX, int sizeY) {
        return (mouseX >= x && mouseX <= x + sizeX) && (mouseY >= y && mouseY <= y + sizeY);
    }
}