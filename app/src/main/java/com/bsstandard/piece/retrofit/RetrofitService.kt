package com.bsstandard.piece.retrofit

import com.bsstandard.piece.data.datamodel.dmodel.MemberPinModel
import com.bsstandard.piece.data.datamodel.dmodel.SmsAuthModel
import com.bsstandard.piece.data.datamodel.dmodel.join.JoinModel
import com.bsstandard.piece.data.dto.*
import com.bsstandard.piece.data.dto.portfolio.PortfolioDTO
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

/**
 * packageName    : com.bsstandard.piece.api
 * fileName       : RetrofitService
 * author         : piecejhm
 * date           : 2022/06/10
 * description    : API Interface
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/06/10        piecejhm       최초 생성
 */
interface RetrofitService {
    // 앱 버전 조회 - jhm 2022/06/21
    @GET("member/app/version/{deviceType}")
    fun getVersion(@Path("deviceType") deviceType: String?): Call<VersionDTO?>?

    // 약관 목록 조회 - jhm 2022/06/21
    @get:GET("board/consent")
    val consent: Call<ConsentDTO?>?

    // 약관 동의 상세 조회 - jhm 2022/06/21
    @GET("board/consent/{consentCode}")
    fun getConsentDetail(@Path("consentCode") consentCode: String?): Call<ConsentDetailDTO?>?

    // 문자 발송 요청 - jhm 2022/06/22
    @POST("member/call_sms_auth")
    fun reqSmsAuth(@Body smsAuthModel: SmsAuthModel?): Call<CallSmsAuthDTO?>?

    // 문자 sms 검증요청 - jhm 2022/06/24
    @POST("member/call_sms_verification")
    fun PostVerification(@Body smsAuthModel: SmsAuthModel?): Call<SmsVerificationDTO?>?

    // 회원가입 요청 - jhm 2022/06/30
    @POST("member/join")
    fun PostMemberJoin(@Body joinModel: JoinModel?): Call<JoinDTO?>?

    // 회원 PIN 번호 검증 - jhm 2022/07/04
    @GET("member/auth/pin")
    fun getAuthPin(@HeaderMap headers: Map<String?, String?>?): Call<AuthPinDTO?>?

    // 회원 PIN 번호 변경 - jhm 2022/07/05
    @PUT("member/auth/pin")
    fun putAuthPin(
        @HeaderMap headers: Map<String?, String?>?,
        @Body memberPinModel: MemberPinModel?
    ): Call<AuthPinDTO?>?

    @GET("portfolio")
    fun getPortfolio() : Observable<PortfolioDTO>



}