package ru.kondrashen.testanimeapp.repository.data_class.responses

data class MainResponse(
    override val status: String,
    override val code: Int,
    val maxPage: Int?,
    val curPage: Int?,
): ServerResponse
