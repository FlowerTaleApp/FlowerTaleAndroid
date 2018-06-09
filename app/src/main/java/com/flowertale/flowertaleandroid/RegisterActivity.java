package com.flowertale.flowertaleandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
}
