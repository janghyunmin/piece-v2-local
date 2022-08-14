package com.bsstandard.piece.data.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bsstandard.piece.data.datamodel.dmodel.join.JoinModel;
import com.bsstandard.piece.data.repository.JoinRepository;
import com.bsstandard.piece.data.dto.JoinDTO;
import com.bsstandard.piece.widget.utils.LogUtil;

/**
 * packageName    : com.bsstandard.piece.data.viewmodel
 * fileName       : JoinViewModel
 * author         : piecejhm
 * date           : 2022/06/30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/30        piecejhm       최초 생성
 */
public class MemberJoinViewModel extends AndroidViewModel {
    @SuppressWarnings({"FieldCanBeLocal"})
    public MutableLiveData<JoinDTO> joinData = null;
    public final JoinRepository joinRepository;

    public MemberJoinViewModel(@NonNull Application application){
        super(application);
        joinRepository = new JoinRepository(application);
    }

    public MutableLiveData<JoinDTO> postCallJoinData(JoinModel joinModel){
        if(joinData == null){
            joinData = new MutableLiveData<>();
            joinData = loadJoinData(joinModel);
        }
        else {
            LogUtil.logE("join data not null..");
            joinData = loadJoinData(joinModel);
        }
        return joinData;
    }
    public MutableLiveData<JoinDTO> loadJoinData(JoinModel joinModel){
        return joinRepository.getJoinData(joinModel);
    }


}
