package me.t3sl4.hydraulic.utils.database.Model.ModernEditor;

import org.json.JSONException;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Provides functionality for editing, undoing, and managing changes to JSON/YAML files.
 */
public class ModernEditor {

    // Enum to define operation types
    public enum Operation {
        ADD, DELETE, EDIT
    }

    // Class fields
    private final List<Operation> supportedOperations;
    private final Path filePath;
    private final int undoLimit;
    private String currentFileContent;
    private final Map<String, Object> dataMap = new HashMap<>();
    private final Deque<String> changeHistory = new LinkedList<>();
    private final List<String> mainKeys = new ArrayList<>();
    private final List<String> subKeys = new ArrayList<>();

    /**
     * Initializes ModernEditor with the supported operations, file path, and undo limit.
     *
     * @param supportedOperations List of supported operations.
     * @param filePath            Path to the target file.
     * @param undoLimit           Maximum number of changes that can be undone.
     */
    public ModernEditor(List<Operation> supportedOperations, String filePath, int undoLimit) {
        this.supportedOperations = supportedOperations;
        this.filePath = Path.of(filePath);
        this.undoLimit = undoLimit;

        loadFileContent();
        parseData();
        populateKeys();
    }

    /**
     * Loads the content of the file into memory.
     */
    private void loadFileContent() {
        try {
            currentFileContent = Files.readString(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load file: " + filePath, e);
        }
    }

    /**
     * Parses the file content into a data map based on its format (JSON or YAML).
     */
    private void parseData() {
        if (filePath.toString().endsWith(".json")) {
            parseJson();
        } else if (filePath.toString().endsWith(".yml") || filePath.toString().endsWith(".yaml")) {
            parseYaml();
        } else {
            throw new UnsupportedOperationException("Unsupported file format: " + filePath);
        }
    }

    /**
     * Parses JSON content into the data map.
     */
    private void parseJson() {
        try {
            JSONObject jsonObject = new JSONObject(currentFileContent);
            for (String key : jsonObject.keySet()) {
                dataMap.put(key, jsonObject.get(key));
            }
        } catch (JSONException e) {
            throw new RuntimeException("Failed to parse JSON file: " + filePath, e);
        }
    }

    /**
     * Parses YAML content into the data map.
     */
    private void parseYaml() {
        try (InputStream inputStream = Files.newInputStream(filePath)) {
            Yaml yaml = new Yaml();
            Object yamlData = yaml.load(inputStream);
            mapYamlData(yamlData, "");
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse YAML file: " + filePath, e);
        }
    }

    /**
     * Recursively maps YAML data into the data map, handling specific key formats.
     *
     * @param yamlData  Current YAML data node.
     * @param parentKey Parent key for nested data.
     */
    private void mapYamlData(Object yamlData, String parentKey) {
        if (yamlData instanceof Map) {
            ((Map<?, ?>) yamlData).forEach((key, value) -> {
                String compositeKey = parentKey.isEmpty() ? key.toString() : parentKey + "." + key;

                if (value instanceof Map) {
                    Map<?, ?> valueMap = (Map<?, ?>) value;

                    if (valueMap.containsKey("parts")) {
                        dataMap.put(compositeKey + ".parts", valueMap.get("parts"));
                        //mapYamlData(valueMap.get("parts"), compositeKey + ".parts");
                    } else if (valueMap.containsKey("options")) {
                        dataMap.put(compositeKey + ".options", valueMap.get("options"));
                        //mapYamlData(valueMap.get("options"), compositeKey + ".options");
                    } else {
                        dataMap.put(compositeKey, value);
                        mapYamlData(value, compositeKey);
                    }
                } else if (value instanceof List) {
                    dataMap.put(compositeKey, value);
                } else {
                    dataMap.put(compositeKey, value);
                }
            });
        }
    }

    /**
     * Populates mainKeys and subKeys lists based on dataMap.
     */
    private void populateKeys() {
        for (String key : dataMap.keySet()) {
            if (!key.contains(".")) {
                mainKeys.add(key);
            } else {
                subKeys.add(key);
            }
        }
    }

    /**
     * Retrieves the list of main keys (top-level keys).
     *
     * @return List of main keys.
     */
    public List<String> getMainKeys() {
        return Collections.unmodifiableList(mainKeys);
    }

    /**
     * Retrieves the list of sub-keys (nested keys).
     *
     * @return List of sub-keys.
     */
    public List<String> getSubKeys() {
        return Collections.unmodifiableList(subKeys);
    }

    /**
     * Saves the updated content to the file and maintains a history for undo operations.
     *
     * @param newContent The new content to save.
     */
    public void saveChanges(String newContent) {
        if (changeHistory.size() >= undoLimit) {
            changeHistory.pollFirst(); // Remove the oldest change
        }
        changeHistory.offerLast(currentFileContent);
        writeToFile(newContent);
        currentFileContent = newContent;
    }

    /**
     * Writes the specified content to the file.
     *
     * @param content Content to write.
     */
    private void writeToFile(String content) {
        try {
            Files.writeString(filePath, content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to file: " + filePath, e);
        }
    }

    /**
     * Undoes the last change, if available.
     */
    public void undoChange() {
        if (!changeHistory.isEmpty()) {
            currentFileContent = changeHistory.pollLast();
            writeToFile(currentFileContent);
        } else {
            System.out.println("No changes to undo.");
        }
    }

    /**
     * Retrieves the data map representation of the file.
     *
     * @return Data map of the file content.
     */
    public Map<String, Object> getDataMap() {
        return Collections.unmodifiableMap(dataMap);
    }

    /**
     * Retrieves the current content of the file.
     *
     * @return Current file content.
     */
    public String getCurrentFileContent() {
        return currentFileContent;
    }

    /**
     * Retrieves the data for the specified topKey and subKey from the dataMap.
     *
     * @param topKey   The top-level key (main key).
     * @param subKey   The nested sub-key, can be null.
     * @return         The data corresponding to the specified topKey and subKey.
     */
    public Object getKeyData(String topKey, String subKey) {
        if (subKey == null) {
            return dataMap.get(topKey);
        }

        if (subKey.startsWith(topKey + ".")) {
            subKey = subKey.substring(topKey.length() + 1);
        }

        String compositeKey = topKey + "." + subKey;

        return dataMap.get(compositeKey);
    }
}