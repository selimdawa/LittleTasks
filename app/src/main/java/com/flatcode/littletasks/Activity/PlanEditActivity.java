package com.flatcode.littletasks.Activity;

import static com.flatcode.littletasks.Unit.DATA.EMPTY;

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

import com.flatcode.littletasks.Model.Plan;
import com.flatcode.littletasks.R;
import com.flatcode.littletasks.Unit.DATA;
import com.flatcode.littletasks.Unit.THEME;
import com.flatcode.littletasks.Unit.VOID;
import com.flatcode.littletasks.databinding.ActivityPlanAddBinding;
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

public class PlanEditActivity extends AppCompatActivity {

    private ActivityPlanAddBinding binding;
    Activity activity;
    Context context = activity = PlanEditActivity.this;

    String id;

    private Uri imageUri = null;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityPlanAddBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        id = intent.getStringExtra(DATA.ID);

        dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait");
        dialog.setCanceledOnTouchOutside(false);

        loadInfo();

        binding.toolbar.nameSpace.setText(R.string.edit_plan);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.editImage.setOnClickListener(v -> VOID.CropImageSquare(activity));
        binding.toolbar.ok.setOnClickListener(v -> validateData());
    }

    private String name = EMPTY;

    private void validateData() {
        name = binding.planEt.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(context, "Enter name...", Toast.LENGTH_SHORT).show();
        } else {
            if (imageUri == null) {
                update(EMPTY);
            } else {
                uploadImage();
            }
        }
    }

    private void uploadImage() {
        dialog.setMessage("Updating Plan...");
        dialog.show();

        String filePathAndName = "Images/Plans/" + id;

        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context));
        reference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            String uploadedImageUrl = EMPTY + uriTask.getResult();

            update(uploadedImageUrl);
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Failed to upload image due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void update(String imageUrl) {
        dialog.setMessage("Updating Plan image...");
        dialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(DATA.NAME, EMPTY + name);
        if (imageUri != null) {
            hashMap.put(DATA.IMAGE, EMPTY + imageUrl);
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.PLANS);
        reference.child(id).updateChildren(hashMap).addOnSuccessListener(unused -> {
            dialog.dismiss();
            Toast.makeText(context, "Plan updated...", Toast.LENGTH_SHORT).show();
            //finish();
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Failed to update db duo to " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void loadInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.PLANS);
        reference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Plan item = snapshot.getValue(Plan.class);
                assert item != null;
                String name = item.getName();
                String image = item.getImage();

                VOID.Glide(true, context, image, binding.image);
                binding.planEt.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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