package ru.company.gists.data.network.entity

import com.google.gson.annotations.SerializedName

data class Commit(@SerializedName("version") val version: String,
                  @SerializedName("committed_at") val committedAt: String,
                  @SerializedName("change_status") val changeStatus: ChangeStatus)

