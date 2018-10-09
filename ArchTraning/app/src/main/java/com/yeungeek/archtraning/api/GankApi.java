package com.yeungeek.archtraning.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import static com.yeungeek.archtraning.api.ServiceGenerator.GANK_URL;

/**
 * @author yangjian
 * @date 2018/10/09
 */

public interface GankApi {
    @GET(GANK_URL + "data/福利/{number}/{page}")
    Call<BaseResponse<List<GankData>>> getFuli(@Path("number") int number, @Path("page") int page);
}
