package com.abhi.vedanam.teacher;

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

public class ProfileTeacher extends AppCompatActivity {

    ProgressDialog progressDialog;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ImageView imageTeacher;
    EditText nameTeacher, housenumberTeacher, streetnumberTeacher, arealocalityTeacher,
            landmarkTeacher, pincodeTeacher, emailTeacher, numberTeacher, coaching, subject;
    Button teacherbutton;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("TeacherProfile").child(user.getUid());
    String permissions[] = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private StorageReference mStorageRef  = FirebaseStorage.getInstance().getReference();
    byte bb[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_teacher);

        progressDialog = new ProgressDialog(ProfileTeacher.this);
        imageTeacher = findViewById(R.id.imageteacher);
        nameTeacher = findViewById(R.id.nameteacher);
        coaching = findViewById(R.id.coachingname);
        subject = findViewById(R.id.subjectclass);
        housenumberTeacher = findViewById(R.id.housenumberteacher);
        streetnumberTeacher = findViewById(R.id.streetnumberteacher);
        arealocalityTeacher = findViewById(R.id.arealocalityteacher);
        landmarkTeacher = findViewById(R.id.landmarkteacher);
        pincodeTeacher = findViewById(R.id.pincodeteacher);
        emailTeacher = findViewById(R.id.emailteacher);
        numberTeacher = findViewById(R.id.numberteacher);
        teacherbutton = findViewById(R.id.profileteacher);

        preferences = getSharedPreferences("TeacherProfileDetails", Context.MODE_PRIVATE);
        editor = preferences.edit();

        String t_name = preferences.getString("teacher_name", null);
        String t_coaching = preferences.getString("teacher_coaching", null);
        String t_subject = preferences.getString("teacher_subject", null);
        String t_house = preferences.getString("teacher_house", null);
        String t_street = preferences.getString("teacher_street", null);
        String t_area = preferences.getString("teacher_area", null);
        String t_landmark = preferences.getString("teacher_landmark", null);
        String t_pincode = preferences.getString("teacher_pincode", null);
        String t_email = preferences.getString("teacher_email", null);
        String t_number = preferences.getString("teacher_number", null);

