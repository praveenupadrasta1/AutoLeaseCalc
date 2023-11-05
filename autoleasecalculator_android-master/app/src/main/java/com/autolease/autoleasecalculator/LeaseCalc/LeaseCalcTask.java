package com.autolease.autoleasecalculator.LeaseCalc;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.autolease.autoleasecalculator.Login.LoginActivity;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Iterator;

import Helper.HTTPConnection;
import Miscellaneous.Config;
import Miscellaneous.Utils;



public class LeaseCalcTask extends AsyncTask<Void, Void, JSONObject>
{
    private ProgressDialog pd = null;
    private LeaseCalcActivity leaseCalcActivity = null;
    private byte[] urlParams = null;

    public LeaseCalcTask(LeaseCalcActivity leaseCalcActivity, byte[] urlParams)
    {
        this.leaseCalcActivity = leaseCalcActivity;
        this.pd = new ProgressDialog(leaseCalcActivity);
        this.urlParams = urlParams;
    }

    @Override
    protected void onPreExecute()
    {
        pd.setMessage("Calculating...");
        pd.show();
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        return sendCalculateLeaseRequest();
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
                leaseCalcActivity.updateView(errors);
            }
            else {
                leaseCalcActivity.leaseCalcSuccessAction((JSONObject)response.get("data"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private JSONObject sendCalculateLeaseRequest()
    {
        JSONObject response = null;
        try {
            HttpURLConnection urlConnection = new HTTPConnection().doPost(Config.leaseAmtCalc, urlParams, true);
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
