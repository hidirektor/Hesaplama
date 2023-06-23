package me.t3sl4.hesaplama.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class PopupController implements Initializable {

    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Label label3;
    @FXML
    private Label label4;
    @FXML
    private Label label5;
    @FXML
    private Label label6;
    @FXML
    private Label label7;
    @FXML
    private Label label8;
    @FXML
    private Label label9;
    @FXML
    private Label label10;
    @FXML
    private Label label11;
    @FXML
    private Label label12;
    @FXML
    private Label label13;
    @FXML
    private Label label14;
    @FXML
    private Label label15;
    @FXML
    private Label label16;
    @FXML
    private Label label17;
    @FXML
    private Label label18;
    @FXML
    private Label label19;
    @FXML
    private Label label20;
    @FXML
    private Label label21;
    @FXML
    private Label label22;
    @FXML
    private Label label23;
    @FXML
    private Label label24;
    @FXML
    private Label label25;

    private int kampanaBoslukX;
    private int kampanaBoslukY;
    private int valfBoslukX;
    private int valfBoslukYArka;
    private int valfBoslukYOn;
    private int kilitliBlokAraBoslukX;
    private int tekHizAraBoslukX;
    private int ciftHizAraBoslukX;
    private int kompanzasyonTekHizAraBoslukX;
    private int sogutmaAraBoslukX;
    private int sogutmaAraBoslukYkOn;
    private int sogutmaAraBoslukYkArka;
    private int kilitMotorKampanaBosluk;
    private int kilitMotorMotorBoslukX;
    private int kilitMotorBoslukYOn;
    private int kilitMotorBoslukYArka;
    private int kayipLitre;

    public void setValues(int kampanaBoslukX, int kampanaBoslukY, int valfBoslukX, int valfBoslukYArka, int valfBoslukYOn,
                          int kilitliBlokAraBoslukX, int tekHizAraBoslukX, int ciftHizAraBoslukX, int kompanzasyonTekHizAraBoslukX,
                          int sogutmaAraBoslukX, int sogutmaAraBoslukYkOn, int sogutmaAraBoslukYkArka, int kilitMotorKampanaBosluk,
                          int kilitMotorMotorBoslukX, int kilitMotorBoslukYOn, int kilitMotorBoslukYArka, int kayipLitre) {
        this.kampanaBoslukX = kampanaBoslukX;
        this.kampanaBoslukY = kampanaBoslukY;
        this.valfBoslukX = valfBoslukX;
        this.valfBoslukYArka = valfBoslukYArka;
        this.valfBoslukYOn = valfBoslukYOn;
        this.kilitliBlokAraBoslukX = kilitliBlokAraBoslukX;
        this.tekHizAraBoslukX = tekHizAraBoslukX;
        this.ciftHizAraBoslukX = ciftHizAraBoslukX;
        this.kompanzasyonTekHizAraBoslukX = kompanzasyonTekHizAraBoslukX;
        this.sogutmaAraBoslukX = sogutmaAraBoslukX;
        this.sogutmaAraBoslukYkOn = sogutmaAraBoslukYkOn;
        this.sogutmaAraBoslukYkArka = sogutmaAraBoslukYkArka;
        this.kilitMotorKampanaBosluk = kilitMotorKampanaBosluk;
        this.kilitMotorMotorBoslukX = kilitMotorMotorBoslukX;
        this.kilitMotorBoslukYOn = kilitMotorBoslukYOn;
        this.kilitMotorBoslukYArka = kilitMotorBoslukYArka;
        this.kayipLitre = kayipLitre;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        label1.setText("kampanaBoslukX: " + this.kampanaBoslukX);
        label2.setText("kampanaBoslukY: " + kampanaBoslukY);
        label3.setText("valfBoslukX: " + valfBoslukX);
        label4.setText("valfBoslukYArka: " + valfBoslukYArka);
        label5.setText("valfBoslukYOn: " + valfBoslukYOn);
        label6.setText("kilitliBlokAraBoslukX: " + kilitliBlokAraBoslukX);
        label7.setText("tekHizAraBoslukX: " + tekHizAraBoslukX);
        label8.setText("ciftHizAraBoslukX: " + ciftHizAraBoslukX);
        label9.setText("kompanzasyonTekHizAraBoslukX: " + kompanzasyonTekHizAraBoslukX);
        label10.setText("sogutmaAraBoslukX: " + sogutmaAraBoslukX);
        label11.setText("sogutmaAraBoslukYkOn: " + sogutmaAraBoslukYkOn);
        label12.setText("sogutmaAraBoslukYkArka: " + sogutmaAraBoslukYkArka);
        label13.setText("kilitMotorKampanaBosluk: " + kilitMotorKampanaBosluk);
        label14.setText("kilitMotorMotorBoslukX: " + kilitMotorMotorBoslukX);
        label15.setText("kilitMotorBoslukYOn: " + kilitMotorBoslukYOn);
        label16.setText("kilitMotorBoslukYArka: " + kilitMotorBoslukYArka);
        label17.setText("kayipLitre: " + kayipLitre);
    }
}