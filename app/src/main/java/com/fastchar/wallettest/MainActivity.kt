package com.fastchar.wallettest

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.fastchar.wallettest.databinding.ActivityMainLayoutBinding
import com.fastchar.wallettest.fragment.HomeDefiFragment
import com.fastchar.wallettest.fragment.HomeWalletFragment
import com.gyf.immersionbar.ImmersionBar

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainLayoutBinding

    
    private var currentFragment: Fragment? = null
    private var defiFragment: HomeDefiFragment? = null
    private var walletFragment: HomeWalletFragment? = null

    companion object{
        val TAG_DEFI="defi"
        val TAG_WALLET="wallet"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ImmersionBar.with(this).statusBarDarkFont(false).init()
        binding = ActivityMainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.llBottomDefi.setOnClickListener(this)
        binding.llBottomWallet.setOnClickListener(this)

        // 默认展示 Defi Fragment
        switchToFragment(TAG_WALLET)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.llBottomDefi -> switchToFragment(TAG_DEFI)
            binding.llBottomWallet -> switchToFragment(TAG_WALLET)
        }
    }

    private fun switchToFragment(tag: String) {
        val transaction = supportFragmentManager.beginTransaction()

        val targetFragment = when (tag) {
            TAG_DEFI -> {
                if (defiFragment == null) {
                    defiFragment = HomeDefiFragment()
                    transaction.add(R.id.fl_container, defiFragment!!, TAG_DEFI)
                }
                ImmersionBar.with(this).statusBarDarkFont(true).init()
                defiFragment
            }

            TAG_WALLET -> {
                if (walletFragment == null) {
                    walletFragment = HomeWalletFragment()
                    transaction.add(R.id.fl_container, walletFragment!!, TAG_WALLET)
                }
                ImmersionBar.with(this).statusBarDarkFont(false).init()
                walletFragment
            }

            else -> null
        }

        // 切换 Fragment 显示状态
        if (targetFragment != null && currentFragment != targetFragment) {
            currentFragment?.let { transaction.hide(it) }
            transaction.show(targetFragment)
            currentFragment = targetFragment
        }

        updateBottomUI(tag)
        transaction.commitAllowingStateLoss()
    }

    private fun updateBottomUI(selected: String) {
        val isDefiSelected = selected == TAG_DEFI
        binding.ivDefi.isSelected = isDefiSelected
        binding.tvBottomDefi.isSelected = isDefiSelected
        binding.ivWallet.isSelected = !isDefiSelected
        binding.tvBottomWallet.isSelected = !isDefiSelected
    }
}