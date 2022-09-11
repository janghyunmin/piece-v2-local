//package com.bsstandard.piece.data.viewmodel;
//
//import android.app.Application;
//
//import androidx.annotation.NonNull;
//import androidx.lifecycle.AndroidViewModel;
//import androidx.lifecycle.MutableLiveData;
//
//import com.bsstandard.piece.data.dto.MemberDTO;
//import com.bsstandard.piece.data.repository.GetMemberRepository;
//
///**
// * packageName    : com.bsstandard.piece.data.viewmodel
// * fileName       : GetMemberViewModel
// * author         : piecejhm
// * date           : 2022/09/03
// * description    :
// * ===========================================================
// * DATE              AUTHOR             NOTE
// * -----------------------------------------------------------
// * 2022/09/03        piecejhm       최초 생성
// */
//public class GetMemberViewModel extends AndroidViewModel {
//    @SuppressWarnings({"FieldCanBeLocal"})
//    public MutableLiveData<MemberDTO> getMemberData = null;
//    public final GetMemberRepository getMemberRepository;
//
//    public GetMemberViewModel(@NonNull Application application) {
//        super(application);
//        getMemberRepository = new GetMemberRepository(application);
//    }
//
//    public MutableLiveData<MemberDTO> getMemberData() {
//        if(getMemberData == null) {
//            getMemberData = new MutableLiveData<>();
//            getMemberData = loadMemberData();
//        } else {
//            getMemberData = loadMemberData();
//        }
//        return getMemberData;
//    }
//    public MutableLiveData<MemberDTO> loadMemberData() {
//        return getMemberRepository.getMemberData();
//    }
//}
