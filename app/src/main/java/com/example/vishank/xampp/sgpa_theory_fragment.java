package com.example.vishank.xampp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListPopupWindow;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class sgpa_theory_fragment extends Fragment implements View.OnTouchListener, AdapterView.OnItemClickListener {
    EditText edittext_1;
    EditText edittext_2;
    EditText edittext_3;
    EditText edittext_4;
    EditText edittext_5;
    //EditText edittext_6;
    private ListPopupWindow lpw;
    private String[] list;

    int count ;

    public sgpa_theory_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sgpa_theory_fragment, container, false);

        instantiate(view);

        edittext_1.setOnTouchListener(this);
        edittext_2.setOnTouchListener(this);
        edittext_3.setOnTouchListener(this);
        edittext_4.setOnTouchListener(this);
        edittext_5.setOnTouchListener(this);
       // edittext_6.setOnTouchListener(this);

        list = new String[]{"F", "D", "C", "B", "A", "E", "O"};
        lpw = new ListPopupWindow(getActivity());
        lpw.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, list));

        lpw.setAnchorView(edittext_1);
        lpw.setAnchorView(edittext_2);
        lpw.setAnchorView(edittext_3);
        lpw.setAnchorView(edittext_4);
        lpw.setAnchorView(edittext_5);
        //lpw.setAnchorView(edittext_6);


        lpw.setModal(true);
        lpw.setOnItemClickListener(this);
        return view;
    }

    private void instantiate(View view) {

        edittext_1 = (EditText) view.findViewById(R.id.edittext_1);
        edittext_2 = (EditText) view.findViewById(R.id.edittext_2);
        edittext_3 = (EditText) view.findViewById(R.id.edittext_3);
        edittext_4 = (EditText) view.findViewById(R.id.edittext_4);
        edittext_5 = (EditText) view.findViewById(R.id.edittext_5);
       // edittext_6 = (EditText) view.findViewById(R.id.edittext_6);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        final int DRAWABLE_RIGHT = 2;

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (event.getX() >= (v.getWidth() - ((EditText) v)
                    .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                count = v.getId();
                lpw.show();
                return true;
            }
        }
        return false;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        switch (count) {

            case R.id.edittext_1:
                String item = list[position];
                edittext_1.setText(item);
                ((sgpaCalActivity)getActivity()).set_text_theory_1(list[position]);
                break;

            case R.id.edittext_2:
                // String item2 = list[position];
                edittext_2.setText(list[position]);
                ((sgpaCalActivity)getActivity()).set_text_theory_2(list[position]);
                break;

            case R.id.edittext_3:
                // String item = list[position];
                edittext_3.setText(list[position]);
                ((sgpaCalActivity)getActivity()).set_text_theory_3(list[position]);
                break;

            case R.id.edittext_4:
                //String item = list[position];
                edittext_4.setText(list[position]);
                ((sgpaCalActivity)getActivity()).set_text_theory_4(list[position]);
                break;

            case R.id.edittext_5:
                //String item = list[position];
                edittext_5.setText(list[position]);
                ((sgpaCalActivity)getActivity()).set_text_theory_5(list[position]);
                break;

            case R.id.edittext_6:
                //String item = list[position];
              //  edittext_6.setText(list[position]);
                break;
        }
        lpw.dismiss();

    }
}
