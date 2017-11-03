import java.util.*;

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

        System.out.println(tempMapOfRoutes);
        System.out.println(tempMapOfWagons);

        for (int a = 0; a < countRoutes; a++) {

            // Очищаем массивы
            listOfRoutes.clear();
            tempStationDistrict.clear();
            mapDistance.clear();

            // Заполняем массив маршрутами
            for (HashMap.Entry<Integer, List<Object>> tempMapOfRoute : tempMapOfRoutes.entrySet()) {
                listOfRoutes.add(tempMapOfRoute.getValue());
            }

            //System.out.println(listOfRoutes);

            // Получаем список станций без задвоений(District), чтобы снизить нагрузку на базу
            for (int j = 0; j < listOfRoutes.size(); j++) {
                String[] startStation = listOfRoutes.get(j).toString().split(", ");
                tempStationDistrict.add(startStation[1].trim());
            }

            // По каждому вагону высчитываем расстояние до каждой начальной станнции маршрутов
            for (int i = 0; i < countWagons; i++) {
                String[] valueOfTempMapOfWagons = tempMapOfWagons.get(i).split(", ");

                // Поулчаем номер вагона
                String numberOfWagon = valueOfTempMapOfWagons[0].trim();

                // Получаем станцию назначения вагона
                String stationNameEnd = valueOfTempMapOfWagons[1].trim();

                // Цикл расчета расстояния и заполнения мапы
                for (String stationNameStart : tempStationDistrict) {
                    List<String> stationAndDistance = new ArrayList<>();
                    stationAndDistance.add(numberOfWagon);
                    stationAndDistance.add(stationNameStart);
                    mapDistance.put(stationAndDistance, getDistanceBetweenStations.getDistanceBetweenStations(stationNameEnd, stationNameStart));
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
            HashMap<Integer, String> mapNewOfWagons = new HashMap<>();
            List<Object> listOfRoutesForDelete = new ArrayList();
            HashMap<String, Object> finalTempMap = new HashMap<>();

            System.out.println(mapDistanceSort);

            for (int i = 0; i < countWagons; i++) {

                Map.Entry<Integer, List<String>> mapDistanceSortFirstElement = mapDistanceSort.entrySet().iterator().next();
                List<String> listRouteMinDistance = mapDistanceSortFirstElement.getValue();
                String[] parserRouteMinDistance = listRouteMinDistance.get(0).split(", ");
                String numberOfWagon = parserRouteMinDistance[0].replace("[", "").trim();
                String stationStartOfWagon = parserRouteMinDistance[1].replace("]", "").trim();

                for (HashMap.Entry<Integer, List<Object>> tempMapOfRouteForDelete : mapOfRoutesForDelete.entrySet()) {
                    listOfRoutesForDelete.add(tempMapOfRouteForDelete.getValue());
                }

                int countFoundStations = 0;
                StringBuilder stringBuilder = new StringBuilder();
                for (Iterator<HashMap.Entry<Integer, List<Object>>> it = mapOfRoutesForDelete.entrySet().iterator(); it.hasNext(); ) {
                    HashMap.Entry<Integer, List<Object>> entry = it.next();
                    for (int j = 0; j < listOfRoutesForDelete.size(); j++) {
                        String[] startStation = listOfRoutesForDelete.get(j).toString().split(", ");
                        if (startStation[1].trim().equals(stationStartOfWagon)) {
                            if (entry.getValue().equals(listOfRoutesForDelete.get(j))) {
                                countFoundStations++;
                                it.remove();
                                finalTempMap.put(numberOfWagon, listOfRoutesForDelete.get(j));
                                stringBuilder.append(numberOfWagon);
                                stringBuilder.append(", ");
                                stringBuilder.append(startStation[3].replace("]", "").trim());
                                mapNewOfWagons.put(i, stringBuilder.toString());
                            }
                            break;
                        }
                    }
                }

                tempMapOfWagons = mapNewOfWagons;
                tempMapOfRoutes = mapOfRoutesForDelete;

                listOfRoutesForDelete.clear();

                for (Iterator<Map.Entry<Integer, List<String>>> it = mapDistanceSort.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<Integer, List<String>> entry = it.next();

                    for (String listStation : tempStationDistrict) {
                        if (!(countFoundStations == 0)) {
                            if (entry.getValue().contains("[" + numberOfWagon + ", " + listStation + "]")) {
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
                }

                //System.out.println(mapDistanceSort);
                System.out.println("Вагон номер " + numberOfWagon + " едет на станцию " + stationStartOfWagon + ": " + listRouteMinDistance.get(1) + " км.");
            }

            System.out.println("----------------------------------------");
            System.out.println(a + " finalTempMap: " + finalTempMap);
            System.out.println("----------------------------------------");
        }
    }

    private static class CompareMapValue implements Comparable {
        public List<String> list;
        public Integer i;

        private CompareMapValue(List<String> list, Integer i) {
            this.list = list;
            this.i = i;
        }

        public int compareTo(Object o) {
            if (o instanceof CompareMapValue) {
                final int diff = i.intValue() - ((CompareMapValue) o).i.intValue();
                return diff < 0 ? -1 : (diff > 0 ? 1 : 0);
            } else {
                return 0;
            }
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
