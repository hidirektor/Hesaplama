package me.t3sl4.hydraulic.controllers.Editor;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.utils.Utils;
import me.t3sl4.hydraulic.utils.database.Model.File.FileDescription;
import me.t3sl4.hydraulic.utils.general.SceneUtil;
import me.t3sl4.hydraulic.utils.general.SystemVariables;
import org.json.JSONException;
import org.json.JSONObject;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.parser.ParserException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
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
    private JFXTextArea fileContentArea;

    private final Map<String, FileDescription> fileDescriptions = new HashMap<>();

    private boolean isContentModified = false;
    private boolean validationPassed = false;

    private static final Pattern JSON_PATTERN = Pattern.compile(
            "(\"[^\"]*\")|(\\{)|(\\})|(\\[)|(\\])|(:)|([0-9]+)|(\\s+)"
    );

    private static final Pattern YAML_PATTERN = Pattern.compile(
            "(\\w+)(:)|(\"[^\"]*\")|(\\{)|(\\})|(\\[)|(\\])|(\\s+)"
    );

    public void initialize() {
        minimizeImage.toFront();

        fileDescriptions.put("cabis.json", new FileDescription("Kabin yapılandırma dosyası.", SystemVariables.cabinsDBPath));
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
                fileDescription.setVisible(false);
            }
        });

        modernEditor.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                classicEditor.setSelected(false);
                modernMode();
            } else {
                fileComboBox.setDisable(true);
                fileDescription.setVisible(false);
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
    }

    private void loadFileContent(String path) {
        try {
            String content = new String(Files.readAllBytes(new File(path).toPath()));
            fileContentArea.setText(content);
        } catch (IOException e) {
            fileContentArea.setText("Dosya yüklenemedi: " + e.getMessage());
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
            if (selectedFile.endsWith(".yml")) {
                DumperOptions options = new DumperOptions();
                options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
                options.setPrettyFlow(true);

                Yaml yaml = new Yaml(options);

                Map<String, Object> data = parseYamlToMap(content);
                try (FileWriter writer = new FileWriter(description.getPath())) {
                    yaml.dump(data, writer);
                }
            } else {
                Files.write(new File(description.getPath()).toPath(), content.getBytes());
            }
            isContentModified = false;
            System.out.println("Dosya başarıyla kaydedildi.");
        } catch (IOException e) {
            System.out.println("Dosya kaydedilemedi: " + e.getMessage());
        }
    }

    private Map<String, Object> parseYamlToMap(String yamlContent) {
        Yaml yaml = new Yaml();
        return yaml.load(yamlContent);
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
}
