package me.t3sl4.hydraulic.Utils.Database.File.Excel;

import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Utils.Database.Model.HydraulicData.HydraulicData;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExcelUtil {

    private static final Logger logger = Logger.getLogger(ExcelUtil.class.getName());

    public static void excelDataRead() {
        /*
        Power Pack
         */
        readExcel4ParcaHidrosMotor380(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaHidrosMotor220(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaHidrosPompa(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaHidrosPompaCivata(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaHidrosTankDikey(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaHidrosTankYatay(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaHidrosValfDikeyCift(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaHidrosValfDikeyTek(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaHidrosValfYatayTek(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaHidrosValfYatayCift(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaHidrosValfDikeyCiftESP(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaHidrosValfDikeyTekESP(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaHidrosValfYatayTekESP(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaHidrosValfYatayCiftESP(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaHidrosPlatformDevirmeli(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaHidrosGenel(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaHidrosTam(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaHidrosTamYatay(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaHidrosTamDikey(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaHidrosTamESPYok(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaHidrosOzelTekValf(Launcher.excelDBPath, Launcher.getDataManipulator());
    }

    public static void readExcel4ParcaHidrosMotor380(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Hidros-Motor380";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell keyCell = row.getCell(0);
                Cell valueCell1 = row.getCell(1);
                Cell valueCell2 = row.getCell(2);

                String key = keyCell.getStringCellValue();
                String value1 = valueCell1.getStringCellValue();
                String value2 = String.valueOf(valueCell2.getNumericCellValue());

                if(!key.isEmpty()) {
                    HashMap<String, String> innerMap = new HashMap<>();
                    innerMap.put("B", value1);
                    innerMap.put("C", value2);
                    hydraulicData.hidros380Parca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosMotor220(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Hidros-Motor220";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell keyCell = row.getCell(0);
                Cell valueCell1 = row.getCell(1);
                Cell valueCell2 = row.getCell(2);

                String key = keyCell.getStringCellValue();
                String value1 = valueCell1.getStringCellValue();
                String value2 = String.valueOf(valueCell2.getNumericCellValue());

                if(!key.isEmpty()) {
                    HashMap<String, String> innerMap = new HashMap<>();
                    innerMap.put("B", value1);
                    innerMap.put("C", value2);
                    hydraulicData.hidros220Parca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosPompa(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Hidros-Pompa";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell keyCell = row.getCell(0);
                Cell valueCell1 = row.getCell(1);
                Cell valueCell2 = row.getCell(2);

                String key = keyCell.getStringCellValue();
                String value1 = valueCell1.getStringCellValue();
                String value2 = String.valueOf(valueCell2.getNumericCellValue());

                if(!key.isEmpty()) {
                    HashMap<String, String> innerMap = new HashMap<>();
                    innerMap.put("B", value1);
                    innerMap.put("C", value2);
                    hydraulicData.hidrosPompaParca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosPompaCivata(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Hidros-Pompa-Civata";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell keyCell = row.getCell(0);
                Cell valueCell1 = row.getCell(1);
                Cell valueCell2 = row.getCell(2);

                String key = keyCell.getStringCellValue();
                String value1 = valueCell1.getStringCellValue();
                String value2 = String.valueOf(valueCell2.getNumericCellValue());

                if(!key.isEmpty()) {
                    HashMap<String, String> innerMap = new HashMap<>();
                    innerMap.put("B", value1);
                    innerMap.put("C", value2);
                    hydraulicData.hidrosPompaCivataParca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosTankDikey(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Hidros-Tank-Dikey";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell keyCell = row.getCell(0);
                Cell valueCell1 = row.getCell(1);
                Cell valueCell2 = row.getCell(2);

                String key = keyCell.getStringCellValue();
                String value1 = valueCell1.getStringCellValue();
                String value2 = String.valueOf(valueCell2.getNumericCellValue());

                if(!key.isEmpty()) {
                    HashMap<String, String> innerMap = new HashMap<>();
                    innerMap.put("B", value1);
                    innerMap.put("C", value2);
                    hydraulicData.hidrosDikeyTankParca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosTankYatay(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Hidros-Tank-Yatay";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell keyCell = row.getCell(0);
                Cell valueCell1 = row.getCell(1);
                Cell valueCell2 = row.getCell(2);

                String key = keyCell.getStringCellValue();
                String value1 = valueCell1.getStringCellValue();
                String value2 = String.valueOf(valueCell2.getNumericCellValue());

                if(!key.isEmpty()) {
                    HashMap<String, String> innerMap = new HashMap<>();
                    innerMap.put("B", value1);
                    innerMap.put("C", value2);
                    hydraulicData.hidrosYatayTankParca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosValfDikeyCift(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Hidros-Valf-Dikey-Çift";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell keyCell = row.getCell(0);
                Cell valueCell1 = row.getCell(1);
                Cell valueCell2 = row.getCell(2);

                String key = keyCell.getStringCellValue();
                String value1 = valueCell1.getStringCellValue();
                String value2 = String.valueOf(valueCell2.getNumericCellValue());

                if(!key.isEmpty()) {
                    HashMap<String, String> innerMap = new HashMap<>();
                    innerMap.put("B", value1);
                    innerMap.put("C", value2);
                    hydraulicData.hidrosDikeyCiftHizParca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosValfDikeyTek(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Hidros-Valf-Dikey-Tek";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell keyCell = row.getCell(0);
                Cell valueCell1 = row.getCell(1);
                Cell valueCell2 = row.getCell(2);

                String key = keyCell.getStringCellValue();
                String value1 = valueCell1.getStringCellValue();
                String value2 = String.valueOf(valueCell2.getNumericCellValue());

                if(!key.isEmpty()) {
                    HashMap<String, String> innerMap = new HashMap<>();
                    innerMap.put("B", value1);
                    innerMap.put("C", value2);
                    hydraulicData.hidrosDikeyTekHizParca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosValfYatayTek(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Hidros-Valf-Yatay-Tek";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell keyCell = row.getCell(0);
                Cell valueCell1 = row.getCell(1);
                Cell valueCell2 = row.getCell(2);

                String key = keyCell.getStringCellValue();
                String value1 = valueCell1.getStringCellValue();
                String value2 = String.valueOf(valueCell2.getNumericCellValue());

                if(!key.isEmpty()) {
                    HashMap<String, String> innerMap = new HashMap<>();
                    innerMap.put("B", value1);
                    innerMap.put("C", value2);
                    hydraulicData.hidrosYatayTekHizParca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosValfYatayCift(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Hidros-Valf-Yatay-Çift";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell keyCell = row.getCell(0);
                Cell valueCell1 = row.getCell(1);
                Cell valueCell2 = row.getCell(2);

                String key = keyCell.getStringCellValue();
                String value1 = valueCell1.getStringCellValue();
                String value2 = String.valueOf(valueCell2.getNumericCellValue());

                if(!key.isEmpty()) {
                    HashMap<String, String> innerMap = new HashMap<>();
                    innerMap.put("B", value1);
                    innerMap.put("C", value2);
                    hydraulicData.hidrosYatayCiftHizParca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosValfDikeyCiftESP(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Hidros-Valf-Dikey-ÇiftESP";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell keyCell = row.getCell(0);
                Cell valueCell1 = row.getCell(1);
                Cell valueCell2 = row.getCell(2);

                String key = keyCell.getStringCellValue();
                String value1 = valueCell1.getStringCellValue();
                String value2 = String.valueOf(valueCell2.getNumericCellValue());

                if(!key.isEmpty()) {
                    HashMap<String, String> innerMap = new HashMap<>();
                    innerMap.put("B", value1);
                    innerMap.put("C", value2);
                    hydraulicData.hidrosDikeyCiftHizParcaESP.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosValfDikeyTekESP(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Hidros-Valf-Dikey-Tek";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell keyCell = row.getCell(0);
                Cell valueCell1 = row.getCell(1);
                Cell valueCell2 = row.getCell(2);

                String key = keyCell.getStringCellValue();
                String value1 = valueCell1.getStringCellValue();
                String value2 = String.valueOf(valueCell2.getNumericCellValue());

                if(!key.isEmpty()) {
                    HashMap<String, String> innerMap = new HashMap<>();
                    innerMap.put("B", value1);
                    innerMap.put("C", value2);
                    hydraulicData.hidrosDikeyTekHizParcaESP.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosValfYatayTekESP(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Hidros-Valf-Yatay-Tek";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell keyCell = row.getCell(0);
                Cell valueCell1 = row.getCell(1);
                Cell valueCell2 = row.getCell(2);

                String key = keyCell.getStringCellValue();
                String value1 = valueCell1.getStringCellValue();
                String value2 = String.valueOf(valueCell2.getNumericCellValue());

                if(!key.isEmpty()) {
                    HashMap<String, String> innerMap = new HashMap<>();
                    innerMap.put("B", value1);
                    innerMap.put("C", value2);
                    hydraulicData.hidrosYatayTekHizParcaESP.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosValfYatayCiftESP(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Hidros-Valf-Yatay-ÇiftESP";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell keyCell = row.getCell(0);
                Cell valueCell1 = row.getCell(1);
                Cell valueCell2 = row.getCell(2);

                String key = keyCell.getStringCellValue();
                String value1 = valueCell1.getStringCellValue();
                String value2 = String.valueOf(valueCell2.getNumericCellValue());

                if(!key.isEmpty()) {
                    HashMap<String, String> innerMap = new HashMap<>();
                    innerMap.put("B", value1);
                    innerMap.put("C", value2);
                    hydraulicData.hidrosYatayCiftHizParcaESP.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosPlatformDevirmeli(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Hidros-Platform-Devirmeli";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell keyCell = row.getCell(0);
                Cell valueCell1 = row.getCell(1);
                Cell valueCell2 = row.getCell(2);

                String key = keyCell.getStringCellValue();
                String value1 = valueCell1.getStringCellValue();
                String value2 = String.valueOf(valueCell2.getNumericCellValue());

                if(!key.isEmpty()) {
                    HashMap<String, String> innerMap = new HashMap<>();
                    innerMap.put("B", value1);
                    innerMap.put("C", value2);
                    hydraulicData.hidrosDevirmeliParca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosGenel(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Hidros-Genel";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell keyCell = row.getCell(0);
                Cell valueCell1 = row.getCell(1);
                Cell valueCell2 = row.getCell(2);

                String key = keyCell.getStringCellValue();
                String value1 = valueCell1.getStringCellValue();
                String value2 = "";
                if(valueCell2 != null) {
                    value2 = String.valueOf(valueCell2.getNumericCellValue());
                }

                if(!key.isEmpty()) {
                    HashMap<String, String> innerMap = new HashMap<>();
                    innerMap.put("B", value1);
                    innerMap.put("C", value2);
                    hydraulicData.hidrosGenelParca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosTam(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Hidros-Tam";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell keyCell = row.getCell(0);
                Cell valueCell1 = row.getCell(1);
                Cell valueCell2 = row.getCell(2);

                String key = keyCell.getStringCellValue();
                String value1 = valueCell1.getStringCellValue();
                String value2 = "";
                if(valueCell2 != null) {
                    value2 = String.valueOf(valueCell2.getNumericCellValue());
                }

                if(!key.isEmpty()) {
                    HashMap<String, String> innerMap = new HashMap<>();
                    innerMap.put("B", value1);
                    innerMap.put("C", value2);
                    hydraulicData.hidrosTamParca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosTamYatay(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Hidros-Tam-Yatay";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell keyCell = row.getCell(0);
                Cell valueCell1 = row.getCell(1);
                Cell valueCell2 = row.getCell(2);

                String key = keyCell.getStringCellValue();
                String value1 = valueCell1.getStringCellValue();
                String value2 = "";
                if(valueCell2 != null) {
                    value2 = String.valueOf(valueCell2.getNumericCellValue());
                }

                if(!key.isEmpty()) {
                    HashMap<String, String> innerMap = new HashMap<>();
                    innerMap.put("B", value1);
                    innerMap.put("C", value2);
                    hydraulicData.hidrosTamParcaYatay.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosTamDikey(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Hidros-Tam-Dikey";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell keyCell = row.getCell(0);
                Cell valueCell1 = row.getCell(1);
                Cell valueCell2 = row.getCell(2);

                String key = keyCell.getStringCellValue();
                String value1 = valueCell1.getStringCellValue();
                String value2 = "";
                if(valueCell2 != null) {
                    value2 = String.valueOf(valueCell2.getNumericCellValue());
                }

                if(!key.isEmpty()) {
                    HashMap<String, String> innerMap = new HashMap<>();
                    innerMap.put("B", value1);
                    innerMap.put("C", value2);
                    hydraulicData.hidrosTamParcaDikey.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosTamESPYok(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Hidros-ESP-Yok";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell keyCell = row.getCell(0);
                Cell valueCell1 = row.getCell(1);
                Cell valueCell2 = row.getCell(2);

                String key = keyCell.getStringCellValue();
                String value1 = valueCell1.getStringCellValue();
                String value2 = "";
                if(valueCell2 != null) {
                    value2 = String.valueOf(valueCell2.getNumericCellValue());
                }

                if(!key.isEmpty()) {
                    HashMap<String, String> innerMap = new HashMap<>();
                    innerMap.put("B", value1);
                    innerMap.put("C", value2);
                    hydraulicData.hidrosTamParcaESPHaric.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosOzelTekValf(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Hidros-Özel-Tek-Valf";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell keyCell = row.getCell(0);
                Cell valueCell1 = row.getCell(1);
                Cell valueCell2 = row.getCell(2);

                String key = keyCell.getStringCellValue();
                String value1 = valueCell1.getStringCellValue();
                String value2 = "";
                if(valueCell2 != null) {
                    value2 = String.valueOf(valueCell2.getNumericCellValue());
                }

                if(!key.isEmpty()) {
                    HashMap<String, String> innerMap = new HashMap<>();
                    innerMap.put("B", value1);
                    innerMap.put("C", value2);
                    hydraulicData.hidrosTamParcaOzelTekValf.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
