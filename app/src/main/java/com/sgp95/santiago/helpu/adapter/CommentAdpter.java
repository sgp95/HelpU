package com.sgp95.santiago.helpu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sgp95.santiago.helpu.R;
import com.sgp95.santiago.helpu.model.Comment;

import java.util.List;


public class CommentAdpter extends SelectableAdapter<CommentAdpter.ViewHolder> {

    private MyItemClickListener myItemClickListener;
    private List<Comment> commentList;

    public CommentAdpter(RecyclerView recyclerView,List<Comment> commentList){
        super(recyclerView);
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
        holder.bind(commentList.get(position));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.myItemClickListener = listener;
    }

    public class ViewHolder extends SelectableAdapter.ViewHolder {

        TextView userName, complain, date, solution;
        ImageView imgUser;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.txt_Name_user);
            complain = (TextView) itemView.findViewById(R.id.txt_Complain);
            date = (TextView) itemView.findViewById(R.id.txt_date_created);
            solution = (TextView) itemView.findViewById(R.id.txt_solution_area);
            imgUser = (ImageView) itemView.findViewById(R.id.img_user);
            setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick() {
                    if(myItemClickListener != null){
                        myItemClickListener.onItemClick(commentList.get(getAdapterPosition()));
                    }
                }
            });
        }

        /*
        public ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.txt_Name_user);
            complain = (TextView) itemView.findViewById(R.id.txt_Complain);
            date = (TextView) itemView.findViewById(R.id.txt_date_created);
            solution = (TextView) itemView.findViewById(R.id.txt_solution_area);
            imgUser = (ImageView) itemView.findViewById(R.id.img_user);
        }
        */

        public void bind(Comment comment){
            userName.setText(comment.getUsername());
            complain.setText(comment.getComplain());
            date.setText(comment.getDate());
            solution.setText(comment.getSolution());

        }
    }

    public interface MyItemClickListener {
        void onItemClick(Comment comment);
    }
}
