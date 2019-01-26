package com.nehvedovich.vladimir.pogoda.screens.utils;

import android.content.Context;
import android.widget.Toast;

public final class ViewUtilities {

    public static void makeToast(Context context, String toast) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }

    private ViewUtilities() {
    }
}