package com.uraltranscom.service.impl;

import com.uraltranscom.model.Wagon;
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
import java.util.ArrayList;
import java.util.List;

/*
*
* Класс получения списка вагонов
*
* @author Vladislav Klochkov
* @version 2.0
* @create 25.10.2017
*
* 06.11.2017
*   1. Добавлено внесение в Map название ЖД, для более детального поиска номера станции
* 10.11.2017
*   1. Переделано получения целого числа в поле Номер вагона
* 17.11.2017
*   1. Изменен метод заполнения Map
*
*/

public class GetListOfWagonsImpl implements GetListOfWagons {

    // Подключаем логгер
    private static Logger logger = LoggerFactory.getLogger(GetListOfWagonsImpl.class);

    // Основаная мапа, куда записываем все вагоны
    private List<Wagon> listOfWagons = new ArrayList<>();

    // Переменные для работы с файлами
    private File file = new File("C:\\Users\\Vladislav.Klochkov\\Desktop\\wagons.xlsx");
    private FileInputStream fileInputStream;

    // Переменные для работы с Excel файлом(формат XLSX)
    private XSSFWorkbook xssfWorkbook;
    private XSSFSheet sheet;

    public GetListOfWagonsImpl() {
        fillMapOfWagons();
    }

    // Заполняем Map вагонами
    @Override
    public void fillMapOfWagons() {
        // Получаем файл формата xls
        try {
            fileInputStream = new FileInputStream(this.file);
            xssfWorkbook = new XSSFWorkbook(fileInputStream);

            // Заполняем мапу данными
            sheet = xssfWorkbook.getSheetAt(0);
            for (int j = 5; j < sheet.getLastRowNum() + 1; j++) {
                XSSFRow row = sheet.getRow(4);

                String numberOfWagon = null;
                String typeOfWagon = null;
                String keyOfStationDestination = null;

                for (int c = 0; c < row.getLastCellNum(); c++) {
                    if (row.getCell(c).getStringCellValue().equals("Вагон №")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        String val = Double.toString(xssfRow.getCell(c).getNumericCellValue());
                        double valueDouble = xssfRow.getCell(c).getNumericCellValue();
                        if ((valueDouble - (int) valueDouble) * 1000 == 0) {
                            val = (int) valueDouble + "";
                        }
                        numberOfWagon = val;
                    }
                    if (row.getCell(c).getStringCellValue().equals("Тип вагона")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        typeOfWagon = xssfRow.getCell(c).getStringCellValue();
                    }
                    if (row.getCell(c).getStringCellValue().equals("Код станции назначения")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        keyOfStationDestination = xssfRow.getCell(c).getStringCellValue();
                    }
                }
                listOfWagons.add(new Wagon(numberOfWagon, typeOfWagon, keyOfStationDestination));
            }
        } catch (IOException e) {
            logger.error("Ошибка загруки файла");
        } catch (OLE2NotOfficeXmlFileException e1) {
            logger.error("Некорректный формат файла, необходим формат xlsx");
        }
    }

    public List<Wagon> getListOfWagons() {
        return listOfWagons;
    }

    public void setListOfWagons(List<Wagon> listOfWagons) {
        this.listOfWagons = listOfWagons;
    }
}
