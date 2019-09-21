package ru.company.gists.data.network.entity

import com.google.gson.annotations.SerializedName

data class Gist(@SerializedName("id") val id: String,
                @SerializedName("files") val files: Map<String, File>,
                @SerializedName("history") val history: List<Commit>,
                @SerializedName("created_at") val createdAt: String,
                @SerializedName("updated_at") val updatedAt: String,
                @SerializedName("description") val description: String,
                @SerializedName("owner") val owner: Owner)
