import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface SomeRestApi {
    @GET("/rail_proxy.php")
    Call<List<SomeResponce>> execSomeMethod(@Query("page") String page,
                                            @Query("s") String s);

    @GET("/engine.php")
    Call<ResponseBody> execSomeMethod2(@Query("q") String q,
                                       @Query("action") String action);
}
