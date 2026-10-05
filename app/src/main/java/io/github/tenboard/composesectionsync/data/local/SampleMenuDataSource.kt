package io.github.tenboard.composesectionsync.data.local

import io.github.tenboard.composesectionsync.model.Category
import io.github.tenboard.composesectionsync.model.Menu
import io.github.tenboard.composesectionsync.model.PrimaryCategory

internal object SampleMenuDataSource {
    // Free photo licenses: https://www.pexels.com/license/ and https://unsplash.com/license

    private val secondaryCategories: List<Category> = listOf(
        Category(
            id = "burger",
            name = "Burgers",
            menuList = listOf(
                // mahdisa ramezanzadeh: https://www.pexels.com/photo/close-up-of-a-beef-cheeseburger-on-a-plate-18592187/
                menu(
                    id = "burger-classic",
                    name = "Classic Cheeseburger",
                    priceCents = 890,
                    imageUrl = "https://images.pexels.com/photos/18592187/pexels-photo-18592187.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600",
                ),
                // Enis Yavuz: https://unsplash.com/photos/burger-with-lettuce-and-tomato-tuuh4DrgMpU
                menu(
                    id = "burger-double",
                    name = "Double Beef Burger",
                    priceCents = 1_190,
                    imageUrl = "https://images.unsplash.com/photo-1599155253646-7989e08c05c1?auto=format&fit=crop&w=800&h=600&q=80",
                ),
                // Jonathan Borba: https://unsplash.com/photos/a-bacon-egg-and-cheese-burger-on-a-black-background-fB905lrmzMU
                menu(
                    id = "burger-bacon",
                    name = "Bacon & Egg Burger",
                    priceCents = 1_090,
                    imageUrl = "https://images.unsplash.com/photo-1700835880369-467ebff9d324?auto=format&fit=crop&w=800&h=600&q=80",
                ),
                // Erwin Quintana: https://www.pexels.com/photo/photograph-of-a-chicken-burger-7963093/
                menu(
                    id = "burger-chicken",
                    name = "Crispy Chicken Burger",
                    priceCents = 950,
                    imageUrl = "https://images.pexels.com/photos/7963093/pexels-photo-7963093.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600",
                ),
                // FAÍSCA CRIATIVA: https://www.pexels.com/photo/a-shrimp-sandwich-with-cheese-and-sauce-on-top-27988485/
                menu(
                    id = "burger-shrimp",
                    name = "Shrimp Burger",
                    priceCents = 1_050,
                    imageUrl = "https://images.pexels.com/photos/27988485/pexels-photo-27988485.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600",
                ),
                // Christian Gazzabini: https://www.pexels.com/photo/gourmet-mushroom-and-cheese-burger-with-fries-31992843/
                menu(
                    id = "burger-mushroom",
                    name = "Mushroom Cheeseburger",
                    priceCents = 1_090,
                    imageUrl = "https://images.pexels.com/photos/31992843/pexels-photo-31992843.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600&crop=bottom",
                ),
            ),
        ),
        Category(
            id = "pizza",
            name = "Pizza",
            menuList = listOf(
                // Amit Fulwaria: https://www.pexels.com/photo/top-view-of-a-pizza-margherita-20115306/
                menu(
                    id = "pizza-margherita",
                    name = "Margherita Pizza",
                    priceCents = 1_590,
                    imageUrl = "https://images.pexels.com/photos/20115306/pexels-photo-20115306.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600",
                ),
                // amirali mirhashemian: https://unsplash.com/photos/pepperoni-pizza-r_1JnXTYKnA
                menu(
                    id = "pizza-pepperoni",
                    name = "Pepperoni Pizza",
                    priceCents = 1_790,
                    imageUrl = "https://images.unsplash.com/photo-1564249484723-8303f8ee99e4?auto=format&fit=crop&w=800&h=600&q=80",
                ),
                // Anhelina Vasylyk: https://www.pexels.com/photo/rustic-four-cheese-pizza-on-wooden-table-33592983/
                menu(
                    id = "pizza-cheese",
                    name = "Four Cheese Pizza",
                    priceCents = 1_890,
                    imageUrl = "https://images.pexels.com/photos/33592983/pexels-photo-33592983.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600",
                ),
                // Amit Fulwaria: https://www.pexels.com/photo/top-view-of-a-pizza-on-a-wooden-cutting-board-20115309/
                menu(
                    id = "pizza-hawaiian",
                    name = "Hawaiian Pizza",
                    priceCents = 1_750,
                    imageUrl = "https://images.pexels.com/photos/20115309/pexels-photo-20115309.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600",
                ),
                // Foad Roshan: https://unsplash.com/photos/a-close-up-of-a-pizza-with-meat-and-cheese-QRUFgHHd2F8
                menu(
                    id = "pizza-bulgogi",
                    name = "Beef & Mushroom Pizza",
                    priceCents = 1_990,
                    imageUrl = "https://images.unsplash.com/photo-1719467297742-ab5665fd5d2e?auto=format&fit=crop&w=800&h=600&q=80",
                ),
                // esrannuur: https://www.pexels.com/photo/a-pizza-with-potato-and-cheese-toppings-on-the-table-13599446/
                menu(
                    id = "pizza-potato",
                    name = "Potato & Cheese Pizza",
                    priceCents = 1_950,
                    imageUrl = "https://images.pexels.com/photos/13599446/pexels-photo-13599446.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600&crop=bottom",
                ),
            ),
        ),
        Category(
            id = "pasta",
            name = "Pasta",
            menuList = listOf(
                // Nadin Sh: https://www.pexels.com/photo/pasta-with-mussels-prawns-and-tomato-sauce-10895785/
                menu(
                    id = "pasta-tomato",
                    name = "Seafood Tomato Pasta",
                    priceCents = 1_490,
                    imageUrl = "https://images.pexels.com/photos/10895785/pexels-photo-10895785.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600",
                ),
                // Helen Van: https://unsplash.com/photos/a-plate-of-pasta-with-bacon-on-it-bYSvpF8NBmE
                menu(
                    id = "pasta-carbonara",
                    name = "Bacon Carbonara",
                    priceCents = 1_590,
                    imageUrl = "https://images.unsplash.com/photo-1683827449087-ccf5a9e6283c?auto=format&fit=crop&w=800&h=600&q=80",
                ),
                // solod_sha: https://www.pexels.com/photo/pasta-on-white-ceramic-plate-7664088/
                menu(
                    id = "pasta-rose",
                    name = "Creamy Shrimp Pasta",
                    priceCents = 1_690,
                    imageUrl = "https://images.pexels.com/photos/7664088/pexels-photo-7664088.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600",
                ),
                // adrian vieriu: https://www.pexels.com/photo/spaghetti-aglio-e-olio-with-chopped-basil-on-white-plate-11654236/
                menu(
                    id = "pasta-aglio",
                    name = "Aglio e Olio",
                    priceCents = 1_390,
                    imageUrl = "https://images.pexels.com/photos/11654236/pexels-photo-11654236.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600",
                ),
                // Klaus Nielsen: https://www.pexels.com/photo/appetizing-spaghetti-pasta-with-bolognese-sauce-served-in-kitchen-6287520/
                menu(
                    id = "pasta-ragu",
                    name = "Bolognese Pasta",
                    priceCents = 1_650,
                    imageUrl = "https://images.pexels.com/photos/6287520/pexels-photo-6287520.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600",
                ),
                // Nadin Sh: https://www.pexels.com/photo/pasta-dish-with-mussels-17243892/
                menu(
                    id = "pasta-vongole",
                    name = "Vongole Pasta",
                    priceCents = 1_590,
                    imageUrl = "https://images.pexels.com/photos/17243892/pexels-photo-17243892.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600",
                ),
            ),
        ),
        Category(
            id = "salad",
            name = "Salads",
            menuList = listOf(
                // Julia Filirovska: https://www.pexels.com/photo/close-up-of-a-salad-with-chicken-8251536/
                menu(
                    id = "salad-caesar",
                    name = "Chicken Caesar Salad",
                    priceCents = 1_090,
                    imageUrl = "https://images.pexels.com/photos/8251536/pexels-photo-8251536.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600",
                ),
                // Nadin Sh: https://www.pexels.com/photo/delicious-cream-with-tomatoes-on-plate-16854488/
                menu(
                    id = "salad-ricotta",
                    name = "Tomato & Ricotta Salad",
                    priceCents = 1_190,
                    imageUrl = "https://images.pexels.com/photos/16854488/pexels-photo-16854488.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600",
                ),
                // dytmeri: https://www.pexels.com/photo/fresh-salad-bowl-with-avocado-and-poached-egg-28508745/
                menu(
                    id = "salad-avocado",
                    name = "Avocado & Egg Salad",
                    priceCents = 1_290,
                    imageUrl = "https://images.pexels.com/photos/28508745/pexels-photo-28508745.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600",
                ),
                // Ayat Shahin: https://www.pexels.com/photo/shrimp-salad-27863236/
                menu(
                    id = "salad-shrimp",
                    name = "Grilled Shrimp Salad",
                    priceCents = 1_350,
                    imageUrl = "https://images.pexels.com/photos/27863236/pexels-photo-27863236.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600",
                ),
                // Change C.C: https://www.pexels.com/photo/close-up-of-a-salad-with-salmon-20150371/
                menu(
                    id = "salad-salmon",
                    name = "Smoked Salmon Salad",
                    priceCents = 1_490,
                    imageUrl = "https://images.pexels.com/photos/20150371/pexels-photo-20150371.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600",
                ),
                // Aveedibya Dey: https://unsplash.com/photos/a-plate-of-food-IhzS6pOVrRE
                menu(
                    id = "salad-tofu",
                    name = "Grilled Tofu Salad",
                    priceCents = 990,
                    imageUrl = "https://images.unsplash.com/photo-1664681340334-5e45eae48e4e?auto=format&fit=crop&w=800&h=600&q=80",
                ),
            ),
        ),
        Category(
            id = "side",
            name = "Sides",
            menuList = listOf(
                // Aleks Magnusson: https://www.pexels.com/photo/macro-photography-of-french-fries-2962450/
                menu(
                    id = "side-fries",
                    name = "French Fries",
                    priceCents = 450,
                    imageUrl = "https://images.pexels.com/photos/2962450/pexels-photo-2962450.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600",
                ),
                // Valeria Boltneva: https://www.pexels.com/photo/fries-in-melted-cheese-17035133/
                menu(
                    id = "side-cheese-fries",
                    name = "Cheese Fries",
                    priceCents = 590,
                    imageUrl = "https://images.pexels.com/photos/17035133/pexels-photo-17035133/free-photo-of-fries-in-melted-cheese.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600",
                ),
                // Ron Lach: https://www.pexels.com/photo/onion-rings-in-close-up-photography-8880734/
                menu(
                    id = "side-onion-ring",
                    name = "Onion Rings",
                    priceCents = 550,
                    imageUrl = "https://images.pexels.com/photos/8880734/pexels-photo-8880734.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600&crop=bottom",
                ),
                // Robert Moutongoh: https://www.pexels.com/photo/pieces-of-crispy-fried-chicken-in-close-up-shot-8919199/
                menu(
                    id = "side-chicken",
                    name = "Crispy Chicken",
                    priceCents = 790,
                    imageUrl = "https://images.pexels.com/photos/8919199/pexels-photo-8919199.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600",
                ),
                // Scott Eckersley: https://unsplash.com/photos/fried-chicken-on-black-plate-R-7_ErUOLxw
                menu(
                    id = "side-wings",
                    name = "Buffalo Wings",
                    priceCents = 890,
                    imageUrl = "https://images.unsplash.com/photo-1608039755401-742074f0548d?auto=format&fit=crop&w=800&h=600&q=80&crop=bottom",
                ),
                // Ilo Frey: https://www.pexels.com/photo/close-up-of-freshly-baked-garlic-bread-loaves-37043987/
                menu(
                    id = "side-garlic-bread",
                    name = "Garlic Bread",
                    priceCents = 490,
                    imageUrl = "https://images.pexels.com/photos/37043987/pexels-photo-37043987/free-photo-of-close-up-of-freshly-baked-garlic-bread-loaves.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600",
                ),
            ),
        ),
        Category(
            id = "dessert",
            name = "Desserts",
            menuList = listOf(
                // Yulia Khlebnikova: https://unsplash.com/photos/cheesecake-with-pink-toppings-FDYbS43jUrU
                menu(
                    id = "dessert-cheesecake",
                    name = "New York Cheesecake",
                    priceCents = 690,
                    imageUrl = "https://images.unsplash.com/photo-1578775887804-699de7086ff9?auto=format&fit=crop&w=800&h=600&q=80",
                ),
                // Jade Wulfraat: https://unsplash.com/photos/square-brown-nut-cake-on-black-baking-pan-fVpY6vK9Luo
                menu(
                    id = "dessert-chocolate",
                    name = "Walnut Brownie",
                    priceCents = 650,
                    imageUrl = "https://images.unsplash.com/photo-1464196209402-a16825eb34f9?auto=format&fit=crop&w=800&h=600&q=80",
                ),
                // You Le: https://unsplash.com/photos/a-slice-of-tiramisu-dusted-with-cocoa-powder-M8eoHveep-8
                menu(
                    id = "dessert-tiramisu",
                    name = "Classic Tiramisu",
                    priceCents = 750,
                    imageUrl = "https://images.unsplash.com/photo-1774428755024-88024a28223c?auto=format&fit=crop&w=800&h=600&q=80",
                ),
                // Electra Studio: https://www.pexels.com/photo/vanilla-ice-cream-in-glass-dish-on-red-background-30649019/
                menu(
                    id = "dessert-ice-cream",
                    name = "Vanilla Ice Cream",
                    priceCents = 490,
                    imageUrl = "https://images.pexels.com/photos/30649019/pexels-photo-30649019/free-photo-of-vanilla-ice-cream-in-glass-dish-on-red-background.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600",
                ),
                // Axel Bimashanda: https://unsplash.com/photos/a-waffle-covered-in-syrup-sitting-on-top-of-a-white-plate-XwZ9nqAFoh8
                menu(
                    id = "dessert-croffle",
                    name = "Syrup Croffle",
                    priceCents = 790,
                    imageUrl = "https://images.unsplash.com/photo-1634481568985-0a118f0ac531?auto=format&fit=crop&w=800&h=600&q=80",
                ),
                // Anna Tukhfatullina: https://unsplash.com/photos/pancake-with-strawberry-and-blue-berry-71exsgFy6CI
                menu(
                    id = "dessert-pancake",
                    name = "Berry Cheese Pancakes",
                    priceCents = 850,
                    imageUrl = "https://images.unsplash.com/photo-1565592826255-e4a6946f5706?auto=format&fit=crop&w=800&h=600&q=80",
                ),
            ),
        ),
        Category(
            id = "beverage",
            name = "Drinks",
            menuList = listOf(
                // Edge2Edge Media: https://unsplash.com/photos/clear-drinking-glass-with-ice-and-red-straw-0KxfiWujzyY/
                menu(
                    id = "beverage-cola",
                    name = "Cola",
                    priceCents = 250,
                    imageUrl = "https://images.unsplash.com/photo-1605712916345-6ef6bcc2e29c?auto=format&fit=crop&w=800&h=600&q=80",
                ),
                // Ron Lach: https://www.pexels.com/photo/a-glass-of-iced-cola-8879617/
                menu(
                    id = "beverage-zero-cola",
                    name = "Iced Cola",
                    priceCents = 250,
                    imageUrl = "https://images.pexels.com/photos/8879617/pexels-photo-8879617.jpeg?auto=compress&cs=tinysrgb&fit=crop&w=800&h=600",
                ),
                // Rémy Golinelli: https://unsplash.com/photos/a-tall-glass-of-lemonade-with-mint-and-lemon-DTlDH3jF89k
                menu(
                    id = "beverage-lemonade",
                    name = "House Lemonade",
                    priceCents = 550,
                    imageUrl = "https://images.unsplash.com/photo-1763379978357-482f322c93f5?auto=format&fit=crop&w=800&h=600&q=80",
                ),
                // Photo Tora: https://unsplash.com/photos/refreshing-grapefruit-drink-on-a-wooden-tray-Xf6Uc2rHp74
                menu(
                    id = "beverage-grapefruit",
                    name = "Grapefruit Soda",
                    priceCents = 550,
                    imageUrl = "https://images.unsplash.com/photo-1752550901046-ad49e6063dd7?auto=format&fit=crop&w=800&h=600&q=80",
                ),
                // Gerson Cifuentes: https://unsplash.com/photos/white-coffee-cup-on-brown-wooden-table-JNhaaPEz3FY
                menu(
                    id = "beverage-americano",
                    name = "Americano",
                    priceCents = 400,
                    imageUrl = "https://images.unsplash.com/photo-1551030173-122aabc4489c?auto=format&fit=crop&w=800&h=600&q=80",
                ),
                // Matt Palmer: https://unsplash.com/photos/clear-drinking-glass-with-beer-lGzhgzkN6UI
                menu(
                    id = "beverage-beer",
                    name = "Draft Beer",
                    priceCents = 590,
                    imageUrl = "https://images.unsplash.com/photo-1581457954431-18ef26091378?auto=format&fit=crop&w=800&h=600&q=80",
                ),
            ),
        ),
    )

    val primaryCategories: List<PrimaryCategory> = listOf(
        PrimaryCategory(
            id = "main-dish",
            name = "Main Dishes",
            subCategories = selectCategories(
                "burger",
                "pizza",
                "pasta",
            ),
        ),
        PrimaryCategory(
            id = "light-meal",
            name = "Light Bites",
            subCategories = selectCategories(
                "salad",
                "side",
            ),
        ),
        PrimaryCategory(
            id = "cafe",
            name = "Desserts & Drinks",
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
        priceCents: Int,
        imageUrl: String,
    ): Menu = Menu(
        id = id,
        name = name,
        priceCents = priceCents,
        imageUrl = imageUrl,
    )
}
