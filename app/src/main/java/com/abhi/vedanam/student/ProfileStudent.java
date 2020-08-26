package com.abhi.vedanam.student;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.abhi.vedanam.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

public class ProfileStudent extends AppCompatActivity {

    private ProgressDialog progressDialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ImageView imageStudent;
    private EditText nameStudent1, housenumberStudent, streetnumberStudent, arealocalityStudent,
            landmarkStudent, pincodeStudent, emailStudent, numberStudent;
    private Button studentbutton;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("StudentProfile").child(user.getUid());
    String permissions[] = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private StorageReference mStorageRef  = FirebaseStorage.getInstance().getReference();
    byte bb[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_student);

        progressDialog = new ProgressDialog(ProfileStudent.this);
        imageStudent = findViewById(R.id.imagestudent);
        nameStudent1 = findViewById(R.id.namestudent);
        housenumberStudent = findViewById(R.id.housenumberstudent);
        streetnumberStudent = findViewById(R.id.streetnumberstudent);
        arealocalityStudent = findViewById(R.id.arealocalityteacher);
        landmarkStudent = findViewById(R.id.landmarkstudent);
        pincodeStudent = findViewById(R.id.pincodestudent);
        emailStudent = findViewById(R.id.emailteacher);
        numberStudent = findViewById(R.id.numberteacher);
        studentbutton = findViewById(R.id.profileteacher);

        preferences = getSharedPreferences("StudentProfileDetails", Context.MODE_PRIVATE);
        editor = preferences.edit();

        String s_name = preferences.getString("student_name", null);
        String s_house = preferences.getString("student_house", null);
        String s_street = preferences.getString("student_street", null);
        String s_area = preferences.getString("student_area", null);
        String s_landmark = preferences.getString("student_landmark", null);
        String s_pincode = preferences.getString("student_pincode", null);
        String s_email = preferences.getString("student_email", null);
        String s_number = preferences.getString("student_number", null);

