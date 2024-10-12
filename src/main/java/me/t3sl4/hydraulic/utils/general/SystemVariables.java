package me.t3sl4.hydraulic.utils.general;

import me.t3sl4.hydraulic.utils.database.Model.HydraulicData.HydraulicData;

public class SystemVariables {
    public static boolean offlineMode = false;
    public static String otpSentTime;

    public static String BASE_URL = "https://ondergrup.hidirektor.com.tr/api/v2";

    public static String WEB_URL = "https://ondergrup.com";
    public static String developedBy = "Designed and Coded by\nHalil İbrahim Direktör";

    // Logged in User
    public static String userName;
    public static String userID;
    public static String accessToken;
    public static String refreshToken;

    //Local Data Paths
    public static String mainPath;
    public static String tokenPath;
    public static String profilePhotoLocalPath;
    public static String pdfFileLocalPath;
    public static String excelFileLocalPath;
    public static String dataFileLocalPath;
    public static String generalDBPath;
    public static String cabinsDBPath;

    public static String classicComboDBPath;
    public static String powerPackComboDBPath;
    public static String classicPartsDBPath;
    public static String powerPackPartsHidrosDBPath;
    public static String powerPackPartsIthalDBPath;
    public static String schematicTextsDBPath;

    //Used endpoints
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

    // Local Data
    public static HydraulicData localHydraulicData = new HydraulicData();

    public static boolean isOfflineMode() {
        return offlineMode;
    }

    public static void setOfflineMode(boolean offlineMode) {
        SystemVariables.offlineMode = offlineMode;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        SystemVariables.userName = userName;
    }

    public static String getUserID() {
        return userID;
    }

    public static void setUserID(String userID) {
        SystemVariables.userID = userID;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        SystemVariables.accessToken = accessToken;
    }

    public static String getRefreshToken() {
        return refreshToken;
    }

    public static void setRefreshToken(String refreshToken) {
        SystemVariables.refreshToken = refreshToken;
    }

    public static HydraulicData getLocalHydraulicData() {
        return localHydraulicData;
    }

    public static void setLocalHydraulicData(HydraulicData localHydraulicData) {
        SystemVariables.localHydraulicData = localHydraulicData;
    }
}
