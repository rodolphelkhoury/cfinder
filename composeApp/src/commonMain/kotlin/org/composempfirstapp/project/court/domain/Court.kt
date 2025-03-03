package org.composempfirstapp.project.court.domain

data class Court(
    val id: Long,
    val complexId: Long,
    val courtTypeId: Long,
    val surfaceTypeId: Long,
    val name: String,
    val description: String?,
    val hourlyRate: Double,
    val divisible: Boolean,
    val maxDivisions: Int?,
    val openingTime: String,
    val closingTime: String,
    val imageUrl: String?,
//    val sections: List<Section> = emptyList(),
//    val complex: Complex,
//    val courtType: CourtType,
//    val surfaceType: SurfaceType,
//    val reservations: List<Reservation> = emptyList()
)