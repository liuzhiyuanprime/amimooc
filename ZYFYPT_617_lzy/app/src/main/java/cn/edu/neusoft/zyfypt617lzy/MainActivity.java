package cn.edu.neusoft.zyfypt617lzy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import cn.edu.neusoft.zyfypt617lzy.bean.ErrorBean;
import cn.edu.neusoft.zyfypt617lzy.bean.UserInfo;
import cn.edu.neusoft.zyfypt617lzy.retrofit.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private EditText usernameE,passwordE;
    private Button sign_in,sign_up;
    private CheckBox remember;
    private SharedPreferences sharedPreferences;
    private final String FILE_NAME = "zyfypt";
    private final String KEY_ID = "id";
    private final String KEY_USERNAME = "username";
    private final String KEY_REALNAME = "realname";
    private final String KEY_SESSIONID = "sessionid";
    private final String KEY_ROLENAME = "rolename";
    private final String KEY_SEX = "sex";
    private final String KEY_PASSWORD = "password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        sharedPreferences = getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
        init();
        String savedusername = sharedPreferences.getString(KEY_USERNAME,"");
        String savedpassword = sharedPreferences.getString(KEY_PASSWORD,"");
        String re = getIntent().getStringExtra("re");
        if(!savedusername.equals("") && !savedpassword.equals(""))
        {
            usernameE.setText(savedusername);
            passwordE.setText(savedpassword);
            if(!(re!=null&&re.equals("3")))
            signin(savedusername,savedpassword);
        }
    }

    private void init(){
        usernameE = findViewById(R.id.login_username);
        passwordE = findViewById(R.id.login_password);
        remember = findViewById(R.id.login_remember);
        sign_in = findViewById(R.id.login_signin);
        sign_up = findViewById(R.id.login_signup);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignUpActivity.class));
            }
        });
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            String username = usernameE.getText().toString();
                            String password = passwordE.getText().toString();
                            if (!username.equals("") && !password.equals(""))
                                signin(username,password);

            }
        });

    }

    private void signin(final String username, final String password){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://amicool.neusoft.edu.cn/").build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<ResponseBody> call = retrofitService.login(username, password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    byte[] data = response.body().bytes();
                    String text = new String(data);
                    ObjectMapper objectMapper = new ObjectMapper();
                    UserInfo userInfo = objectMapper.readValue(text,UserInfo.class);
                    if(userInfo == null)
                    {
                        ErrorBean errorBean = objectMapper.readValue(text,ErrorBean.class);
                        Toast.makeText(MainActivity.this,errorBean.getError() , Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if(remember.isChecked())
                            editor.putString(KEY_PASSWORD,password);
                        else
                            editor.putString(KEY_PASSWORD,"");
                        editor.putString(KEY_ID,userInfo.getId());
                        editor.putString(KEY_USERNAME,username);
                        editor.putString(KEY_REALNAME,userInfo.getRealname() );
                        editor.putString(KEY_SESSIONID,userInfo.getSessionid());
                        editor.putString(KEY_ROLENAME,userInfo.getRealname() );
                        editor.putString(KEY_SEX,userInfo.getSex());
                        editor.commit();
                        startActivity( new Intent(MainActivity.this,SignInActivity.class));
                        finish();
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
