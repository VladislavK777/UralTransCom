package com.uraltranscom.service;
/*
*
* Интерфейс получения расстояния между станциями
*
* @author Vladislav Klochkov
* @version 1.0
* @create 25.10.2017
*
* 06.11.2017
*   1. Добавлено внесение в мапу название ЖД, для более детального поиска номера станции
*
*/

public interface GetDistanceBetweenStations {
    String getDistanceBetweenStations(String nameOfStationStart, String roadOfStationStart, String nameOfStationEnd, String roadOfStationEnd);

}
