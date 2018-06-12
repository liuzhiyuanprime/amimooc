package cn.edu.neusoft.zyfypt617lzy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import cn.edu.neusoft.zyfypt617lzy.bean.ArticleBean;
import cn.edu.neusoft.zyfypt617lzy.bean.UserInfo;
import cn.edu.neusoft.zyfypt617lzy.retrofit.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ArticleActivity extends AppCompatActivity {
    private WebView article_webview ;
    private Menu menu;
    private String article_id,sessionid,mod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        init();
    }
    private void init(){
        sessionid = getSharedPreferences("zyfypt",Context.MODE_PRIVATE).getString("sessionid","");
        article_webview = findViewById(R.id.article_webview);
        Intent intent = getIntent();
         article_id = intent.getStringExtra("article_id");
         mod = intent.getStringExtra("mod");
        article_webview.loadUrl("http://amicool.neusoft.edu.cn/"+mod+".php/show/index/id/"+article_id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_collect,menu);
        this.menu = menu;
        getCollectState();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_collect :
                if(item.getTitle().toString().equals("收藏")){
                    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://amicool.neusoft.edu.cn/").build();
                    RetrofitService service = retrofit.create(RetrofitService.class);
                    Call<ResponseBody> call = service.collect(mod,article_id,sessionid);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            try {
                                byte[] data =  response.body().bytes();
                                String text = new String(data);
                                if(text.equals("0"))
                                    Toast.makeText(ArticleActivity.this, "收藏失败", Toast.LENGTH_SHORT).show();
                                else if(text.contentEquals("error")) {
                                    Toast.makeText(ArticleActivity.this, "error", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(ArticleActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                                    menu.findItem(R.id.item_collect).setTitle("取消收藏");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(ArticleActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://amicool.neusoft.edu.cn/").build();
                    RetrofitService service = retrofit.create(RetrofitService.class);
                    Call<ResponseBody> call = service.uncollect(mod,article_id,sessionid);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            try {
                                byte[] data =  response.body().bytes();
                                String text = new String(data);
                                if(text.equals("0"))
                                    Toast.makeText(ArticleActivity.this, "失败", Toast.LENGTH_SHORT).show();
                                else if(text.contentEquals("error")) {
                                    Toast.makeText(ArticleActivity.this, "error", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(ArticleActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                                    menu.findItem(R.id.item_collect).setTitle("收藏");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(ArticleActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void getCollectState(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://amicool.neusoft.edu.cn/").build();
        RetrofitService service = retrofit.create(RetrofitService.class);
        Call<ResponseBody> call = service.exists(mod,article_id,sessionid);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try { byte[] data = response.body().bytes();
                    String text = new String(data);
                    if(text.equals("1"))
                        menu.findItem(R.id.item_collect).setTitle("收藏");
                    else
                        menu.findItem(R.id.item_collect).setTitle("取消收藏");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ArticleActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
