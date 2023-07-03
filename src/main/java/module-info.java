module me.t3sl4.hesaplama {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.desktop;
    requires itext.xtra;
    requires itextpdf;

    opens me.t3sl4.hesaplama to javafx.fxml;
    exports me.t3sl4.hesaplama;
    exports me.t3sl4.hesaplama.ui;
    opens me.t3sl4.hesaplama.ui to javafx.fxml;
    opens me.t3sl4.hesaplama.hydraulic to javafx.base;
}