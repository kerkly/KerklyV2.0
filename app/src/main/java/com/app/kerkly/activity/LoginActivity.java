package com.app.kerkly.activity;

import static com.app.kerkly.utils.SessionManager.login;
import static com.app.kerkly.utils.Utility.isvarification;

import android.Manifest;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.app.kerkly.R;
import com.app.kerkly.model.ContryCode;
import com.app.kerkly.model.LoginUser;
import com.app.kerkly.model.ResponseMessge;
import com.app.kerkly.model.User;
import com.app.kerkly.retrofit.APIClient;
import com.app.kerkly.retrofit.GetResult;
import com.app.kerkly.utils.CustPrograssbar;
import com.app.kerkly.utils.SessionManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class LoginActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.txt_slogin)
    TextView txtSlogin;
    @BindView(R.id.txt_sregister)
    TextView txtSregister;
    @BindView(R.id.lvl_login)
    LinearLayout lvlLogin;
    @BindView(R.id.ed_email)
    EditText edEmail;
    @BindView(R.id.ed_passsword)
    EditText edPasssword;
    @BindView(R.id.txt_forgotpassword)
    TextView txtForgotpassword;
    @BindView(R.id.txt_login)
    TextView txtLogin;
    @BindView(R.id.lvl_register)
    LinearLayout lvlRegister;
    @BindView(R.id.ed_name)
    EditText edName;
    @BindView(R.id.ed_emailnew)
    EditText edEmailnew;
    @BindView(R.id.ed_mobile)
    EditText edMobile;
    @BindView(R.id.ed_passswordnew)
    EditText edPassswordnew;
    @BindView(R.id.ed_refercode)
    EditText edRefercode;
    @BindView(R.id.txt_register)
    TextView txtRegister;
    @BindView(R.id.at_code)
    AutoCompleteTextView atCode;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
TextInputLayout login_password_container;
    Animation animacion1,animacion2,animacion3,animacion4,animacion5,animacion6,animacion7;

    VideoView simpleVideoView;
    MediaController mediaControls;
     @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        FirebaseApp.initializeApp(this);

        login_password_container = findViewById(R.id.login_password_container);
        //video de fondo
         //animaciones
         animacion1 = AnimationUtils.loadAnimation(this, R.anim.from_left_quick_math);
         edEmail.startAnimation(animacion1);

         animacion2 = AnimationUtils.loadAnimation(this, R.anim.from_right_0);
         edPasssword.startAnimation(animacion2);


         animacion3 = AnimationUtils.loadAnimation(this, R.anim.from_left_advanced);
         edName.startAnimation(animacion3);

         animacion4 = AnimationUtils.loadAnimation(this, R.anim.from_right_3);
         edEmailnew.startAnimation(animacion4);

         animacion5 = AnimationUtils.loadAnimation(this, R.anim.from_left_advanced);
         edMobile.startAnimation(animacion5);

         animacion6 = AnimationUtils.loadAnimation(this, R.anim.from_right_3);
         edPassswordnew.startAnimation(animacion6);

         animacion7 = AnimationUtils.loadAnimation(this, R.anim.from_right_1);
         edRefercode.startAnimation(animacion7);
