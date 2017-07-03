package com.sgp95.santiago.helpu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
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
    List<Comment> commentList;
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

        mFirebaseDatabase = mFirebaseInstance.getReference("student");

        String userCode = getArguments().getString("userCode");
        Log.d("prueba", userCode);

        mFirebaseDatabase.child(userCode).addValueEventListener(new ValueEventListener() {
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



        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_comment);

        commentList = new ArrayList<>();
        for (int i =0;i<6; i++){
            Comment comment = new Comment();
            comment.setId((long) i);
            comment.setUsername("User #"+i);
            comment.setComplain("Complain #"+i);
            comment.setDate("Day "+i+" 24:00");
            comment.setSolution("Esta es la solucion de prueba #"+i);
            commentList.add(comment);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        commentAdpter = new CommentAdpter(recyclerView,commentList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(commentAdpter);
        commentAdpter.notifyDataSetChanged();

    }
}
