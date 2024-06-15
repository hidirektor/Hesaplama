package me.t3sl4.hydraulic;

import me.t3sl4.hydraulic.Screens.Main;

public class Launcher {
    public static String BASE_URL = "http://85.95.231.92:3000";

    public static String loginURLPrefix = "/api/login";
    public static String getPassURLPrefix = "/api/getCipheredPass";
    public static String directLoginURLPrefix = "/api/directLogin";
    public static String registerURLPrefix = "/api/register";
    public static String otpURLPrefix = "/api/sendOTP";


    public static String profileInfoURLPrefix = "/api/users/profileInfo/";
    public static String updatePassURLPrefix = "/api/users/updatePass";
    public static String updateProfileURLPrefix = "/api/users/updateProfile";
    public static String wholeProfileURLPrefix = "/api/users/getWholeProfileInfo";


    public static String hydraulicGetStatsURLPrefix = "/api/hydraulic/getStatistics";
    public static String hydraulicGetInfoURLPrefix = "/api/hydraulic/getHydraulicInfo";
    public static String hydraulicGetCustomInfoURLPrefix = "/api/hydraulic/getCustomHydraulicInfo";
    public static String insertHydraulicURLPrefix = "/api/hydraulic/insertHidrolik";
    public static String orderNumbersURLPrefix = "/api/hydraulic/orderNumbers";


    public static String fileViewURLPrefix = "/api/fileSystem/viewer/";
    public static String uploadURLPrefix = "/api/fileSystem/upload";
    public static String uploadPDFURLPrefix = "/api/fileSystem/uploadPDF";
    public static String uploadExcelURLPrefix = "/api/fileSystem/uploadExcel";
    public static String downloadPhotoURLPrefix = "/api/fileSystem/downloadPhoto";

    public static String mainPath;
    public static String profilePhotoLocalPath;
    public static String pdfFileLocalPath;
    public static String excelFileLocalPath;
    public static String dataFileLocalPath;
    public static String loginFilePath;
    public static String excelDBPath;

    public static void main(String[] args) {
        if(System.getProperty("os.name").contains("Windows")) {
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.verbose", "true");
        } else {
            System.setProperty("CG_PDF_VERBOSE", "1");
        }
        Main.main(args);
    }
}
