package com.autolease.autoleasecalculator.Customer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.autolease.autoleasecalculator.LeaseCalc.LeaseCalcActivity;
import com.autolease.autoleasecalculator.R;

import org.json.JSONObject;

import java.util.ArrayList;

import Miscellaneous.DialogBox;
import Miscellaneous.Utils;

public class SearchCustomer extends AppCompatActivity {

    private EditText editSearch;
    private Button btnSearch;
    private RelativeLayout relCustomerDetails;
    private TextView txtNameVal;
    private TextView txtAddressVal;
    private TextView txtPhoneVal;
    private TextView txtEmailVal;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_customer);

        initViews();
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        //respond to menu item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, LeaseCalcActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initViews()
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        editSearch = (EditText)findViewById(R.id.editSearch);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        relCustomerDetails = (RelativeLayout)findViewById(R.id.relCustomerDetails);
        txtNameVal = (TextView)findViewById(R.id.txtNameVal);
        txtAddressVal = (TextView)findViewById(R.id.txtAddressVal);
        txtPhoneVal = (TextView)findViewById(R.id.txtPhoneVal);
        txtEmailVal = (TextView)findViewById(R.id.txtEmailVal);

        btnSearch.setOnClickListener(btnSearch_OnClickListener);
    }

    View.OnClickListener btnSearch_OnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            ArrayList<Integer> idList = null;
            idList = Utils.getEmptyFields(getWindow(), R.id.editSearch);
            if (idList.isEmpty()) {
                JSONObject searchCustomer = new JSONObject();
                try {
                    searchCustomer.put("unique", editSearch.getText().toString());
                    new SearchCustomerTask(SearchCustomer.this, searchCustomer.toString().getBytes()).execute();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                new SearchCustomerTask(SearchCustomer.this, searchCustomer.toString().getBytes()).execute();
            }
            else
            {
                final Dialog error = new DialogBox().createErrorDialogBox("One or more fields left empty", context);
                error.show();
                TextView btnOk = error.findViewById(R.id.btnOk);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        error.dismiss();
                    }
                });
            }
        }
    };

    public void updateView(String StrError)
    {
        final Dialog error = new DialogBox().createErrorDialogBox(StrError, context);
        error.show();
        TextView btnOk = error.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error.dismiss();
            }
        });
    }

    public void addCustomerSuccessAction(JSONObject response)
    {
        try {
            response = (JSONObject)response.get("data");
            txtNameVal.setText(response.get("title").toString()+ " " +
                    response.get("first_name").toString()+" "+
                    response.get("last_name").toString());
            String address = "";
            String street_1 = response.get("street_1").toString();
            String street_2 = response.get("street_2").toString();
            String city = response.get("city").toString();
            String state = response.get("state").toString();
            String country = response.get("country").toString();
            String zip = response.get("zip").toString();
            if(!street_1.equals("")) {address += street_1;}
            if(!street_2.equals("")) {address += "\n"+street_2;}
            if(!city.equals("")) {address += "\n"+city;}
            if(!state.equals("")) {address += "\n"+state;}
            if(!country.equals("")) {address += "\n"+country;}
            if(!zip.equals("")) {address += "\n"+zip;}

            txtAddressVal.setText(address);

            String phone = response.get("phone_1").toString()+" "+response.get("phone_1_type").toString();
            if (!response.get("phone_2").toString().equals(""))
            {
                phone += response.get("phone_2").toString() + " " + response.get("phone_2_type").toString();
            }
            txtPhoneVal.setText(phone);
            txtEmailVal.setText(response.get("email").toString());
            relCustomerDetails.setVisibility(View.VISIBLE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
