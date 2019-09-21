package ru.company.gists.data.network.entity

import com.google.gson.annotations.SerializedName

data class ChangeStatus(@SerializedName("total") val total: String,
                        @SerializedName("additions") val additions: String,
                        @SerializedName("deletions") val deletions: String)

