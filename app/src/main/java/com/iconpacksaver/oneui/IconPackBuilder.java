package com.iconpacksaver.oneui;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class IconPackBuilder {
    private static final String TAG = "IconPackBuilder";
    private Context context;

    public IconPackBuilder(Context context) {
        this.context = context;
    }

    public File buildIconPack(String packName, String packageName, Bitmap launcherIcon, List<AppIconInfo> apps) {
        try {
            // Use app-specific external storage instead of deprecated getExternalStoragePublicDirectory
            File outputDir;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                // For Android 10+, use app-specific external storage
                outputDir = new File(context.getExternalFilesDir(null), "IconPacks");
            } else {
                // For older versions, use Downloads directory
                outputDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "IconPacks");
            }
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            // Create temporary working directory
            File workDir = new File(context.getCacheDir(), "iconpack_" + System.currentTimeMillis());
            workDir.mkdirs();

            // Create directory structure
            File resDir = new File(workDir, "res");
            File drawableDir = new File(resDir, "drawable");
            File xmlDir = new File(resDir, "xml");
            File valuesDir = new File(resDir, "values");
            drawableDir.mkdirs();
            xmlDir.mkdirs();
            valuesDir.mkdirs();

            // Save launcher icon
            if (launcherIcon != null) {
                File iconFile = new File(drawableDir, "ic_launcher.png");
                FileOutputStream fos = new FileOutputStream(iconFile);
                launcherIcon.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            }

            // Save custom app icons
            for (int i = 0; i < apps.size(); i++) {
                AppIconInfo app = apps.get(i);
                if (app.getIconBitmap() != null) {
                    File iconFile = new File(drawableDir, "icon_" + i + ".png");
                    FileOutputStream fos = new FileOutputStream(iconFile);
                    app.getIconBitmap().compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                }
            }

            // Generate appfilter.xml
            generateAppFilter(xmlDir, apps);

            // Generate drawable.xml
            generateDrawableXml(xmlDir, apps);

            // Generate AndroidManifest.xml
            generateManifest(workDir, packName, packageName);

            // Generate strings.xml
            generateStrings(valuesDir, packName);

            // Create APK (actually a ZIP with resources)
            File apkFile = new File(outputDir, packName.replaceAll(" ", "_") + ".apk");
            zipDirectory(workDir, apkFile);

            // Clean up temp directory
            deleteDirectory(workDir);

            Log.d(TAG, "Icon pack built successfully: " + apkFile.getAbsolutePath());
            return apkFile;

        } catch (Exception e) {
            Log.e(TAG, "Error building icon pack", e);
            return null;
        }
    }

    private void generateAppFilter(File xmlDir, List<AppIconInfo> apps) throws IOException {
        File appFilterFile = new File(xmlDir, "appfilter.xml");
        FileWriter writer = new FileWriter(appFilterFile);
        
        writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        writer.write("<resources>\n");
        
        for (int i = 0; i < apps.size(); i++) {
            AppIconInfo app = apps.get(i);
            writer.write(String.format("    <item component=\"ComponentInfo{%s}\" drawable=\"icon_%d\" />\n", 
                    app.getComponentName(), i));
        }
        
        writer.write("</resources>\n");
        writer.close();
    }

    private void generateDrawableXml(File xmlDir, List<AppIconInfo> apps) throws IOException {
        File drawableFile = new File(xmlDir, "drawable.xml");
        FileWriter writer = new FileWriter(drawableFile);
        
        writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        writer.write("<resources>\n");
        writer.write("    <version>1</version>\n");
        
        for (int i = 0; i < apps.size(); i++) {
            AppIconInfo app = apps.get(i);
            writer.write(String.format("    <item drawable=\"icon_%d\" />\n", i));
        }
        
        writer.write("</resources>\n");
        writer.close();
    }

    private void generateManifest(File workDir, String packName, String packageName) throws IOException {
        File manifestFile = new File(workDir, "AndroidManifest.xml");
        FileWriter writer = new FileWriter(manifestFile);
        
        writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        writer.write("<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\"\n");
        writer.write("    package=\"" + packageName + "\">\n\n");
        writer.write("    <application\n");
        writer.write("        android:icon=\"@drawable/ic_launcher\"\n");
        writer.write("        android:label=\"@string/app_name\">\n");
        writer.write("    </application>\n");
        writer.write("</manifest>\n");
        writer.close();
    }

    private void generateStrings(File valuesDir, String packName) throws IOException {
        File stringsFile = new File(valuesDir, "strings.xml");
        FileWriter writer = new FileWriter(stringsFile);
        
        writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        writer.write("<resources>\n");
        writer.write("    <string name=\"app_name\">" + packName + "</string>\n");
        writer.write("</resources>\n");
        writer.close();
    }

    private void zipDirectory(File sourceDir, File zipFile) throws IOException {
        FileOutputStream fos = new FileOutputStream(zipFile);
        ZipOutputStream zos = new ZipOutputStream(fos);

        zipDirectoryRecursive(sourceDir, sourceDir, zos);

        zos.close();
        fos.close();
    }

    private void zipDirectoryRecursive(File rootDir, File currentDir, ZipOutputStream zos) throws IOException {
        File[] files = currentDir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                zipDirectoryRecursive(rootDir, file, zos);
            } else {
                String relativePath = rootDir.toURI().relativize(file.toURI()).getPath();
                ZipEntry zipEntry = new ZipEntry(relativePath);
                zos.putNextEntry(zipEntry);

                java.io.FileInputStream fis = new java.io.FileInputStream(file);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                fis.close();
                zos.closeEntry();
            }
        }
    }

    private void deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        dir.delete();
    }
}
