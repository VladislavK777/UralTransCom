import java.util.*;

/*
*
* Основной класс алгоритма расчета
*
* @author Vladislav Klochkov
* @version 1.0
* @create 01.11.2017
*
* 06.11.2017 - Переработана логина с использованием названия ЖД
* 07.11.2017 - Переработан цикл для формирования маршрута некратного значения маршруты/вагоны
*
*/

public class MethodOfLogicBasic {

    private GetBasicListOfRoutes getBasicListOfRoutes = new GetBasicListOfRoutes();
    private GetDistanceBetweenStationsImpl getDistanceBetweenStations = new GetDistanceBetweenStationsImpl();
    private GetListOfWagons getListOfWagons = new GetListOfWagons();

    private HashMap<Integer, List<Object>> tempMapOfRoutes = new HashMap<>();
    private HashMap<Integer, String> tempMapOfWagons = new HashMap<>();

    private List<Object> listOfRoutes = new ArrayList();
    private HashSet<String> tempStationDistrict = new HashSet();

    private final int LOADING_OF_WAGON = 7;
    private final int UNLOADING_OF_WAGON = 4;

    public void lookingForOptimalMapOfRoute() {

        tempMapOfRoutes = getBasicListOfRoutes.getMapOfRoutes();
        tempMapOfWagons = getListOfWagons.getMapOfWagons();

        // int countRoutes = tempMapOfRoutes.size();
        // int countCircle = countRoutes / countWagons;
        HashMap<List<String>, String> mapDistance = new HashMap<>();

        int a = 0; // временные метки

        while (!tempMapOfRoutes.isEmpty() && !tempMapOfWagons.isEmpty()) {

            int countWagons = tempMapOfWagons.size();
            //System.out.println(countWagons);

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

            /*
            * По каждому вагону высчитываем расстояние до каждой начальной станнции маршрутов
            * Перенести в отдельный класс.
            * Переработать метод, чтобы не обращаться в БД повторно с одини и теме же значениями
            */
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

            HashMap<Integer, List<Object>> mapOfRoutesForDelete = tempMapOfRoutes;
            HashMap<Integer, String> mapNewOfWagons = new HashMap<>();
            List<Object> listOfRoutesForDelete = new ArrayList();
            HashMap<String, Object> finalTempMap = new HashMap<>();

            //System.out.println(mapDistanceSort);

            // for (int i = 0; i < countWagons; i++) {

            if (!mapDistanceSort.isEmpty() && !tempMapOfWagons.isEmpty()) {
                Map.Entry<Integer, List<String>> mapDistanceSortFirstElement = mapDistanceSort.entrySet().iterator().next();
                List<String> listRouteMinDistance = mapDistanceSortFirstElement.getValue();
                String[] parserRouteMinDistance = listRouteMinDistance.get(0).split(", ");
                String numberOfWagon = parserRouteMinDistance[0].replace("[", "").trim();
                String stationStartOfWagon = parserRouteMinDistance[1].replace("]", "").trim();

                for (HashMap.Entry<Integer, List<Object>> tempMapOfRouteForDelete : mapOfRoutesForDelete.entrySet()) {
                    listOfRoutesForDelete.add(tempMapOfRouteForDelete.getValue());
                }

                //int countFoundStations = 0;

                StringBuilder stringBuilder = new StringBuilder();
                for (Iterator<HashMap.Entry<Integer, List<Object>>> it = mapOfRoutesForDelete.entrySet().iterator(); it.hasNext(); ) {
                    HashMap.Entry<Integer, List<Object>> entry = it.next();
                    for (int j = 0; j < listOfRoutesForDelete.size(); j++) {
                        String[] startStation = listOfRoutesForDelete.get(j).toString().split(", ");
                        if (startStation[1].trim().equals(stationStartOfWagon)) {
                            if (entry.getValue().equals(listOfRoutesForDelete.get(j))) {
                                //countFoundStations++;

                                fullDays(numberOfWagon, listRouteMinDistance.get(1), startStation[4].replace("]", "").trim());
                                System.out.println(numberOfWagon + " " + getNumberOfDaysOfWagon(numberOfWagon));

                                int getKeyNumber = 0;
                                Set<Map.Entry<Integer, String>> entrySet = tempMapOfWagons.entrySet();
                                for (Map.Entry<Integer, String> pair : entrySet) {
                                    if (pair.getValue().contains(numberOfWagon)) {
                                        getKeyNumber = pair.getKey();
                                    }
                                }
                                if (getNumberOfDaysOfWagon(numberOfWagon) < 31) {
                                    it.remove();
                                    finalTempMap.put(numberOfWagon, listOfRoutesForDelete.get(j));
                                    stringBuilder.append(numberOfWagon);
                                    stringBuilder.append(", ");
                                    stringBuilder.append(startStation[2].trim());
                                    stringBuilder.append(", ");
                                    stringBuilder.append(startStation[3].replace("]", "").trim());


                                    //System.out.println(getKeyNumber);
                                    tempMapOfWagons.replace(getKeyNumber, stringBuilder.toString());
                                    a++;
                                    //System.out.println("----------------------------------------");
                                    System.out.println(a + " finalTempMap: " + finalTempMap);
                                    //System.out.println(tempMapOfWagons);
                                } else {
                                    System.out.println("Delete the wagon: " + tempMapOfWagons.get(getKeyNumber));
                                    //finalTempMap.put(numberOfWagon, listOfRoutesForDelete.get(j));
                                    tempMapOfWagons.remove(getKeyNumber);
                                    int i = 0;
                                    for (HashMap.Entry<Integer, String> m : tempMapOfWagons.entrySet()) {
                                        mapNewOfWagons.put(i, m.getValue());
                                        i++;
                                    }
                                    tempMapOfWagons = mapNewOfWagons;
                                }
                                //mapNewOfWagons.put(i, stringBuilder.toString());
                            }
                            break;
                        }
                    }
                }
                //System.out.println(a + ": " + tempMapOfWagons);
                tempMapOfRoutes = mapOfRoutesForDelete;


                listOfRoutesForDelete.clear();

                    /*for (Iterator<Map.Entry<Integer, List<String>>> it = mapDistanceSort.entrySet().iterator(); it.hasNext(); ) {
                        Map.Entry<Integer, List<String>> entry = it.next();

                        for (String listStation : tempStationDistrict) {
                            String[] stationOfList = listStation.split(", ");
                            if (!(countFoundStations == 0)) {
                                if (entry.getValue().contains("[" + numberOfWagon + ", " + stationOfList[0].trim() + "]")) {
                                    it.remove();
                                }
                            } else {
                                if (entry.getValue().contains(listRouteMinDistance.get(0))) {
                                    it.remove();
                                    i--;
                                    break;
                                }
                            }
                        }
                    }*/

                // временные метки
                System.out.println("Вагон номер " + numberOfWagon + " едет на станцию " + stationStartOfWagon + ": " + listRouteMinDistance.get(1) + " км.");
                System.out.println("-------------------------------------------------");
            }
            //}

            // временные метки

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


    HashMap<String, Double> mapOfDaysOfWagon = new HashMap<>();

    private void fullDays(String numberOfWagon, String distanceOfEmpty, String distanceOfRoute) {
        double fullMonthCircle = 0;
        if (mapOfDaysOfWagon.get(numberOfWagon) == null) {
            fullMonthCircle += Double.valueOf(distanceOfEmpty) / 300 + 1;
            fullMonthCircle += LOADING_OF_WAGON;
            fullMonthCircle += Double.valueOf(distanceOfRoute) / 300 + 1;
            fullMonthCircle += UNLOADING_OF_WAGON;
            mapOfDaysOfWagon.put(numberOfWagon, fullMonthCircle);
        } else {
            for (Map.Entry<String, Double> map : mapOfDaysOfWagon.entrySet()) {
                if (map.getKey().equals(numberOfWagon)) {
                    double tempDays = map.getValue();
                    tempDays += Double.valueOf(distanceOfEmpty) / 300 + 1;
                    tempDays += LOADING_OF_WAGON;
                    tempDays += Double.valueOf(distanceOfRoute) / 300 + 1;
                    tempDays += UNLOADING_OF_WAGON;
                    mapOfDaysOfWagon.replace(map.getKey(), tempDays);
                }
            }
        }
        //System.out.println(mapOfDaysOfWagon);
    }

    private double getNumberOfDaysOfWagon(String numberOfWagon) {
        return mapOfDaysOfWagon.get(numberOfWagon);
    }

}
