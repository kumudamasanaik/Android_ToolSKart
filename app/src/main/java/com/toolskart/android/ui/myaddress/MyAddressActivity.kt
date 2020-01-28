package com.toolskart.android.ui.myaddress

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import com.toolskart.android.R
import com.toolskart.android.api.LiveNetworkMonitor
import com.toolskart.android.constants.Constants
import com.toolskart.android.customview.ScreenStateView
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.model.*
import com.toolskart.android.model.inputmodel.AddressInput
import com.toolskart.android.ui.base.BaseActivity
import com.toolskart.android.ui.base.adapter.BaseRecAdapter
import com.toolskart.android.utils.CommonUtils
import com.toolskart.android.utils.Validation
import com.toolskart.android.utils.showToastMsg
import kotlinx.android.synthetic.main.activity_my_address.*
import kotlinx.android.synthetic.main.add_address_child_item.*
import kotlinx.android.synthetic.main.empty_view.*
import kotlinx.android.synthetic.main.error_view.*

class MyAddressActivity : BaseActivity(), View.OnClickListener, MyAddressContract.View, IAdapterClickListener {
    private var mContext: Context? = null
    private lateinit var presenter: MyAddressPresenter
    private lateinit var snackbbar: View
    private val TAG = "MyAddressActivity"
    private lateinit var source: String
    private lateinit var addressAdapter: BaseRecAdapter
    private var addressList: GetAddressResp? = null
    private var deliveryAddressInput: AddressInput? = null
    private var address_id: String? = null
    private var edit: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutInflater.inflate(R.layout.activity_my_address, fragmentLayout)
        init()
    }

    override fun init() {
        mContext = this
        snackbbar = address_snackbar
        empty_button.setOnClickListener { selectDeliveryAddress() }
        error_button.setOnClickListener { selectDeliveryAddress() }
        presenter = MyAddressPresenter(this)
        tvTitle?.text = getString(R.string.my_address)
        hideCartView()
        add_address_layout.setOnClickListener(this)
        btn_save.setOnClickListener(this)
    }

    private fun setUpRecyclerView() {
        val linearLayout = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        addressAdapter = BaseRecAdapter(mContext!!, R.layout.partial_address_list_item, adapterClickListener = this, requestType = Constants.ADDRESS)
        rv_address.apply {
            layoutManager = linearLayout
            adapter = addressAdapter
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.add_address_layout -> {
                if (child_data.visibility == View.GONE && img_right_arrow.visibility == View.VISIBLE) {
                    img_right_arrow.setImageResource(R.drawable.ic_down_arrow)
                    child_data.visibility = View.VISIBLE
                    ev_name.text!!.clear()
                    ed_mobile.text!!.clear()
                    ed_address.text!!.clear()
                    ed_pincode.text!!.clear()
                } else {
                    img_right_arrow.setImageResource(R.drawable.ic_right_arrow)
                    child_data.visibility = View.GONE
                }
            }
            R.id.btn_save -> {
                if (edit == true)
                    callUpdateAddress()
                else
                    addDeliveryAddress()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        selectDeliveryAddress()
    }

    override fun addDeliveryAddress() {
        deliveryAddressInput = AddressInput(
                _id = CommonUtils.getCustomerID(),
                op = Constants.CREATE,
                selected = "1",
                name = ev_name.text.toString(),
                phone = ed_mobile.text.toString(),
                address1 = ed_address.text.toString(),
                pincode = ed_pincode.text.toString()
        )

        if (presenter.validate(deliveryAddressInput!!)) {
            if (LiveNetworkMonitor(this).isConnected()) {
                showViewState(ScreenStateView.VIEW_STATE_LOADING)
                presenter.callAddDeliveryAddressApi(deliveryAddressInput!!)
            } else {
                showMsg(getString(R.string.error_no_internet))
                showViewState(ScreenStateView.VIEW_STATE_ERROR)
            }
        }
    }

    override fun setAddDeliveryAddressResp(addAddressRes: AddAddressRes) {
        if (Validation.isValidStatus(addAddressRes)) {
            CommonUtils.showCustomToast(this, getString(R.string.address_added_successfully), 1)
            child_data.visibility = View.GONE
            selectDeliveryAddress()
        } else
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
    }

    override fun callUpdateAddress() {
        deliveryAddressInput = AddressInput(
                _id = CommonUtils.getCustomerID(),
                op = Constants.UPDATE,
                id_address = address_id,
                selected = "",
                name = ev_name.text.toString(),
                phone = ed_mobile.text.toString(),
                address1 = ed_address.text.toString(),
                pincode = ed_pincode.text.toString()
        )

        if (presenter.validate(deliveryAddressInput!!)) {
            if (LiveNetworkMonitor(this).isConnected()) {
                showViewState(ScreenStateView.VIEW_STATE_LOADING)
                presenter.callUpdateAddressApi(deliveryAddressInput!!)
            } else {
                showMsg(getString(R.string.error_no_internet))
                showViewState(ScreenStateView.VIEW_STATE_ERROR)
            }
        }
    }

    override fun setUpdateDeliveryAddressResp(updateAddressResp: UpdateAddress) {
        if (Validation.isValidObject(updateAddressResp)) {
            CommonUtils.showCustomToast(this, getString(R.string.address_updated_successfully), 1)
            selectDeliveryAddress()
            img_right_arrow.setImageResource(R.drawable.ic_right_arrow)
            child_data.visibility = View.GONE
        } else
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
    }

    override fun selectDeliveryAddress() {
        if (LiveNetworkMonitor(this).isConnected()) {
            showViewState(ScreenStateView.VIEW_STATE_LOADING)
            presenter.callSelectDeliveryAddressApi(Constants.GET)
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setSelectDeliveryAddressResp(selectAddressResp: GetAddressResp) {
        if (Validation.isValidStatus(selectAddressResp)) {
            showViewState(ScreenStateView.VIEW_STATE_CONTENT)
            setUpRecyclerView()
            addressList = selectAddressResp
            if (addressList!!.result!!.isNotEmpty())
                setData()
            else
                child_data.visibility = View.VISIBLE
        } else
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
    }

    fun callDeleteAddress(data: MyAddress) {
        deliveryAddressInput = AddressInput(
                _id = CommonUtils.getCustomerID(),
                op = Constants.DELETE,
                id_address = data._id
        )
        if (LiveNetworkMonitor(this).isConnected()) {
            showViewState(ScreenStateView.VIEW_STATE_LOADING)
            presenter.callDeleteAddressApi(deliveryAddressInput!!)
        } else {
            showMsg(getString(R.string.error_no_internet))
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
        }
    }

    override fun setDeleteAddressResp(deleteAddressResp: DeleteAddressResp) {
        if (Validation.isValidStatus(deleteAddressResp)) {
            showViewState(ScreenStateView.VIEW_STATE_CONTENT)
            selectDeliveryAddress()
        } else
            showViewState(ScreenStateView.VIEW_STATE_ERROR)
    }

    private fun setData() {
        addressList!!.result?.apply {
            if (addressList!!.result!!.isNotEmpty()) {
                addressAdapter.addList(addressList!!.result!!)
            }
        }
    }

    override fun onclick(data: Any, pos: Int, type: String, op: String) {
        if (data is MyAddress) {
            when (type) {
                Constants.EDIT_ADDRESS -> {
                    edit = true
                    img_right_arrow.setImageResource(R.drawable.ic_down_arrow)
                    child_data.visibility = View.VISIBLE
                    setupUI(data)
                }
                Constants.DELETE_ADDRESS -> {
                    CommonUtils.showConfirmationDialogPopup(this, this, getString(R.string.do_u_want_to_delete), data, pos)
                }
                Constants.ORDER_CANCELLED_CONFIRMATION -> {
                    callDeleteAddress(data)
                }
            }
        }
    }

    private fun setupUI(data: MyAddress) {
        data.apply {
            ev_name.setText(name)
            ed_mobile.setText(phone)
            ed_address.setText(address1)
            ed_pincode.setText(pincode)
            address_id = _id
        }
    }

    override fun showDeliveryAddressValidateErrorMsg(msg: String) {
        when (msg) {
            "1" -> showMsg(getString(R.string.et_enter_your_name))
            "2" -> showMsg(getString(R.string.et_enter_your_mobile_number))
            "3" -> showMsg(getString(R.string.et_enter_your_address))
            "4" -> showMsg(getString(R.string.et_enter_your_pincode))
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
        if (address_multiStateView != null)
            address_multiStateView.viewState = state
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}