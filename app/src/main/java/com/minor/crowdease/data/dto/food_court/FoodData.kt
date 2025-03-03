package com.minor.crowdease.data.dto.food_court

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FoodData(
    val categoryId: String,
    val description: String,
    val id: String,
    val image: String,
    val isAvailable: Boolean,
    val name: String,
    val preparationTime: Int,
    val price: String,
    val shopId: String
): Parcelable {

    constructor():this("","","","",true,"",10,"","",)

}