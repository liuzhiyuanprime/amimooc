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

/**
 * A simple {@link Fragment} subclass.
 */
public class Video extends Fragment {
    private RecyclerView recyclerView;
    private ArticleListAdapter adapter;
    private String sessionid;

    public Video() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public Video(String sessionid){
        this.sessionid = sessionid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView_video);
        adapter = new ArticleListAdapter();

        if (getActivity() != null) {

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

            Retrofit retrofit = new Retrofit.Builder().baseUrl("http://amicool.neusoft.edu.cn/").build();
            RetrofitService service = retrofit.create(RetrofitService.class);
            Call<ResponseBody> call = service.getArticleList( "video", "1",sessionid);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        byte[] data = response.body().bytes();
                        String text = new String(data);
                        ObjectMapper objectMapper = new ObjectMapper();
                        List<ArticleBean> articleList = objectMapper.readValue(text, new TypeReference<List<ArticleBean>>(){} );
                        adapter.setArticleList(articleList,"video");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        }
    }

}
