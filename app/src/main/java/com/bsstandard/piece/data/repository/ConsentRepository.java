package com.bsstandard.piece.data.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.bsstandard.piece.data.dto.ConsentDTO;
import com.bsstandard.piece.di.hilt.ApiModule;
import com.bsstandard.piece.retrofit.RetrofitService;
import com.bsstandard.piece.widget.utils.LogUtil;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * packageName    : com.bsstandard.piece.data.repository
 * fileName       : ConsentRepository
 * author         : piecejhm
 * date           : 2022/06/17
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/17        piecejhm       최초 생성
 */


public class ConsentRepository {
    private static RetrofitService mInstance;
    private static ConsentRepository consentRepository;
    private final MutableLiveData<ConsentDTO> consentData = new MutableLiveData<>();

    public static ConsentRepository getInstance(Application application) {
        if (consentRepository == null) {
            consentRepository = new ConsentRepository(application);
        }
        return consentRepository;
    }

    public ConsentRepository(Application application) {
//        mInstance = RetrofitClient.getService();
        mInstance = ApiModule.INSTANCE.provideRetrofit().create(RetrofitService.class);
    }

    public MutableLiveData<ConsentDTO> getConsentData(String consentDvn) {
        Call<ConsentDTO> consentDAOCall = mInstance.getConsent(consentDvn);
        consentDAOCall.enqueue(new Callback<ConsentDTO>() {
            @Override
            public void onResponse(Call<ConsentDTO> call, Response<ConsentDTO> response) {
                if (response.body() != null) {
                    Gson gson = new Gson();
                    LogUtil.logE("get Consent : " +  gson.toJson(response.body()));
                    consentData.setValue(response.body());
                } else {
                    LogUtil.logE("get Consent Error " + response.message());
                    consentData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ConsentDTO> call, Throwable t) {
                LogUtil.logE("get Consent Error " + t);
                consentData.postValue(null);
            }
        });
        return consentData;
    }
}
