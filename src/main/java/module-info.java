module me.t3sl4.hesaplama {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.desktop;
    requires itext.xtra;
    requires itextpdf;
    requires org.apache.poi.poi;

    opens me.t3sl4.hesaplama to javafx.fxml;
    exports me.t3sl4.hesaplama;
    exports me.t3sl4.hesaplama.ui;
    exports me.t3sl4.hesaplama.hydraulic;
    opens me.t3sl4.hesaplama.ui to javafx.fxml;
    opens me.t3sl4.hesaplama.hydraulic to javafx.base;
}