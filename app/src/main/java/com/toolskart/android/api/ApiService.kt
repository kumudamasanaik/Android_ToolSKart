package com.toolskart.android.api

import com.google.gson.JsonObject
import com.toolskart.android.model.*
import com.toolskart.android.model.inputmodel.*
import com.toolskart.android.model.AddAddressRes
import com.toolskart.android.model.DeleteAddressResp
import com.toolskart.android.model.GetAddressResp
import com.toolskart.android.model.UpdateAddress
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST(ApiConstants.REGISTER)
    fun doRegister(@Body registerInput: SignUpInput): Call<CustomerRes>

    @POST(ApiConstants.LOGIN)
    fun doLogin(@Body json: JsonObject): Call<CustomerRes>

    @POST(ApiConstants.OTP)
    fun verifyOtp(@Body json: JsonObject): Call<CustomerRes>

    @POST(ApiConstants.RESEND_OTP)
    fun resendOtp(@Body jsonObject: JsonObject): Call<CustomerRes>

    @POST(ApiConstants.HOME)
    fun getHomeApi(@Body json: JsonObject): Call<HomeResponse>

    @POST(ApiConstants.PRODUCT_LIST)
    fun getChildCategoryForViewPageHeaders(@Body jsonObject: JsonObject): Call<ProductListResponse>

    @POST(ApiConstants.PRODUCT_DETAIL)
    fun getProductDetail(@Body json: JsonObject): Call<ProductDetailResp>

    @POST(ApiConstants.MY_PROFILE)
    fun getMyProfile(@Body json:JsonObject):Call<MyProfileRes>

    @POST(ApiConstants.UPDATE_MY_PROFILE)
    fun getUpdatedProfile(@Body json:JsonObject):Call<UpdateProfileRes>

    @POST(ApiConstants.PROVIDE_REVIEW)
    fun provideReview(@Body input: RatingFeedbackInput): Call<CommonRes>

    @POST(ApiConstants.MODIFY_WISH_LIST)
    fun getModifyWishList(@Body jsonObject: JsonObject): Call<WishListResp>

    @POST(ApiConstants.MODIFY_WISH_LIST)
    fun getWishList(@Body json: JsonObject): Call<WishListResp>

    @POST(ApiConstants.CART)
    fun getModifyCart(@Body jsonObject: ModifyCartIp): Call<CartRes>

    @POST(ApiConstants.REMOVE_CART)
    fun removeCart(@Body json: ModifyCartIp): Call<CartRes>

    @POST(ApiConstants.SEARCH_PRODUCTS)
    fun getSearchResult(@Body input: SearchInput): Call<SearchProductsRes>

    @POST(ApiConstants.NOTIFICATION)
    fun getNotification(@Body json: JsonObject): Call<CustomerRes>

    @POST(ApiConstants.GET_ADDRESS)
    fun getAddress(@Body json: JsonObject): Call<GetAddressResp>

    @POST(ApiConstants.UPDATEADDRESS)
    fun getUpdateAddress(@Body input: AddressInput): Call<UpdateAddress>

    @POST(ApiConstants.UPDATEADDRESS)
    fun getAddAddress(@Body input: AddressInput): Call<AddAddressRes>

    @POST(ApiConstants.UPDATEADDRESS)
    fun getDeleteAddress(@Body input: AddressInput): Call<DeleteAddressResp>

    @POST(ApiConstants.CART)
    fun getCart(@Body json: JsonObject): Call<CartRes>

    @POST(ApiConstants.MY_ORDER)
    fun getMyOrdersList(@Body jsonObject: JsonObject): Call<MyOrderRes>

    @POST(ApiConstants.CANCEL)
    fun getCancelOrder(@Body json: JsonObject): Call<MyOrderRes>

    @POST(ApiConstants.REVIEW)
    fun getAllReview(@Body json: JsonObject): Call<CustomerRes>

    @POST(ApiConstants.ORDER_DETAIL)
    fun getOrderDetailsList(@Body json: JsonObject): Call<OrderDetailRes>

    @GET(ApiConstants.ABOUT_US)
    fun getAboutUsScreen(): Call<AboutUsRes>

    @GET(ApiConstants.PRIVACY_POLICY)
    fun getPrivacuPolicy(): Call<PrivacyPolicyRes>

    @GET(ApiConstants.SHOP_BY_CATEGORY)
    fun getShopByCategory(): Call<ShopByCategoryRes>

    @GET(ApiConstants.FEATURED_PRODUCT_LIST)
    fun getProductList(): Call<FeaturedProductListRes>
}