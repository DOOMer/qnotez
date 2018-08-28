package org.doomer.qnotez.utils

import android.content.ContentResolver

import android.net.Uri
import android.os.ParcelFileDescriptor


import java.io.FileReader
import java.io.FileWriter

class IOUtils {
    companion object {
        const val MODE_READ = "r"
        const val MODE_WRITE = "w"

        fun writeText(contentResolver : ContentResolver , url : Uri, text : String) {
            val pfd : ParcelFileDescriptor = contentResolver.openFileDescriptor(url, MODE_WRITE)
            val fw : FileWriter = FileWriter(pfd.fileDescriptor)

            fw.write(text)

            fw.close()
            pfd.close()
        }

        fun readText(contentResolver : ContentResolver , url : Uri) : String {
            val pfd : ParcelFileDescriptor = contentResolver.openFileDescriptor(url, MODE_READ)
            val fr : FileReader = FileReader(pfd.fileDescriptor)

            val data = fr.readText()

            fr.close()
            pfd.close()

            return data
        }
    }
}