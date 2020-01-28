package com.toolskart.android.ui.login

import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.toolskart.android.R
import com.toolskart.android.api.LiveNetworkMonitor
import com.toolskart.android.boardcastreceiver.MySMSBroadcastReceiver
import com.toolskart.android.constants.Constants
import com.toolskart.android.model.CustomerRes
import com.toolskart.android.model.inputmodel.SignUpInput
import com.toolskart.android.ui.verifyotp.VerifyOTP
import com.toolskart.android.utils.*
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : DaggerAppCompatActivity(), LoginContract.View, View.OnClickListener, OnSuccessListener<Void>, OnFailureListener, MySMSBroadcastReceiver.OTPReceiveListener {

    @Inject
    lateinit var appShredPref: AppSharedPreference

    private lateinit var context: Context
    private val TAG = "LoginInActivity"
    private lateinit var presenter: LoginPresenter
    private lateinit var registerInput: SignUpInput
    private lateinit var smsReceiver: MySMSBroadcastReceiver
    private var intentFilter: IntentFilter? = null
    var otpPhone: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        context = this
        init()
    }

    override fun init() {
        AppSignatureHelper(this).appSignatures

        presenter = LoginPresenter(this)

        btn_sign_in.setOnClickListener(this)
        btn_sign_up.setOnClickListener(this)
        btn_get_otp.setOnClickListener(this)
        btn_next.setOnClickListener(this)
        tv_sign_in_guest.setOnClickListener(this)

    }


    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_sign_in -> {
                    ll_sign_in.visibility = View.VISIBLE
                    ll_sign_up.visibility = View.GONE
                    view_sign_in.visibility = View.VISIBLE
                    view_sign_out.visibility = View.GONE
                }

                R.id.btn_sign_up -> {
                    ll_sign_in.visibility = View.GONE
                    ll_sign_up.visibility = View.VISIBLE
                    view_sign_in.visibility = View.GONE
                    view_sign_out.visibility = View.VISIBLE
                }

                R.id.btn_get_otp -> {
                    doLogin()
                }

                R.id.btn_next -> {
                    doRegister()
                }

                R.id.tv_sign_in_guest -> {
                    CommonUtils.goToDashBoard(this)
                }
            }
        }
    }

    override fun doLogin() {
        if (presenter.validateLogin(et_email.text.toString())) {
            if (LiveNetworkMonitor(this).isConnected()) {
                showLoader()
                presenter.doLogin(et_email.text.toString())
            } else {
                showToastMsg(getString(R.string.error_no_internet))
            }
        }
    }

    override fun setLoginResp(res: CustomerRes) {
        if (Validation.isValidStatus(res)) {
            hideLoader()
            if (res.result.isNotEmpty()) {
                CommonUtils.saveMobileNumber(res.result[0].mobile!!)
                showotpscreen()
            } else
                showMsg(res.message ?: getString(R.string.woops_something_went_wrong))
        } else if (res.message != null) {
            showMsg(res.message!!)
        }
    }

    override fun doRegister() {
        registerInput = SignUpInput(
                name = et_full_name.text.toString(),
                email = et_email_reg.text.toString(),
                mobile = et_mobile.text.toString(),
                sms_key = CommonUtils.getOtpVerificationHashKey()
        )
        if (presenter.validate(registerInput)) {
            if (LiveNetworkMonitor(this).isConnected()) {
                showLoader()
                presenter.callRegister(registerInput)
            } else
                Toast.makeText(this, getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show()
        }
    }

    override fun setRegsiterRes(res: CustomerRes) {
        if (Validation.isValidStatus(res)) {
            hideLoader()
            if (Validation.isValidList(res.result)) {
//                CommonUtils.setCustomerData(res.result[0])
                showotpscreen()
            } else
                showMsg(res.message ?: getString(R.string.woops_something_went_wrong))
        } else {
            if (Validation.isValidString(res.error_code) && res.error_code == Constants.ERROR_CODE_100) {
                showMsg(res.message!!)
                showotpscreen()
            } else
                showMsg(res.message!!)
        }
    }

    override fun showSignupValidateErrorMsg(msg: String) {
        when (msg) {
            "1" -> et_full_name.error = getString(R.string.err_please_enter_valid_firstname)
            "2" -> et_email_reg.error = getString(R.string.err_please_enter_valid_email)
            "3" -> et_mobile.error = getString(R.string.err_please_enter_valid_mobile)
        }
    }

    override fun invalidEmailPhone() {
        et_email.error = getString(R.string.error_invalid_email)
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

    }

    override fun onOTPReceived(otp: String) {
        if (otp.isNotEmpty()) otpPhone = otp
    }

    override fun onOTPTimeOut() {
        MyLogUtils.i(TAG, getString(R.string.error_something_wrong))
    }

    override fun onFailure(p0: Exception) {
        MyLogUtils.i(TAG, "Failed to receive otp from phone")
    }

    override fun onSuccess(p0: Void?) {
        MyLogUtils.i(TAG, "Received otp from phone")
    }

    private fun showotpscreen() {
        val otpDialog = VerifyOTP()
        otpDialog.show(supportFragmentManager, otpDialog.tag)
        otpDialog.isCancelable = false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
