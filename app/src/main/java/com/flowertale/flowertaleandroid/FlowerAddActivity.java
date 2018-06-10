package com.flowertale.flowertaleandroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class FlowerAddActivity extends AppCompatActivity {

    private Intent intent = new Intent();
    public static final int FLOWER_TITLE = 1;
    public static final int FLOWER_MEMBER = 2;
    public int count;
    private LinearLayout flowerTypeLayout;
    private LinearLayout flowerTitleLayout;
    private LinearLayout flowerMemberLayout;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case FLOWER_TITLE:
                    flowerTypeLayout.setVisibility(View.GONE);
                    flowerTitleLayout.setVisibility(View.VISIBLE);
                    count++;
                    break;
                case FLOWER_MEMBER:
                    flowerTitleLayout.setVisibility(View.GONE);
                    flowerMemberLayout.setVisibility(View.VISIBLE);
                    count++;
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_add);
        count = 0;
        initView();
    }

    private void initView(){
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        flowerTypeLayout = findViewById(R.id.flower_type_layout);
        flowerTitleLayout = findViewById(R.id.flower_title_layout);
        flowerMemberLayout = findViewById(R.id.flower_member_layout);
        flowerTypeLayout.setVisibility(View.VISIBLE);
        flowerTitleLayout.setVisibility(View.GONE);
        flowerMemberLayout.setVisibility(View.GONE);

        //FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.done_fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.flower_add_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.next:
                if (count==0){
                    item.setIcon(R.drawable.ic_done);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            TextInputEditText textInputEditText = findViewById(R.id.flower_type);
                            intent.putExtra("flowerType", textInputEditText.getText().toString());
                            Log.d("FlowerAddActivity", textInputEditText.getText().toString());
                            Message message = new Message();
                            message.what = FLOWER_TITLE;
                            handler.sendMessage(message);
                        }
                    }).start();
                }else if (count==1){
                    item.setIcon(R.drawable.ic_done);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            TextInputEditText textInputEditText = findViewById(R.id.flower_title);
                            intent.putExtra("flowerTitle", textInputEditText.getText().toString());
                            Log.d("FlowerAddActivity", textInputEditText.getText().toString());
                            setResult(RESULT_OK, intent);
                            finish();
                            /*Message message = new Message();
                            message.what = FLOWER_MEMBER;
                            handler.sendMessage(message);*/
                        }
                    }).start();
                }else if(count==2){
                    TextInputEditText textInputEditText = findViewById(R.id.flower_member);
                    intent.putExtra("flowerMember", textInputEditText.getText().toString());
                    Log.d("FlowerAddActivity", textInputEditText.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }


}
