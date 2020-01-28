package com.toolskart.android.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class HomeResponse(
        val id_customer: String?,
        val result: Home,
        val summary: CartSummary?
) : CommonRes()

@Parcelize
data class CartSummary(
        val cart_count: String?,
        val delivery_charge: String?,
        val grand_total: String?,
        val mrp: String?,
        val realization: String?,
        val selling_price: String?
) : Parcelable

@Parcelize
data class Home(
        val banner: ArrayList<Banner?>?,
        val category: ArrayList<Category>?,
        val featured_products: ArrayList<FeaturedProduct?>?,
        val offers: ArrayList<Offer?>?

) : Parcelable

@Parcelize
data class FeaturedProduct(
        val _id: String?,
        val article_no: String?,
        val brand: String?,
        val description: String?,
        val id_category: String?,
        val name: String?,
        val pic: ArrayList<Pic?>?,
        val status: String?,
        val tags: String?,
        val wishlist: Int?
) : Parcelable {

    @Parcelize
    data class Pic(
            val _id: String?,
            val id_product: String?,
            val pic: String?,
            val pic_thumb: String?
    ) : Parcelable
}

@Parcelize
data class Banner(
        val _id: String?,
        val id_warehouse: String?,
        val main_cat: String?,
        val name: String?,
        val name_en: String?,
        val name_gu: String?,
        val parent_warehouseid: String?,
        val pic: String?,
        val pic_web: String?,
        val sequence: String?,
        val target: String?,
        val title: String?,
        val title_en: String?,
        val title_gu: String?,
        val type: String?,
        val type_id: String?,
        val validity_end: String?,
        val validity_start: String?
) : Parcelable

@Parcelize
data class Offer(
        val _id: String?,
        val condition_items: String?,
        val condition_price: String?,
        val conditions: String?,
        val coupon_code: String?,
        val coupon_name: String?,
        val deal_type: String?,
        val discount: String?,
        val discount_type: String?,
        val id_warehouse: String?,
        val image: String?,
        val sub_title: String?,
        val target: String?,
        val target_type: String?,
        val terms: String?,
        val text: String?,
        val title: String?,
        val validity_end: String?,
        val validity_start: String?
) : Parcelable

@Parcelize
data class Category(
        val _id: String?,
        val description: String?,
        val name: String?,
        val pic: String?,
        val subcategory: ArrayList<Subcategory>?
) : Parcelable

@Parcelize
data class Subcategory(
        val _id: String?,
        val description: String?,
        val express: String?,
        val lowcategory: ArrayList<Lowcategory>?,
        val name_en: String?,
        val name_gu: String?,
        val pic: String?,
        val sort_order: String?
) : Parcelable

@Parcelize
data class Lowcategory(
        val _id: String?,
        val description: String?,
        val express: String?,
        val name_en: String?,
        val name_gu: String?,
        val pic: String?,
        val sort_order: String?
) : Parcelable



