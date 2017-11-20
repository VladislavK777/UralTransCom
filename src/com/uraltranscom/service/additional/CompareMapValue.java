package com.uraltranscom.service.additional;

import com.uraltranscom.model.Route;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
*
* Класс сортировки
*
* @author Vladislav Klochkov
* @version 1.0
* @create 06.11.2017
*
*/

public class CompareMapValue implements Comparable {
    public List<Object> wagon;
    public Integer distance;
    public static Route r;

    public CompareMapValue(List<Object> wagon, Integer distance) {
        this.wagon = wagon;
        this.distance = distance;
    }

    public int compareTo(Object o) {
        if (o instanceof CompareMapValue) {
            final int diff = distance.intValue() - ((CompareMapValue) o).distance.intValue();
            return diff < 0 ? -1 : (diff > 0 ? 1 : 0);
        } else {
            return 0;
        }
    }

    public static Map sortMap(Map<List<Object>, Integer> mapDistanceSort) {
        Map<List<Object>, Integer> sortedMap = new LinkedHashMap<>(mapDistanceSort.size());

        mapDistanceSort.forEach((k, v) -> {
            r = (Route) k.get(1);
            if ("1".equals(r.getVIP())) sortedMap.put(k, v);
        });

        mapDistanceSort.forEach((k, v) -> {
            r = (Route) k.get(1);
            if ("0".equals(r.getVIP())) sortedMap.put(k, v);
        });

        return sortedMap;
    }

    /*Map<List<String>, Integer> sortedMap = mapDistance.entrySet().stream()
                    .sorted(
                            Comparator.<Map.Entry<List<String>, Integer>, String>
                                    comparing(e -> e.getKey().get(2)).reversed()
                                    .thenComparingInt(Map.Entry::getValue)
                    ).collect(
                            Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
                    );

            mapDistance.entrySet().stream()
                    .sorted(
                            Comparator.<Map.Entry<List<String>, Integer>, String>
                                    comparing(e -> e.getKey().get(2)).reversed()
                                    .thenComparingInt(Map.Entry::getValue)
                    ).forEach(System.out::println);
*/
}
