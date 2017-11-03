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
*/

public class GetNumberOfStationImpl extends ConnectionToDBMySQL implements GetNumberOfStation {

    // Подключаем логгер
    private static Logger logger = LoggerFactory.getLogger(GetNumberOfStationImpl.class);

    private static Connection connection;
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;

    @Override
    public String codeOfStation(String name) {
        // Переменная, Код станции
        String stationCode = new String();
        try {
            // Открываем соединение с БД
            connection = DriverManager.getConnection(getUrl(), getUser(), getPass());

            // Подготавливаем запрос
            preparedStatement = connection.prepareStatement("select s.station_key from stations s where s.station_name = ?");


            // Определяем значения параметров
            preparedStatement.setString(1, name);

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
    public String getStringQueryOfRoute(String nameOfStation1, String nameOfStation2) {
        StringBuilder stringBuilder = new StringBuilder();

        // Получаем код станции назначения вагона
        stringBuilder.append(codeOfStation(nameOfStation1));

        stringBuilder.append(";");

        // Получаем код станции отправления маршрута
        stringBuilder.append(codeOfStation(nameOfStation2));
        return stringBuilder.toString();
    }


}
