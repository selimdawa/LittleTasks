package com.flatcode.littletasks.Activity;

import static com.flatcode.littletasks.Unit.DATA.EMPTY;
import static com.flatcode.littletasks.Unit.DATA.FirebaseUserUid;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littletasks.Model.OBJECT;
import com.flatcode.littletasks.Model.Plan;
import com.flatcode.littletasks.R;
import com.flatcode.littletasks.Unit.DATA;
import com.flatcode.littletasks.Unit.THEME;
import com.flatcode.littletasks.Unit.VOID;
import com.flatcode.littletasks.databinding.ActivityCategoryAddBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class CategoryAddActivity extends AppCompatActivity {

    private ActivityCategoryAddBinding binding;
    Activity activity;
    Context context = activity = CategoryAddActivity.this;

    private Uri imageUri = null;
    private ProgressDialog dialog;

    String planId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryAddBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        planId = getIntent().getStringExtra(DATA.ID);

        dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait...");
        dialog.setCanceledOnTouchOutside(false);

        binding.toolbar.nameSpace.setText(R.string.add_new_category);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.editImage.setOnClickListener(v -> VOID.CropImageSquare(activity));
        binding.toolbar.ok.setOnClickListener(v -> validateData());

        PlanName();
    }

    private String title = EMPTY;

    private void validateData() {
        //get data
        title = binding.categoryEt.getText().toString().trim();

        //validate data
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(context, "Enter Title...", Toast.LENGTH_SHORT).show();
        } else if (imageUri == null) {
            Toast.makeText(context, "Pick Image...", Toast.LENGTH_SHORT).show();
        } else {
            uploadToStorage();
        }
    }

    private void PlanName() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.PLANS).child(planId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Plan plan = dataSnapshot.getValue(Plan.class);
                assert plan != null;
                String planName = DATA.EMPTY + plan.getName();
                binding.plan.setText(planName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void uploadToStorage() {
        dialog.setMessage("Uploading Category...");
        dialog.show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.CATEGORIES);
        String id = ref.push().getKey();

        String filePathAndName = "Images/Category/" + id;

        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context));
        reference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            String uploadedImageUrl = EMPTY + uriTask.getResult();

            uploadInfoDB(uploadedImageUrl, id, ref);
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Category upload failed due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void uploadInfoDB(String uploadedImageUrl, String id, DatabaseReference ref) {
        dialog.setMessage("Uploading category info...");
        dialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(DATA.PUBLISHER, EMPTY + FirebaseUserUid);
        hashMap.put(DATA.TIMESTAMP, System.currentTimeMillis());
        hashMap.put(DATA.ID, id);
        hashMap.put(DATA.NAME, EMPTY + title);
        hashMap.put(DATA.PLAN, EMPTY + planId);
        hashMap.put(DATA.IMAGE, uploadedImageUrl);

        assert id != null;
        ref.child(id).setValue(hashMap).addOnSuccessListener(unused -> {
            dialog.dismiss();
            getItems(id);
            Toast.makeText(context, "Successfully uploaded...", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Failure to upload to db due to : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void getItems(String categoryId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.OBJECTS);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    OBJECT object = data.getValue(OBJECT.class);
                    assert object != null;
                    if (object.getPublisher().equals(DATA.FirebaseUserUid))
                        checkObject(object.getId(), categoryId, object.getName(), object.getPoints());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkObject(String objectId, String categoryId, String name, int points) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.PLANS).child(planId).child(DATA.AUTO_TASKS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(objectId).exists()) {
                    addAutoTasks(name, points, categoryId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addAutoTasks(String name, int point, String categoryId) {
        dialog.setMessage("Uploading Objects...");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.TASKS);
        String id = ref.push().getKey();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(DATA.PUBLISHER, DATA.EMPTY + DATA.FirebaseUserUid);
        hashMap.put(DATA.ID, id);
        hashMap.put(DATA.NAME, DATA.EMPTY + name);
        hashMap.put(DATA.POINTS, point);
        hashMap.put(DATA.AVAILABLE_POINTS, DATA.ZERO);
        hashMap.put(DATA.RANK, DATA.ZERO);
        hashMap.put(DATA.CATEGORY, DATA.EMPTY + categoryId);
        hashMap.put(DATA.TIMESTAMP, System.currentTimeMillis());
        hashMap.put(DATA.START, DATA.ZERO);
        hashMap.put(DATA.END, DATA.ZERO);

        assert id != null;
        ref.child(id).setValue(hashMap);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = CropImage.getPickImageResultUri(context, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(context, uri)) {
                imageUri = uri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                VOID.CropImageSquare(activity);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                binding.image.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Error! " + error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}