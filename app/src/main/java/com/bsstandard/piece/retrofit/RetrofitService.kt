package com.bsstandard.piece.retrofit

import com.bsstandard.piece.data.datamodel.dmodel.MemberPinModel
import com.bsstandard.piece.data.datamodel.dmodel.SmsAuthModel
import com.bsstandard.piece.data.datamodel.dmodel.join.JoinModel
import com.bsstandard.piece.data.datamodel.dmodel.magazine.MemberBookmarkRegModel
import com.bsstandard.piece.data.datamodel.dmodel.magazine.MemberBookmarkRemoveModel
import com.bsstandard.piece.data.datamodel.dmodel.member.MemberModifyModel
import com.bsstandard.piece.data.dto.*
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
    @GET("board/consent")
    fun getConsent(@Query("consentDvn") consentDvn: String): Call<ConsentDTO?>?
    //val consent: Call<ConsentDTO?>?

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

    // accessToken 검증 요청 - jhm 2022/07/31
    @GET("member/auth")
    fun getAccessToken(@HeaderMap headers: Map<String?, String?>?): Call<BaseDTO?>?

    // accessToken 생성 요청 - jhm 2022/08/04
    @POST("member/auth")
    suspend fun postAccessToken(
        @Header("deviceId") deviceId: String?,
        @Header("grantType") grantType: String?,
        @Header("memberId") memberId: String?
    ): Observable<PostTokenDTO?>?

    // accessToken refresh 요청 - jhm 2022/08/04
    @PUT("member/auth")
    fun putRefreshToken(@HeaderMap headers: HashMap<String, String>): Call<PostTokenDTO?>?


    // 회원 PIN 번호 검증 - jhm 2022/07/04
    @GET("member/auth/pin")
    fun getAuthPin(@HeaderMap headers: Map<String?, String?>?): Call<AuthPinDTO?>?

    // 회원 PIN 번호 변경 - jhm 2022/07/05
    @PUT("member/auth/pin")
    fun putAuthPin(
        @HeaderMap headers: Map<String?, String?>?,
        @Body memberPinModel: MemberPinModel?
    ): Call<AuthPinDTO?>?

    // 회원 정보 조회 - jhm 2022/09/03
//    @GET("member")
//    fun getMember(@HeaderMap headers: Map<String?, String?>?): Call<MemberDTO?>?
    @GET("member")
    fun getMember(
        @Header("accessToken") accessToken: String?,
        @Header("deviceId") deviceId: String?,
        @Header("memberId") memberId: String?,
    ): Observable<MemberDTO>


    // 회원 정보 수정 요청 - jhm 2022/09/05
    @PUT("member")
    fun putMember(
        @HeaderMap headers: HashMap<String?, String?>,
        @Body memberModifyModel: MemberModifyModel?
    ): Call<MemberPutDTO?>?


    // 포트폴리오 조회 - jhm 2022/08/17
    @GET("portfolio")
    fun getPortfolio(): Observable<PortfolioDTO>

    // 포트폴리오 상세 조회 - jhm 2022/08/17
    @GET("portfolio/{portfolioId}")
    //fun getPortfolioDetail(@Path("portfolioId") portfolioId:String?) : Observable<PortfolioDetailDTO> // Observable 방식 - jhm 2022/08/19
    //fun getPortfolioDetail(@Path("portfolioId") portfolioId: String?) : Call<PortfolioDetailDTO> // Call방식 - jhm 2022/08/19
    suspend fun getPortfolioDetail(@Path("portfolioId") portfolioId: String?): PortfolioDetailDTO


    // 매거진(라운지) 조회 (비로그인 전용)- jhm 2022/08/29
    @GET("board/magazine")
    fun getNoMemberMagazine(@Query("magazineType") magazineType: String): Observable<MagazineDTO>

    @GET("board/magazine/{magazineId}")
    suspend fun getMagazineDetail(@Path("magazineId") magazineId: String?): MagazineDetailDTO

    // 매거진(라운지) 조회 (회원 전용) - jhm 2022/08/30
    @GET("member/magazine")
    fun getMagazine(
        @Header("accessToken") accessToken: String?,
        @Header("deviceId") deviceId: String?,
        @Header("memberId") memberId: String?,
        @Query("magazineType") magazineType: String
    ): Observable<MagazineDTO>

    // 회원 북마크 조회 - jhm 2022/09/04
    @GET("member/bookmark")
    fun getBookMark(
        @Header("accessToken") accessToken: String?,
        @Header("deviceId") deviceId: String?,
        @Header("memberId") memberId: String?
    ): Observable<BookMarkDTO>

    // 매거진(라운지) 북마크 요청 - jhm 2022/08/29
    @POST("member/bookmark")
    fun updateBookMark(
        @Header("accessToken") accessToken: String?,
        @Header("deviceId") deviceId: String?,
        @Header("memberId") memberId: String?,
        @Body memberBookmarkRegModel: MemberBookmarkRegModel?
    ): Call<BaseDTO>

    // 매거진(라운지) 북마크 삭제 요청 - jhm 2022/08/30
    @HTTP(method = "DELETE", path = "member/bookmark", hasBody = true)
    fun deleteBookMark(
        @Header("accessToken") accessToken: String?,
        @Header("deviceId") deviceId: String?,
        @Header("memberId") memberId: String?,
        @Body memberBookmarkRemoveModel: MemberBookmarkRemoveModel?
    ): Call<BaseDTO>


    // 내정보 상세 주소검색 - jhm 2022/09/04
    @GET("common/location")
//    fun getSearchAddress(
//        @Query("countPerPage") countPerPage: Int ,
//        @Query("currentPage") currentPage: Int,
//        @Query("keyword") keyword:String ): Call<LocationDTO?>?
    fun getSearchAddress(@Query("keyword") keyword: String): Call<LocationDTO?>?





    // board 공지사항 조회 - jhm 2022/09/10
    @GET("board")
    fun getNotice(@Query ("boardType") boardType: String): Observable<BoardDTO>

    // board 공지사항 상세 조회 - jhm 2022/09/11
    @GET("board/detail/{boardId}")
    suspend fun getNoticeDetail(@Path("boardId") boardId: String): BoardDetailDTO


    // 이벤트 목록 조회 - jhm 2022/09/11
    @GET("board/event")
    fun getEvent(): Observable<EventDTO>
    
    // 이벤트 상세 조회 - jhm 2022/09/11
    @GET("board/event/{eventId}")
    suspend fun getEventDetail(@Path("eventId") eventId: String) : EventDetailDTO

    // 쿠폰 및 프로모션 코드 사용 요청 - jhm 2022/09/13
    @GET("member/coupon/{couponCode}")
    fun getCouponCodeInput(
        @Path("couponCode") couponCode: String,
        @Header("accessToken") accessToken: String?,
        @Header("deviceId") deviceId: String?,
        @Header("memberId") memberId: String?) : Call<CouponDTO>

}