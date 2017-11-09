import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/*
*
* Класс получения Кодов станций
*
* @author Vladislav Klochkov
* @version 1.0
* @create 25.10.2017
*
* 06.11.2017
*   @version 1.1
*   1. В метод и запрос добавлен параметр road
*
*/

public class GetNumberOfStationImpl extends ConnectionToDBMySQL implements GetNumberOfStation {

    // Подключаем логгер
    private static Logger logger = LoggerFactory.getLogger(GetNumberOfStationImpl.class);

    private static Connection connection;
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;

    @Override
    public String codeOfStation(String name, String road) {
        // Переменная, Код станции
        String stationCode = new String();
        try {
            // Открываем соединение с БД
            connection = DriverManager.getConnection(getUrl(), getUser(), getPass());

            // Подготавливаем запрос
            preparedStatement = connection.prepareStatement("select s.station_key from stations s where s.station_name = ? and s.road = ?");


            // Определяем значения параметров
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, road);

            // Выполняем запрос
            resultSet = preparedStatement.executeQuery();

            // Вычитываем полученное значение
            while (resultSet.next()) {
                stationCode = resultSet.getString(1);
            }
        } catch (SQLException sqlEx) {
            logger.error("Ошибка запроса " + preparedStatement);
        } finally {
            try {
                connection.close();
            } catch (SQLException se) {
                logger.error("Ошибка закрытия соединения");
            }
            try {
                resultSet.close();
            } catch (SQLException se) {
                logger.error("Ошибка закрытия соединения");
            }
            try {
                preparedStatement.close();
            } catch (SQLException se) {
                logger.error("Ошибка закрытия соединения");
            }
        }
        return stationCode;
    }

    // Метод выстроения строки
    @Override
    public String getStringQueryOfRoute(String nameOfStationStart, String roadOfStationStart, String nameOfStationEnd, String roadOfStationEnd) {
        StringBuilder stringBuilder = new StringBuilder();

        // Получаем код станции назначения вагона
        stringBuilder.append(codeOfStation(nameOfStationStart, roadOfStationStart));

        stringBuilder.append(";");

        // Получаем код станции отправления маршрута
        stringBuilder.append(codeOfStation(nameOfStationEnd, roadOfStationEnd));
        return stringBuilder.toString();
    }
}
