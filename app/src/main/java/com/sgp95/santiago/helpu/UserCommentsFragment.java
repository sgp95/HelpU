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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sgp95.santiago.helpu.adapter.UserCommentsAdapter;
import com.sgp95.santiago.helpu.model.Comment;
import com.sgp95.santiago.helpu.model.Complain;
import com.sgp95.santiago.helpu.model.User;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UserCommentsFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Comment> commentList;
    private UserCommentsAdapter userCommentsAdapter;
    private DatabaseReference mFirebaseDataBase,commentReference,comCounterReference,userReference;
    String fullnameuser,userCode,userImage,complainId,userComment,commentDate,pushKey;
    private FloatingActionButton fabSend;
    private EditText edtComment;
    private Comment commentToSend;
    private Complain complain;
    private TextView userFullName, commentComplain, dateCreated,info,hour;
    private ImageView imgUser,imgRequest;
    private ProgressBar progressBar,progressBar3;
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
        recyclerView =  view.findViewById(R.id.rv_user_comments) ;
        fabSend = view.findViewById(R.id.fab_send_comment);
        edtComment = view.findViewById(R.id.input_comment);

        //Complain inside
        info = view.findViewById(R.id.txt_info);
        hour = view.findViewById(R.id.txt_hour);
        userFullName = view.findViewById(R.id.txt_fullname);
        commentComplain =  view.findViewById(R.id.txt_complaint_area);
        dateCreated = view.findViewById(R.id.txt_date);
        imgUser = view.findViewById(R.id.img_user);
        imgRequest = view.findViewById(R.id.img_complaint);
        progressBar = view.findViewById(R.id.progressBar2);
        progressBar3 = view.findViewById(R.id.progressBar3);
        commentList = new ArrayList<>();

        fullnameuser = getArguments().getString("fullnameuser");
        userCode = getArguments().getString("userCode");
        userImage = getArguments().getString("userImage");
        complainId = getArguments().getString("commentId");

        userReference = mFirebaseDataBase.child("student").child(complain.getUserCode());



        Picasso.with(getContext())
                .load(getArguments().getString("complainImage"))
                .into(imgRequest, new Callback(){
                    @Override
                    public void onSuccess() {
                        progressBar3.setVisibility(View.GONE);
                        imgRequest.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onError() {
                        progressBar3.setVisibility(View.GONE);
                    }
                });

        setCommentComplain(complain);

        setRecyclerView();

        updateCommentList();

        progressBar.setVisibility(View.VISIBLE);
        Picasso.with(getContext())
                .load(userImage)
                .into(imgUser, new Callback(){
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                        imgUser.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onError() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
        commentReference = mFirebaseDataBase.child("comment").child(complainId);
        comCounterReference = mFirebaseDataBase.child("complaint").child(complainId);
        Log.d("rastro",complain.getUserCode());

        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushKey = commentReference.push().getKey();

                userComment = edtComment.getText().toString();
                commentDate = getCurrentTime();

                commentToSend = new Comment();
                commentToSend.setCommentId(pushKey);
                commentToSend.setUserName(fullnameuser);
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
                updateCommentCounter();
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
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Picasso.with(getContext())
                        .load(user.getImage())
                        .into(imgUser);

                userFullName.setText(user.getLastName() + ' ' + user.getFirstName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(!complain.getComplainImage().equals("null")){
            Picasso.with(getContext())
                    .load(complain.getComplainImage())
                    .into(imgRequest);
        }
        info.setText(complain.getHeadquarter() + "/" + complain.getCategory());
        hour.setText(complain.getDateCreated().substring(11,16));
        commentComplain.setText(complain.getComplain());
        dateCreated.setText(complain.getDateCreated().substring(0,11));
    }

    public void setUserComplain(Complain complain){
        this.complain = complain;
    }

    public void updateCommentCounter(){
        int counter = Integer.parseInt(complain.getComCounter());
        //Log.d("CommentCounter",counter+"");
        counter = counter+1;
        //Log.d("CommentCounter",counter+"");
        //Log.d("CommentCounter",String.valueOf(counter));
        complain.setComCounter(String.valueOf(counter));
        comCounterReference.child("comCounter").setValue(String.valueOf(counter)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("CommentCounter","counter updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("CommentCounter", "Counter Update Failed -> " + e);
            }
        });
    }
}
