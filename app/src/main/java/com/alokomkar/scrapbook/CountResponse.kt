package com.alokomkar.scrapbook

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.NonNull

@Entity(tableName = "CountResponse")
data class CountResponse(
        @PrimaryKey
        @NonNull
        var url: String = "",
        var countJson: String = "") : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CountResponse

        if (url != other.url) return false
        if (countJson != other.countJson) return false

        return true
    }

    override fun hashCode(): Int {
        var result = url.hashCode()
        result = 31 * result + countJson.hashCode()
        return result
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(url)
        writeString(countJson)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CountResponse> = object : Parcelable.Creator<CountResponse> {
            override fun createFromParcel(source: Parcel): CountResponse = CountResponse(source)
            override fun newArray(size: Int): Array<CountResponse?> = arrayOfNulls(size)
        }
    }
}
