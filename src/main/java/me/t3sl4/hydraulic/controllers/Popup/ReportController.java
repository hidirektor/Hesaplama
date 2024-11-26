package me.t3sl4.hydraulic.controllers.Popup;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.controllers.Calculation.Classic.ClassicController;
import me.t3sl4.hydraulic.controllers.Calculation.PowerPack.PowerPackController;
import me.t3sl4.hydraulic.utils.general.SystemVariables;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReportController {

    @FXML
    private Button sendReportButton;

    @FXML
    private Button uploadFile;

    @FXML
    private TextField errorSubjectField;

    @FXML
    private TextArea errorDetailsArea;

    @FXML
    private TextArea uploadedFilesArea;

    @FXML
    private CheckBox programParameters;

    @FXML
    private CheckBox programFiles;

    private final ObservableList<File> uploadedFiles = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        uploadedFilesArea.setEditable(false);
        errorDetailsArea.setWrapText(true);
        errorDetailsArea.setScrollTop(Double.MAX_VALUE);

        // Sağ tıklamayı engelle
        uploadedFilesArea.setOnContextMenuRequested(event -> event.consume());

        // CheckBox programParameters ile ilişkilendirme
        programParameters.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if(ClassicController.hesaplamaBitti) {
                    //Klasik hesaplama yapılmıştır.
                    String inputText =
                            "Ünite Tipi: " + ClassicController.secilenUniteTipi + "\n" +
                                    "Sipariş Numarası: " + ClassicController.girilenSiparisNumarasi + "\n" +
                                    "Motor: " + ClassicController.secilenMotor + "\n" +
                                    "Soğutma: " + ClassicController.secilenSogutmaDurumu + "\n" +
                                    "Hidrolik Kilit: " + ClassicController.secilenHidrolikKilitDurumu + "\n" +
                                    "Pompa: " + ClassicController.secilenPompa + "\n" +
                                    "Gerekli Yağ Miktarı: " + ClassicController.girilenTankKapasitesiMiktari + "\n" +
                                    "Kompanzasyon: " + ClassicController.kompanzasyonDurumu + "\n" +
                                    "Valf Tipi: " + ClassicController.secilenValfTipi + "\n" +
                                    "Kilit Motor: " + ClassicController.secilenKilitMotor + "\n" +
                                    "Kilit Pompa: " + ClassicController.secilenKilitPompa + "\n";
                    errorDetailsArea.appendText(inputText);
                } else if(PowerPackController.hesaplamaBitti) {
                    //PowerPack hesaplama yapılmıştır.
                    String inputText =
                            "Ünite Tipi: " + PowerPackController.secilenUniteTipi + "\n" +
                                    "Sipariş Numarası: " + PowerPackController.girilenSiparisNumarasi + "\n" +
                                    "Motor Voltaj: " + PowerPackController.secilenMotorTipi + "\n" +
                                    "Ünite Tipi: " + PowerPackController.uniteTipiDurumu + "\n" +
                                    "Motor Gücü: " + PowerPackController.secilenMotorGucu + "\n" +
                                    "Pompa: " + PowerPackController.secilenPompa + "\n" +
                                    "Tank Tipi: " + PowerPackController.secilenTankTipi + "\n" +
                                    "Tank Kapasitesi: " + PowerPackController.secilenTankKapasitesi + "\n" +
                                    "Özel Tank Ölçüleri (GxDxY): " + PowerPackController.secilenOzelTankGenislik + "x" + PowerPackController.secilenOzelTankDerinlik + "x" + PowerPackController.secilenOzelTankYukseklik + "\n" +
                                    "Platform Tipi: " + PowerPackController.secilenPlatformTipi + "\n" +
                                    "1. Valf Tipi: " + PowerPackController.secilenBirinciValf + "\n" +
                                    "İniş Metodu: " + PowerPackController.secilenInisTipi + "\n" +
                                    "2. Valf Tipi: " + PowerPackController.secilenIkinciValf + "\n";
                    errorDetailsArea.appendText(inputText);
                } else {
                    //Hiçbir hesaplama yapılmamıştır.
                    showError("Bu özelliği sadece hesaplama yaptıktan sonra kullanabilirsiniz.");
                    programParameters.setSelected(false);
                }
            } else {
                //Yazılanı geri al
                String text = errorDetailsArea.getText();
                if(text != null) {
                    if(ClassicController.hesaplamaBitti) {
                        String inputText =
                                "Ünite Tipi: " + ClassicController.secilenUniteTipi + "\n" +
                                        "Sipariş Numarası: " + ClassicController.girilenSiparisNumarasi + "\n" +
                                        "Motor: " + ClassicController.secilenMotor + "\n" +
                                        "Soğutma: " + ClassicController.secilenSogutmaDurumu + "\n" +
                                        "Hidrolik Kilit: " + ClassicController.secilenHidrolikKilitDurumu + "\n" +
                                        "Pompa: " + ClassicController.secilenPompa + "\n" +
                                        "Gerekli Yağ Miktarı: " + ClassicController.girilenTankKapasitesiMiktari + "\n" +
                                        "Kompanzasyon: " + ClassicController.kompanzasyonDurumu + "\n" +
                                        "Valf Tipi: " + ClassicController.secilenValfTipi + "\n" +
                                        "Kilit Motor: " + ClassicController.secilenKilitMotor + "\n" +
                                        "Kilit Pompa: " + ClassicController.secilenKilitPompa + "\n";

                        errorDetailsArea.setText(text.replace(inputText, ""));
                    } else if(PowerPackController.hesaplamaBitti) {
                        String inputText =
                                "Ünite Tipi: " + PowerPackController.secilenUniteTipi + "\n" +
                                        "Sipariş Numarası: " + PowerPackController.girilenSiparisNumarasi + "\n" +
                                        "Motor Voltaj: " + PowerPackController.secilenMotorTipi + "\n" +
                                        "Ünite Tipi: " + PowerPackController.uniteTipiDurumu + "\n" +
                                        "Motor Gücü: " + PowerPackController.secilenMotorGucu + "\n" +
                                        "Pompa: " + PowerPackController.secilenPompa + "\n" +
                                        "Tank Tipi: " + PowerPackController.secilenTankTipi + "\n" +
                                        "Tank Kapasitesi: " + PowerPackController.secilenTankKapasitesi + "\n" +
                                        "Özel Tank Ölçüleri (GxDxY): " + PowerPackController.secilenOzelTankGenislik + "x" + PowerPackController.secilenOzelTankDerinlik + "x" + PowerPackController.secilenOzelTankYukseklik + "\n" +
                                        "Platform Tipi: " + PowerPackController.secilenPlatformTipi + "\n" +
                                        "1. Valf Tipi: " + PowerPackController.secilenBirinciValf + "\n" +
                                        "İniş Metodu: " + PowerPackController.secilenInisTipi + "\n" +
                                        "2. Valf Tipi: " + PowerPackController.secilenIkinciValf + "\n";

                        errorDetailsArea.setText(text.replace(inputText, ""));
                    }
                }
            }
        });

        // CheckBox programFiles ile ilişkilendirme
        programFiles.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                try {
                    addOutputFiles();
                } catch (Exception e) {
                    showError("Çıktı dosyaları eklenemedi.");
                    // Checkbox işaretini kaldır
                    programFiles.setSelected(false);
                }
            }
        });

        sendReportButton.setOnAction(e -> sendReport());
        uploadFile.setOnAction(e -> openFileChooser());
    }

    private void sendReport() {
        String subject = errorSubjectField.getText().trim();
        String details = errorDetailsArea.getText().trim();

        if (subject.isEmpty() || details.isEmpty()) {
            showError("Lütfen konu ve hata detayları alanlarını doldurun.");
            return;
        }

        // İşlemi burada gerçekleştirin
        showInfo("Hata bildirimi başarıyla gönderildi.");
    }

    private void openFileChooser() {
        if (uploadedFiles.size() >= 5) {
            showError("Maksimum 5 dosya yükleyebilirsiniz.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Dosya Seç");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Desteklenen Dosyalar", "*.pdf", "*.xlsx", "*.xls", "*.png", "*.jpg", "*.jpeg", "*.bmp")
        );

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(uploadFile.getScene().getWindow());
        if (selectedFiles != null) {
            int remainingSlots = 5 - uploadedFiles.size();
            if (selectedFiles.size() > remainingSlots) {
                showError("En fazla " + remainingSlots + " dosya daha yükleyebilirsiniz.");
                return;
            }

            for (File file : selectedFiles) {
                if (!uploadedFiles.contains(file)) {
                    uploadedFiles.add(file);
                    addFileToUploadedFilesArea(file);
                }
            }
        }
    }

    private void addFileToUploadedFilesArea(File file) {
        String fileName = file.getName();
        uploadedFilesArea.appendText(fileName + " [Sil]\n");

        // Sağ tıklama engellendiği için çift tıklama için listener ekleyelim
        uploadedFilesArea.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                handleFileDoubleClick(event, file);
            } else if (event.getClickCount() == 1 && event.getTarget() instanceof Text) {
                String clickedText = ((Text) event.getTarget()).getText();
                if (clickedText.contains("[Sil]")) {
                    deleteFile(file);
                }
            }
        });
    }

    private void deleteFile(File file) {
        uploadedFiles.remove(file);
        uploadedFilesArea.setText("");  // Mevcut yazıyı temizle
        for (File f : uploadedFiles) {
            addFileToUploadedFilesArea(f);  // Kalan dosyaları tekrar ekle
        }
        showInfo(file.getName() + " dosyası silindi.");
    }

    private void handleFileDoubleClick(MouseEvent event, File file) {
        if (event.getClickCount() == 2) {
            try {
                Desktop.getDesktop().open(file.getParentFile());
            } catch (IOException e) {
                showError("Klasör açılamadı.");
            }
        }
    }

    private void addOutputFiles() throws Exception {
        String pdfPath = "";
        String excelPath = "";

        // ClassicController veya PowerPackController'e göre dosya yollarını belirle
        if (ClassicController.hesaplamaBitti) {
            pdfPath = SystemVariables.pdfFileLocalPath + ClassicController.girilenSiparisNumarasi + ".pdf";
            excelPath = SystemVariables.excelFileLocalPath + ClassicController.girilenSiparisNumarasi + ".xlsx";
        } else if (PowerPackController.hesaplamaBitti) {
            pdfPath = SystemVariables.pdfFileLocalPath + PowerPackController.girilenSiparisNumarasi + ".pdf";
            excelPath = SystemVariables.excelFileLocalPath + PowerPackController.girilenSiparisNumarasi + ".xlsx";
        } else {
            showError("Hesaplama yapılmamış. Dosyalar eklenemez.");
            programFiles.setSelected(false); // CheckBox'ı işaretsiz yap
        }

        // Dosya varlığı kontrolü
        File pdfFile = new File(pdfPath);
        File excelFile = new File(excelPath);

        // İki dosya da mevcutsa
        if (pdfFile.exists() && excelFile.exists()) {
            addFileToUploadedFilesArea(pdfFile);
            addFileToUploadedFilesArea(excelFile);
            uploadedFiles.add(pdfFile);
            uploadedFiles.add(excelFile);
        }
        // Sadece PDF mevcutsa
        else if (pdfFile.exists()) {
            addFileToUploadedFilesArea(pdfFile);
            uploadedFiles.add(pdfFile);
        }
        // Sadece Excel mevcutsa
        else if (excelFile.exists()) {
            addFileToUploadedFilesArea(excelFile);
            uploadedFiles.add(excelFile);
        }
        // Hiçbiri yoksa hata
        else {
            showError("Hesaplama yapılmamış. Dosyalar eklenemez.");
            programFiles.setSelected(false); // CheckBox'ı işaretsiz yap
        }
    }


    private List<File> findOutputFiles() {
        // Çıktı dosyalarını burada belirle
        // Örnek olarak boş bir liste döndürüyoruz
        return new ArrayList<>();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Hata");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Bilgi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void popupKapat() {
        Stage stage = (Stage) sendReportButton.getScene().getWindow();
        stage.close();
    }
}