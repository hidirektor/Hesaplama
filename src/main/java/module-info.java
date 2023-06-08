module me.t3sl4.hesaplama {
    requires javafx.controls;
    requires javafx.fxml;


    opens me.t3sl4.hesaplama to javafx.fxml;
    exports me.t3sl4.hesaplama;
}