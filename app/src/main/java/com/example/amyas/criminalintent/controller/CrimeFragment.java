package com.example.amyas.criminalintent.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.amyas.criminalintent.R;
import com.example.amyas.criminalintent.model.Crime;
import com.example.amyas.criminalintent.model.CrimeLab;

import java.util.Date;
import java.util.UUID;

import static android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * author: amyas
 * date: 2017/10/26
 */

public class CrimeFragment extends Fragment implements View.OnClickListener {
    //    private String TAG = CrimeFragment.class.getSimpleName();
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_CODE = 1;
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;


    public static final String ARG_CRIME_ID = "crime_id";

    public static CrimeFragment newInstance(UUID uuid) {
        Bundle arg = new Bundle();
        arg.putSerializable(ARG_CRIME_ID, uuid);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);


        mTitleField = view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());


        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateButton  = view.findViewById(R.id.crime_date);
        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setOnClickListener(this);


        mSolvedCheckBox = view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setSolved(b);
            }
        });
        return view;
    }
    //    public void returnResult(){
    //        getActivity().setResult(CrimeListActivity.RESULT_OK);
    //    }


    @Override
    public void onClick(View view) {
        FragmentManager manager = getFragmentManager();
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mCrime.getDate());
        datePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_CODE);
        datePickerFragment.show(manager, DIALOG_DATE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            mDateButton.setText(mCrime.getDate().toString());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove_crime:
                CrimeLab crimeLab = CrimeLab.get(getActivity());
                crimeLab.remove(mCrime);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
