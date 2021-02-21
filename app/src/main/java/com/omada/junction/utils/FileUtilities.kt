package com.omada.junction.utils;

import android.net.Uri
import android.util.Log
import androidx.annotation.NonNull
import com.omada.junction.application.JunctionApplication
import me.shouheng.utils.store.PathUtils
import java.io.File

open class FileUtilities {

    companion object {

        fun getTempFilesPath() = PathUtils.getInternalAppFilesPath().plus("/temp/")

        private fun deleteFile(@NonNull uri: Uri): Boolean {

            val fileToDelete = File(uri.path)
            if (fileToDelete.exists()) {
                return if (fileToDelete.delete()) {
                    if (fileToDelete.exists()) {
                        fileToDelete.canonicalFile.delete()
                        if (fileToDelete.exists()) {
                            JunctionApplication.getContext().deleteFile(fileToDelete.name)
                        }
                    }
                    Log.e("", "File Deleted " + uri.path)
                    true
                } else {
                    Log.e("", "File not Deleted " + uri.path)
                    false
                }
            }
            return false
        }

        fun clearTemporaryFiles() {

            Log.e("Files", "Starting delete of all temporary files")
            val tempDirPath = PathUtils.getInternalAppFilesPath().plus("/temp/")
            val tempDir = File(tempDirPath)
            if(!tempDir.exists()) {
                tempDir.mkdir()
            }
            val files = tempDir.listFiles()
            files?.filter { it -> it.isFile }?.forEach { it -> deleteFile(Uri.fromFile(it)) }
        }
    }

}
