package dev.training.spotify_clone.utils.annotations

import android.widget.Toast
import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(
    flag = true,
    value = [Toast.LENGTH_SHORT, Toast.LENGTH_LONG]
)
annotation class ToastDuration
