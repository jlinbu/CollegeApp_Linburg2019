package com.linburgtech.collegeapp_linburg2019;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.DataQueryBuilder;

import java.util.Date;
import java.util.List;

public class ProfileFragment extends Fragment {

    public static final int REQUEST_DATE_OF_BIRTH = 0;
    Button DatePickerButton;
    Button mSubmit;
    EditText firstNameEdit;
    EditText lastNameEdit;
    Profile mProfile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup view, Bundle bundle){
        super.onCreateView(inflater, view, bundle);
        mProfile = new Profile();
        String whereClause = "email = 'jlinburg@doversd.org'";
        //Retrieve from Backendless
        DataQueryBuilder query = DataQueryBuilder.create();
        query.setWhereClause(whereClause);
        Backendless.Data.of(Profile.class).find(query, new AsyncCallback<List<Profile>>() {
            @Override
            public void handleResponse(List<Profile> response) {
                if (!response.isEmpty()) {
                    mProfile = response.get(0);
                }

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.e("Profile Fragment", "Failed to find profile: " + fault.getMessage());
            }
        });
        //New code
        View rootView = inflater.inflate(R.layout.fragment_profile, view, false);


        DatePickerButton = (Button)rootView.findViewById(R.id.DatePickerButton);
        mSubmit = (Button)rootView.findViewById(R.id.profile_submit);
        firstNameEdit = (EditText)rootView.findViewById(R.id.first_name_edit);
        lastNameEdit = (EditText)rootView.findViewById(R.id.last_name_edit);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstNameEdit.getText().toString()!=null){
                    mProfile.firstName = firstNameEdit.getText().toString();
                }
                if (lastNameEdit.getText().toString()!=null){
                    mProfile.lastName = lastNameEdit.getText().toString();
                }
                saveToBackendless();
            }
        });
        DatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mProfile.dateOfBirth);
                dialog.setTargetFragment(ProfileFragment.this, REQUEST_DATE_OF_BIRTH);
                dialog.show(fm, "DialogDateOfBirth");

            }
        });


        return rootView;
    }

    @Override
    public void onPause(){
        super.onPause();
        saveToBackendless();

    }

   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent intent){
        Log.i("ProfileFragment", "" + requestCode + " " + resultCode);
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == REQUEST_DATE_OF_BIRTH){
                mProfile.dateOfBirth = (Date)intent.getSerializableExtra(DatePickerFragment.EXTRA_DATE_OF_BIRTH);
                Log.i("ProfileFragment", mProfile.dateOfBirth.toString());
                DatePickerButton.setText(mProfile.dateOfBirth.toString());
                saveToBackendless();
            }
        }
   }

   //convenience method for saving to Backendless
   private void saveToBackendless(){
       String whereClause = "email = 'jlinburg@doversd.org'";
       DataQueryBuilder query = DataQueryBuilder.create();
       query.setWhereClause(whereClause);
       Backendless.Data.of(Profile.class).find(query, new AsyncCallback<List<Profile>>() {
           @Override
           public void handleResponse(List<Profile> response) {
               if (!response.isEmpty()) {
                   String profileId = response.get(0).getObjectId();
                   Log.d("Profile Fragment", "Object ID: " + profileId);
                   mProfile.setObjectId(profileId);
                   Backendless.Data.of(Profile.class).save(mProfile, new AsyncCallback<Profile>() {
                       @Override
                       public void handleResponse(Profile response) {
                           Log.i("success", response.getFirstName() + " has been saved");
                       }

                       @Override
                       public void handleFault(BackendlessFault fault) {
                           Log.e("Error", fault.getMessage());
                       }
                   });
               }
               else{
                   Backendless.Data.of(Profile.class).save(mProfile, new AsyncCallback<Profile>() {
                       @Override
                       public void handleResponse(Profile response) {
                           Log.i("success", response.getFirstName() + " has been saved");
                           mProfile.objectId = response.objectId;
                       }

                       @Override
                       public void handleFault(BackendlessFault fault) {
                           Log.e("Error", fault.getMessage());
                       }
                   });
               }
           }

           @Override
           public void handleFault(BackendlessFault fault) {
               Log.e("Profile Fragment", "Failed to find profile: " + fault.getMessage());
           }
       });
   }

}
