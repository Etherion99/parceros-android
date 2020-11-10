package com.aciv.parceros.Fragments.Partnert;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.aciv.parceros.Adapters.ShowCommentAdapter;
import com.aciv.parceros.Interfaces.RetrofitInterface;
import com.aciv.parceros.Models.Database.Comment;
import com.aciv.parceros.Models.Database.User;
import com.aciv.parceros.R;
import com.aciv.parceros.Tools.RetrofitTools;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowCommentsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_partner_show_comments, container, false);

        final ViewPager2 commentsVP = root.findViewById(R.id.commentsVP);
        final ImageButton prevCommentIB = root.findViewById(R.id.prevCommentIB);
        final ImageButton nextCommentIB = root.findViewById(R.id.nextCommentIB);

        final Context context = getActivity();

        RetrofitInterface retrofit = RetrofitTools.getInterface();

        Call<ArrayList<Comment>> showCommentsCall = retrofit.showCommentsByUserCreatedTo(8);

        showCommentsCall.enqueue(new Callback<ArrayList<Comment>>() {
            @Override
            public void onResponse(Call<ArrayList<Comment>> call, Response<ArrayList<Comment>> response) {
                if(response.body() != null){
                    final List<Comment> comments = new ArrayList<>();

                    comments.addAll(response.body());

                    Log.e("comments", new Gson().toJson(comments));

                    commentsVP.setAdapter(new ShowCommentAdapter(context, comments));

                    commentsVP.setUserInputEnabled(false);

                    prevCommentIB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(commentsVP.getCurrentItem()-1 >= 0)
                                commentsVP.setCurrentItem(commentsVP.getCurrentItem()-1);

                        }
                    });

                    nextCommentIB.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(commentsVP.getCurrentItem()+1 <= comments.size()-1)
                                commentsVP.setCurrentItem(commentsVP.getCurrentItem()+1);
                        }
                    });
                }else{
                    Toast.makeText(context, "error comments", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Comment>> call, Throwable t) {
                Log.e("comments error", t.getMessage());
            }
        });





        return root;
    }
}