        if (t_name!=null&& t_coaching!=null&& t_subject!=null&& t_house != null && t_street != null && t_area != null && t_landmark != null
                && t_pincode != null&&t_email!=null&&t_number!=null){
            nameTeacher.setText(t_name);
            coaching.setText(t_coaching);
            subject.setText(t_subject);
            housenumberTeacher.setText(t_house);
            streetnumberTeacher.setText(t_street);
            arealocalityTeacher.setText(t_area);
            landmarkTeacher.setText(t_landmark);
            pincodeTeacher.setText(t_pincode);
            emailTeacher.setText(t_email);
            numberTeacher.setText(t_number);
        } else {
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String teacher_name = snapshot.child("" + "teacher_name").getValue(String.class);
                    nameTeacher.setText(teacher_name);
                    String teacher_coaching = snapshot.child("" + "teacher_coaching").getValue(String.class);
                    nameTeacher.setText(teacher_coaching);
                    String teacher_subject = snapshot.child("" + "teacher_subject").getValue(String.class);
                    nameTeacher.setText(teacher_subject);
                    String teacher_house = snapshot.child("teacher_house").getValue(String.class);
                    housenumberTeacher.setText(teacher_house);
                    String teacher_street = snapshot.child("teacher_street").getValue(String.class);
                    streetnumberTeacher.setText(teacher_street);
                    String teacher_area = snapshot.child("teacher_area").getValue(String.class);
                    arealocalityTeacher.setText(teacher_area);
                    String teacher_land = snapshot.child("teacher_land").getValue(String.class);
                    landmarkTeacher.setText(teacher_land);
                    String teacher_pincode = snapshot.child("teacher_pincode").getValue(String.class);
                    pincodeTeacher.setText(teacher_pincode);
                    String teacher_email = snapshot.child("teacher_email").getValue(String.class);
                    emailTeacher.setText(teacher_email);
                    String teacher_number = snapshot.child("teacher_number").getValue(String.class);
                    numberTeacher.setText(teacher_number);

                    editor.putString("teacher_name", teacher_name);
                    editor.putString("teacher_coaching", teacher_coaching);
                    editor.putString("teacher_subject", teacher_subject);
                    editor.putString("teacher_house", teacher_house);
                    editor.putString("teacher_street", teacher_street);
                    editor.putString("teacher_area", teacher_area);
                    editor.putString("teacher_land", teacher_land);
                    editor.putString("teacher_pincode", teacher_pincode);
                    editor.putString("teacher_email", teacher_email);
                    editor.putString("teacher_number", teacher_number);
                    editor.commit();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        functionpermission();
        setImage();

        imageTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setCancelable(false);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog_view);
                progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 101);
            }
        });

            teacherbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    profileteacher();
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
                imageTeacher.setImageBitmap(thumbnail);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                bb = bytes.toByteArray();

                if (bb != null) {
                    StorageReference storageReference = mStorageRef.child("Vedanamteacher/profilepic/"+user.getUid()+".jpg");
                    storageReference.putBytes(bb).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){
                                Log.e("jello","hello");
                                StorageReference storageReference = mStorageRef.child("Vedanamteacher/profilepic/"+user.getUid()+".jpg");
                                Log.e("success",storageReference+"");
                                final long SIZE = 1024 * 1024 * 15;
                                storageReference.getBytes(SIZE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        Log.e("success",bytes.length+"");
                                        try {
                                            File root = Environment.getExternalStorageDirectory();
                                            File folder = new File(root, "VedanamTeacher");
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
                                Toast.makeText(ProfileTeacher.this, "Successfully Upload", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(ProfileTeacher.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        }
    }

    private void setImage() {
        StorageReference storageReference = mStorageRef.child("Vedanamteacher/profilepic/"+user.getUid()+".jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ProfileTeacher.this).load(uri).into(imageTeacher);
            }
        });
    }

    public void functionpermission()
    {
        if (ActivityCompat.checkSelfPermission(ProfileTeacher.this,permissions[0])!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(ProfileTeacher.this,permissions,2);
        }
        else
        {
            Toast.makeText(ProfileTeacher.this, "", Toast.LENGTH_SHORT).show();
        }
    }
    public void profileteacher() {

        String tname = nameTeacher.getText().toString();
        String tcoaching = coaching.getText().toString();
        String tsubject = subject.getText().toString();
        String thouse = housenumberTeacher.getText().toString();
        String tstreet = streetnumberTeacher.getText().toString();
        String tarea = arealocalityTeacher.getText().toString();
        String tland = landmarkTeacher.getText().toString();
        String tpincode = pincodeTeacher.getText().toString();
        String temail = emailTeacher.getText().toString();
        String tnumber = numberTeacher.getText().toString();

        if(tname.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Enter Your Name ",Toast.LENGTH_SHORT).show();
        } else if(tcoaching.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Enter Your Coaching Name ",Toast.LENGTH_SHORT).show();
        } else if(tsubject.isEmpty()) {
            Toast.makeText(getApplicationContext(),"Enter Your subject ",Toast.LENGTH_SHORT).show();
        }else if (thouse.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Your House Number ", Toast.LENGTH_SHORT).show();
        } else if (tstreet.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Your Street/Building/Block ", Toast.LENGTH_SHORT).show();
        } else if (tarea.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Your Area/Locality Name ", Toast.LENGTH_SHORT).show();
        } else if (tland.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Your Landmark ", Toast.LENGTH_SHORT).show();
        } else if (tpincode.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Your Pincode ", Toast.LENGTH_SHORT).show();
        }else if(temail.equals("")){
            Toast.makeText(getApplicationContext(),"Enter Your Email ",Toast.LENGTH_SHORT).show();
        }else if (!temail.matches(emailPattern)) {
            Toast.makeText(getApplicationContext(),"Enter Valid Email Address",Toast.LENGTH_SHORT).show();
        }else if(tnumber.equals("")){
            Toast.makeText(getApplicationContext(),"Enter Your Number ",Toast.LENGTH_SHORT).show();
        }else{
            editor.putString("teacher_name", tname);
            editor.putString("teacher_coaching", tcoaching);
            editor.putString("teacher_subject", tsubject);
            editor.putString("teacher_house", thouse);
            editor.putString("teacher_street", tstreet);
            editor.putString("teacher_area", tarea);
            editor.putString("teacher_land", tland);
            editor.putString("teacher_pincode", tpincode);
            editor.putString("teacher_email", temail);
            editor.putString("teacher_number", tnumber);
            editor.commit();
            Toast.makeText(this, "" + tname, Toast.LENGTH_SHORT).show();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("teacher_uid", user.getUid());
            hashMap.put("teacher_name", tname);
            hashMap.put("teacher_coaching", tcoaching);
            hashMap.put("teacher_subject", tsubject);
            hashMap.put("teacher_house", thouse);
            hashMap.put("teacher_street", tstreet);
            hashMap.put("teacher_area", tarea);
            hashMap.put("teacher_land", tland);
            hashMap.put("teacher_pincode", tpincode);
            hashMap.put("teacher_email", temail);
            hashMap.put("teacher_number", tnumber);

            myRef.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ProfileTeacher.this, "Successfully Updated" , Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("profileteacher", e.getMessage());
                }
            });
        }

    }
}
