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
        readExcel4ParcaListesiKampana1k(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaListesiKampana2k(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaListesiPompa(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaListesiMotor(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaListesiKaplin(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaListesiValfBlok(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaListesiBasincSalteri(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaListesiStandart(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaListesiSogutucu(Launcher.excelDBPath, Launcher.getDataManipulator());

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

    public static void readExcel4ParcaListesiKampana1k(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Kampana-1K";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            Row variableValuesRow;

            int okunacakSatir = 1;

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 5; j++) {
                    String temp = "";
                    String temp1 = "", temp2 = "", temp3 = "";
                    variableValuesRow = sheet.getRow(okunacakSatir);

                    if (variableValuesRow != null) {
                        if (variableValuesRow.getCell(1) != null) {
                            if (isNumeric(variableValuesRow.getCell(1))) {
                                double temp1Value = variableValuesRow.getCell(1).getNumericCellValue();
                                temp1 = String.valueOf((int) temp1Value);
                            } else {
                                temp1 = String.valueOf(variableValuesRow.getCell(1));
                            }
                        }
                        if (variableValuesRow.getCell(2) != null) {
                            temp2 = String.valueOf(variableValuesRow.getCell(2));
                        }
                        if (variableValuesRow.getCell(3) != null) {
                            double temp3Value = variableValuesRow.getCell(3).getNumericCellValue();
                            temp3 = String.valueOf((int) temp3Value);
                        }

                        if (!temp1.isEmpty() && !temp2.isEmpty() && !temp3.isEmpty()) {
                            temp += temp1 + ";" + temp2 + ";" + temp3;

                            if (i == 0) {
                                hydraulicData.parcaListesiKampana2501k.add(temp);
                                okunacakSatir++;
                            } else if (i == 1) {
                                hydraulicData.parcaListesiKampana3001k.add(temp);
                                okunacakSatir++;
                            } else if (i == 2) {
                                hydraulicData.parcaListesiKampana3501k.add(temp);
                                okunacakSatir++;
                            } else if (i == 3) {
                                hydraulicData.parcaListesiKampana4001k.add(temp);
                                okunacakSatir++;
                            }
                        }
                    } else {
                        okunacakSatir++;
                    }
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaListesiKampana2k(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Kampana-2K";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            Row variableValuesRow;

            int okunacakSatir = 1;

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 5; j++) {
                    String temp = "";
                    String temp1 = "", temp2 = "", temp3 = "";
                    variableValuesRow = sheet.getRow(okunacakSatir);

                    if (variableValuesRow != null) {
                        if (variableValuesRow.getCell(1) != null) {
                            if (isNumeric(variableValuesRow.getCell(1))) {
                                double temp1Value = variableValuesRow.getCell(1).getNumericCellValue();
                                temp1 = String.valueOf((int) temp1Value);
                            } else {
                                temp1 = String.valueOf(variableValuesRow.getCell(1));
                            }
                        }
                        if (variableValuesRow.getCell(2) != null) {
                            temp2 = String.valueOf(variableValuesRow.getCell(2));
                        }
                        if (variableValuesRow.getCell(3) != null) {
                            double temp3Value = variableValuesRow.getCell(3).getNumericCellValue();
                            temp3 = String.valueOf((int) temp3Value);
                        }

                        if (!temp1.isEmpty() && !temp2.isEmpty() && !temp3.isEmpty()) {
                            temp += temp1 + ";" + temp2 + ";" + temp3;

                            if (i == 0) {
                                hydraulicData.parcaListesiKampana2502k.add(temp);
                                okunacakSatir++;
                            } else if (i == 1) {
                                hydraulicData.parcaListesiKampana3002k.add(temp);
                                okunacakSatir++;
                            } else if (i == 2) {
                                hydraulicData.parcaListesiKampana3502k.add(temp);
                                okunacakSatir++;
                            } else if (i == 3) {
                                hydraulicData.parcaListesiKampana4002k.add(temp);
                                okunacakSatir++;
                            }
                        }
                    } else {
                        okunacakSatir++;
                    }
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaListesiPompa(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Pompa";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            Row variableValuesRow;

            int okunacakSatir = 1;

            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 10; j++) {
                    String temp = "";
                    String temp1 = "", temp2 = "", temp3 = "";
                    variableValuesRow = sheet.getRow(okunacakSatir);

                    if (variableValuesRow != null) {
                        if (variableValuesRow.getCell(1) != null) {
                            if (isNumeric(variableValuesRow.getCell(1))) {
                                double temp1Value = variableValuesRow.getCell(1).getNumericCellValue();
                                temp1 = String.valueOf((int) temp1Value);
                            } else {
                                temp1 = String.valueOf(variableValuesRow.getCell(1));
                            }
                        }
                        if (variableValuesRow.getCell(2) != null) {
                            temp2 = String.valueOf(variableValuesRow.getCell(2));
                        }
                        if (variableValuesRow.getCell(3) != null) {
                            double temp3Value = variableValuesRow.getCell(3).getNumericCellValue();
                            temp3 = String.valueOf((int) temp3Value);
                        }

                        if (!temp1.isEmpty() && !temp2.isEmpty() && !temp3.isEmpty()) {
                            temp += temp1 + ";" + temp2 + ";" + temp3;

                            if(!temp.contains("hidirektor")) {
                                if (i == 0) {
                                    hydraulicData.parcaListesiPompa95.add(temp);
                                    okunacakSatir++;
                                } else if (i == 1) {
                                    hydraulicData.parcaListesiPompa119.add(temp);
                                    okunacakSatir++;
                                } else if (i == 2) {
                                    hydraulicData.parcaListesiPompa14.add(temp);
                                    okunacakSatir++;
                                } else if (i == 3) {
                                    hydraulicData.parcaListesiPompa146.add(temp);
                                    okunacakSatir++;
                                } else if (i == 4) {
                                    hydraulicData.parcaListesiPompa168.add(temp);
                                    okunacakSatir++;
                                } else if (i == 5) {
                                    hydraulicData.parcaListesiPompa192.add(temp);
                                    okunacakSatir++;
                                } else if (i == 6) {
                                    hydraulicData.parcaListesiPompa229.add(temp);
                                    okunacakSatir++;
                                } else if (i == 7) {
                                    hydraulicData.parcaListesiPompa281.add(temp);
                                    okunacakSatir++;
                                } else if (i == 8) {
                                    hydraulicData.parcaListesiPompa288.add(temp);
                                    okunacakSatir++;
                                } else if (i == 9) {
                                    hydraulicData.parcaListesiPompa333.add(temp);
                                    okunacakSatir++;
                                } else if (i == 10) {
                                    hydraulicData.parcaListesiPompa379.add(temp);
                                    okunacakSatir++;
                                } else if (i == 11) {
                                    hydraulicData.parcaListesiPompa426.add(temp);
                                    okunacakSatir++;
                                } else if (i == 12) {
                                    hydraulicData.parcaListesiPompa455.add(temp);
                                    okunacakSatir++;
                                } else if (i == 13) {
                                    hydraulicData.parcaListesiPompa494.add(temp);
                                    okunacakSatir++;
                                } else {
                                    hydraulicData.parcaListesiPompa561.add(temp);
                                    okunacakSatir++;
                                }
                            } else {
                                okunacakSatir++;
                            }
                        }
                    } else {
                        okunacakSatir++;
                    }
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaListesiMotor(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Motor";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            Row variableValuesRow;

            int okunacakSatir = 1;

            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 1; j++) {
                    String temp = "";
                    String temp1 = "", temp2 = "", temp3 = "";
                    variableValuesRow = sheet.getRow(okunacakSatir);

                    if (variableValuesRow != null) {
                        if (variableValuesRow.getCell(1) != null) {
                            if (isNumeric(variableValuesRow.getCell(1))) {
                                double temp1Value = variableValuesRow.getCell(1).getNumericCellValue();
                                temp1 = String.valueOf((int) temp1Value);
                            } else {
                                temp1 = String.valueOf(variableValuesRow.getCell(1));
                            }
                        }
                        if (variableValuesRow.getCell(2) != null) {
                            temp2 = String.valueOf(variableValuesRow.getCell(2));
                        }
                        if (variableValuesRow.getCell(3) != null) {
                            double temp3Value = variableValuesRow.getCell(3).getNumericCellValue();
                            temp3 = String.valueOf((int) temp3Value);
                        }

                        if (!temp1.isEmpty() && !temp2.isEmpty() && !temp3.isEmpty()) {
                            temp += temp1 + ";" + temp2 + ";" + temp3;

                            if (i == 0) {
                                hydraulicData.parcaListesiMotor202.add(temp);
                                okunacakSatir++;
                                break;
                            } else if (i == 1) {
                                hydraulicData.parcaListesiMotor3.add(temp);
                                okunacakSatir++;
                                break;
                            } else if (i == 2) {
                                hydraulicData.parcaListesiMotor4.add(temp);
                                okunacakSatir++;
                                break;
                            } else if (i == 3) {
                                hydraulicData.parcaListesiMotor55.add(temp);
                                okunacakSatir++;
                                break;
                            } else if (i == 4) {
                                hydraulicData.parcaListesiMotor55Kompakt.add(temp);
                                okunacakSatir++;
                            } else if (i == 5) {
                                hydraulicData.parcaListesiMotor75Kompakt.add(temp);
                                okunacakSatir++;
                                break;
                            } else if (i == 6) {
                                hydraulicData.parcaListesiMotor11.add(temp);
                                okunacakSatir++;
                                break;
                            } else if (i == 7) {
                                hydraulicData.parcaListesiMotor11Kompakt.add(temp);
                                okunacakSatir++;
                                break;
                            } else if (i == 8) {
                                hydraulicData.parcaListesiMotor15.add(temp);
                                okunacakSatir++;
                                break;
                            } else if (i == 9) {
                                hydraulicData.parcaListesiMotor185.add(temp);
                                okunacakSatir++;
                                break;
                            } else if (i == 10) {
                                hydraulicData.parcaListesiMotor22.add(temp);
                                okunacakSatir++;
                                break;
                            } else if (i == 11) {
                                hydraulicData.parcaListesiMotor37.add(temp);
                                okunacakSatir++;
                                break;
                            }
                        }
                    } else {
                        okunacakSatir++;
                    }
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaListesiKaplin(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Kaplin";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            Row variableValuesRow;

            int okunacakSatir = 1;

            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 1; j++) {
                    String temp = "";
                    String temp1 = "", temp2 = "", temp3 = "";
                    variableValuesRow = sheet.getRow(okunacakSatir);

                    if (variableValuesRow != null) {
                        if (variableValuesRow.getCell(1) != null) {
                            if (isNumeric(variableValuesRow.getCell(1))) {
                                double temp1Value = variableValuesRow.getCell(1).getNumericCellValue();
                                temp1 = String.valueOf((int) temp1Value);
                            } else {
                                temp1 = String.valueOf(variableValuesRow.getCell(1));
                            }
                        }
                        if (variableValuesRow.getCell(2) != null) {
                            temp2 = String.valueOf(variableValuesRow.getCell(2));
                        }
                        if (variableValuesRow.getCell(3) != null) {
                            double temp3Value = variableValuesRow.getCell(3).getNumericCellValue();
                            temp3 = String.valueOf((int) temp3Value);
                        }

                        if (!temp1.isEmpty() && !temp2.isEmpty() && !temp3.isEmpty()) {
                            temp += temp1 + ";" + temp2 + ";" + temp3;

                            if (i == 0) {
                                hydraulicData.parcaListesiKaplin1PN28.add(temp);
                                okunacakSatir++;
                            } else if (i == 1) {
                                hydraulicData.parcaListesiKaplin1PN38.add(temp);
                                okunacakSatir++;
                            } else if (i == 2) {
                                hydraulicData.parcaListesiKaplin1PN42.add(temp);
                                okunacakSatir++;
                            } else if (i == 3) {
                                hydraulicData.parcaListesiKaplin2PN28.add(temp);
                                okunacakSatir++;
                            } else if (i == 4) {
                                hydraulicData.parcaListesiKaplin2PN38.add(temp);
                                okunacakSatir++;
                            } else if (i == 5) {
                                hydraulicData.parcaListesiKaplin2PN42.add(temp);
                                okunacakSatir++;
                            }
                        }
                    } else {
                        okunacakSatir++;
                    }
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaListesiValfBlok(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Valf Blokları";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            Row variableValuesRow;

            int okunacakSatir = 1;

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 11; j++) {
                    String temp = "";
                    String temp1 = "", temp2 = "", temp3 = "";
                    variableValuesRow = sheet.getRow(okunacakSatir);

                    if (variableValuesRow != null) {
                        if (variableValuesRow.getCell(1) != null) {
                            if (isNumeric(variableValuesRow.getCell(1))) {
                                double temp1Value = variableValuesRow.getCell(1).getNumericCellValue();
                                temp1 = String.valueOf((int) temp1Value);
                            } else {
                                temp1 = String.valueOf(variableValuesRow.getCell(1));
                            }
                        }
                        if (variableValuesRow.getCell(2) != null) {
                            temp2 = String.valueOf(variableValuesRow.getCell(2));
                        }
                        if (variableValuesRow.getCell(3) != null) {
                            double temp3Value = variableValuesRow.getCell(3).getNumericCellValue();
                            temp3 = String.valueOf((int) temp3Value);
                        }

                        if (!temp1.isEmpty() && !temp2.isEmpty() && !temp3.isEmpty()) {
                            temp += temp1 + ";" + temp2 + ";" + temp3;

                            if (i == 0) {
                                hydraulicData.parcaListesiValfBloklariTekHiz.add(temp);
                                okunacakSatir++;
                            } else if (i == 1) {
                                hydraulicData.parcaListesiValfBloklariCiftHiz.add(temp);
                                okunacakSatir++;
                            } else if (i == 2) {
                                hydraulicData.parcaListesiValfBloklariKilitliBlok.add(temp);
                                okunacakSatir++;
                            } else if (i == 3) {
                                hydraulicData.parcaListesiValfBloklariKompanzasyon.add(temp);
                                okunacakSatir++;
                            }
                        }
                    } else {
                        okunacakSatir++;
                    }
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaListesiBasincSalteri(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Basınç Şalteri";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            Row variableValuesRow;

            int okunacakSatir = 1;

            for (int i = 0; i < 1; i++) {
                for (int j = 0; j < 4; j++) {
                    String temp = "";
                    String temp1 = "", temp2 = "", temp3 = "";
                    variableValuesRow = sheet.getRow(okunacakSatir);

                    if (variableValuesRow != null) {
                        if (variableValuesRow.getCell(1) != null) {
                            if (isNumeric(variableValuesRow.getCell(1))) {
                                double temp1Value = variableValuesRow.getCell(1).getNumericCellValue();
                                temp1 = String.valueOf((int) temp1Value);
                            } else {
                                temp1 = String.valueOf(variableValuesRow.getCell(1));
                            }
                        }
                        if (variableValuesRow.getCell(2) != null) {
                            temp2 = String.valueOf(variableValuesRow.getCell(2));
                        }
                        if (variableValuesRow.getCell(3) != null) {
                            double temp3Value = variableValuesRow.getCell(3).getNumericCellValue();
                            temp3 = String.valueOf((int) temp3Value);
                        }

                        if (!temp1.isEmpty() && !temp2.isEmpty() && !temp3.isEmpty()) {
                            temp += temp1 + ";" + temp2 + ";" + temp3;

                            if (i == 0) {
                                hydraulicData.parcaListesiBasincSalteri.add(temp);
                                okunacakSatir++;
                            }
                        }
                    } else {
                        okunacakSatir++;
                    }
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaListesiStandart(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Standart";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            Row variableValuesRow;

            int okunacakSatir = 1;

            for (int i = 0; i < 1; i++) {
                for (int j = 0; j < 4; j++) {
                    String temp = "";
                    String temp1 = "", temp2 = "", temp3 = "";
                    variableValuesRow = sheet.getRow(okunacakSatir);

                    if (variableValuesRow != null) {
                        if (variableValuesRow.getCell(1) != null) {
                            if (isNumeric(variableValuesRow.getCell(1))) {
                                double temp1Value = variableValuesRow.getCell(1).getNumericCellValue();
                                temp1 = String.valueOf((int) temp1Value);
                            } else {
                                temp1 = String.valueOf(variableValuesRow.getCell(1));
                            }
                        }
                        if (variableValuesRow.getCell(2) != null) {
                            temp2 = String.valueOf(variableValuesRow.getCell(2));
                        }
                        if (variableValuesRow.getCell(3) != null) {
                            double temp3Value = variableValuesRow.getCell(3).getNumericCellValue();
                            temp3 = String.valueOf((int) temp3Value);
                        }

                        if (!temp1.isEmpty() && !temp2.isEmpty() && !temp3.isEmpty()) {
                            temp += temp1 + ";" + temp2 + ";" + temp3;

                            if (i == 0) {
                                hydraulicData.parcaListesiStandart.add(temp);
                                okunacakSatir++;
                            }
                        }
                    } else {
                        okunacakSatir++;
                    }
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaListesiSogutucu(String filePath, HydraulicData hydraulicData) {
        String sheetName = "Parça-Soğutucu";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                Cell cell2 = row.getCell(1);
                Cell cell3 = row.getCell(2);
                String data = cell.getStringCellValue() + ";" + cell2.getStringCellValue() + ";" + float2String(String.valueOf(cell3.getNumericCellValue()));
                hydraulicData.parcaListesiSogutucu.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
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

    private static boolean isNumeric(Cell cell) {
        return cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA;
    }

    public static String float2String(String inputStr) {
        float floatAdet = Float.parseFloat(inputStr);
        int tamSayi = (int) floatAdet;

        return String.valueOf(tamSayi);
    }
}
