package com.fastchar.wallettest.adapter

import com.blankj.utilcode.util.ActivityUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.fastchar.wallettest.R
import com.fastchar.wallettest.data.UserWalletBalanceEntity

class UserWalletBalanceAdapter :
    BaseQuickAdapter<UserWalletBalanceEntity, BaseViewHolder>(R.layout.ry_user_wallet_balance_item_layout) {
    override fun convert(holder: BaseViewHolder, item: UserWalletBalanceEntity) {
        holder.setText(R.id.tv_coin_name, item.name)
            .setText(R.id.tv_flat_balance,"$ ${item.fiatBalance}")
            .setText(R.id.tv_balance,"${item.balance} ${item.shortName}")
        ActivityUtils.getTopActivity()?.let {
            Glide.with(it)
                .load(item.iconUrl)
                .into(holder.getView(R.id.iv_coin_icon))
        }
    }
}