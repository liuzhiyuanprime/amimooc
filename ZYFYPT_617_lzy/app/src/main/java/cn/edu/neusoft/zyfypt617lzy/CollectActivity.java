package cn.edu.neusoft.zyfypt617lzy;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.neusoft.zyfypt617lzy.SignInFrags.CArticleAdapter;
import cn.edu.neusoft.zyfypt617lzy.bean.ArticleBean;
import cn.edu.neusoft.zyfypt617lzy.bean.CArticleBean;
import cn.edu.neusoft.zyfypt617lzy.retrofit.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CollectActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CArticleAdapter adapter;//适配器
    private List<CArticleBean<ArticleBean>> list=null;//数据源
    private String mod;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        init();
    }
    private void init() {
       mod = getIntent().getStringExtra("mod");
        recyclerView=findViewById(R.id.collect_RecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        //创建Adaper
        Retrofit  retrofit=new Retrofit.Builder().baseUrl("http://amicool.neusoft.edu.cn/").build();
        RetrofitService service
                =retrofit.create(RetrofitService.class);
        Call<ResponseBody> call = service.getCArticleList(mod,"1",getSharedPreferences("zyfypt",MODE_PRIVATE).getString("sessionid",""));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    byte[] bytes = response.body().bytes();
                    System.out.println(response.body().string());
                    String text = new String(bytes);
                    ObjectMapper objectMapper = new ObjectMapper();
                    List<CArticleBean<ArticleBean>> articleList = objectMapper.readValue(text, new TypeReference<List<CArticleBean<ArticleBean>>>(){} );
                    adapter = new CArticleAdapter(CollectActivity.this,articleList,mod);
                    recyclerView.setAdapter(adapter);
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
