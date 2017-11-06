import java.util.List;

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
}
