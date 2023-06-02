//package com.hijewel.utils;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.support.annotation.RequiresApi;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AlertDialog;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class Permissions {
//
//    public static final int READ_SMS_ACTION = 100;
//
//    public static boolean checkPermissionSMS(final Context context) {
//        int currentAPIVersion = Build.VERSION.SDK_INT;
//        if (currentAPIVersion >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(context,
//                    Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
//                if (ActivityCompat.shouldShowRequestPermissionRationale(
//                        (Activity) context, Manifest.permission.RECEIVE_SMS)) {
//                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
//                            context);
//                    alertBuilder.setCancelable(true);
//                    alertBuilder.setTitle("Permission necessary");
//                    alertBuilder.setMessage(" RECEIVE_SMS Permission is necessary");
//                    alertBuilder.setPositiveButton(android.R.string.yes,
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog,
//                                                    int which) {
//                                    ActivityCompat
//                                            .requestPermissions(
//                                                    (Activity) context,
//                                                    new String[]{Manifest.permission.RECEIVE_SMS},
//                                                    READ_SMS_ACTION);
//                                }
//                            });
//                    AlertDialog alert = alertBuilder.create();
//                    alert.show();
//
//                } else {
//                    ActivityCompat.requestPermissions((Activity) context,
//                            new String[]{Manifest.permission.RECEIVE_SMS},
//                            READ_SMS_ACTION);
//                }
//                return false;
//            } else {
//                return true;
//            }
//        } else {
//            return true;
//        }
//    }
//
//
//}
