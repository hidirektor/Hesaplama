package me.t3sl4.hydraulic;

import me.t3sl4.hydraulic.Screens.Main;

public class Launcher {
    public static String BASE_URL = "http://ondergrup.hidirektor.com.tr:3000/api/v2";

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
    public static String hydraulicGetInfoURLPrefix = "/hydraulic/getHydraulicInfo";
    public static String hydraulicGetCustomInfoURLPrefix = "/hydraulic/getHydraulicCustomInfo";

    public static String mainPath;
    public static String tokenPath;
    public static String profilePhotoLocalPath;
    public static String pdfFileLocalPath;
    public static String excelFileLocalPath;
    public static String dataFileLocalPath;
    public static String excelDBPath;

    public static void main(String[] args) {
        if(System.getProperty("os.name").contains("Windows")) {
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.verbose", "true");
            System.setProperty("javafx.animation.fullspeed", "true");
        } else {
            System.setProperty("CG_PDF_VERBOSE", "1");
            System.setProperty("apple.awt.UIElement", "true");
        }
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
}
