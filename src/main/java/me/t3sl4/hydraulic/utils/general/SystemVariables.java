package me.t3sl4.hydraulic.utils.general;

import lombok.Getter;
import me.t3sl4.hydraulic.utils.database.Model.HydraulicData.HydraulicData;
import me.t3sl4.hydraulic.utils.service.UserDataService.User;

public class SystemVariables {
    @Getter
    public static boolean offlineMode = false;
    public static String otpSentTime;

    private static final String CURRENT_VERSION = "v3.4.8";

    public static String BASE_URL = "https://ondergrup.hidirektor.com.tr/api/v2";
    public static String RELEASE_URL = "https://github.com/hidirektor/ondergrup-hydraulic-tool/releases";
    public static String NEW_VERSION_URL = "https://github.com/hidirektor/ondergrup-hydraulic-tool/releases/latest";
    public static String ASSET_URL = "https://api.github.com/repos/hidirektor/ondergrup-hydraulic-tool/releases/latest";

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

    public static String checkLicenseUrlPrefix = "/license/check";
    public static String activateLicenseUrlPrefix = "/license/activate";

    // Local Data
    @Getter
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
}
