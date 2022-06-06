package aj.dev.event.view

import android.content.Context
import android.content.DialogInterface
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogUtils {
    companion object {
        fun showDialog(
            context: Context,
            title: String,
            message: String,
            positiveButton: String,
            positiveButtonClick: DialogInterface.OnClickListener
        ) {
            MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButton, positiveButtonClick)
                .show()
        }
    }
}