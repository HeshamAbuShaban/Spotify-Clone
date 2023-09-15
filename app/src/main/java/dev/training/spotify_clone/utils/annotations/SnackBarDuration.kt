package dev.training.spotify_clone.utils.annotations

import androidx.annotation.IntDef
import com.google.android.material.snackbar.Snackbar

@Retention(AnnotationRetention.SOURCE)
@IntDef(
    flag = true,
    value = [Snackbar.LENGTH_SHORT, Snackbar.LENGTH_LONG, Snackbar.LENGTH_INDEFINITE]
)
annotation class SnackBarDuration
