package org.impactindiafoundation.iifllemeddocket.Model.RefractiveModel

data class getRefractiveErrorResponse(
    val ErrorCode: Int,
    val ErrorMessage: String,
    val refractiveErrors: List<RefractiveError>,
    val success_count: Int
) {
    data class RefractiveError(
        val _id: String
    )
}