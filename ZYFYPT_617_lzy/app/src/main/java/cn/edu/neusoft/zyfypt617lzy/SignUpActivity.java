package cn.edu.neusoft.zyfypt617lzy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import cn.edu.neusoft.zyfypt617lzy.bean.ErrorBean;
import cn.edu.neusoft.zyfypt617lzy.retrofit.RetrofitService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity {

    private EditText userNameEdit, passwordEdit, telEdit, emailEdit;
    private RadioButton teacherRadio, studentRadio;
    private Button signupBtu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        init();
    }

    private void init() {
        userNameEdit = findViewById(R.id.signup_username);
        passwordEdit = findViewById(R.id.signup_password);
        telEdit = findViewById(R.id.signup_tel);
        emailEdit = findViewById(R.id.signup_email);
        teacherRadio = findViewById(R.id.signup_teacher);
        studentRadio = findViewById(R.id.signup_student);
        signupBtu = findViewById(R.id.signup_signup);
        signupBtu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userNameEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                String tel = telEdit.getText().toString();
                String email = emailEdit.getText().toString();
                String roleid = "2";
                if (teacherRadio.isChecked()) {
                    roleid = "3";
                }
                if (!username.equals("") && !password.equals("") && !tel.equals("") && !email.equals("")) {
                    //1创建Retrofit对象
                    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://amicool.neusoft.edu.cn/").build();
                    //2
                    RetrofitService retrofitService = retrofit.create(RetrofitService.class);
                    Call<ResponseBody> call = retrofitService.signIn(username, password, tel, email, roleid);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try{
                                    byte [] data = response.body().bytes();
                                    String text = new String(data);//response转成文本类型，再由文本类型钻java
                                    if(text.equals("1"))
                                    {
                                        Toast.makeText(SignUpActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }else{
                                        ObjectMapper objectMapper = new ObjectMapper();
                                        ErrorBean errorBean = objectMapper.readValue(text,ErrorBean.class);//字符串类型转java

                                        Toast.makeText(SignUpActivity.this, errorBean.getError(), Toast.LENGTH_SHORT).show();

                                    }
                                }catch (IOException e){
                                    e.printStackTrace();
                                }
                            }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });

                } else {
                    Toast.makeText(SignUpActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}

