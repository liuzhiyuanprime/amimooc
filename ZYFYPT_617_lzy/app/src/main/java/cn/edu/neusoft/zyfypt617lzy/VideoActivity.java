package cn.edu.neusoft.zyfypt617lzy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;

import cn.edu.neusoft.zyfypt617lzy.retrofit.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class VideoActivity extends AppCompatActivity {
    private VideoView videoView;
    private ProgressDialog dialog;
    private String mod,video_id,videopath;
    private Menu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoView = findViewById(R.id.videoView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//设置屏幕方向为横向
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        getWindow().setFormat(PixelFormat.TRANSLUCENT);//透明
        init();
    }
    private void init(){
        Intent intent = getIntent();
        videopath = intent.getStringExtra("videopath");
        mod = intent .getStringExtra("mod");
        video_id = intent.getStringExtra("article_id");
        dialog=ProgressDialog.show(VideoActivity.this, "视频加载中...", "请您稍候");//进度条

        Uri uri = Uri.parse("http://amicool.neusoft.edu.cn/Uploads/video/video/"+videopath);
        videoView.setMediaController(new MediaController(this));//媒体播放控制工具
        videoView.setVideoURI(uri);//设置视频路径
        videoView.setOnPreparedListener(new MyPlayerOnPreparedListener());//播放开始回调
        videoView.setOnCompletionListener( new MyPlayerOnCompletionListener());//播放完成回调
        videoView.requestFocus();// 让VideoView获取焦点
        videoView.start();//开始播放
    }
    //自定义子类，监听视频准备好，消除加载对话框
    class MyPlayerOnPreparedListener implements MediaPlayer.OnPreparedListener {
        @Override
        public void onPrepared(MediaPlayer mp) {
            videoView.setBackgroundColor(Color.argb(0, 0, 255, 0));
            dialog.dismiss();
        }
    }
    //自定义子类，监听播放完成，显示完成
    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText( VideoActivity.this, "播放完成", Toast.LENGTH_SHORT).show();
            getSupportActionBar().show();
        }
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
                    Call<ResponseBody> call = service.collect(mod,video_id,getSharedPreferences("zyfypt",MODE_PRIVATE).getString("sessionid",""));
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            try {
                                byte[] data =  response.body().bytes();
                                String text = new String(data);
                                if(text.equals("0"))
                                    Toast.makeText(VideoActivity.this, "收藏失败", Toast.LENGTH_SHORT).show();
                                else if(text.contentEquals("error")) {
                                    Toast.makeText(VideoActivity.this, "error", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(VideoActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                                    menu.findItem(R.id.item_collect).setTitle("取消收藏");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(VideoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }else{
                    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://amicool.neusoft.edu.cn/").build();
                    RetrofitService service = retrofit.create(RetrofitService.class);
                    Call<ResponseBody> call = service.uncollect(mod,video_id,getSharedPreferences("zyfypt",MODE_PRIVATE).getString("sessionid",""));
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                            try {
                                byte[] data =  response.body().bytes();
                                String text = new String(data);
                                if(text.equals("0"))
                                    Toast.makeText(VideoActivity.this, "失败", Toast.LENGTH_SHORT).show();
                                else if(text.contentEquals("error")) {
                                    Toast.makeText(VideoActivity.this, "error", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(VideoActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                                    menu.findItem(R.id.item_collect).setTitle("收藏");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(VideoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
        Call<ResponseBody> call = service.exists(mod,video_id,getSharedPreferences("zyfypt",MODE_PRIVATE).getString("sessionid",""));
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
                Toast.makeText(VideoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
