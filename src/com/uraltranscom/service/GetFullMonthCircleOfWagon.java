package com.uraltranscom.service;

/*
*
* Интерфейс расчета количества дней, затраченных вагоном за один цикл. По вагонам количесво дней суммируется
*
* @author Vladislav Klochkov
* @version 1.0
* @create 08.11.2017
*
*/

public interface GetFullMonthCircleOfWagon {
    void fullDays(String numberOfWagon, String typeOfWagon, Integer distanceOfEmpty, String distanceOfRoute);
}
