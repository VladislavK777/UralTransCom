import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class GetBasicListOfRoutes {
    private HashMap<Integer, List<Object>> map = new HashMap<>();
    private File file = new File("C:\\Users\\Vladislav.Klochkov\\Desktop\\test.xls");
    private HSSFWorkbook workBook;
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;
    private Sheet sheet;

    public GetBasicListOfRoutes() {
        fillMap();
    }

    public void fillMap() {
        // Получаем файл формата xls
        try {
            fileInputStream = new FileInputStream(this.file);
            workBook = new HSSFWorkbook(fileInputStream);
        } catch (IOException e) {
            // Добавить логгер
            System.out.println("Ошибка загруки файла");
        }

        // Заполняем маппу данными
        sheet = workBook.getSheetAt(0);
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
            map.put(i, tempList);
            i++;
        }
       // System.out.println(map);
    }

    public HashMap<Integer, List<Object>> getMap() {
        return map;
    }

    public void setMap(HashMap<Integer, List<Object>> map) {
        this.map = map;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public HSSFWorkbook getWorkBook() {
        return workBook;
    }

    public void setWorkBook(HSSFWorkbook workBook) {
        this.workBook = workBook;
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

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }
}
