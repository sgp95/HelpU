package com.sgp95.santiago.helpu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

public class CommentsFragment extends Fragment implements CommentAdpter.MyItemClickListener {
    private RecyclerView recyclerView;
    private CommentAdpter commentAdpter;
    private List<Complain> complaintList;
    private List<Complain> complaintListReverse;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private Bundle commentsData;
    String userfullname, userImage, userCode;




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


        userCode = getArguments().getString("userCode");
        Log.d("prueba", userCode);


        mFirebaseDatabase.child("student").child(userCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String fullName = user.getLastName()+ ", " + user.getFirstName();
                setUserData(getArguments().getString("userCode"),fullName,user.getImage());
                TextView userFullName = (TextView) getActivity().findViewById(R.id.user_name_header);
                ImageView userImg = (ImageView) getActivity().findViewById(R.id.user_profile_img);
                userfullname = user.getLastName().toString() + ' ' + user.getFirstName().toString();
                userFullName.setText(userfullname);
                Picasso.with(getContext())
                        .load(user.getImage())
                        .resize(60, 60)
                        .into(userImg);
                Log.d("prueba", "User name: " + user.getFirstName() + ", email " + user.getLastName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("prueba", "Failed to read value.", databaseError.toException());
            }
        });

        complaintList = new ArrayList<>();
        final ArrayList<Complain> complaintListReverse = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentAdpter = new CommentAdpter(recyclerView, complaintList,getContext());
        commentAdpter.setOnItemClickListener(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(commentAdpter);




        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Cargando Quejas");
        mProgressDialog.show();


        mFirebaseDatabase.child("complaint").orderByChild("complainId").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {


                Complain complain = dataSnapshot.getValue(Complain.class);
                // System.out.println(dataSnapshot.getKey() + " was " + category.getName());

                    if(complain.getPrivacy().equals("Publico")) {

                    final Complain objcomplain = new Complain();

                    objcomplain.setCategory(complain.getCategory());
                    objcomplain.setComplain(complain.getComplain());
                    objcomplain.setComplainImage(complain.getComplainImage());
                    objcomplain.setComplaintId(complain.getComplaintId());
                    objcomplain.setDateCreated(complain.getDateCreated());
                    objcomplain.setPrivacy(complain.getPrivacy());
                    objcomplain.setState(complain.getState());
                    objcomplain.setUserCode(complain.getUserCode());
                    objcomplain.setHeadquarter(complain.getHeadquarter());
                    objcomplain.setmFirebaseDatabase(mFirebaseDatabase);


                    complaintList.add(objcomplain);
                   //     ListIterator iter = complaintList.listIterator(complaintList.size());

                     /*   Collections.sort(complaintList, new Comparator<Complain>() {
                            @Override
                            public int compare(Complain p1, Complain p2) {
                                return new Integer(p1.getDateCreated()).compareTo(new Integer(p2.getDateCreated()));
                            }
                        });
                        for (Complain lteam : complaintList) {
                            complaintListReverse.add(objcomplain);
                        } */

                        if (dataSnapshot.exists()) {
                            mProgressDialog.dismiss();
                            commentAdpter.notifyDataSetChanged();
                        }

                }
                /*
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                commentAdpter = new CommentAdpter(recyclerView,complaintList,getContext());
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(commentAdpter);
                */
                    mProgressDialog.dismiss();

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
                mProgressDialog.dismiss();
            }

            // ...
        });


    }

    public void setUserData(String userCode,String userFullName, String userImage){
        this.userCode = userCode;
        this.userfullname = userFullName;
        this.userImage = userImage;
    }

    @Override
    public void onItemClick(Complain complain) {
        commentsData = new Bundle();
        commentsData.putString("fullnameuser",userfullname);
        commentsData.putString("userCode",userCode);
        commentsData.putString("userImage",userImage);
        commentsData.putString("commentId",complain.getComplaintId());
        commentsData.putString("category",complain.getCategory());
        commentsData.putString("headquarter",complain.getHeadquarter());
        commentsData.putString("complainImage",complain.getComplainImage());

        UserCommentsFragment userCommentsFragment = new UserCommentsFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        userCommentsFragment.setArguments(commentsData);
        userCommentsFragment.setUserComplain(complain);
        //ft.add(R.id.content,userCommentsFragment);
        ft.replace(R.id.content,userCommentsFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
