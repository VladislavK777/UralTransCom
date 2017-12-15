package com.uraltranscom.service;

/*
*
* Интерфейс получения списка вагонов
*
* @author Vladislav Klochkov
* @version 2.0
* @create 25.10.2017
*
* 06.11.2017
*   1. Добавлено внесение в мапу название ЖД, для более детального поиска номера станции
* 10.11.2017
*   1. Переделано получения целого числа в поле Номер вагона
*
*/

public interface GetListOfWagons {
    void fillMapOfWagons();
}
