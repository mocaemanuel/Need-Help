package com.brainiac.mocae.needhelp;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;


public class AskHelpActivity extends AppCompatActivity {
    private EditText txtName;
    private EditText txtDescription;
    private EditText txtPeople;
    private EditText txtStartDate;
    private EditText txtEndDate;
    private EditText txtTag;
    private EditText txtLocation;
    private CheckBox txtLocationCheckBox;
    private static Boolean isShowingDate = false;
    private View.OnClickListener dateClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_help);

        //GPSTracker mGPS = new GPSTracker(this);

        txtName = (EditText) findViewById(R.id.nameEditText);
        txtDescription = (EditText) findViewById(R.id.descriptionEditText);
        txtPeople = (EditText) findViewById(R.id.minRequiredPeopleEditText);
        txtPeople.setInputType(InputType.TYPE_CLASS_NUMBER);
        txtStartDate = (EditText) findViewById(R.id.dateEditText);
        txtTag = (EditText) findViewById(R.id.tagEditText);
        txtLocation = (EditText) findViewById(R.id.locationEditText);
        txtLocationCheckBox = (CheckBox) findViewById(R.id.autoLocationCheckBox);
        dateClickListener = new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    DatePickerFragment calendarView = new DatePickerFragment();
                    calendarView.SetOnDateHandler(new OnDateSet() {
                        @Override
                        public void OnNewDateSet(final int y, final int m, final int d) {
                            if (isShowingDate == true)
                                return;

                            isShowingDate = true;
                            TimePickerFragment timePicker = new TimePickerFragment();
                            timePicker.show(getSupportFragmentManager(), "timePicker");

                            timePicker.SetOnTimeHandler(new OnTimeSet() {
                                @Override
                                public void OnNewTimeSet(int h, int minute) {
                                    String str = String.format("%02d/%02d/%d %d:%02d", d, m, y, h, minute);
                                    ((EditText) v).setText(str);
                                    isShowingDate = false;
                                }
                            });
                        }
                    });
                    calendarView.show(getSupportFragmentManager(), "datePicker");
                }
            };
        txtStartDate.setOnClickListener(dateClickListener);
        txtEndDate = (EditText) findViewById(R.id.endDateEditText);
        txtEndDate.setOnClickListener(dateClickListener);
        }
    private boolean validateEditText (EditText txt) {
        if (txt.getText().toString().length() == 0) {
            txt.setError("fill in");
            return false;
        }

        if (txtLocationCheckBox.isChecked()){
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);

        }

        return true;
    }

    public void onBtnSaveHelpRequest(View view) {
        if (validateEditText(txtName) == false ||
                validateEditText(txtDescription) == false ||
                validateEditText(txtPeople) == false ||
                validateEditText(txtStartDate) == false ||
                validateEditText(txtTag) == false) {
            return ;
        }

        HelpRequest helpRequest = new HelpRequest();
        helpRequest.Name = txtName.getText().toString();
        helpRequest.Description = txtDescription.getText().toString();
        helpRequest.NumberOfPeople = Integer.parseInt(txtPeople.getText().toString());
        helpRequest.StartDate = txtStartDate.getText().toString();
        helpRequest.EndDate = txtEndDate.getText().toString();
        helpRequest.Tag = txtTag.getText().toString();
        helpRequest.Location = txtLocation.getText().toString();

        DataStorage.getInstance().saveRequest(helpRequest);

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }
}
