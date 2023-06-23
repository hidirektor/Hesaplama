package me.t3sl4.hesaplama.hydraulic;

import javafx.beans.property.SimpleStringProperty;

public class TableData {
    private final SimpleStringProperty satir1Property;
    private final SimpleStringProperty satir2Property;

    public TableData(String satir1, String satir2) {
        this.satir1Property = new SimpleStringProperty(satir1);
        this.satir2Property = new SimpleStringProperty(satir2);
    }

    public String getSatir1Property() {
        return satir1Property.get();
    }

    public void setSatir1Property(String satir1) {
        satir1Property.set(satir1);
    }

    public String getSatir2Property() {
        return satir2Property.get();
    }

    public void setSatir2Property(String satir2) {
        satir2Property.set(satir2);
    }
}


