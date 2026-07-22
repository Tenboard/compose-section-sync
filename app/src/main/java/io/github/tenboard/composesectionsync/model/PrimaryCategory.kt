package io.github.tenboard.composesectionsync.model

data class PrimaryCategory(
    val id: String,
    val name: String,
    val subCategories: List<Category>,
)
