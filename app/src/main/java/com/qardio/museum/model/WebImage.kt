package com.qardio.museum.model

import android.os.Parcelable
import androidx.room.Entity
import com.squareup.moshi.Json

/**
 * Immutable model class holds all the information about a art image.
 * Objects of this type are received from the API, so all the fields are annotated
 * with the serialized name.
 * Its a parcelable class which can be shared between UI fragments or activities
 */

@kotlinx.parcelize.Parcelize
@Entity
data class WebImage (

	@Json(name = "guid")
	val guid : String,

	@Json(name = "offsetPercentageX")
	val offsetPercentageX : Int,

	@Json(name = "offsetPercentageY")
	val offsetPercentageY : Int,

	@Json(name = "width")
	val width : Int,

	@Json(name = "height")
	val height : Int,

	@Json(name = "url")
	val url : String

) : Parcelable