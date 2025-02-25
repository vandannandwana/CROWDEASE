package com.minor.crowdease.utlis

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.minor.crowdease.R

class Constants {
    companion object{
        val POOPINS_FONT_REGULAR = FontFamily(Font(R.font.poppins_regular))
        val POOPINS_FONT_SEMI_BOLD = FontFamily(Font(R.font.poppins_semi_bold))
        val POOPINS_FONT_BOLD = FontFamily(Font(R.font.poppins_bold))
        val PROTEST_FONT = FontFamily(Font(R.font.protest_revolution_regular))
        val BLUE_COLOR = Color(0xFF2563E9)
        val GREEN_COLOR = Color(0xFF22C45E)
        val PAYMENT_GREEN_COLOR = Color(0xFF7AC142)

        val BASE_URL = "http://15.206.128.6:3000"
    }

}
