package com.fastchar.wallettest.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.SpanUtils
import com.fastchar.wallettest.R
import com.fastchar.wallettest.adapter.UserWalletBalanceAdapter
import com.fastchar.wallettest.data.UserWalletBalanceEntity
import com.fastchar.wallettest.databinding.FragmentHomeWalletLayoutBinding
import com.fastchar.wallettest.pojo.*
import com.fastchar.wallettest.pojo.Currency
import com.fastchar.wallettest.util.AssetsJsonReader
import com.fastchar.wallettest.util.BigDecimalUtils
import com.google.gson.Gson
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

class HomeWalletFragment : Fragment() {

    private var mAdapter: UserWalletBalanceAdapter? = null
    private var _binding: FragmentHomeWalletLayoutBinding? = null
    private val binding get() = _binding!!

    private val updateIntervalMs = 2000L
    private val random = Random()
    private var shouldAnimate = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeWalletLayoutBinding.inflate(inflater, container, false)
        init()
        initData()
        return binding.root
    }

    private fun init() {
        mAdapter = UserWalletBalanceAdapter()
        binding.ryCryptosList.adapter = mAdapter
        initBalance("--")
    }

    private fun initBalance(balance: String) {
        val sp2px = ConvertUtils.sp2px(20f)
        SpanUtils.with(binding.tvBalance)
            .append("$ ")
            .setForegroundColor(resources.getColor(R.color.white_alpha_40))
            .setFontSize(sp2px)
            .setBold()
            .append(balance)
            .setForegroundColor(resources.getColor(R.color.white))
            .setFontSize(ConvertUtils.sp2px(26f))
            .setBold()
            .append(" USD")
            .setForegroundColor(resources.getColor(R.color.white_alpha_40))
            .setFontSize(sp2px)
            .setBold()
            .create()
    }

    private fun initData() {
        ActivityUtils.getTopActivity()?.let {
            val currenciesStr = AssetsJsonReader.readJson(it, "json/currencies.json")
            val walletBalanceStr = AssetsJsonReader.readJson(it, "json/wallet-balance.json")
            val liveRatesStr = AssetsJsonReader.readJson(it, "json/live-rates.json")

            if (ObjectUtils.isEmpty(currenciesStr) || ObjectUtils.isEmpty(walletBalanceStr) || ObjectUtils.isEmpty(liveRatesStr)) {
                return
            }

            val balanceResponse = Gson().fromJson(walletBalanceStr, BalanceResponse::class.java)
            val currencyResponse = Gson().fromJson(currenciesStr, CurrencyResponse::class.java)
            val rateResponse = Gson().fromJson(liveRatesStr, LiveRateResponse::class.java)

            val displayList = buildWalletDisplayList(
                balances = balanceResponse.wallet,
                currencies = currencyResponse.currencies,
                rateTiers = rateResponse.tiers
            )
            mAdapter?.setNewData(displayList.toMutableList())
            startDynamicFiatAnimation()
        }
    }

    private fun buildWalletDisplayList(
        balances: List<WalletBalance>,
        currencies: List<Currency>,
        rateTiers: List<RateTier>
    ): List<UserWalletBalanceEntity> {
        val currencyMap = currencies.associateBy { it.code.uppercase() }
        val fiatRatesMap = rateTiers.associateBy { it.from_currency.uppercase() }
        var totalBalance = BigDecimal.ZERO

        val resultList = balances.mapNotNull { balance ->
            val code = balance.currency.uppercase()
            val currencyInfo = currencyMap[code] ?: return@mapNotNull null
            val fiatRate = fiatRatesMap[code]?.rates?.firstOrNull()?.rate?.toDoubleOrNull() ?: 0.0
            val origin = BigDecimal(balance.amount * fiatRate)
            val fiatFormatted = BigDecimalUtils.formatDecimal(origin, 2, RoundingMode.UP)
            totalBalance = totalBalance.add(origin)

            UserWalletBalanceEntity(
                currency = code,
                name = currencyInfo.name,
                iconUrl = currencyInfo.colorful_image_url,
                balance = BigDecimalUtils.formatDecimal(
                    BigDecimal(balance.amount),
                    currencyInfo.display_decimal,
                    RoundingMode.DOWN
                ),
                fiatBalance = fiatFormatted,
                shortName = currencyInfo.symbol
            ).apply {
                originFiatBalance = origin
            }
        }

        initBalance(BigDecimalUtils.formatDecimal(totalBalance, 2, RoundingMode.HALF_UP))
        return resultList
    }

    private fun startDynamicFiatAnimation() {//仿ws推送
        binding.ryCryptosList.postDelayed(object : Runnable {
            override fun run() {
                if (!shouldAnimate) return

                var total = BigDecimal.ZERO
                mAdapter?.data?.forEach { item ->
                    val fluctuation = BigDecimal.valueOf((random.nextDouble() * 0.006) - 0.003)
                    val origin = item.originFiatBalance
                    val changed = origin.add(origin.multiply(fluctuation))
                    item.fiatBalance = BigDecimalUtils.formatDecimal(changed, 2, RoundingMode.HALF_UP)
                    total = total.add(changed)
                }
                initBalance(BigDecimalUtils.formatDecimal(total, 2, RoundingMode.HALF_UP))
                mAdapter?.notifyDataSetChanged()

                binding.ryCryptosList.postDelayed(this, updateIntervalMs)
            }
        }, updateIntervalMs)
    }

    override fun onDestroyView() {
        shouldAnimate = false
        _binding = null
        super.onDestroyView()
    }
}