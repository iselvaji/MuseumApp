package com.qardio.museum.model

import android.os.Parcelable
import com.squareup.moshi.Json

/**
 *
 * Immutable model class for a Art repo that holds list of a art object and count.
 * Objects of this type are received from the API, so all the fields are annotated
 * with the serialized name.
 * Its a parcelable class which can be shared between UI fragments or activities
 */

@kotlinx.parcelize.Parcelize
data class ArtResponse (

	@Json(name = "elapsedMilliseconds")
	val elapsedMilliseconds : Int,

	@Json(name = "count")
	val count : Int,

	@Json(name = "countFacets")
	val countFacets : CountFacets,

	@Json(name = "artObjects")
	val artObjects : List<ArtObjects>

) : Parcelable