package com.uraltranscom.service.impl;

/*
*
* Класс проверки корректности кода станции
*
* @author Vladislav Klochkov
* @version 2.0
* @create 12.01.2018
*
*/

import com.uraltranscom.dao.ConnectionToDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class CheckExistKeyOfStation extends ConnectionToDB {
    // Подключаем логгер
    private static Logger logger = LoggerFactory.getLogger(CheckExistKeyOfStation.class);

    private static Connection connection;
    private static ResultSet resultSet;
    private static PreparedStatement preparedStatement;

    public boolean checkExistKey(String keyOfStation) {
        Boolean isExist = false;
        try {
            // Открываем соединение с БД
            connection = DriverManager.getConnection(getURL(), getUSER(), getPASS());

            // Подготавливаем запрос
            preparedStatement = connection.prepareStatement("select distinct 1 from distances where (station_id1 = ? or station_id2 = ?)");

            // Определяем значения параметров
            preparedStatement.setString(1, keyOfStation);
            preparedStatement.setString(2, keyOfStation);

            // Выполняем запрос
            resultSet = preparedStatement.executeQuery();

            // Вычитываем полученное значение
            while (resultSet.next()) {
                if (!resultSet.wasNull()) {
                    isExist = true;
                }
            }
        } catch (SQLException ex) {
            logger.error("Ошибка запроса");
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
            return isExist;
        }
    }
}
