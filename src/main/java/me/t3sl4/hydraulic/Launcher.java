package me.t3sl4.hydraulic;

import me.t3sl4.hydraulic.Screens.Main;
import me.t3sl4.hydraulic.Utils.File.GeneralFileSystem;
import me.t3sl4.hydraulic.Utils.Model.Excel.DataManipulator;

public class Launcher {
    public static String BASE_URL = "https://ondergrup.hidirektor.com.tr/api/v2";

    public static String userName;
    public static String userID;
    public static String accessToken;
    public static String refreshToken;

    public static String otpSentTime;

    public static String loginURLPrefix = "/auth/login";
    public static String registerURLPrefix = "/auth/register";
    public static String uploadProfilePhotoURLPrefix = "/user/uploadProfilePhoto";
    public static String updatePassURLPrefix = "/auth/resetPass";

    public static String profileInfoURLPrefix = "/user/getProfile";
    public static String downloadPhotoURLPrefix = "/user/downloadProfilePhoto";
    public static String updateProfileURLPrefix = "/user/updateProfile";

    public static String otpURLPrefix = "/otp/sendMail";
    public static String verifyOTPURLPrefix = "/otp/verifyOTP";

    public static String createHydraulicURLPrefix = "/hydraulic/createHydraulicUnit";
    public static String orderNumbersURLPrefix = "/hydraulic/getOrderNumber";
    public static String getPartListURLPrefix = "/hydraulic/getPartList/";
    public static String getSchematicURLPrefix = "/hydraulic/getSchematic/";
    public static String hydraulicGetStatsURLPrefix = "/hydraulic/getHydraulicStats";
    public static String hydraulicGetDetailsURLPrefix = "/hydraulic/getHydraulicDetails";

    public static String mainPath;
    public static String tokenPath;
    public static String profilePhotoLocalPath;
    public static String pdfFileLocalPath;
    public static String excelFileLocalPath;
    public static String dataFileLocalPath;
    public static String excelDBPath;
    public static String generalDBPath;
    public static String cabinetesDBPath;
    public static String classicDBPath;
    public static String powerPackDBPath;

    public static DataManipulator dataManipulator = new DataManipulator();

    public static void main(String[] args) {
        System.setProperty("prism.allowhidpi", "false");

        if(System.getProperty("os.name").contains("Windows")) {
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.verbose", "true");
            System.setProperty("javafx.animation.fullspeed", "true");
        } else {
            System.setProperty("CG_PDF_VERBOSE", "1");
            System.setProperty("apple.awt.UIElement", "true");
        }
        System.setProperty("java.util.logging.level", "WARNING");

        GeneralFileSystem.firstLaunch();
        Main.main(args);
    }

    public static String getUserID() {
        return userID.replaceAll("userID: ", "");
    }

    public static String getAccessToken() {
        return accessToken.replaceAll("AccessToken: ", "");
    }

    public static String getRefreshToken() {
        return refreshToken.replaceAll("RefreshToken: ", "");
    }

    public static String getUserName() {
        return userName.replaceAll("userName: ", "");
    }

    public static DataManipulator getDataManipulator() {
        return dataManipulator;
    }
}
