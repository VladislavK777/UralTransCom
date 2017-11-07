import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

/*
*
* Класс получения списка вагонов
*
* @author Vladislav Klochkov
* @version 1.0
* @create 25.10.2017
*
* 06.11.2017 - Добавлено внесение в мапу название ЖД, для более детального поиска номера станции
*
*/

public class GetListOfWagons {

    // Подключаем логгер
    private static Logger logger = LoggerFactory.getLogger(GetListOfWagons.class);

    // Основаная мапа, куда записываем все вагоны
    private HashMap<Integer, String> mapOfWagons = new HashMap<>();

    // Переменные для работы с файлами
    private File file = new File("C:\\Users\\Vladislav.Klochkov\\Desktop\\wagons.xlsx");
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;

    // Переменные для работы с Excel файлом(формат XLSX)
    private XSSFWorkbook xssfWorkbook;
    private XSSFSheet sheet;

    public GetListOfWagons() {
        fillMapOfWagons();
    }

    // Заполняем мапу вагонами
    private void fillMapOfWagons() {
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
                        stringStationAndWagon.append(xssfRow.getCell(c).getStringCellValue());
                        stringStationAndWagon.append(", ");
                    } else if (row.getCell(c).getStringCellValue().equals("Дорога назначения")) {
                        XSSFRow xssfRow = sheet.getRow(j);
                        stringStationAndWagon.append(xssfRow.getCell(c).getStringCellValue());
                        stringStationAndWagon.append(", ");
                    } else if (row.getCell(c).getStringCellValue().equals("Ст. назначения")) {
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

        System.out.println(mapOfWagons);
    }

    public HashMap<Integer, String> getMapOfWagons() {
        return mapOfWagons;
    }

    public void setMapOfWagons(HashMap<Integer, String> mapOfWagons) {
        this.mapOfWagons = mapOfWagons;
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
