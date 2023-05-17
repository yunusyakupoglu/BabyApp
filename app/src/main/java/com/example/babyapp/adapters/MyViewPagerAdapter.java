package com.example.babyapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.babyapp.fragments.CommentFragment;
import com.example.babyapp.fragments.QuestionFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {
    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new CommentFragment();
            case 1:
                return new QuestionFragment();
            default:
                return new CommentFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
