package com.toolskart.android.di

import com.toolskart.android.ui.aboutus.AboutUsActivity
import com.toolskart.android.ui.customercare.CustomerCareActivity
import com.toolskart.android.ui.home.HomeActivity
import com.toolskart.android.ui.login.LoginActivity
import com.toolskart.android.ui.myaddress.MyAddressActivity
import com.toolskart.android.ui.mycart.MyCartActivity
import com.toolskart.android.ui.myorder.MyOrderActivity
import com.toolskart.android.ui.myprofile.MyProfileActivity
import com.toolskart.android.ui.navigationdrawer.NavigationDrawerFragment
import com.toolskart.android.ui.notification.NotificationActivity
import com.toolskart.android.ui.offerzone.OfferZoneActivity
import com.toolskart.android.ui.orderdetails.OrderDetailsActivity
import com.toolskart.android.ui.payment.PaymentActivity
import com.toolskart.android.ui.privacypolicy.PrivacyPolicyActivity
import com.toolskart.android.ui.productdetails.ProductDetailsActivity
import com.toolskart.android.ui.productlist.ProductListActivity
import com.toolskart.android.ui.products.ProductsActivity
import com.toolskart.android.ui.review.ReviewActivity
import com.toolskart.android.ui.search.SearchActivity
import com.toolskart.android.ui.shopbycategory.ShopByCategoryActivity
import com.toolskart.android.ui.splash.SplashActivity
import com.toolskart.android.ui.transactionhistory.TransactionHistoryActivity
import com.toolskart.android.ui.verifyotp.VerifyOTP
import com.toolskart.android.ui.viewallproduct.ViewAllProductActivity
import com.toolskart.android.ui.wishlist.WishListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MyActivityBuilder {

    @ContributesAndroidInjector()
    internal abstract fun splashActivity(): SplashActivity

    @ContributesAndroidInjector()
    internal abstract fun loginActivity(): LoginActivity

    @ContributesAndroidInjector()
    internal abstract fun homeActivity(): HomeActivity

    @ContributesAndroidInjector()
    internal abstract fun navigationFragment(): NavigationDrawerFragment

    @ContributesAndroidInjector()
    internal abstract fun verifyOTP(): VerifyOTP

    @ContributesAndroidInjector()
    internal abstract fun productsActivity(): ProductsActivity

    @ContributesAndroidInjector()
    internal abstract fun productDetailActivity(): ProductDetailsActivity


    @ContributesAndroidInjector()
    internal abstract fun viewAllproductActivity(): ViewAllProductActivity

    @ContributesAndroidInjector()
    internal abstract fun myOrderActivity(): MyOrderActivity

    @ContributesAndroidInjector()
    internal abstract fun myAddressActivity(): MyAddressActivity

    @ContributesAndroidInjector()
    internal abstract fun privacyPolicyActivity(): PrivacyPolicyActivity

    @ContributesAndroidInjector()
    internal abstract fun notificationActivity(): NotificationActivity

    @ContributesAndroidInjector()
    internal abstract fun aboutusActivity(): AboutUsActivity

    @ContributesAndroidInjector()
    internal abstract fun customercareActivity(): CustomerCareActivity

    @ContributesAndroidInjector()
    internal abstract fun shopBycategoryActivity(): ShopByCategoryActivity

    @ContributesAndroidInjector()
    internal abstract fun offerZoneActivity(): OfferZoneActivity

    @ContributesAndroidInjector()
    internal abstract fun myCartActivity(): MyCartActivity

    @ContributesAndroidInjector()
    internal abstract fun viewOrderDetailActivity(): OrderDetailsActivity

    @ContributesAndroidInjector()
    internal abstract fun reviewActivity(): ReviewActivity

    @ContributesAndroidInjector()
    internal abstract fun wishListActivity(): WishListActivity

    @ContributesAndroidInjector()
    internal abstract fun myProfileActivity(): MyProfileActivity

    @ContributesAndroidInjector()
    internal abstract fun transactionHistoryActivity(): TransactionHistoryActivity

    @ContributesAndroidInjector()
    internal abstract fun productListActivity(): ProductListActivity

    @ContributesAndroidInjector()
    internal abstract fun searchActivity(): SearchActivity

    @ContributesAndroidInjector()
    internal abstract fun paymentActivity(): PaymentActivity
}