package com.example.aap.bplfantasyleague.control;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aap.bplfantasyleague.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;


public class EmailPasswordActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";
    //private TextView mStatusTextView;
    //private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;
    private FirebaseAuth mAuth;
    DatabaseReference dref;
    @VisibleForTesting

    public ProgressDialog mProgressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailpassword);
        mEmailField = findViewById(R.id.field_email);
        mPasswordField = findViewById(R.id.field_password);
        mEmailField.setText("aap.pavel@gmail.com");
        mPasswordField.setText("123456");
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void createAccount(final String email, final String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String userId=user.getUid();dref= FirebaseDatabase.getInstance().getReference();
                            dref = dref.child("USERS").child(userId);
                            dref.child("Email").setValue(email);dref.child("Password").setValue(password);
                            dref.child("Balance").setValue(100);dref.child("Score").setValue(0);
                            dref.child("Since").setValue(ServerValue.TIMESTAMP);
                            dref.child("TotalSelected").setValue(0);
                            Toast.makeText(EmailPasswordActivity.this,"Signed In",Toast.LENGTH_SHORT).show();
                            Intent startIntent = new Intent(EmailPasswordActivity.this,SelectSquadActivity.class);
                            startIntent.putExtra("userId",userId);
                            startActivity(startIntent);
                            finish();
                        }
                        else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        hideProgressDialog();

                    }

                });
    }


    private void signIn(String email, String password) {

        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }
        showProgressDialog();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            final String userId=user.getUid();
                            Toast.makeText(EmailPasswordActivity.this,"Signed In",Toast.LENGTH_SHORT).show();
                            final DatabaseReference db= FirebaseDatabase.getInstance().getReference("USERS/"+userId).child("TotalSelected");
                            db.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    int total = Integer.parseInt(dataSnapshot.getValue().toString());
                                    if (total==11){
                                        Intent startIntent = new Intent(EmailPasswordActivity.this,MainActivity.class);
                                        startIntent.putExtra("userId",userId);
                                        startActivity(startIntent);
                                        /*---you might want to call finish() method here but never do that
                                        ----call finish() method from outside the listener---
                                         */
                                    }
                                    else{
                                        Intent startIntent = new Intent(EmailPasswordActivity.this,SelectSquadActivity.class);
                                        startIntent.putExtra("userId",userId);
                                        startActivity(startIntent);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            finish();
                        }
                        else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        if (!task.isSuccessful()) {

                            Toast.makeText(EmailPasswordActivity.this,"Autehentication Failed.",Toast.LENGTH_SHORT);

                        }

                        hideProgressDialog();
                    }

                });

    }

    /*private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }*/

    /*private void sendEmailVerification() {
        //findViewById(R.id.verify_email_button).setEnabled(false);
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //findViewById(R.id.verify_email_button).setEnabled(true);
                        if (task.isSuccessful()) {
                            Toast.makeText(EmailPasswordActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            Toast.makeText(EmailPasswordActivity.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }*/

    private boolean validateForm() {
        boolean valid = true;
        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        }
        else {
            mEmailField.setError(null);
        }
        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        }
        else {
            mPasswordField.setError(null);
        }
        return valid;
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            final String userId=user.getUid();
            DatabaseReference db= FirebaseDatabase.getInstance().getReference("USERS/"+userId).child("TotalSelected");
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int total = Integer.parseInt(dataSnapshot.getValue().toString());
                    if (total==11){
                        Intent startIntent = new Intent(EmailPasswordActivity.this,MainActivity.class);
                        startIntent.putExtra("userId",userId);
                        startActivity(startIntent);
                        /*---you might want to call finish() method here but never do that
                         ----call finish() method from outside the listener---
                          */
                    }
                    else{
                        Intent startIntent = new Intent(EmailPasswordActivity.this,SelectSquadActivity.class);
                        startIntent.putExtra("userId",userId);
                        startActivity(startIntent);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            finish();
        }
         else{
            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.email_create_account_button) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
        else if (i == R.id.email_sign_in_button) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
    }

    public void showProgressDialog() {

        if (mProgressDialog == null) {

            mProgressDialog = new ProgressDialog(this);

            mProgressDialog.setMessage(getString(R.string.loading));

            mProgressDialog.setIndeterminate(true);

        }



        mProgressDialog.show();

    }



    public void hideProgressDialog() {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {

            mProgressDialog.dismiss();

        }

    }

    @Override
    public void onStop() {

        super.onStop();

        hideProgressDialog();

    }
}
