package me.t3sl4.hydraulic.utils.service.HTTP.Request.HydraulicUnit;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import me.t3sl4.hydraulic.app.Main;
import me.t3sl4.hydraulic.utils.Utils;
import me.t3sl4.hydraulic.utils.service.HTTP.HTTPMethod;

import java.io.IOException;

import static me.t3sl4.hydraulic.utils.general.SystemVariables.BASE_URL;
import static me.t3sl4.hydraulic.utils.general.SystemVariables.deleteHydraulicURLPrefix;

public class HydraulicService {

    public static void deleteHydraulicUnit(String jsonDeleteBody, Label lblErrors, String accessToken) throws IOException {
        String deleteHydraulicUnitUrl = BASE_URL + deleteHydraulicURLPrefix;

        HTTPMethod.sendAuthorizedJsonRequest(deleteHydraulicUnitUrl, "POST", jsonDeleteBody, accessToken, new HTTPMethod.RequestCallback() {
            @Override
            public void onSuccess(String profileInfoResponse) {
                Stage lblErrorsStage = (Stage) lblErrors.getScene().getWindow();
                Utils.showSuccessMessage("Hidrolik ünitesi silindi!", Main.defaultScreen, lblErrorsStage);
            }

            @Override
            public void onFailure() {
                Stage lblErrorsStage = (Stage) lblErrors.getScene().getWindow();
                Utils.showErrorMessage("Hidrolik ünitesi silinemedi!", Main.defaultScreen, lblErrorsStage);
            }
        });
    }
}
