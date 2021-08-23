package com.segment.analytics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.jetbrains.annotations.NotNull;

public interface RemoteLogger {
  void logE(@NonNull String message, @Nullable Exception e);

  void customKV(@NonNull String key, @NotNull String value);
}
