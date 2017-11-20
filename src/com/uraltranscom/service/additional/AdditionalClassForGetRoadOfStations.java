package com.uraltranscom.service.additional;/*
*
* Дополнительный класс для получения названия ЖД
*
* @author Vladislav Klochkov
* @version 1.0
* @create 03.11.2017
*
* 07.11.2017
*   1. Удалены методы получения названия ЖД
*
*/

import com.uraltranscom.dao.ConnectionToDBMySQL;
import com.uraltranscom.service.impl.GetNumberOfStationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class AdditionalClassForGetRoadOfStations extends ConnectionToDBMySQL {

    // Подключаем логгер
    private static Logger logger = LoggerFactory.getLogger(AdditionalClassForGetRoadOfStations.class);

    private static Connection connection;
    private static PreparedStatement preparedStatement;

    private GetNumberOfStationImpl getNumberOfStation = new GetNumberOfStationImpl();

    // Метод вставки записи в БД
    public void insertDistanceToDB(String nameOfStationStart, String roadOfStationStart, String nameOfStationEnd, String roadOfStationEnd, String distance) {
        try {
            // Открываем соединение с БД
            connection = DriverManager.getConnection(getURL(), getUSER(), getPASS());

            // Подготавливаем запрос
            preparedStatement = connection.prepareStatement("insert into distancebetweentwostations (station_key_start, station_name_start, road_station_start, station_key_end, station_name_end, road_station_end, distance) values (?, ?, ?, ?, ?, ?, ?)");

            // Заполняем параметры
            int i = 0;
            preparedStatement.setString(++i, getNumberOfStation.codeOfStation(nameOfStationStart, roadOfStationStart));
            preparedStatement.setString(++i, nameOfStationStart);
            preparedStatement.setString(++i, roadOfStationStart);
            preparedStatement.setString(++i, getNumberOfStation.codeOfStation(nameOfStationEnd, roadOfStationEnd));
            preparedStatement.setString(++i, nameOfStationEnd);
            preparedStatement.setString(++i, roadOfStationEnd);
            preparedStatement.setString(++i, distance);

            // Выполняем запрос
            preparedStatement.executeUpdate();
        } catch (SQLException sqlEx) {
            logger.error("Ошибка запроса INSERT " + sqlEx.getMessage() + "; Start: " + nameOfStationStart + ": Road: " + roadOfStationStart + "; End: " + nameOfStationEnd + ": Road: " + roadOfStationEnd);
        } finally {
            try {
                connection.close();
            } catch (SQLException se) {
                logger.error("Ошибка закрытия соединения");
            }
            try {
                preparedStatement.close();
            } catch (SQLException se) {
                logger.error("Ошибка закрытия соединения");
            }
        }
    }

    /*// Метод получения имени жд для станции старта
    public String getNameOfRoadOfStationStart(String nameStationStart) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String nameRoad = new String();
        try {
            // Открываем соединение с БД
            connection = DriverManager.getConnection(getUrl(), getUser(), getPass());

            // Подготавливаем запрос
            preparedStatement = connection.prepareStatement("select d.road_station_start from distancebetweentwostations d where d.station_key_start = ?");

            // Определяем значения параметров
            preparedStatement.setString(1, getNumberOfStation.codeOfStation(nameStationStart));

            // Выполняем запрос
            resultSet = preparedStatement.executeQuery();

            // Вычитываем полученное значение
            while (resultSet.next()) {
                nameRoad = resultSet.getString(1);
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
                preparedStatement.close();
            } catch (SQLException se) {
                logger.error("Ошибка закрытия соединения");
            }
            try {
                resultSet.close();
            } catch (SQLException se) {
                logger.error("Ошибка закрытия соединения");
            }
        }
        return nameRoad;
    }

    // Метод получения имени жд для станции конца
    public String getNameOfRoadOfStationEnd(String nameStationEnd) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String nameRoad = new String();
        try {
            // Открываем соединение с БД
            connection = DriverManager.getConnection(getUrl(), getUser(), getPass());

            // Подготавливаем запрос
            preparedStatement = connection.prepareStatement("select d.road_station_end from distancebetweentwostations d where d.station_key_end = ?");

            // Определяем значения параметров
            preparedStatement.setString(1, getNumberOfStation.codeOfStation(nameStationEnd));

            // Выполняем запрос
            resultSet = preparedStatement.executeQuery();

            // Вычитываем полученное значение
            while (resultSet.next()) {
                nameRoad = resultSet.getString(1);
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
                preparedStatement.close();
            } catch (SQLException se) {
                logger.error("Ошибка закрытия соединения");
            }
            try {
                resultSet.close();
            } catch (SQLException se) {
                logger.error("Ошибка закрытия соединения");
            }
        }
        return nameRoad;
    }*/
}
