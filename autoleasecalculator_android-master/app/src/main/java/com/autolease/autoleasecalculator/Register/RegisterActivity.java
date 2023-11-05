package com.autolease.autoleasecalculator.Register;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.autolease.autoleasecalculator.Login.LoginActivity;
import com.autolease.autoleasecalculator.R;

import org.json.JSONObject;

import java.util.ArrayList;

import Miscellaneous.DialogBox;
import Miscellaneous.Utils;

public class RegisterActivity extends AppCompatActivity {

    private EditText editUsername = null;
    private EditText editPassword = null;
    private Button btnSignUp = null;
    private TextView txtSignIn = null;
    private TextView txtErrorMsg = null;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
    }

    private void initViews()
    {
        editUsername = (EditText)findViewById(R.id.edit_username);
        editPassword = (EditText)findViewById(R.id.edit_password);
        btnSignUp = (Button)findViewById(R.id.btn_signup);
        txtSignIn = (TextView)findViewById(R.id.txtSignIn);
        txtErrorMsg = (TextView)findViewById(R.id.txtErrorMsg);

        btnSignUp.setOnClickListener(btnSignUp_OnClickListener);
        txtSignIn.setOnClickListener(txtSignIn_OnClickListener);
    }

    View.OnClickListener btnSignUp_OnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            ArrayList<Integer> idList = null;
            idList = Utils.getEmptyFields(getWindow(), R.id.edit_username, R.id.edit_password);
            if (idList.isEmpty()) {
//                editUsername.setBackgroundResource(R.drawable.btn_shape);
//                editPassword.setBackgroundResource(R.drawable.btn_shape);
                byte[] registerData = getRegisterData();
                new RegisterUserTask(RegisterActivity.this, registerData).execute();
            }
            else
            {
                Utils.showErrorMsg(getWindow(), "One or more fields left empty!",
                        R.id.txtErrorMsg, idList);
            }
        }
    };

    View.OnClickListener txtSignIn_OnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginActivity);
            finish();
        }
    };

    public void updateView(JSONObject response)
    {
        try {
            String msg = "";
            if (response.has("username")) {
                String temp = response.get("username").toString();
                msg += "Username: " + temp.substring(2, temp.length() - 2) + "\n";
                editUsername.setBackgroundResource(R.drawable.edittext_error_background);
            }
            if (response.has("password")) {
                String temp = response.get("password").toString();
                msg += "Password: " + temp.substring(2, temp.length() - 2) + "\n";
                editPassword.setBackgroundResource(R.drawable.edittext_error_background);
            }
            txtErrorMsg.setText(msg);
            txtErrorMsg.setVisibility(View.VISIBLE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public byte[] getRegisterData()
    {
        JSONObject registerData = null;
        JSONObject user = null;
        try {
            user = new JSONObject();
            registerData = new JSONObject();
            registerData.put("username", editUsername.getText().toString());
            registerData.put("password", editPassword.getText().toString());
            user.put("user",registerData);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return user.toString().getBytes();
    }

    public void registerUserSuccessAction()
    {
        Dialog success = new DialogBox().createInfoDialogBox("User registration successfull", context);
        success.show();
        TextView btnOk = (TextView) success.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(context, LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
