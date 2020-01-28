package com.toolskart.android.ui.commonadapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.toolskart.android.R
import com.toolskart.android.api.LiveNetworkMonitor
import com.toolskart.android.constants.Constants
import com.toolskart.android.interfaces.IAdapterClickListener
import com.toolskart.android.model.Product
import com.toolskart.android.model.Sku
import com.toolskart.android.model.inputmodel.ModifyCartIp
import com.toolskart.android.utils.*
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.partial_cart_list_item.*
import kotlinx.android.synthetic.main.partial_cart_list_item.view.*
import kotlinx.android.synthetic.main.product_quantity_add_control_layout.*
import kotlinx.android.synthetic.main.quantity_inc_dec_layout.*

class WishListAdapter(var context: Context, var adapterType: String = "common",
                      var adapterClickListener: IAdapterClickListener? = null) : RecyclerView.Adapter<WishListAdapter.ViewHolder>() {
    internal var list = ArrayList<Product>()
    var modifProd: Product? = null

    fun addList(list: ArrayList<Product>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.partial_cart_list_item)
        return ViewHolder(view, adapterClickListener, adapterType)
    }

    override fun getItemCount(): Int {
        return if (list != null && list.size > 0) list.size else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (Validation.isValidObject(list))
            holder.bind(context, list[position], position)
    }

    inner class ViewHolder(override val containerView: View, var adapterClickListener: IAdapterClickListener?,
                           var adapterType: String = "common") : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(context: Context, item: Any, pos: Int) {
            if ((item is Product) && Validation.isValidObject(item) && item.sku!!.isNotEmpty()) {

                if (adapterType == Constants.TYPE_CART_ADAPTER) {
                    btn_return.text = context.getString(R.string.add_to_wishList)
                    btn_remove.text = context.getString(R.string.tx_delete)
                } else {
                    btn_return.text = context.getString(R.string.go_to_cart)
                    btn_remove.text = context.getString(R.string.tx_remove)
                }

                /*only for cart*/
                if (!item.image.isNullOrEmpty()) {
                    ImageLoader.setImage(itemView.pro_image, item.image!!)
                }

                /*only for wishList*/
                if (item.pic != null) {
                    item.pic.apply {
                        if (item.pic.isNotEmpty()) {
                            if (!item.pic[0].pic!!.isBlank() && item.pic.isNotEmpty()) {
                                ImageLoader.setImage(itemView.pro_image, item.pic[0].pic!!)
                            } else {
                                ImageLoader.setImage(itemView.pro_image, "")
                            }
                        }
                    }
                }
                if (!item.name.isNullOrEmpty()) {
                    pro_name.text = item.name!!
                }

                if (item.sku!!.isNotEmpty()) {
                    item.selSku = item.getSelectedSku()
                    // if (item.sku!![0].mrp!!.isNotEmpty()) prod_price.text = item.sku!![0].mrp
                    prod_price.text = CommonUtils.getRupeesSymbol(context, item.sku!![0]!!.mrp.toString())
                }
                updateButtonText(item.wishlist, btn_return)

                if (adapterType == Constants.TYPE_CART_ADAPTER) {
                    if (item.sku != null)
                        prod_price.text = CommonUtils.getRupeesSymbol(context, item.sku!![0]!!.mrp.toString())
                    else
                        prod_price.visibility = View.GONE

                    /* prod_price.apply {
                         visibility = if (item.sku!![0].mrp!!.toInt() > 0) View.VISIBLE else View.GONE
                         text = item.selling_price!!.toString()
                     }*/

                    if (item.sku!![0].stock!!.toInt() <= 0)
                        no_of_stock.visibility = View.GONE
                    else
                        no_of_stock.visibility = View.VISIBLE
                }

                if (adapterType == Constants.TYPE_WISH_LIST_ADAPTER) {
                      if (item.sku!!.isNotEmpty()) {
                          wish_list_item_price.text = CommonUtils.getRupeesSymbol(context, item.sku!![0]!!.mrp.toString())
                      }

                    updateSelectedSkuPrices(item)

                }
                updateSelectedSkuPrices(item)

                tv_increment.setOnClickListener {
                    modifyCart(it, item)
                }
                tv_decrement.setOnClickListener {
                    modifyCart(it, item)
                }

                tv_decrement_from_wishList.setOnClickListener {
                    modifyCart(it, item)
                }

                tv_increment_from_wishList.setOnClickListener {
                    modifyCart(it, item)
                }

                btn_return.setOnClickListener {
                    if (adapterType == Constants.TYPE_CART_ADAPTER) {
                        modifyWishList(item)
                    } else {
                        modifyCart(it, item)
                    }
                }

                btn_remove.setOnClickListener {
                    if (adapterType == Constants.TYPE_WISH_LIST_ADAPTER) {
                        modifyWishList(item)
                    } else {
                        removeFromCart(item)
                    }
                }
            } else {
                no_of_products.visibility = View.GONE
                btn_return.text = context.getString(R.string.go_to_cart)
                btn_remove.text = context.getString(R.string.tx_remove)
                if (!(item as Product).name.isNullOrEmpty()) {
                    pro_name.text = item.name!!
                }
                if (item.pic != null) {
                    item.pic.apply {
                        if (item.pic.isNotEmpty()) {
                            if (!item.pic[0].pic!!.isBlank() && item.pic.isNotEmpty()) {
                                ImageLoader.setImage(itemView.pro_image, item.pic[0].pic!!)
                            } else {
                                ImageLoader.setImage(itemView.pro_image, "")
                            }
                        }
                    }
                }

                btn_return.setOnClickListener {
                    CommonUtils.showCustomToast(MyApplication.context, MyApplication.context.getString(R.string.cannot_add_items_out_of_stock), Toast.LENGTH_SHORT)
                }
                btn_remove.setOnClickListener {
                    if (adapterType == Constants.TYPE_WISH_LIST_ADAPTER) {
                        modifyWishList(item)
                    } else {
                        removeFromCart(item)
                    }
                }
            }
            buttonTextsetting()
        }

        fun buttonTextsetting() {
            if (adapterType == Constants.TYPE_WISH_LIST_ADAPTER) {
                no_of_products.visibility = View.GONE
                wishlist_layout.visibility = View.VISIBLE
                no_of_stock.visibility = View.GONE
                wishList_date.visibility = View.VISIBLE
            } else {
                no_of_products.visibility = View.VISIBLE
                wishlist_layout.visibility = View.GONE
                no_of_stock.visibility = View.VISIBLE
                wishList_date.visibility = View.GONE
            }
        }

        private fun updateSelectedSkuPrices(item: Product) {
            val selSku = item.getSelectedSku()
            selSku.apply {
                val sellingPrice = selling_price!!.toFloat()
                val mrp = mrp!!.toFloat()
                tempMyCart = -1
                setCartCount(this@ViewHolder, this)
            }
        }

        private fun setCartCount(viewHolder: WishListAdapter.ViewHolder, sku: Sku) {
            viewHolder.apply {
                if (adapterType == Constants.TYPE_WISH_LIST_ADAPTER)
                    qty.text = if (sku.tempMyCart != -1) sku.tempMyCart.toString() else sku.mycart.toString()
                else
                    tv_sku_count.text = if (sku.tempMyCart != -1) sku.tempMyCart.toString() else sku.mycart.toString()
            }
        }

        private fun modifyWishList(item: Product) {
            if (LiveNetworkMonitor(context).isConnected()) {
                item.tempWishlist = if (item.wishlist == 1) 0 else 1
                updateButtonText(item.wishlist, btn_return)
                modifProd = item
                item.prodPos = adapterPosition
                adapterClickListener?.onclick(data = item, pos = adapterPosition, op = Constants.WISHLIST)
            } else
                Toast.makeText(context, R.string.error_no_internet, Toast.LENGTH_SHORT).show()
        }

        private fun updateButtonText(item: Int?, wish_list_button_text: TextView?) {
            if (item == 1) {
                if (adapterType == Constants.TYPE_CART_ADAPTER) {
                    btn_return.visibility = View.INVISIBLE
                } else {
                    wish_list_button_text?.text = context.getString(R.string.add_to_cart)
                }
            } else
                wish_list_button_text?.text = context.getString(R.string.tx_move_to_wish_list)
        }

        private fun modifyCart(view: View, item: Product) {
            if (!(Validation.isValidObject(item.getSelectedSku())))
                return
            when (view.id) {
                R.id.tv_increment, R.id.btn_return, R.id.tv_increment_from_wishList -> {
                    item.getSelectedSku().apply {
                        if (item.getSelectedSku().stock!!.toFloat() <= item.getSelectedSku().mycart!!.toFloat()) {
                            CommonUtils.showCustomToast(MyApplication.context, MyApplication.context.getString(R.string.cannot_add_items_out_of_stock), Toast.LENGTH_SHORT)
                            return
                        }
                    }
                    if (item.getSelectedSku().mycart!!.toInt() < item.getSelectedSku().min_quantity!!.toInt()) {
                        if (item.getSelectedSku().min_quantity!!.toInt() > 1)
                            CommonUtils.showCustomToast(MyApplication.context, MyApplication.context.getString(R.string.should_be_minimum_quantity) + "${item.getSelectedSku().min_quantity}", Toast.LENGTH_SHORT)
                        item.getSelectedSku().tempMyCart = item.getSelectedSku().mycart!!.toInt() + item.getSelectedSku().min_quantity!!.toInt()
                    } else
                        item.getSelectedSku().tempMyCart = item.getSelectedSku().mycart!!.toInt() + 1

                    item.getSelectedSku().apply {
                        item.modifyCartIp = ModifyCartIp(_id = CommonUtils.getCustomerId(), _session = CommonUtils.getSession(),
                                op = Constants.MODIFY, id_sku = item.selSku!!._id!!, id_product = item._id!!, quantity = tempMyCart.toString(), wh_pincode = " ", lang = "en")
                        modifProd = item
                        item.prodPos = adapterPosition
                        adapterClickListener!!.onclick(data = item, op = Constants.OP_MODIFY_CART, pos = adapterPosition)
                    }
                }
                R.id.tv_decrement, R.id.tv_decrement_from_wishList -> {
                    item.selSku?.apply {
                        if (item.getSelectedSku().mycart!!.toInt() <= item.getSelectedSku().min_quantity!!.toInt()) {
                            if (item.getSelectedSku().min_quantity!!.toInt() > 1) {
                                CommonUtils.showCustomToast(context, context.getString(R.string.should_be_minimum_quantity) + "${item.getSelectedSku().min_quantity}", Toast.LENGTH_SHORT)
                                this.tempMyCart = 0
                            }
                        } else
                            this.tempMyCart = mycart!!.toInt() - 1

                        item.modifyCartIp = ModifyCartIp(_id = CommonUtils.getCustomerId(), _session = CommonUtils.getSession(), op = Constants.MODIFY,
                                id_sku = item.selSku!!._id!!, id_product = item._id!!, quantity = tempMyCart.toString())
                        modifProd = item
                        item.prodPos = adapterPosition
                        if (this.mycart == this.min_quantity!!) {
                            adapterClickListener?.onclick(data = item, pos = adapterPosition, op = Constants.OP_REMOVE_CART)
                        } else
                            adapterClickListener?.onclick(data = item, op = Constants.OP_MODIFY_CART, pos = adapterPosition)
                    }
                }
            }
        }

        fun removeFromCart(item: Product) {
            item.selSku?.apply {
                if (LiveNetworkMonitor(context).isConnected()) {
                    modifProd = item
                    item.modifyCartIp = ModifyCartIp(
                            _id = CommonUtils.getCustomerId(),
                            _session = CommonUtils.getSession(),
                            op = Constants.MODIFY,
                            id_sku = item.selSku!!._id!!,
                            id_product = item._id!!,
                            quantity = tempMyCart.toString(),
                            lang = "en", wh_pincode = "")
                    item.prodPos = adapterPosition
                    adapterClickListener?.onclick(data = item, pos = adapterPosition, op = Constants.OP_REMOVE_CART)
                } else
                    Toast.makeText(context, R.string.error_no_internet, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun showModifiedRes(type: String) {
        modifProd?.apply {
            when (type) {
                Constants.RES_SUCCESS -> {
                    selSku!!.mycart = getSelectedSku().tempMyCart.toString()
                    selSku!!.tempMyCart = -1
                    notifyItemChanged(prodPos)
                }
                Constants.RES_FAILED -> {
                    selSku!!.tempMyCart = -1
                }
            }
        }
    }

    fun showModifiedWishListRes(type: String) {
        modifProd?.apply {
            when (type) {
                Constants.RES_SUCCESS -> {
                    wishlist = tempWishlist
                    notifyItemChanged(prodPos)
                    tempWishlist = -1
                }

                Constants.RES_FAILED -> {
                    tempWishlist = -1
                    notifyItemChanged(prodPos)
                }
            }
        }
    }
}