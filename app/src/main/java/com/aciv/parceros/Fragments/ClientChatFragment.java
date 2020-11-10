package com.aciv.parceros.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aciv.parceros.Adapters.ChatAdapter;
import com.aciv.parceros.Models.Firebase.Chat;
import com.aciv.parceros.Models.Firebase.Message;
import com.aciv.parceros.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ClientChatFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Chat> chats;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_client_chat, container, false);

        final ListView chatsLV = root.findViewById(R.id.chatsLV);

        chats = new ArrayList<>();

        db.collection("chat")
                .whereArrayContains("users", "1")
                .orderBy("last-date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> chatsTask) {
                        if(chatsTask.isSuccessful()){
                            for (QueryDocumentSnapshot chatData: chatsTask.getResult()) {
                                chats.add(new Chat(chatData.getId(), new Message("", chatData.getData().get("last-msg").toString(), null)));
                            }

                            chatsLV.setAdapter(new ChatAdapter(getActivity(), chats));
                        }else{
                            Log.e("Chats", "Error al obtener chats");
                        }
                    }
                });

        return root;
    }
}
