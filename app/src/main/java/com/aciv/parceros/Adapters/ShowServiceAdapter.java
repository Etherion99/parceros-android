package com.aciv.parceros.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.aciv.parceros.Interfaces.RetrofitInterface;
import com.aciv.parceros.Models.Database.APIResponse;
import com.aciv.parceros.Models.Database.Reaction;
import com.aciv.parceros.Models.Database.Service;
import com.aciv.parceros.Models.Database.User;
import com.aciv.parceros.R;
import com.aciv.parceros.Tools.RetrofitTools;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowServiceAdapter extends ArrayAdapter<Service> {
    private Context context;
    private List<Service> data;
    private RetrofitInterface retrofit = RetrofitTools.getInterface();

    public ShowServiceAdapter(Context context, List<Service> data) {
        super(context, 0, data);

        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_show_service, parent, false);
        }

        final Service service = data.get(position);

        TextView nameTV = convertView.findViewById(R.id.nameTV);
        TextView costTV = convertView.findViewById(R.id.costTV);
        TextView durationTV = convertView.findViewById(R.id.durationTV);
        ImageButton likeIB = convertView.findViewById(R.id.likeIB);
        ImageButton dislikeIB = convertView.findViewById(R.id.dislikeIB);

        if(service != null){
            nameTV.setText(service.getName());
            costTV.setText("$" + service.getCost());

            String[] durationParts = service.getDuration().split(":");

            durationTV.setText(durationParts[0] + "H - " + durationParts[1] + "M");

            Drawable likeDrawable = ContextCompat.getDrawable(context, R.drawable.ic_like_off);
            Drawable dislikeDrawable = ContextCompat.getDrawable(context, R.drawable.ic_dislike_off);

            if(service.getReactions().size() > 0){
                if(service.getReactions().get(0).isLike())
                    likeDrawable = ContextCompat.getDrawable(context, R.drawable.ic_like_on);
                else
                    dislikeDrawable = ContextCompat.getDrawable(context, R.drawable.ic_dislike_on);
            }

            likeIB.setImageDrawable(likeDrawable);
            dislikeIB.setImageDrawable(dislikeDrawable);

            likeIB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(service.getReactions().size() == 0){
                        Reaction newReaction = new Reaction(9, service.getId(), true);

                        createReaction(position, newReaction);
                    }else{
                        Reaction reaction = service.getReactions().get(0);

                        if(reaction.isLike()){
                            deleteReaction(reaction.getId());
                            service.getReactions().remove(0);
                        }else{
                            reaction.setLike(true);
                            updateReaction(reaction);
                        }
                    }
                }
            });

            dislikeIB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(service.getReactions().size() == 0){
                        Reaction newReaction = new Reaction(9, service.getId(), false);

                        createReaction(position, newReaction);
                    }else{
                        Reaction reaction = service.getReactions().get(0);

                        if(reaction.isLike()){
                            reaction.setLike(false);
                            updateReaction(reaction);
                        }else{
                            deleteReaction(reaction.getId());
                            service.getReactions().remove(0);
                        }
                    }
                }
            });
        }
        return convertView;
    }

    private void createReaction(final int serviceIndex, final Reaction reaction){
        Call<APIResponse> createReactionCall = retrofit.createServiceReaction(reaction);

        createReactionCall.clone().enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse = response.body();

                if(apiResponse.isOk()){
                    reaction.setId(Long.parseLong(apiResponse.getMessage()));
                    data.get(serviceIndex).getReactions().add(reaction);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Log.e("reaction create error", t.toString());
            }
        });
    }

    private void updateReaction(final Reaction reaction){
        Call<APIResponse> updateReactionCall = retrofit.updateServiceReaction(reaction);

        updateReactionCall.clone().enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse = response.body();

                if(apiResponse.isOk()){
                    Log.e("update status", apiResponse.getMessage());
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Log.e("reaction update error", t.toString());
            }
        });

        /*Service service = data.get(serviceIndex);
        int action = -1;

        if(service.getReactions().get(0) == null)
            action = 2;
        else
            if(service.getReactions().get(0).isLike())
                action = 1;
            else
                action = 0;

        if(reactionId == -1){
            Call<APIResponse> createReactionCall = retrofit.createServiceReaction(new Reaction(9, service.getId(), action == 1));

            createReactionCall.clone().enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    APIResponse apiResponse = response.body();

                    if(apiResponse.isOk()){
                        Log.e("update status", apiResponse.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {

                }
            });
        }else{

        }*/
    }

    private void deleteReaction(long id){
        Log.e("deleting...", id+"");
        Call<APIResponse> createReactionCall = retrofit.deleteServiceReaction(id);

        createReactionCall.clone().enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                APIResponse apiResponse = response.body();

                Log.e("delete response", new Gson().toJson(apiResponse));

                if(apiResponse.isOk()){
                    Log.e("delete status", apiResponse.getMessage());
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                Log.e("reaction delete error", t.toString());
            }
        });
    }
}
