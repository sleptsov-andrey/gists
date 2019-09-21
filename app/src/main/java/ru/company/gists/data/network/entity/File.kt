package ru.company.gists.data.network.entity

import com.google.gson.annotations.SerializedName

data class File(@SerializedName("filename") val filename:String,
                @SerializedName("content") val content: String)
