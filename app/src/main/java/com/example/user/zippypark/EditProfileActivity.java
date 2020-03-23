package com.example.user.zippypark;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.*;

/**
 * Created by user on 3/19/2020.
 */

public class EditProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button confirmEditProfileButton;
    Button cancelEditProfileButton;
    DatePicker picker;
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText emailEditText;
    EditText phoneEditText;
    EditText passwordEditText;
    EditText licenseNumEditText;
    EditText registrationNumEditText;
    EditText creditCardTypeEditText;
    EditText creditCardNumEditText;
    EditText cvvEditText;
    TextView points;
    static Connection conn;

    int barcode;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        final Bundle b = getIntent().getExtras();
        barcode = b.getInt("barcode"); //customer barcode id

        //Handicap Spinner
        final Spinner spinner = (Spinner) findViewById(R.id.handicapSpinner);
        spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.handicap_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //EXPDATE SPINNER SETUP
        picker=(DatePicker)findViewById(R.id.expDateEditText);


        //pull data from DB like in Profile
        //to prepopulate .setText() for EditText fields kinda like SignUp
        //FIELDS TO UPDATE INFO
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        licenseNumEditText = findViewById(R.id.licenseNumEditText);
        registrationNumEditText = findViewById(R.id.registrationNumEditText);
        creditCardTypeEditText = findViewById(R.id.creditCardTypeEditText);
        creditCardNumEditText = findViewById(R.id.creditCardNumEditText);
        //expDateEditText = findViewById(R.id.expDateEditText);
        cvvEditText = findViewById(R.id.cvvEditText);
        points = findViewById(R.id.points);

//CONFIRM BUTTON
        confirmEditProfileButton = findViewById(R.id.confirm);
        confirmEditProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final String firstName = firstNameEditText.getText().toString();
                final String lastName = lastNameEditText.getText().toString();
                final String email = emailEditText.getText().toString();
                //final int phone = Integer.parseInt(phoneEditText.getText().toString());
                final String phone = phoneEditText.getText().toString();
                final String password = passwordEditText.getText().toString();
                final String licenseNum = licenseNumEditText.getText().toString();
                final String registrationNum = registrationNumEditText.getText().toString();
                final String creditCardType = creditCardTypeEditText.getText().toString();
                final String creditCardNum = creditCardNumEditText.getText().toString();
                //final String expDate = expDateEditText.getText().toString();
                final String cvv = cvvEditText.getText().toString();
                final String handicapAns = spinner.getSelectedItem().toString();
                final String pointVal = points.getText().toString();

                //formatting handicap response: convert to binary
                int num = 0;
                if (handicapAns.equals("Yes")) {
                    num = 1;
                }
                final int handicapAnsNum = num;

                //formatting expDate response: convert to YYYY-MM-DD
                final String year = Integer.toString(picker.getYear()); //year

                final String tempMonth; //month
                int num2 = picker.getMonth();
                if(num2<10){
                    tempMonth = "0" + picker.getMonth();
                }else {
                    tempMonth = Integer.toString(picker.getMonth());
                }
                final String month = tempMonth;

                final String tempDay; //day
                int num3 = picker.getDayOfMonth();
                if(num3<10){
                    tempDay = "0" + picker.getDayOfMonth();
                }else {
                    tempDay = Integer.toString(picker.getMonth());
                }
                final String day = tempDay;

                final String expDate = year + "-" + month + "-" + day; //combining format

                
                //EXECUTE SQL STATEMENT
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Statement stmt = MainActivity.conn.createStatement();
                            //query updates all but points
                            String query = "UPDATE `CustomerInfo` SET `FirstName`='" + firstName +
                                    "', `LastName`='" + lastName + "', `Email`='" + email +
                                    "', `PhoneNum`='" + phone + "', `Password`='" + password +
                                    "', `LicenseNum`='" + licenseNum + "', `RegistrationNum`='" +
                                    registrationNum + "', `CreditCardType`='" + creditCardType +
                                    "', `CreditCardNum`='" + creditCardNum + "', `ExpDate`='" +
                                    expDate + "', `CVV`='" + cvv + "', `Handicapped`=" +
                                    handicapAnsNum + " WHERE `Barcode` = " + barcode;
                            //System.out.println(query);
                            stmt.executeUpdate(query);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });

                //MOVE TO PROFILE
                Intent i = new Intent(EditProfileActivity.this, ProfileActivity.class);
                i.putExtras(b);
                startActivity(i);
            }

        });

        //TRANSITION TO PROFILE SCREEN (cancel)
        cancelEditProfileButton = findViewById(R.id.cancel);
        cancelEditProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditProfileActivity.this,
                        ProfileActivity.class);
                i.putExtras(b);
                startActivity(i);
            }

        });

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


}