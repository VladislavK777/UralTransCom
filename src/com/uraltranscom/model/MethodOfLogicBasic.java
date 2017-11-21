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
*   @version 1.1
*   1. Переработана логина с использованием названия ЖД
* 07.11.2017
*   @version 1.2
 *  1. Переработан цикл для формирования маршрута некратного значения маршруты/вагоны
* 08.11.2017
*   @version 1.3
*   1. Добавлена логика расчета по дням, лимит 30 дней
* 16.11.2017
*   @version 1.6
*   1.Добалена сортировка по приоритеам станций
*
*/

public class MethodOfLogicBasic {

    // Подключаем логгер
    private static Logger logger = LoggerFactory.getLogger(MethodOfLogicBasic.class);

    private GetBasicListOfRoutesImpl getBasicListOfRoutesImpl = new GetBasicListOfRoutesImpl();
    private GetDistanceBetweenStationsImpl getDistanceBetweenStations = new GetDistanceBetweenStationsImpl();
    private GetListOfWagonsImpl getListOfWagonsImpl = new GetListOfWagonsImpl();
    private GetFullMonthCircleOfWagonImpl getFullMonthCircleOfWagonImpl = new GetFullMonthCircleOfWagonImpl();

    private Map<Integer, List<Object>> tempMapOfRoutes = new HashMap<>();
    private Map<Integer, String> tempMapOfWagons = new HashMap<>();

