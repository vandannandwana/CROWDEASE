package com.minor.crowdease.utlis

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.minor.crowdease.R
import java.text.NumberFormat

class Constants {
    companion object{
        val POOPINS_FONT_REGULAR = FontFamily(Font(R.font.poppins_regular))
        val POOPINS_FONT_SEMI_BOLD = FontFamily(Font(R.font.poppins_semi_bold))
        val POOPINS_FONT_BOLD = FontFamily(Font(R.font.poppins_bold))
        val PROTEST_FONT = FontFamily(Font(R.font.protest_revolution_regular))
        val ORANGE_COLOR = R.color.orange
        val LIGHT_ORANGE_COLOR = R.color.light_orange
        val BUTTON_TEXT_COLOR = R.color.button_text_color
        val GREEN_COLOR = R.color.green
        val PAYMENT_GREEN_COLOR = R.color.green
        val GREY = R.color.grey
        val DARK_GREY = R.color.dark_grey
        val TEXT_COLOR = R.color.text
        val BACKGROUND_COLOR = R.color.background
        val TOKENPREF = "TOKENPREF"
        val STUDENT_NAME = "STUDENT_NAME"
        val STUDENT_EMAIL = "STUDENT_EMAIL"
        val STUDENT_PHONE_NUMBER = "STUDENT_PHONE_NUMBER"
        val BASE_URL = "http://192.168.29.233:3000"

        @Composable
        fun getCurrency(amount: Any): String {
            val formater = NumberFormat.getInstance()
            formater.maximumFractionDigits = 0
            formater.currency = java.util.Currency.getInstance("INR")

            val currency = formater.format(amount)
            val c = formater.currency?.symbol
            return c+currency

        }

    }

}
