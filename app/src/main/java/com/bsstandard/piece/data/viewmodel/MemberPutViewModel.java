package com.bsstandard.piece.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.bsstandard.piece.data.datamodel.dmodel.member.MemberModifyModel;
import com.bsstandard.piece.data.dto.MemberPutDTO;
import com.bsstandard.piece.data.repository.MemberUpdateRepository;
import com.bsstandard.piece.widget.utils.LogUtil;
import com.bsstandard.piece.widget.utils.SingleLiveEvent;

/**
 * packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : MemberPutViewModel
 * author         : piecejhm
 * date           : 2022/09/05
 * description    : 회원 정보 수정 요청 ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/05        piecejhm       최초 생성
 */
public class MemberPutViewModel extends AndroidViewModel {
    @SuppressWarnings({"FieldCanBeLocal"})
    public SingleLiveEvent<MemberPutDTO> memberPutData = null;
    public final MemberUpdateRepository memberUpdateRepository;

    public MemberPutViewModel(@NonNull Application application) {
        super(application);
        memberUpdateRepository = new MemberUpdateRepository(application);
    }

    public SingleLiveEvent<MemberPutDTO> putCallMemberData(MemberModifyModel memberModifyModel) {
        if(memberPutData == null){
            LogUtil.logE("memberPutData null..");
            memberPutData = new SingleLiveEvent<>();
            memberPutData = loadMemberData(memberModifyModel);
        } else {
            LogUtil.logE("memberPutData not null..");
            memberPutData = loadMemberData(memberModifyModel);
        }
        return memberPutData;
    }

    public SingleLiveEvent<MemberPutDTO> loadMemberData(MemberModifyModel memberModifyModel) {
        return memberUpdateRepository.putMemberData(memberModifyModel);
    }
}
