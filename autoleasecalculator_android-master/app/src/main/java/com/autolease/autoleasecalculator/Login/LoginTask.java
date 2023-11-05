package com.autolease.autoleasecalculator.Login;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Iterator;

import Helper.HTTPConnection;
import Miscellaneous.Config;
import Miscellaneous.Utils;


class LoginTask extends AsyncTask<Void, Void, JSONObject>
{
    private ProgressDialog pd = null;
    private LoginActivity loginActivity = null;
    private byte[] urlParams = null;

    public LoginTask(LoginActivity loginActivity, byte[] urlParams)
    {
        this.loginActivity = loginActivity;
        this.pd = new ProgressDialog(loginActivity);
        this.urlParams = urlParams;
    }

    @Override
    protected void onPreExecute()
    {
        pd.setMessage("Logging you in...");
        pd.show();
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        return sendLoginUserRequest();
    }

    @Override
    protected void onPostExecute(JSONObject response)
    {
        pd.dismiss();
        try {
            int resCode = (int)response.get("status_code");
            if(resCode != 200) {
                String errors_string = response.get("errors").toString();
                JSONObject errors_obj = new JSONObject(errors_string);
                Iterator<String> keys = errors_obj.keys();
                String errors = "";
                while (keys.hasNext())
                {
                    String tempError = errors_obj.get(keys.next().toString()).toString();
                    tempError = tempError.substring(2,tempError.length()-2);
                    errors += tempError+"\n";
                }
                loginActivity.updateView(errors);
            }
            else {
                loginActivity.loginUserSuccessAction(response);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private JSONObject sendLoginUserRequest()
    {
        JSONObject response = null;
        try {
            HttpURLConnection urlConnection = new HTTPConnection().doPost(Config.login, urlParams, false);
            OutputStream os = urlConnection.getOutputStream();
            os.write(urlParams);
            os.close();

            urlConnection.connect();
            int resCode = urlConnection.getResponseCode();
            response = Utils.readResponse(urlConnection);
            response.put("status_code", resCode);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return response;
    }

}