/*
         animation3 = AnimationUtils.loadAnimation(this, R.anim.from_left_advanced);
         layoutContra.startAnimation(animation3);

         animation4 = AnimationUtils.loadAnimation(this, R.anim.from_right_0);
         boton.startAnimation(animation4);


 */

         simpleVideoView = (VideoView) findViewById(R.id.vv_fondo);

         if (mediaControls == null) {
             // create an object of media controller class
             mediaControls = new MediaController(this);
             mediaControls.setAnchorView(simpleVideoView);
         }
         simpleVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
             @Override
             public void onPrepared(MediaPlayer mp) {
                 float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
                 float screenRatio = simpleVideoView.getWidth() / (float)
                         simpleVideoView.getHeight();
                 float scaleX = videoRatio / screenRatio;
                 if (scaleX >= 1f) {
                     simpleVideoView.setScaleX(scaleX);
                 } else {
                     simpleVideoView.setScaleY(1f / screenRatio);
                 }
             }
         });

         // set the uri for the video view
         simpleVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.fondokerklyy));
         // start a video
         simpleVideoView.start();

         // implement on completion listener on video view
         simpleVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
             @Override
             public void onCompletion(MediaPlayer mp) {
                 simpleVideoView.start();
              }
         });
         simpleVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
             @Override
             public boolean onError(MediaPlayer mp, int what, int extra) {
                 Toast.makeText(getApplicationContext(), "Oops An Error Occur While Playing Video...!!!", Toast.LENGTH_LONG).show(); // display a toast when an error is occured while playing an video
                 return false;
             }
         });

        requestPermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(LoginActivity.this);
        atCode.setOnFocusChangeListener((view, b) -> {
            if (!b) {
                // on focus off
                String str = atCode.getText().toString();

                ListAdapter listAdapter = atCode.getAdapter();
                for (int i = 0; i < listAdapter.getCount(); i++) {
                    String temp = listAdapter.getItem(i).toString();
                    if (str.compareTo(temp) == 0) {
                        return;
                    }
                }

                atCode.setText("");

            }
        });
        getCodelist();
    }

    private void getCodelist() {
        custPrograssbar.prograssCreate(LoginActivity.this);
        JSONObject jsonObject = new JSONObject();
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getCodelist(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");

    }

    private void loginUser() {
        custPrograssbar.prograssCreate(LoginActivity.this);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("mobile", edEmail.getText().toString());
                jsonObject.put("password", edPasssword.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "ERROR +"+e, Toast.LENGTH_SHORT).show();
            }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().loginUser(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }
    private void checkUser() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", edMobile.getText().toString());
            jsonObject.put("ccode", atCode.getText().toString());
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().getMobileCheck(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "3");
            custPrograssbar.prograssCreate(LoginActivity.this);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "ERROR +"+e, Toast.LENGTH_SHORT).show();

        }
    }


    @OnClick({R.id.txt_slogin, R.id.txt_sregister, R.id.txt_register, R.id.txt_login, R.id.txt_forgotpassword})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.txt_slogin:
                txtSlogin.setBackground(getResources().getDrawable(R.drawable.tab1));
                txtSlogin.setTextColor(getResources().getColor(R.color.white));
                txtSregister.setBackground(getResources().getDrawable(R.drawable.tab2));
                txtSregister.setTextColor(getResources().getColor(R.color.black));
                lvlLogin.setVisibility(View.VISIBLE);
                lvlRegister.setVisibility(View.GONE);

                break;

            case R.id.txt_sregister:
                txtSlogin.setBackground(getResources().getDrawable(R.drawable.tab2));
                txtSlogin.setTextColor(getResources().getColor(R.color.black));
                txtSregister.setBackground(getResources().getDrawable(R.drawable.tab1));
                txtSregister.setTextColor(getResources().getColor(R.color.white));
                lvlLogin.setVisibility(View.GONE);
                lvlRegister.setVisibility(View.VISIBLE);
                break;

            case R.id.txt_register:
                if (validationCreate()) {
                    checkUser();
                }

                break;
            case R.id.txt_login:
                if (validation()) {
                    loginUser();
                }
                break;
            case R.id.txt_forgotpassword:
                startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                LoginUser loginUser = gson.fromJson(result.toString(), LoginUser.class);
                Toast.makeText(LoginActivity.this, loginUser.getResponseMsg(), Toast.LENGTH_LONG).show();
                if (loginUser.getResult().equalsIgnoreCase("true")) {
                    sessionManager.setUserDetails("", loginUser.getUser());
                    sessionManager.setBooleanData(login, true);
                    OneSignal.sendTag("userid", loginUser.getUser().getId());
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                ContryCode contryCode = gson.fromJson(result.toString(), ContryCode.class);
                ArrayList<String> countries = new ArrayList<>();
                if (contryCode.getResult().equalsIgnoreCase("true")) {
                    countries = new ArrayList<>();
                    for (int i = 0; i < contryCode.getCountryCode().size(); i++) {
                        countries.add(contryCode.getCountryCode().get(i).getCcode());
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>
                        (this, android.R.layout.simple_list_item_1, countries);
                atCode.setAdapter(adapter);
                atCode.setThreshold(1);
            }else if (callNo.equalsIgnoreCase("3")) {
                Gson gson = new Gson();
                ResponseMessge response = gson.fromJson(result.toString(), ResponseMessge.class);
                if (response.getResult().equals("true")) {
                    isvarification = 1;
                    User user = new User();
                    user.setName(edName.getText().toString());
                    user.setEmail(edEmailnew.getText().toString());
                    user.setCcode(atCode.getText().toString());
                    user.setMobile(edMobile.getText().toString());
                    user.setPassword(edPassswordnew.getText().toString());
                    user.setRefercode(edRefercode.getText().toString());
                    sessionManager.setUserDetails("", user);
                    startActivity(new Intent(this, VerifyPhoneActivity.class).putExtra("code", atCode.getText().toString()).putExtra("phone", edMobile.getText().toString()));
                } else {
                    Toast.makeText(LoginActivity.this, "" + response.getResponseMsg(), Toast.LENGTH_LONG).show();

                }
            }
        } catch (Exception e) {
            Log.e("Errror", "==>" + e.toString());

        }
    }

    public boolean validation() {
        if (TextUtils.isEmpty(edEmail.getText().toString())) {
            edEmail.setError("Enter Email");
            return false;
        }
        if (TextUtils.isEmpty(edPasssword.getText().toString())) {
            edPasssword.setError("Enter Password");
            return false;
        }
        return true;
    }

    public boolean isValidEmail(String emailToReview){
        final String regex = "(?:[^<>()\\[\\].,;:\\s@\"]+(?:\\.[^<>()\\[\\].,;:\\s@\"]+)*|\"[^\\n\"]+\")@(?:[^<>()\\[\\].,;:\\s@\"]+\\.)+[^<>()\\[\\]\\.,;:\\s@\"]{2,63}";
        if (!emailToReview.matches(regex)) {
            return false; //Es incorrecto
        }else{
            return true; // Es correcto
        }
    }
    public boolean validationCreate() {
        if (TextUtils.isEmpty(edName.getText().toString())) {
            edName.setError("Enter Name");
            return false;
        }
        if (TextUtils.isEmpty(edEmailnew.getText().toString())) {

            edEmailnew.setError("Enter Email");



            return false;
        }
        if (TextUtils.isEmpty(edMobile.getText().toString())) {
            edMobile.setError("Enter Mobile");
            return false;
        }
        if (TextUtils.isEmpty(edPassswordnew.getText().toString())) {
            edPassswordnew.setError("Enter Password");
            return false;
        }

        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mCurrentVideoPosition = mMediaPlayer.currentPosition;
       simpleVideoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
       simpleVideoView.start();
    }


}
