package cn.edu.neusoft.zyfypt617lzy.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RetrofitService {
    //注册
@GET("api.php/reg")
    Call<ResponseBody> signIn(@Query("username") String username,
                              @Query("password") String password,
                              @Query("tel") String tel,
                              @Query("email") String email,
                              @Query("roleid") String roleid
);
    @GET("api.php/login")
    Call<ResponseBody> login(@Query("username") String username,
                             @Query("password") String password);

    @GET("api.php/logout")
    Call<ResponseBody> logout(@Header("SessionID") String sessionID);

    @GET("api.php/lists")
    Call<ResponseBody> getArticleList(@Query("mod")String mod,
                                      @Query("page") String page,
                                      @Header("SessionID") String sessionId);

    @GET("api.php/listspecial")
    Call<ResponseBody> getspecial(@Path("mod")String mod,
                                      @Query("page") String page,
                                      @Header("SessionID") String sessionID);


    @GET("api.php/exists/mod/collect{mod}")
    Call<ResponseBody> exists(@Path("mod") String mod,
                              @Query("resid") String resid,
                              @Header("SessionID") String sessionID);

    @GET("api.php/create/mod/collect{mod}")
    Call<ResponseBody> collect(@Path("mod")String mod,
                         @Query("resid") String resid,
                         @Header("SessionID") String sessionID);

    @GET("api.php/delete/mod/collect{mod}")
    Call<ResponseBody> uncollect(@Path("mod")String mod,
                                 @Query("resid") String resid,
                                 @Header("SessionID") String sessionID);

    @GET("api.php/listmycollect/mod/collect{mod}")
    Call<ResponseBody> getCArticleList(@Path("mod")String mod,
                                       @Query("page") String page,
                                       @Query("SessionID") String sessionID);

    @GET
    Call<ResponseBody> getPdf(@Url String url);

}
