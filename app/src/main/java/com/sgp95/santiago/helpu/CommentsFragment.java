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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sgp95.santiago.helpu.adapter.CommentAdpter;
import com.sgp95.santiago.helpu.model.Complain;
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
    private List<Complain> complaintList;
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
        mFirebaseDatabase = mFirebaseInstance.getReference();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_comment);
        complaintList = new ArrayList<>();
        final List<Complain> commentList;

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


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("complaint");
        complaintList = new ArrayList<>();
        ref.orderByChild("category").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Complain complain = dataSnapshot.getValue(Complain.class);
                // System.out.println(dataSnapshot.getKey() + " was " + category.getName());

                if(complain.getPrivacy().equals("Publico")) {

                    Complain objcomplain = new Complain();

                    objcomplain.setCategory(complain.getCategory());
                    objcomplain.setComplain(complain.getComplain());
                    objcomplain.setComplainImage(complain.getComplainImage());
                    objcomplain.setComplaintId(complain.getComplaintId());
                    objcomplain.setDateCreated(complain.getDateCreated());
                    objcomplain.setPrivacy(complain.getPrivacy());
                    objcomplain.setState(complain.getState());
                    objcomplain.setUserCode(complain.getUserCode());
                    complaintList.add(objcomplain);
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                commentAdpter = new CommentAdpter(recyclerView,complaintList,getContext());
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(commentAdpter);
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

            // ...
        });


    }
}
