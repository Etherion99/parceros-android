package com.aciv.parceros.Activities.Results;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.aciv.parceros.Adapters.SimpleListAdapter;
import com.aciv.parceros.Models.Database.ScheduleDay;
import com.aciv.parceros.Models.Local.MultipleSelectText;
import com.aciv.parceros.Models.Local.SelectText;
import com.aciv.parceros.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PickWorkDaysActivity extends AppCompatActivity {
    private ListView daysLV;
    private Button doneB;
    private ImageButton backIB;

    private Context context;
    private List<SelectText> days;
    private SimpleListAdapter daysAdapter;
    private final int PICK_HOURS_CODE = 18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_work_days);

        daysLV = findViewById(R.id.daysLV);
        doneB = findViewById(R.id.doneB);
        backIB = findViewById(R.id.backIB);

        context = this;

        days = SelectText.generateList(getResources().getStringArray(R.array.days));
        daysAdapter = new SimpleListAdapter(context, days, View.TEXT_ALIGNMENT_TEXT_START);

        daysLV.setAdapter(daysAdapter);

        daysLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == days.size()-1){
                    MultipleSelectText.selectAll(days, !days.get(days.size()-1).isSelected());
                }else{
                    days.get(position).setSelected(!days.get(position).isSelected());
                }

                daysAdapter.notifyDataSetChanged();
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
                if(MultipleSelectText.isOneSelectedAtLeast(days)){
                    startActivityForResult(new Intent(context, PickWorkHoursActivity.class), PICK_HOURS_CODE);
                }else{
                    Toast.makeText(context, "Selecciona mínimo un día", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case PICK_HOURS_CODE:
                switch(resultCode){
                    case RESULT_OK:
                        if (data != null) {
                            ArrayList<Integer> daysList = MultipleSelectText.getSelecteds(days);
                            daysList.remove(Integer.valueOf(7));

                            List<ScheduleDay> scheduleDayList = new ArrayList<>();

                            for(Integer day: daysList){
                                scheduleDayList.add(new ScheduleDay(day));
                            }

                            data.putExtra("days", new Gson().toJson(scheduleDayList));

                            setResult(RESULT_OK, data);
                            finish();
                        }
                        break;
                    case RESULT_CANCELED:
                        break;
                }
                break;
        }
    }
}
