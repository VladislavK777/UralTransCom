package com.uraltranscom.service;

/*
*
* Интерфейс получения списка вагонов
*
* @author Vladislav Klochkov
* @version 1.0
* @create 25.10.2017
*
* 06.11.2017
*   @version 1.1
*   1. Добавлено внесение в мапу название ЖД, для более детального поиска номера станции
* 10.11.2017
*   @version 1.4
*   1. Переделано получения целого числа в поле Номер вагона
*
*/

public interface GetListOfWagons {
    void fillMapOfWagons();
}
