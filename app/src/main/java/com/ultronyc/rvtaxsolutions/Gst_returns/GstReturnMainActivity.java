package com.ultronyc.rvtaxsolutions.Gst_returns;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.developer.kalert.KAlertDialog;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.ultronyc.rvtaxsolutions.Itr_filling.BusinessReturnActivity;
import com.ultronyc.rvtaxsolutions.Itr_filling.DialogBusinessReturnDocument;
import com.ultronyc.rvtaxsolutions.Itr_filling.UploadBusinessReturnDocActivity;
import com.ultronyc.rvtaxsolutions.R;
import com.ultronyc.rvtaxsolutions.RegistrationActivity;
import com.ultronyc.rvtaxsolutions.paymentMainactivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;


public class GstReturnMainActivity extends AppCompatActivity {



    SearchableSpinner businessnatureSpinner, GSTTYPESpinner, itrpurposeSpinner;
    ProgressBar progress_BnatureSpinner_GST,progress_GSTTYPESpinner_GST,progress_itrpurposeSpinner_BR,progress_GSTmain;
    Button submit;
    EditText full_name,email,mobile_no;
    TextView birth_date,view_doc_list,GSTTYPEPLAN_AMT,GSTTYPEPLAN_DESC;
    ScrollView mail;
    LinearLayout GSTPLANDESC;

    String loginId,profileName;

    private int mYear, mMonth, mDay, type;

    String charges;

    String businessnatureStringName,GSTTYPEStringName,itrpurposeStringName;
    boolean verify = false,verify1 = false,verify2 = false;
    int position1,position2,position3;
    int businessnature_id,GSTTYPE_id,itrpurpose_id;


    List<String> businessnaturedata1 = new ArrayList<String>();
    List<String> businessnatureName = new ArrayList<String>();
    List<String> businessnatureIdLst = new ArrayList<String>();

    List<String> GSTTYPEdata1 = new ArrayList<String>();
    List<String> GSTTYPEName = new ArrayList<String>();
    List<String> GSTTYPEIdLst = new ArrayList<String>();



    String get_business_nature_list_url = "http://rvtaxs.com/android/business_nature.php";
    String get_GSTTYPE_list_url = "http://rvtaxs.com/android/gst_return_type.php";
    String URL_MAINSUBMIT = "http://rvtaxs.com/android/gst_returns_master.php";


    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gst_return_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = PreferenceManager.getDefaultSharedPreferences(GstReturnMainActivity.this);

        final ActionBar abar = getSupportActionBar();
        //abar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));//line under the action bar
        View viewActionBar = getLayoutInflater().inflate(R.layout.actionbar_titletext_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textviewTitle.setText("GST RETURNS");
        abar.setCustomView(viewActionBar, params);
        abar.setDisplayShowCustomEnabled(true);
        abar.setDisplayShowTitleEnabled(false);
        abar.setDisplayHomeAsUpEnabled(true);
        abar.setIcon(R.color.transparent);
        abar.setHomeButtonEnabled(true);


        loginId = pref.getString("LOGIN_ID", null);
        profileName = pref.getString("PROFILE_NAME", null);

        mail=(ScrollView)findViewById(R.id.GST1_mainScroll);
        progress_GSTmain = (ProgressBar) findViewById(R.id.progress_GST1main);
        mail.setVisibility(View.GONE);
        progress_GSTmain.setVisibility(View.VISIBLE);

        businessnatureSpinner = (SearchableSpinner) findViewById(R.id.businessnaturespinner_GST1);
        progress_BnatureSpinner_GST = (ProgressBar) findViewById(R.id.progress_BnatureSpinner_GST1);

        GSTTYPESpinner = (SearchableSpinner) findViewById(R.id.GSTTYPEspinner_GST1);
        progress_GSTTYPESpinner_GST = (ProgressBar) findViewById(R.id.progress_GSTTYPESpinner_GST1);
        GSTTYPEPLAN_AMT = (TextView) findViewById(R.id.GSTTYPEPLAN_AMT);
        GSTTYPEPLAN_DESC = (TextView) findViewById(R.id.GSTTYPEPLAN_DESC);


        full_name = (EditText) findViewById(R.id.full_name_gstreturn);
        email = (EditText) findViewById(R.id.email_gstreturn);
        mobile_no = (EditText) findViewById(R.id.mobile_no_gstreturn);
       
        submit = (Button) findViewById(R.id.GST1_submit);

        GetBusinessNatureList_BR();
        GetGSTYPEList_GST();

       

     


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(SignUpActivity.this, OTP_Activity.class));

