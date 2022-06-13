package com.bsstandard.piece.retrofit;

import com.bsstandard.piece.dao.VersionDAO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * packageName    : com.bsstandard.piece.api
 * fileName       : RetrofitService
 * author         : piecejhm
 * date           : 2022/06/10
 * description    : API Interface
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/10        piecejhm       최초 생성
 */

public interface RetrofitService {
    @GET("member/app/version/{deviceType}")
    Call<VersionDAO> getVersion(@Path("deviceType") String deviceType);

}
