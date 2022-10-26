package com.bsstandard.piece.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.bsstandard.piece.data.datamodel.dmodel.occupation.OccupationVerifyModel;
import com.bsstandard.piece.data.dto.OccupationDTO;
import com.bsstandard.piece.data.repository.OccupationRepository;
import com.bsstandard.piece.widget.utils.LogUtil;
import com.bsstandard.piece.widget.utils.SingleLiveEvent;

/**
 * packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : OccupationViewModel
 * author         : piecejhm
 * date           : 2022/10/06
 * description    : 휴대폰 점유 인증 검증 요청 ViewModel
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/06        piecejhm       최초 생성
 */


public class OccupationViewModel extends AndroidViewModel {
    @SuppressWarnings({"FieldCanBeLocal"})
    public SingleLiveEvent<OccupationDTO> data = null;
    public final OccupationRepository occupationRepository;

    public OccupationViewModel(@NonNull Application application) {
        super(application);
        occupationRepository = new OccupationRepository(application);
    }

    public SingleLiveEvent<OccupationDTO> postVerifyData(String accessToken, String deviceId, String memberId,OccupationVerifyModel occupationVerifyModel) {
        if(data == null) {
            LogUtil.logE("data null..");
            data = new SingleLiveEvent<>();
            data = loadVerifyData(accessToken,deviceId,memberId,occupationVerifyModel);
        } else {
            LogUtil.logE("data not null..");
            data = loadVerifyData(accessToken,deviceId,memberId,occupationVerifyModel);
        }
        return data;
    }

    public SingleLiveEvent<OccupationDTO> loadVerifyData(String accessToken, String deviceId, String memberId,OccupationVerifyModel occupationVerifyModel) {
        return occupationRepository.getVerifyData(accessToken, deviceId,memberId,occupationVerifyModel);
    }


}
