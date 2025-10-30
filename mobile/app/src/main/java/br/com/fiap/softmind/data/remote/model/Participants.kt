package br.com.fiap.softmind.data.remote.model

import com.google.gson.annotations.SerializedName

data class Participants(
    @SerializedName("total")
    val total: Int? = null,

    @SerializedName("byDepartment")
    val byDepartment: Map<String, Int>? = null
)

