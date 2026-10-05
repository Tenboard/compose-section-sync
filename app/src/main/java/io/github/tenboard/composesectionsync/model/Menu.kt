package io.github.tenboard.composesectionsync.model

data class Menu(
    val id: String,
    val name: String,
    /** Price in USD cents: 890 represents $8.90. */
    val priceCents: Int,
    val imageUrl: String,
)
