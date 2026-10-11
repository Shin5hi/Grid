# Project-specific ProGuard/R8 rules.
# OkHttp and kotlinx ship their own consumer rules; keep this file for app-only rules.

# Keep data classes used for reflection-free JSON parsing debuggability in stack traces.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
