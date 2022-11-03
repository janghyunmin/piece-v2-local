package com.bsstandard.piece.view.myInfo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bsstandard.piece.data.datamodel.dmodel.consent.UpdateConsentList
import com.bsstandard.piece.data.datamodel.dmodel.member.MemberModifyModel
import com.bsstandard.piece.data.datamodel.dmodel.member.NotificationModel
import com.bsstandard.piece.data.datasource.shared.PrefsHelper
import com.bsstandard.piece.data.dto.ConsentDTO
import com.bsstandard.piece.data.viewmodel.ConsentViewModel
import com.bsstandard.piece.data.viewmodel.MemberPutViewModel
import com.bsstandard.piece.databinding.SlideBottomEmailBinding
import com.bsstandard.piece.widget.utils.DialogManager
import com.bsstandard.piece.widget.utils.LogUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 *packageName    : com.bsstandard.piece.view.myInfo
 * fileName       : MyInfoEmailDialog
 * author         : piecejhm
 * date           : 2022/10/17
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/10/17        piecejhm       최초 생성
 */


class MyInfoEmailDialog(context: Context) : BottomSheetDialogFragment() {
    private val mContext: Context = context
    lateinit var binding: SlideBottomEmailBinding
    private var evm: EmailViewModel? = null
    var mEmail: String? = null

    // 회원 정보 수정 요청 Model - jhm 2022/09/07
    var consentList: ArrayList<UpdateConsentList?>? = ArrayList() // 약관 리스트 - jhm 2022/09/06
    private var memberPutViewModel: MemberPutViewModel? = null
    private var consentViewModel: ConsentViewModel? = null
    private var memberModifyModel: MemberModifyModel? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SlideBottomEmailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        evm = ViewModelProvider(this)[EmailViewModel::class.java]


        binding.emailViewModel = evm
        memberPutViewModel = ViewModelProvider(this).get(MemberPutViewModel::class.java)
        consentViewModel = ViewModelProvider(this).get(ConsentViewModel::class.java)

        emailChkObserver()
        binding.getEmail()






        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun emailChkObserver() {

        val emailObserver =
            Observer { inputEmail: String ->
                mEmail = inputEmail
            }

        evm?.getEmail()?.observe(this, emailObserver)
        evm?.getEmail()?.observe(this, Observer { email ->
            mEmail = email
            LogUtil.logE("email : $email")
            emailRegister()
        })


    }

    private fun emailRegister() {

        binding.emailViewModel?.email?.observe(viewLifecycleOwner) {
            fun checkEmail(email: String): Boolean = email.contains("@")

            if (evm?.email?.value.toString().isEmpty()) {
                LogUtil.logE("이메일 입력값 없음..")

                binding.clear.visibility = View.GONE
                binding.confirmBtn.isSelected = false
            } else {

                binding.clear.visibility = View.VISIBLE
                binding.clear.setOnClickListener {
                    binding.email.setText("")
                }
                binding.confirmBtn.isSelected = true

                binding.confirmBtn.setOnClickListener {
                    LogUtil.logE("이메일 등록 확인 버튼 OnClick..")

                    // 이메일 형식인지 체크 - jhm 2022/10/17
                    if (checkEmail(binding.email.text.toString())) {
                        LogUtil.logE("정상적인 이메일")
                        binding.emailError.visibility = View.GONE


                        val memberId = PrefsHelper.read("memberId", "")
                        val name = PrefsHelper.read("name", "")
                        val pinNumber = PrefsHelper.read("pinNumber", "")
                        val cellPhoneNo = PrefsHelper.read("cellPhoneNo", "")
                        val cellPhoneIdNo = PrefsHelper.read("cellPhoneIdNo", "")
                        val birthDay = PrefsHelper.read("birthDay", "")
                        val zipCode = PrefsHelper.read("zipCode", "")
                        val baseAddress: String = PrefsHelper.read("baseAddress", "")
                        val detailAddress: String = PrefsHelper.read("detailAddress", "")
                        val ci = PrefsHelper.read("ci", "")
                        val di = PrefsHelper.read("di", "")
                        val gender = PrefsHelper.read("gender", "")
                        val email = binding.email.text.toString()
                        val isFido = PrefsHelper.read("isFido", "")

                        val notification = NotificationModel(
                            PrefsHelper.read("memberId", ""),
                            "Y",
                            "Y",
                            "Y",
                            "Y"
                        )

                        consentList!!.clear()
                        consentViewModel!!.getConsentData("SIGN").observe(
                            viewLifecycleOwner
                        ) { response: ConsentDTO ->
                            LogUtil.logE("response : ${response.data.size}")
                            for (index in response.data.indices) {
                                consentList!!.add(
                                    UpdateConsentList(
                                        PrefsHelper.read("memberId", ""),
                                        response.data[index].consentCode,
                                        "Y"
                                    )
                                )
                            }
                        }

                        memberModifyModel = MemberModifyModel(
                            memberId,
                            name,
                            "",
                            cellPhoneNo,
                            cellPhoneIdNo,
                            birthDay,
                            zipCode,
                            baseAddress,
                            detailAddress,
                            ci,
                            di,
                            gender,
                            email,
                            isFido,
                            notification,
                            consentList
                        )
                        memberPutViewModel!!.putCallMemberData(memberModifyModel)
                        memberPutViewModel!!.memberPutData.observe(
                            viewLifecycleOwner,
                            Observer { memberPutDTO ->

                                PrefsHelper.write("email",memberPutDTO.data.email.toString())
                                LogUtil.logE("이메일 정보 변경 완료 " + memberPutDTO.status)

                                dismiss()

                                DialogManager.openNotGoDalog(requireContext(),"이메일 등록 완료","이메일이 성공적으로 등록되었어요")
                            })

                    } else {
                        LogUtil.logE("정상적인 이메일 형식이 아님")
                        binding.emailError.visibility = View.VISIBLE
                    }
                }

            }
        }
    }


}