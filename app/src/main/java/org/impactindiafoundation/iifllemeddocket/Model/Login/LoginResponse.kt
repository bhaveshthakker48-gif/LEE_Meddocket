package org.impactindiafoundation.iifllemeddocket.Model.Login


data class LoginResponse(
    val ErrorCode: Int,
    val ErrorMessage: String,
    val LoginData: List<LoginData>
)