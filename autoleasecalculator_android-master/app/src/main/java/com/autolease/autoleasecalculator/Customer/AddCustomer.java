package com.autolease.autoleasecalculator.Customer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.autolease.autoleasecalculator.LeaseCalc.LeaseCalcActivity;
import com.autolease.autoleasecalculator.LeaseCalc.LeaseCalcTask;
import com.autolease.autoleasecalculator.R;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import Miscellaneous.DialogBox;
import Miscellaneous.Utils;

public class AddCustomer extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinTitle = null;
    private EditText editFirstName = null;
    private EditText editLastName = null;
    private EditText editStreet1 = null;
    private EditText editStreet2 = null;
    private EditText editCity = null;
    private EditText editState = null;
    private EditText editZip = null;
    private Spinner spinCountry = null;
    private EditText editPhone1 = null;
    private Spinner spinPhone1 = null;
    private EditText editPhone2 = null;
    private Spinner spinPhone2 = null;
    private EditText editEmail = null;
    private Button addCustomer = null;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

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

        spinTitle = (Spinner)findViewById(R.id.spinTitle);
        editFirstName = (EditText)findViewById(R.id.editFirstName);
        editLastName = (EditText)findViewById(R.id.editLastName);
        editStreet1 = (EditText)findViewById(R.id.editStreet1);
        editStreet2 = (EditText)findViewById(R.id.editStreet2);
        editCity = (EditText)findViewById(R.id.editCity);
        editState = (EditText)findViewById(R.id.editState);
        editZip = (EditText)findViewById(R.id.editZip);
        spinCountry = (Spinner) findViewById(R.id.spinCountry);
        editPhone1 = (EditText)findViewById(R.id.editPhone1);
        spinPhone1 = (Spinner) findViewById(R.id.spinPhone1);
        editPhone2 = (EditText)findViewById(R.id.editPhone2);
        spinPhone2 = (Spinner) findViewById(R.id.spinPhone2);
        editEmail = (EditText)findViewById(R.id.editEmail);
        addCustomer = (Button) findViewById(R.id.AddCustomer);
        addCustomer.setOnClickListener(addCustomer_OnClickListener);

        // Spinner click listener
        spinTitle.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> titles = new ArrayList<String>();
        titles.add("Mr.");
        titles.add("Miss");
        titles.add("Mrs.");

        spinCountry.setOnItemSelectedListener(this);
        List<String> countries = new ArrayList<String>();
        for(String country: Countries.countries)
        {
            countries.add(country);
        }

        spinPhone1.setOnItemSelectedListener(this);
        spinPhone2.setOnItemSelectedListener(this);
        List<String> phoneTypes = new ArrayList<String>();
        phoneTypes.add("Mobile");
        phoneTypes.add("Residence");
        phoneTypes.add("Office");

        spinTitle.setAdapter(getAdapter(titles));
        spinCountry.setAdapter(getAdapter(countries));
        spinPhone1.setAdapter(getAdapter(phoneTypes));
        spinPhone2.setAdapter(getAdapter(phoneTypes));
    }

    public ArrayAdapter<String> getAdapter(List<String> options)
    {
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, options);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return dataAdapter;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    View.OnClickListener addCustomer_OnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            ArrayList<Integer> idList = null;
            idList = Utils.getEmptyFields(getWindow(), R.id.editFirstName, R.id.editLastName,
                    R.id.editPhone1, R.id.editEmail);
            if (idList.isEmpty()) {
                JSONObject customerData = getCustomerData();
                JSONObject leaseData = getLeaseData();
                JSONObject leaseCustomerData = new JSONObject();
                try {
                    leaseCustomerData.put("lease", leaseData);
                    leaseCustomerData.put("customer", customerData);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                new AddCustomerTask(AddCustomer.this, leaseCustomerData.toString().getBytes()).execute();
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

    private JSONObject getLeaseData()
    {
        JSONObject values = null;
        try {
            JSONObject inputOutputValues = new JSONObject(getIntent().getStringExtra("input_output_values"));
            values = (JSONObject)inputOutputValues.get("input_values");
            values.put("tot_lease_with_tax", ((JSONObject)inputOutputValues.get("output_values"))
                    .get("tot_lease_with_tax"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return values;
    }

    public JSONObject getCustomerData()
    {
        JSONObject customerData = new JSONObject();
        try {
            customerData.put("title", spinTitle.getSelectedItem().toString());
            customerData.put("first_name", editFirstName.getText().toString());
            customerData.put("last_name", editLastName.getText().toString());
            customerData.put("street_1", editStreet1.getText().toString());
            customerData.put("street_2", editStreet2.getText().toString());
            customerData.put("city", editCity.getText().toString());
            customerData.put("state", editState.getText().toString());
            customerData.put("zip", editZip.getText().toString());
            customerData.put("country", spinCountry.getSelectedItem().toString());
            customerData.put("phone_1", editPhone1.getText().toString());
            String ph = editPhone1.getText().toString();
            String phoneType = null;
            if(spinPhone1.getSelectedItem().toString().equals("Mobile"))
            {
                phoneType = "0";
            }
            else if(spinPhone1.getSelectedItem().toString().equals("Residence"))
            {
                phoneType = "1";
            }
            else if(spinPhone1.getSelectedItem().toString().equals("Office"))
            {
                phoneType = "2";
            }
            customerData.put("phone_1_type", phoneType);
            customerData.put("phone_2", editPhone2.getText().toString());

            if(spinPhone2.getSelectedItem().toString().equals("Mobile"))
            {
                phoneType = "0";
            }
            else if(spinPhone2.getSelectedItem().toString().equals("Residence"))
            {
                phoneType = "1";
            }
            else if(spinPhone2.getSelectedItem().toString().equals("Office"))
            {
                phoneType = "2";
            }
            customerData.put("phone_2_type", phoneType);
            customerData.put("email", editEmail.getText().toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return customerData;
    }

    public void updateView(String errors)
    {
        final Dialog error = new DialogBox().createErrorDialogBox(errors, context);
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
            Dialog info = new DialogBox().createInfoDialogBox("Customer added successfully!\nCustomer " +
                    "Unique ID is " + response.get("data"), context);
            info.show();
            TextView btnOk = info.findViewById(R.id.btnOk);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(AddCustomer.this, LeaseCalcActivity.class);
                    startActivity(i);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
