package com.sgp95.santiago.helpu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sgp95.santiago.helpu.adapter.CommentAdpter;
import com.sgp95.santiago.helpu.model.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hiraoka on 16/06/2017.
 */

public class CommentsFragment extends Fragment {
    private RecyclerView recyclerView;
    private CommentAdpter commentAdpter;
    List<Comment> commentList;



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
