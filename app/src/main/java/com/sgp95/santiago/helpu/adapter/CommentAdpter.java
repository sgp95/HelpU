package com.sgp95.santiago.helpu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sgp95.santiago.helpu.R;
import com.sgp95.santiago.helpu.model.Complain;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CommentAdpter extends SelectableAdapter<CommentAdpter.ViewHolder> {

    private MyItemClickListener myItemClickListener;
    private List<Complain> complainList;
    private Context context;

    public CommentAdpter(RecyclerView recyclerView, List<Complain> complainList, Context context){
        super(recyclerView);
        this.complainList = complainList;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment,parent,false);
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

        TextView userCode, complain, dateCreated;
        ImageView imgUser,imgRequest;

        public ViewHolder(View itemView) {
            super(itemView);
            userCode = (TextView) itemView.findViewById(R.id.txt_code_user);
            complain = (TextView) itemView.findViewById(R.id.txt_complaint_area);
            dateCreated = (TextView) itemView.findViewById(R.id.txt_date_created);
            imgUser = (ImageView) itemView.findViewById(R.id.img_user);
            imgRequest = (ImageView) itemView.findViewById(R.id.img_complaint);

        }

        public void bind(Complain comment){


            Picasso.with(context)
                    .load(comment.getComplainImage())
                    .into(imgRequest);

            userCode.setText(comment.getUserCode());
            complain.setText(comment.getComplain());
            dateCreated.setText(comment.getDateCreated());
        }
    }

    public interface MyItemClickListener {
        void onItemClick(Complain complein);
    }

}
