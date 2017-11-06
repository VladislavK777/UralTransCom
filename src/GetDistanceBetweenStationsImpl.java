import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;

import java.io.IOException;
import java.sql.*;
import java.util.List;

/*
*
* Класс получения расстояния между станциями
*
* @author Vladislav Klochkov
* @version 1.0
* @create 25.10.2017
*
*/

public class GetDistanceBetweenStationsImpl extends VaribaleForRestAPI implements GetDistanceBetweenStations {

    // Подключаем логгер
    private static Logger logger = LoggerFactory.getLogger(GetDistanceBetweenStationsImpl.class);

    private ConnectionToDBMySQL connectionToDBMySQL = new ConnectionToDBMySQL();
    private static Connection connection;
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private AdditionalClassForGetRoadOfStations additionalClassForGetRoadOfStations = new AdditionalClassForGetRoadOfStations();

    private GetNumberOfStationImpl getNumberOfStationImpl = new GetNumberOfStationImpl();

    @Override
    public String getDistanceBetweenStations(String nameOfStationStart, String nameOfStationEnd) {
        String distance = new String();
        try {
            // Открываем соединение с БД
            connection = DriverManager.getConnection(connectionToDBMySQL.getUrl(), connectionToDBMySQL.getUser(), connectionToDBMySQL.getPass());

            // Подготавливаем запрос
            preparedStatement = connection.prepareStatement("select d.distance from distancebetweentwostations d where d.station_name_start = ? and d.station_name_end = ?");

            // Определяем значения параметров
            preparedStatement.setString(1, nameOfStationStart);
            preparedStatement.setString(2, nameOfStationEnd);

            // Выполняем запрос
            resultSet = preparedStatement.executeQuery();

            // Вычитываем полученное значение
            while (resultSet.next()) {
                distance = resultSet.getString(1);
            }

            // Если в базе нет расстояния, то берем с веб-сервиса и добавляем в базу
            if (distance.isEmpty()) {
                String route = getNumberOfStationImpl.getStringQueryOfRoute(nameOfStationStart, nameOfStationEnd);
                Call<List<SomeResponce>> result = null;
                try {
                    result = api.execSomeMethod("froute.php", route);
                    distance = result.execute().body().get(0).routes;
                    additionalClassForGetRoadOfStations.insertDistanceToDB(nameOfStationStart, nameOfStationEnd, distance);
                } catch (IOException e) {
                    logger.error("Ошибка получения данных с портала: " + result.request().url() + " " + nameOfStationStart + " " + nameOfStationEnd);
                }
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
        return distance;
    }
}
