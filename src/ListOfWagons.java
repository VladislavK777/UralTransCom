import java.util.HashMap;

public class ListOfWagons {
    private HashMap<Integer, String> mapOfWagons = new HashMap<>();

    public ListOfWagons() {
        fillMapOfWagons();
    }

    public HashMap<Integer,String> getMapOfWagons() {
        return mapOfWagons;
    }

    public void setMapOfWagons(HashMap<Integer, String> mapOfWagons) {
        this.mapOfWagons = mapOfWagons;
    }
    private void fillMapOfWagons() {
        mapOfWagons.put(1,"Голицыно");
        mapOfWagons.put(2,"Казань");
        mapOfWagons.put(3,"Абакан");
        mapOfWagons.put(4,"Яничкино");

    }
}
