package com.autolease.autoleasecalculator.Customer;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Iterator;

import Helper.HTTPConnection;
import Miscellaneous.Config;
import Miscellaneous.Utils;


public class SearchCustomerTask extends AsyncTask<Void, Void, JSONObject>
{
    private ProgressDialog pd = null;
    private SearchCustomer searchCustomer = null;
    private byte[] urlParams = null;

    public SearchCustomerTask(SearchCustomer searchCustomer, byte[] urlParams)
    {
        this.searchCustomer = searchCustomer;
        this.pd = new ProgressDialog(searchCustomer);
        this.urlParams = urlParams;
    }

    @Override
    protected void onPreExecute()
    {
        pd.setMessage("Searching Customer...");
        pd.show();
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        return sendSearchCustomerRequest();
    }

    @Override
    protected void onPostExecute(JSONObject response)
    {
        pd.dismiss();
        try {
            int resCode = (int)response.get("status_code");
            if(resCode != 200) {
                searchCustomer.updateView("No Such Customer exists!");
            }
            else {
                searchCustomer.addCustomerSuccessAction(response);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private JSONObject sendSearchCustomerRequest()
    {
        JSONObject response = null;
        try {
            HttpURLConnection urlConnection = new HTTPConnection().doPost(Config.getCustomer, urlParams, true);
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
