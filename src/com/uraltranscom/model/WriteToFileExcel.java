package com.uraltranscom.model;

/*
*
* класс записи в файл Excel
*
* @author Vladislav Klochkov
* @version 1.3
* @create 09.11.2017
*
*/

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WriteToFileExcel {

    private WriteToFileExcel() {
    }

    // Подключаем логгер
    private static Logger logger = LoggerFactory.getLogger(WriteToFileExcel.class);

    // Успешная выгрузка
    private static boolean isOk = false;
    private static Path path = Paths.get("C:\\Users\\Vladislav.Klochkov\\Desktop\\report.xlsx");
    private static String fileName = path.toString();

    // Метод записи в файл распределенных маршрутов
    protected static synchronized void writeToFileExcelDistributedRoutes (String numberOfWagon, Route listOfRoutes, Integer distance, Double days) {
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
                try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(new File(fileName)))) {
                    XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
                    xssfWorkbook.createSheet("Распределенные рейсы");
                    xssfWorkbook.createSheet("Нераспределенные рейсы");
                    xssfWorkbook.createSheet("Нераспределенные вагоны");
                    xssfWorkbook.write(bufferedOutputStream);
                    xssfWorkbook.close();
                }
            }

            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(fileName)))) {
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook(bufferedInputStream);
                XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
                XSSFRow rowHead = sheet.createRow(0);
                int rowCount = sheet.getPhysicalNumberOfRows() - 1;
                XSSFRow row = sheet.createRow(rowCount + 1);
                Cell cell;

                // Заголовки столбцов
                cell = rowHead.createCell(0, CellType.STRING);
                cell.setCellValue("Номер вагона");
                cell = rowHead.createCell(1, CellType.STRING);
                cell.setCellValue("Маршрут");
                cell = rowHead.createCell(2, CellType.STRING);
                cell.setCellValue("Порожнее расстояние до станции отправления, км");
                cell = rowHead.createCell(3, CellType.STRING);
                cell.setCellValue("Количесвто дней оботора, сутки");

                // Заполняем данными
                cell = row.createCell(0);
                cell.setCellValue(numberOfWagon);
                cell = row.createCell(1);
                cell.setCellValue(listOfRoutes.toString());
                cell = row.createCell(2);
                cell.setCellValue(distance);
                cell = row.createCell(3);
                cell.setCellValue(days);

                try (BufferedOutputStream fio = new BufferedOutputStream(new FileOutputStream(fileName))) {
                    xssfWorkbook.write(fio);
                }
            }
            isOk = true;
        } catch (IOException e) {
            logger.error("Ошибка записи в файл");
            isOk = false;
        }
    }

    // Метод записи в файл нераспределенных маршрутов
    protected static synchronized void writeToFileExcelUnDistributedRoutes (Map<Integer, Route> unDistributedRoutes) {
        try {
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(fileName)))) {
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook(bufferedInputStream);
                XSSFSheet sheet = xssfWorkbook.getSheetAt(1);
                XSSFRow rowHead = sheet.createRow(0);
                XSSFRow row;
                Cell cell;

                // Заголовки столбцов
                cell = rowHead.createCell(0, CellType.STRING);
                cell.setCellValue("Маршрут");

                // Заполняем данными
                List<Route> unDistributedList = new ArrayList<>();
                for (Map.Entry<Integer, Route> unDistributedRoute : unDistributedRoutes.entrySet()) {
                    unDistributedList.add(unDistributedRoute.getValue());
                }

                for (int i = 0; i < unDistributedList.size(); i++) {
                    int rowCount = sheet.getPhysicalNumberOfRows() - 1;
                    row = sheet.createRow(rowCount + 1);
                    cell = row.createCell(0);
                    cell.setCellValue(String.valueOf(unDistributedList.get(i).toString()));
                }

                try (BufferedOutputStream fio = new BufferedOutputStream(new FileOutputStream(fileName))) {
                    xssfWorkbook.write(fio);
                }
            }
            isOk = true;
        } catch (IOException e) {
            logger.error("Ошибка записи в файл");
            isOk = false;
        }
    }

    // Метод записи в файл нераспределенных вагонов
    protected static synchronized void writeToFileExcelUnDistributedWagons (Set<String> listOfWagon) {
        try {
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(fileName)))) {
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook(bufferedInputStream);
                XSSFSheet sheet = xssfWorkbook.getSheetAt(2);
                XSSFRow rowHead = sheet.createRow(0);
                XSSFRow row;
                Cell cell;

                // Заголовки столбцов
                cell = rowHead.createCell(0, CellType.STRING);
                cell.setCellValue("Номер вагона");

                // Заполняем данными
                for (String list : listOfWagon) {
                    int rowCount = sheet.getPhysicalNumberOfRows() - 1;
                    row = sheet.createRow(rowCount + 1);
                    cell = row.createCell(0);
                    cell.setCellValue(list);
                }

                try (BufferedOutputStream fio = new BufferedOutputStream(new FileOutputStream(fileName))) {
                    xssfWorkbook.write(fio);
                }
            }
            isOk = true;
        } catch (IOException e) {
            logger.error("Ошибка записи в файл");
            isOk = false;
        }
    }
}
