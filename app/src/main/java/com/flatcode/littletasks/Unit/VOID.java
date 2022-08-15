package com.flatcode.littletasks.Unit;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.flatcode.littletasks.BuildConfig;
import com.flatcode.littletasks.Model.Category;
import com.flatcode.littletasks.Model.OBJECT;
import com.flatcode.littletasks.Model.Plan;
import com.flatcode.littletasks.Model.Task;
import com.flatcode.littletasks.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Objects;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class VOID {

    public static void IntentClear(Context context, Class c) {
        Intent intent = new Intent(context, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void Intent(Context context, Class c) {
        Intent intent = new Intent(context, c);
        context.startActivity(intent);
    }

    public static void IntentExtra(Context context, Class c, String key, String value) {
        Intent intent = new Intent(context, c);
        intent.putExtra(key, value);
        context.startActivity(intent);
    }

    public static void IntentExtra2(Context context, Class c, String key, String value, String key2, String value2) {
        Intent intent = new Intent(context, c);
        intent.putExtra(key, value);
        intent.putExtra(key2, value2);
        context.startActivity(intent);
    }

    public static void deleteItem(String database, Context context, String id, String name) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait");
        dialog.setMessage("Deleting " + name + " ...");
        dialog.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(database);
        reference.child(id).removeValue().addOnSuccessListener(unused -> {
            dialog.dismiss();
            Toast.makeText(context, "The item has been deleted successfully...", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    public static void Glide(Boolean isUser, Context context, String Url, ImageView Image) {
        try {
            if (Url.equals(DATA.BASIC)) {
                if (isUser) {
                    Image.setImageResource(R.drawable.basic_user);
                } else {
                    Image.setImageResource(R.drawable.basic_book);
                }
            } else {
                Glide.with(context).load(Url).placeholder(R.color.image_profile).into(Image);
            }
        } catch (Exception e) {
            Image.setImageResource(R.drawable.basic_book);
        }
    }

    public static void GlideBlur(Boolean isUser, Context context, String Url, ImageView Image, int level) {
        try {
            if (Url.equals(DATA.BASIC)) {
                if (isUser) {
                    Image.setImageResource(R.drawable.basic_user);
                } else {
                    Image.setImageResource(R.drawable.basic_book);
                }
            } else {
                Glide.with(context).load(Url).placeholder(R.color.image_profile)
                        .apply(bitmapTransform(new BlurTransformation(level))).into(Image);
            }
        } catch (Exception e) {
            Image.setImageResource(R.drawable.basic_book);
        }
    }

    public static void closeApp(Context context, Activity a) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_close_app);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.findViewById(R.id.yes).setOnClickListener(v -> a.finish());
        dialog.findViewById(R.id.no).setOnClickListener(v -> dialog.cancel());

        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setAttributes(lp);
    }

    public static void dialogLogout(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.findViewById(R.id.yes).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            IntentClear(context, CLASS.AUTH);
        });

        dialog.findViewById(R.id.no).setOnClickListener(v -> dialog.cancel());

        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setAttributes(lp);
    }

    public static void shareApp(Context context) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "share app");
        shareIntent.putExtra(Intent.EXTRA_TEXT, " Download the app now from Google Play " + " https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        context.startActivity(Intent.createChooser(shareIntent, "Choose how to share"));
    }

    public static void rateApp(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public static void dialogAboutApp(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_about_app);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.findViewById(R.id.website).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(getWebsiteIntent());
            }

            public Intent getWebsiteIntent() {
                return new Intent(Intent.ACTION_VIEW, Uri.parse(DATA.WEBSITE));
            }
        });

        dialog.findViewById(R.id.facebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(getOpenFacebookIntent());
            }

            public Intent getOpenFacebookIntent() {
                try {
                    context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/" + DATA.FB_ID));
                } catch (Exception e) {
                    return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + DATA.FB_ID));
                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public static void isFavorite(final ImageView add, final String TaskId, final String UserId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(DATA.FAVORITES).child(UserId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(TaskId).exists()) {
                    add.setImageResource(R.drawable.ic_remove);
                    add.setTag("added");
                } else {
                    add.setImageResource(R.drawable.ic___add);
                    add.setTag("add");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void checkFavorite(ImageView image, String TaskId) {
        if (image.getTag().equals("add")) {
            FirebaseDatabase.getInstance().getReference().child(DATA.FAVORITES).child(DATA.FirebaseUserUid)
                    .child(TaskId).setValue(true);
        } else {
            FirebaseDatabase.getInstance().getReference().child(DATA.FAVORITES).child(DATA.FirebaseUserUid)
                    .child(TaskId).removeValue();
        }
    }

    public static void isPlan(final ImageView add, final String ObjectId, final String planId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(DATA.PLANS)
                .child(planId).child(DATA.AUTO_TASKS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(ObjectId).exists()) {
                    add.setImageResource(R.drawable.ic_heart_selected);
                    add.setTag("added");
                } else {
                    add.setImageResource(R.drawable.ic_heart_unselected);
                    add.setTag("add");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void checkPlan(ImageView image, String ObjectId, String planId) {
        if (image.getTag().equals("add")) {
            FirebaseDatabase.getInstance().getReference().child(DATA.PLANS).child(planId).child(DATA.AUTO_TASKS)
                    .child(ObjectId).setValue(true);
        } else {
            FirebaseDatabase.getInstance().getReference().child(DATA.PLANS).child(planId).child(DATA.AUTO_TASKS)
                    .child(ObjectId).removeValue();
        }
    }

    public static void moreObject(Context context, OBJECT item) {
        String id = item.getId();
        String name = item.getName();

        //options to show in dialog
        String[] options = {"Edit", "Delete"};

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Options").setItems(options, (dialog, which) -> {
            //handle dialog option click
            if (which == 0) {
                //Edit clicked ,Open new activity to edit the book info
                IntentExtra(context, CLASS.OBJECT_EDIT, DATA.ID, id);
            } else if (which == 1) {
                //Delete Clicked
                dialogOptionDelete(context, DATA.OBJECTS, DATA.EMPTY + id, DATA.EMPTY + name);
            }
        }).show();
    }

    public static void moreTask(Context context, Task item) {
        String id = item.getId();
        String name = item.getName();
        String category = item.getCategory();
        long start = item.getStart();
        long end = item.getEnd();

        String[] one = {"Edit", "Delete"};
        String[] two = {"Edit", "Delete", "Start Again"};
        String[] three = {"Edit", "Delete", "Start Again", "Not End"};
        String[] options = new String[0];
        //options to show in dialog
        if (start == 0 && end == 0) {
            options = one;
        } else if (start != 0 && end == 0) {
            options = two;
        } else if (start != 0) {
            options = three;
        }

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Options").setItems(options, (dialog, which) -> {
            //handle dialog option click
            if (which == 0) {
                //Edit clicked ,Open new activity to edit the book info
                IntentExtra2(context, CLASS.TASK_EDIT, DATA.TASK_ID, id, DATA.CATEGORY_ID, category);
            } else if (which == 1) {
                //Delete Clicked
                dialogOptionDelete(context, DATA.TASKS, DATA.EMPTY + id, DATA.EMPTY + name);
            } else if (which == 2) {
                //Delete Clicked
                EditTaskStatus(context, id, true, false);
            } else if (which == 3) {
                //Delete Clicked
                EditTaskStatus(context, id, false, true);
            }
        }).show();
    }

    private static void EditTaskStatus(Context context, String taskId, boolean startStatus, boolean endStatus) {

        HashMap<String, Object> hashMap = new HashMap<>();
        if (startStatus)
            hashMap.put(DATA.START, DATA.ZERO);
        if (endStatus)
            hashMap.put(DATA.END, DATA.ZERO);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.TASKS);
        ref.child(taskId).updateChildren(hashMap).addOnSuccessListener(unused -> {
            if (startStatus)
                Toast.makeText(context, "Task started again...", Toast.LENGTH_SHORT).show();
            if (endStatus)
                Toast.makeText(context, "Task did not End...", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> Toast.makeText(context, DATA.EMPTY + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public static void moreCategory(Context context, Category item) {
        String id = item.getId();
        String name = item.getName();
        String plan = item.getPlan();

        //options to show in dialog
        String[] options = {"Add Task", "Edit", "Delete"};

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Options").setItems(options, (dialog, which) -> {
            //handle dialog option click
            if (which == 0) {
                IntentExtra2(context, CLASS.TASK_ADD, DATA.CATEGORY_ID, id, DATA.PLAN_ID, plan);
            } else if (which == 1) {
                IntentExtra2(context, CLASS.CATEGORY_EDIT, DATA.CATEGORY_ID, id, DATA.PLAN_ID, plan);
            } else if (which == 2) {
                dialogOptionDelete(context, DATA.CATEGORIES, DATA.EMPTY + id, DATA.EMPTY + name);
            }
        }).show();
    }

    public static void morePlan(Context context, Plan item) {
        String id = item.getId();
        String name = item.getName();

        //options to show in dialog
        String[] options = {"Edit", "Delete"};

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Options").setItems(options, (dialog, which) -> {
            //handle dialog option click
            if (which == 0) {
                //Edit clicked ,Open new activity to edit the book info
                IntentExtra(context, CLASS.PLAN_EDIT, DATA.ID, id);
            } else if (which == 1) {
                //Delete Clicked
                dialogOptionDelete(context, DATA.PLANS, DATA.EMPTY + id, DATA.EMPTY + name);
            }
        }).show();
    }

    public static void dialogOptionDelete(Context context, String database, String id, String name) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        TextView title = dialog.findViewById(R.id.title);
        title.setText(R.string.do_you_want_to_delete_the);
        String Title = title.getText().toString();
        if (database.equals(DATA.CATEGORIES)) {
            title.setText(MessageFormat.format("{0} Category?", Title));
        } else if (database.equals(DATA.OBJECTS)) {
            title.setText(MessageFormat.format("{0} Object?", Title));
        } else if (database.equals(DATA.TASKS)) {
            title.setText(MessageFormat.format("{0} Task?", Title));
        } else if (database.equals(DATA.PLANS)) {
            title.setText(MessageFormat.format("{0} Plan?", Title));
        }

        dialog.findViewById(R.id.yes).setOnClickListener(v -> {
            dialog.dismiss();
            deleteItem(database, context, id, name);
        });
        dialog.findViewById(R.id.no).setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public static void CropImageSquare(Activity activity) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setMinCropResultSize(DATA.MIX_SQUARE, DATA.MIX_SQUARE)
                .setAspectRatio(1, 1)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(activity);
    }

    public static void CropImageWide(Activity activity) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setMinCropResultSize(DATA.MIX_SQUARE, DATA.MIX_SQUARE)
                .setAspectRatio(2, 1)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(activity);
    }

    public static void isTask(final Context context, final ImageView image, final String taskId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(DATA.TASKS).child(taskId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Task item = dataSnapshot.getValue(Task.class);
                assert item != null;
                if (item.getTimestamp() != DATA.ZERO) {
                    image.setImageResource(R.drawable.ic_star_unselected);
                    image.setOnClickListener(v -> FirebaseDatabase.getInstance().getReference()
                            .child(DATA.TASKS).child(taskId).child(DATA.START).setValue(System.currentTimeMillis()));
                }
                if (item.getStart() != DATA.ZERO) {
                    image.setImageResource(R.drawable.ic_star_half);
                    image.setOnClickListener(v -> {
                        FirebaseDatabase.getInstance().getReference()
                                .child(DATA.TASKS).child(taskId).child(DATA.END).setValue(System.currentTimeMillis());
                        int points = item.getPoints();
                        int AVPoints = item.getAVPoints();
                        if (AVPoints != points)
                            FirebaseDatabase.getInstance().getReference()
                                    .child(DATA.TASKS).child(taskId).child(DATA.AVAILABLE_POINTS).setValue(points);
                    });
                }
                if (item.getEnd() != DATA.ZERO) {
                    image.setImageResource(R.drawable.ic_star_selected);
                    image.setOnClickListener(v -> Toast.makeText(context, "Task Completed", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static int levelPoint(int AVPoints, int point) {
        int half = point / 2, level, S1 = point * 5, S2 = loop(S1, half, 3),
                S3 = loop(S2, half, 4), S4 = loop(S3, half, 5), S5 = loop(S4, half, 6),
                S6 = loop(S5, half, 7), S7 = loop(S6, half, 8), S8 = loop(S7, half, 9),
                S9 = loop(S8, half, 10), S10 = loop(S9, half, 11), S11 = loop(S10, half, 12),
                S12 = loop(S11, half, 13), S13 = loop(S12, half, 14), S14 = loop(S13, half, 15),
                S15 = loop(S14, half, 16), S16 = loop(S15, half, 17), S17 = loop(S16, half, 18),
                S18 = loop(S17, half, 19), S19 = loop(S18, half, 20), S20 = loop(S19, half, 21);
        // 0 TO 5
        if (AVPoints <= S1) {
            level = AVPoints / point;
        }
        // 5 TO 100
        else if (AVPoints <= S20) {
            int a;
            // 5 TO 10
            if (AVPoints <= S2) {
                a = AVPoints - S1;
                level = 5;
                //10 = 10+5;
                point = point + half;
            }
            // 10 TO 15
            else if (AVPoints <= S3) {
                a = AVPoints - S2;
                level = 10;
                point = point + (half * 2);
            }
            // 15 TO 20
            else if (AVPoints <= S4) {
                a = AVPoints - S3;
                level = 15;
                point = point + (half * 3);
            }
            // 20 TO 25
            else if (AVPoints <= S5) {
                a = AVPoints - S4;
                level = 20;
                point = point + (half * 4);
            }
            // 25 TO 30
            else if (AVPoints <= S6) {
                a = AVPoints - S5;
                level = 25;
                point = point + (half * 5);
            }
            // 30 TO 35
            else if (AVPoints <= S7) {
                a = AVPoints - S6;
                level = 30;
                point = point + (half * 6);
            }
            // 35 TO 40
            else if (AVPoints <= S8) {
                a = AVPoints - S7;
                level = 35;
                point = point + (half * 7);
            }
            // 40 TO 45
            else if (AVPoints <= S9) {
                a = AVPoints - S8;
                level = 40;
                point = point + (half * 8);
            }
            // 45 TO 50
            else if (AVPoints <= S10) {
                a = AVPoints - S9;
                level = 45;
                point = point + (half * 9);
            }
            // 50 TO 55
            else if (AVPoints <= S11) {
                a = AVPoints - S10;
                level = 50;
                point = point + (half * 10);
            }
            // 55 TO 60
            else if (AVPoints <= S12) {
                a = AVPoints - S11;
                level = 55;
                point = point + (half * 11);
            }
            // 60 TO 65
            else if (AVPoints <= S13) {
                a = AVPoints - S12;
                level = 60;
                point = point + (half * 12);
            }
            // 65 TO 70
            else if (AVPoints <= S14) {
                a = AVPoints - S13;
                level = 65;
                point = point + (half * 13);
            }
            // 70 TO 75
            else if (AVPoints <= S15) {
                a = AVPoints - S14;
                level = 70;
                point = point + (half * 14);
            }
            // 75 TO 80
            else if (AVPoints <= S16) {
                a = AVPoints - S15;
                level = 75;
                point = point + (half * 15);
            }
            // 80 TO 85
            else if (AVPoints <= S17) {
                a = AVPoints - S16;
                level = 80;
                point = point + (half * 16);
            }
            // 85 TO 90
            else if (AVPoints <= S18) {
                a = AVPoints - S17;
                level = 85;
                point = point + (half * 17);
            }
            // 90 TO 95
            else if (AVPoints <= S19) {
                a = AVPoints - S18;
                level = 90;
                point = point + (half * 18);
            }
            // 95 TO 100
            else {
                a = AVPoints - S19;
                level = 95;
                point = point + (half * 19);
            }
            level = level + (a / point);
        } else {
            level = 100;
        }
        return level;
        //MAX 5750 - 100
    }

    private static int loop(int S, int half, int number) {
        int SA;
        SA = S + (half * number * half);
        return SA;
    }

    public static void Intro(Context context, ImageView background, ImageView backWhite, ImageView backDark) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        if (sharedPreferences.getString("color_option", "ONE").equals("ONE")) {
            background.setImageResource(R.drawable.background_day);
            backWhite.setVisibility(View.VISIBLE);
            backDark.setVisibility(View.GONE);
        } else if (sharedPreferences.getString("color_option", "NIGHT_ONE").equals("NIGHT_ONE")) {
            background.setImageResource(R.drawable.background_night);
            backWhite.setVisibility(View.GONE);
            backDark.setVisibility(View.VISIBLE);
        }
    }

    public static void Logo(Context context, ImageView background) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        if (sharedPreferences.getString("color_option", "ONE").equals("ONE")) {
            background.setImageResource(R.drawable.logo);
        } else if (sharedPreferences.getString("color_option", "NIGHT_ONE").equals("NIGHT_ONE")) {
            background.setImageResource(R.drawable.logo_night);
        }
    }

    public static String getFileExtension(Uri uri, Context context) {
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}