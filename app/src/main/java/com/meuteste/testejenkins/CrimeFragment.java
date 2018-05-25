package com.meuteste.testejenkins;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;




import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.widget.CompoundButton.*;


public class CrimeFragment extends Fragment {
    private static final String ARG_CRIME_ID="crime_id";
    private static final String DIALOG_DATE="DialogDate";
    private static final int REQUEST_DATE=0;
    private static final int REQUEST_CONTACT=1;
    private static final int REQUEST_FOTO=2;
    private File mPhotoFile;
    private Button mReportButton;
    private Button mSuspectButton;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;


    private ImageButton mFotoButon;
    private ImageView mFotoView;

    private Callbacks mCallbacks;

    public interface Callbacks{
        void onCrimeUpdate(Crime crime);
    }

    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,crimeId);

        CrimeFragment fragment =new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks=(Callbacks) context;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        UUID crimeId= (UUID)getActivity().getIntent().getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);
//        mCrime=CrimeLab.get(getActivity()).getCrime(crimeId);

        UUID crimeId=(UUID)getArguments().getSerializable(ARG_CRIME_ID);
        mCrime=CrimeLab.get(getActivity()).getCrime(crimeId);
        mPhotoFile=CrimeLab.get(getActivity()).getFoto(mCrime);
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_crime, container, false);

        mSolvedCheckBox=(CheckBox)view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.ismSolves());

        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setmSolves(isChecked);
                updateCrime();
            }
        });

        mDateButton=(Button)view.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
//                DatePickerFragment dialog=new DatePickerFragment();
                DatePickerFragment dialog =DatePickerFragment.newInstance(mCrime.getmDate());

                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE );

                dialog.show(manager,DIALOG_DATE);
            }
        });

        mTitleField = (EditText) view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getmTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setmTitle(s.toString());
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mReportButton=(Button)view.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_subject));
                intent.createChooser(intent,getString(R.string.send_report));
                startActivity(intent);
            }
        });
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//        pickContact.addCategory(Intent.CATEGORY_HOME);
        mSuspectButton=(Button)view.findViewById(R.id.crime_supect);
        mSuspectButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact,REQUEST_CONTACT);
            }
        });
        if(mCrime.getmSuspect()!=null){
            mSuspectButton.setText(mCrime.getmSuspect());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if(packageManager.resolveActivity(pickContact,PackageManager.MATCH_DEFAULT_ONLY)==null){
            mSuspectButton.setEnabled(false);
        }

        mFotoButon=(ImageButton) view.findViewById(R.id.crime_camera);
        final Intent capturaFoto= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean podeTirarFoto=mPhotoFile!=null&& capturaFoto.resolveActivity(packageManager)!=null;
        mFotoButon.setEnabled(podeTirarFoto);

        mFotoButon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri= FileProvider.getUriForFile(getActivity(),"com.example.msn_r.bignerdranch.fileprovider",mPhotoFile);
                capturaFoto.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                List<ResolveInfo> cameraActivities = getActivity().getPackageManager()
                        .queryIntentActivities(capturaFoto,PackageManager.MATCH_DEFAULT_ONLY);
                for(ResolveInfo activity:cameraActivities){
                    getActivity().grantUriPermission(activity.activityInfo.packageName,uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(capturaFoto,REQUEST_FOTO);
            }
        });

        mFotoView=(ImageView)view.findViewById(R.id.crime_foto);
        updateFotoView();

        return view;
    }

    private void updateFotoView(){
        if(mPhotoFile==null || !mPhotoFile.exists()){
            mFotoView.setImageDrawable(null);
        }else{
            Bitmap bitmap=PictureUtils.getScaleBitmap(mPhotoFile.getPath(),getActivity());
            mFotoView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!= Activity.RESULT_OK){
            return;
        }

        if(requestCode==REQUEST_DATE){
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setmDate(date);
            updateCrime();
            updateDate();
        }
        else if(requestCode==REQUEST_CONTACT && data !=null){
            Uri contactUri = data.getData();
            //especifica qual campo voce quer que a sua consulta retorne os valores
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };
        //executa a consulta - o contatoUri é tipo um "where" clasula aqui
            Cursor cursor = getActivity().getContentResolver().query(contactUri,queryFields,null,null,null);
            try{
                //checagem dupla se ha um resultado
                if(cursor.getCount()==0){
                    return;
                }
                //puxa a primeira coluna da primeira linha de dados que sao seus nomes suspeitos
                cursor.moveToFirst();
                String suspect = cursor.getString(0);
                mCrime.setmSuspect(suspect);
                updateCrime();
                mSuspectButton.setText(suspect);
            }finally {
                cursor.close();
            }
        }
        else if(requestCode==REQUEST_FOTO){
            Uri uri= FileProvider.getUriForFile(getActivity(),"com.example.msn_r.bignerdranch.fileprovider",mPhotoFile);
            getActivity().revokeUriPermission(uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updateCrime();
            updateFotoView();
        }


    }

    private void updateDate() {
        mDateButton.setText(mCrime.getmDate().toString());
    }

    private String getCrimeReport(){
        String solvedString=null;
        if(mCrime.ismSolves()){
            solvedString=getString(R.string.crime_report_solved);
        }else{
            solvedString= getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString;
        dateString = DateFormat.format(dateFormat,mCrime.getmDate()).toString();

        String suspect = mCrime.getmSuspect();
        if(suspect==null){
            suspect=getString(R.string.crime_report_no_suspect);
        }else{
            suspect=getString(R.string.crime_report_suspect,suspect);
        }

        String report = getString(R.string.crime_report,mCrime.getmTitle(),dateString,solvedString,suspect);
        return report;
    }

    private void updateCrime(){

        CrimeLab.get(getActivity()).updateCrime(mCrime);

        mCallbacks.onCrimeUpdate(mCrime);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks=null;
    }

}
