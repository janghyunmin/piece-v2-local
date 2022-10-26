package com.bsstandard.piece.data.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.bsstandard.piece.data.dto.LocationDTO;
import com.bsstandard.piece.di.hilt.ApiModule;
import com.bsstandard.piece.retrofit.RetrofitService;
import com.bsstandard.piece.widget.utils.LogUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * packageName    : com.bsstandard.piece.data.repository
 * fileName       : AddressRepository
 * author         : piecejhm
 * date           : 2022/09/04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/04        piecejhm       최초 생성
 */
public class AddressRepository {
    private static RetrofitService retrofitService;
    private static AddressRepository addressRepository;
    private final MutableLiveData<LocationDTO> addressData = new MutableLiveData<>();

    public static AddressRepository getInstance(Application application) {
        if(addressRepository == null) {
            addressRepository = new AddressRepository(application);
        }
        return addressRepository;
    }

    public AddressRepository(Application application) {
        retrofitService = ApiModule.INSTANCE.provideRetrofit().create(RetrofitService.class);
    }

    public MutableLiveData<LocationDTO> getAddressData(String keyword , int countPerPage) {
        Call<LocationDTO> addressCall = retrofitService.getSearchAddress(keyword,countPerPage);
        addressCall.enqueue(new Callback<LocationDTO>() {
            @Override
            public void onResponse(Call<LocationDTO> call, Response<LocationDTO> response) {
                if(response.isSuccessful()) {
                    LogUtil.logE("주소찾기 성공");
                    if(response.body() != null) {
                        addressData.postValue(response.body());
                    } else {
                        addressData.postValue(response.body());
                    }
                } else {
                    addressData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<LocationDTO> call, Throwable t) {
                LogUtil.logE("get Address Error " + t);
                addressData.postValue(null);
            }
        });
        return addressData;
    }
}
