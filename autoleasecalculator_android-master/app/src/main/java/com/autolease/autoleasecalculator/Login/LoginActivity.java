package com.autolease.autoleasecalculator.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.autolease.autoleasecalculator.LeaseCalc.LeaseCalcActivity;
import com.autolease.autoleasecalculator.R;
import com.autolease.autoleasecalculator.Register.RegisterActivity;

import org.json.JSONObject;
import java.util.ArrayList;
import Miscellaneous.Utils;


public class LoginActivity extends AppCompatActivity {

    EditText editUsername = null, editPassword = null;
    Button btnLogin = null;
    TextView txtErrorMsg = null;
    TextView txtRegisterHere = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    private void initViews()
    {
        editUsername = (EditText)findViewById(R.id.edit_username);
        editPassword = (EditText)findViewById(R.id.edit_password);
        btnLogin = (Button)findViewById(R.id.btn_signin);
        txtErrorMsg = (TextView)findViewById(R.id.txtErrorMsg);
        txtRegisterHere = (TextView)findViewById(R.id.txtRegisterHere);
        txtRegisterHere.setOnClickListener(btnRegisterHere_OnClickListener);
        btnLogin.setOnClickListener(btnLogin_OnClickListener);
    }

    View.OnClickListener btnRegisterHere_OnClickListener = new View.OnClickListener(){
      @Override
        public void onClick(View view){
          Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
          startActivity(registerIntent);
          finish();
      }
    };

    View.OnClickListener btnLogin_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                ArrayList<Integer> idList = null;
                idList = Utils.getEmptyFields(getWindow(), R.id.edit_username, R.id.edit_password);
                if (idList.isEmpty()) {
                    byte[] urlParams = getLoginDataFromView();
                    new LoginTask(LoginActivity.this, urlParams).execute();
                }
                else
                {
                    Utils.showErrorMsg(getWindow(), "One or more fields left empty!",
                            R.id.txtErrorMsg, idList);
                }
            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    };

    public byte[] getLoginDataFromView()
    {
        byte[] urlParams = null;
        try {
            JSONObject loginData = new JSONObject();
            loginData.put("username", editUsername.getText().toString());
            loginData.put("password", editPassword.getText().toString());
            urlParams = loginData.toString().getBytes();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return urlParams;
    }

    public void updateView(String response)
    {
        txtErrorMsg.setText(response);
        txtErrorMsg.setVisibility(View.VISIBLE);
        editUsername.setBackgroundResource(R.drawable.edittext_error_background);
        editPassword.setBackgroundResource(R.drawable.edittext_error_background);
    }

    public void loginUserSuccessAction(JSONObject response)
    {
        new LoginModel(getApplicationContext()).storeAccessToken(response);
        Intent loginIntent = new Intent(getApplicationContext(), LeaseCalcActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
