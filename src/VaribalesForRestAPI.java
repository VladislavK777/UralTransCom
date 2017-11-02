import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VaribalesForRestAPI {

    public Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://poisk-vagonov.ru")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public SomeRestApi api = retrofit.create(SomeRestApi.class);

}
