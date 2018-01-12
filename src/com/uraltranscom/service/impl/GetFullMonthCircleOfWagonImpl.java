package com.uraltranscom.service.impl;

import com.uraltranscom.service.GetFullMonthCircleOfWagon;

import java.util.HashMap;
import java.util.Map;

/*
*
* Класс расчета количества дней, затраченных вагоном за один цикл. По вагонам количесво дней суммируется
*
* @author Vladislav Klochkov
* @version 2.0
* @create 08.11.2017
*
*/

public class GetFullMonthCircleOfWagonImpl implements GetFullMonthCircleOfWagon {

    // Параметры загрузки вагонов
    // Крытый вагон
    private final int LOADING_OF_WAGON_KR = 7;

    // Полувагон
    private final int LOADING_OF_WAGON_PV = 4;

    // Выгрузка вагонов
    private final int UNLOADING_OF_WAGON = 4;

    Map<String, Double> mapOfDaysOfWagon = new HashMap<>();

    /*
    * Метод расчета дней, пройденных вагоном за один цикл
    * По вагонам количесво дней суммируется
    */
    @Override
    public void fullDays(String numberOfWagon, String typeOfWagon, Integer distanceOfEmpty, String distanceOfRoute) {
        double fullMonthCircle = 0;
        if (mapOfDaysOfWagon.get(numberOfWagon) == null) {
            fullMonthCircle += Double.valueOf(distanceOfEmpty) / 300 + 1;
            if (typeOfWagon.equals("КР")) {
                fullMonthCircle += LOADING_OF_WAGON_KR;
            } else {
                fullMonthCircle += LOADING_OF_WAGON_PV;
            }
            fullMonthCircle += Double.valueOf(distanceOfRoute) / 300 + 1;
            fullMonthCircle += UNLOADING_OF_WAGON;
            mapOfDaysOfWagon.put(numberOfWagon, fullMonthCircle);
        } else {
            for (Map.Entry<String, Double> map : mapOfDaysOfWagon.entrySet()) {
                if (map.getKey().equals(numberOfWagon)) {
                    double tempDays = map.getValue();
                    tempDays += Double.valueOf(distanceOfEmpty) / 300 + 1;
                    if (typeOfWagon.equals("КР")) {
                        tempDays += LOADING_OF_WAGON_KR;
                    } else {
                        tempDays += LOADING_OF_WAGON_PV;
                    }
                    tempDays += Double.valueOf(distanceOfRoute) / 300 + 1;
                    tempDays += UNLOADING_OF_WAGON;
                    mapOfDaysOfWagon.replace(map.getKey(), tempDays);
                }
            }
        }
    }

    public double getNumberOfDaysOfWagon(String numberOfWagon) {
        return mapOfDaysOfWagon.get(numberOfWagon);
    }
}
