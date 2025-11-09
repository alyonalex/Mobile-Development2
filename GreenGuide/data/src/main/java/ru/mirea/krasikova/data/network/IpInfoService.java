package ru.mirea.krasikova.data.network;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.mirea.krasikova.data.model.IpInfoResponse;

public interface IpInfoService {
    @GET("json")
    Call<IpInfoResponse> getIpInfo();
}
