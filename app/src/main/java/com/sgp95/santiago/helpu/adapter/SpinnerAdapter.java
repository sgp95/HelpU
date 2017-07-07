package com.sgp95.santiago.helpu.adapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.sgp95.santiago.helpu.model.Category;

import java.util.ArrayList;

public class SpinnerAdapter {
    DatabaseReference db;
    Boolean saved=null;

    public SpinnerAdapter(DatabaseReference db) {
        this.db = db;
    }

    //READ
    public ArrayList<String> retrieve()
    {
        final ArrayList<String> categories =new ArrayList<>();
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot,categories);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot,categories);
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
        });
        return categories;
    }
    private void fetchData(DataSnapshot snapshot,ArrayList<String> spacecrafts)
    {
        for (DataSnapshot ds:snapshot.getChildren())
        {
            String name=ds.getValue(Category.class).getName();
            spacecrafts.add(name);
        }
    }
}
