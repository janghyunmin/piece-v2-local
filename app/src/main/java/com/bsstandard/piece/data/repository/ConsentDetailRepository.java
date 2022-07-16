package com.bsstandard.piece.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.bsstandard.piece.data.dto.ConsentDetailDTO;
import com.bsstandard.piece.retrofit.RetrofitClient;
import com.bsstandard.piece.retrofit.RetrofitService;
import com.bsstandard.piece.widget.utils.LogUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * packageName    : com.bsstandard.piece.data.repository
 * fileName       : ConsentDetailRepository
 * author         : piecejhm
 * date           : 2022/06/21
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/21        piecejhm       최초 생성
 */
public class ConsentDetailRepository {
    private static RetrofitService mInstance;
    private static ConsentDetailRepository consentDetailRepository;
    private final MutableLiveData<ConsentDetailDTO> consentDetailData = new MutableLiveData<>();

    public static ConsentDetailRepository getInstance() {
        if (consentDetailRepository == null) {
            consentDetailRepository = new ConsentDetailRepository();
        }
        return consentDetailRepository;
    }

    public ConsentDetailRepository() {
        mInstance = RetrofitClient.getService();
    }

    public MutableLiveData<ConsentDetailDTO> getConsentDetailData(String consentCode) {
        Call<ConsentDetailDTO> consentDetailDtoCall = mInstance.getConsentDetail(consentCode);
        consentDetailDtoCall.enqueue(new Callback<ConsentDetailDTO>() {
            @Override
            public void onResponse(Call<ConsentDetailDTO> call, Response<ConsentDetailDTO> response) {
                if (response.body() != null) {
                    consentDetailData.setValue(response.body());
                } else {
                    LogUtil.logE("get consentDetail error .. " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ConsentDetailDTO> call, Throwable t) {
                LogUtil.logE("get ConsentDetail error .." + t);
                consentDetailData.postValue(null);
            }
        });
        return consentDetailData;
    }
}
