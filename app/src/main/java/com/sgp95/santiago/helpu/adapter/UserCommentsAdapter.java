package com.sgp95.santiago.helpu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sgp95.santiago.helpu.R;
import com.sgp95.santiago.helpu.model.Comment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Hiraoka on 11/07/2017.
 */

public class UserCommentsAdapter extends SelectableAdapter<UserCommentsAdapter.ViewHolder>{
    private Context context;
    private List<Comment> commentList;

    public UserCommentsAdapter(RecyclerView recyclerView, List<Comment> commentList, Context context) {
        super(recyclerView);
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(commentList.get(getItemCount() - 1 - position));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends SelectableAdapter.ViewHolder{

        ImageView userImageProfile;
        TextView  txtUserFullName, txtUserCode,txtUserComment,txtCommentDate,hour;
        ProgressBar progressBar;
        public ViewHolder(View itemView) {
            super(itemView);
            userImageProfile = (ImageView)itemView.findViewById(R.id.img_user_comments_profile);
            txtUserFullName = (TextView) itemView.findViewById(R.id.txt_user_comments_full_name) ;
            txtUserCode = (TextView) itemView.findViewById(R.id.txt_user_comments_code);
            txtUserComment = (TextView) itemView.findViewById(R.id.txt_user_comments_comment);
            txtCommentDate = (TextView) itemView.findViewById(R.id.txt_user_comments_date);
            hour = (TextView) itemView.findViewById(R.id.hourcomment);
            progressBar = (ProgressBar)itemView.findViewById(R.id.progressBar5);
        }

        public void bind(Comment comment){

            progressBar.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(comment.getImage())
                    .into(userImageProfile, new Callback(){
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                            userImageProfile.setVisibility(View.VISIBLE);
                        }
                        @Override
                        public void onError() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });

            txtUserFullName.setText(comment.getUserName());
            txtUserCode.setText(comment.getUserCode());
            txtUserComment.setText(comment.getComment());
            txtCommentDate.setText(comment.getCommentDate().substring(0,11).replace("-","/").replace("-","/").replace("-","/"));
            hour.setText(comment.getCommentDate().substring(11,16));
        }
    }
}
