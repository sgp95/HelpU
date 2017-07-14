package com.sgp95.santiago.helpu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.sgp95.santiago.helpu.R;
import com.sgp95.santiago.helpu.model.Complain;
import com.sgp95.santiago.helpu.model.User;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class CommentAdpter extends SelectableAdapter<CommentAdpter.ViewHolder> {

    private MyItemClickListener myItemClickListener;
    private List<Complain> complainList;
    private Context context;
    private DatabaseReference mFirebaseDatabase;

    public CommentAdpter(RecyclerView recyclerView, List<Complain> complainList, Context context){
        super(recyclerView);
        this.complainList = complainList;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.complain,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

               holder.bind(complainList.get(position));
    }


    @Override
    public int getItemCount() {
        return complainList.size();
    }


    public void setOnItemClickListener(MyItemClickListener listener) {
        this.myItemClickListener = listener;
    }

    public class ViewHolder extends SelectableAdapter.ViewHolder {

        TextView userFullName, complain, dateCreated,info,hour;
        ImageView imgUser,imgRequest;
        ProgressBar progressBar,progressBar3;

        public ViewHolder(View itemView) {
            super(itemView);
            info = (TextView) itemView.findViewById(R.id.txt_info);
            hour = (TextView) itemView.findViewById(R.id.txt_hour);
            userFullName = (TextView) itemView.findViewById(R.id.txt_fullname);
            complain = (TextView) itemView.findViewById(R.id.txt_complaint_area);
            dateCreated = (TextView) itemView.findViewById(R.id.txt_date);
            progressBar = (ProgressBar)itemView.findViewById(R.id.progressBar2);
            progressBar3 = (ProgressBar)itemView.findViewById(R.id.progressBar3);
            imgUser = (ImageView) itemView.findViewById(R.id.img_user);
            imgRequest = (ImageView) itemView.findViewById(R.id.img_complaint);

            setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick() {
                    if (myItemClickListener != null){
                        myItemClickListener.onItemClick(complainList.get(getItemCount() - 1 - getAdapterPosition()));
                    }
                }
            });

        }

        public void bind(Complain comment){


            if(!comment.getComplainImage().equals("null")) {

                progressBar.setVisibility(View.VISIBLE);
                Picasso.with(context)
                        .load(comment.getComplainImage())
                        .into(imgRequest, new Callback(){
                                    @Override
                                    public void onSuccess() {
                                        progressBar.setVisibility(View.GONE);
                                        imgRequest.setVisibility(View.VISIBLE);
                                    }
                                    @Override
                                    public void onError() {
                                        progressBar.setVisibility(View.GONE);
                                    }
                        });


            }

            mFirebaseDatabase = comment.getmFirebaseDatabase();
            mFirebaseDatabase.child("student").child(comment.getUserCode()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    progressBar3.setVisibility(View.VISIBLE);
                    Picasso.with(context)
                            .load(user.getImage())
                            .into(imgUser, new Callback(){
                                @Override
                                public void onSuccess() {
                                    progressBar3.setVisibility(View.GONE);
                                    imgUser.setVisibility(View.VISIBLE);
                                }
                                @Override
                                public void onError() {
                                    progressBar3.setVisibility(View.GONE);
                                }
                            });

                    userFullName.setText(user.getLastName() + " " + user.getFirstName());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("prueba", "Failed to read value.", databaseError.toException());
                }
            });



            info.setText(comment.getHeadquarter() + "/" + comment.getCategory());

            complain.setText(comment.getComplain());
            dateCreated.setText(comment.getDateCreated().substring(0,11));
            hour.setText(comment.getDateCreated().substring(11,16));
        }
    }

    public interface MyItemClickListener {
        void onItemClick(Complain complein);
    }

}
