package com.bsstandard.piece.view.portfolioDetail

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsstandard.piece.data.dto.PortfolioDetailDTO
import com.bsstandard.piece.data.repository.PortfolioDetailRepository
import com.bsstandard.piece.view.adapter.portfolio.PortfolioDetailCompositionAdapter
import com.bsstandard.piece.view.adapter.portfolio.PortfolioDetailEvidenceAdapter
import com.bsstandard.piece.view.adapter.portfolio.PortfolioDetailProductsAdapter
import com.bsstandard.piece.view.adapter.portfolio.PortfolioDetailPurchaseAdapter
import com.bsstandard.piece.widget.utils.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch


/**
 *packageName    : com.bsstandard.piece.view.portfolioDetail
 * fileName       : PortfolioDetailViewModel
 * author         : piecejhm
 * date           : 2022/08/12
 * description    : 포트폴리오 상세 ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/08/12        piecejhm       최초 생성
 */

@HiltViewModel
class PortfolioDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: PortfolioDetailRepository = PortfolioDetailRepository(application)

    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    private var portfolioDetailPurchaseAdapter =
        PortfolioDetailPurchaseAdapter(this, context) // 포트폴리오 구매포인트 Adapter - jhm 2022/08/22
    private var portfolioDetailProductAdapter =
        PortfolioDetailProductsAdapter(this, context) // 포트폴리오 상세 정보 구성 Adapter - jhm 2022/08/22
    private var portfolioDetailCompositionAdapter =
        PortfolioDetailCompositionAdapter(this, context) // 포트폴리오 하단 구성 Adater - jhm 2022/08/22
    private var portfolioDetailEvidenceAdapter =
        PortfolioDetailEvidenceAdapter(this, context) // 포트폴리오 증빙 구성 - jhm 2022/08/25

    // 구매 포인트 ArrayList - jhm 2022/08/18
    private val purchaseGuidesList: ArrayList<PortfolioDetailDTO.Data.PurchaseGuide> = arrayListOf()

    // 포트폴리오 정보 구성 ArrayList - jhm 2022/08/22
    private val productItemList: ArrayList<PortfolioDetailDTO.Data.Product> = arrayListOf()

    // 포트폴리오 하단 구성 layout ArrayList - jhm 2022/08/22
    private val compositionList: ArrayList<PortfolioDetailDTO.Data.Product> = arrayListOf()

    // 증빙 구성 layout ArrayList - jhm 2022/10/12
    private val documentList: ArrayList<PortfolioDetailDTO.Data.Product.ProductDocument> =
        arrayListOf()

    // 포트폴리오 증빙 구성 ArrayList - jhm 2022/08/25
    private val evidenceList: ArrayList<PortfolioDetailDTO.Data.Product.ProductDocument> =
        arrayListOf()


    var n = 1
    val detailResponse: MutableLiveData<PortfolioDetailDTO> = MutableLiveData()
//  get() = portfolioDetailRepository.detailResponse

    @SuppressLint("NotifyDataSetChanged")
    fun getPortfolioDetail(portfolioId: String) {
        viewModelScope.launch {
            val response = repo.getPortfolioDetails(portfolioId = portfolioId)
            try {
                if (response.status.equals("OK")) {
                    detailResponse.value = response

                    purchaseGuidesList.clear() // 포트폴리오 구매포인트 ArrayList 초기화 - jhm 2022/08/22
                    productItemList.clear() // 포트폴리오 정보 구성 ArrayList 초기화 - jhm 2022/08/22
                    compositionList.clear() // 포트폴리오 하단 구성 전체 ArrayList 초기화 - jhm 2022/08/22
                    evidenceList.clear() // 포트폴리오 증빙 구성 ArrayList 초기화 - jhm 2022/08/25
                    documentList.clear() // 포트폴리오 증빙구성 증빙자료 ArrayList 초기화 - jhm 2022/10/12

                    // 포트폴리오 구매포인트 - jhm 2022/08/25
                    for (i in ArrayList(detailResponse.value!!.data.purchaseGuides).indices) {
                        purchaseGuidesList.add(detailResponse.value!!.data.purchaseGuides[i])
                        portfolioDetailPurchaseAdapter.notifyDataSetChanged()
                    }

                    // 포트폴리오 구성 - jhm 2022/08/25
                    for (i in ArrayList(detailResponse.value!!.data.products).indices) {
                        productItemList.add(detailResponse.value!!.data.products[i])
                        compositionList.add(detailResponse.value!!.data.products[i])


                        portfolioDetailProductAdapter.notifyDataSetChanged()

                    }

                    for(i in 0 until detailResponse.value!!.data.products.size) {
                        if(detailResponse.value?.data?.products?.isNotEmpty() == true){
                            // 포트폴리오 하단 구성 - jhm 2022/08/25
                            evidenceList.add(detailResponse.value!!.data.products[i].productDocuments[i])
                            portfolioDetailEvidenceAdapter.notifyDataSetChanged()

                            for(i in 0 until detailResponse.value!!.data.products[i].productDocuments.size) {
                                // 포트폴리오 증빙 자료 - jhm 2022/10/13
                                documentList.add(detailResponse.value!!.data.products[i]!!.productDocuments[i])
                                portfolioDetailCompositionAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                } else {
                    // Room DB Select 조회 후 보여주기 로직 추가해야함 - jhm 2022/10/13
                    LogUtil.logE("포트폴리오 상세 try Error !")
                }
            } catch (ex: Exception) {
                LogUtil.logE("포트폴리오 상세 catch Error ! ")
                ex.printStackTrace()
            }
        }
    }

    // Recycler Adapter 수평 - jhm 2022/08/22
    fun viewInitHorizontal(recyclerView: RecyclerView, viewType: Int) {
        when (viewType) {
            // viewType - jhm 2022/08/22
            // 1 - 구매포인트 - jhm 2022/08/22
            // 2 - 포트폴리오 구성 - jhm 2022/08/22
            1 -> {
                recyclerView.adapter = portfolioDetailPurchaseAdapter
                recyclerView.layoutManager =
                    LinearLayoutManager(getApplication(), RecyclerView.HORIZONTAL, false)
            }
            2 -> {
                recyclerView.adapter = portfolioDetailCompositionAdapter
                recyclerView.layoutManager =
                    LinearLayoutManager(getApplication(), RecyclerView.HORIZONTAL, false)

            }
        }
    }

    // Recycler Adapter 수직 - jhm 2022/08/22
    fun viewInitVertical(recyclerView: RecyclerView) {
        recyclerView.adapter = portfolioDetailProductAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(getApplication(), RecyclerView.VERTICAL, false)
    }


    // 포트폴리오 구매포인트 리스트 - jhm 2022/08/18
    fun getPortfolioPurchasGuideList(): List<PortfolioDetailDTO.Data.PurchaseGuide> {
        return purchaseGuidesList;
    }

    // 포트폴리오 상세 정보 구성 리스트  - jhm 2022/08/22
    fun getPortfolioProductList(): List<PortfolioDetailDTO.Data.Product> {
        return productItemList;
    }

    // 포트폴리오 하단 구성 레이아웃 전체 리스트  - jhm 2022/08/22
    fun getPortfolioCompositionList(): List<PortfolioDetailDTO.Data.Product> {
        return compositionList;
    }

    fun getProductDocumentList(): List<PortfolioDetailDTO.Data.Product.ProductDocument> {
        return documentList;
    }

    // 포트폴리오 하단 구성품 리스트 - jhm 2022/08/25
    fun getPortfolioDocumentsList(): List<PortfolioDetailDTO.Data.Product.ProductDocument> {
        return evidenceList;
    }


}
