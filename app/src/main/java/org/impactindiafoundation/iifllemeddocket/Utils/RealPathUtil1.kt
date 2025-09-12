package org.impactindiafoundation.iifllemeddocket.Utils

import android.content.Context
import android.database.Cursor
import android.net.Uri

object RealPathUtil1 {

    fun getRealPath(context: Context, uri: Uri): String? {
        return when {
            isContentUri(uri) -> {
                getDataColumn(context, uri, null, null)
            }
            isFileUri(uri) -> {
                uri.path
            }
            else -> {
                // Unsupported URI scheme
                null
            }
        }
    }

    private fun isContentUri(uri: Uri): Boolean {
        return "content".equals(uri.scheme, ignoreCase = true)
    }

    private fun isFileUri(uri: Uri): Boolean {
        return "file".equals(uri.scheme, ignoreCase = true)
    }

    private fun getDataColumn(
        context: Context,
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex: Int = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(columnIndex)
            }
        } finally {
            cursor?.close()
        }
        return null
    }
}
