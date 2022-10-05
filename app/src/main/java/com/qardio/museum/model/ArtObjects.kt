package com.qardio.museum.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.qardio.museum.utils.Constants
import com.squareup.moshi.Json

/**
 * Immutable model class for a Art repo that holds all the information about a art object.
 * Objects of this type are received from the API, so all the fields are annotated
 * with the serialized name.
 * This class also defines the Room table, where the artId [id] is the primary key.
 * Its a parcelable class which can be shared between UI fragments or activities
 */

@kotlinx.parcelize.Parcelize
@Entity(tableName = Constants.DBConstants.TABLE_ART)
data class ArtObjects (

	@PrimaryKey
	@Json(name = "id")
	val artId : String = "",

	@Json(name = "title")
	val title : String = "",

	@Json(name = "hasImage")
	val hasImage : Boolean = false,

	@Json(name = "principalOrFirstMaker")
	val principalOrFirstMaker : String = "",

	@Json(name = "longTitle")
	val longTitle : String = "",

	@Json(name = "showImage")
	val showImage : Boolean = false,

	@Json(name = "permitDownload")
	val permitDownload : Boolean = false,

	@Json(name = "webImage")
	@Embedded val webImage : WebImage?

) : Parcelable