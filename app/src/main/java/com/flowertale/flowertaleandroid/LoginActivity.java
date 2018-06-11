package com.flowertale.flowertaleandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.flowertale.flowertaleandroid.DTO.Response.BaseResponse;
import com.flowertale.flowertaleandroid.constant.TokenConstant;
import com.flowertale.flowertaleandroid.service.FlowerTaleApiService;
import com.flowertale.flowertaleandroid.util.ContextUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 用户名
     */
    private EditText mUsername;
    /**
     * 密码
     */
    private EditText mPassword;
    /**
     * 登录
     */
    private Button mLoginBtn;
    /**
     * 注册
     */
    private Button mRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mLoginBtn = (Button) findViewById(R.id.login_btn);
        mLoginBtn.setOnClickListener(this);
        mRegisterBtn = (Button) findViewById(R.id.register_btn);
        mRegisterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.login_btn:
                doSignIn(mUsername.getText().toString(), mPassword.getText().toString());
                break;
            case R.id.register_btn:
                Intent intent_reg = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent_reg);
                break;
        }
    }

    /**
     * 登录操作
     * @param username 用户名
     * @param password 密码
     * 成功后直接跳转至MainActivity
     */
    private void doSignIn(String username, String password) {
        Call<BaseResponse<String>> call = FlowerTaleApiService.getInstance().doLogin(username, password);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        String token = response.body().getObject();
                        PreferenceManager.getDefaultSharedPreferences(ContextUtil.getContext()).edit()
                                .putString(TokenConstant.TOKEN, token).apply();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "请求失败!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
