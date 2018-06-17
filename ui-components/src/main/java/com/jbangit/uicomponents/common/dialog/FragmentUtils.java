package com.jbangit.uicomponents.common.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Objects;

public class FragmentUtils {

    public static void saveState(Fragment fragment) {
        Bundle argument = fragment.getArguments();

        Field[] fields = fragment.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.getAnnotation(State.class) != null) {
                saveFiled(fragment, argument, field);
            }
        }
    }

    private static void saveFiled(Fragment fragment, Bundle arguments, Field field) {
        field.setAccessible(true);
        Class<?> type = field.getType();
        String keyName = getKeyName(field);

        try {
            if (type == int.class) {
                arguments.putInt(keyName, field.getInt(fragment));
            } else if (type == long.class) {
                arguments.putLong(keyName, field.getLong(fragment));
            } else if (type == boolean.class) {
                arguments.putBoolean(keyName, field.getBoolean(fragment));
            } else if (type == String.class) {
                arguments.putString(keyName, (String) field.get(fragment));
            } else if (Serializable.class.isAssignableFrom(type)) {
                arguments.putSerializable(keyName, (Serializable) field.get(fragment));
            } else {
                throw new IllegalArgumentException("This type of filed is not supports");
            }
        } catch (IllegalAccessException ignored) {

        }
    }

    @NonNull
    private static String getKeyName(Field field) {
        return "STATE_" + field.getName().toUpperCase();
    }

    public static void restoreState(Fragment fragment) {
        Bundle arguments = fragment.getArguments();

        if (arguments == null) {
            return;
        }

        Field[] fields = fragment.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(State.class) != null) {
                restoreField(fragment, arguments, field);
            }
        }
    }

    private static void restoreField(Fragment fragment, Bundle arguments, Field field) {
        field.setAccessible(true);
        Class<?> type = field.getType();
        String keyName = getKeyName(field);

        try {
            if (type == int.class) {
                field.setInt(fragment, arguments.getInt(keyName));
            } else if (type == long.class) {
                field.setLong(fragment, arguments.getLong(keyName));
            } else if (type == boolean.class) {
                field.setBoolean(fragment, arguments.getBoolean(keyName));
            } else if (type == String.class) {
                field.set(fragment, arguments.getString(keyName));
            } else if (Serializable.class.isAssignableFrom(type)) {
                field.set(fragment, arguments.getSerializable(keyName));
            } else {
                throw new IllegalArgumentException("This type of filed is not supports");
            }
        } catch (IllegalAccessException ignored) {

        }
    }

    public static void show(DialogFragment dialog, FragmentActivity activity, int requestCode) {
        dialog.setTargetFragment(null, requestCode);
        dialog.show(activity.getSupportFragmentManager(), dialog.getClass().getName());
        dialog.setArguments(new Bundle());
        saveState(dialog);
    }

    public static void show(DialogFragment dialog, Fragment fragment, int requestCode) {
        dialog.setTargetFragment(fragment, requestCode);
        dialog.show(
                Objects.requireNonNull(fragment.getFragmentManager()), dialog.getClass().getName());
        dialog.setArguments(new Bundle());
        saveState(dialog);
    }

    public static void setResult(DialogFragment dialog, boolean ok) {
        int requestCode = dialog.getTargetRequestCode();
        int resultCode = ok ? Activity.RESULT_OK : Activity.RESULT_CANCELED;
        if (dialog.getTargetFragment() == null) {
            FragmentActivity activity = dialog.getActivity();
            if (activity instanceof OnFragmentResultListener) {
                ((OnFragmentResultListener) activity)
                        .onFragmentResult(
                                dialog, requestCode, resultCode);
            }
        } else {
            Fragment fragment = dialog.getTargetFragment();
            if (fragment instanceof OnFragmentResultListener) {
                ((OnFragmentResultListener) fragment)
                        .onFragmentResult(
                                dialog, requestCode, resultCode);
            }
        }
    }
}
