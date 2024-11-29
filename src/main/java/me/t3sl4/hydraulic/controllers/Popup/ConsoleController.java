package me.t3sl4.hydraulic.controllers.Popup;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.yaml.snakeyaml.reader.StreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.UncheckedIOException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ConsoleController {
    @FXML
    private TextArea consoleOutput;

    @FXML
    public void initialize() {
        redirectStandardOut(consoleOutput);

        System.out.println("Önder Grup Debug Tool v1.0");
    }

    private void redirectStandardOut(TextArea area) {
        try {
            PipedInputStream in = new PipedInputStream();
            System.setOut(new PrintStream(new PipedOutputStream(in), true, UTF_8));

            Thread thread = new Thread(new StreamReader(in, area));
            thread.setDaemon(true);
            thread.start();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private static class StreamReader implements Runnable {

        private final StringBuilder buffer = new StringBuilder();
        private boolean notify = true;

        private final BufferedReader reader;
        private final TextArea textArea;

        StreamReader(InputStream input, TextArea textArea) {
            this.reader = new BufferedReader(new InputStreamReader(input, UTF_8));
            this.textArea = textArea;
        }

        @Override
        public void run() {
            try (reader) {
                int charAsInt;
                while ((charAsInt = reader.read()) != -1) {
                    synchronized (buffer) {
                        buffer.append((char) charAsInt);
                        if (notify) {
                            notify = false;
                            Platform.runLater(this::appendTextToTextArea);
                        }
                    }
                }
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }

        private void appendTextToTextArea() {
            synchronized (buffer) {
                textArea.appendText(buffer.toString());
                buffer.delete(0, buffer.length());
                notify = true;
            }
        }
    }
}
