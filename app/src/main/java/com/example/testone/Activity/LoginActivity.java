package com.example.testone.Activity;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.testone.Main2Activity;
import com.example.testone.Model.Responses;
import com.example.testone.Model.User;
import com.example.testone.Net.OkHttpHelper;
import com.example.testone.Net.ServerHelper;
import com.example.testone.Net.TaskRunner;
import com.example.testone.R;
import com.example.testone.Util.SPUtil;
import com.google.gson.reflect.TypeToken;
import com.example.testone.Net.RequestCode;

import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.phone_number)
    EditText editTextPhone;

    @BindView(R.id.code)
    EditText editTextCode;

    @BindView(R.id.ok)
    Button buttonOk;

    @BindView(R.id.register)
    Button buttonRegister;

    private static final String TAG="登陆界面";

    private Responses<User> result;

    private Context context;


    private SPUtil spUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        spUtil = new SPUtil(context);
        ButterKnife.bind(this);
//        editTextCode.requestFocus();
        editTextCode.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
    }

    @OnClick(R.id.ok)
    void ok(){

        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, "网络不可用，检查您的网络设置", Toast.LENGTH_LONG).show();
            return;
        }

        String sPhone = editTextPhone.getText().toString().trim();
        String sPwd = editTextCode.getText().toString();



        String url = ServerHelper.getUrlLogin(sPhone, sPwd);
        try {
            TaskRunner.execute(() -> {
                Type type = new TypeToken<Responses<User>>() {
                }.getType();

                Log.d(TAG, "start to send verification code.");
                Responses<User> response = OkHttpHelper.get(url, type);
                runOnUiThread(() -> {
                    if (response!=null) {
                        switch (response.getCode()){
                            case RequestCode.SUCCESS:
                                result=response;
                                success();
                                break;
                            case RequestCode.NOT_FOUND:
                                Toast.makeText(context,"登录失败:"+response.getMsg(),Toast.LENGTH_LONG).show();
                                break;
                            default:
                                break;
                        }
                    } else {
                        Toast.makeText(context, "无法连接服务器", Toast.LENGTH_LONG).show();
                        Log.i(TAG,"响应："+response+";");
                    }
                });
            });
        }
        catch (Exception e){
            Log.e(TAG, "fastLogin exception: "+e);
        }

    }

    @OnClick(R.id.register)
    void register(){
        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, "网络不可用，检查您的网络设置", Toast.LENGTH_LONG).show();
            return;
        }

        String sPhone = editTextPhone.getText().toString().trim();
        String sPwd = editTextCode.getText().toString();

        String url=ServerHelper.getUrlRegister(sPhone,sPwd);
        try {
            TaskRunner.execute(() -> {
                Type type = new TypeToken<Responses<User>>() {
                }.getType();

                Log.d(TAG, "start to send register code.");
                Responses<User> response = OkHttpHelper.get(url, type);
                runOnUiThread(() -> {
                    if (response!=null) {
                        switch (response.getCode()){
                            case RequestCode.SUCCESS:
                                Toast.makeText(context,"注册成功:"+response.getMsg(),Toast.LENGTH_LONG).show();
                                break;
                            case RequestCode.NOT_FOUND:
                                Toast.makeText(context,"注册失败:"+response.getMsg(),Toast.LENGTH_LONG).show();
                                break;
                            default:
                                break;
                        }
                    } else {
                        Toast.makeText(context, "无法连接服务器", Toast.LENGTH_LONG).show();
                        Log.i(TAG,"响应："+response+";");
                    }
                });
            });
        }
        catch (Exception e){
            Log.e(TAG, "register exception: "+e);
        }


    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        } catch (Exception e) {
            Log.e(TAG, "isNetworkAvailable exception"+ " " + e);
        }
        return false;
    }
    private void success() {
        User user=result.getObject();
        Log.d("User:",user.getName());
        Intent intent=new Intent(context, MainActivity.class);
        if (!spUtil.getString(spUtil.KEY_PHONE, "").equals(user.getName())) {
            spUtil.clearAll();
        }
        spUtil.putString(spUtil.KEY_PHONE, user.getName());
        startActivity(intent);
        finish();

    }
}
