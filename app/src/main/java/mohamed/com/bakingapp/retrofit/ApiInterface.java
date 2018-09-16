package mohamed.com.bakingapp.retrofit;

import java.util.List;

import mohamed.com.bakingapp.model.BakedResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<BakedResponse>> getBakingResponse();
}
