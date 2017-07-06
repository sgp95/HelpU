package com.sgp95.santiago.helpu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sgp95.santiago.helpu.adapter.CommentAdpter;
import com.sgp95.santiago.helpu.model.Comment;
import com.sgp95.santiago.helpu.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hiraoka on 16/06/2017.
 */

public class CommentsFragment extends Fragment {
    private RecyclerView recyclerView;
    private CommentAdpter commentAdpter;
    private List<Comment> complaintList;
    //private NavigationView navigationView;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFirebaseInstance = FirebaseDatabase.getInstance();

        //Firebase Reference, work with this
        mFirebaseDatabase = mFirebaseInstance.getReference();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_comment);
        complaintList = new ArrayList<>();

        String userCode = getArguments().getString("userCode");
        Log.d("prueba", userCode);


        mFirebaseDatabase.child("student").child(userCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                TextView userFullName = (TextView) getActivity().findViewById(R.id.user_name_header);
                ImageView userImg = (ImageView) getActivity().findViewById(R.id.user_profile_img);
                userFullName.setText(user.getLastName()+ ' ' + user.getFirstName());
                Picasso.with(getContext())
                        .load(user.getImage())
                        .resize(300, 300)
                        .into(userImg);
                Log.d("prueba", "User name: " + user.getFirstName() + ", email " + user.getLastName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("prueba", "Failed to read value.", databaseError.toException());
            }
        });

        /*
        for (int i =0;i<6; i++){
            Comment comment = new Comment();
            comment.setComplaintId("edddddd");
            comment.setUserCode("142000"+i);
            comment.setComplain("Complain #"+i);
            comment.setDateCreated("Day "+i+" 24:00");
            comment.setComplainImage("www.img.com");
            commentList.add(comment);
        }
        */

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        commentAdpter = new CommentAdpter(recyclerView,complaintList,getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(commentAdpter);
        //commentAdpter.notifyDataSetChanged();
    }
}
