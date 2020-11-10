package com.aciv.parceros.Activities.Results;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.aciv.parceros.Adapters.SimpleListAdapter;
import com.aciv.parceros.Models.Local.SelectText;
import com.aciv.parceros.Models.Local.SingleSelectText;
import com.aciv.parceros.R;

import java.util.List;

public class PickRestBreaksActivity extends AppCompatActivity {
    private ImageButton backIB;
    private Button doneB;
    private ListView restBreaksLV;

    private Context context;
    List<SelectText> restBreaks;
    private SimpleListAdapter restBreaksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_rest_breaks);

        backIB = findViewById(R.id.backIB);
        doneB = findViewById(R.id.doneB);
        restBreaksLV = findViewById(R.id.restBreaksLV);

        context = this;

        restBreaks = SelectText.generateList(getResources().getStringArray(R.array.rest_breaks));

        if(getIntent() != null)
            if(getIntent().getIntExtra("rest_break", -1) != -1)
                restBreaks.get(getIntent().getIntExtra("rest_break", -1)).setSelected(true);

        restBreaksAdapter = new SimpleListAdapter(context, restBreaks, Gravity.CENTER);
        restBreaksLV.setAdapter(restBreaksAdapter);

        restBreaksLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SingleSelectText.deselect(restBreaks);
                restBreaks.get(position).setSelected(true);

                restBreaksAdapter.notifyDataSetChanged();
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
                response.putExtra("rest_break", SingleSelectText.getSelected(restBreaks));

                setResult(RESULT_OK, response);
                finish();
            }
        });
    }
}
