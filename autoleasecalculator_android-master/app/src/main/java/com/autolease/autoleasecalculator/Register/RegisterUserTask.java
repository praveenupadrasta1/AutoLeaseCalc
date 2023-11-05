package com.autolease.autoleasecalculator.Register;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;

import Helper.HTTPConnection;
import Miscellaneous.Config;
import Miscellaneous.Utils;

/**
 * Created by praveenupadrasta on 06-07-2017.
 */

public class RegisterUserTask extends AsyncTask<Void, Void, JSONObject>
{
    private ProgressDialog pd = null;
    private byte[] registerData = null;
    private RegisterActivity registerActivity = null;

    public RegisterUserTask(RegisterActivity registerActivity, byte[] registerData)
    {
        this.registerActivity = registerActivity;
        this.pd = new ProgressDialog(registerActivity);
        this.registerData = registerData;
    }

    @Override
    protected void onPreExecute()
    {
        pd.setMessage("Adding you as our employee...");
        pd.show();
    }

    @Override
    protected JSONObject doInBackground(Void... params)
    {
        JSONObject response = null;
        try {
            HttpURLConnection urlConnection = new HTTPConnection().doPost(Config.registerUser, registerData, false);
            OutputStream os = urlConnection.getOutputStream();
            os.write(registerData);
            os.close();
            urlConnection.connect();
            response = Utils.readResponse(urlConnection);
            response.put("status_code", urlConnection.getResponseCode());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(JSONObject response)
    {
        pd.dismiss();
        try {
            if ((int) response.get("status_code") != 200) {
                registerActivity.updateView(new JSONObject(response.get("errors").toString()));
            }
            else
            {
                registerActivity.registerUserSuccessAction();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
