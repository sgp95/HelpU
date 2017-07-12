package com.sgp95.santiago.helpu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sgp95.santiago.helpu.adapter.UserCommentsAdapter;
import com.sgp95.santiago.helpu.model.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UserCommentsFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Comment> commentList;
    private UserCommentsAdapter userCommentsAdapter;
    private DatabaseReference mFirebaseDataBase,commentReference;
    String userFullname,userCode,userImage,complainId,userComment,commentDate,pushKey;
    private FloatingActionButton fabSend;
    private EditText edtComment;
    private Comment commentToSend;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_comments,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFirebaseDataBase = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_user_comments) ;
        fabSend = (FloatingActionButton) view.findViewById(R.id.fab_send_comment);
        edtComment = (EditText) view.findViewById(R.id.input_comment);

        commentList = new ArrayList<>();

        userFullname = getArguments().getString("userFullname");
        userCode = getArguments().getString("userCode");
        userImage = getArguments().getString("userImage");
        complainId = getArguments().getString("commentId");

        commentReference = mFirebaseDataBase.child("comment").child(complainId);
        Log.d("UserComment",userFullname+"---"+userCode+"---"+complainId);

        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushKey = commentReference.push().getKey();

                userComment = edtComment.getText().toString();
                commentDate = getCurrentTime();

                commentToSend = new Comment();
                commentToSend.setCommentId(pushKey);
                commentToSend.setUserName(userFullname);
                commentToSend.setUserCode(userCode);
                commentToSend.setImage(userImage);
                commentToSend.setComment(userComment);
                commentToSend.setCommentDate(commentDate);
                sendComment(commentToSend);
            }
        });

    }

    public String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String date = dateFormat.format(calendar.getTime());
        return date;
    }

    public void sendComment(Comment comment){
        commentReference.child(comment.getCommentId()).setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(),"envio exitoso",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),"envio Fallido",Toast.LENGTH_SHORT).show();
                Log.e("ErrorSentComment", "Upload Failed -> " + e);
            }
        });
    }
}