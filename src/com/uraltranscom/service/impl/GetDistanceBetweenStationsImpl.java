package com.uraltranscom.service.impl;

import com.uraltranscom.api.SomeResponce;
import com.uraltranscom.api.VaribaleForRestAPI;
import com.uraltranscom.dao.ConnectionToDBMySQL;
import com.uraltranscom.service.GetDistanceBetweenStations;
import com.uraltranscom.service.additional.AdditionalClassForGetRoadOfStations;
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
* 06.11.2017
*   1. Добавлено внесение в мапу название ЖД, для более детального поиска номера станции
*
*/

public class GetDistanceBetweenStationsImpl extends VaribaleForRestAPI implements GetDistanceBetweenStations {

    // Подключаем логгер
    private static Logger logger = LoggerFactory.getLogger(GetDistanceBetweenStationsImpl.class);

    private static Connection connection;
    private static PreparedStatement preparedStatement;
    private static ResultSet resultSet;
    private AdditionalClassForGetRoadOfStations additionalClassForGetRoadOfStations = new AdditionalClassForGetRoadOfStations();

    private GetNumberOfStationImpl getNumberOfStationImpl = new GetNumberOfStationImpl();

    @Override
    public String getDistanceBetweenStations(String nameOfStationStart, String roadOfStationStart, String nameOfStationEnd, String roadOfStationEnd) {
        String distance = new String();
        try {
            // Открываем соединение с БД
            connection = DriverManager.getConnection(ConnectionToDBMySQL.getURL(), ConnectionToDBMySQL.getUSER(), ConnectionToDBMySQL.getPASS());

            // Подготавливаем запрос
            preparedStatement = connection.prepareStatement("select d.distance from distancebetweentwostations d where ((d.station_name_start = ? and d.road_station_start = ?) and (d.station_name_end = ? and d.road_station_end = ?)) or ((d.station_name_start = ? and d.road_station_start = ?) and (d.station_name_end = ? and d.road_station_end = ?))");

            // Определяем значения параметров
            preparedStatement.setString(1, nameOfStationStart);
            preparedStatement.setString(2, roadOfStationStart);
            preparedStatement.setString(3, nameOfStationEnd);
            preparedStatement.setString(4, roadOfStationEnd);
            preparedStatement.setString(5, nameOfStationEnd);
            preparedStatement.setString(6, roadOfStationEnd);
            preparedStatement.setString(7, nameOfStationStart);
            preparedStatement.setString(8, roadOfStationStart);

            // Выполняем запрос
            resultSet = preparedStatement.executeQuery();

            // Вычитываем полученное значение
            while (resultSet.next()) {
                distance = resultSet.getString(1);
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
            //System.out.println(nameOfStationStart + " " +roadOfStationStart+ " " + nameOfStationEnd + " " + distance);
            // Если в базе нет расстояния, то берем с веб-сервиса и добавляем в базу
            if (distance.isEmpty()) {
                String route = getNumberOfStationImpl.getStringQueryOfRoute(nameOfStationStart, roadOfStationStart, nameOfStationEnd, roadOfStationEnd);
                Call<List<SomeResponce>> result = null;
                try {
                    result = api.execSomeMethod("froute.php", route);
                    distance = result.execute().body().get(0).routes;

                    //System.out.println("Нет в базе: " + nameOfStationStart + " " + nameOfStationEnd + " " + route);
                    additionalClassForGetRoadOfStations.insertDistanceToDB(nameOfStationStart, roadOfStationStart, nameOfStationEnd, roadOfStationEnd, distance);
                } catch (IOException e) {
                    logger.error("Ошибка получения данных с портала: " + result.request().url() + "; Start: " + nameOfStationStart + ": Road: " + roadOfStationStart + "; End: " + nameOfStationEnd + ": Road: " + roadOfStationEnd);
                }
            }
        return distance;
    }
}
