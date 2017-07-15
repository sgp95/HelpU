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
import com.sgp95.santiago.helpu.model.Complain;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;


public class HistoryAdapter extends SelectableAdapter<HistoryAdapter.ViewHolder> {

    private MyItemClickListener myItemClickListener;
    private List<Complain> complainList;
    private Context context;

    public HistoryAdapter(RecyclerView recyclerView, List<Complain> complainList, Context context){
        super(recyclerView);
        this.complainList = complainList;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_history,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(complainList.get(getItemCount() - 1 - position));
    }

    @Override
    public int getItemCount() {
        return complainList.size();
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.myItemClickListener = listener;
    }

    public class ViewHolder extends SelectableAdapter.ViewHolder {
        TextView userCode, complain, dateCreated, state,headquarter,timeCreated;
        ImageView imgUser,imgRequest;
        ProgressBar loadhistory,loadhistory2;

        public ViewHolder(View itemView) {
            super(itemView);
            complain = (TextView) itemView.findViewById(R.id.txt_history_content);
            timeCreated = (TextView) itemView.findViewById(R.id.txt_time_created);
            dateCreated = (TextView) itemView.findViewById(R.id.txt_date_created);
            imgUser = (ImageView) itemView.findViewById(R.id.img_user);
            imgRequest = (ImageView) itemView.findViewById(R.id.img_content_history);
            state = (TextView) itemView.findViewById(R.id.txt_state_title);
            headquarter = (TextView) itemView.findViewById(R.id.text_headquarter_name);
            loadhistory = (ProgressBar) itemView.findViewById(R.id.load_history);
        }

        public void bind(Complain comment){

            if(!comment.getComplainImage().equals("null") || !comment.getComplainImage().equals("") || !comment.getComplainImage().isEmpty() ) {

                loadhistory.setVisibility(View.VISIBLE);
                Picasso.with(context)
                        .load(comment.getComplainImage())
                        .into(imgRequest, new Callback(){
                            @Override
                            public void onSuccess() {
                                loadhistory.setVisibility(View.GONE);
                                imgRequest.setVisibility(View.VISIBLE);
                            }
                            @Override
                            public void onError() {
                                loadhistory.setVisibility(View.GONE);
                            }
                        });


            }





            headquarter.setText(comment.getHeadquarter()+" / "+comment.getCategory());
            complain.setText(comment.getComplain());
            dateCreated.setText(comment.getDateCreated().substring(0,10).replace("-","/").replace("-","/").replace("-","/"));
            timeCreated.setText(comment.getDateCreated().substring(11,16));
            System.out.println("Estado " + comment.getState());
            String uri="";
            if(comment.getState().equals("Pendiente")){
                uri="https://firebasestorage.googleapis.com/v0/b/helpu-66213.appspot.com/o/red-card.png?alt=media&token=91ae9226-848e-43b6-a976-790bd4ff8f86";
            }
            else if(comment.getState().equals("En Proceso")){
                uri="https://firebasestorage.googleapis.com/v0/b/helpu-66213.appspot.com/o/yellow-card.png?alt=media&token=91ae9226-848e-43b6-a976-790bd4ff8f86";
            }
            else if(comment.getState().equals("Finalizado")){
                uri="https://firebasestorage.googleapis.com/v0/b/helpu-66213.appspot.com/o/green-card.png?alt=media&token=91ae9226-848e-43b6-a976-790bd4ff8f86";

            }else{

            }
            if(!comment.getState().equals("null")){
                Picasso.with(context)
                        .load(uri)
                        .into(imgUser);
                imgUser.setVisibility(View.VISIBLE);
            }


        }
    }


    public interface MyItemClickListener {
        void onItemClick(Complain complein);
    }


}