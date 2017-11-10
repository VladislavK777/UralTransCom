package com.uraltranscom.service.impl;

import com.uraltranscom.service.GetBasicListOfRoutes;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/*
*
* Класс получения списка маршрутов
*
* @author Vladislav Klochkov
* @version 1.0
* @create 25.10.2017
*
*/

public class GetBasicListOfRoutesImpl implements GetBasicListOfRoutes {

    // Подключаем логгер
    private static Logger logger = LoggerFactory.getLogger(GetBasicListOfRoutesImpl.class);

    // Основаная мапа, куда записываем все маршруты
    private Map<Integer, List<Object>> mapOfRoutes = new HashMap<>();

    // Переменные для работы с файлами
    private File file = new File("C:\\Users\\Vladislav.Klochkov\\Desktop\\test.xlsx");
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;

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

            // Заполняем мапу данными
            sheet = xssfWorkbook.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();
            int i = 0;
            while (it.hasNext()) {
                Row row = it.next();
                Iterator<Cell> cells = row.iterator();
                List<Object> tempList = new ArrayList<>();
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    tempList.add(cell);
                }
                mapOfRoutes.put(i, tempList);
                i++;
            }
        } catch (IOException e) {
            logger.error("Ошибка загруки файла");
        } catch (OLE2NotOfficeXmlFileException e1) {
            logger.error("Некорректный формат файла, необходим формат xlsx");
        }
    }

    public Map<Integer, List<Object>> getMapOfRoutes() {
        return mapOfRoutes;
    }

    public void setMapOfRoutes(Map<Integer, List<Object>> mapOfRoutes) {
        this.mapOfRoutes = mapOfRoutes;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public FileInputStream getFileInputStream() {
        return fileInputStream;
    }

    public void setFileInputStream(FileInputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
    }

    public FileOutputStream getFileOutputStream() {
        return fileOutputStream;
    }

    public void setFileOutputStream(FileOutputStream fileOutputStream) {
        this.fileOutputStream = fileOutputStream;
    }

    public XSSFWorkbook getXssfWorkbook() {
        return xssfWorkbook;
    }

    public void setXssfWorkbook(XSSFWorkbook xssfWorkbook) {
        this.xssfWorkbook = xssfWorkbook;
    }

    public XSSFSheet getSheet() {
        return sheet;
    }

    public void setSheet(XSSFSheet sheet) {
        this.sheet = sheet;
    }
}
