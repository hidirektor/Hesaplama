package me.t3sl4.hydraulic;

import me.t3sl4.hydraulic.MainModel.Main;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Launcher {
    public static String BASE_URL2 = "http://78.135.82.253:3000";
    public static String BASE_URL = "http://85.95.231.92:3000";

    public static String loginURLPrefix = "/api/login";
    public static String registerURLPrefix = "/api/register";
    public static String otpURLPrefix = "/api/sendOTP";


    public static String profileInfoURLPrefix = "/api/users/profileInfo/";
    public static String updatePassURLPrefix = "/api/users/updatePass";
    public static String updateProfileURLPrefix = "/api/users/updateProfile";
    public static String wholeProfileURLPrefix = "/api/users/getWholeProfileInfo";


    public static String hydraulicGetStatsURLPrefix = "/api/hydraulic/getStatistics";
    public static String hydraulicGetInfoURLPrefix = "/api/hydraulic/getHydraulicInfo";
    public static String insertHydraulicURLPrefix = "/api/hydraulic/insertHidrolik";
    public static String orderNumbersURLPrefix = "/api/hydraulic/orderNumbers";


    public static String fileViewURLPrefix = "/api/fileSystem/viewer/";
    public static String uploadURLPrefix = "/api/fileSystem/upload";
    public static String uploadPDFURLPrefix = "/api/fileSystem/uploadPDF";
    public static String uploadExcelURLPrefix = "/api/fileSystem/uploadExcel";
    public static String downloadPhotoURLPrefix = "/api/fileSystem/downloadPhoto";

    public static void main(String[] args) {
        Main.main(args);
    }
}
