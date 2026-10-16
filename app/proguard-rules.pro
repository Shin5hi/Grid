# Reglas ProGuard / R8 para Discord Bot Maker
# Kotlin / Compose

# Mantener anotaciones de serialización de kotlinx.serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**

# kotlinx.serialization: mantener serializadores generados
-keepclassmembers class com.discordbotmaker.android.** {
    *** Companion;
}
-keepclasseswithmembers class com.discordbotmaker.android.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Compose no requiere reglas específicas (viene con consumer-rules)

# OkHttp / Okio
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn org.conscrypt.**
