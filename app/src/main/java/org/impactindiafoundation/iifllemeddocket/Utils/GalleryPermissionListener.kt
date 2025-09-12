package org.impactindiafoundation.iifllemeddocket.Utils

import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


class GalleryPermissionListener(private val onPermissionGrantedAction: () -> Unit) : PermissionListener {
    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        // Call the provided action when permission is granted
        onPermissionGrantedAction.invoke()
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        // Empty implementation, as this is an "empty" listener
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: PermissionRequest?,
        token: PermissionToken?
    ) {
        // Empty implementation, as this is an "empty" listener
    }
}

