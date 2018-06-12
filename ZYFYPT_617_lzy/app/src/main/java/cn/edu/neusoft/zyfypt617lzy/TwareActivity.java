package cn.edu.neusoft.zyfypt617lzy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.edu.neusoft.zyfypt617lzy.retrofit.RetrofitService;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TwareActivity extends AppCompatActivity {
    private PDFView pdfView;
    private Menu menu;
    private String pdfattach,tware_id,mod;
    //设置Retrofit访问网络的超时时间
    private static final OkHttpClient client =
            new OkHttpClient.Builder().
                    connectTimeout(600, TimeUnit.SECONDS).
                    readTimeout(600, TimeUnit.SECONDS).
                    writeTimeout(600, TimeUnit.SECONDS).build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tware);
        pdfView = findViewById(R.id.pdfView);
        Intent intent = getIntent();
        pdfattach = intent.getStringExtra("pdfattach");
        tware_id = intent.getStringExtra("article_id");
        mod = intent.getStringExtra("mod");
        loadPdf();
    }

    private void loadPdf(){

        //显示本地文件（assets目录下的文件）
//        pdfView.fromAsset("fragment.pdf").load();
//        pdfView.fromAsset("Thinking_in_Java_4th_edition.pdf").swipeHorizontal(true).load();

        //显示在线文件（校内网测试文件）

        String baseUrl = "http://amicool.neusoft.edu.cn/Uploads/";

        Retrofit retrofit =
                new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .client(client)
                        .build();


        RetrofitService service = retrofit.create(RetrofitService.class);

        final Call<ResponseBody> call = service.getPdf(pdfattach);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()){

                    try {
                        byte[] data = response.body().bytes();
                        pdfView.fromBytes(data).swipeHorizontal(true).load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
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
                    Call<ResponseBody> call = service.collect(mod,tware_id,getSharedPreferences("zyfypt",MODE_PRIVATE).getString("sessionid",""));
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            try {
                                byte[] data =  response.body().bytes();
                                String text = new String(data);
                                if(text.equals("0"))
                                    Toast.makeText(TwareActivity.this, "收藏失败", Toast.LENGTH_SHORT).show();
                                else if(text.contentEquals("error")) {
                                    Toast.makeText(TwareActivity.this, "error", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(TwareActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                                    menu.findItem(R.id.item_collect).setTitle("取消收藏");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                        }
                    });

                }else{
                    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://amicool.neusoft.edu.cn/").build();
                    RetrofitService service = retrofit.create(RetrofitService.class);
                    Call<ResponseBody> call = service.uncollect(mod,tware_id,getSharedPreferences("zyfypt",MODE_PRIVATE).getString("sessionid",""));
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            try {
                                byte[] data =  response.body().bytes();
                                String text = new String(data);
                                if(text.equals("0"))
                                    Toast.makeText(TwareActivity.this, "失败", Toast.LENGTH_SHORT).show();
                                else {
                                    Toast.makeText(TwareActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                                    menu.findItem(R.id.item_collect).setTitle("收藏");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
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
        Call<ResponseBody> call = service.exists(mod,tware_id,getSharedPreferences("zyfypt",MODE_PRIVATE).getString("sessionid",""));
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
            }
        });
    }
}
