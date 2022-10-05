package com.qardio.museum.model

import android.os.Parcelable
import com.squareup.moshi.Json

/**
 * Immutable model class for a Art repo that holds all the information about a art count facets.
 * Objects of this type are received from the API, so all the fields are annotated
 * with the serialized name.
 * Its a parcelable class which can be shared between UI fragments or activities
 */

@kotlinx.parcelize.Parcelize
data class CountFacets (

	@Json(name = "hasimage")
	val hasimage : Int,

	@Json(name = "ondisplay")
	val ondisplay : Int

): Parcelable