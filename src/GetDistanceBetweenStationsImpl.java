import retrofit2.Call;

import java.io.IOException;
import java.util.List;

public class GetDistanceBetweenStationsImpl extends VaribalesForRestAPI implements GetDistanceBetweenStations  {

    GetNumberOfStationImpl getNumberOfStationImpl = new GetNumberOfStationImpl();

    @Override
    public String getDistanceBetweenStations(String nameOfStation1, String nameOfStation2) {
        String route = getNumberOfStationImpl.getStringQueryOfRoute(nameOfStation1, nameOfStation2);
        //System.out.println(route + " " + nameOfStation1 + " " + nameOfStation2);
        String distance = null;
        try {
            Call<List<SomeResponce>> result = api.execSomeMethod("froute.php", route);
            distance = result.execute().body().get(0).routes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return distance;
    }



}
