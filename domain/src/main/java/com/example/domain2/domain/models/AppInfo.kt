package com.example.weba.domain.models

data class AppInfo(
    val name: String,
    val version: String,
    val packageName: String,
    val sha256: String
)