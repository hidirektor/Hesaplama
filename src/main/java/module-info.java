module me.t3sl.hydraulic {
    requires javafx.controls;
    requires javafx.fxml;


    requires java.desktop;
    requires itext.xtra;
    requires itextpdf;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.sql;
    requires org.json;
    requires com.google.gson;
    requires okhttp3;
    requires com.fasterxml.jackson.databind;
    requires org.apache.commons.codec;
    requires javafx.web;

    opens me.t3sl4.hydraulic to javafx.fxml;
    exports me.t3sl4.hydraulic;
    exports me.t3sl4.hydraulic.Screens.Controllers;
    exports me.t3sl4.hydraulic.Utility;
    opens me.t3sl4.hydraulic.Screens.Controllers to javafx.fxml;
    opens me.t3sl4.hydraulic.Utility to javafx.base;
    exports me.t3sl4.hydraulic.Utility.HTTPUtil;
    opens me.t3sl4.hydraulic.Utility.HTTPUtil to javafx.base;
    exports me.t3sl4.hydraulic.Utility.DataUtil.Table;
    opens me.t3sl4.hydraulic.Utility.DataUtil.Table to javafx.base;
    exports me.t3sl4.hydraulic.Utility.DataUtil.Excel;
    opens me.t3sl4.hydraulic.Utility.DataUtil.Excel to javafx.base;
    exports me.t3sl4.hydraulic.Utility.DataUtil.User;
    opens me.t3sl4.hydraulic.Utility.DataUtil.User to javafx.base;
    exports me.t3sl4.hydraulic.Utility.DataUtil.HydraulicUnit;
    opens me.t3sl4.hydraulic.Utility.DataUtil.HydraulicUnit to javafx.base;
    exports me.t3sl4.hydraulic.Screens;
    opens me.t3sl4.hydraulic.Screens to javafx.base, javafx.fxml;
}