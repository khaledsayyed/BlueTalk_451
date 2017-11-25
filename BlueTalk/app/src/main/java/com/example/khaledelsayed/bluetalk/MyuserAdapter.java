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
public class MyuserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<DataUser> data = Collections.emptyList();
    DataUser current;
    int currentPos=0;
   private final OnListFragmentInteractionListener mListener;

    public MyuserAdapter(OnListFragmentInteractionListener listener,Context context, List<DataUser> data) {
        this.mListener=listener;
        this.context = context;
        inflater = LayoutInflater.from(context);
       this.data = data;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.fragment_user, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyHolder myHolder= (MyHolder) holder;
        DataUser current=data.get(position);

        myHolder.nameView.setText(current.name);
        myHolder.unreadView.setText(String.valueOf(current.unread_messages));
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

       myHolder.mView.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(myHolder.mItem);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public DataUser mItem;

        public final TextView nameView;
        public final TextView unreadView;


        public MyHolder(View view) {
            super(view);
            mView = view;

           nameView = (TextView) view.findViewById(R.id.name);
            unreadView = (TextView) view.findViewById(R.id.unread);
        }

    }
}
