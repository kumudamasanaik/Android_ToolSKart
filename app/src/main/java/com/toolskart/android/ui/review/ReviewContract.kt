package com.toolskart.android.ui.review

import android.content.Context
import com.toolskart.android.model.CommonRes
import com.toolskart.android.model.inputmodel.RatingFeedbackInput
import com.toolskart.android.ui.BasePresenter
import com.toolskart.android.ui.BaseView

interface ReviewContract {
    interface view : BaseView {
        fun callReview()
        fun showInValidFiledMsg(msg: String)
        fun setcallReviewRes(response: CommonRes)
        fun getContext(): Context

    }

    interface presenter : BasePresenter<view> {
        fun validate(input: RatingFeedbackInput): Boolean
        fun callReviewApi(input: RatingFeedbackInput)
    }
}