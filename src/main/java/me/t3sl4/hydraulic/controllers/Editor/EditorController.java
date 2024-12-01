package me.t3sl4.hydraulic.controllers.Editor;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.utils.Utils;
import me.t3sl4.hydraulic.utils.database.Model.File.FileDescription;
import me.t3sl4.hydraulic.utils.database.Model.ModernEditor.ModernEditor;
import me.t3sl4.hydraulic.utils.general.Highlighter.JsonSyntaxHighlighter;
import me.t3sl4.hydraulic.utils.general.Highlighter.YamlSyntaxHighlighter;
import me.t3sl4.hydraulic.utils.general.SceneUtil;
import me.t3sl4.hydraulic.utils.general.SystemVariables;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.snakeyaml.engine.v2.api.LoadSettings;
import org.snakeyaml.engine.v2.api.lowlevel.Compose;
import org.snakeyaml.engine.v2.nodes.Node;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditorController {

    @FXML
    private Button saveButton;

    @FXML
    private JFXCheckBox classicEditor;

    @FXML
    private JFXCheckBox modernEditor;

    @FXML
    private ImageView minimizeImage;

    @FXML
    private JFXComboBox<String> fileComboBox;

    @FXML
    private Label fileDescription;

    @FXML
    private CodeArea fileContentArea;

    /*
    Modern Editör componentleri
     */
    @FXML
    private ScrollPane fileItemsScrollPane;

    @FXML
    private VBox fileItems;

    @FXML
    private JFXComboBox<String> firstKeyCombo;

    @FXML
    private JFXComboBox<String> secondKeyCombo;

    @FXML
    private Label topKeyTableLabel;

    @FXML
    private Label subKeyTableLabel;

    @FXML
    private Label keyTableLabel;

    @FXML
    private Label valueTableLabel;

    private final Map<String, FileDescription> fileDescriptions = new HashMap<>();

    private boolean isContentModified = false;
    private boolean validationPassed = false;

    public void initialize() {
        minimizeImage.toFront();

        fileContentArea.setParagraphGraphicFactory(LineNumberFactory.get(fileContentArea));

        fileDescriptions.put("cabins.json", new FileDescription("Kabin yapılandırma dosyası.", SystemVariables.cabinsDBPath));
        fileDescriptions.put("general.json", new FileDescription("Parametreleri içeren dosyadır.", SystemVariables.generalDBPath));
        fileDescriptions.put("part_origins_classic.yml", new FileDescription("Klasik parçaların orijinal isim ve stok kodu dosyasıdır.", SystemVariables.partOriginsClassicDBPath));
        fileDescriptions.put("part_origins_powerpack.yml", new FileDescription("PowerPack parçaların orijinal isim ve stok kodu dosyasıdır.", SystemVariables.partOriginsPowerPackDBPath));
        fileDescriptions.put("schematic_texts.yml", new FileDescription("Klasik hesaplama yönteminde yer alan 2D şemanın üzerinde ki yazıların yer aldığı dosyadır.", SystemVariables.schematicTextsDBPath));
        fileDescriptions.put("classic_combo.yml", new FileDescription("Klasik mod için kombo box (açılır liste) öğelerinin yer aldığı dosyadır.", SystemVariables.classicComboDBPath));
        fileDescriptions.put("classic_parts.yml", new FileDescription("Klasik mod için parça bilgilerini içeren dosyadır.", SystemVariables.classicPartsDBPath));
        fileDescriptions.put("powerpack_combo.yml", new FileDescription("PowerPack mod için kombo box (açılır liste) öğelerinin yer aldığı dosyadır.", SystemVariables.powerPackComboDBPath));
        fileDescriptions.put("powerpack_parts_hidros.yml", new FileDescription("PowerPack mod için Hidros parça bilgilerini içeren dosyadır.", SystemVariables.powerPackPartsHidrosDBPath));
        fileDescriptions.put("powerpack_parts_ithal.yml", new FileDescription("PowerPack mod için İthal parça bilgilerini içeren dosyadır.", SystemVariables.powerPackPartsIthalDBPath));

        fileComboBox.getItems().addAll(fileDescriptions.keySet());

        classicEditor.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                modernEditor.setSelected(false);
                classicMode();
            } else {
                fileComboBox.setDisable(true);
                fileComboBox.getSelectionModel().clearSelection();
                fileDescription.setVisible(false);
                fileDescription.setText("");
                fileContentArea.setVisible(false);
                fileContentArea.clear();
            }
        });

        modernEditor.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                classicEditor.setSelected(false);
                modernMode();
            } else {
                fileItemsScrollPane.setVisible(false);
                fileItems.setVisible(false);
                firstKeyCombo.setVisible(false);
                secondKeyCombo.setVisible(false);
                topKeyTableLabel.setVisible(false);
                subKeyTableLabel.setVisible(false);
                keyTableLabel.setVisible(false);
                valueTableLabel.setVisible(false);
            }
        });

        fileComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && classicEditor.isSelected()) {
                FileDescription description = fileDescriptions.get(newValue);
                if (description != null) {
                    fileDescription.setText("Açıklama: " + description.getDescription() + "\nPath: " + description.getPath());
                    fileDescription.setVisible(true);
                    fileContentArea.setVisible(true);
                    loadFileContent(description.getPath());
                } else {
                    fileDescription.setText("Dosya bilgisi bulunamadı.");
                }
            } else if(newValue != null && modernEditor.isSelected()) {
                //Modern Editör kodları
                fileContentArea.setVisible(false);
            } else {
                fileDescription.setText("Açıklama bulunamadı.");
                fileDescription.setVisible(false);
            }
        });

        fileContentArea.textProperty().addListener((observable, oldValue, newValue) -> {
            isContentModified = !newValue.equals(oldValue);
        });

        saveButton.setOnAction(event -> saveFile());
    }

    public void minimizeProgram() {
        if (saveButton != null) {
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.setIconified(true);
        }
    }

    private void classicMode() {
        System.out.println("Classic Mode seçildi");
        fileComboBox.setDisable(false);
    }

    private void modernMode() {
        System.out.println("Modern Mode seçildi");
        fileComboBox.setDisable(false);

        topKeyTableLabel.setVisible(true);
        subKeyTableLabel.setVisible(true);
        keyTableLabel.setVisible(true);
        valueTableLabel.setVisible(true);

        fileComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String path = fileDescriptions.get(newValue).getPath();
                ModernEditor modernEditor = new ModernEditor(List.of(ModernEditor.Operation.ADD, ModernEditor.Operation.EDIT, ModernEditor.Operation.DELETE), path, 5);
                displayFileKeys(modernEditor);
            }
        });
    }

    private void displayFileKeys(ModernEditor modernEditor) {
        fileItemsScrollPane.setVisible(true);
        fileItems.setVisible(true);

        fileItems.getChildren().clear(); // Mevcut öğeleri temizle

        firstKeyCombo.setVisible(true);
        firstKeyCombo.setDisable(false);
        firstKeyCombo.getItems().clear();

        // Tüm üst anahtarları ComboBox'a ekle
        modernEditor.getDataMap().forEach((key, value) -> {
            String[] keys = key.split("\\.", 2); // En fazla 2 parçaya böl
            String topKey = keys[0];
            if (!firstKeyCombo.getItems().contains(topKey)) {
                firstKeyCombo.getItems().add(topKey);
            }
        });

        // İlk ComboBox seçim olayları
        firstKeyCombo.setOnAction(event -> {
            String selectedTopKey = firstKeyCombo.getValue();
            System.out.println("Selected Top Key: " + selectedTopKey);

            // İlgili alt anahtarları yüklemek için ikinci ComboBox'u temizle ve doldur
            secondKeyCombo.getItems().clear();
            modernEditor.getDataMap().forEach((key, value) -> {
                if (key.startsWith(selectedTopKey + ".") && key.split("\\.").length > 1) {
                    String subKey = key.substring(selectedTopKey.length() + 1);

                    // Eğer subKey ".parts" içermiyorsa sonuna ekle
                    if (!subKey.endsWith(".parts")) {
                        subKey += ".parts";
                    }

                    // Alt anahtarları ikinci ComboBox'a ekle
                    if (!secondKeyCombo.getItems().contains(subKey)) {
                        secondKeyCombo.getItems().add(subKey);
                    }
                }
            });

            // Eğer alt anahtarlar varsa ComboBox'u göster, yoksa verileri yükle
            if (!secondKeyCombo.getItems().isEmpty()) {
                secondKeyCombo.setVisible(true);
                secondKeyCombo.setDisable(false);
            } else {
                loadKeyData(selectedTopKey, null, modernEditor);
            }
        });

        // İkinci ComboBox seçim olayları
        secondKeyCombo.setOnAction(event -> {
            String selectedSubKey = secondKeyCombo.getValue();
            System.out.println("Selected Sub Key: " + selectedSubKey);

            loadKeyData(firstKeyCombo.getValue(), selectedSubKey, modernEditor);
        });
    }

    private void loadKeyData(String topKey, String subKey, ModernEditor modernEditor) {
        fileItems.getChildren().clear(); // Mevcut öğeleri temizle

        modernEditor.getDataMap().forEach((key, value) -> {
            if (key.equals(topKey + (subKey != null ? "." + subKey : ""))) {
                System.out.println("Key: " + key + ", Value: " + value);

                try {
                    FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("fxml/EditorItem.fxml"));
                    javafx.scene.Node node = loader.load();

                    HBox itemC = (HBox) loader.getNamespace().get("itemC");
                    Label topKeyLabel = (Label) loader.getNamespace().get("topKeyLabel");
                    Label subKeyLabel = (Label) loader.getNamespace().get("subKeyLabel");
                    Label keyLabel = (Label) loader.getNamespace().get("keyLabel");
                    Label valueLabel = (Label) loader.getNamespace().get("valueLabel");
                    ImageView addIcon = (ImageView) loader.getNamespace().get("addIcon");
                    ImageView editIcon = (ImageView) loader.getNamespace().get("editIcon");
                    ImageView deleteIcon = (ImageView) loader.getNamespace().get("deleteIcon");

                    topKeyLabel.setText(topKey);
                    subKeyLabel.setText(subKey != null ? subKey : "N/A");

                    fileItems.getChildren().add(node);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadFileContent(String path) {
        try {
            String content = new String(Files.readAllBytes(new File(path).toPath()));
            fileContentArea.clear();
            fileContentArea.appendText(content);
            highlightSyntax(content);
        } catch (IOException e) {
            fileContentArea.clear();
            fileContentArea.appendText("Dosya yüklenemedi: " + e.getMessage());
        }
    }

    private void saveFile() {
        if (!isContentModified) {
            Utils.showErrorMessage("Dosyada değişiklik yok.", SceneUtil.getScreenOfNode(fileDescription), (Stage)fileDescription.getScene().getWindow());
            return;
        }

        String selectedFile = fileComboBox.getSelectionModel().getSelectedItem();
        if (selectedFile == null) {
            System.out.println("Lütfen bir dosya seçin.");
            return;
        }

        FileDescription description = fileDescriptions.get(selectedFile);
        if (description == null) {
            System.out.println("Geçersiz dosya seçimi.");
            return;
        }

        String content = fileContentArea.getText();
        validationPassed = validateFile(selectedFile, content);

        if (!validationPassed) {
            String errorMessage = "Dosya formatı hatalı. Satır: ";
            if (selectedFile.endsWith(".json")) {
                int jsonErrorLine = findJsonErrorLine(content);  // JSON hatasını kontrol et
                errorMessage += (jsonErrorLine - 1) + " ya da " + jsonErrorLine;
            } else if (selectedFile.endsWith(".yml") || selectedFile.endsWith(".yaml")) {
                int yamlErrorLine = findYamlErrorLine(content);  // YAML hatasını kontrol et
                errorMessage += (yamlErrorLine - 1) + " ya da " + yamlErrorLine;
            } else {
                errorMessage += "Geçersiz format.";
            }
            Utils.showErrorMessage(errorMessage, SceneUtil.getScreenOfNode(fileDescription), (Stage)fileDescription.getScene().getWindow());
            return;
        }

        try {
            if (selectedFile.endsWith(".yml") || selectedFile.endsWith(".yaml")) {
                LoadSettings settings = LoadSettings.builder()
                        .setUseMarks(true)
                        .build();

                Compose compose = new Compose(settings);

                InputStream yamlStream = Files.newInputStream(Paths.get(description.getPath()));

                Node node = compose.composeInputStream(yamlStream).orElseThrow(() -> new IOException("YAML okuma hatası"));

                Files.write(Paths.get(description.getPath()), content.getBytes());
            } else {
                Files.write(new File(description.getPath()).toPath(), content.getBytes());
            }
            isContentModified = false;
            Utils.showSuccessMessage("Dosya başarıyla kaydedildi.", SceneUtil.getScreenOfNode(fileDescription), (Stage)fileDescription.getScene().getWindow());
        } catch (IOException e) {
            Utils.showErrorMessage("Dosya kaydedilemedi: " + e.getMessage(), SceneUtil.getScreenOfNode(fileDescription), (Stage)fileDescription.getScene().getWindow());
        }
    }

    private boolean validateFile(String fileName, String content) {
        if (fileName.endsWith(".json")) {
            return isValidJson(content);
        } else if (fileName.endsWith(".yml") || fileName.endsWith(".yaml")) {
            return isValidYaml(content);
        }
        return false;
    }

    private boolean isValidJson(String content) {
        try {
            new com.fasterxml.jackson.databind.ObjectMapper().readTree(content);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean isValidYaml(String content) {
        try {
            new Yaml().load(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int findJsonErrorLine(String message) {
        try {
            new JSONObject(message);
        } catch (JSONException e) {
            String regex = "line\\s*(\\d+)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(e.getMessage());

            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }
        }

        return -1;
    }

    private int findYamlErrorLine(String content) {
        try {
            Yaml yaml = new Yaml();
            yaml.load(content);
        } catch (Exception e) {
            String message = e.getMessage();

            Pattern pattern = Pattern.compile("line\\s*(\\d+), column\\s*(\\d+):");
            Matcher matcher = pattern.matcher(message);
            int count = 0;

            while (matcher.find()) {
                count++;
                if (count == 2) {
                    return Integer.parseInt(matcher.group(1));
                }
            }
        }
        return -1;
    }

    private void highlightSyntax(String content) {
        if (fileComboBox.getSelectionModel().getSelectedItem().endsWith(".json")) {
            fileContentArea.setStyleSpans(0, JsonSyntaxHighlighter.getStyleSpans(content));
        } else if (fileComboBox.getSelectionModel().getSelectedItem().endsWith(".yml")) {
            fileContentArea.setStyleSpans(0, YamlSyntaxHighlighter.getStyleSpans(content));
        }
    }
}
