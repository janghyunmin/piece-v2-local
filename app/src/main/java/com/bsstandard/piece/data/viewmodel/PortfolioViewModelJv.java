//package com.bsstandard.piece.data.viewmodel;
//
//import android.app.Application;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.AndroidViewModel;
//import androidx.lifecycle.MutableLiveData;
//
//import com.bsstandard.piece.data.dto.portfolio.PortfolioDTO;
//
///**
// * packageName    : com.bsstandard.piece.data.viewmodel
// * fileName       : PortfolioViewModelJv
// * author         : piecejhm
// * date           : 2022/07/15
// * description    :
// * ===========================================================
// * DATE              AUTHOR             NOTE
// * -----------------------------------------------------------
// * 2022/07/15        piecejhm       최초 생성
// */
//public class PortfolioViewModelJv extends AndroidViewModel {
//    @SuppressWarnings({"FieldCanBeLocal"})
//    public MutableLiveData<PortfolioDTO> callPortfolioData = null;
//    public final PortfolioRepositoryJv portfolioRepository;
//
//    public PortfolioViewModelJv(@NonNull Application application){
//        super(application);
//        portfolioRepository = new PortfolioRepositoryJv();
//    }
//
//    public MutableLiveData<PortfolioDTO> getPortfolioData(){
//        callPortfolioData = loadPortfolioData();
//        return callPortfolioData;
//    }
//    public MutableLiveData<PortfolioDTO> loadPortfolioData(){
//        return portfolioRepository.getPortfolioData();
//    }
//
//
//}
