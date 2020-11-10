package com.aciv.parceros.Activities.Results;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aciv.parceros.R;

public class InputDescriptionActivity extends AppCompatActivity {
    private EditText descriptionET;
    private TextView counterTV;
    private Button doneB;
    private ImageButton backIB;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_description);

        descriptionET = findViewById(R.id.descriptionET);
        counterTV = findViewById(R.id.counterTV);
        doneB = findViewById(R.id.doneB);
        backIB = findViewById(R.id.backIB);

        context = this;

        if(getIntent().getExtras() != null){
            descriptionET.setText(getIntent().getStringExtra("description"));
        }

        descriptionET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changeFillState(s.toString().length());
            }
        });

        backIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        doneB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent response = new Intent();
                response.putExtra("description", descriptionET.getText().toString());
                setResult(RESULT_OK, response);
                finish();
            }
        });
    }

    private void changeFillState(int length){
        if(length <= 300){
            counterTV.setTextColor(ContextCompat.getColor(context, R.color.green));
        }else if(length <= 400){
            counterTV.setTextColor(ContextCompat.getColor(context, R.color.yellow));
        }else{
            counterTV.setTextColor(ContextCompat.getColor(context, R.color.red));
        }

        counterTV.setText(length + "/500");
    }
}
