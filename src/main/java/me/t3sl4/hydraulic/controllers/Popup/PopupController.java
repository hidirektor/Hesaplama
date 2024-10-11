package me.t3sl4.hydraulic.controllers.Popup;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    @FXML
    private TextField label20;
    @FXML
    private TextField label21;
    @FXML
    private TextField label22;
    @FXML
    private TextField label23;
    @FXML
    private TextField label24;
    @FXML
    private TextField label25;
    @FXML
    private TextField label26;
    @FXML
    private TextField label27;
    @FXML
    private TextField label28;
    @FXML
    private TextField label29;
    @FXML
    private TextField label30;
    @FXML
    private TextField label31;
    @FXML
    private TextField label32;
    @FXML
    private TextField label33;

    private int kampanaTankArasiBoslukX;
    private int kampanaTankArasiBoslukY;
    private int kampanaBoslukYOn;

    private int tekHizTankArasiBoslukX;
    private int tekHizTankArasiBoslukY;
    private int tekHizAraBoslukX;
    private int tekHizYOn;
    private int tekHizBlokX;
    private int tekHizBlokY;

    private int ciftHizTankArasiBoslukX;
    private int ciftHizTankArasiBoslukY;
    private int ciftHizAraBoslukX;
    private int ciftHizYOn;
    private int ciftHizBlokX;
    private int ciftHizBlokY;

    private int kilitliBlokTankArasiBoslukX;
    private int kilitliBlokTankArasiBoslukY;
    private int kilitliBlokAraBoslukX;
    private int kilitliBlokYOn;
    private int kilitliBlokX;
    private int kilitliBlokY;

    private int kilitMotorTankArasiBoslukX;
    private int kilitMotorTankArasiBoslukY;
    private int kilitMotorAraBoslukX;
    private int kilitMotorYOn;
    private int tekHizKilitAyriY;
    private int tekHizKilitAyriYOn;
    private int ciftHizKilitAyriY;
    private int ciftHizKilitAyriYOn;
    private int kilitMotorX;
    private int kilitMotorY;

    private int kayipLitre;
    private int defaultHeight;

    public void setValues(int kampanaTankArasiBoslukX, int kampanaTankArasiBoslukY, int kampanaBoslukYOn, int tekHizTankArasiBoslukX, int tekHizTankArasiBoslukY, int tekHizAraBoslukX, int tekHizYOn, int tekHizBlokX, int tekHizBlokY, int ciftHizTankArasiBoslukX, int ciftHizTankArasiBoslukY, int ciftHizAraBoslukX, int ciftHizYOn, int ciftHizBlokX, int ciftHizBlokY, int kilitliBlokTankArasiBoslukX, int kilitliBlokTankArasiBoslukY, int kilitliBlokAraBoslukX, int kilitliBlokYOn, int kilitliBlokX, int kilitliBlokY, int kilitMotorTankArasiBoslukX, int kilitMotorTankArasiBoslukY, int kilitMotorAraBoslukX, int kilitMotorYOn, int tekHizKilitAyriY, int tekHizKilitAyriYOn, int ciftHizKilitAyriY, int ciftHizKilitAyriYOn, int kilitMotorX, int kilitMotorY, int kayipLitre, int defaultHeight) {
        this.kampanaTankArasiBoslukX = kampanaTankArasiBoslukX;
        this.kampanaTankArasiBoslukY = kampanaTankArasiBoslukY;
        this.kampanaBoslukYOn = kampanaBoslukYOn;
        this.tekHizTankArasiBoslukX = tekHizTankArasiBoslukX;
        this.tekHizTankArasiBoslukY = tekHizTankArasiBoslukY;
        this.tekHizAraBoslukX = tekHizAraBoslukX;
        this.tekHizYOn = tekHizYOn;
        this.tekHizBlokX = tekHizBlokX;
        this.tekHizBlokY = tekHizBlokY;
        this.ciftHizTankArasiBoslukX = ciftHizTankArasiBoslukX;
        this.ciftHizTankArasiBoslukY = ciftHizTankArasiBoslukY;
        this.ciftHizAraBoslukX = ciftHizAraBoslukX;
        this.ciftHizYOn = ciftHizYOn;
        this.ciftHizBlokX = ciftHizBlokX;
        this.ciftHizBlokY = ciftHizBlokY;
        this.kilitliBlokTankArasiBoslukX = kilitliBlokTankArasiBoslukX;
        this.kilitliBlokTankArasiBoslukY = kilitliBlokTankArasiBoslukY;
        this.kilitliBlokAraBoslukX = kilitliBlokAraBoslukX;
        this.kilitliBlokYOn = kilitliBlokYOn;
        this.kilitliBlokX = kilitliBlokX;
        this.kilitliBlokY = kilitliBlokY;
        this.kilitMotorTankArasiBoslukX = kilitMotorTankArasiBoslukX;
        this.kilitMotorTankArasiBoslukY = kilitMotorTankArasiBoslukY;
        this.kilitMotorAraBoslukX = kilitMotorAraBoslukX;
        this.kilitMotorYOn = kilitMotorYOn;
        this.tekHizKilitAyriY = tekHizKilitAyriY;
        this.tekHizKilitAyriYOn = tekHizKilitAyriYOn;
        this.ciftHizKilitAyriY = ciftHizKilitAyriY;
        this.ciftHizKilitAyriYOn = ciftHizKilitAyriYOn;
        this.kilitMotorX = kilitMotorX;
        this.kilitMotorY = kilitMotorY;
        this.kayipLitre = kayipLitre;
        this.defaultHeight = defaultHeight;
    }

    public void showValues() {
        //Tabloya dönüştür
        label1.setText(String.valueOf(this.kampanaTankArasiBoslukX));
        label2.setText(String.valueOf(this.kampanaTankArasiBoslukY));
        label3.setText(String.valueOf(this.kampanaBoslukYOn));
        label4.setText(String.valueOf(this.tekHizTankArasiBoslukX));
        label5.setText(String.valueOf(this.tekHizTankArasiBoslukY));
        label6.setText(String.valueOf(this.tekHizAraBoslukX));
        label7.setText(String.valueOf(this.tekHizYOn));
        label8.setText(String.valueOf(this.tekHizBlokX));
        label9.setText(String.valueOf(this.tekHizBlokY));
        label10.setText(String.valueOf(this.ciftHizTankArasiBoslukX));
        label11.setText(String.valueOf(this.ciftHizTankArasiBoslukY));
        label12.setText(String.valueOf(this.ciftHizAraBoslukX));
        label13.setText(String.valueOf(this.ciftHizYOn));
        label14.setText(String.valueOf(this.ciftHizBlokX));
        label15.setText(String.valueOf(this.ciftHizBlokY));
        label16.setText(String.valueOf(this.kilitliBlokTankArasiBoslukX));
        label17.setText(String.valueOf(this.kilitliBlokTankArasiBoslukY));
        label18.setText(String.valueOf(this.kilitliBlokAraBoslukX));
        label19.setText(String.valueOf(this.kilitliBlokYOn));
        label20.setText(String.valueOf(this.kilitliBlokX));
        label21.setText(String.valueOf(this.kilitliBlokY));
        label22.setText(String.valueOf(this.kilitMotorTankArasiBoslukX));
        label23.setText(String.valueOf(this.kilitMotorTankArasiBoslukY));
        label24.setText(String.valueOf(this.kilitMotorAraBoslukX));
        label25.setText(String.valueOf(this.kilitMotorYOn));
        label26.setText(String.valueOf(this.tekHizKilitAyriY));
        label27.setText(String.valueOf(this.tekHizKilitAyriYOn));
        label28.setText(String.valueOf(this.ciftHizKilitAyriY));
        label29.setText(String.valueOf(this.ciftHizKilitAyriYOn));
        label30.setText(String.valueOf(this.kilitMotorX));
        label31.setText(String.valueOf(this.kilitMotorY));
        label32.setText(String.valueOf(this.kayipLitre));
        label33.setText(String.valueOf(this.defaultHeight));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Gerekli popup işlemleri...
    }

    public void minimizeProgram() {
        if (label31 != null) {
            Stage stage = (Stage) label31.getScene().getWindow();
            stage.setIconified(true);
        }
    }
}