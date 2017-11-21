package com.uraltranscom.service.additional;

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
    public List<String> list;
    public Integer i;

    public CompareMapValue(List<String> list, Integer i) {
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

    public static Map sortMap(Map<Integer, List<String>> mapDistanceSort) {
        Map<Integer, List<String>> sortedMap = new LinkedHashMap<>(mapDistanceSort.size());

        mapDistanceSort.forEach((k, v) -> {
            String[] o3 = v.get(0).split(", ");
            if ("1".equals(o3[3].replace("]", "").trim())) sortedMap.put(k, v);
        });

        mapDistanceSort.forEach((k, v) -> {
            String[] o3 = v.get(0).split(", ");
            if ("0".equals(o3[3].replace("]", "").trim())) sortedMap.put(k, v);
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
