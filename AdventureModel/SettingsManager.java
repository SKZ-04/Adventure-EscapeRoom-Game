package AdventureModel;

import java.util.HashMap;

public class SettingsManager {
    private double volume;
    private static SettingsManager instance;

    public SettingsManager() {
        this.volume = 1;
    }
    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    /**
     * sets volume
     * @param volume
     */
    public void setVolume(double volume) {
        this.volume = volume;
    }

    /**
     * get volume
     * @return
     */
    public double getVolume() {
        return volume;
    }
}
