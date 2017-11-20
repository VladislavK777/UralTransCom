package com.uraltranscom.service.impl;

import com.uraltranscom.service.GetListOfWagons;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
*
* Класс получения списка вагонов
*
* @author Vladislav Klochkov
* @version 1.0
* @create 25.10.2017
*
* 06.11.2017
*   @version 1.1
*   1. Добавлено внесение в мапу название ЖД, для более детального поиска номера станции
* 10.11.2017
*   @version 1.4
*   1. Переделано получения целого числа в поле Номер вагона
*
*/

public class GetListOfWagonsImpl implements GetListOfWagons {

    // Подключаем логгер
    private static Logger logger = LoggerFactory.getLogger(GetListOfWagonsImpl.class);

    // Основаная мапа, куда записываем все вагоны
    private Map<Integer, String> mapOfWagons = new HashMap<>();

    // Переменные для работы с файлами
    private File file = new File("C:\\Users\\Vladislav.Klochkov\\Desktop\\wagons.xlsx");
    private FileInputStream fileInputStream;

    // Переменные для работы с Excel файлом(формат XLSX)
    private XSSFWorkbook xssfWorkbook;
    private XSSFSheet sheet;

    public GetListOfWagonsImpl() {
        fillMapOfWagons();
    }

    // Заполняем мапу вагонами
    @Override
    public void fillMapOfWagons() {
        // Получаем файл формата xls
        try {
            fileInputStream = new FileInputStream(this.file);
            xssfWorkbook = new XSSFWorkbook(fileInputStream);

            // Заполняем мапу данными
            sheet = xssfWorkbook.getSheetAt(0);
            int i = 0;
            for (int j = 5; j < sheet.getLastRowNum() + 1; j++) {
                StringBuilder stringStationAndWagon = new StringBuilder();
                XSSFRow row = sheet.getRow(4);
                for (int c = 0; c < row.getLastCellNum(); c++) {
                    if (row.getCell(c).getStringCellValue().equals("Вагон №")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        String val = Double.toString(xssfRow.getCell(c).getNumericCellValue());
                        double valueDouble = xssfRow.getCell(c).getNumericCellValue();
                        if ((valueDouble - (int) valueDouble) * 1000 == 0) {
                            val = (int) valueDouble + "";
                        }
                        stringStationAndWagon.append(val);
                        stringStationAndWagon.append(", ");
                    }
                    if (row.getCell(c).getStringCellValue().equals("Дорога назначения")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        stringStationAndWagon.append(xssfRow.getCell(c).getStringCellValue());
                        stringStationAndWagon.append(", ");
                    }
                    if (row.getCell(c).getStringCellValue().equals("Станция назначения")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        stringStationAndWagon.append(xssfRow.getCell(c).getStringCellValue());
                    }
                }
                mapOfWagons.put(i, stringStationAndWagon.toString());
                i++;
            }
        } catch (IOException e) {
            logger.error("Ошибка загруки файла");
        } catch (OLE2NotOfficeXmlFileException e1) {
            logger.error("Некорректный формат файла, необходим формат xlsx");
        }
    }

    public Map<Integer, String> getMapOfWagons() {
        return mapOfWagons;
    }

    public void setMapOfWagons(Map<Integer, String> mapOfWagons) {
        this.mapOfWagons = mapOfWagons;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