        if (s_name!=null&& s_house != null && s_street != null && s_area != null && s_landmark != null
                && s_pincode != null&&s_email!=null&&s_number!=null){
            nameStudent1.setText(s_name);
            housenumberStudent.setText(s_house);
            streetnumberStudent.setText(s_street);
            arealocalityStudent.setText(s_area);
            landmarkStudent.setText(s_landmark);
            pincodeStudent.setText(s_pincode);
            emailStudent.setText(s_email);
            numberStudent.setText(s_number);
        } else {
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String student_name = snapshot.child("" + "student_name").getValue(String.class);
                    nameStudent1.setText(student_name);
                    String student_house = snapshot.child("student_house").getValue(String.class);
                    housenumberStudent.setText(student_house);
                    String student_street = snapshot.child("student_street").getValue(String.class);
                    streetnumberStudent.setText(student_street);
                    String student_area = snapshot.child("student_area").getValue(String.class);
                    arealocalityStudent.setText(student_area);
                    String student_land = snapshot.child("student_land").getValue(String.class);
                    landmarkStudent.setText(student_land);
                    String student_pincode = snapshot.child("student_pincode").getValue(String.class);
                    pincodeStudent.setText(student_pincode);
                    String student_email = snapshot.child("student_email").getValue(String.class);
                    emailStudent.setText(student_email);
                    String student_number = snapshot.child("student_number").getValue(String.class);
                    numberStudent.setText(student_number);

                    editor.putString("student_name", student_name);
                    editor.putString("student_house", student_house);
                    editor.putString("student_street", student_street);
                    editor.putString("student_area", student_area);
                    editor.putString("student_land", student_land);
                    editor.putString("student_pincode", student_pincode);
                    editor.putString("student_email", student_email);
                    editor.putString("student_number", student_number);
                    editor.commit();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        functionpermission();
        setImage();

        imageStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setCancelable(false);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog_view);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 101);
            }
        });

            studentbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    profilestudent();
                }
            });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 101&&data!= null) {
                Uri selectedImage = data.getData();
                String[] filepath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filepath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filepath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                imageStudent.setImageBitmap(thumbnail);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                bb = bytes.toByteArray();

                if (bb != null) {
                    StorageReference storageReference = mStorageRef.child("Vedanamstudent/profilepic/"+user.getUid()+".jpg");
                    storageReference.putBytes(bb).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){
                                Log.e("jello","hello");
                                StorageReference storageReference = mStorageRef.child("Vedanamstudent/profilepic/"+user.getUid()+".jpg");
                                Log.e("success",storageReference+"");
                                final long SIZE = 1024 * 1024 * 15;
                                storageReference.getBytes(SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        Log.e("success",bytes.length+"");
                                        try {
                                            File root = Environment.getExternalStorageDirectory();
                                            File folder = new File(root, "VedanamStudent");
                                            //if (!folder.exists()) {
                                                folder.mkdir();
                                            //}
                                            File file = new File(folder, "Profilepic/"+user.getUid()+".jpg");
                                            //if (!file.exists()) {
                                                file.createNewFile();
                                            //}
                                            FileOutputStream fo = new FileOutputStream(file);
                                            fo.write(bytes);
                                            fo.close();
                                            Log.e("path", file.getAbsolutePath());
                                        } catch (Exception e) {
                                            Log.d("path", e.toString());
                                        }
                                    }
                                });
                                progressDialog.dismiss();
                                Toast.makeText(ProfileStudent.this, "Successfully Upload", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(ProfileStudent.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        }
    }

    private void setImage() {
        StorageReference storageReference = mStorageRef.child("Vedanamstudent/profilepic/"+user.getUid()+".jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ProfileStudent.this).load(uri).into(imageStudent);
            }
        });
    }

    public void functionpermission()
    {
        if (ActivityCompat.checkSelfPermission(ProfileStudent.this,permissions[0])!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(ProfileStudent.this,permissions,2);
        }
        else
        {
            Toast.makeText(ProfileStudent.this, "", Toast.LENGTH_SHORT).show();
        }
    }
    public void profilestudent() {

        String sname = nameStudent1.getText().toString();
        String shouse = housenumberStudent.getText().toString();
        String sstreet = streetnumberStudent.getText().toString();
        String sarea = arealocalityStudent.getText().toString();
        String sland = landmarkStudent.getText().toString();
        String spincode = pincodeStudent.getText().toString();
        String semail = emailStudent.getText().toString();
        String snumber = numberStudent.getText().toString();

        if(sname.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Enter Your Name ",Toast.LENGTH_SHORT).show();
        }else if (shouse.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Your House Number ", Toast.LENGTH_SHORT).show();
        } else if (sstreet.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Your Street/Building/Block ", Toast.LENGTH_SHORT).show();
        } else if (sarea.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Your Area/Locality Name ", Toast.LENGTH_SHORT).show();
        } else if (sland.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Your Landmark ", Toast.LENGTH_SHORT).show();
        } else if (spincode.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Your Pincode ", Toast.LENGTH_SHORT).show();
        }else if(semail.equals("")){
            Toast.makeText(getApplicationContext(),"Enter Your Email ",Toast.LENGTH_SHORT).show();
        }else if (!semail.matches(emailPattern)) {
            Toast.makeText(getApplicationContext(),"Enter Valid Email Address",Toast.LENGTH_SHORT).show();
        }else if(snumber.equals("")){
            Toast.makeText(getApplicationContext(),"Enter Your Number ",Toast.LENGTH_SHORT).show();
        }else{
            editor.putString("student_name", sname);
            editor.putString("student_house", shouse);
            editor.putString("student_street", sstreet);
            editor.putString("student_area", sarea);
            editor.putString("student_land", sland);
            editor.putString("student_pincode", spincode);
            editor.putString("student_email", semail);
            editor.putString("student_number", snumber);
            editor.commit();
            Toast.makeText(this, "" + sname, Toast.LENGTH_SHORT).show();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("student_name", sname);
            hashMap.put("student_house", shouse);
            hashMap.put("student_street", sstreet);
            hashMap.put("student_area", sarea);
            hashMap.put("student_land", sland);
            hashMap.put("student_pincode", spincode);
            hashMap.put("student_email", semail);
            hashMap.put("student_number", snumber);

            myRef.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ProfileStudent.this, "Successfully Updated" , Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("profilestudent", e.getMessage());
                }
            });
        }

    }
}
