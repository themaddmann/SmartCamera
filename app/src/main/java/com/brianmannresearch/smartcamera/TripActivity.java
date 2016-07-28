package com.brianmannresearch.smartcamera;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;


public class TripActivity extends AppCompatActivity implements View.OnClickListener {

    Button startButton, continueButton, tripButton, logoutButton, deleteButton;
    File imagesFolder;

    String mode, username;
    int userid, tripid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            mode = extras.getString("mode");
            assert mode != null;
            if (mode.matches("login")){
                userid = extras.getInt("userid");
                username = extras.getString("username");
            }
        }


        startButton = (Button) findViewById(R.id.startButton);
        continueButton = (Button) findViewById(R.id.continueButton);
        tripButton = (Button) findViewById(R.id.tripButton);
        logoutButton = (Button) findViewById(R.id.logoutButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);


        startButton.setOnClickListener(this);
        continueButton.setOnClickListener(this);
        tripButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startButton:
                showNewTripAlert();
                break;
            case R.id.continueButton:
                showContinueTripAlert();
                break;
            case R.id.tripButton:
                showTripAlert();
                break;
            case R.id.logoutButton:
                showFinishAlert();
                break;
            case R.id.deleteButton:
                showDeleteAlert();
                break;
        }
    }

    @Override
    public void onBackPressed(){

    }

    private void showFinishAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                });
        final AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private void showTripAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        alertDialog.setMessage("What trip number do you want to view?")
                .setCancelable(false)
                .setView(inflater.inflate(R.layout.trip_dialog, null))
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Dialog f = (Dialog) dialogInterface;
                        EditText text = (EditText) f.findViewById(R.id.tripID);
                        tripid = Integer.parseInt(text.getText().toString());
                        Intent galleryIntent = new Intent(TripActivity.this, GalleryActivity.class);
                        galleryIntent.putExtra("tripid", tripid);
                        galleryIntent.putExtra("username", username);
                        startActivity(galleryIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                });
        final AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private void showNewTripAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        alertDialog.setMessage("What trip number is this?")
                .setCancelable(false)
                .setView(inflater.inflate(R.layout.trip_dialog, null))
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Dialog f = (Dialog) dialogInterface;
                        EditText text = (EditText) f.findViewById(R.id.tripID);
                        tripid = Integer.parseInt(text.getText().toString());
                        imagesFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), username + "_Trip_" + tripid);
                    if (imagesFolder.exists() && imagesFolder.isDirectory() && (imagesFolder.listFiles().length != 0)) {
                        showExistsNewAlert();
                    }else {
                        if ("login".matches(mode)) {
                            Intent cameraIntent = new Intent(TripActivity.this, CameraActivity.class);
                            cameraIntent.putExtra("userid", userid);
                            cameraIntent.putExtra("tripid", tripid);
                            cameraIntent.putExtra("username", username);
                            cameraIntent.putExtra("mode", "new");
                            startActivity(cameraIntent);
                        } else {
                            Intent cameraIntent = new Intent(TripActivity.this, GuestActivity.class);
                            cameraIntent.putExtra("tripid", tripid);
                            cameraIntent.putExtra("mode", mode);
                            cameraIntent.putExtra("username", username);
                            cameraIntent.putExtra("mode", "new");
                            startActivity(cameraIntent);
                        }
                    }
                }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                });
        final AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private void showContinueTripAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        alertDialog.setMessage("What trip do you want to continue?")
                .setCancelable(false)
                .setView(inflater.inflate(R.layout.trip_dialog, null))
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Dialog f = (Dialog) dialogInterface;
                        EditText text = (EditText) f.findViewById(R.id.tripID);
                        tripid = Integer.parseInt(text.getText().toString());
                        imagesFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), username + "_Trip_" + tripid);
                        if (!imagesFolder.exists() && !imagesFolder.isDirectory()) {
                            showExistsContinueAlert();
                        }else {
                            if ("login".matches(mode)) {
                                Intent cameraIntent = new Intent(TripActivity.this, CameraActivity.class);
                                cameraIntent.putExtra("userid", userid);
                                cameraIntent.putExtra("tripid", tripid);
                                cameraIntent.putExtra("mode", "continue");
                                startActivity(cameraIntent);
                            } else {
                                Intent cameraIntent = new Intent(TripActivity.this, GuestActivity.class);
                                cameraIntent.putExtra("tripid", tripid);
                                cameraIntent.putExtra("mode", mode);
                                cameraIntent.putExtra("mode", "continue");
                                startActivity(cameraIntent);
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                });
        final AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private void showExistsNewAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("This trip already exists. Do you want to continue this trip, instead?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if ("login".matches(mode)){
                            Intent cameraIntent = new Intent(TripActivity.this, CameraActivity.class);
                            cameraIntent.putExtra("userid", userid);
                            cameraIntent.putExtra("tripid", tripid);
                            cameraIntent.putExtra("mode", "continue");
                            startActivity(cameraIntent);
                        }else{
                            Intent cameraIntent = new Intent(TripActivity.this, GuestActivity.class);
                            cameraIntent.putExtra("tripid", tripid);
                            cameraIntent.putExtra("mode", "continue");
                            startActivity(cameraIntent);
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        final AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private void showExistsContinueAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("This trip does not exist. Do you want to create it, instead?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if ("login".matches(mode)){
                            Intent cameraIntent = new Intent(TripActivity.this, CameraActivity.class);
                            cameraIntent.putExtra("userid", userid);
                            cameraIntent.putExtra("tripid", tripid);
                            cameraIntent.putExtra("mode", "new");
                            startActivity(cameraIntent);
                        }else{
                            Intent cameraIntent = new Intent(TripActivity.this, GuestActivity.class);
                            cameraIntent.putExtra("tripid", tripid);
                            cameraIntent.putExtra("mode", "new");
                            startActivity(cameraIntent);
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        final AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private void showConfirmDeleteAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Are you sure you want to delete this trip?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!imagesFolder.exists() && !imagesFolder.isDirectory()) {
                            showExistsAlert();
                        } else {
                            deleteRecursive(imagesFolder);
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        final AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private void showDeleteAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        alertDialog.setMessage("What trip number do you want to delete?")
                .setCancelable(false)
                .setView(inflater.inflate(R.layout.trip_dialog, null))
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Dialog f = (Dialog) dialogInterface;
                        EditText text = (EditText) f.findViewById(R.id.tripID);
                        tripid = Integer.parseInt(text.getText().toString());
                        imagesFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), username + "_Trip_" + tripid);
                        showConfirmDeleteAlert();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                });
        final AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private void deleteRecursive(File FileorDirectory){
        if (FileorDirectory.isDirectory()){
            for (File child : FileorDirectory.listFiles()){
                deleteRecursive(child);
            }
        }
        FileorDirectory.delete();
    }

    private void showExistsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("This trip does not exist!")
                .setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        final AlertDialog alert = alertDialog.create();
        alert.show();
    }
}

