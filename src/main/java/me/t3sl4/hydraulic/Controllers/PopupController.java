package me.t3sl4.hydraulic.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class PopupController implements Initializable {

    @FXML
    private TextField label1;
    @FXML
    private TextField label2;
    @FXML
    private TextField label3;
    @FXML
    private TextField label4;
    @FXML
    private TextField label5;
    @FXML
    private TextField label6;
    @FXML
    private TextField label7;
    @FXML
    private TextField label8;
    @FXML
    private TextField label9;
    @FXML
    private TextField label10;
    @FXML
    private TextField label11;
    @FXML
    private TextField label12;
    @FXML
    private TextField label13;
    @FXML
    private TextField label14;
    @FXML
    private TextField label15;
    @FXML
    private TextField label16;
    @FXML
    private TextField label17;
    @FXML
    private TextField label18;
    @FXML
    private TextField label19;

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
    private int kilitPlatformMotorBosluk;
    private int valfXBoslukSogutma;

    public void setValues(int kampanaBoslukX, int kampanaBoslukY, int valfBoslukX, int valfBoslukYArka, int valfBoslukYOn,
                          int kilitliBlokAraBoslukX, int tekHizAraBoslukX, int ciftHizAraBoslukX, int kompanzasyonTekHizAraBoslukX,
                          int sogutmaAraBoslukX, int sogutmaAraBoslukYkOn, int sogutmaAraBoslukYkArka, int kilitMotorKampanaBosluk,
                          int kilitMotorMotorBoslukX, int kilitMotorBoslukYOn, int kilitMotorBoslukYArka, int kayipLitre, int kilitPlatformMotorBosluk, int valfXBoslukSogutma) {
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
        this.kilitPlatformMotorBosluk = kilitPlatformMotorBosluk;
        this.valfXBoslukSogutma = valfXBoslukSogutma;
    }

    public void showValues() {
        //Tabloya dönüştür
        label1.setText(String.valueOf(this.kampanaBoslukX));
        label2.setText(String.valueOf(this.kampanaBoslukY));
        label3.setText(String.valueOf(this.valfBoslukX));
        label4.setText(String.valueOf(this.valfBoslukYArka));
        label5.setText(String.valueOf(this.valfBoslukYOn));
        label6.setText(String.valueOf(this.kilitliBlokAraBoslukX));
        label7.setText(String.valueOf(this.tekHizAraBoslukX));
        label8.setText(String.valueOf(this.ciftHizAraBoslukX));
        label9.setText(String.valueOf(this.kompanzasyonTekHizAraBoslukX));
        label10.setText(String.valueOf(this.sogutmaAraBoslukX));
        label11.setText(String.valueOf(this.sogutmaAraBoslukYkOn));
        label12.setText(String.valueOf(this.sogutmaAraBoslukYkArka));
        label13.setText(String.valueOf(this.kilitMotorKampanaBosluk));
        label14.setText(String.valueOf(this.kilitMotorMotorBoslukX));
        label15.setText(String.valueOf(this.kilitMotorBoslukYOn));
        label16.setText(String.valueOf(this.kilitMotorBoslukYArka));
        label17.setText(String.valueOf(this.kayipLitre));
        label18.setText(String.valueOf(this.kilitPlatformMotorBosluk));
        label19.setText(String.valueOf(this.valfXBoslukSogutma));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Gerekli popup işlemleri...
    }
}