package com.toolskart.android.ui.review

import com.toolskart.android.R
import com.toolskart.android.api.ApiResponsePresenter
import com.toolskart.android.api.ApiType
import com.toolskart.android.api.IResponseInterface
import com.toolskart.android.model.CommonRes
import com.toolskart.android.model.inputmodel.RatingFeedbackInput
import com.toolskart.android.utils.MyApplication
import com.toolskart.android.utils.Validation
import retrofit2.Call
import retrofit2.Response

class ReviewPresenter(view: ReviewContract.view) : ReviewContract.presenter, IResponseInterface {

    private var iResponseInterface: ApiResponsePresenter = ApiResponsePresenter(this)
    var view: ReviewContract.view? = view

    override fun validate(input: RatingFeedbackInput): Boolean {
        if (input.review.isNullOrBlank()) {
            view?.showInValidFiledMsg(view?.getContext()?.getString(R.string.err_please_enter_valid_message)!!)
            return false
        }
        return true
    }

    override fun callReviewApi(input: RatingFeedbackInput) {
        iResponseInterface.callApi(MyApplication.service.provideReview(input), ApiType.REVIEW)
    }

    override fun <T> onResponseSuccess(call: Call<T>, response: Response<T>, reqType: String) {
        view?.hideLoader()
        if (response.isSuccessful && Validation.isValidObject(response)) {
            when (reqType) {
                ApiType.REVIEW ->
                    view?.setcallReviewRes(response.body() as CommonRes)
            }
        }
    }

    override fun <T> onResponseFailure(call: Call<T>, responseError: Throwable, reqType: String) {
        view?.hideLoader()
        when (reqType) {
            ApiType.REVIEW ->
                view?.showMsg(responseError.message)
        }
    }
}