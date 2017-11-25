package com.example.khaledelsayed.bluetalk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.khaledelsayed.bluetalk.ChatsFragment.OnListFragmentInteractionListener;


import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<DataMessage> data = Collections.emptyList();
    DataMessage current;
    int currentPos=0;
    String myName;


    public MyMessageAdapter(String name,Context context, List<DataMessage> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        myName = name;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(data.get(currentPos++).sender.equals(myName)) {
            view = inflater.inflate(R.layout.message_sent, parent, false);
        }else{
            view = inflater.inflate(R.layout.message_received, parent, false);
          }
        MyHolder holder=new MyHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
       // currentPos=position;
        final MyHolder myHolder= (MyHolder) holder;
        DataMessage current=data.get(position);

        myHolder.messageView.setText(myName+current.sender+current.message);
       myHolder.mItem = current;

        // load image into imageview using glide
        /*may be implemented later
        Glide.with(context).load("http://192.168.1.7/test/images/" + current.fishImage)
                .placeholder(R.drawable.ic_img_error)
                .error(R.drawable.ic_img_error)
                .into(myHolder.ivFish);
                */

        //old code
        //holder.mItem = mValues.get(position);
        //holder.mContentView.setText(mValues.get(position).content);

        //



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public DataMessage mItem;

        public final TextView messageView;


        public MyHolder(View view) {
            super(view);
            mView = view;

            messageView = (TextView) view.findViewById(R.id.messageView);

        }

    }
}
