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

    public void lookingForOptimalMapOfRoute() {

        tempMapOfRoutes = getBasicListOfRoutes.getMapOfRoutes();
        tempMapOfWagons = getListOfWagons.getMapOfWagons();
        int countWagons = tempMapOfWagons.size();
        int countRoutes = tempMapOfRoutes.size();
        int countCircle = countRoutes / countWagons;
        HashMap<List<String>, String> mapDistance = new HashMap<>();

        int a = 0; // временные метки
        while (!tempMapOfRoutes.isEmpty()) {

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

            // По каждому вагону высчитываем расстояние до каждой начальной станнции маршрутов
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

            // Поиск меньшего значения в мапе(переделать в метод, организовать отдельный класс)
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
            //HashMap<Integer, String> mapNewOfWagons = new HashMap<>();
            List<Object> listOfRoutesForDelete = new ArrayList();
            HashMap<String, Object> finalTempMap = new HashMap<>();
            //System.out.println(mapDistanceSort);

            // for (int i = 0; i < countWagons; i++) {

            if (!mapDistanceSort.isEmpty()) {
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
                                it.remove();
                                finalTempMap.put(numberOfWagon, listOfRoutesForDelete.get(j));
                                stringBuilder.append(numberOfWagon);
                                stringBuilder.append(", ");
                                stringBuilder.append(startStation[2].trim());
                                stringBuilder.append(", ");
                                stringBuilder.append(startStation[3].replace("]", "").trim());

                                int getKeyNumber = 0;
                                Set<Map.Entry<Integer, String>> entrySet = tempMapOfWagons.entrySet();
                                for (Map.Entry<Integer, String> pair : entrySet) {
                                    if (pair.getValue().contains(numberOfWagon)) {
                                        getKeyNumber = pair.getKey();
                                    }
                                }
                                //System.out.println(getKeyNumber);
                                tempMapOfWagons.replace(getKeyNumber, stringBuilder.toString());
                                //System.out.println(tempMapOfWagons);
                                //mapNewOfWagons.put(i, stringBuilder.toString());
                            }
                            break;
                        }
                    }
                }

               // tempMapOfWagons = mapNewOfWagons;
               // System.out.println(tempMapOfWagons);
                tempMapOfRoutes = mapOfRoutesForDelete;
                //System.out.println(tempMapOfRoutes);

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
            }
            //}

            // временные метки
            a++;
            //System.out.println("----------------------------------------");
            System.out.println(a + " finalTempMap: " + finalTempMap);
            System.out.println("----------------------------------------");
        }
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
