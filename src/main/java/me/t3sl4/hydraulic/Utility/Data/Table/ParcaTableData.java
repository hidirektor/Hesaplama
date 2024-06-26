package me.t3sl4.hydraulic.Utility.Data.Table;

import javafx.beans.property.SimpleStringProperty;

public class ParcaTableData {
    private final SimpleStringProperty satir1Property;
    private final SimpleStringProperty satir2Property;
    private final SimpleStringProperty satir3Property;

    public ParcaTableData(String data1, String data2, String data3) {
        this.satir1Property = new SimpleStringProperty(data1);
        this.satir2Property = new SimpleStringProperty(data2);
        this.satir3Property = new SimpleStringProperty(data3);
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

    public String getSatir3Property() {
        return satir3Property.get();
    }

    public void setSatir3Property(String satir2) {
        satir3Property.set(satir2);
    }
}