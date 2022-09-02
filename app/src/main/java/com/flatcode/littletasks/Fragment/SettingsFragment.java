package com.flatcode.littletasks.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.flatcode.littletasks.Adapter.SettingAdapter;
import com.flatcode.littletasks.Model.Category;
import com.flatcode.littletasks.Model.OBJECT;
import com.flatcode.littletasks.Model.Plan;
import com.flatcode.littletasks.Model.Setting;
import com.flatcode.littletasks.Model.Task;
import com.flatcode.littletasks.Model.User;
import com.flatcode.littletasks.R;
import com.flatcode.littletasks.Unit.CLASS;
import com.flatcode.littletasks.Unit.DATA;
import com.flatcode.littletasks.Unit.VOID;
import com.flatcode.littletasks.databinding.FragmentSettingsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Objects;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    private ArrayList<Setting> list;
    private SettingAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(LayoutInflater.from(getContext()), container, false);

        binding.recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new SettingAdapter(getContext(), list);
        binding.recyclerView.setAdapter(adapter);

        binding.toolbar.item.setOnClickListener(v ->
                VOID.IntentExtra(getContext(), CLASS.PROFILE, DATA.PROFILE_ID, DATA.FirebaseUserUid));

        return binding.getRoot();
    }

    int C = 0, P = 0, O = 0, F = 0;

    private void nrItems() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.CATEGORIES);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                C = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Category item = data.getValue(Category.class);
                    assert item != null;
                    if (item.getId() != null)
                        if (item.getPublisher().equals(DATA.FirebaseUserUid))
                            C++;
                }
                nrPlans();
            }

            private void nrPlans() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.PLANS);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        P = 0;
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Plan item = data.getValue(Plan.class);
                            assert item != null;
                            if (item.getId() != null)
                                if (item.getPublisher().equals(DATA.FirebaseUserUid))
                                    P++;
                        }
                        nrObjects();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            private void nrObjects() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.OBJECTS);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        O = 0;
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            OBJECT item = data.getValue(OBJECT.class);
                            assert item != null;
                            if (item.getId() != null)
                                if (item.getPublisher().equals(DATA.FirebaseUserUid))
                                    O++;
                        }
                        nrFavorites();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            private void nrFavorites() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.FAVORITES).child(DATA.FirebaseUserUid);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        F = O;
                        F = (int) dataSnapshot.getChildrenCount();
                        loadSettings(C, P, O, F);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadUserInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.USERS);
        reference.child(Objects.requireNonNull(DATA.FirebaseUserUid)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User item = snapshot.getValue(User.class);
                assert item != null;
                String ProfileImage = item.getProfileImage();
                String Username = item.getUsername();
                VOID.Glide(true, getContext(), ProfileImage, binding.toolbar.imageProfile);
                binding.toolbar.username.setText(Username);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadSettings(int categories, int plans, int objects, int favorites) {
        list.clear();
        Setting item = new Setting("1", "Edit Profile", R.drawable.ic_edit_white, 0, CLASS.PROFILE_EDIT);
        Setting item2 = new Setting("2", "Categories", R.drawable.ic_category, categories, CLASS.CATEGORIES);
        Setting item3 = new Setting("4", "Plans", R.drawable.ic_list, plans, DATA.PLANS);
        Setting item4 = new Setting("7", "Objects", R.drawable.ic_object, objects, CLASS.OBJECTS);
        Setting item5 = new Setting("9", "Favorites", R.drawable.ic_star_selected, favorites, CLASS.FAVORITES);
        Setting item6 = new Setting("10", "About App", R.drawable.ic_info, 0);
        Setting item7 = new Setting("11", "Logout", R.drawable.ic_logout_white, 0);
        Setting item8 = new Setting("12", "Share App", R.drawable.ic_share, 0);
        Setting item9 = new Setting("13", "Rate APP", R.drawable.ic_heart_selected, 0);
        Setting item10 = new Setting("14", "Privacy Policy", R.drawable.ic_privacy_policy, 0, CLASS.PRIVACY_POLICY);
        list.add(item);
        list.add(item2);
        list.add(item3);
        list.add(item4);
        list.add(item5);
        list.add(item6);
        list.add(item7);
        list.add(item8);
        list.add(item9);
        list.add(item10);
        adapter.notifyDataSetChanged();
    }

    private void getPoints() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.TASKS);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                int a = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Task item = snapshot.getValue(Task.class);
                    assert item != null;
                    i = i + item.getPoints();
                    a = a + item.getAVPoints();
                }
                binding.toolbar.all.setText(MessageFormat.format("{0}{1}", DATA.EMPTY, i));
                binding.toolbar.availablePoints.setText(MessageFormat.format("{0}{1}", DATA.EMPTY, a));
                binding.toolbar.level.setText(MessageFormat.format("{0}{1}", DATA.EMPTY, VOID.levelPoint(a, 10)));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        loadUserInfo();
        getPoints();
        nrItems();
        super.onResume();
    }
}