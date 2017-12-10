package com.example.khaledelsayed.bluetalk;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUp#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUp extends Fragment {
    public EditText username_text;
    public EditText mobilenum_text;
    public Button signupbutton;
 public SignupInterface mInterface;

    public SignUp() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SignUp.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUp newInstance() {
        SignUp fragment = new SignUp();

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SignupInterface) {
           mInterface = (SignupInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        username_text = (EditText) view.findViewById(R.id.user_name);
        mobilenum_text = (EditText)view.findViewById(R.id.phone_number);
        signupbutton = (Button) view.findViewById(R.id.sign_up_button);
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            mInterface.sign_the_user_up(username_text.getText().toString(),Integer.parseInt(mobilenum_text.getText().toString()));
            }
        });
        return view;
    }
public interface SignupInterface{
    public void sign_the_user_up(String uname,int mobile);

    }
}
