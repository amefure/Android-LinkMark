package com.amefure.linkmark.View.Category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amefure.linkmark.Model.Database.Category
import com.amefure.linkmark.R
import com.amefure.linkmark.View.Category.RecycleViewSetting.CategoryAdapter
import com.amefure.linkmark.View.Category.RecycleViewSetting.CategoryItemTouchListener
import com.amefure.linkmark.View.Utility.OneTouchHelperCallback
import com.amefure.linkmark.View.Dialog.CustomNotifyDialogFragment
import com.amefure.linkmark.View.Locator.LocatorListFragment
import com.amefure.linkmark.View.Setting.SettingFragment
import com.amefure.linkmark.View.Utility.ClipOutlineProvider
import com.amefure.linkmark.ViewModel.CategoryViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class CategoryListFragment : Fragment() {

    private var mInterstitialAd:InterstitialAd? = null
    private var mInterstitialCount: Int = 0

    private val viewModel: CategoryViewModel by viewModels()

    // UI
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_category_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 広告の読み込み
        var adView: AdView = view.findViewById(R.id.adView)
        adView.loadAd(AdRequest.Builder().build())

        loadInterstitial()
        observeInterstitialCount()

        viewModel.fetchAllCategorys()

        // リサイクルビューの角丸
        recyclerView = view.findViewById(R.id.category_list)
        recyclerView.outlineProvider = ClipOutlineProvider()
        recyclerView.clipToOutline = true

        // ヘッダーセットアップ
        setUpHeaderAction(view)

        var addButton: FloatingActionButton = view.findViewById(R.id.add_button)

        addButton.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                add(R.id.main_frame, CategoryInputFragment())
                addToBackStack(null)
                commit()
            }
        }

        setUpRecyclerView()
    }


    /**
     * ヘッダーボタンセットアップ
     * [LeftButton]：非表示(GONE)
     * [RightButton]：設定画面遷移ボタン
     */
    private fun setUpHeaderAction(view: View) {
        val headerView: ConstraintLayout = view.findViewById(R.id.include_header)
        val leftButton: ImageButton = headerView.findViewById(R.id.left_button)
        leftButton.visibility = View.GONE

        val rightButton: ImageButton = headerView.findViewById(R.id.right_button)
        rightButton.setImageResource(R.drawable.button_settings)
        rightButton.setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                add(R.id.main_frame, SettingFragment())
                addToBackStack(null)
                commit()
            }
        }
    }

    /**
     * リサイクルビューセットアップ
     * [categoryList]を観測して変更があるたびに自動描画
     */
    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this.requireActivity())
        recyclerView.addItemDecoration(
            DividerItemDecoration(this.requireActivity(), DividerItemDecoration.VERTICAL)
        )

        viewModel.categoryWithLocators.observe(viewLifecycleOwner) {
            val adapter = CategoryAdapter(viewModel, it, this.requireContext())
            adapter.setOnTappedListner(
                object : CategoryAdapter.onTappedListner {
                    override fun onEditTapped(categoryId: Int) {
                        parentFragmentManager.beginTransaction().apply {
                            add(R.id.main_frame, CategoryInputFragment.newInstance(categoryId))
                            commit()
                        }
                    }

                    override fun onDeleteTapped(category: Category, completion: (result: Boolean) -> Unit) {
                        val dialog = CustomNotifyDialogFragment.newInstance(
                            title = getString(R.string.dialog_title_notice),
                            msg = getString(R.string.dialog_msg_delete_confirm, category.name),
                            showPositive = true,
                            showNegative = true
                        )
                        dialog.setOnTappedListner(
                            object : CustomNotifyDialogFragment.onTappedListner {
                                override fun onNegativeButtonTapped() { }

                                override fun onPositiveButtonTapped() {
                                    viewModel.deleteCategory(category)
                                    completion(true)
                                }
                            }
                        )
                        dialog.show(parentFragmentManager, "ValidateNameDialog")
                    }
                }
            )

            val itemTouchListener = CategoryItemTouchListener()
            itemTouchListener.setOnTappedListner(
                object : CategoryItemTouchListener.onTappedListner{
                    override fun onTapped(categoryId: Int, name: String, color: String) {

                        if (mInterstitialCount >= 5) {
                            mInterstitialCount = 0
                            viewModel.saveInterstitialCount(0)
                            if (mInterstitialAd != null) {
                                mInterstitialAd?.show(this@CategoryListFragment.requireActivity())
                            }
                        } else {
                            mInterstitialCount = mInterstitialCount + 1
                            viewModel.saveInterstitialCount(mInterstitialCount)
                        }
                        parentFragmentManager.beginTransaction().apply {
                            add(R.id.main_frame, LocatorListFragment.newInstance(categoryId, name, color))
                            addToBackStack(null)
                            commit()
                        }
                    }
                }
            )
            recyclerView.addOnItemTouchListener(itemTouchListener)

            OneTouchHelperCallback(recyclerView).build()

            recyclerView.adapter = adapter
        }
    }

    /**
     * インタースティシャルカウント観測
     */
    private fun observeInterstitialCount() {
        lifecycleScope.launch {
            viewModel.observeInterstitialCount().collect {
                mInterstitialCount = it ?: 0
            }
        }
    }

    /**
     * AdMobインタースティシャル読み込み
     */
    private fun loadInterstitial() {

        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this.requireContext(),"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })
    }
}
