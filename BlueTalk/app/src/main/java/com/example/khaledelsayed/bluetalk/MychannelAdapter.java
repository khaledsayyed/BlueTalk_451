package com.example.khaledelsayed.bluetalk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.khaledelsayed.bluetalk.HomeFragment.OnFragmentcInteractionListener;
import java.util.Collections;
import java.util.List;
/**
 * Created by User on 26/11/2017.
 */
/**
 * specified {@link HomeFragment.OnFragmentcInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MychannelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private LayoutInflater inflater;
    List<DataChannel> data = Collections.emptyList();
    DataChannel current;
    int currentPos=0;
    private final OnFragmentcInteractionListener mListener;

    public MychannelAdapter(OnFragmentcInteractionListener listener,Context context, List<DataChannel> data) {
        this.mListener=listener;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
}
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        if( viewType==0) {
           view = inflater.inflate(R.layout.fragment_channelinfo, parent, false);
        }
        else if (viewType==1){
            view = inflater.inflate(R.layout.fragment_channelinfo_create, parent, false);
        }
        MyHolder holder=new MyHolder(view);
        return holder;

    }
    @Override

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyHolder myHolder= (MyHolder) holder;
        DataChannel current=data.get(position);

        myHolder.idView.setText(String.valueOf(current.channelid));
        myHolder.nameView.setText(current.channel_name);
        myHolder.numofusersView.setText(String.valueOf(current.number_of_users));
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
                    mListener.onFragmentcInteraction(myHolder.mItem);

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
        public DataChannel mItem;
        public  TextView idView =null;
        public  TextView nameView= null;
        public  TextView numofusersView=null;
        public  Button buttonjoin=null;
        public  Button buttoncreate = null;


        public MyHolder(View view, int viewType) {
            super(view);
            mView = view;

            if(viewType==0) {
                nameView = (TextView) view.findViewById(R.id.channel_name);
                idView = (TextView) view.findViewById(R.id.channelid);
                numofusersView = (TextView) view.findViewById(R.id.number_of_users);
                buttonjoin= (Button) view.findViewById(R.id.buttonjoin);
            }
            else if(viewType==1){

            }
        }

    }
}
