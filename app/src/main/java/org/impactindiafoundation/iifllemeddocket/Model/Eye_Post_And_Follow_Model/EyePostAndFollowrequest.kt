package org.impactindiafoundation.iifllemeddocket.Model.Eye_Post_And_Follow_Model

import com.google.gson.annotations.SerializedName

data class EyePostAndFollowrequest(

    @SerializedName("eye_Post_Ops")
    val eye_Post_Ops: List<EyePostOp>
)