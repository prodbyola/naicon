package org.dududaa.naicon.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.AwtWindow
import java.awt.FileDialog
import java.awt.Frame
import java.io.FilenameFilter

@Composable
fun FilePicker(
    parent: Frame? = null,
    onCloseRequest: (result: String?) -> Unit,
    allowedExtensions: List<String>,
    allowMultiple: Boolean = true,
) = AwtWindow(
    create = {
        object : FileDialog(parent, "Choose a file", LOAD) {
            init {
                // Set FilenameFilter to filter files based on allowed extensions
                filenameFilter = FilenameFilter { _, name ->
                    allowedExtensions.any { name.endsWith(it, ignoreCase = true) }
                }

                // Set multiple file selection mode
                isMultipleMode = allowMultiple
            }

            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    onCloseRequest(file)
                }
            }
        }
    },
    dispose = FileDialog::dispose
)