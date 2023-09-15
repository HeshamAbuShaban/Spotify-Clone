package dev.training.spotify_clone.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import dev.training.spotify_clone.utils.annotations.SnackBarDuration
import dev.training.spotify_clone.utils.annotations.ToastDuration

object GeneralUtils {
    fun showSnackBar(
        view: View,
        text: String,
        @SnackBarDuration duration: Int,
    ) {
        Snackbar.make(view, text, duration).show()
    }

    fun showToast(
        context: Context,
        text: String,
        @ToastDuration duration: Int,
    ) {
        Toast.makeText(context, text, duration).show()
    }


}