package com.uraltranscom.model;

import com.uraltranscom.service.additional.CompareMapValue;
import com.uraltranscom.service.impl.GetBasicListOfRoutesImpl;
import com.uraltranscom.service.impl.GetDistanceBetweenStationsImpl;
import com.uraltranscom.service.impl.GetFullMonthCircleOfWagonImpl;
import com.uraltranscom.service.impl.GetListOfWagonsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/*
*
* Основной класс алгоритма расчета
*
* @author Vladislav Klochkov
* @version 1.0
* @create 01.11.2017
*
* 06.11.2017
*   1. Переработана логина с использованием названия ЖД
* 07.11.2017
 *  1. Переработан цикл для формирования маршрута некратного значения маршруты/вагоны
* 08.11.2017
*   1. Добавлена логика расчета по дням, лимит 30 дней
* 16.11.2017
*   1.Добалена сортировка по приоритеам станций
* 17.11.2017
*   1.Изменение типов Map
*
*/

public class MethodOfLogicBasic {

    // Подключаем логгер
    private static Logger logger = LoggerFactory.getLogger(MethodOfLogicBasic.class);

    private GetBasicListOfRoutesImpl getBasicListOfRoutesImpl = new GetBasicListOfRoutesImpl();
    private GetDistanceBetweenStationsImpl getDistanceBetweenStations = new GetDistanceBetweenStationsImpl();
    private GetListOfWagonsImpl getListOfWagonsImpl = new GetListOfWagonsImpl();
    private GetFullMonthCircleOfWagonImpl getFullMonthCircleOfWagonImpl = new GetFullMonthCircleOfWagonImpl();

    private Map<Integer, Route> tempMapOfRoutes = new HashMap<>();
    private List<Wagon> tempListOfWagons = new ArrayList<>();

