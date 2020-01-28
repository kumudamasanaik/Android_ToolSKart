package com.toolskart.android.ui.myaddress

import com.toolskart.android.model.inputmodel.AddressInput
import com.toolskart.android.model.AddAddressRes
import com.toolskart.android.model.DeleteAddressResp
import com.toolskart.android.model.GetAddressResp
import com.toolskart.android.model.UpdateAddress
import com.toolskart.android.ui.BasePresenter
import com.toolskart.android.ui.BaseView

interface MyAddressContract {
    interface View : BaseView {
        fun selectDeliveryAddress()
        fun setSelectDeliveryAddressResp(selectAddressResp: GetAddressResp)
        fun callUpdateAddress()
        fun setUpdateDeliveryAddressResp(updateAddressResp: UpdateAddress)
        fun addDeliveryAddress()
        fun setDeleteAddressResp(deleteAddressResp: DeleteAddressResp)
        fun setAddDeliveryAddressResp(addAddressRes: AddAddressRes)
        fun showDeliveryAddressValidateErrorMsg(msg: String)

    }

    interface Presenter : BasePresenter<View> {
        fun validate(input: AddressInput): Boolean
        fun callSelectDeliveryAddressApi(op: String)
        fun callUpdateAddressApi(input: AddressInput)
        fun callDeleteAddressApi(input: AddressInput)
        fun callAddDeliveryAddressApi(input: AddressInput)
    }
}