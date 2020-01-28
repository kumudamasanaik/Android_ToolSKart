package com.toolskart.android.ui.myprofile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.MenuItem
import android.view.View
import com.theartofdev.edmodo.cropper.CropImage
import com.toolskart.android.R
import com.toolskart.android.api.LiveNetworkMonitor
import com.toolskart.android.customview.ScreenStateView
import com.toolskart.android.model.Customer
import com.toolskart.android.model.MyProfileRes
import com.toolskart.android.model.UpdateProfileRes
import com.toolskart.android.ui.base.BaseActivity
import com.toolskart.android.ui.transactionhistory.TransactionHistoryActivity
import com.toolskart.android.utils.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*

class MyProfileActivity : BaseActivity(), View.OnClickListener, MyProfileContract.View {

    private val TAG = "MyProfileActivity"
    private lateinit var mContext: Context
    private lateinit var presenter: MyProfilePresenter
    private lateinit var snackbbar: View
    private var mMyAccountResult: Customer? = null
    private lateinit var bitmap: Bitmap
    var resultImageString: String = ""
    private lateinit var customerPicURl: String

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_my_profile, fragmentLayout)

        init()
    }

    override fun init() {
        tv_profile_name!!.isEnabled = false
        tv_mob_num.isEnabled = false
        snackbbar = my_profile_multiStateView
        tvTitle?.text = getString(R.string.my_profile)
        iv_cart!!.setImageResource(R.drawable.ic_edit_icon)
        presenter = MyProfilePresenter(this)
        empty_button.setOnClickListener { callMyProfile() }
        error_button.setOnClickListener { callMyProfile() }
        setProfiletoolbar()
        showBack()
        callMyProfile()
        transaction.setOnClickListener(this)
        updateCustomerProfilePic()
        btn_update_profile.setOnClickListener(this)
        iv_cart!!.setOnClickListener {
            CropImage.activity().start(this)
        }
    }


    private fun updateCustomerProfilePic() {
        if (CommonUtils.getProfilePic().isNotEmpty()) {
            iv_profile_image.visibility = View.VISIBLE
            customerPicURl = CommonUtils.getProfilePic()
            if (Validation.isValidString(customerPicURl)) {
                ImageLoader.setImage(imageView = iv_profile_image, imageUrl = customerPicURl)
            }
        } else {
            iv_profile_image.setBackgroundResource(R.drawable.ic_user_grey)
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_update_profile -> {
                updateMyProfile()
            }
            R.id.transaction -> {
                startActivity(Intent(this, TransactionHistoryActivity::class.java))
            }
        }
    }

    override fun callMyProfile() {
        if (LiveNetworkMonitor(this).isConnected()) {
            showViewState(ScreenStateView.VIEW_STATE_LOADING)
            presenter.callMyProfileApi()
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setMyProfileResp(response: MyProfileRes) {
        if (Validation.isValidStatus(response)) {
            mMyAccountResult = response.result!![0]
            CommonUtils.setCustomerData(response.result[0])
            showViewState(ScreenStateView.VIEW_STATE_CONTENT)
            showData()
        } else {
            showViewState(ScreenStateView.VIEW_STATE_EMPTY)
        }
    }

    private fun showData() {
        mMyAccountResult = CommonUtils.getCurrentUser()
        mMyAccountResult?.apply {
            if (Validation.isValidString(first_name))
                tv_profile_name.setText(first_name)

            if (Validation.isValidString(mobile))
                tv_mob_num.setText(mobile)

            if (Validation.isValidString(address))
                tv_my_address.setText(address)

            if (Validation.isValidString(pic))
                ImageLoader.setImage(iv_profile_image, pic!!)
        }
    }

    override fun updateMyProfile() {
        if (LiveNetworkMonitor(this).isConnected()) {
            showViewState(ScreenStateView.VIEW_STATE_LOADING)
            presenter.callUpdateMyProfileApi(tv_my_address.text.toString(), resultImageString)
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setUpdateMyProfileResp(updatedMyProfileRes: UpdateProfileRes) {
        hideLoader()
        if (Validation.isValidStatus(updatedMyProfileRes)) {
            if (Validation.isValidObject(updatedMyProfileRes.result)) {
                showViewState(ScreenStateView.VIEW_STATE_CONTENT)
                CommonUtils.setCustomerData(updatedMyProfileRes.result!![0])
                updateUI()
            }
        } else
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
    }

    private fun updateUI() {
        mMyAccountResult = CommonUtils.getCurrentUser()!!
        mMyAccountResult?.apply {

            if (address!!.isNotEmpty())
                tv_my_address.setText(address)

            if (pic!!.isNotEmpty())
                ImageLoader.setImage(iv_profile_image, pic)

        }
    }

    override fun showMsg(msg: String?) {
        showToastMsg(msg!!)
    }

    override fun showLoader() {
        CommonUtils.showLoading(this, "", true)
    }

    override fun hideLoader() {
        CommonUtils.hideLoading()
    }

    override fun showViewState(state: Int) {
        if (my_profile_multiStateView != null)
            my_profile_multiStateView.viewState = state
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                iv_profile_image.setImageURI(resultUri)

                bitmap = (iv_profile_image.drawable as BitmapDrawable).bitmap
                getBase64Image(bitmap).execute()

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class getBase64Image(private var bitmap: Bitmap) : AsyncTask<Void, Void, String>() {

        override fun doInBackground(vararg voids: Void): String {
            return CommonUtils.convertToBase64(bitmap)
        }

        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
            resultImageString = s
            MyLogUtils.d(TAG, "IMAGE URL :$resultImageString")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}