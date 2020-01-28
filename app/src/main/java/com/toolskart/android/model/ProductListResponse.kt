package com.toolskart.android.model

import com.toolskart.android.model.inputmodel.ModifyCartIp

data class ProductListResponse(val product: ArrayList<Product>?) : CommonRes()

data class Product(
        val _id: String? = null,
        val article_no: String? = null,
        val avg_rating: Float?,
        val brand: String? = null,
        val cat1: String? = null,
        val cat1_name: String? = null,
        val cat2: String? = null,
        val cat2_name: String? = null,
        val cat3: String? = null,
        val cat3_name: String? = null,
        val description: String? = null,
        val express: String? = null,
        val id_category: String? = null,
        val name: String? = null,
        val pic: ArrayList<Pic>? = null,
        val ratingtest: Ratingtest? = null,
        val review: ArrayList<Review>? = null,
        var sku: List<Sku>? = null,
        val spec: ArrayList<Spec?>? = null,
        val tags: String? = null,
        val total_rating: Int? = null,

        /*product detail*/
        val category: String? = null,
        val category_id: String? = null,
        val id_product: String? = null,
        val is_subscribe: String? = null,
        var wishlist: Int?,

       /*wishList*/
        val actual_price: Int?,
        val mrp: Int?,
        val selling_price: Int?,

        /*prodPos extra parameter added by me  for wishList*/
        var prodPos: Int = -1,
        /*added by me */
        var tempWishlist: Int = -1,
        var selSku: Sku?,


       /*cart*/
        val image: String?=null,
        val my_express: String?=null,
        var modifyCartIp: ModifyCartIp? = null


        ){
    fun getSelectedSku(): Sku = if (selSku == null && sku!!.isNotEmpty()) sku!![0] else selSku!!

    fun isWishListEnabled(): Boolean {
        return wishlist == 1
    }
}

data class Spec(
        val _id: String? = null,
        val specification: String? = null,
        val value: String? = null
)

data class Ratingtest(
        val star1: Int? = null,
        val star2: Int? = null,
        val star3: Int? = null,
        val star4: Int? = null,
        val star5: Int? = null,
        val value1: Int? = null,
        val value2: Int? = null,
        val value3: Int? = null,
        val value4: Int? = null,
        val value5: Int? = null
)

data class Pic(
        val _id: String? = null,
        val pic: String? = null,
        val pic_thumb: String? = null
)

data class Review(
        val _id: String? = null,
        val dateadded: String? = null,
        val first_name: String? = null,
        val id_customer: String? = null,
        val is_active: String?= null,
        val last_name: String? = null,
        val pic: String? = null,
        val rating: String? = null,
        val review: String? = null
)

data class Sku(
        val _id: String? = null,
        val active: String? = null,
        val box_color: Any? = null,
        val brand_en: String? = null,
        val case_quantity: String? = null,
        val cessperc: String? = null,
        val cessperpc: String? = null,
        val cgst: String? = null,
        val check_expired: String? = null,
        val ean_codes: String? = null,
        val express: String? = null,
        val fill_rate: String? = null,
        val filter_color: Any? = null,
        val group_id: Any? = null,
        val gst: String? = null,
        val hsn_codes: String? = null,
        val icm_name: String? = null,
        val id_category: String? = null,
        val id_warehouse: String? = null,
        val igst: String? = null,
        val max_quantity: String? = null,
        val mbq: String? = null,
        val min_quantity: String? = null,
        val mrp: Float? = null,
        var mycart: String? = null,
        val ooos: String? = null,
        val order_part_type: String? = null,
        val pack_size: String? = null,
        val parent_warehouse: String? = null,
        val quantity: String? = null,
        val rack_location: String? = null,
        val realization: String? = null,
        val selling_price: Float? = null,
        val sgst: String? = null,
        val size: String? = null,
        val size_measuring_unit: Any? = null,
        val sku: String? = null,
        val sold: String? = null,
        val stock: String? = null,
        val sub_type: String? = null,
        val supplier_price: String? = null,
        val tax: String? = null,
        val taxtype: String? = null,
        val threshold_margin_value: String? = null,
        val variable_weight: String? = null,
        val weight_in_gms: String? = null,

       /*wishList*/
        val id_product: String?=null,

        var tempMyCart: Int = -1


)