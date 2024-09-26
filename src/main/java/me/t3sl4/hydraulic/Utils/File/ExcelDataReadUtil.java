package me.t3sl4.hydraulic.Utils.File;

import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Utils.Model.Excel.DataManipulator;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExcelDataReadUtil {

    private static final Logger logger = Logger.getLogger(ExcelDataReadUtil.class.getName());

    public static void excelDataRead() {
        readExcel4Kampana(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4Motor(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4PompaKlasik(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4PompaTumu(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4KilitMotor(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4KilitPompa(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ValfTipi1(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ValfTipi2(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaListesiKampana1k(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaListesiKampana2k(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaListesiPompa(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaListesiMotor(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaListesiKaplin(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaListesiValfBlok(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaListesiBasincSalteri(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaListesiStandart(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4ParcaListesiSogutucu(Launcher.excelDBPath, Launcher.getDataManipulator());
        initMotorYukseklik();

        /*
        Power Pack
         */
        readExcel4PompaHidros(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4HidrosMotorDegerleri380(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4HidrosMotorDegerleri220(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4HidrosMotorDegerleri1224(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4HidrosPompaKapasite(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4HidrosTankDikey(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4HidrosTankYatay(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4HidrosPlatform(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4HidrosValf(Launcher.excelDBPath, Launcher.getDataManipulator());

        readExcel4IthalMotorDegerleri380(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4IthalMotorDegerleri220(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4IthalMotorDegerleri1224(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4IthalPompaKapasite(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4IthalTankDikey(Launcher.excelDBPath, Launcher.getDataManipulator());
        readExcel4IthalTankYatay(Launcher.excelDBPath, Launcher.getDataManipulator());

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

    public static void readExcel4Kampana(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Kampana";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                data = data.trim();
                data = data.replaceAll("[^0-9]", "");
                dataManipulator.kampanaDegerleri.add(Integer.parseInt(data));
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4Motor(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Motor";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.motorDegerleri.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4PompaHidros(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Pompa-1";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.pompaDegerleriHidros.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4PompaKlasik(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Pompa-2";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.pompaDegerleriKlasik.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4PompaTumu(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Pompa-3";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.pompaDegerleriTumu.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4KilitMotor(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Kilit Motor";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.kilitMotorDegerleri.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4KilitPompa(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Kilit Pompa";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.kilitPompaDegerleri.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ValfTipi1(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Valf Tipi-1";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.valfTipiDegerleri1.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ValfTipi2(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Valf Tipi-2";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.valfTipiDegerleri2.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaListesiKampana1k(String filePath, DataManipulator dataManipulator) {
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
                                dataManipulator.parcaListesiKampana2501k.add(temp);
                                okunacakSatir++;
                            } else if (i == 1) {
                                dataManipulator.parcaListesiKampana3001k.add(temp);
                                okunacakSatir++;
                            } else if (i == 2) {
                                dataManipulator.parcaListesiKampana3501k.add(temp);
                                okunacakSatir++;
                            } else if (i == 3) {
                                dataManipulator.parcaListesiKampana4001k.add(temp);
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

    public static void readExcel4ParcaListesiKampana2k(String filePath, DataManipulator dataManipulator) {
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
                                dataManipulator.parcaListesiKampana2502k.add(temp);
                                okunacakSatir++;
                            } else if (i == 1) {
                                dataManipulator.parcaListesiKampana3002k.add(temp);
                                okunacakSatir++;
                            } else if (i == 2) {
                                dataManipulator.parcaListesiKampana3502k.add(temp);
                                okunacakSatir++;
                            } else if (i == 3) {
                                dataManipulator.parcaListesiKampana4002k.add(temp);
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

    public static void readExcel4ParcaListesiPompa(String filePath, DataManipulator dataManipulator) {
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
                                    dataManipulator.parcaListesiPompa95.add(temp);
                                    okunacakSatir++;
                                } else if (i == 1) {
                                    dataManipulator.parcaListesiPompa119.add(temp);
                                    okunacakSatir++;
                                } else if (i == 2) {
                                    dataManipulator.parcaListesiPompa14.add(temp);
                                    okunacakSatir++;
                                } else if (i == 3) {
                                    dataManipulator.parcaListesiPompa146.add(temp);
                                    okunacakSatir++;
                                } else if (i == 4) {
                                    dataManipulator.parcaListesiPompa168.add(temp);
                                    okunacakSatir++;
                                } else if (i == 5) {
                                    dataManipulator.parcaListesiPompa192.add(temp);
                                    okunacakSatir++;
                                } else if (i == 6) {
                                    dataManipulator.parcaListesiPompa229.add(temp);
                                    okunacakSatir++;
                                } else if (i == 7) {
                                    dataManipulator.parcaListesiPompa281.add(temp);
                                    okunacakSatir++;
                                } else if (i == 8) {
                                    dataManipulator.parcaListesiPompa288.add(temp);
                                    okunacakSatir++;
                                } else if (i == 9) {
                                    dataManipulator.parcaListesiPompa333.add(temp);
                                    okunacakSatir++;
                                } else if (i == 10) {
                                    dataManipulator.parcaListesiPompa379.add(temp);
                                    okunacakSatir++;
                                } else if (i == 11) {
                                    dataManipulator.parcaListesiPompa426.add(temp);
                                    okunacakSatir++;
                                } else if (i == 12) {
                                    dataManipulator.parcaListesiPompa455.add(temp);
                                    okunacakSatir++;
                                } else if (i == 13) {
                                    dataManipulator.parcaListesiPompa494.add(temp);
                                    okunacakSatir++;
                                } else {
                                    dataManipulator.parcaListesiPompa561.add(temp);
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

    public static void readExcel4ParcaListesiMotor(String filePath, DataManipulator dataManipulator) {
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
                                dataManipulator.parcaListesiMotor202.add(temp);
                                okunacakSatir++;
                                break;
                            } else if (i == 1) {
                                dataManipulator.parcaListesiMotor3.add(temp);
                                okunacakSatir++;
                                break;
                            } else if (i == 2) {
                                dataManipulator.parcaListesiMotor4.add(temp);
                                okunacakSatir++;
                                break;
                            } else if (i == 3) {
                                dataManipulator.parcaListesiMotor55.add(temp);
                                okunacakSatir++;
                                break;
                            } else if (i == 4) {
                                dataManipulator.parcaListesiMotor55Kompakt.add(temp);
                                okunacakSatir++;
                            } else if (i == 5) {
                                dataManipulator.parcaListesiMotor75Kompakt.add(temp);
                                okunacakSatir++;
                                break;
                            } else if (i == 6) {
                                dataManipulator.parcaListesiMotor11.add(temp);
                                okunacakSatir++;
                                break;
                            } else if (i == 7) {
                                dataManipulator.parcaListesiMotor11Kompakt.add(temp);
                                okunacakSatir++;
                                break;
                            } else if (i == 8) {
                                dataManipulator.parcaListesiMotor15.add(temp);
                                okunacakSatir++;
                                break;
                            } else if (i == 9) {
                                dataManipulator.parcaListesiMotor185.add(temp);
                                okunacakSatir++;
                                break;
                            } else if (i == 10) {
                                dataManipulator.parcaListesiMotor22.add(temp);
                                okunacakSatir++;
                                break;
                            } else if (i == 11) {
                                dataManipulator.parcaListesiMotor37.add(temp);
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

    public static void readExcel4ParcaListesiKaplin(String filePath, DataManipulator dataManipulator) {
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
                                dataManipulator.parcaListesiKaplin1PN28.add(temp);
                                okunacakSatir++;
                            } else if (i == 1) {
                                dataManipulator.parcaListesiKaplin1PN38.add(temp);
                                okunacakSatir++;
                            } else if (i == 2) {
                                dataManipulator.parcaListesiKaplin1PN42.add(temp);
                                okunacakSatir++;
                            } else if (i == 3) {
                                dataManipulator.parcaListesiKaplin2PN28.add(temp);
                                okunacakSatir++;
                            } else if (i == 4) {
                                dataManipulator.parcaListesiKaplin2PN38.add(temp);
                                okunacakSatir++;
                            } else if (i == 5) {
                                dataManipulator.parcaListesiKaplin2PN42.add(temp);
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

    public static void readExcel4ParcaListesiValfBlok(String filePath, DataManipulator dataManipulator) {
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
                                dataManipulator.parcaListesiValfBloklariTekHiz.add(temp);
                                okunacakSatir++;
                            } else if (i == 1) {
                                dataManipulator.parcaListesiValfBloklariCiftHiz.add(temp);
                                okunacakSatir++;
                            } else if (i == 2) {
                                dataManipulator.parcaListesiValfBloklariKilitliBlok.add(temp);
                                okunacakSatir++;
                            } else if (i == 3) {
                                dataManipulator.parcaListesiValfBloklariKompanzasyon.add(temp);
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

    public static void readExcel4ParcaListesiBasincSalteri(String filePath, DataManipulator dataManipulator) {
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
                                dataManipulator.parcaListesiBasincSalteri.add(temp);
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

    public static void readExcel4ParcaListesiStandart(String filePath, DataManipulator dataManipulator) {
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
                                dataManipulator.parcaListesiStandart.add(temp);
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

    public static void readExcel4ParcaListesiSogutucu(String filePath, DataManipulator dataManipulator) {
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
                dataManipulator.parcaListesiSogutucu.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4HidrosMotorDegerleri380(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Hidros-380";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.motorDegerleriHidros380.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4IthalMotorDegerleri380(String filePath, DataManipulator dataManipulator) {
        String sheetName = "İthal-380";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.motorDegerleriIthal380.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4HidrosMotorDegerleri220(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Hidros-220";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.motorDegerleriIthal220.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4IthalMotorDegerleri220(String filePath, DataManipulator dataManipulator) {
        String sheetName = "İthal-220";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.motorDegerleriHidros220.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4HidrosMotorDegerleri1224(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Hidros-DC";

        try (InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cellA = row.getCell(0); // A sütunundaki hücre
                Cell cellB = row.getCell(1); // B sütunundaki hücre

                if (i >= 2 && i <= 4 && cellA != null && cellA.getCellType() == CellType.STRING) {
                    String dataA = cellA.getStringCellValue();
                    dataManipulator.motorDegerleriHidros12.add(dataA);
                }

                if (cellB != null && cellB.getCellType() == CellType.STRING) {
                    String dataB = cellB.getStringCellValue();
                    dataManipulator.motorDegerleriHidros24.add(dataB);
                }
            }

            workbook.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4IthalMotorDegerleri1224(String filePath, DataManipulator dataManipulator) {
        String sheetName = "İthal-DC";

        try (InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cellA = row.getCell(0); // A sütunundaki hücre
                Cell cellB = row.getCell(1); // B sütunundaki hücre

                if (i >= 2 && i <= 4 && cellA != null && cellA.getCellType() == CellType.STRING) {
                    String dataA = cellA.getStringCellValue();
                    dataManipulator.motorDegerleriIthal12.add(dataA);
                }

                if (cellB != null && cellB.getCellType() == CellType.STRING) {
                    String dataB = cellB.getStringCellValue();
                    dataManipulator.motorDegerleriIthal24.add(dataB);
                }
            }

            workbook.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4HidrosPompaKapasite(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Hidros-Pompa";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.pompaKapasiteDegerleriHidros.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4IthalPompaKapasite(String filePath, DataManipulator dataManipulator) {
        String sheetName = "İthal-Pompa";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.pompaKapasiteDegerleriIthal.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4HidrosTankDikey(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Hidros-Tank-Dikey";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.tankKapasitesiDegerleriHidrosDikey.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4HidrosTankYatay(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Hidros-Tank-Yatay";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.tankKapasitesiDegerleriHidrosYatay.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4IthalTankDikey(String filePath, DataManipulator dataManipulator) {
        String sheetName = "İthal-Tank-Dikey";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.tankKapasitesiDegerleriIthalDikey.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4IthalTankYatay(String filePath, DataManipulator dataManipulator) {
        String sheetName = "İthal-Tank-Yatay";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.tankKapasitesiDegerleriIthalYatay.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4HidrosPlatform(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Hidros-Platform";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.platformDegerleriHidros.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4HidrosValf(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Hidros-Valf-Deger";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.valfDegerleriHidros.add(data);
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosMotor380(String filePath, DataManipulator dataManipulator) {
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
                    dataManipulator.hidros380Parca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosMotor220(String filePath, DataManipulator dataManipulator) {
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
                    dataManipulator.hidros220Parca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosPompa(String filePath, DataManipulator dataManipulator) {
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
                    dataManipulator.hidrosPompaParca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosPompaCivata(String filePath, DataManipulator dataManipulator) {
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
                    dataManipulator.hidrosPompaCivataParca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosTankDikey(String filePath, DataManipulator dataManipulator) {
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
                    dataManipulator.hidrosDikeyTankParca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosTankYatay(String filePath, DataManipulator dataManipulator) {
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
                    dataManipulator.hidrosYatayTankParca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosValfDikeyCift(String filePath, DataManipulator dataManipulator) {
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
                    dataManipulator.hidrosDikeyCiftHizParca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosValfDikeyTek(String filePath, DataManipulator dataManipulator) {
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
                    dataManipulator.hidrosDikeyTekHizParca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosValfYatayTek(String filePath, DataManipulator dataManipulator) {
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
                    dataManipulator.hidrosYatayTekHizParca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosValfYatayCift(String filePath, DataManipulator dataManipulator) {
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
                    dataManipulator.hidrosYatayCiftHizParca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosValfDikeyCiftESP(String filePath, DataManipulator dataManipulator) {
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
                    dataManipulator.hidrosDikeyCiftHizParcaESP.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosValfDikeyTekESP(String filePath, DataManipulator dataManipulator) {
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
                    dataManipulator.hidrosDikeyTekHizParcaESP.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosValfYatayTekESP(String filePath, DataManipulator dataManipulator) {
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
                    dataManipulator.hidrosYatayTekHizParcaESP.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosValfYatayCiftESP(String filePath, DataManipulator dataManipulator) {
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
                    dataManipulator.hidrosYatayCiftHizParcaESP.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosPlatformDevirmeli(String filePath, DataManipulator dataManipulator) {
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
                    dataManipulator.hidrosDevirmeliParca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosGenel(String filePath, DataManipulator dataManipulator) {
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
                    dataManipulator.hidrosGenelParca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosTam(String filePath, DataManipulator dataManipulator) {
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
                    dataManipulator.hidrosTamParca.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosTamYatay(String filePath, DataManipulator dataManipulator) {
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
                    dataManipulator.hidrosTamParcaYatay.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosTamDikey(String filePath, DataManipulator dataManipulator) {
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
                    dataManipulator.hidrosTamParcaDikey.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosTamESPYok(String filePath, DataManipulator dataManipulator) {
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
                    dataManipulator.hidrosTamParcaESPHaric.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4ParcaHidrosOzelTekValf(String filePath, DataManipulator dataManipulator) {
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
                    dataManipulator.hidrosTamParcaOzelTekValf.put(key, innerMap);
                }
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void initMotorYukseklik() {
        Launcher.getDataManipulator().motorYukseklikVerileri.add("345 mm"); //2.2 kW
        Launcher.getDataManipulator().motorYukseklikVerileri.add("345 mm"); //3 kW
        Launcher.getDataManipulator().motorYukseklikVerileri.add("345 mm");
        Launcher.getDataManipulator().motorYukseklikVerileri.add("375 mm");
        Launcher.getDataManipulator().motorYukseklikVerileri.add("365 mm");
        Launcher.getDataManipulator().motorYukseklikVerileri.add("410 mm");
        Launcher.getDataManipulator().motorYukseklikVerileri.add("500 mm");
        Launcher.getDataManipulator().motorYukseklikVerileri.add("470 mm");
        Launcher.getDataManipulator().motorYukseklikVerileri.add("540 mm");
        Launcher.getDataManipulator().motorYukseklikVerileri.add("565 mm");
        Launcher.getDataManipulator().motorYukseklikVerileri.add("565 mm");
        Launcher.getDataManipulator().motorYukseklikVerileri.add("600 mm");
    }

    private static String getStringValueFromCell(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC) {
            double numericValue = cell.getNumericCellValue();
            return String.valueOf((int) numericValue);
        } else {
            return cell.getStringCellValue();
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
