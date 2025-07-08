package com.fastchar.wallettest.pojo

//币种实体类
data class CurrencyResponse(
    val currencies: List<Currency>,
    val total: Int,
    val ok: Boolean
)


data class Currency(
    val coin_id: String,
    val name: String,
    val symbol: String,
    val token_decimal: Int,
    val contract_address: String,
    val withdrawal_eta: List<String>,
    val colorful_image_url: String,
    val gray_image_url: String,
    val has_deposit_address_tag: Boolean,
    val min_balance: Int,
    val blockchain_symbol: String,
    val trading_symbol: String,
    val code: String,
    val explorer: String,
    val is_erc20: Boolean,
    val gas_limit: Int,
    val token_decimal_value: String,
    val display_decimal: Int,
    val supports_legacy_address: Boolean,
    val deposit_address_tag_name: String,
    val deposit_address_tag_type: String,
    val num_confirmation_required: Int
)

//汇率实体类
data class LiveRateResponse(
    val ok: Boolean,
    val warning: String,
    val tiers: List<RateTier>
)

data class RateTier(
    val from_currency: String,
    val to_currency: String,
    val rates: List<RateDetail>,
    val time_stamp: Long
)

data class RateDetail(
    val amount: String,
    val rate: String
)

//余额实体类
data class BalanceResponse(
    val ok: Boolean,
    val warning: String,
    val wallet: List<WalletBalance>
)

data class WalletBalance(
    val currency: String,
    val amount: Double
)