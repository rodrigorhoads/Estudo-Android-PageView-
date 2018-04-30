package com.example.msn_r.bignerdranch;

import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;


public class DatePickerFragment extends DialogFragment {

    private static final String ARG_DATE="date";
    DatePicker mDatePicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date)getArguments().getSerializable(ARG_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int ano = calendar.get(Calendar.YEAR);
        int mes  = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_date_picker,null);

        mDatePicker=(DatePicker) view.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(ano,mes,dia,null);
        mDatePicker.init(ano,mes,dia,null);

        AlertDialog alertDialog=new  AlertDialog.Builder(getActivity())
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok,null)
                .setView(view)
                .create();
        return alertDialog;
    }

    public static DatePickerFragment newInstance(Date date){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_DATE,date);
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(bundle);
        return datePickerFragment;
    }
}
