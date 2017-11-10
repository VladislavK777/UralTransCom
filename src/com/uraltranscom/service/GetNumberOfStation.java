package com.uraltranscom.service;

/*
*
* Интерфейс получения Кодов станций
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

public interface GetNumberOfStation {
    String codeOfStation(String name, String road);
    String getStringQueryOfRoute(String nameOfStationStart, String roadOfStationStart, String nameOfStationEnd, String roadOfStationEnd);
}
