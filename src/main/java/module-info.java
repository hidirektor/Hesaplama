module me.t3sl.hydraulic {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.desktop;
    requires itext.xtra;
    requires itextpdf;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.sql;
    requires org.json;
    requires com.google.gson;
    requires okhttp3;
    requires com.fasterxml.jackson.databind;

    opens me.t3sl4.hydraulic to javafx.fxml;
    exports me.t3sl4.hydraulic;
    exports me.t3sl4.hydraulic.Controllers;
    exports me.t3sl4.hydraulic.Util;
    opens me.t3sl4.hydraulic.Controllers to javafx.fxml;
    opens me.t3sl4.hydraulic.Util to javafx.base;
    exports me.t3sl4.hydraulic.Util.HTTP;
    opens me.t3sl4.hydraulic.Util.HTTP to javafx.base;
    exports me.t3sl4.hydraulic.Util.Table;
    opens me.t3sl4.hydraulic.Util.Table to javafx.base;
    exports me.t3sl4.hydraulic.Util.Data;
    opens me.t3sl4.hydraulic.Util.Data to javafx.base;
    exports me.t3sl4.hydraulic.Util.Gen;
    opens me.t3sl4.hydraulic.Util.Gen to javafx.base;
    exports me.t3sl4.hydraulic.MainModel;
    opens me.t3sl4.hydraulic.MainModel to javafx.fxml;
}