package com.toolskart.android.ui.navigationdrawer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toolskart.android.R
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.ui.aboutus.AboutUsActivity
import com.toolskart.android.ui.base.adapter.BaseRecAdapter
import com.toolskart.android.ui.customercare.CustomerCareActivity
import com.toolskart.android.ui.home.HomeActivity
import com.toolskart.android.ui.login.LoginActivity
import com.toolskart.android.ui.myaddress.MyAddressActivity
import com.toolskart.android.ui.mycart.MyCartActivity
import com.toolskart.android.ui.myorder.MyOrderActivity
import com.toolskart.android.ui.myprofile.MyProfileActivity
import com.toolskart.android.ui.notification.NotificationActivity
import com.toolskart.android.ui.offerzone.OfferZoneActivity
import com.toolskart.android.ui.privacypolicy.PrivacyPolicyActivity
import com.toolskart.android.ui.shopbycategory.ShopByCategoryActivity
import com.toolskart.android.ui.wishlist.WishListActivity
import com.toolskart.android.utils.CommonUtils
import com.toolskart.android.utils.ImageLoader
import com.toolskart.android.utils.MyApplication
import com.toolskart.android.utils.Validation
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_navigation_drawer.*
import javax.inject.Inject

class NavigationDrawerFragment : DaggerFragment(), IAdapterClickListener {

    private var mDrawerLayout: DrawerLayout? = null
    internal var navigationView: View? = null
    var mContext: Context? = null
    private var menuAdapter: BaseRecAdapter? = null
    private lateinit var customerPicURl: String

    @Inject
    lateinit var myApp: MyApplication

    companion object {
        fun newInstance(): NavigationDrawerFragment {
            return NavigationDrawerFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        navigationView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false)
        return navigationView
    }

    override fun onStart() {
        super.onStart()
        upadateCustomerName()
        updateProfilePic()

            img_my_profile.setOnClickListener {
                if (CommonUtils.isUserLogin()) {
                startActivity(Intent(context, MyProfileActivity::class.java))
            }
                else
                    CommonUtils.showCustomToast(this.context!!, getString(R.string.Please_login_to_the_application), 1)
            }
    }

    private fun updateProfilePic() {
        if (CommonUtils.getProfilePic().isNotEmpty()) {
            img_my_profile.visibility = View.VISIBLE
            customerPicURl = CommonUtils.getProfilePic()
            if (Validation.isValidString(customerPicURl)) {
                ImageLoader.setImage(imageView = img_my_profile, imageUrl = customerPicURl)
            } else {
                img_my_profile.setBackgroundResource(R.drawable.ic_user_grey)
            }
        }
    }

    private fun upadateCustomerName() {
        if (CommonUtils.getUserName().isNotEmpty()) {
            btn_login.visibility = View.GONE
            tv_profile_name.visibility = View.VISIBLE
            tv_profile_name.text = CommonUtils.getUserName()
        } else {
            btn_login.visibility = View.VISIBLE
            tv_profile_name.visibility = View.GONE
        }
        btn_login.setOnClickListener {
            navigateToLoginScreen()
        }
    }

    private fun navigateToLoginScreen() {
        startActivity(Intent(mContext, LoginActivity::class.java))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContext = this!!.activity!!
        initScreen()
    }

    override fun onclick(data: Any, pos: Int, type: String, op: String) {
        when (type) {
            getString(R.string.home) -> {
                startActivity(Intent(mContext, HomeActivity::class.java))
            }

            getString(R.string.my_orders) -> {
                startActivity(Intent(mContext, MyOrderActivity::class.java))
            }

            getString(R.string.my_address) -> {
                startActivity(Intent(mContext, MyAddressActivity::class.java))
            }

            getString(R.string.offer_zone) -> {
                startActivity(Intent(mContext, OfferZoneActivity::class.java))
            }

            getString(R.string.my_cart) -> {
                startActivity(Intent(mContext, MyCartActivity::class.java))
            }

            getString(R.string.shop_by_category) -> {
                startActivity(Intent(mContext, ShopByCategoryActivity::class.java))

            }
            "Offers" -> {
            }

            getString(R.string.wish_list) -> {
                startActivity(Intent(mContext, WishListActivity::class.java))

            }
            getString(R.string.notifications) -> {
                startActivity(Intent(mContext, NotificationActivity::class.java))

            }
            "New Products" -> {
            }
            "Recipes" -> {
                /* if (!myApp.currAct.contentEquals(RecipeStylesActivity::class.java.simpleName)) {
                     startActivity(Intent(mContext, RecipeStylesActivity::class.java))
                 }*/
            }
            getString(R.string.customer_care) -> {
                startActivity(Intent(mContext, CustomerCareActivity::class.java))

            }
            getString(R.string.about_us) -> {
                startActivity(Intent(mContext, AboutUsActivity::class.java))

            }
            getString(R.string.privacy_policy) -> {
                startActivity(Intent(mContext, PrivacyPolicyActivity::class.java))
            }
            getString(R.string.log_out) -> {
                if (CommonUtils.getCustomerID().isNotEmpty()) {
                    CommonUtils.showDialog(mContext!!, getString(R.string.do_u_want_to_logout))
                } else {
                    CommonUtils.showCustomToast(this.context!!, getString(R.string.Please_login_to_the_application), 1)

                }

            }

        }
        closeDrawer()
    }

    private fun initScreen() {
        mContext = activity
        setUpNavDrawer()
    }

    fun setUpNavDrawer() {
        rv_drawer_item.layoutManager = LinearLayoutManager(mContext)
        rv_drawer_item!!.setHasFixedSize(true)
        menuAdapter = BaseRecAdapter(mContext!!, R.layout.item_left_menu, adapterClickListener = this)
        rv_drawer_item.adapter = menuAdapter

        getMenuItems()
    }

    private fun getMenuItems() {
        var list = ArrayList<MenuItemModel>()
        list.add(MenuItemModel(R.drawable.ic_home, getString(R.string.home)))
        list.add(MenuItemModel(R.drawable.ic_my_orders, getString(R.string.my_orders)))
        list.add(MenuItemModel(R.drawable.ic_my_cart, getString(R.string.my_cart)))
        list.add(MenuItemModel(R.drawable.ic_addresses, getString(R.string.my_address)))
        list.add(MenuItemModel(R.drawable.ic_offer_zone, getString(R.string.offer_zone)))
        list.add(MenuItemModel(R.drawable.ic_shop_by_category, getString(R.string.shop_by_category)))
        list.add(MenuItemModel(R.drawable.ic_wish_list, getString(R.string.wish_list)))
        list.add(MenuItemModel(R.drawable.ic_bell, getString(R.string.notifications)))
        list.add(MenuItemModel(R.drawable.ic_customer_care, getString(R.string.customer_care)))
        list.add(MenuItemModel(R.drawable.ic_about_us, getString(R.string.about_us)))
        list.add(MenuItemModel(R.drawable.ic_privacy_policy, getString(R.string.privacy_policy)))
        list.add(MenuItemModel(R.drawable.ic_logout, getString(R.string.log_out)))
        menuAdapter!!.addList(list)


    }

    fun setDrawer(drawerLayout: DrawerLayout) {
        mDrawerLayout = drawerLayout
    }

    fun closeDrawer() {
        mDrawerLayout!!.closeDrawer(Gravity.START)
    }
}