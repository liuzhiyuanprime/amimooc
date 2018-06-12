package cn.edu.neusoft.zyfypt617lzy.SignInFrags;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import cn.edu.neusoft.zyfypt617lzy.CollectActivity;
import cn.edu.neusoft.zyfypt617lzy.MainActivity;
import cn.edu.neusoft.zyfypt617lzy.R;
import cn.edu.neusoft.zyfypt617lzy.SignInActivity;
import cn.edu.neusoft.zyfypt617lzy.bean.ArticleBean;
import cn.edu.neusoft.zyfypt617lzy.retrofit.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class Owner extends Fragment {

    private String sessionid;
    private Button aticle, tware, video,logout;

    public Owner() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public Owner(String sessionid) {
        this.sessionid = sessionid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_owner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        aticle = view.findViewById(R.id.owner_article);
        tware = view.findViewById(R.id.owner_tware);
        video = view.findViewById(R.id.owner_video);
        tware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),CollectActivity.class).putExtra("mod","tware"));
            }
        });
        aticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),CollectActivity.class).putExtra("mod","article"));
            }
        });

        logout = view.findViewById(R.id.owner_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl("http://amicool.neusoft.edu.cn/").build();
                RetrofitService retrofitService = retrofit.create(RetrofitService.class);
                Call<ResponseBody> call = retrofitService.logout(sessionid);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
                Toast.makeText(getActivity(), "注销成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MainActivity.class).putExtra("re","3"));
                getActivity().finish();
            }
        });
    }
}
