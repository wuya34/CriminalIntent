package com.example.amyas.criminalintent.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
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
    public static final String ARG_CRIME_ID = "crime_id";
    public static final int REQUEST_CONTACT = 2;
    //    private String TAG = CrimeFragment.class.getSimpleName();
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_CODE = 1;
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mReportButton;
    private Button mSuspectButton;
    private Intent pickContact;
    private CallBacks mCallBacks;

    public interface CallBacks {
        void onCrimeUpdated(Crime crime);

        void onDetachFragment(Fragment fragment);
    }
    public static CrimeFragment newInstance(UUID uuid) {
        Bundle arg = new Bundle();
        arg.putSerializable(ARG_CRIME_ID, uuid);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallBacks = (CallBacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBacks = null;
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
        mReportButton = view.findViewById(R.id.crime_report);
        mSuspectButton = view.findViewById(R.id.crime_suspect);
        mReportButton.setOnClickListener(this);
        mSuspectButton.setOnClickListener(this);
        mTitleField.setText(mCrime.getTitle());

        pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateButton = view.findViewById(R.id.crime_date);
        mDateButton.setText(mCrime.getDate().toString());
        mDateButton.setOnClickListener(this);


        mSolvedCheckBox = view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCrime.setSolved(b);
                updateCrime();
            }
        });
        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }
        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact, packageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }
        return view;
    }
    //    public void returnResult(){
    //        getActivity().setResult(CrimeListActivity.RESULT_OK);
    //    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.crime_date:
                FragmentManager manager = getFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mCrime.getDate());
                datePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_CODE);
                datePickerFragment.show(manager, DIALOG_DATE);
                break;
            case R.id.crime_report:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                intent = Intent.createChooser(intent, getString(R.string.send_report));
                startActivity(intent);
                break;
            case R.id.crime_suspect:

                startActivityForResult(pickContact, REQUEST_CONTACT);

        }

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
        if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            try (Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null)) {
                if (c.getCount() == 0) {
                    return;
                }
                c.moveToFirst();
                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            }
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
                // TODO: 2017/11/1 解决平板模式下的finish问题
                if (getActivity().getSupportFragmentManager().
                        findFragmentById(R.id.detail_fragment_container) != null) {
                    mCallBacks.onCrimeUpdated(null);
                    mCallBacks.onDetachFragment(this);

                } else {
                    getActivity().finish();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    private String getCrimeReport() {
        String crimeSolved;
        if (mCrime.isSolved()) {
            crimeSolved = getString(R.string.crime_report_solved);
        } else {
            crimeSolved = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EE, MMM dd";
        String date = DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect;
        if (mCrime.getSuspect() != null) {
            suspect = getString(R.string.crime_report_suspect);
        } else {
            suspect = getString(R.string.crime_report_no_suspect);
        }
        return getString(R.string.crime_report, mCrime.getTitle(),
                date, crimeSolved, suspect);
    }

    private void updateCrime() {
        CrimeLab.get(getActivity()).updateCrime(mCrime);
        mCallBacks.onCrimeUpdated(mCrime);
    }


}
