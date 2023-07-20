module me.t3sl4.hydraulic {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.desktop;
    requires itext.xtra;
    requires itextpdf;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens me.t3sl4.hydraulic to javafx.fxml;
    exports me.t3sl4.hydraulic;
    exports me.t3sl4.hydraulic.Controllers;
    exports me.t3sl4.hydraulic.Util;
    opens me.t3sl4.hydraulic.Controllers to javafx.fxml;
    opens me.t3sl4.hydraulic.Util to javafx.base;
}