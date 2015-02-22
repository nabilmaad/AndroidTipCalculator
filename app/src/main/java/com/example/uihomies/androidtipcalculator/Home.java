package com.example.uihomies.androidtipcalculator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.math.BigDecimal;
import java.math.RoundingMode;
import android.preference.PreferenceManager;


public class Home extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Field hints

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String currency = prefs.getString("currency_list","$");
        EditText amountText = (EditText) findViewById(R.id.billText);
        amountText.setHint(currency);

        String tipAmount = prefs.getString("default_tip",null);
        EditText tipText = (EditText) findViewById(R.id.tipText);
        tipText.setText(tipAmount);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(Home.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void calculateButtonClick(View view) {
        EditText billAmountTextField = (EditText) findViewById(R.id.billText);
        String billAmountText = billAmountTextField.getText().toString();

        EditText tipTextField = (EditText) findViewById(R.id.tipText);
        String tipText = tipTextField.getText().toString();

        EditText peopleTextField = (EditText) findViewById(R.id.peopleText);
        String peopleText = peopleTextField.getText().toString();

        // TODO: Error handling. Alert views should pop up when user enters no values or invalid values.
        boolean valid = true;
        if(billAmountText.length()==0){
            billAmountTextField.setError("Must enter a bill amount!");
            valid = false;
        }
        if(tipText.length()==0){
            tipTextField.setError("Must enter a tip amount!");
            valid = false;
        }
        if(peopleText.length()==0){
            peopleTextField.setError("Must enter number of people!");
            valid = false;
        }

        // TODO: Suggest a tip.
        if(valid) {
            // Grab valid values
            double billAmount = Double.parseDouble(billAmountTextField.getText().toString());
            double tipPercentage = Double.parseDouble(tipTextField.getText().toString());
            double numberOfPeople = Double.parseDouble(peopleTextField.getText().toString());
            // Number of people should be more than one.
            if(numberOfPeople > 0) {// Calculate tipAmount (bill * %) and round up to 2 decimal places.
                double tipAmount = ((new BigDecimal(billAmount * (tipPercentage / 100.0))).setScale(2, RoundingMode.HALF_UP)).doubleValue();
                double totalAmount = billAmount + tipAmount;

                // Calculate tip per person
                double tipPerPerson = tipAmount / numberOfPeople;
                // Calculate how much each person needs to pay
                double eachPersonPays = totalAmount / numberOfPeople;

                // Save values
                Intent intent = new Intent(Home.this, BillSummaryActivity.class);
                intent.putExtra("billAmount", billAmount);
                intent.putExtra("tipAmount", tipAmount);
                intent.putExtra("totalAmount", totalAmount);
                intent.putExtra("tipPerPerson", tipPerPerson);
                intent.putExtra("eachPersonPays", eachPersonPays);

                startActivity(intent);
            }
            else{
                peopleTextField.setError("People can't be zero");
            }
            // Number of people should be bigger than one alert
        }
        //Please enter amount for each field alert.
    }

    /*public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }*/ // Use method if it is needed multiple times in program.
}
