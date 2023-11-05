package com.autolease.autoleasecalculator.LeaseCalc;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.autolease.autoleasecalculator.Customer.AddCustomer;
import com.autolease.autoleasecalculator.Customer.SearchCustomer;
import com.autolease.autoleasecalculator.R;

import org.json.JSONObject;

import java.util.ArrayList;

import Miscellaneous.DialogBox;
import Miscellaneous.Utils;

public class LeaseCalcActivity extends AppCompatActivity {

    private EditText editStickerPrice = null;
    private EditText editResidualPercentage = null;
    private EditText editNegSalesPrice = null;
    private EditText editDownPayment = null;
    private EditText editLeaseAcqFee = null;
    private EditText editDocTireFee = null;
    private EditText editMoneyFactor = null;
    private EditText editLeaseMonths = null;
    private EditText editTaxRate = null;
    private EditText editRegFee = null;
    private Button btnCalculate = null;
    Dialog valuesDialog = null;
    private static Context context;
    private Context thisContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lease_calc);
        context = getApplicationContext();
        initViews();
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        //respond to menu item selection
        switch (item.getItemId()) {
            case R.id.searchCustomer:
                startActivity(new Intent(this, SearchCustomer.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public static Context getContext() {
        return context;
    }

    private void initViews()
    {
        editStickerPrice = (EditText)findViewById(R.id.editStickerPrice);
        editResidualPercentage = (EditText)findViewById(R.id.editResidualPercentage);
        editNegSalesPrice = (EditText)findViewById(R.id.editNegSalesPrice);
        editDownPayment = (EditText)findViewById(R.id.editDownPayment);
        editLeaseAcqFee = (EditText)findViewById(R.id.editLeaseAcqFee);
        editDocTireFee = (EditText)findViewById(R.id.editDocTireFee);
        editMoneyFactor = (EditText)findViewById(R.id.editMoneyFactor);
        editLeaseMonths = (EditText)findViewById(R.id.editLeaseMonths);
        editTaxRate = (EditText)findViewById(R.id.editTaxRate);
        editRegFee = (EditText)findViewById(R.id.editRegFee);
        valuesDialog = new Dialog(this);
        btnCalculate = (Button)findViewById(R.id.btnCalculate);
        btnCalculate.setOnClickListener(btnCalculate_OnClickListener);
    }

    View.OnClickListener btnCalculate_OnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            ArrayList<Integer> idList = null;
            idList = Utils.getEmptyFields(getWindow(), R.id.editStickerPrice, R.id.editResidualPercentage,
                    R.id.editNegSalesPrice, R.id.editDownPayment, R.id.editLeaseAcqFee, R.id.editDocTireFee,
                    R.id.editMoneyFactor, R.id.editLeaseMonths, R.id.editTaxRate, R.id.editRegFee);
            if (idList.isEmpty()) {
                byte[] leaseData = getLeaseData();
                new LeaseCalcTask(LeaseCalcActivity.this, leaseData).execute();
            }
            else
            {
                final Dialog error = new DialogBox().createErrorDialogBox("One or more fields left empty", thisContext);
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

    private byte[] getLeaseData()
    {
        JSONObject leaseData = new JSONObject();
        try {
            leaseData.put("sticker_price", Integer.parseInt(editStickerPrice.getText().toString()));
            leaseData.put("residual_percent", Double.parseDouble(editResidualPercentage.getText().toString()));
            leaseData.put("neg_sales_price", Integer.parseInt(editNegSalesPrice.getText().toString()));
            leaseData.put("down_payment", Integer.parseInt(editDownPayment.getText().toString()));
            leaseData.put("lease_acq_fee", Integer.parseInt(editLeaseAcqFee.getText().toString()));
            leaseData.put("doc_tire_fee", Integer.parseInt(editDocTireFee.getText().toString()));
            leaseData.put("money_factor", Double.parseDouble(editMoneyFactor.getText().toString()));
            leaseData.put("lease_months", Integer.parseInt(editLeaseMonths.getText().toString()));
            leaseData.put("tax_rate", Double.parseDouble(editTaxRate.getText().toString()));
            leaseData.put("reg_fee", Integer.parseInt(editRegFee.getText().toString()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return leaseData.toString().getBytes();
    }

    public void updateView(String errors)
    {
        final Dialog error = new DialogBox().createErrorDialogBox(errors, getApplicationContext());
        error.show();
        TextView btnOk = error.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error.dismiss();
            }
        });
    }

    public void leaseCalcSuccessAction(final JSONObject response)
    {
        TextView txtResidualValue;
        TextView txtTotalFeesVal;
        TextView txtAdjCapCostVal;
        TextView txtDepAmtVal;
        TextView txtBasePaymentVal;
        TextView txtMonthlyRentVal;
        TextView txtTotalLeaseNoTaxVal;
        TextView txtTotalLeaseWithTaxVal;
        TextView txtDriveOffVal;
        TextView txtTotCostLeaseVal;
        Button btnAddCustomer;
        Button btnClose;

        valuesDialog.setContentView(R.layout.popup_calculated_values);
        txtResidualValue = (TextView) valuesDialog.findViewById(R.id.txtResidualValue);
        txtTotalFeesVal = (TextView) valuesDialog.findViewById(R.id.txtTotalFeesVal);
        txtAdjCapCostVal = (TextView) valuesDialog.findViewById(R.id.txtAdjCapCostVal);
        txtDepAmtVal = (TextView) valuesDialog.findViewById(R.id.txtDepAmtVal);
        txtBasePaymentVal = (TextView) valuesDialog.findViewById(R.id.txtBasePaymentVal);
        txtMonthlyRentVal = (TextView) valuesDialog.findViewById(R.id.txtMonthlyRentVal);
        txtTotalLeaseNoTaxVal = (TextView) valuesDialog.findViewById(R.id.txtTotalLeaseNoTaxVal);
        txtTotalLeaseWithTaxVal = (TextView) valuesDialog.findViewById(R.id.txtTotalLeaseWithTaxVal);
        txtDriveOffVal = (TextView) valuesDialog.findViewById(R.id.txtDriveOffVal);
        txtTotCostLeaseVal = (TextView) valuesDialog.findViewById(R.id.txtTotCostLeaseVal);
        btnAddCustomer = (Button) valuesDialog.findViewById(R.id.btnAddCustomer);
        btnClose = (Button) valuesDialog.findViewById(R.id.btnClose);

        btnAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addCustomerIntent = new Intent(getApplicationContext(), AddCustomer.class);
                addCustomerIntent.putExtra("input_output_values", response.toString());
                startActivity(addCustomerIntent);
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valuesDialog.dismiss();
            }
        });

        JSONObject outputValues = new JSONObject();
        try {
            outputValues = (JSONObject) response.get("output_values");
            txtResidualValue.setText(outputValues.get("residual_value").toString());
            txtTotalFeesVal.setText(outputValues.get("tot_fee").toString());
            txtAdjCapCostVal.setText(outputValues.get("adj_cap_cost").toString());
            txtDepAmtVal.setText(outputValues.get("dep_amt").toString());
            txtBasePaymentVal.setText(outputValues.get("base_pay").toString());
            txtMonthlyRentVal.setText(outputValues.get("month_rent").toString());
            txtTotalLeaseNoTaxVal.setText(outputValues.get("tot_lease_without_tax").toString());
            txtTotalLeaseWithTaxVal.setText(outputValues.get("tot_lease_with_tax").toString());
            txtDriveOffVal.setText(outputValues.get("drive_off").toString());
            txtTotCostLeaseVal.setText(outputValues.get("tot_cost_of_lease").toString());
            valuesDialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
