package com.uraltranscom.service;
/*
*
* Интерфейс получения расстояния между станциями
*
* @author Vladislav Klochkov
* @version 2.0
* @create 25.10.2017
*
* 06.11.2017
*   1. Добавлено внесение в мапу название ЖД, для более детального поиска номера станции
*
*/

public interface GetDistanceBetweenStations {
    int getDistanceBetweenStations(String keyOfStationDeparture, String keyOfStationDestination);

}