    public void lookingForOptimalMapOfRoute() {

        tempMapOfRoutes = getBasicListOfRoutesImpl.getMapOfRoutes();
        tempMapOfWagons = getListOfWagonsImpl.getMapOfWagons();

        // Список маршрутов
        List<Object> listOfRoutes = new ArrayList();

        // Список станций из списка маршрутов с удалением повторок
        Set<String> tempStationDistrict = new HashSet();

        // Список распределенных вагонов
        Set<String> listOfDistributedWagons = new HashSet<>();

        // Список нераспределенных вагонов
        Set<String> listOfUnDistributedWagons = new HashSet<>();

        // Список расстояний
        Map<List<String>, Integer> mapDistance = new HashMap<>();

        // Запускаем цикл
        while (!tempMapOfRoutes.isEmpty() && !tempMapOfWagons.isEmpty()) {
            int countWagons = tempMapOfWagons.size();

            // Очищаем массивы
            listOfRoutes.clear();
            tempStationDistrict.clear();
            mapDistance.clear();

            // Заполняем массив маршрутами
            for (HashMap.Entry<Integer, List<Object>> tempMapOfRoute : tempMapOfRoutes.entrySet()) {
                listOfRoutes.add(tempMapOfRoute.getValue());
            }

            // Получаем список станций без задвоений(District), чтобы снизить нагрузку на базу, плюс список приоритетных маршрутов
            for (int j = 0; j < listOfRoutes.size(); j++) {
                String[] startStation = listOfRoutes.get(j).toString().split(", ");
                tempStationDistrict.add(startStation[0].replace("[", "").trim() + ", " + startStation[1].trim() + ", " + startStation[5].replace("]", "").trim());
            }

            for (int i = 0; i < countWagons; i++) {
                String[] valueOfTempMapOfWagons = tempMapOfWagons.get(i).split(", ");

                // Поулчаем номер вагона
                String numberOfWagon = valueOfTempMapOfWagons[0].trim();

                // Получаем тип вагона
                String typeOfWagon = valueOfTempMapOfWagons[1].trim();

                // Получаем ЖД назначения вагона
                String roadOfWagonEnd = valueOfTempMapOfWagons[2].trim();

                // Получаем станцию назначения вагона
                String stationNameEnd = valueOfTempMapOfWagons[3].trim();

                /*
                * По каждому вагону высчитываем расстояние до каждой начальной станнции маршрутов
                */

                // Цикл расчета расстояния и заполнения мапы
                for (String stationNameStartArray : tempStationDistrict) {
                    List<String> stationAndDistance = new ArrayList<>();
                    String[] roadAndNameOfStation = stationNameStartArray.split(", ");
                    String roadOfStationStart = roadAndNameOfStation[0].trim();
                    String stationNameStart = roadAndNameOfStation[1].replace(" VIP", "").trim();
                    stationAndDistance.add(numberOfWagon);
                    stationAndDistance.add(typeOfWagon);
                    stationAndDistance.add(roadAndNameOfStation[1]);
                    stationAndDistance.add(roadAndNameOfStation[2]);
                    mapDistance.put(stationAndDistance, Integer.parseInt(getDistanceBetweenStations.getDistanceBetweenStations(stationNameEnd, roadOfWagonEnd, stationNameStart, roadOfStationStart)));
                }
            }

            Map<Integer, List<String>> mapDistanceSort = new LinkedHashMap<>();

            // Поиск меньшего значения в мапе
            int index = mapDistance.size();
            CompareMapValue[] compareMapValues = new CompareMapValue[index];
            index = 0;
            for (Map.Entry<List<String>, Integer> entry : mapDistance.entrySet()) {
                compareMapValues[index++] = new CompareMapValue(entry.getKey(), entry.getValue());
            }
            Arrays.sort(compareMapValues);
            int keyNum = 0;
            for (CompareMapValue cmv : compareMapValues) {
                List<String> list = new ArrayList<>();
                list.add(String.valueOf(cmv.list));
                list.add(String.valueOf(cmv.i));
                mapDistanceSort.put(keyNum, list);
                keyNum++;
            }

            Map<Integer, List<String>> mapDistanceSortWithPrior = CompareMapValue.sortMap(mapDistanceSort);

            // Мапа для удаления использованных маршрутов
            Map<Integer, List<Object>> mapOfRoutesForDelete = tempMapOfRoutes;

            // Мапа для формирования измененного списка вагонов
            Map<Integer, String> mapNewOfWagons = new HashMap<>();

            // Массив маршрутов для удаления
            List<Object> listOfRoutesForDelete = new ArrayList();

            // Мапа для добавления успешных рейсов
            Map<String, Object> finalMapOfRoutesWithWagons = new HashMap<>();

            // Цикл формирования рейсов
            // Проверяем на пустоту мап, либо вагоны, либо рейсы
            if (!mapDistanceSortWithPrior.isEmpty() && !tempMapOfWagons.isEmpty()) {
                Map.Entry<Integer, List<String>> mapDistanceSortFirstElement = mapDistanceSortWithPrior.entrySet().iterator().next();
                List<String> listRouteMinDistance = mapDistanceSortFirstElement.getValue();
                String[] parserRouteMinDistance = listRouteMinDistance.get(0).split(", ");
                String numberOfWagon = parserRouteMinDistance[0].replace("[", "").trim();
                String typeOfWagon = parserRouteMinDistance[1].trim();
                String stationStartOfWagon = parserRouteMinDistance[2].replace("]", "").trim();

                for (Map.Entry<Integer, List<Object>> tempMapOfRouteForDelete : mapOfRoutesForDelete.entrySet()) {
                    listOfRoutesForDelete.add(tempMapOfRouteForDelete.getValue());
                }

                StringBuilder stringBuilder = new StringBuilder();
                for (Iterator<Map.Entry<Integer, List<Object>>> it = mapOfRoutesForDelete.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<Integer, List<Object>> entry = it.next();
                    for (int j = 0; j < listOfRoutesForDelete.size(); j++) {
                        String[] startStation = listOfRoutesForDelete.get(j).toString().split(", ");
                        // Находим стацию отправления для вагона
                        if (startStation[1].trim().equals(stationStartOfWagon)) {
                            if (entry.getValue().equals(listOfRoutesForDelete.get(j))) {
                                // Расчет дней затраченных одним вагоном на один цикл
                                getFullMonthCircleOfWagonImpl.fullDays(numberOfWagon, typeOfWagon, listRouteMinDistance.get(1), startStation[4].replace("]", "").trim());
                                int getKeyNumber = 0;
                                Set<Map.Entry<Integer, String>> entrySet = tempMapOfWagons.entrySet();
                                for (Map.Entry<Integer, String> pair : entrySet) {
                                    if (pair.getValue().contains(numberOfWagon)) {
                                        getKeyNumber = pair.getKey();
                                    }
                                }

                                // Число дней пройденных вагоном
                                double numberOfDaysOfWagon = getFullMonthCircleOfWagonImpl.getNumberOfDaysOfWagon(numberOfWagon);

                                // Если больше 30 дней, то исключаем вагон, лимит 30 дней
                                if (numberOfDaysOfWagon < 31) {

                                    // Удаляем маршрут, так как он занят вагоном
                                    it.remove();

                                    // Добавляем маршрут в мапу с номером вагона
                                    finalMapOfRoutesWithWagons.put(numberOfWagon, listOfRoutesForDelete.get(j));

                                    // Пишем в файл Excel
                                    WriteToFileExcel.writeToFileExcelDistributedRoutes(numberOfWagon, listOfRoutesForDelete.get(j), listRouteMinDistance.get(1), numberOfDaysOfWagon);

                                    // Формируем строку для формирования маршрута вагону
                                    stringBuilder.append(numberOfWagon);
                                    stringBuilder.append(", ");
                                    stringBuilder.append(typeOfWagon);
                                    stringBuilder.append(", ");
                                    stringBuilder.append(startStation[2].trim());
                                    stringBuilder.append(", ");
                                    stringBuilder.append(startStation[3].replace("]", "").trim());

                                    // Заменяем маршрут вагону
                                    tempMapOfWagons.replace(getKeyNumber, stringBuilder.toString());

                                    // Добавляем новый вагон в список
                                    listOfDistributedWagons.add(numberOfWagon);

                                    logger.info("Вагон номер " + numberOfWagon + " едет на станцию " + stationStartOfWagon + ": " + listRouteMinDistance.get(1) + " км.");
                                    logger.info("Общее время в пути: " + numberOfDaysOfWagon);
                                    logger.info("Маршрут: " + listOfRoutesForDelete.get(j));
                                    logger.info("-------------------------------------------------");

                                } else {

                                    logger.info("Вагон номер " + numberOfWagon + " должен был ехать на " + stationStartOfWagon + ": " + listRouteMinDistance.get(1) + " км.");
                                    logger.info("Далее по маршруту: " + listOfRoutesForDelete.get(j));
                                    logger.info("-------------------------------------------------");

                                    if (!listOfDistributedWagons.contains(numberOfWagon)) {
                                        listOfUnDistributedWagons.add(numberOfWagon);
                                    }

                                    tempMapOfWagons.remove(getKeyNumber);
                                    int i = 0;
                                    for (HashMap.Entry<Integer, String> m : tempMapOfWagons.entrySet()) {
                                        mapNewOfWagons.put(i, m.getValue());
                                        i++;
                                    }

                                    // Обновляем мапу вагонов
                                    tempMapOfWagons = mapNewOfWagons;
                                }
                            }
                            break;
                        }
                    }
                }
                // Обновляем мапу маршрутов
                tempMapOfRoutes = mapOfRoutesForDelete;

                // Очищаем временный массив рейсов
                listOfRoutesForDelete.clear();
            }
        }
        WriteToFileExcel.writeToFileExcelUnDistributedRoutes(tempMapOfRoutes);
        WriteToFileExcel.writeToFileExcelUnDistributedWagons(listOfUnDistributedWagons);
    }

    public void setTempMapOfRoutes(Map<Integer, List<Object>> tempMapOfRoutes) {
        this.tempMapOfRoutes = tempMapOfRoutes;
    }

    public Map<Integer, String> getTempMapOfWagons() {
        return tempMapOfWagons;
    }

    public void setTempMapOfWagons(Map<Integer, String> tempMapOfWagons) {
        this.tempMapOfWagons = tempMapOfWagons;
    }
}
