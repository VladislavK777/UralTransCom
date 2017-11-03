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

public class GetDistanceBetweenStationsImpl extends VaribalesForRestAPI implements GetDistanceBetweenStations {

    // Подключаем логгер
    private static Logger logger = LoggerFactory.getLogger(GetDistanceBetweenStationsImpl.class);

    private ConnectionToDBMySQL connectionToDBMySQL = new ConnectionToDBMySQL();
    private static Connection connection;
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;

    GetNumberOfStationImpl getNumberOfStationImpl = new GetNumberOfStationImpl();

    @Override
    public String getDistanceBetweenStations(String nameOfStation1, String nameOfStation2) {
        String distance = new String();
        try {
            // Открываем соединение с БД
            connection = DriverManager.getConnection(connectionToDBMySQL.getUrl(), connectionToDBMySQL.getUser(), connectionToDBMySQL.getPass());

            // Подготавливаем запрос
            preparedStatement = connection.prepareStatement("select d.distance from distancebetweentwostations d where d.station_name_start = ? and d.station_name_end = ?");


            // Определяем значения параметров
            preparedStatement.setString(1, nameOfStation1);
            preparedStatement.setString(2, nameOfStation2);

            // Выполняем запрос
            resultSet = preparedStatement.executeQuery();

            // Вычитываем полученное значение
            while (resultSet.next()) {
                distance = resultSet.getString(1);
            }

            if (distance.isEmpty()) {
                String route = getNumberOfStationImpl.getStringQueryOfRoute(nameOfStation1, nameOfStation2);
                System.out.println("С портала " + route);
                //System.out.println(route + " " + nameOfStation1 + " " + nameOfStation2);
                Call<List<SomeResponce>> result = null;
                try {
                    result = api.execSomeMethod("froute.php", route);
                    distance = result.execute().body().get(0).routes;
                    preparedStatement = connection.prepareStatement("insert into distancebetweentwostations (station_key_start, station_name_start, station_key_end, station_name_end, distance) values (?, ?, ?, ?, ?)");
                    preparedStatement.setString(1, getNumberOfStationImpl.codeOfStation(nameOfStation1));
                    preparedStatement.setString(2, nameOfStation1);
                    preparedStatement.setString(3, getNumberOfStationImpl.codeOfStation(nameOfStation2));
                    preparedStatement.setString(4, nameOfStation2);
                    preparedStatement.setString(5, distance);
                    preparedStatement.executeUpdate();
                } catch (IOException e) {
                    logger.error("Ошибка получения данных с портала: " + result.request().url());
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

        System.out.println(nameOfStation1 + " " + nameOfStation2);
        System.out.println(distance);
        return distance;
    }
}
