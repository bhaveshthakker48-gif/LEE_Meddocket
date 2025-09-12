package org.impactindiafoundation.iifllemeddocket.Utils

import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

open class EmptyPermissionListener : PermissionListener {
    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        // Empty implementation, as this is an "empty" listener
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

