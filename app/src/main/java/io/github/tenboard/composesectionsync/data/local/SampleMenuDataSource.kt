package io.github.tenboard.composesectionsync.data.local

import io.github.tenboard.composesectionsync.model.Category
import io.github.tenboard.composesectionsync.model.Menu
import io.github.tenboard.composesectionsync.model.PrimaryCategory

internal object SampleMenuDataSource {
    private const val BURGER_IMAGE_URL =
        "https://images.unsplash.com/photo-1568901346375-23c9450c58cd"
    private const val PIZZA_IMAGE_URL =
        "https://images.unsplash.com/photo-1574071318508-1cdbab80d002"
    private const val PASTA_IMAGE_URL =
        "https://images.unsplash.com/photo-1551892374-ecf8754cf8b0"
    private const val SALAD_IMAGE_URL =
        "https://images.unsplash.com/photo-1540420773420-3366772f4999"
    private const val SIDE_IMAGE_URL =
        "https://images.unsplash.com/photo-1573080496219-bb080dd4f877"
    private const val DESSERT_IMAGE_URL =
        "https://images.unsplash.com/photo-1551024506-0bccd828d307"
    private const val BEVERAGE_IMAGE_URL =
        "https://images.unsplash.com/photo-1544145945-f90425340c7e"

    private val secondaryCategories: List<Category> = listOf(
        Category(
            id = "burger",
            name = "버거",
            menuList = listOf(
                menu("burger-classic", "클래식 치즈버거", 8_900, BURGER_IMAGE_URL),
                menu("burger-double", "더블 비프 버거", 11_900, BURGER_IMAGE_URL),
                menu("burger-bacon", "베이컨 에그 버거", 10_900, BURGER_IMAGE_URL),
                menu("burger-chicken", "크리스피 치킨버거", 9_500, BURGER_IMAGE_URL),
                menu("burger-shrimp", "통새우 버거", 10_500, BURGER_IMAGE_URL),
                menu("burger-mushroom", "머쉬룸 치즈버거", 10_900, BURGER_IMAGE_URL),
            ),
        ),
        Category(
            id = "pizza",
            name = "피자",
            menuList = listOf(
                menu("pizza-margherita", "마르게리타 피자", 15_900, PIZZA_IMAGE_URL),
                menu("pizza-pepperoni", "페퍼로니 피자", 17_900, PIZZA_IMAGE_URL),
                menu("pizza-cheese", "콰트로 치즈 피자", 18_900, PIZZA_IMAGE_URL),
                menu("pizza-hawaiian", "하와이안 피자", 17_500, PIZZA_IMAGE_URL),
                menu("pizza-bulgogi", "불고기 피자", 19_900, PIZZA_IMAGE_URL),
                menu("pizza-potato", "베이컨 포테이토 피자", 19_500, PIZZA_IMAGE_URL),
            ),
        ),
        Category(
            id = "pasta",
            name = "파스타",
            menuList = listOf(
                menu("pasta-tomato", "토마토 해산물 파스타", 14_900, PASTA_IMAGE_URL),
                menu("pasta-carbonara", "베이컨 까르보나라", 15_900, PASTA_IMAGE_URL),
                menu("pasta-rose", "쉬림프 로제 파스타", 16_900, PASTA_IMAGE_URL),
                menu("pasta-aglio", "알리오 올리오", 13_900, PASTA_IMAGE_URL),
                menu("pasta-ragu", "볼로네제 라구 파스타", 16_500, PASTA_IMAGE_URL),
                menu("pasta-vongole", "봉골레 파스타", 15_900, PASTA_IMAGE_URL),
            ),
        ),
        Category(
            id = "salad",
            name = "샐러드",
            menuList = listOf(
                menu("salad-caesar", "그릴드 치킨 시저 샐러드", 10_900, SALAD_IMAGE_URL),
                menu("salad-ricotta", "리코타 치즈 샐러드", 11_900, SALAD_IMAGE_URL),
                menu("salad-avocado", "아보카도 콥 샐러드", 12_900, SALAD_IMAGE_URL),
                menu("salad-shrimp", "갈릭 쉬림프 샐러드", 13_500, SALAD_IMAGE_URL),
                menu("salad-salmon", "훈제 연어 샐러드", 14_900, SALAD_IMAGE_URL),
                menu("salad-tofu", "구운 두부 샐러드", 9_900, SALAD_IMAGE_URL),
            ),
        ),
        Category(
            id = "side",
            name = "사이드",
            menuList = listOf(
                menu("side-fries", "프렌치프라이", 4_500, SIDE_IMAGE_URL),
                menu("side-cheese-fries", "치즈 프라이", 5_900, SIDE_IMAGE_URL),
                menu("side-onion-ring", "어니언링", 5_500, SIDE_IMAGE_URL),
                menu("side-chicken", "크리스피 치킨", 7_900, SIDE_IMAGE_URL),
                menu("side-wings", "버팔로 윙", 8_900, SIDE_IMAGE_URL),
                menu("side-garlic-bread", "갈릭 브레드", 4_900, SIDE_IMAGE_URL),
            ),
        ),
        Category(
            id = "dessert",
            name = "디저트",
            menuList = listOf(
                menu("dessert-cheesecake", "뉴욕 치즈케이크", 6_900, DESSERT_IMAGE_URL),
                menu("dessert-chocolate", "초콜릿 브라우니", 6_500, DESSERT_IMAGE_URL),
                menu("dessert-tiramisu", "클래식 티라미수", 7_500, DESSERT_IMAGE_URL),
                menu("dessert-ice-cream", "바닐라 아이스크림", 4_900, DESSERT_IMAGE_URL),
                menu("dessert-croffle", "메이플 크로플", 7_900, DESSERT_IMAGE_URL),
                menu("dessert-pancake", "베리 팬케이크", 8_500, DESSERT_IMAGE_URL),
            ),
        ),
        Category(
            id = "beverage",
            name = "음료",
            menuList = listOf(
                menu("beverage-cola", "콜라", 2_500, BEVERAGE_IMAGE_URL),
                menu("beverage-zero-cola", "제로 콜라", 2_500, BEVERAGE_IMAGE_URL),
                menu("beverage-lemonade", "수제 레몬에이드", 5_500, BEVERAGE_IMAGE_URL),
                menu("beverage-grapefruit", "자몽에이드", 5_500, BEVERAGE_IMAGE_URL),
                menu("beverage-americano", "아메리카노", 4_000, BEVERAGE_IMAGE_URL),
                menu("beverage-beer", "논알코올 맥주", 5_900, BEVERAGE_IMAGE_URL),
            ),
        ),
    )

    val primaryCategories: List<PrimaryCategory> = listOf(
        PrimaryCategory(
            id = "main-dish",
            name = "메인 메뉴",
            subCategories = selectCategories(
                "burger",
                "pizza",
                "pasta",
            ),
        ),
        PrimaryCategory(
            id = "light-meal",
            name = "라이트 메뉴",
            subCategories = selectCategories(
                "salad",
                "side",
            ),
        ),
        PrimaryCategory(
            id = "cafe",
            name = "디저트와 음료",
            subCategories = selectCategories(
                "dessert",
                "beverage",
            ),
        ),
    )

    /** 전체 하위 카테고리를 Grid 표시 순서로 평탄화한 목록입니다. */
    val categories: List<Category> = primaryCategories.flatMap { primaryCategory ->
        primaryCategory.subCategories
    }

    private fun selectCategories(vararg ids: String): List<Category> = ids.map { id ->
        secondaryCategories.first { category -> category.id == id }
    }

    private fun menu(
        id: String,
        name: String,
        price: Int,
        imageUrl: String,
    ): Menu = Menu(
        id = id,
        name = name,
        price = price,
        imageUrl = imageUrl,
    )
}
