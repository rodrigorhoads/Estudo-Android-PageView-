package com.example.msn_r.bignerdranch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.GregorianCalendar;


public class DatePickerFragment extends DialogFragment {

    private static final String ARG_DATE="date";
    public static final String EXTRA_DATE="com.example.msn_r.bignerdranch.date";
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

        AlertDialog alertDialog=new  AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int ano=mDatePicker.getYear();
                        int mes=mDatePicker.getMonth();
                        int dia=mDatePicker.getDayOfMonth();
                        Date date = new GregorianCalendar(ano,mes,dia).getTime();
                        sendResult(Activity.RESULT_OK,date);
                    }
                })
                .create();
        return alertDialog;
    }

    private void sendResult(int resultCode,Date date){
        if(getTargetFragment()==null){
            return;
        }

        Intent intent=new Intent();
        intent.putExtra(EXTRA_DATE,date);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }


    public static DatePickerFragment newInstance(Date date){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_DATE,date);
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(bundle);
        return datePickerFragment;
    }
}
