# koin
-keep  public  class org.koin** { *; }
-keepclassmembers public class * extends androidx.lifecycle.ViewModel { public <init>(...); }
-keepattributes *Annotation*

# google
-keep public class com.google.android.gms** { *; }
-keep  public  class com.google.firebase** { *; }

# ninenox localization
-keep public class com.ninenox** { *;}

# manifest rules
-adaptresourcefilenames    **.properties,**.gif,**.jpg
-adaptresourcefilecontents **.properties,META-INF/MANIFEST.MF


# gson
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

