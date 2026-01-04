package com.iconpacksaver.oneui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;
    
    private TextInputEditText packNameInput;
    private TextInputEditText packageNameInput;
    private ImageView launcherIconPreview;
    private RecyclerView appsRecyclerView;
    private TextView emptyView;
    private Button addAppBtn;
    private Button buildPackBtn;
    private Button selectLauncherIconBtn;
    private ProgressBar progressBar;

    private AppIconAdapter adapter;
    private List<AppIconInfo> appList;
    private Bitmap launcherIconBitmap;
    
    private ActivityResultLauncher<Intent> launcherIconPickerLauncher;
    private ActivityResultLauncher<Intent> appIconPickerLauncher;
    private AlertDialog addAppDialog;
    private ImageView dialogIconPreview;
    private Bitmap tempIconBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupRecyclerView();
        setupClickListeners();
        setupImagePickers();
        checkPermissions();
    }

    private void initializeViews() {
        packNameInput = findViewById(R.id.packNameInput);
        packageNameInput = findViewById(R.id.packageNameInput);
        launcherIconPreview = findViewById(R.id.launcherIconPreview);
        appsRecyclerView = findViewById(R.id.appsRecyclerView);
        emptyView = findViewById(R.id.emptyView);
        addAppBtn = findViewById(R.id.addAppBtn);
        buildPackBtn = findViewById(R.id.buildPackBtn);
        selectLauncherIconBtn = findViewById(R.id.selectLauncherIconBtn);
        progressBar = findViewById(R.id.progressBar);

        appList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        adapter = new AppIconAdapter();
        adapter.setAppList(appList);
        adapter.setDeleteListener(position -> {
            appList.remove(position);
            adapter.removeApp(position);
            updateEmptyView();
        });

        appsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        appsRecyclerView.setAdapter(adapter);
        updateEmptyView();
    }

    private void setupClickListeners() {
        selectLauncherIconBtn.setOnClickListener(v -> selectLauncherIcon());
        addAppBtn.setOnClickListener(v -> showAddAppDialog());
        buildPackBtn.setOnClickListener(v -> buildIconPack());
    }

    private void setupImagePickers() {
        launcherIconPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        launcherIconBitmap = loadBitmapFromUri(imageUri);
                        if (launcherIconBitmap != null) {
                            launcherIconPreview.setImageBitmap(launcherIconBitmap);
                        }
                    }
                });

        appIconPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        tempIconBitmap = loadBitmapFromUri(imageUri);
                        if (tempIconBitmap != null && dialogIconPreview != null) {
                            dialogIconPreview.setImageBitmap(tempIconBitmap);
                        }
                    }
                });
    }

    private void selectLauncherIcon() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        launcherIconPickerLauncher.launch(intent);
    }

    private void showAddAppDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_app, null);
        
        TextInputEditText appNameInput = dialogView.findViewById(R.id.appNameInput);
        TextInputEditText componentNameInput = dialogView.findViewById(R.id.componentNameInput);
        dialogIconPreview = dialogView.findViewById(R.id.iconPreview);
        Button selectIconBtn = dialogView.findViewById(R.id.selectIconBtn);
        Button cancelBtn = dialogView.findViewById(R.id.cancelBtn);
        Button addBtn = dialogView.findViewById(R.id.addBtn);

        tempIconBitmap = null;

        selectIconBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            appIconPickerLauncher.launch(intent);
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        addAppDialog = builder.create();

        cancelBtn.setOnClickListener(v -> addAppDialog.dismiss());

        addBtn.setOnClickListener(v -> {
            String appName = appNameInput.getText() != null ? appNameInput.getText().toString().trim() : "";
            String componentName = componentNameInput.getText() != null ? componentNameInput.getText().toString().trim() : "";

            if (appName.isEmpty()) {
                Toast.makeText(this, "Please enter app name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (componentName.isEmpty()) {
                Toast.makeText(this, "Please enter component name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (tempIconBitmap == null) {
                Toast.makeText(this, "Please select an icon", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save icon to cache
            String iconPath = saveIconToCache(tempIconBitmap, appName);
            
            AppIconInfo appInfo = new AppIconInfo(appName, componentName, iconPath, tempIconBitmap);
            appList.add(appInfo);
            adapter.addApp(appInfo);
            updateEmptyView();

            addAppDialog.dismiss();
        });

        addAppDialog.show();
    }

    private String saveIconToCache(Bitmap bitmap, String appName) {
        try {
            File cacheDir = new File(getCacheDir(), "icons");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            
            File iconFile = new File(cacheDir, appName.replaceAll(" ", "_") + "_" + System.currentTimeMillis() + ".png");
            FileOutputStream fos = new FileOutputStream(iconFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            
            return iconFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void buildIconPack() {
        String packName = packNameInput.getText() != null ? packNameInput.getText().toString().trim() : "";
        String packageName = packageNameInput.getText() != null ? packageNameInput.getText().toString().trim() : "";

        if (packName.isEmpty()) {
            Toast.makeText(this, "Please enter icon pack name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (packageName.isEmpty()) {
            Toast.makeText(this, "Please enter package name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (appList.isEmpty()) {
            Toast.makeText(this, "Please add at least one app", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        buildPackBtn.setEnabled(false);

        new Thread(() -> {
            IconPackBuilder builder = new IconPackBuilder(this);
            File apkFile = builder.buildIconPack(packName, packageName, launcherIconBitmap, appList);

            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);
                buildPackBtn.setEnabled(true);

                if (apkFile != null && apkFile.exists()) {
                    Toast.makeText(this, getString(R.string.pack_built_success) + "\n" + apkFile.getAbsolutePath(), 
                            Toast.LENGTH_LONG).show();
                    showInstallDialog(apkFile);
                } else {
                    Toast.makeText(this, R.string.pack_built_failed, Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void showInstallDialog(File apkFile) {
        new AlertDialog.Builder(this)
                .setTitle("Icon Pack Built")
                .setMessage("Icon pack has been saved to:\n" + apkFile.getAbsolutePath() + 
                        "\n\nNote: The generated file contains icon resources. To use it as an icon pack on One UI, " +
                        "you'll need to sign and properly package it as an APK using Android development tools.")
                .setPositiveButton("OK", null)
                .show();
    }

    private Bitmap loadBitmapFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (inputStream != null) {
                inputStream.close();
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateEmptyView() {
        if (appList.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            appsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            appsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
