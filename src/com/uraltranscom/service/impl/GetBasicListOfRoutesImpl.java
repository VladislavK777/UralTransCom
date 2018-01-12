package com.uraltranscom.service.impl;

import com.uraltranscom.model.Route;
import com.uraltranscom.service.GetBasicListOfRoutes;
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
* Класс получения списка маршрутов
*
* @author Vladislav Klochkov
* @version 2.0
* @create 25.10.2017
*
* 17.11.2017
*   1. Изменен метод заполнения Map
*/

public class GetBasicListOfRoutesImpl implements GetBasicListOfRoutes {

    // Подключаем логгер
    private static Logger logger = LoggerFactory.getLogger(GetBasicListOfRoutesImpl.class);

    // Основаная мапа, куда записываем все маршруты
    private Map<Integer, Route> mapOfRoutes = new HashMap<>();

    // Переменные для работы с файлами
    private File file = new File("C:\\Users\\Vladislav.Klochkov\\Desktop\\test.xlsx");
    private FileInputStream fileInputStream;

    // Переменные для работы с Excel файлом(формат XLSX)
    private XSSFWorkbook xssfWorkbook;
    private XSSFSheet sheet;

    // Конструктор заполняет основную мапу
    public GetBasicListOfRoutesImpl() {
        fillMapOfRoutes();
    }

    @Override
    public void fillMapOfRoutes() {

        // Получаем файл формата xls
        try {
            fileInputStream = new FileInputStream(this.file);
            xssfWorkbook = new XSSFWorkbook(fileInputStream);

            // Заполняем Map данными
            sheet = xssfWorkbook.getSheetAt(0);
            int i = 0;
            for (int j = 1; j < sheet.getLastRowNum() + 1; j++) {
                XSSFRow row = sheet.getRow(0);

                String keyOfStationDeparture = null;
                String nameOfStationDeparture = null;
                String keyOfStationDestination = null;
                String nameOfStationDestination = null;
                String distanceOfWay = null;
                String VIP = null;

                for (int c = 0; c < row.getLastCellNum(); c++) {
                    if (row.getCell(c).getStringCellValue().equals("Код станции отправления")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        keyOfStationDeparture = xssfRow.getCell(c).getStringCellValue();
                    }
                    if (row.getCell(c).getStringCellValue().equals("Станция отправления")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        nameOfStationDeparture = xssfRow.getCell(c).getStringCellValue();
                    }
                    if (row.getCell(c).getStringCellValue().equals("Код станции назначения")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        keyOfStationDestination = xssfRow.getCell(c).getStringCellValue();
                    }
                    if (row.getCell(c).getStringCellValue().equals("Станция назначения")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        nameOfStationDestination = xssfRow.getCell(c).getStringCellValue();
                    }
                    if (row.getCell(c).getStringCellValue().equals("Расстояние, км.")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        String val = Double.toString(xssfRow.getCell(c).getNumericCellValue());
                        double valueDouble = xssfRow.getCell(c).getNumericCellValue();
                        if ((valueDouble - (int) valueDouble) * 1000 == 0) {
                            val = (int) valueDouble + "";
                        }
                        distanceOfWay = val;
                    }
                    if (row.getCell(c).getStringCellValue().equals("Приоритет")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        String value = xssfRow.getCell(c).getStringCellValue();
                        if (value.equals("Да")) {
                            VIP = "1";
                        } else {
                            VIP = "0";
                        }
                    }
                }
                mapOfRoutes.put(i, new Route(keyOfStationDeparture, nameOfStationDeparture, keyOfStationDestination, nameOfStationDestination, distanceOfWay, VIP));
                i++;
            }
        } catch (IOException e) {
            logger.error("Ошибка загруки файла");
        } catch (OLE2NotOfficeXmlFileException e1) {
            logger.error("Некорректный формат файла, необходим формат xlsx");
        }
    }

    public Map<Integer, Route> getMapOfRoutes() {
        return mapOfRoutes;
    }

    public void setMapOfRoutes(Map<Integer, Route> mapOfRoutes) {
        this.mapOfRoutes = mapOfRoutes;
    }
}
