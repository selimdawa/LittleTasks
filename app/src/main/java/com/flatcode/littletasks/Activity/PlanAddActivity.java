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

import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.littletasks.R;
import com.flatcode.littletasks.Unit.DATA;
import com.flatcode.littletasks.Unit.THEME;
import com.flatcode.littletasks.Unit.VOID;
import com.flatcode.littletasks.databinding.ActivityPlanAddBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class PlanAddActivity extends AppCompatActivity {

    private ActivityPlanAddBinding binding;
    Activity activity;
    Context context = activity = PlanAddActivity.this;

    private Uri imageUri = null;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityPlanAddBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait...");
        dialog.setCanceledOnTouchOutside(false);

        binding.toolbar.nameSpace.setText(R.string.add_new_plan);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.editImage.setOnClickListener(v -> VOID.CropImageWide(activity));
        binding.toolbar.ok.setOnClickListener(v -> validateData());
    }

    private String title = EMPTY;

    private void validateData() {
        //get data
        title = binding.planEt.getText().toString().trim();

        //validate data
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(context, "Enter Title...", Toast.LENGTH_SHORT).show();
        } else if (imageUri == null) {
            Toast.makeText(context, "Pick Image...", Toast.LENGTH_SHORT).show();
        } else {
            uploadToStorage();
        }
    }

    private void uploadToStorage() {
        dialog.setMessage("Uploading Plan...");
        dialog.show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.PLANS);
        String id = ref.push().getKey();

        String filePathAndName = "Images/Plans/" + id;

        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context));
        reference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            String uploadedImageUrl = EMPTY + uriTask.getResult();

            uploadInfoDB(uploadedImageUrl, id, ref);
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Plan upload failed due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void uploadInfoDB(String uploadedImageUrl, String id, DatabaseReference ref) {
        dialog.setMessage("Uploading plan info...");
        dialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(DATA.PUBLISHER, EMPTY + FirebaseUserUid);
        hashMap.put(DATA.TIMESTAMP, System.currentTimeMillis());
        hashMap.put(DATA.ID, id);
        hashMap.put(DATA.NAME, EMPTY + title);
        hashMap.put(DATA.IMAGE, uploadedImageUrl);

        assert id != null;
        ref.child(id).setValue(hashMap).addOnSuccessListener(unused -> {
            dialog.dismiss();
            Toast.makeText(context, "Successfully uploaded...", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Failure to upload to db due to :" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                VOID.CropImageWide(activity);
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