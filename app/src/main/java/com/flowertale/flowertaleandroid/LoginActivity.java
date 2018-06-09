package com.flowertale.flowertaleandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.register_btn:
                Intent intent_reg = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent_reg);
                break;
        }
    }
}
