package org.composempfirstapp.project.core

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import cfinder.composeapp.generated.resources.Res
import cfinder.composeapp.generated.resources.booking
import cfinder.composeapp.generated.resources.reservation
import cfinder.composeapp.generated.resources.dark_mode
import cfinder.composeapp.generated.resources.home
import cfinder.composeapp.generated.resources.light_mode
import cfinder.composeapp.generated.resources.person
import cfinder.composeapp.generated.resources.profile
import cfinder.composeapp.generated.resources.system_default
import org.composempfirstapp.project.court.data.CourtsResponse
import org.composempfirstapp.project.court.domain.Court
import org.composempfirstapp.project.core.navigation.BottomNavigationItem
import org.composempfirstapp.project.core.navigation.MainRouteScreen
import org.jetbrains.compose.resources.StringResource

const val datastoreFileName = "setting.preferences_pb"

const val BASE_URL = "https://g6oqfim2ie.sharedwithexpose.com/api/"



enum class Theme(val title: StringResource) {
    SYSTEM_DEFAULT(Res.string.system_default),
    LIGHT_MODE(Res.string.light_mode),
    DARK_MODE(Res.string.dark_mode)
}

val bottomNavigationList = listOf(
    BottomNavigationItem(
        icon = Res.drawable.home,
        title = Res.string.home,
        route = MainRouteScreen.Home.route
    ),
    BottomNavigationItem(
        icon = Res.drawable.booking,
        title = Res.string.reservation,
        route = MainRouteScreen.Reservation.route
    ),
    BottomNavigationItem(
        icon = Res.drawable.person,
        title = Res.string.profile,
        route = MainRouteScreen.Profile.route
    ),
)