               String full_name1 = full_name.getText().toString();
                String email1 = email.getText().toString();
                String mobile_no1 = mobile_no.getText().toString();

                if(full_name1.matches("")){
                    Toasty.error(GstReturnMainActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    full_name.setError("Please Enter Full name!");
                }  else if(email1.matches("")){
                    Toasty.error(GstReturnMainActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    email.setError("Enter Email");
                } else if(businessnatureStringName.matches("")){

                    Toasty.error(GstReturnMainActivity.this, "Select Business Nature!", Toasty.LENGTH_LONG).show();
                } else if(mobile_no1.length() != 10){
                    Toasty.error(GstReturnMainActivity.this, "All Fileds are reqired", Toast.LENGTH_SHORT, true).show();
                    mobile_no.setError("Mobile Number must be 10 digit");
                }
                else
                {
                        doSubmit( email1, mobile_no1,full_name1);


                }
            }
        });

    }



    //Get Business Nature List ro Spinner
    private void GetBusinessNatureList_BR() {
        progress_BnatureSpinner_GST.setVisibility(View.VISIBLE);

        businessnaturedata1.clear();
        businessnatureName.clear();
        businessnatureIdLst.clear();
        businessnatureSpinner.setAdapter(null);
        businessnatureStringName = null;

        // Log.d("checkss", " response = " + " " + get_contry_list_url);

        final String statement = "Select";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_business_nature_list_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("checkss", " response = " + " " + response);
                        try {

                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;
                            for (int i = 0; i < ja.length(); i++) {
                                jo = ja.getJSONObject(i);
                                businessnaturedata1.add(jo.getString("nature_name") + jo.getString("id"));
                                businessnatureIdLst.add(jo.getString("id"));
                            }

                            try {
                                for (int i = 0; i <  businessnaturedata1.size(); i++) {
                                    String temp;
                                    temp =  businessnaturedata1.get(i);
                                    temp = temp.replaceAll("[0-9]", "");
                                    businessnatureName.add(temp);
                                }

                                if ( businessnatureName.isEmpty()) {
                                    Toasty.warning(getApplicationContext(), "Business Nature is empty", Toast.LENGTH_SHORT).show();
                                } else {
                                    progress_BnatureSpinner_GST.setVisibility(View.GONE);//.setVisibility(View.GONE);
                                    final ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(GstReturnMainActivity.this, android.R.layout.simple_spinner_item,  businessnatureName); //selected item will look like a spinner set from XML
                                    spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    businessnatureSpinner.setAdapter(spinnerArrayAdapter1);

                                    businessnatureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                            position1 = position;
                                            try {
                                                String s =  businessnaturedata1.get(position1);
                                                s = s.replaceAll("[^\\d]", "");
                                                businessnature_id = Integer.decode(s);
                                            } catch (Exception e) {}


                                            businessnatureStringName =  businessnatureSpinner.getSelectedItem().toString().trim();

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                }

                                verify = true;
                                mail.setVisibility(View.VISIBLE);
                                progress_GSTmain.setVisibility(View.GONE);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(RegistrationActivity.this, "Submit Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("checkss", " response = " + " " + e.toString());
                            progress_BnatureSpinner_GST.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(RegistrationActivity.this, "Submit Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("checkss", " response = " + " " + error.toString());
                        progress_BnatureSpinner_GST.setVisibility(View.GONE);
                        //scrollViewMainLayout.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("condition", "GET");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(GstReturnMainActivity.this);
        requestQueue.add(stringRequest);
    }



    //Get Business Nature List ro Spinner
    private void GetGSTYPEList_GST() {
        progress_GSTTYPESpinner_GST.setVisibility(View.VISIBLE);

        GSTTYPEdata1.clear();
        GSTTYPEName.clear();
        GSTTYPEIdLst.clear();
        GSTTYPESpinner.setAdapter(null);
        GSTTYPEStringName = null;

        // Log.d("checkss", " response = " + " " + get_contry_list_url);

        final String statement = "Select";


        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_GSTTYPE_list_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("checkss", " response = " + " " + response);
                        try {

                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;
                            for (int i = 0; i < ja.length(); i++) {
                                jo = ja.getJSONObject(i);
                                GSTTYPEdata1.add(jo.getString("type_name") );
                                GSTTYPEIdLst.add(jo.getString("id"));
                            }

                            try {
                                for (int i = 0; i <  GSTTYPEdata1.size(); i++) {
                                    String temp;
                                    temp =  GSTTYPEdata1.get(i);
                                    // temp = temp.replaceAll("[0-9]", "");
                                    GSTTYPEName.add(temp);
                                }

                                if ( GSTTYPEName.isEmpty()) {
                                    Toasty.warning(getApplicationContext(), "Business Nature is empty", Toast.LENGTH_SHORT).show();
                                } else {
                                    progress_GSTTYPESpinner_GST.setVisibility(View.GONE);//.setVisibility(View.GONE);
                                    final ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(GstReturnMainActivity.this, android.R.layout.simple_spinner_item,  GSTTYPEName); //selected item will look like a spinner set from XML
                                    spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    GSTTYPESpinner.setAdapter(spinnerArrayAdapter1);

                                    GSTTYPESpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                            position2 = position;
                                            try {
                                                String s =  GSTTYPEIdLst.get(position2);
                                                s = s.replaceAll("[^\\d]", "");
                                                GSTTYPE_id = Integer.decode(s);

                                            } catch (Exception e) {}

                                            GetGSTTYPEPLAN_CHART(GSTTYPE_id);
                                            GSTTYPEStringName =  GSTTYPESpinner.getSelectedItem().toString().trim();

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                }

                                verify1 = true;
                                mail.setVisibility(View.VISIBLE);
                                progress_GSTmain.setVisibility(View.GONE);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(RegistrationActivity.this, "Submit Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("checkss", " response = " + " " + e.toString());
                            progress_GSTTYPESpinner_GST.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(RegistrationActivity.this, "Submit Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("checkss", " response = " + " " + error.toString());
                        progress_GSTTYPESpinner_GST.setVisibility(View.GONE);
                        //scrollViewMainLayout.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("condition", "GET");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(GstReturnMainActivity.this);
        requestQueue.add(stringRequest);
    }


    //get al registered info
    private void GetGSTTYPEPLAN_CHART(int typeid) {

        final String statement = "Select";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, get_GSTTYPE_list_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //   Toast.makeText(getApplicationContext(), "Response = "+" "+response, Toast.LENGTH_LONG).show();

                        String res = null;

                        try {
                            //JSONArray ja = new JSONArray(result);
                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;

                            //data = new String[ja.length()];

                            for (int i = 0; i < ja.length(); i++) {
                                jo = ja.getJSONObject(i);
                                //tire_compnay_id.add(jo.getString("tire_id"));
                                //tire_company_name.add(jo.getString("tire_compnay"));
                                //categoryid.add(jo.getString("cid"));

                                //res = jo.getString("Message");
                                charges=jo.getString("gst_rate");
                                GSTTYPEPLAN_AMT.setText("Rs.: "+jo.getString("gst_rate")+".00/-");
                                GSTTYPEPLAN_DESC.setText("Facility :"+jo.getString("type_desc"));

                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Toast.makeText(assign_tire_tag.this, "Submit Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                            //   progressMainLay.setVisibility(View.GONE);
                            //scrollViewMainLayout.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(assign_tire_tag.this, "Submit Error! " + error.toString(), Toast.LENGTH_SHORT).show();
                        //  progressMainLay.setVisibility(View.GONE);
                        // scrollViewMainLayout.setVisibility(View.VISIBLE);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("condition", "GET");
                params.put("id", ""+typeid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(GstReturnMainActivity.this);
        requestQueue.add(stringRequest);
    }



    //Form Submit
    private void   doSubmit(String  email,String  mobile_no,String full_name)
    {
        // progresssubmit.setVisibility(View.VISIBLE);
        submit.setEnabled(false);
        submit.setAlpha(0.5f);
        final String full_name1 = String.valueOf(full_name);

       final String email1 = String.valueOf(email);
        final String mobile_no1 = String.valueOf(mobile_no);

        final String businessnatureid = String.valueOf(businessnature_id);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_MAINSUBMIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("demooo",""+response);
                        //Toast.makeText(SignUpActivity.this, "Response : " + " " + response, Toast.LENGTH_LONG).show();

                        try{
                            JSONArray ja = new JSONArray(response);
                            JSONObject jo = null;

                            String gst_registration_no = null;
                            String amt = null;
                            String email = null;
                            String number = null;
                            String surl = null;
                            String message = null;
                            for(int i = 0; i<ja.length(); i++){
                                jo = ja.getJSONObject(i);
                                message = jo.getString("Message");
                                if(message.equals("1")) {
                                    Toasty.success(GstReturnMainActivity.this, "Data Saved.", Toast.LENGTH_SHORT, true).show();
                                    gst_registration_no = jo.getString("gst_return_no");
                                   // amt = jo.getString("payment_rs");
                                    email = jo.getString("EMAIL_ID");
                                    number = jo.getString("CUSTOMER_NUMBER");

                                }
                            }

                            //Toast.makeText(SignUpActivity.this, "Message : " + " " + custId, Toast.LENGTH_SHORT).show();

                            if(message.equals("1")){

                                if(gst_registration_no != null){
                                    Intent intent = new Intent(GstReturnMainActivity.this, paymentMainactivity.class);
                                    intent.putExtra("tran_id", gst_registration_no);
                                    intent.putExtra("AMOUNT", charges);
                                    intent.putExtra("EMAIL_ID", email);
                                    intent.putExtra("CUSTOMER_NUMBER", number);
                                    intent.putExtra("SURL", amt);
                                    intent.putExtra("FURL", amt);
                                    intent.putExtra("CURL", amt);
                                    startActivity(intent);
                                }

                            }

                            else {


                                new KAlertDialog(GstReturnMainActivity.this, KAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error!")
                                        .setContentText("There was an unexpected error, Please try again!")
                                        .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                            @Override
                                            public void onClick(KAlertDialog kAlertDialog) {
                                                kAlertDialog.dismiss();
                                            }
                                        })
                                        .confirmButtonColor(R.drawable.kalert_button_background)
                                        .show();
                            }

                            // progresssubmit.setVisibility(View.GONE);
                            submit.setEnabled(true);
                            submit.setAlpha(1);

                        } catch (JSONException e){
                            e.printStackTrace();

                            //Toast.makeText(SignUpActivity.this, "JSON Exception : " + " " + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d("EXCEPTION", "JSON Exception : " + " " + e.toString());

                            new KAlertDialog(GstReturnMainActivity.this, KAlertDialog.ERROR_TYPE)
                                    .setTitleText("Error!")
                                    .setContentText("Ops, Something went wrong!")
                                    .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                        @Override
                                        public void onClick(KAlertDialog kAlertDialog) {
                                            kAlertDialog.dismiss();
                                        }
                                    })
                                    .confirmButtonColor(R.drawable.kalert_button_background)
                                    .show();

                            // progresssubmit.setVisibility(View.GONE);
                            submit.setEnabled(true);
                            submit.setAlpha(1);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //Toast.makeText(SignUpActivity.this, "Volley Error : " + " " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d("EXCEPTION", "Volley Error : " + " " + error.toString());

                        new KAlertDialog(GstReturnMainActivity.this, KAlertDialog.ERROR_TYPE)
                                .setTitleText("Error!")
                                .setContentText("Can't communicate with server, Please try again.")
                                .setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                                    @Override
                                    public void onClick(KAlertDialog kAlertDialog) {
                                        kAlertDialog.dismiss();
                                    }
                                })
                                .confirmButtonColor(R.drawable.kalert_button_background)
                                .show();
                        submit.setEnabled(true);
                        submit.setAlpha(1);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("condition","IN");
                params.put("login_regi_no",loginId);
                params.put("business_nature_id",businessnatureid);
                params.put("plan_id",""+GSTTYPE_id);
                params.put("full_name",full_name1);
                params.put("email",email1);
                params.put("rs",charges);
                params.put("mobile_no",mobile_no1);
                params.put("status","pending");


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(GstReturnMainActivity.this);
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
