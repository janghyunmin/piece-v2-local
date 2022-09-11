package com.bsstandard.piece.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bsstandard.piece.data.dto.LocationDTO;
import com.bsstandard.piece.data.repository.AddressRepository;

/**
 * packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : AddressViewModel
 * author         : piecejhm
 * date           : 2022/09/04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/09/04        piecejhm       최초 생성
 */
public class AddressViewModel extends AndroidViewModel {
    @SuppressWarnings({"FieldCanBeLocal"})
    public MutableLiveData<LocationDTO> addressData = new MutableLiveData<>();
    public final AddressRepository addressRepository;

    public AddressViewModel(@NonNull Application application) {
        super(application);
        addressRepository = new AddressRepository(application);
    }
    public MutableLiveData<LocationDTO> getAddressData(String keyword) {
        addressData = loadAddressData(keyword);
        return addressData;
    }
    public MutableLiveData<LocationDTO> loadAddressData(String keyword) {
        return addressRepository.getAddressData(keyword);
    }
}