//val courts: List<Court> = listOf(
//    Court(1, 1, 1, 1, "Court A", "Indoor basketball court", 50.0, true, 2, "08:00", "22:00","https://www.google.com/imgres?q=sports%20courts%20images&imgurl=https%3A%2F%2Fwww.snapsports.com%2Fwp-content%2Fuploads%2F2021%2F04%2Fres-sport-multi-court-1.jpg&imgrefurl=https%3A%2F%2Fwww.snapsports.com%2Fyour-sport-residential%2F&docid=KRNhS5e2BADa7M&tbnid=F8f5lIhJbjMJKM&vet=12ahUKEwiH3NuK7e6LAxW8_AIHHfsJLEMQM3oECFEQAA..i&w=769&h=400&hcb=2&ved=2ahUKEwiH3NuK7e6LAxW8_AIHHfsJLEMQM3oECFEQAA"),
//    Court(2, 1, 2, 2, "Court B", "Outdoor tennis court", 30.0, false, null, "07:00", "21:00","https://www.google.com/imgres?q=sports%20courts%20images&imgurl=https%3A%2F%2Fwww.mistershademe.com%2Fwp-content%2Fuploads%2F2020%2F03%2FMultipurpose-Sports-Courts.jpg&imgrefurl=https%3A%2F%2Fwww.mistershademe.com%2Fmultipurpose-sports-courts-uae.html&docid=TOaS6CBlvfdCnM&tbnid=zw0ANCbZPoTWZM&vet=12ahUKEwiH3NuK7e6LAxW8_AIHHfsJLEMQM3oECGkQAA..i&w=950&h=400&hcb=2&ved=2ahUKEwiH3NuK7e6LAxW8_AIHHfsJLEMQM3oECGkQAA"),
//    Court(3, 2, 3, 3, "Court C", "Football field", 80.0, true, 4, "06:00", "23:00","https://www.google.com/imgres?q=sports%20courts%20images&imgurl=https%3A%2F%2Fcdn-ilaiknj.nitrocdn.com%2FIMWatEEUAgOsxLugkbioEpXNZhquvwTL%2Fassets%2Fimages%2Foptimized%2Frev-a81e0e7%2Flonestaraz.com%2Fwp-content%2Fuploads%2F2021%2F01%2Fsport-court-tennis-court-phoenix-az-scaled.jpg&imgrefurl=https%3A%2F%2Flonestaraz.com%2Foutdoor-sport-courts-in-phoenix-az%2F&docid=mO9cQL5mFBlv0M&tbnid=lv3HvNTl9IFwJM&vet=12ahUKEwiH3NuK7e6LAxW8_AIHHfsJLEMQM3oECDAQAA..i&w=2560&h=1886&hcb=2&ved=2ahUKEwiH3NuK7e6LAxW8_AIHHfsJLEMQM3oECDAQAA"),
//    Court(4, 2, 4, 1, "Court D", "Multipurpose court", 40.0, true, 2, "09:00", "20:00","https://www.google.com/imgres?q=sports%20courts%20images&imgurl=https%3A%2F%2Fwww.gordon.us.com%2Fwp-content%2Fuploads%2F2020%2F10%2FIMG_8356-scaled.jpg&imgrefurl=https%3A%2F%2Fwww.gordon.us.com%2Fsports-recreation-services%2Fsport-courts%2F&docid=hj_S3MwKJAq94M&tbnid=frS_tRFPg5hS1M&vet=12ahUKEwiH3NuK7e6LAxW8_AIHHfsJLEMQM3oECFsQAA..i&w=2560&h=1920&hcb=2&ved=2ahUKEwiH3NuK7e6LAxW8_AIHHfsJLEMQM3oECFsQAA"),
//    Court(5, 3, 1, 4, "Court E", "Indoor volleyball court", 45.0, false, null, "10:00", "19:00","https://www.google.com/imgres?q=sports%20courts%20images%20squash&imgurl=https%3A%2F%2Fintegralspor.com%2Fuploads%2Ffacility%2Fdetail%2Fsquash-salonlari-img-1.jpg&imgrefurl=https%3A%2F%2Fintegralspor.com%2Fsports-facilities%2Fsquash-courts&docid=1GR_vtX5Xk0hbM&tbnid=MocJMeKfc1yrdM&vet=12ahUKEwjlgvGy7e6LAxWM9QIHHe83NjcQM3oECGwQAA..i&w=827&h=417&hcb=2&ved=2ahUKEwjlgvGy7e6LAxWM9QIHHe83NjcQM3oECGwQAA"),
//    Court(6, 3, 2, 2, "Court F", "Clay tennis court", 35.0, false, null, "07:00", "22:00", "https://www.google.com/imgres?q=sports%20courts%20images%20basketball&imgurl=https%3A%2F%2Fcdn.versacourt.com%2Fcmss_files%2Fphotogallery%2Fstructure%2FResidential_Basketball_Courts%2Fimage57751.jpg&imgrefurl=https%3A%2F%2Fwww.versacourt.com%2Fbackyard-basketball-courts.html&docid=z6nwuBsx9e7QzM&tbnid=CqajpoOhyxfgtM&vet=12ahUKEwie6eS57e6LAxW79gIHHcaGGikQM3oECH0QAA..i&w=800&h=532&hcb=2&ved=2ahUKEwie6eS57e6LAxW79gIHHcaGGikQM3oECH0QAA"),
//    Court(7, 4, 3, 3, "Court G", "Mini football pitch", 60.0, true, 3, "08:00", "22:30", "https://www.google.com/imgres?q=sports%20courts%20images%20soccer&imgurl=https%3A%2F%2Fscissortailpark.org%2Fwp-content%2Fuploads%2F2022%2F09%2FSoccer-Field-cover.png&imgrefurl=https%3A%2F%2Fscissortailpark.org%2Frentasportscourt%2F&docid=8nQdbd2jZwdUvM&tbnid=lC4MclNcDizELM&vet=12ahUKEwjim5HE7e6LAxWz3wIHHWNAAZIQM3oECGYQAA..i&w=1920&h=792&hcb=2&ved=2ahUKEwjim5HE7e6LAxWz3wIHHWNAAZIQM3oECGYQAA"),
//    Court(8, 4, 4, 1, "Court H", "Badminton hall", 25.0, false, null, "09:00", "21:00","https://www.google.com/imgres?q=sports%20courts%20images%20soccer&imgurl=https%3A%2F%2Fwww.espressoenglish.net%2Fwp-content%2Fuploads%2F2014%2F01%2Fsoccer-field.jpg&imgrefurl=https%3A%2F%2Fwww.espressoenglish.net%2Fenglish-vocabulary-words-sports-locations%2F&docid=m0WQs3I6DRN13M&tbnid=lmoIucOIp5n7wM&vet=12ahUKEwjim5HE7e6LAxWz3wIHHWNAAZIQM3oECEEQAA..i&w=400&h=252&hcb=2&ved=2ahUKEwjim5HE7e6LAxWz3wIHHWNAAZIQM3oECEEQAA"),
//    Court(9, 5, 1, 4, "Court I", "Futsal court", 55.0, true, 2, "06:30", "22:00", "https://fm-hero-images-test.s3.amazonaws.com/images/61qckhBYmwsqsZ4RUCN9bWe50r2pzB2zk667LUiZ.png?X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIARWBKIG6ICJPMTVXB%2F20250303%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20250303T213130Z&X-Amz-SignedHeaders=host&X-Amz-Expires=64800&X-Amz-Signature=6ee174c4511e51ac4412929caa81cc5daeda4b5134b1ec451da10274012577c3"),
//    Court(10, 5, 2, 3, "Court J", "Grass tennis court", 40.0, false, null, "08:00", "20:30","https://www.marketscreener.com/images/reuters/2024-03-05T144855Z_1_LYNXNPEK240IP_RTROPTP_3_GERMANY-TESLA-FIRE.JPG")
//)

//val courtsResponse = CourtsResponse(
//    courts,
//    "dwe",
//)

val FadeIn = fadeIn(animationSpec = tween(220, delayMillis = 90)) +
        scaleIn(
            initialScale = 0.92f,
            animationSpec = tween(220, delayMillis = 90)
        )

val FadeOut = fadeOut(animationSpec = tween(90))

//google maps api key AIzaSyCa73avNv6ek-teXlZJjGYas0ekJkfmhuI