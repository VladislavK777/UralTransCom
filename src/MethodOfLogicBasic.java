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
*
*/

public class MethodOfLogicBasic {

    private GetBasicListOfRoutes getBasicListOfRoutes = new GetBasicListOfRoutes();
    private GetDistanceBetweenStationsImpl getDistanceBetweenStations = new GetDistanceBetweenStationsImpl();
    private GetListOfWagons getListOfWagons = new GetListOfWagons();
    private GetFullMonthCircleOfWagon getFullMonthCircleOfWagon = new GetFullMonthCircleOfWagon();

    private HashMap<Integer, List<Object>> tempMapOfRoutes = new HashMap<>();
    private HashMap<Integer, String> tempMapOfWagons = new HashMap<>();

    private List<Object> listOfRoutes = new ArrayList();
    private HashSet<String> tempStationDistrict = new HashSet();

    public void lookingForOptimalMapOfRoute() {

        tempMapOfRoutes = getBasicListOfRoutes.getMapOfRoutes();
        tempMapOfWagons = getListOfWagons.getMapOfWagons();

        HashMap<List<String>, String> mapDistance = new HashMap<>();

        int a = 0; // временные метки

        while (!tempMapOfRoutes.isEmpty() && !tempMapOfWagons.isEmpty()) {
            int countWagons = tempMapOfWagons.size();

            /*
            * По каждому вагону высчитываем расстояние до каждой начальной станнции маршрутов
            * Перенести в отдельный класс.
            * Переработать метод, чтобы не обращаться в БД повторно с одини и теме же значениями
            */

            // Очищаем массивы
            listOfRoutes.clear();
            tempStationDistrict.clear();
            mapDistance.clear();

            // Заполняем массив маршрутами
            for (HashMap.Entry<Integer, List<Object>> tempMapOfRoute : tempMapOfRoutes.entrySet()) {
                listOfRoutes.add(tempMapOfRoute.getValue());
            }

            // Получаем список станций без задвоений(District), чтобы снизить нагрузку на базу
            for (int j = 0; j < listOfRoutes.size(); j++) {
                String[] startStation = listOfRoutes.get(j).toString().split(", ");
                tempStationDistrict.add(startStation[1].trim() + ", " + startStation[0].trim());
            }

            for (int i = 0; i < countWagons; i++) {
                String[] valueOfTempMapOfWagons = tempMapOfWagons.get(i).split(", ");

                // Поулчаем номер вагона
                String numberOfWagon = valueOfTempMapOfWagons[0].trim();

                // Получаем ЖД назначения вагона
                String roadOfWagonEnd = valueOfTempMapOfWagons[1].trim();

                // Получаем станцию назначения вагона
                String stationNameEnd = valueOfTempMapOfWagons[2].trim();

                // Цикл расчета расстояния и заполнения мапы
                for (String stationNameStartArray : tempStationDistrict) {
                    List<String> stationAndDistance = new ArrayList<>();
                    String[] roadAndNameOfStation = stationNameStartArray.split(", ");
                    String stationNameStart = roadAndNameOfStation[0].trim();
                    String roadOfStationStart = roadAndNameOfStation[1].trim().replace("[", "");
                    stationAndDistance.add(numberOfWagon);
                    stationAndDistance.add(stationNameStart);
                    mapDistance.put(stationAndDistance, getDistanceBetweenStations.getDistanceBetweenStations(stationNameEnd, roadOfWagonEnd, stationNameStart, roadOfStationStart));
                }
            }

            Map<Integer, List<String>> mapDistanceSort = new LinkedHashMap<>();

            // Поиск меньшего значения в мапе
            int index = mapDistance.size();
            CompareMapValue[] compareMapValues = new CompareMapValue[index];
            index = 0;
            for (HashMap.Entry<List<String>, String> entry : mapDistance.entrySet()) {
                compareMapValues[index++] = new CompareMapValue(entry.getKey(), Integer.parseInt(entry.getValue()));
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

            // Мапа для удаления использованных маршрутов
            HashMap<Integer, List<Object>> mapOfRoutesForDelete = tempMapOfRoutes;

            // Мапа для формирования измененного списка вагонов
            HashMap<Integer, String> mapNewOfWagons = new HashMap<>();

            // Массив маршрутов для удаления
            List<Object> listOfRoutesForDelete = new ArrayList();

            // Мапа для добавления успешных рейсов
            HashMap<String, Object> finalTempMap = new HashMap<>();

            // Цикл формирования ресой
            // Проверяем на пустоту мап, либо вагоны, либо рейсы
            if (!mapDistanceSort.isEmpty() && !tempMapOfWagons.isEmpty()) {
                Map.Entry<Integer, List<String>> mapDistanceSortFirstElement = mapDistanceSort.entrySet().iterator().next();
                List<String> listRouteMinDistance = mapDistanceSortFirstElement.getValue();
                String[] parserRouteMinDistance = listRouteMinDistance.get(0).split(", ");
                String numberOfWagon = parserRouteMinDistance[0].replace("[", "").trim();
                String stationStartOfWagon = parserRouteMinDistance[1].replace("]", "").trim();

                for (HashMap.Entry<Integer, List<Object>> tempMapOfRouteForDelete : mapOfRoutesForDelete.entrySet()) {
                    listOfRoutesForDelete.add(tempMapOfRouteForDelete.getValue());
                }

                StringBuilder stringBuilder = new StringBuilder();
                for (Iterator<HashMap.Entry<Integer, List<Object>>> it = mapOfRoutesForDelete.entrySet().iterator(); it.hasNext(); ) {
                    HashMap.Entry<Integer, List<Object>> entry = it.next();
                    for (int j = 0; j < listOfRoutesForDelete.size(); j++) {
                        String[] startStation = listOfRoutesForDelete.get(j).toString().split(", ");
                        if (startStation[1].trim().equals(stationStartOfWagon)) {
                            if (entry.getValue().equals(listOfRoutesForDelete.get(j))) {

                                // Расчет дней затраченных одним вагоном на один цикл
                                getFullMonthCircleOfWagon.fullDays(numberOfWagon, listRouteMinDistance.get(1), startStation[4].replace("]", "").trim());
                                int getKeyNumber = 0;
                                Set<Map.Entry<Integer, String>> entrySet = tempMapOfWagons.entrySet();
                                for (Map.Entry<Integer, String> pair : entrySet) {
                                    if (pair.getValue().contains(numberOfWagon)) {
                                        getKeyNumber = pair.getKey();
                                    }
                                }

                                double numberOfDaysOfWagon = getFullMonthCircleOfWagon.getNumberOfDaysOfWagon(numberOfWagon);

                                // Если больше 30 дней, то исключаем вагон, лимит 30 дней
                                if (numberOfDaysOfWagon < 31) {
                                    it.remove();
                                    finalTempMap.put(numberOfWagon, listOfRoutesForDelete.get(j));
                                    stringBuilder.append(numberOfWagon);
                                    stringBuilder.append(", ");
                                    stringBuilder.append(startStation[2].trim());
                                    stringBuilder.append(", ");
                                    stringBuilder.append(startStation[3].replace("]", "").trim());
                                    tempMapOfWagons.replace(getKeyNumber, stringBuilder.toString());

                                    a++;
                                    System.out.println("Вагон номер " + numberOfWagon + " едет на станцию " + stationStartOfWagon + ": " + listRouteMinDistance.get(1) + " км.");
                                    System.out.println("Рейс: " + finalTempMap);
                                    System.out.println("Общее время в пути: " + numberOfDaysOfWagon);
                                    System.out.println("-------------------------------------------------");

                                } else {

                                    System.out.println("Вагон номер " + numberOfWagon + " должен был ехать на " + stationStartOfWagon + ": " + listRouteMinDistance.get(1) + " км.");
                                    System.out.println("Далее по маршруту: " + listOfRoutesForDelete.get(j));
                                    System.out.println("-------------------------------------------------");

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
        System.out.println(tempMapOfRoutes);
    }


    public HashMap<Integer, List<Object>> getTempMapOfRoutes() {
        return tempMapOfRoutes;
    }

    public void setTempMapOfRoutes(HashMap<Integer, List<Object>> tempMapOfRoutes) {
        this.tempMapOfRoutes = tempMapOfRoutes;
    }

    public HashMap<Integer, String> getTempMapOfWagons() {
        return tempMapOfWagons;
    }

    public void setTempMapOfWagons(HashMap<Integer, String> tempMapOfWagons) {
        this.tempMapOfWagons = tempMapOfWagons;
    }
}
