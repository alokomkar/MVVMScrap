package com.alokomkar.scrapbook.data

import android.os.Parcel
import android.os.Parcelable

data class WordCount(
        var word: String = "",
        var count: Int = 0) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(word)
        writeInt(count)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<WordCount> = object : Parcelable.Creator<WordCount> {
            override fun createFromParcel(source: Parcel): WordCount = WordCount(source)
            override fun newArray(size: Int): Array<WordCount?> = arrayOfNulls(size)
        }
    }
}