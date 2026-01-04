# Add project specific ProGuard rules here.
-keep class com.iconpacksaver.oneui.** { *; }
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
