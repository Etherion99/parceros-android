package com.aciv.parceros.Activities.Results;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.aciv.parceros.Adapters.SimpleListAdapter;
import com.aciv.parceros.Models.Local.MultipleSelectText;
import com.aciv.parceros.Models.Local.SelectText;
import com.aciv.parceros.Models.Local.SingleSelectText;
import com.aciv.parceros.R;

import java.util.List;

public class PickWorkHoursActivity extends AppCompatActivity {
    private ImageButton backIB;
    private ListView fromHourLV, toHourLV;
    private Button doneB;

    private Context context;
    private List<SelectText> fromHours, toHours;
    private SimpleListAdapter fromHourAdapter, toHourAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_work_hours);

        backIB = findViewById(R.id.backIB);
        fromHourLV = findViewById(R.id.fromHourLV);
        toHourLV = findViewById(R.id.toHourLV);
        doneB = findViewById(R.id.doneB);

        context = this;

        fromHours = SelectText.generateList(getResources().getStringArray(R.array.clock_hours));
        toHours = SelectText.generateList(getResources().getStringArray(R.array.clock_hours));

        fromHours.get(48).setSelected(true);
        toHours.get(48).setSelected(true);

        fromHourAdapter = new SimpleListAdapter(context, fromHours, Gravity.CENTER);
        toHourAdapter = new SimpleListAdapter(context, toHours, Gravity.CENTER);

        fromHourLV.setAdapter(fromHourAdapter);
        toHourLV.setAdapter(toHourAdapter);

        fromHourLV.setSelection(44);
        toHourLV.setSelection(44);

        fromHourLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SingleSelectText.deselect(fromHours);
                fromHours.get(position).setSelected(true);

                fromHourAdapter.notifyDataSetChanged();
            }
        });

        toHourLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SingleSelectText.deselect(toHours);
                toHours.get(position).setSelected(true);

                toHourAdapter.notifyDataSetChanged();
            }
        });

        doneB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SingleSelectText.getSelected(toHours) > SingleSelectText.getSelected(fromHours)){
                    Intent response = new Intent();

                    response.putExtra("fromHour", SingleSelectText.getSelectedName(fromHours));
                    response.putExtra("toHour", SingleSelectText.getSelectedName(toHours));

                    setResult(RESULT_OK, response);
                    finish();
                }else{
                    Toast.makeText(context, "Tu horario de servicio debe ser de mínimo 15 minutos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