    public void lookingForOptimalMapOfRoute() {

        tempMapOfRoutes = getBasicListOfRoutesImpl.getMapOfRoutes();
        tempListOfWagons = getListOfWagonsImpl.getListOfWagons();

        // Список расстояний
        Map<List<Object>, Integer> mapDistance = new HashMap<>();

        // Список распределенных вагонов
        Set<String> listOfDistributedWagons = new HashSet<>();

        // Список нераспределенных вагонов
        Set<String> listOfUnDistributedWagons = new HashSet<>();

        // Запускаем цикл

        while (!tempMapOfRoutes.isEmpty() && !tempListOfWagons.isEmpty()) {
            int countWagons = tempListOfWagons.size();

            // Очищаем массивы
            mapDistance.clear();

            for (int i = 0; i < countWagons; i++) {

                // Поулчаем номер вагона
                String numberOfWagon = tempListOfWagons.get(i).getNumberOfWagon().trim();

                // Получаем ЖД назначения вагона
                String roadOfWagonDestination = tempListOfWagons.get(i).getRoadNameDestination().trim();

                // Получаем станцию назначения вагона
                String stationOfWagonDestination = tempListOfWagons.get(i).getStationNameDestination().trim();

                // По каждому вагону высчитываем расстояние до каждой начальной станнции маршрутов
                // Цикл расчета расстояния и заполнения мапы
                for (Map.Entry<Integer, Route> tempMapOfRoute : tempMapOfRoutes.entrySet()) {
                    List<Object> list = new ArrayList<>();
                    String roadOfStationDeparture = tempMapOfRoute.getValue().getRoadOfStationDeparture();
                    String nameOfStationDeparture = tempMapOfRoute.getValue().getNameOfStationDeparture();
                    list.add(numberOfWagon);
                    list.add(tempMapOfRoute.getValue());
                    mapDistance.put(list, Integer.parseInt(getDistanceBetweenStations.getDistanceBetweenStations(stationOfWagonDestination, roadOfWagonDestination, nameOfStationDeparture, roadOfStationDeparture)));
                }
            }
            // Отсортированный список расстояний
            Map<List<Object>, Integer> mapDistanceSort = new LinkedHashMap<>();

            // Поиск меньшего значения в мапе
            int index = mapDistance.size();
            CompareMapValue[] compareMapValues = new CompareMapValue[index];
            index = 0;
            for (Map.Entry<List<Object>, Integer> entry : mapDistance.entrySet()) {
                compareMapValues[index++] = new CompareMapValue(entry.getKey(), entry.getValue());
            }
            Arrays.sort(compareMapValues);
            for (CompareMapValue cmv : compareMapValues) {
                mapDistanceSort.put(cmv.wagon, cmv.distance);
            }

            Map<List<Object>, Integer> mapDistanceSortWithPrior = CompareMapValue.sortMap(mapDistanceSort);

            // Мапа для удаления использованных маршрутов
            Map<Integer, Route> mapOfRoutesForDelete = tempMapOfRoutes;

            // Цикл формирования рейсов
            // Проверяем на пустоту мап, либо вагоны, либо рейсы
            outer:
            if (!mapDistanceSortWithPrior.isEmpty() && !tempListOfWagons.isEmpty()) {
                Map.Entry<List<Object>, Integer> mapDistanceSortFirstElement = mapDistanceSortWithPrior.entrySet().iterator().next();
                List<Object> listRouteMinDistance = mapDistanceSortFirstElement.getKey();
                Route r = (Route) listRouteMinDistance.get(1);
                String numberOfWagon = (String) listRouteMinDistance.get(0);
                String stationDepartureOfWagon = r.getNameOfStationDeparture();

                final int[] o = {0};
                Map<Integer, Route> tempMapOfRouteForDelete = new HashMap<>();
                mapOfRoutesForDelete.forEach((k, v) -> {
                    tempMapOfRouteForDelete.put(o[0], v);
                    o[0]++;
                });

                for (Iterator<Map.Entry<Integer, Route>> it = mapOfRoutesForDelete.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<Integer, Route> entry = it.next();
                    for (int j = 0; j < tempMapOfRouteForDelete.size(); j++) {
                        // Находим маршрут для вагона
                        if (tempMapOfRouteForDelete.get(j).equals(r)) {
                            if (tempMapOfRouteForDelete.get(j).equals(entry.getValue())) {
                                int getKeyNumber = 0;
                                for (int i = 0; i < tempListOfWagons.size(); i++) {
                                    if (tempListOfWagons.get(i).getNumberOfWagon().equals(numberOfWagon)) {
                                        getKeyNumber = i;
                                    }
                                }
                                // Расчет дней затраченных одним вагоном на один цикл
                                getFullMonthCircleOfWagonImpl.fullDays(numberOfWagon, tempListOfWagons.get(getKeyNumber).getTypeOfWagon(), mapDistanceSortFirstElement.getValue(), r.getDistanceOfWay());

                                // Число дней пройденных вагоном
                                double numberOfDaysOfWagon = getFullMonthCircleOfWagonImpl.getNumberOfDaysOfWagon(numberOfWagon);

                                // Если больше 30 дней, то исключаем вагон, лимит 30 дней
                                if (numberOfDaysOfWagon < 31) {

                                    // Пишем в файл Excel
                                    WriteToFileExcel.writeToFileExcelDistributedRoutes(numberOfWagon, tempMapOfRouteForDelete.get(j), mapDistanceSortFirstElement.getValue(), numberOfDaysOfWagon);

                                    // Заменяем маршрут вагону
                                    tempListOfWagons.set(getKeyNumber, new Wagon(numberOfWagon, tempListOfWagons.get(getKeyNumber).getTypeOfWagon(), tempMapOfRouteForDelete.get(j).getRoadOfStationDestination(), tempMapOfRouteForDelete.get(j).getNameOfStationDestination()));

                                    // Добавляем новый вагон в список
                                    listOfDistributedWagons.add(numberOfWagon);

                                    logger.info("Вагон номер " + numberOfWagon + " едет на станцию " + stationDepartureOfWagon + ": " + mapDistanceSortFirstElement.getValue() + " км.");
                                    logger.info("Общее время в пути: " + numberOfDaysOfWagon);
                                    logger.info("Маршрут: " + tempMapOfRouteForDelete.get(j).toString());
                                    logger.info("-------------------------------------------------");

                                    // Удаляем маршрут, так как он занят вагоном
                                    it.remove();

                                    // Выходим из цикла, так как с ним больше ничего не сделать
                                    break outer;
                                } else {
                                    logger.info("Вагон номер " + numberOfWagon + " должен был ехать на " + stationDepartureOfWagon + ": " + mapDistanceSortFirstElement.getValue() + " км.");
                                    logger.info("Далее по маршруту: " + tempMapOfRouteForDelete.get(j).toString());
                                    logger.info("-------------------------------------------------");

                                    if (!listOfDistributedWagons.contains(numberOfWagon)) {
                                        listOfUnDistributedWagons.add(numberOfWagon);
                                    }

                                    // Удаляем вагон и начинаем снова перерасчет
                                    tempListOfWagons.remove(getKeyNumber);

                                    // Выходим из цикла, так как с ним больше ничего не сделать
                                    break outer;
                                }
                            }
                        }
                    }
                }

                // Обновляем мапу маршрутов
                tempMapOfRoutes = mapOfRoutesForDelete;

                // Очищаем временный массив рейсов
                tempMapOfRouteForDelete.clear();
            }
        }
        WriteToFileExcel.writeToFileExcelUnDistributedRoutes(tempMapOfRoutes);
        WriteToFileExcel.writeToFileExcelUnDistributedWagons(listOfUnDistributedWagons);
    }
}
