package com.example.smartswitch;

public class FirebaseModel {
    private boolean Lamp;
    private boolean Switch;
    private String TimeSet;
    private Integer Type;

    public boolean isLamp() {
        return Lamp;
    }

    public boolean isSwitch() {
        return Switch;
    }

    public String getTimeSet() {
        return TimeSet;
    }

    public Integer getType() {
        return Type;
    }
}
