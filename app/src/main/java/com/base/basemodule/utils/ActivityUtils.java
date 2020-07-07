package com.base.basemodule.utils;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ActivityUtils {

    public static void replaceFragmentInActivity(@NonNull FragmentManager fragmentManager,
                                                 @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.commitAllowingStateLoss();
    }

    public static void replaceFragmentInActivity(@NonNull FragmentManager fragmentManager,
                                                 @NonNull Fragment fragment, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(fragment, tag);
        transaction.commitAllowingStateLoss();
    }


    public static void removeFragmentInActivityByTag(@NonNull FragmentManager fragmentManager, String tag) {
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment != null) {
            fragmentManager.beginTransaction().remove(fragment);
        }
    }


    public static void replaceFragmentInActivity(@NonNull FragmentManager fragmentManager,
                                                 @NonNull Fragment fragment, int frameId, boolean isToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragment != null && fragment.isAdded()) {
            //
            return;
        }
//        transaction.setCustomAnimations(R.anim.activity_right_in, R.anim.activity_right_out);
        transaction.replace(frameId, fragment).show(fragment);
        if (isToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commitAllowingStateLoss();
    }


    public static Fragment findFragmentByTag(FragmentManager fragmentManager, String tag) {
        return fragmentManager.findFragmentByTag(tag);
    }

    public static void hideFragment(FragmentManager fragmentManager, Fragment fragment) {
        if (fragment != null) {
            fragmentManager.beginTransaction().hide(fragment).commitAllowingStateLoss();
        }
    }

    public static void showFragment(FragmentManager fragmentManager, Fragment fragment) {
        if (fragment != null) {
            fragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss();
        }
    }

    public static void addFragmentByTag(FragmentManager fragmentManager, Fragment fragment, int resID, String tag) {
        if (fragment != null && !fragment.isAdded()) {
            fragmentManager.beginTransaction().add(resID, fragment, tag).commitAllowingStateLoss();
        }
    }
}
