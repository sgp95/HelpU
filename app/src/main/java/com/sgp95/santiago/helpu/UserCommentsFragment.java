package com.sgp95.santiago.helpu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sgp95.santiago.helpu.adapter.UserCommentsAdapter;
import com.sgp95.santiago.helpu.model.Comment;
import com.sgp95.santiago.helpu.model.Complain;
import com.squareup.picasso.Picasso;

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
    private Complain complain;
    private TextView userFullName, commentComplain, dateCreated,info,hour;
    private ImageView imgUser,imgRequest;

    private static final String ARG_USER_COMPLAIN = "userComplain";

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
        //Log.d("userComplain",complain.getComplain());
        super.onViewCreated(view, savedInstanceState);

        mFirebaseDataBase = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_user_comments) ;
        fabSend = (FloatingActionButton) view.findViewById(R.id.fab_send_comment);
        edtComment = (EditText) view.findViewById(R.id.input_comment);

        //Complain inside
        info = (TextView) view.findViewById(R.id.txt_info);
        hour = (TextView) view.findViewById(R.id.txt_hour);
        userFullName = (TextView) view.findViewById(R.id.txt_fullname);
        commentComplain = (TextView) view.findViewById(R.id.txt_complaint_area);
        dateCreated = (TextView) view.findViewById(R.id.txt_date);
        imgUser = (ImageView) view.findViewById(R.id.img_user);
        imgRequest = (ImageView) view.findViewById(R.id.img_complaint);

        commentList = new ArrayList<>();

        userFullname = getArguments().getString("userFullname");
        userCode = getArguments().getString("userCode");
        userImage = getArguments().getString("userImage");
        complainId = getArguments().getString("commentId");

        setCommentComplain(complain);

        setRecyclerView();

        updateCommentList();

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

                edtComment.setText("");
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
                //Toast.makeText(getActivity(),"envio exitoso",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(getActivity(),"envio Fallido",Toast.LENGTH_SHORT).show();
                Log.e("ErrorSentComment", "Upload Failed -> " + e);
            }
        });
    }

    public void setRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userCommentsAdapter = new UserCommentsAdapter(recyclerView,commentList,getContext());
        recyclerView.setAdapter(userCommentsAdapter);
    }

    public void updateCommentList(){
        mFirebaseDataBase.child("comment").child(complainId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                Log.d("listComments",comment.getCommentId()+" || "+comment.getComment()+" || "+comment.getUserCode());
                commentList.add(comment);
                userCommentsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setCommentComplain(Complain complain){
        if(!complain.getComplainImage().equals("null")){
            Picasso.with(getContext())
                    .load(complain.getComplainImage())
                    .into(imgRequest);
        }
        Picasso.with(getContext())
                .load(complain.getUserImg())
                .into(imgUser);
        info.setText(complain.getHeadquarter() + "/" + complain.getCategory());
        hour.setText(complain.getDateCreated().substring(11,16));
        commentComplain.setText(complain.getComplain());
        dateCreated.setText(complain.getDateCreated().substring(0,11));
        userFullName.setText(complain.getFullName());
    }

    public void setUserComplain(Complain complain){
        this.complain = complain;
    }
}
