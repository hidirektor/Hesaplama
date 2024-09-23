package me.t3sl4.hydraulic.Utils.File;

import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Utils.Data.Excel.DataManipulator;
import me.t3sl4.hydraulic.Utils.Data.Tank.Tank;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExcelUtil {

    private static final Logger logger = Logger.getLogger(ExcelUtil.class.getName());

    public static DataManipulator dataManipulator;

    static {
        dataManipulator = new DataManipulator();
    }

    public static void readExcelData(String filePath, String sheetName, DataManipulator dataManipulator, BiConsumer<Row, DataManipulator> rowProcessor) {
        try (InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            if (sheet == null) {
                logger.log(Level.SEVERE, "Sheet " + sheetName + " not found in " + filePath);
                return;
            }

            int rowCount = sheet.getPhysicalNumberOfRows();
            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    rowProcessor.accept(row, dataManipulator);
                } else {
                    //logger.log(Level.WARNING, "Null row encountered at index " + i + " in sheet " + sheetName);
                }
            }

            workbook.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void excelDataRead() {
        readExcel4DefinedTanks(Launcher.excelDBPath, dataManipulator);
        readExcel4Bosluk(Launcher.excelDBPath, dataManipulator);
        readExcel4Kampana(Launcher.excelDBPath, dataManipulator);
        readExcel4Motor(Launcher.excelDBPath, dataManipulator);
        readExcel4UniteTipi(Launcher.excelDBPath, dataManipulator);
        readExcel4PompaHidros(Launcher.excelDBPath, dataManipulator);
        readExcel4PompaKlasik(Launcher.excelDBPath, dataManipulator);
        readExcel4PompaTumu(Launcher.excelDBPath, dataManipulator);
        readExcel4KilitMotor(Launcher.excelDBPath, dataManipulator);
        readExcel4KilitPompa(Launcher.excelDBPath, dataManipulator);
        readExcel4ValfTipi1(Launcher.excelDBPath, dataManipulator);
        readExcel4ValfTipi2(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaListesiKampana1k(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaListesiKampana2k(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaListesiPompa(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaListesiMotor(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaListesiKaplin(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaListesiValfBlok(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaListesiBasincSalteri(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaListesiStandart(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaListesiSogutucu(Launcher.excelDBPath, dataManipulator);
        readExcel4HidrosMotorDegerleri380(Launcher.excelDBPath, dataManipulator);
        readExcel4IthalMotorDegerleri380(Launcher.excelDBPath, dataManipulator);
        readExcel4HidrosMotorDegerleri220(Launcher.excelDBPath, dataManipulator);
        readExcel4IthalMotorDegerleri220(Launcher.excelDBPath, dataManipulator);
        readExcel4HidrosPompaKapasite(Launcher.excelDBPath, dataManipulator);
        readExcel4IthalPompaKapasite(Launcher.excelDBPath, dataManipulator);
        readExcel4HidrosTankDikey(Launcher.excelDBPath, dataManipulator);
        readExcel4HidrosTankYatay(Launcher.excelDBPath, dataManipulator);
        readExcel4HidrosPlatform(Launcher.excelDBPath, dataManipulator);
        readExcel4HidrosValf(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaHidrosMotor380(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaHidrosMotor220(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaHidrosPompa(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaHidrosPompaCivata(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaHidrosTankDikey(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaHidrosTankYatay(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaHidrosValfDikeyCift(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaHidrosValfDikeyTek(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaHidrosValfYatayTek(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaHidrosValfYatayCift(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaHidrosValfDikeyCiftESP(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaHidrosValfDikeyTekESP(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaHidrosValfYatayTekESP(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaHidrosValfYatayCiftESP(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaHidrosPlatformDevirmeli(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaHidrosGenel(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaHidrosTam(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaHidrosTamYatay(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaHidrosTamDikey(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaHidrosTamESPYok(Launcher.excelDBPath, dataManipulator);
        readExcel4ParcaHidrosOzelTekValf(Launcher.excelDBPath, dataManipulator);
        initMotorYukseklik();
    }

    public static void readExcel4DefinedTanks(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Kabinler";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);

                String tankName = row.getCell(0).getStringCellValue();
                String kabinName = row.getCell(1).getStringCellValue();
                int kabinHacim = (int) row.getCell(8).getNumericCellValue();
                int gecisX = (int) row.getCell(2).getNumericCellValue();
                int gecisY = (int) row.getCell(3).getNumericCellValue();
                int gecisH = (int) row.getCell(4).getNumericCellValue();
                int kabinX = (int) row.getCell(5).getNumericCellValue();
                int kabinY = (int) row.getCell(6).getNumericCellValue();
                int kabinH = (int) row.getCell(7).getNumericCellValue();
                String malzemeKodu = String.valueOf(row.getCell(9).getStringCellValue());
                String malzemeAdi = String.valueOf(row.getCell(10).getStringCellValue());

                Tank tank = new Tank(tankName, kabinName, kabinHacim, gecisX, gecisY, gecisH, kabinX, kabinY, kabinH, malzemeKodu, malzemeAdi);
                dataManipulator.inputTanks.add(tank);
            }

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void readExcel4Bosluk(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Boşluk Değerleri";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            //Row variableNamesRow = sheet.getRow(0);
            Row variableValuesRow = sheet.getRow(1);

            dataManipulator.kampanaBoslukX = (int) variableValuesRow.getCell(0).getNumericCellValue();
            dataManipulator.kampanaBoslukY = (int) variableValuesRow.getCell(1).getNumericCellValue();
            dataManipulator.valfBoslukX = (int) variableValuesRow.getCell(2).getNumericCellValue();
            dataManipulator.valfBoslukYArka = (int) variableValuesRow.getCell(3).getNumericCellValue();
            dataManipulator.valfBoslukYOn = (int) variableValuesRow.getCell(4).getNumericCellValue();
            dataManipulator.kilitliBlokAraBoslukX = (int) variableValuesRow.getCell(5).getNumericCellValue();
            dataManipulator.tekHizAraBoslukX = (int) variableValuesRow.getCell(6).getNumericCellValue();
            dataManipulator.ciftHizAraBoslukX = (int) variableValuesRow.getCell(7).getNumericCellValue();
            dataManipulator.kompanzasyonTekHizAraBoslukX = (int) variableValuesRow.getCell(8).getNumericCellValue();
            dataManipulator.sogutmaAraBoslukX = (int) variableValuesRow.getCell(9).getNumericCellValue();
            dataManipulator.sogutmaAraBoslukYkOn = (int) variableValuesRow.getCell(10).getNumericCellValue();
            dataManipulator.sogutmaAraBoslukYkArka = (int) variableValuesRow.getCell(11).getNumericCellValue();
            dataManipulator.kilitMotorKampanaBosluk = (int) variableValuesRow.getCell(12).getNumericCellValue();
            dataManipulator.kilitMotorMotorBoslukX = (int) variableValuesRow.getCell(13).getNumericCellValue();
            dataManipulator.kilitMotorBoslukYOn = (int) variableValuesRow.getCell(14).getNumericCellValue();
            dataManipulator.kilitMotorBoslukYArka = (int) variableValuesRow.getCell(15).getNumericCellValue();
            dataManipulator.kayipLitre = (int) variableValuesRow.getCell(16).getNumericCellValue();
            dataManipulator.kilitPlatformMotorBosluk = (int) variableValuesRow.getCell(17).getNumericCellValue();
            dataManipulator.valfXBoslukSogutma = (int) variableValuesRow.getCell(18).getNumericCellValue();

            workbook.close();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
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

    public static void readExcel4UniteTipi(String filePath, DataManipulator dataManipulator) {
        String sheetName = "Ünite Tipi";

        try(InputStream file = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getPhysicalNumberOfRows();

            for(int i=1; i<rowCount; i++) {
                Row row = sheet.getRow(i);
                Cell cell = row.getCell(0);
                String data = cell.getStringCellValue();
                dataManipulator.uniteTipiDegerleri.add(data);
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
        dataManipulator.motorYukseklikVerileri.add("345 mm"); //2.2 kW
        dataManipulator.motorYukseklikVerileri.add("345 mm"); //3 kW
        dataManipulator.motorYukseklikVerileri.add("345 mm");
        dataManipulator.motorYukseklikVerileri.add("375 mm");
        dataManipulator.motorYukseklikVerileri.add("365 mm");
        dataManipulator.motorYukseklikVerileri.add("410 mm");
        dataManipulator.motorYukseklikVerileri.add("500 mm");
        dataManipulator.motorYukseklikVerileri.add("470 mm");
        dataManipulator.motorYukseklikVerileri.add("540 mm");
        dataManipulator.motorYukseklikVerileri.add("565 mm");
        dataManipulator.motorYukseklikVerileri.add("565 mm");
        dataManipulator.motorYukseklikVerileri.add("600 mm");
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
