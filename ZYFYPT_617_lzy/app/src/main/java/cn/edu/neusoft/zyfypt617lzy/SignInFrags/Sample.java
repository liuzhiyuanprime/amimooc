package cn.edu.neusoft.zyfypt617lzy.SignInFrags;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import cn.edu.neusoft.zyfypt617lzy.R;
import cn.edu.neusoft.zyfypt617lzy.bean.ArticleBean;
import cn.edu.neusoft.zyfypt617lzy.retrofit.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class Sample extends Fragment {
        private String sessionid;
    public Sample() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public Sample(String sessionid){
        this.sessionid = sessionid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample, container, false);
    }

}
