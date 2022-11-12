package com.jorgetargz.recycler.ui.main

data class MainState(
    val error: String? = null,
    val onAdd: Boolean = false,
    val cleanFields: Boolean = false,
)