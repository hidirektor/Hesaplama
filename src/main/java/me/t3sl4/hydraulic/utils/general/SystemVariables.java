package me.t3sl4.hydraulic.utils.general;

import me.t3sl4.hydraulic.utils.database.Model.HydraulicData.HydraulicData;
import me.t3sl4.hydraulic.utils.service.UserDataService.User;

public class SystemVariables {
    public static boolean offlineMode = false;
    public static String otpSentTime;

    public static final String CURRENT_VERSION = "v4.0.0";

    public static final String REPO_OWNER = "hidirektor";
    public static final String HYDRAULIC_REPO_NAME = "ondergrup-hydraulic-tool";

    public static final String PREF_NODE_NAME = "canicula";
    public static final String HYDRAULIC_PREF_KEY = "hydraulic_version";
    public static final String DISPLAY_PREF_KEY = "default_display";

    public static String BASE_URL = "https://ondergrup.hidirektor.com.tr/api/v2";

    public static String WEB_URL = "https://ondergrup.com";
    public static String developedBy = "Designed and Coded by\nHalil İbrahim Direktör";

    // Logged in User
    public static User loggedInUser;

    //Local Data Paths
    public static String mainPath;
    public static String tokenPath;
    public static String licensePath;
    public static String profilePhotoLocalPath;
    public static String pdfFileLocalPath;
    public static String excelFileLocalPath;
    public static String localHydraulicStatsPath;
    public static String dataFileLocalPath;
    public static String generalDBPath;
    public static String cabinsDBPath;

    public static String classicComboDBPath;
    public static String powerPackComboDBPath;
    public static String classicPartsDBPath;
    public static String powerPackPartsHidrosDBPath;
    public static String powerPackPartsIthalDBPath;
    public static String schematicTextsDBPath;
    public static String partOriginsClassicDBPath;
    public static String partOriginsPowerPackDBPath;

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
    public static String deleteHydraulicURLPrefix = "/hydraulic/deleteHydraulicUnit";
    public static String orderNumbersURLPrefix = "/hydraulic/getOrderNumber";
    public static String getPartListURLPrefix = "/hydraulic/getPartList/";
    public static String getSchematicURLPrefix = "/hydraulic/getSchematic/";
    public static String hydraulicGetStatsURLPrefix = "/hydraulic/getHydraulicStats";
    public static String hydraulicGetDetailsURLPrefix = "/hydraulic/getHydraulicDetails";

    public static String sendBugReportURLPrefix = "/authorized/sendErrorReport";

    public static String checkLicenseUrlPrefix = "/license/check";
    public static String activateLicenseUrlPrefix = "/license/activate";

    // Local Data
    public static HydraulicData localHydraulicData = new HydraulicData();

    public static void setOfflineMode(boolean offlineMode) {
        SystemVariables.offlineMode = offlineMode;
    }

    public static void setLocalHydraulicData(HydraulicData localHydraulicData) {
        SystemVariables.localHydraulicData = localHydraulicData;
    }

    public static String getVersion() {
        return CURRENT_VERSION;
    }

    public static HydraulicData getLocalHydraulicData() {
        return localHydraulicData;
    }

    public static boolean isOfflineMode() {
        return offlineMode;
    }
}
