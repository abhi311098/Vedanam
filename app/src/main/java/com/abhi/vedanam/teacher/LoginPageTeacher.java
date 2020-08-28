package com.abhi.vedanam.teacher;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.abhi.vedanam.R;
import com.abhi.vedanam.Verify;
import com.abhi.vedanam.student.LoginPageStudent;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginPageTeacher extends AppCompatActivity {

    ProgressDialog progressDialog;
    private static final String TAG = "loginpage";
    private EditText temail, tpassword;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    GoogleSignInClient mgoogleSignInClient;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page_teacher);
        progressDialog = new ProgressDialog(LoginPageTeacher.this);
        SignInButton signInButton = findViewById(R.id.sign_in_buttonteacher);
        temail = findViewById(R.id.loginemailteacher);
        tpassword = findViewById(R.id.loginpasswordteacher);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setCancelable(false);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog_view);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                signIn();
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mgoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void loginbuttonteacher(View view) {
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog_view);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        String email = temail.getText().toString().trim();
        String password = tpassword.getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "enter email address", Toast.LENGTH_SHORT).show();
        } else if (!email.matches(emailPattern)) {
            Toast.makeText(getApplicationContext(), "not valid email address", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "enter password", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                checkingprocess();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginPageTeacher.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                            // ...
                        }
                    });
        }
    }

    private void gotonext() {
        progressDialog.dismiss();
        Intent i = new Intent(LoginPageTeacher.this, HomeTeacher.class);
        startActivity(i);
        finish();
    }

    public void registrationbutton(View view) {
        Intent i = new Intent(LoginPageTeacher.this, RegistrationPageTeacher.class);
        i.putExtra("loginkey", "teacher");
        startActivity(i);
        finish();
    }

    private void signIn() {
        Intent signInIntent = mgoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1001);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1001) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginPageTeacher.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            gotonext();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                        // ...
                    }
                });
    }

    private void checkingprocess() {
        final FirebaseUser user = mAuth.getCurrentUser();
        final String currentuser1 = user.getUid();
        Log.e("usercheck", "" + user.getUid() + user.getEmail());
        Toast.makeText(this, "" + user.getUid(), Toast.LENGTH_SHORT).show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("teacher").child(currentuser1);
        Log.e("usercheck", "" + databaseReference);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String loginkey = snapshot.child("impkey").getValue(String.class);
                Log.e("usercheck", "loginkey" + loginkey);
                if (snapshot.exists()) {
                    if (loginkey.equals(currentuser1)) {
                        if (!loginkey.equals("")) {
                            SharedPreferences sharedPreferences = getSharedPreferences("impkey", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("key", loginkey);
                            editor.commit();
                            gotonext();
                        }
                    } else {
                        Toast.makeText(LoginPageTeacher.this, "id not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginPageTeacher.this, "You are not a teacher check in student login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginPageTeacher.this, Verify.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
