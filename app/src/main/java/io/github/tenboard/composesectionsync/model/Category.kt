package io.github.tenboard.composesectionsync.model

data class Category(
    val id: String,
    val name: String,
    val menuList: List<Menu>,
)