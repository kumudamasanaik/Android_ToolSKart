package com.toolskart.android.ui.verifyotp

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.toolskart.android.R
import com.toolskart.android.api.LiveNetworkMonitor
import com.toolskart.android.boardcastreceiver.MySMSBroadcastReceiver
import com.toolskart.android.model.CommonRes
import com.toolskart.android.model.Customer
import com.toolskart.android.model.CustomerRes
import com.toolskart.android.ui.dialog.BaseDialog
import com.toolskart.android.ui.login.LoginActivity
import com.toolskart.android.utils.AppSharedPreference
import com.toolskart.android.utils.AppSignatureHelper.TAG
import com.toolskart.android.utils.CommonUtils
import com.toolskart.android.utils.MyLogUtils
import com.toolskart.android.utils.Validation
import kotlinx.android.synthetic.main.fragment_verify_ot.*

class VerifyOTP : BaseDialog(), View.OnClickListener, MySMSBroadcastReceiver.OTPReceiveListener, OtpVerificationContract.View {

    lateinit var screenView: View
    private lateinit var smsReceiver: MySMSBroadcastReceiver
    private var intentFilter: IntentFilter? = null
    lateinit var presenter: OtpVerificationPresenter
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.customDialogFragment)
        init()
    }

    override fun init() {
        presenter = OtpVerificationPresenter(this)
        //setupOtpReader()
    }

    private fun setupOtpReader() {
        smsReceiver = MySMSBroadcastReceiver()
        val client = SmsRetriever.getClient(this.context!!)
        val task = client.startSmsRetriever()
        smsReceiver.initOTPListener(this)
        intentFilter = IntentFilter()
        intentFilter!!.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
        activity!!.registerReceiver(smsReceiver, intentFilter)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn_confirm -> {
                    if (pinview.value.toString().isNotEmpty())
                        verifyOtp()
                    else
                        Toast.makeText(context, getString(R.string.err_please_enter_otp), Toast.LENGTH_SHORT).show()
                }
                R.id.tv_resend -> {
                    resendOtp()
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        screenView = inflater.inflate(R.layout.fragment_verify_ot, container, false)
        mContext = this.activity!!
        return screenView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_confirm.setOnClickListener(this)
        tv_resend.setOnClickListener(this)
    }

    override fun onOTPReceived(otp: String) {
        if (otp.isNotEmpty()) {
            Log.i("Anything", "otp is $otp")
            dialog.pinview.value = otp
            verifyOtp()
        }
    }

    override fun verifyOtp() {
        if (LiveNetworkMonitor(this.context!!).isConnected()) {
            if (!pinview.value.toString().isBlank()) {
                showLoader()
                presenter.verifyOtp(otp = pinview.value.toString(), mobile = CommonUtils.getMobileNumber())
                AppSharedPreference.clearPreferences()
            } else
                showMsg(getString(R.string.err_please_enter_otp))
        } else
            showMsg(getString(R.string.error_no_internet))
    }

    override fun setOtpRes(res: CustomerRes) {
        if (Validation.isValidStatus(res)) {
            if (res.result.isNotEmpty()) {
                hideLoader()
                showMsg(res.message)
                setupData(res.result[0])

                /** Setting session to shared preferences*/
                CommonUtils.setSession(if (res._session!!.isEmpty()) CommonUtils.getSession() else res._session!!)
            }
        } else {
            showMsg(res.message ?: getString(R.string.woops_something_went_wrong))
        }
    }

    private fun setupData(customer: Customer) {
        CommonUtils.setCustomerData(customer)
        CommonUtils.setIsLoggedIn(true)

        if (CommonUtils.isUserLogin()) {
            CommonUtils.goToDashBoard(this.mContext)
        } else
            startActivity(Intent(context, LoginActivity::class.java))
    }

    private fun resendOtp() {
        if (LiveNetworkMonitor(this.context!!).isConnected()) {
            showLoader()
            presenter.resendOtp(CommonUtils.getMobileNum())
        } else
            showMsg(getString(R.string.error_no_internet))
    }

    override fun showResendOtpRes(res: CommonRes) {
        if (Validation.isValidStatus(res)) {
            showMsg(getString(R.string.msg_otp_resend_success))
        }
    }

    override fun showMsg(msg: String?) {

    }

    override fun hideLoader() {
        CommonUtils.hideLoading()
    }

    override fun showViewState(state: Int) {

    }

    override fun showLoader() {
        CommonUtils.showLoading(this.context!!, "", true)
    }

    override fun onOTPTimeOut() {
        MyLogUtils.i(TAG, getString(R.string.error_something_wrong))
    }
}