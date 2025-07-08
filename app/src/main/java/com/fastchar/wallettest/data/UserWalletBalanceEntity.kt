package com.fastchar.wallettest.data

import java.math.BigDecimal

data class UserWalletBalanceEntity(
    val currency: String,             // 币种
    val name: String,                 // 币种全称
    val iconUrl: String,              // 币种图标
    val balance: String,              // 币种余额
    var fiatBalance: String  ,         // 法币余额
    val shortName: String ,          // 法币余额
    var originFiatBalance: BigDecimal = BigDecimal.ZERO
)