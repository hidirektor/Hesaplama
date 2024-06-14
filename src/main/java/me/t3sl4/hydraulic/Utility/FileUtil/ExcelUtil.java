package me.t3sl4.hydraulic.Utility.FileUtil;

import me.t3sl4.hydraulic.Launcher;
import me.t3sl4.hydraulic.Utility.DataUtil.Excel.DataManipulator;
import me.t3sl4.hydraulic.Utility.Util;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExcelUtil {

    private static final Logger logger = Logger.getLogger(ExcelUtil.class.getName());

    public static DataManipulator dataManipulator;

    static {
        dataManipulator = new DataManipulator();
    }

    private static String getStringValueFromCell(Cell cell) {
        if (cell == null) {
            return "";
        }
        if (cell.getCellType() == CellType.NUMERIC) {
            double numericValue = cell.getNumericCellValue();
            return String.valueOf((int) numericValue);
        } else {
            return cell.getStringCellValue();
        }
    }

    private static boolean isNumeric(Cell cell) {
        return cell != null && (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA);
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
        readExcel4HidrosMotorDegerleri220(Launcher.excelDBPath, dataManipulator);
        readExcel4HidrosPompaKapasite(Launcher.excelDBPath, dataManipulator);
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

    public static void readExcel4Bosluk(String filePath, DataManipulator dataManipulator) {
        readExcelData(filePath, "Boşluk Değerleri", dataManipulator, (row, dm) -> {
            dm.kampanaBoslukX = (int) row.getCell(0).getNumericCellValue();
            dm.kampanaBoslukY = (int) row.getCell(1).getNumericCellValue();
            dm.valfBoslukX = (int) row.getCell(2).getNumericCellValue();
            dm.valfBoslukYArka = (int) row.getCell(3).getNumericCellValue();
            dm.valfBoslukYOn = (int) row.getCell(4).getNumericCellValue();
            dm.kilitliBlokAraBoslukX = (int) row.getCell(5).getNumericCellValue();
            dm.tekHizAraBoslukX = (int) row.getCell(6).getNumericCellValue();
            dm.ciftHizAraBoslukX = (int) row.getCell(7).getNumericCellValue();
            dm.kompanzasyonTekHizAraBoslukX = (int) row.getCell(8).getNumericCellValue();
            dm.sogutmaAraBoslukX = (int) row.getCell(9).getNumericCellValue();
            dm.sogutmaAraBoslukYkOn = (int) row.getCell(10).getNumericCellValue();
            dm.sogutmaAraBoslukYkArka = (int) row.getCell(11).getNumericCellValue();
            dm.kilitMotorKampanaBosluk = (int) row.getCell(12).getNumericCellValue();
            dm.kilitMotorMotorBoslukX = (int) row.getCell(13).getNumericCellValue();
            dm.kilitMotorBoslukYOn = (int) row.getCell(14).getNumericCellValue();
            dm.kilitMotorBoslukYArka = (int) row.getCell(15).getNumericCellValue();
            dm.kayipLitre = (int) row.getCell(16).getNumericCellValue();
            dm.kilitPlatformMotorBosluk = (int) row.getCell(17).getNumericCellValue();
            dm.valfXBoslukSogutma = (int) row.getCell(18).getNumericCellValue();
        });
    }

    public static void readExcel4Kampana(String filePath, DataManipulator dataManipulator) {
        readExcelData(filePath, "Kampana", dataManipulator, (row, dm) -> {
            String data = row.getCell(0).getStringCellValue().trim().replaceAll("[^0-9]", "");
            dm.kampanaDegerleri.add(Integer.parseInt(data));
        });
    }

    public static void readExcel4Motor(String filePath, DataManipulator dataManipulator) {
        readExcelData(filePath, "Motor", dataManipulator, (row, dm) -> {
            dm.motorDegerleri.add(row.getCell(0).getStringCellValue());
        });
    }

    public static void readExcel4UniteTipi(String filePath, DataManipulator dataManipulator) {
        readExcelData(filePath, "Ünite Tipi", dataManipulator, (row, dm) -> {
            dm.uniteTipiDegerleri.add(row.getCell(0).getStringCellValue());
        });
    }

    public static void readExcel4PompaHidros(String filePath, DataManipulator dataManipulator) {
        readExcelData(filePath, "Pompa-1", dataManipulator, (row, dm) -> {
            dm.pompaDegerleriHidros.add(row.getCell(0).getStringCellValue());
        });
    }

    public static void readExcel4PompaKlasik(String filePath, DataManipulator dataManipulator) {
        readExcelData(filePath, "Pompa-2", dataManipulator, (row, dm) -> {
            dm.pompaDegerleriKlasik.add(row.getCell(0).getStringCellValue());
        });
    }

    public static void readExcel4PompaTumu(String filePath, DataManipulator dataManipulator) {
        readExcelData(filePath, "Pompa-3", dataManipulator, (row, dm) -> {
            dm.pompaDegerleriTumu.add(row.getCell(0).getStringCellValue());
        });
    }

    public static void readExcel4KilitMotor(String filePath, DataManipulator dataManipulator) {
        readExcelData(filePath, "Kilit Motor", dataManipulator, (row, dm) -> {
            dm.kilitMotorDegerleri.add(row.getCell(0).getStringCellValue());
        });
    }

    public static void readExcel4KilitPompa(String filePath, DataManipulator dataManipulator) {
        readExcelData(filePath, "Kilit Pompa", dataManipulator, (row, dm) -> {
            dm.kilitPompaDegerleri.add(row.getCell(0).getStringCellValue());
        });
    }

    public static void readExcel4ValfTipi1(String filePath, DataManipulator dataManipulator) {
        readExcelData(filePath, "Valf Tipi-1", dataManipulator, (row, dm) -> {
            dm.valfTipiDegerleri1.add(row.getCell(0).getStringCellValue());
        });
    }

    public static void readExcel4ValfTipi2(String filePath, DataManipulator dataManipulator) {
        readExcelData(filePath, "Valf Tipi-2", dataManipulator, (row, dm) -> {
            dm.valfTipiDegerleri2.add(row.getCell(0).getStringCellValue());
        });
    }

    public static void readExcel4ParcaListesi(String filePath, String sheetName, List<String> list) {
        readExcelData(filePath, sheetName, new DataManipulator(), (row, dm) -> {
            String temp1 = row.getCell(1) != null ? row.getCell(1).toString() : "";
            String temp2 = row.getCell(2) != null ? row.getCell(2).toString() : "";
            String temp3 = row.getCell(3) != null ? String.valueOf(row.getCell(3).getNumericCellValue()) : "";
            if (!temp1.isEmpty() && !temp2.isEmpty() && !temp3.isEmpty()) {
                list.add(temp1 + ";" + temp2 + ";" + temp3);
            }
        });
    }

    public static void readExcel4ParcaListesiKampana1k(String filePath, DataManipulator dataManipulator) {
        readExcel4ParcaListesi(filePath, "Parça-Kampana-1K", dataManipulator.parcaListesiKampana2501k);
        readExcel4ParcaListesi(filePath, "Parça-Kampana-1K", dataManipulator.parcaListesiKampana3001k);
        readExcel4ParcaListesi(filePath, "Parça-Kampana-1K", dataManipulator.parcaListesiKampana3501k);
        readExcel4ParcaListesi(filePath, "Parça-Kampana-1K", dataManipulator.parcaListesiKampana4001k);
    }

    public static void readExcel4ParcaListesiKampana2k(String filePath, DataManipulator dataManipulator) {
        readExcel4ParcaListesi(filePath, "Parça-Kampana-2K", dataManipulator.parcaListesiKampana2502k);
        readExcel4ParcaListesi(filePath, "Parça-Kampana-2K", dataManipulator.parcaListesiKampana3002k);
        readExcel4ParcaListesi(filePath, "Parça-Kampana-2K", dataManipulator.parcaListesiKampana3502k);
        readExcel4ParcaListesi(filePath, "Parça-Kampana-2K", dataManipulator.parcaListesiKampana4002k);
    }

    public static void readExcel4ParcaListesiPompa(String filePath, DataManipulator dataManipulator) {
        readExcel4ParcaListesi(filePath, "Parça-Pompa", dataManipulator.parcaListesiPompa95);
        readExcel4ParcaListesi(filePath, "Parça-Pompa", dataManipulator.parcaListesiPompa119);
        readExcel4ParcaListesi(filePath, "Parça-Pompa", dataManipulator.parcaListesiPompa14);
        readExcel4ParcaListesi(filePath, "Parça-Pompa", dataManipulator.parcaListesiPompa146);
        readExcel4ParcaListesi(filePath, "Parça-Pompa", dataManipulator.parcaListesiPompa168);
        readExcel4ParcaListesi(filePath, "Parça-Pompa", dataManipulator.parcaListesiPompa192);
        readExcel4ParcaListesi(filePath, "Parça-Pompa", dataManipulator.parcaListesiPompa229);
        readExcel4ParcaListesi(filePath, "Parça-Pompa", dataManipulator.parcaListesiPompa281);
        readExcel4ParcaListesi(filePath, "Parça-Pompa", dataManipulator.parcaListesiPompa288);
        readExcel4ParcaListesi(filePath, "Parça-Pompa", dataManipulator.parcaListesiPompa333);
        readExcel4ParcaListesi(filePath, "Parça-Pompa", dataManipulator.parcaListesiPompa379);
        readExcel4ParcaListesi(filePath, "Parça-Pompa", dataManipulator.parcaListesiPompa426);
        readExcel4ParcaListesi(filePath, "Parça-Pompa", dataManipulator.parcaListesiPompa455);
        readExcel4ParcaListesi(filePath, "Parça-Pompa", dataManipulator.parcaListesiPompa494);
        readExcel4ParcaListesi(filePath, "Parça-Pompa", dataManipulator.parcaListesiPompa561);
    }

    public static void readExcel4ParcaListesiMotor(String filePath, DataManipulator dataManipulator) {
        readExcel4ParcaListesi(filePath, "Parça-Motor", dataManipulator.parcaListesiMotor202);
        readExcel4ParcaListesi(filePath, "Parça-Motor", dataManipulator.parcaListesiMotor3);
        readExcel4ParcaListesi(filePath, "Parça-Motor", dataManipulator.parcaListesiMotor4);
        readExcel4ParcaListesi(filePath, "Parça-Motor", dataManipulator.parcaListesiMotor55);
        readExcel4ParcaListesi(filePath, "Parça-Motor", dataManipulator.parcaListesiMotor55Kompakt);
        readExcel4ParcaListesi(filePath, "Parça-Motor", dataManipulator.parcaListesiMotor75Kompakt);
        readExcel4ParcaListesi(filePath, "Parça-Motor", dataManipulator.parcaListesiMotor11);
        readExcel4ParcaListesi(filePath, "Parça-Motor", dataManipulator.parcaListesiMotor11Kompakt);
        readExcel4ParcaListesi(filePath, "Parça-Motor", dataManipulator.parcaListesiMotor15);
        readExcel4ParcaListesi(filePath, "Parça-Motor", dataManipulator.parcaListesiMotor185);
        readExcel4ParcaListesi(filePath, "Parça-Motor", dataManipulator.parcaListesiMotor22);
        readExcel4ParcaListesi(filePath, "Parça-Motor", dataManipulator.parcaListesiMotor37);
    }

    public static void readExcel4ParcaListesiKaplin(String filePath, DataManipulator dataManipulator) {
        readExcel4ParcaListesi(filePath, "Parça-Kaplin", dataManipulator.parcaListesiKaplin1PN28);
        readExcel4ParcaListesi(filePath, "Parça-Kaplin", dataManipulator.parcaListesiKaplin1PN38);
        readExcel4ParcaListesi(filePath, "Parça-Kaplin", dataManipulator.parcaListesiKaplin1PN42);
        readExcel4ParcaListesi(filePath, "Parça-Kaplin", dataManipulator.parcaListesiKaplin2PN28);
        readExcel4ParcaListesi(filePath, "Parça-Kaplin", dataManipulator.parcaListesiKaplin2PN38);
        readExcel4ParcaListesi(filePath, "Parça-Kaplin", dataManipulator.parcaListesiKaplin2PN42);
    }

    public static void readExcel4ParcaListesiValfBlok(String filePath, DataManipulator dataManipulator) {
        readExcel4ParcaListesi(filePath, "Parça-Valf Blokları", dataManipulator.parcaListesiValfBloklariTekHiz);
        readExcel4ParcaListesi(filePath, "Parça-Valf Blokları", dataManipulator.parcaListesiValfBloklariCiftHiz);
        readExcel4ParcaListesi(filePath, "Parça-Valf Blokları", dataManipulator.parcaListesiValfBloklariKilitliBlok);
        readExcel4ParcaListesi(filePath, "Parça-Valf Blokları", dataManipulator.parcaListesiValfBloklariKompanzasyon);
    }

    public static void readExcel4ParcaListesiBasincSalteri(String filePath, DataManipulator dataManipulator) {
        readExcel4ParcaListesi(filePath, "Parça-Basınç Şalteri", dataManipulator.parcaListesiBasincSalteri);
    }

    public static void readExcel4ParcaListesiStandart(String filePath, DataManipulator dataManipulator) {
        readExcel4ParcaListesi(filePath, "Parça-Standart", dataManipulator.parcaListesiStandart);
    }

    public static void readExcel4ParcaListesiSogutucu(String filePath, DataManipulator dataManipulator) {
        readExcelData(filePath, "Parça-Soğutucu", dataManipulator, (row, dm) -> {
            String data = row.getCell(0).getStringCellValue() + ";" + row.getCell(1).getStringCellValue() + ";" + Util.float2String(String.valueOf(row.getCell(2).getNumericCellValue()));
            dm.parcaListesiSogutucu.add(data);
        });
    }

    public static void readExcel4HidrosMotorDegerleri380(String filePath, DataManipulator dataManipulator) {
        readExcelData(filePath, "Hidros-380", dataManipulator, (row, dm) -> {
            dm.motorDegerleriHidros380.add(row.getCell(0).getStringCellValue());
        });
    }

    public static void readExcel4HidrosMotorDegerleri220(String filePath, DataManipulator dataManipulator) {
        readExcelData(filePath, "Hidros-220", dataManipulator, (row, dm) -> {
            dm.motorDegerleriHidros220.add(row.getCell(0).getStringCellValue());
        });
    }

    public static void readExcel4HidrosPompaKapasite(String filePath, DataManipulator dataManipulator) {
        readExcelData(filePath, "Hidros-Pompa", dataManipulator, (row, dm) -> {
            dm.pompaKapasiteDegerleriHidros.add(row.getCell(0).getStringCellValue());
        });
    }

    public static void readExcel4HidrosTankDikey(String filePath, DataManipulator dataManipulator) {
        readExcelData(filePath, "Hidros-Tank-Dikey", dataManipulator, (row, dm) -> {
            dm.tankKapasitesiDegerleriHidrosDikey.add(row.getCell(0).getStringCellValue());
        });
    }

    public static void readExcel4HidrosTankYatay(String filePath, DataManipulator dataManipulator) {
        readExcelData(filePath, "Hidros-Tank-Yatay", dataManipulator, (row, dm) -> {
            dm.tankKapasitesiDegerleriHidrosYatay.add(row.getCell(0).getStringCellValue());
        });
    }

    public static void readExcel4HidrosPlatform(String filePath, DataManipulator dataManipulator) {
        readExcelData(filePath, "Hidros-Platform", dataManipulator, (row, dm) -> {
            dm.platformDegerleriHidros.add(row.getCell(0).getStringCellValue());
        });
    }

    public static void readExcel4HidrosValf(String filePath, DataManipulator dataManipulator) {
        readExcelData(filePath, "Hidros-Valf-Deger", dataManipulator, (row, dm) -> {
            dm.valfDegerleriHidros.add(row.getCell(0).getStringCellValue());
        });
    }

    public static void readExcel4Parca(String filePath, String sheetName, HashMap<String, HashMap<String, String>> map) {
        readExcelData(filePath, sheetName, new DataManipulator(), (row, dm) -> {
            Cell keyCell = row.getCell(0);
            Cell value1Cell = row.getCell(1);
            Cell value2Cell = row.getCell(2);

            if (keyCell != null) {
                if (value1Cell != null) {
                    if (value2Cell != null) {
                        String key = keyCell.getStringCellValue();
                        String value1 = value1Cell.getStringCellValue();
                        String value2 = String.valueOf(value2Cell.getNumericCellValue());

                        if (!key.isEmpty()) {
                            HashMap<String, String> innerMap = new HashMap<>();
                            innerMap.put("B", value1);
                            innerMap.put("C", value2);
                            map.put(key, innerMap);
                        }
                    } else {
                        //logger.log(Level.WARNING, "Null value2Cell encountered at row " + row.getRowNum() + " in sheet " + sheetName);
                    }
                } else {
                    //logger.log(Level.WARNING, "Null value1Cell encountered at row " + row.getRowNum() + " in sheet " + sheetName);
                }
            } else {
                //logger.log(Level.WARNING, "Null keyCell encountered at row " + row.getRowNum() + " in sheet " + sheetName);
            }
        });
    }

    public static void readExcel4ParcaHidrosMotor380(String filePath, DataManipulator dataManipulator) {
        readExcel4Parca(filePath, "Parça-Hidros-Motor380", dataManipulator.hidros380Parca);
    }

    public static void readExcel4ParcaHidrosMotor220(String filePath, DataManipulator dataManipulator) {
        readExcel4Parca(filePath, "Parça-Hidros-Motor220", dataManipulator.hidros220Parca);
    }

    public static void readExcel4ParcaHidrosPompa(String filePath, DataManipulator dataManipulator) {
        readExcel4Parca(filePath, "Parça-Hidros-Pompa", dataManipulator.hidrosPompaParca);
    }

    public static void readExcel4ParcaHidrosPompaCivata(String filePath, DataManipulator dataManipulator) {
        readExcel4Parca(filePath, "Parça-Hidros-Pompa-Civata", dataManipulator.hidrosPompaCivataParca);
    }

    public static void readExcel4ParcaHidrosTankDikey(String filePath, DataManipulator dataManipulator) {
        readExcel4Parca(filePath, "Parça-Hidros-Tank-Dikey", dataManipulator.hidrosDikeyTankParca);
    }

    public static void readExcel4ParcaHidrosTankYatay(String filePath, DataManipulator dataManipulator) {
        readExcel4Parca(filePath, "Parça-Hidros-Tank-Yatay", dataManipulator.hidrosYatayTankParca);
    }

    public static void readExcel4ParcaHidrosValfDikeyCift(String filePath, DataManipulator dataManipulator) {
        readExcel4Parca(filePath, "Parça-Hidros-Valf-Dikey-Çift", dataManipulator.hidrosDikeyCiftHizParca);
    }

    public static void readExcel4ParcaHidrosValfDikeyTek(String filePath, DataManipulator dataManipulator) {
        readExcel4Parca(filePath, "Parça-Hidros-Valf-Dikey-Tek", dataManipulator.hidrosDikeyTekHizParca);
    }

    public static void readExcel4ParcaHidrosValfYatayTek(String filePath, DataManipulator dataManipulator) {
        readExcel4Parca(filePath, "Parça-Hidros-Valf-Yatay-Tek", dataManipulator.hidrosYatayTekHizParca);
    }

    public static void readExcel4ParcaHidrosValfYatayCift(String filePath, DataManipulator dataManipulator) {
        readExcel4Parca(filePath, "Parça-Hidros-Valf-Yatay-Çift", dataManipulator.hidrosYatayCiftHizParca);
    }

    public static void readExcel4ParcaHidrosValfDikeyCiftESP(String filePath, DataManipulator dataManipulator) {
        readExcel4Parca(filePath, "Parça-Hidros-Valf-Dikey-ÇiftESP", dataManipulator.hidrosDikeyCiftHizParcaESP);
    }

    public static void readExcel4ParcaHidrosValfDikeyTekESP(String filePath, DataManipulator dataManipulator) {
        readExcel4Parca(filePath, "Parça-Hidros-Valf-Dikey-Tek", dataManipulator.hidrosDikeyTekHizParcaESP);
    }

    public static void readExcel4ParcaHidrosValfYatayTekESP(String filePath, DataManipulator dataManipulator) {
        readExcel4Parca(filePath, "Parça-Hidros-Valf-Yatay-Tek", dataManipulator.hidrosYatayTekHizParcaESP);
    }

    public static void readExcel4ParcaHidrosValfYatayCiftESP(String filePath, DataManipulator dataManipulator) {
        readExcel4Parca(filePath, "Parça-Hidros-Valf-Yatay-ÇiftESP", dataManipulator.hidrosYatayCiftHizParcaESP);
    }

    public static void readExcel4ParcaHidrosPlatformDevirmeli(String filePath, DataManipulator dataManipulator) {
        readExcel4Parca(filePath, "Parça-Hidros-Platform-Devirmeli", dataManipulator.hidrosDevirmeliParca);
    }

    public static void readExcel4ParcaHidrosGenel(String filePath, DataManipulator dataManipulator) {
        readExcel4Parca(filePath, "Parça-Hidros-Genel", dataManipulator.hidrosGenelParca);
    }

    public static void readExcel4ParcaHidrosTam(String filePath, DataManipulator dataManipulator) {
        readExcel4Parca(filePath, "Parça-Hidros-Tam", dataManipulator.hidrosTamParca);
    }

    public static void readExcel4ParcaHidrosTamYatay(String filePath, DataManipulator dataManipulator) {
        readExcel4Parca(filePath, "Parça-Hidros-Tam-Yatay", dataManipulator.hidrosTamParcaYatay);
    }

    public static void readExcel4ParcaHidrosTamDikey(String filePath, DataManipulator dataManipulator) {
        readExcel4Parca(filePath, "Parça-Hidros-Tam-Dikey", dataManipulator.hidrosTamParcaDikey);
    }

    public static void readExcel4ParcaHidrosTamESPYok(String filePath, DataManipulator dataManipulator) {
        readExcel4Parca(filePath, "Parça-Hidros-ESP-Yok", dataManipulator.hidrosTamParcaESPHaric);
    }

    public static void readExcel4ParcaHidrosOzelTekValf(String filePath, DataManipulator dataManipulator) {
        readExcel4Parca(filePath, "Parça-Hidros-Özel-Tek-Valf", dataManipulator.hidrosTamParcaOzelTekValf);
    }
}
