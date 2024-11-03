package me.t3sl4.hydraulic.utils.general;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import me.t3sl4.hydraulic.utils.Utils;

public class InternetService extends Service<Boolean> {

    @Override
    protected Task<Boolean> createTask() {
        return new Task<>() {
            @Override
            protected Boolean call() {
                return Utils.netIsAvailable();
            }
        };
    }
}