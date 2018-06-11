package com.flowertale.flowertaleandroid;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flowertale.flowertaleandroid.DTO.response.BaseResponse;
import com.flowertale.flowertaleandroid.constant.TokenConstant;
import com.flowertale.flowertaleandroid.service.FlowerTaleApiService;
import com.flowertale.flowertaleandroid.util.ContextUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 返回
     */
    private TextView mRegGoBack;
    /**
     * 用户名
     */
    private EditText mUsername;
    /**
     * 密码
     */
    private EditText mPassword;
    /**
     * 确认密码
     */
    private EditText mConfirmPass;
    /**
     * 邮箱
     */
    private EditText mEmail;
    /**
     * 注册
     */
    private Button mRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        mRegGoBack = (TextView) findViewById(R.id.reg_goBack);
        mRegGoBack.setOnClickListener(this);
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mConfirmPass = (EditText) findViewById(R.id.confirm_pass);
        mEmail = (EditText) findViewById(R.id.email);
        mRegisterBtn = (Button) findViewById(R.id.register_btn);
        mRegisterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.reg_goBack:
                finish();
                break;
            case R.id.register_btn:
                break;
        }
    }

    /**
     * 注册操作
     *
     * @param username
     * @param password
     * @param email    成功后直接跳转到MainActivity
     */
    private void doSignUp(String username, String password, String email) {
        Call<BaseResponse<String>> call = FlowerTaleApiService.getInstance().doRegister(username, password, email);
        call.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() != 0) {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    String token = response.body().getObject();
                    PreferenceManager.getDefaultSharedPreferences(ContextUtil.getContext()).edit()
                            .putString(TokenConstant.TOKEN, token).apply();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "未知错误!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "请求失败!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
