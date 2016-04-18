package com.brotherhood.gravityshooter.gravity;

/**
 * Created by Wojtek on 2016-04-16.
 */
public class Orbit {
    private GravityBody center;//cialo wokol ktorego orbitujemy
    private float height;//wysokosc na ktorej orbitujemy
    private boolean set = false;

    public Orbit(GravityBody center, float height) {
        this.center = center;
        this.height = height;
    }

    public GravityBody getCenter() {
        return center;
    }

    public void setCenter(GravityBody center) {
        this.center = center;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean isSet() {
        return set;
    }

    public void setSet(boolean set) {
        this.set = set;
    }
}
