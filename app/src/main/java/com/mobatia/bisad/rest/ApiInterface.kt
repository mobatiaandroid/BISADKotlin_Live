package com.mobatia.bisad.rest

import com.google.gson.JsonObject
import com.mobatia.bisad.activity.absence.model.*
import com.mobatia.bisad.activity.canteen.model.TimeExceedModel
import com.mobatia.bisad.activity.canteen.model.add_orders.CanteenItemsApiModel
import com.mobatia.bisad.activity.canteen.model.add_orders.CatListModel
import com.mobatia.bisad.activity.canteen.model.add_orders.ItemsListModel
import com.mobatia.bisad.activity.canteen.model.add_to_cart.*
import com.mobatia.bisad.activity.canteen.model.canteen_cart.CanteenCartApiModel
import com.mobatia.bisad.activity.canteen.model.canteen_cart.CanteenCartModel
import com.mobatia.bisad.activity.canteen.model.information.InfoCanteenModel
import com.mobatia.bisad.activity.canteen.model.myorders.CancelCanteenPreOrderApiModel
import com.mobatia.bisad.activity.canteen.model.myorders.CancelCanteenPreorderItemId
import com.mobatia.bisad.activity.canteen.model.myorders.PreOrdersModel
import com.mobatia.bisad.activity.canteen.model.myorders.UpdateCanteenPreorderItemApiModel
import com.mobatia.bisad.activity.canteen.model.order_history.OrderHistoryApiModel
import com.mobatia.bisad.activity.canteen.model.order_history.OrderHistoryResponseModel
import com.mobatia.bisad.activity.canteen.model.preorder.CanteenPreorderApiModel
import com.mobatia.bisad.activity.canteen.model.preorder.CanteenPreorderModel
import com.mobatia.bisad.activity.canteen.model.topup.WalletAmountApiModel
import com.mobatia.bisad.activity.canteen.model.topup.WalletAmountModel
import com.mobatia.bisad.activity.canteen.model.wallet.WalletBalanceApiModel
import com.mobatia.bisad.activity.canteen.model.wallet.WalletBalanceModel
import com.mobatia.bisad.activity.canteen.model.wallethistory.WalletHistoryApiModel
import com.mobatia.bisad.activity.canteen.model.wallethistory.WalletHistoryModel
import com.mobatia.bisad.activity.common.model.DeviceRegModel
import com.mobatia.bisad.activity.communication.letter.model.LetterResponseModel
import com.mobatia.bisad.activity.communication.magazine.model.MagazineResponseModel
import com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterDetailApiModel
import com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterDetailModel
import com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterListModel
import com.mobatia.bisad.activity.home.model.DataCollectionSubmissionModel
import com.mobatia.bisad.activity.message.model.MessageDetailApiModel
import com.mobatia.bisad.activity.message.model.MessageDetailModel
import com.mobatia.bisad.activity.parent_meetings.model.PtaAllottedDatesApiModel
import com.mobatia.bisad.activity.parent_meetings.model.PtaAllottedDatesModel
import com.mobatia.bisad.activity.parent_meetings.model.cancel.PtaCancelApiModel
import com.mobatia.bisad.activity.parent_meetings.model.cancel.PtaCancelModel
import com.mobatia.bisad.activity.parent_meetings.model.confirm_pta.PtaConfirmationApiModel
import com.mobatia.bisad.activity.parent_meetings.model.confirm_pta.PtaConfirmationModel
import com.mobatia.bisad.activity.parent_meetings.model.insert_pta.PtaInsertApiModel
import com.mobatia.bisad.activity.parent_meetings.model.insert_pta.PtaInsertModel
import com.mobatia.bisad.activity.parent_meetings.model.listing_pta.PtaListingApiModel
import com.mobatia.bisad.activity.parent_meetings.model.listing_pta.PtaListingModel
import com.mobatia.bisad.activity.parent_meetings.model.review_list.PtaReviewListModel
import com.mobatia.bisad.activity.payment.model.InfoPaymentModel
import com.mobatia.bisad.activity.payment.model.PayCategoryModel
import com.mobatia.bisad.activity.payment.model.PaymentCategoriesApiModel
import com.mobatia.bisad.activity.payment.model.payment_gateway.PaymentGatewayApiModel
import com.mobatia.bisad.activity.payment.model.payment_gateway.PaymentGatewayModel
import com.mobatia.bisad.activity.payment.model.payment_submit.PaymentSubmitApiModel
import com.mobatia.bisad.activity.payment.model.payment_submit.PaymentSubmitModel
import com.mobatia.bisad.activity.payment.model.payment_token.PaymentTokenApiModel
import com.mobatia.bisad.activity.payment.model.payment_token.PaymentTokenModel
import com.mobatia.bisad.activity.permission_slip.model.PermissionResApiModel
import com.mobatia.bisad.activity.permission_slip.model.PermissionResponseModel
import com.mobatia.bisad.activity.school_trips.model.PaymentGatewayApiModelTrip
import com.mobatia.bisad.activity.school_trips.model.SubmitDocResponseModel
import com.mobatia.bisad.activity.school_trips.model.TripChoicePreferenceResponseModel
import com.mobatia.bisad.activity.school_trips.model.TripConsentResponseModel
import com.mobatia.bisad.activity.school_trips.model.TripCountResponseModel
import com.mobatia.bisad.activity.school_trips.model.TripDetailsResponseModel
import com.mobatia.bisad.activity.school_trips.model.TripDocumentSubmitResponseModel
import com.mobatia.bisad.activity.school_trips.model.TripHistoryResponseModel
import com.mobatia.bisad.activity.school_trips.model.TripInvoiceResponseModel
import com.mobatia.bisad.activity.school_trips.model.TripListResponseModel
import com.mobatia.bisad.activity.school_trips.model.TripPaymentInitiateResponseModel
import com.mobatia.bisad.activity.school_trips.model.TripPaymentSubmitModel
import com.mobatia.bisad.activity.settings.re_enrollment.model.EnrollmentStatusModel
import com.mobatia.bisad.activity.settings.termsofservice.model.TermsOfServiceModel
import com.mobatia.bisad.activity.term_dates.model.TermDatesDetailApiModel
import com.mobatia.bisad.activity.term_dates.model.TermDatesDetailModel
import com.mobatia.bisad.constants.GeneralSubmitResponseModel
import com.mobatia.bisad.fragment.apps.model.AppsApiModel
import com.mobatia.bisad.fragment.apps.model.AppsListModel
import com.mobatia.bisad.fragment.attendance.model.AttendanceApiModel
import com.mobatia.bisad.fragment.attendance.model.AttendanceListModel
import com.mobatia.bisad.fragment.calendar.model.CalendarApiModel
import com.mobatia.bisad.fragment.calendar.model.CalendarListModel
import com.mobatia.bisad.fragment.calendar_new.model.CalendarModel
import com.mobatia.bisad.fragment.canteen.model.CanteenBannerResponseModel
import com.mobatia.bisad.fragment.canteen.model.CanteenSendEmailApiModel
import com.mobatia.bisad.fragment.contact_us.model.ContactusModel
import com.mobatia.bisad.fragment.curriculum.model.CuriculumListModel
import com.mobatia.bisad.fragment.curriculum.model.CurriculumStudentApiModel
import com.mobatia.bisad.fragment.forms.model.FormsResponseModel
import com.mobatia.bisad.fragment.home.model.BannerModel
import com.mobatia.bisad.fragment.home.model.CountryListModel
import com.mobatia.bisad.fragment.home.model.RelationShipListModel
import com.mobatia.bisad.fragment.home.model.TilesListModel
import com.mobatia.bisad.fragment.home.model.datacollection.DataCollectionModel
import com.mobatia.bisad.fragment.home.model.reenrollment.*
import com.mobatia.bisad.fragment.messages.model.MessageListApiModel
import com.mobatia.bisad.fragment.messages.model.MessageListModel
import com.mobatia.bisad.fragment.payment.model.PaymentBannerResponseModel
import com.mobatia.bisad.fragment.permission_slip.model.PermissionSlipListApiModel
import com.mobatia.bisad.fragment.permission_slip.model.PermissionSlipModel
import com.mobatia.bisad.fragment.report_absence.model.AbsenceLeaveApiModel
import com.mobatia.bisad.fragment.report_absence.model.AbsenceListModel
import com.mobatia.bisad.fragment.report_absence.model.PickupModel
import com.mobatia.bisad.fragment.reports.model.ReportApiModel
import com.mobatia.bisad.fragment.reports.model.ReportListModel
import com.mobatia.bisad.fragment.school_trips.model.TripBannerResponse
import com.mobatia.bisad.fragment.school_trips.model.TripCategoriesResponseModel
import com.mobatia.bisad.fragment.settings.model.ChangePasswordApiModel
import com.mobatia.bisad.fragment.settings.model.DeleteAccountModel
import com.mobatia.bisad.fragment.settings.model.TriggerUSer
import com.mobatia.bisad.fragment.socialmedia.model.SocialMediaListModel
import com.mobatia.bisad.fragment.staff_directory.model.SendStaffEmailApiModel
import com.mobatia.bisad.fragment.staff_directory.model.SendStaffEmailModel
import com.mobatia.bisad.fragment.staff_directory.model.TeachingStaffListApiModel
import com.mobatia.bisad.fragment.staff_directory.model.TeachingStaffListModel
import com.mobatia.bisad.fragment.student_information.model.StudentInfoApiModel
import com.mobatia.bisad.fragment.student_information.model.StudentInfoModel
import com.mobatia.bisad.fragment.student_information.model.StudentListModel
import com.mobatia.bisad.fragment.teacher_contact.model.SendStaffMailApiModel
import com.mobatia.bisad.fragment.teacher_contact.model.StaffListApiModel
import com.mobatia.bisad.fragment.teacher_contact.model.StaffListModel
import com.mobatia.bisad.fragment.time_table.model.apimodel.TimeTableApiDataModel
import com.mobatia.bisad.fragment.time_table.model.apimodel.TimeTableApiModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
  /*************HOME_BANNER_IMAGE****************/
    @POST("banner_images")
    @Headers("Content-Type: application/json")
    fun bannerimages(
        @Body  bannerModel: BannerModel,
        @Header("Authorization") token:String
    ):
     Call<ResponseBody>
    /*************SETTINGS USER DETAIL****************/
    @POST("settings_userdetails")
    @Headers("Content-Type: application/json")
    fun settingsUserDetail(
        @Body  bannerModel: BannerModel,
        @Header("Authorization") token:String
    ):
     Call<ResponseBody>
    /*************COMMUNICATION_BANNER_IMAGE****************/
    @POST("communication/banner_images")
    @Headers("Content-Type: application/x-www-form-urlencode","Accept: application/json")
    fun communication(
        @Header("Authorization") token:String
    ):
     Call<ResponseBody>

    /*************ACCESS TOKEN ****************/
    @POST("user/token")
    @Headers("Content-Type: application/x-www-form-urlencode","Accept: application/json")
    fun access_token(
        @Header("authorization-user") usercode:String?
    ):
     Call<ResponseBody>

    /*************SIGN UP****************/
    @POST("parent_signup")
    @FormUrlEncoded
    fun signup(
        @Field("email") email: String,
        @Field("devicetype") devicetype: Int,
        @Field("deviceid") deviceid: String
    ): Call<ResponseBody>
    /*************Forget Password****************/
    @POST("forgot_password")
    @FormUrlEncoded
    fun forgetPassword(
        @Field("email") email: String
    ): Call<ResponseBody>

    /*************LOGIN****************/
    @POST("login")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("devicetype") devicetype: Int,
        @Field("deviceid") deviceid: String,
        @Field("fcm_id") fcmid: String,
        @Field("device_name") device_name: String,
        @Field("app_version") app_version: String

    ): Call<ResponseBody>

    /*************STUDENT_LIST****************/
    @POST("student/list")
    @Headers("Content-Type: application/x-www-form-urlencode","Accept: application/json")
    fun studentList(
        @Header("Authorization") token:String
    ): Call<StudentListModel>
 /*************STAFF_LIST****************/
    @POST("staff/list")
    @Headers("Content-Type: application/json")
    fun staffList(
     @Body  staffBody: StaffListApiModel,
     @Header("Authorization") token:String
    ): Call<StaffListModel>

    /*************STUDENT_INFO****************/
    @POST("student/info")
    @Headers("Content-Type: application/json")
    fun studentInfo(
        @Body  studentbody: StudentInfoApiModel,
        @Header("Authorization") token:String
    ): Call<StudentInfoModel>

    /*************NOTIFICATION_LIST****************/
    @POST("notification/list")
    @Headers("Content-Type: application/json")
    fun notificationList(
        @Body  messageListApiModel: MessageListApiModel,
        @Header("Authorization") token:String
    ): Call<MessageListModel>

      /*************Calendar List****************/
    @POST("calendar")
    @Headers("Content-Type: application/json")
    fun calendarList(): Call<CalendarModel>
    /*************ABSENCE List****************/
    @POST("getcalendar_detail")
    @Headers("Content-Type: application/json")
    fun calendarDetail(
        @Body  calendarApi: CalendarApiModel,
        @Header("Authorization") token:String
    ): Call<CalendarListModel>
    /*************ABSENCE List****************/
    @POST("leave/request")
    @Headers("Content-Type: application/json")
    fun absenceList(
        @Body  studentInfoModel: AbsenceLeaveApiModel,
        @Header("Authorization") token:String
    ): Call<AbsenceListModel>
    /*************PLANNED List****************/
    @POST("planned_leave/request")
    @Headers("Content-Type: application/json")
    fun plannedList(
        @Body  studentInfoModel: AbsenceLeaveApiModel,
        @Header("Authorization") token:String
    ): Call<AbsenceListModel>
    @POST("social_media")
    @Headers("Content-Type: application/x-www-form-urlencode","Accept: application/json")
    fun socialMedia(
        @Header("Authorization") token:String
    ): Call<SocialMediaListModel>

/*Leave Request*/
   @POST("request/leave")
   @Headers("Content-Type: application/json")
    fun leaveRequest(
    @Body requestLeave: RequestAbsenceApiModel,
    @Header("Authorization") token:String
    ): Call<ResponseBody>
 @POST("request/planned_leave")
   @Headers("Content-Type: application/json")
    fun plannedLeaveRequest(
    @Body requestLeave: RequestAbsenceApiModel,
    @Header("Authorization") token:String
    ): Call<ResponseBody>

   /*SEND EMAIL TO STAFF*/
   @POST("sendemail")
   @Headers("Content-Type: application/json")
    fun sendStaffMail(
    @Body  sendMail: SendStaffMailApiModel,
    @Header("Authorization") token:String
    ): Call<ResponseBody>

    /*************TERM_DATES LIST****************/
    @POST("termdates")
    @Headers("Content-Type: application/json")
    fun termdates(
        @Header("Authorization") token:String
    ): Call<TermDatesDetailModel>

    @POST("terms_of_service")
    @Headers("Content-Type: application/x-www-form-urlencode","Accept: application/json")
    fun termsOfService(
        @Header("Authorization") token:String
    ): Call<TermsOfServiceModel>


    @POST("parent/logout")
    @Headers("Content-Type: application/x-www-form-urlencode","Accept: application/json")
    fun logout(
        @Header("Authorization") token:String
    ): Call<ResponseBody>

    /*CHANGE PASSWORD*/
    @POST("changepassword")
    @Headers("Content-Type: application/json")
    fun changePassword(
        @Body  changePassword: ChangePasswordApiModel,
        @Header("Authorization") token:String
    ): Call<ResponseBody>

    /*************APPS LIST****************/
    @POST("apps")
    @Headers("Content-Type: application/json")
    fun appsList(
        @Body  appsList: AppsApiModel,
        @Header("Authorization") token:String
    ): Call<AppsListModel>

    /*************TERM_DATES DETAIL****************/
    @POST("termdate/details")
    @Headers("Content-Type: application/json")
    fun termDatesDetails(
        @Body  termDates: TermDatesDetailApiModel,
        @Header("Authorization") token:String
    ): Call<TermDatesDetailModel>


    /*************NEWSLETTER List****************/
    @POST("newsletters")
    @Headers("Content-Type: application/json")
    fun newsletters(
        @Header("Authorization") token:String
    ): Call<NewsLetterListModel>

    /*************NEWSLETTER DETAIL****************/
    @POST("newsletters/details")
    @Headers("Content-Type: application/json")
    fun newsletterdetail(
        @Body  newsLetterDetailApi: NewsLetterDetailApiModel,
        @Header("Authorization") token:String
    ): Call<NewsLetterDetailModel>

    /*************NOTIFICATION DETAIL****************/
    @POST("notification/details")
    @Headers("Content-Type: application/json")
    fun notifictaionDetail(
        @Body  newsLetterDetailApi: MessageDetailApiModel,
        @Header("Authorization") token:String
    ): Call<MessageDetailModel>

    /*************TIME TABLE DATA****************/
    @POST("timetable")
    @Headers("Content-Type: application/json")
    fun timetable(
        @Body  timeTableApi: TimeTableApiModel,
        @Header("Authorization") token:String
    ): Call<TimeTableApiDataModel>

    /*************TITLES_LIST****************/
    @POST("titles")
    @Headers("Content-Type: application/x-www-form-urlencode","Accept: application/json")
    fun titlesList(
        @Header("Authorization") token:String
    ): Call<TilesListModel>
 /*************COUNTRY_LIST****************/
    @POST("countries")
    @Headers("Content-Type: application/x-www-form-urlencode","Accept: application/json")
    fun countryList(
        @Header("Authorization") token:String
    ): Call<CountryListModel>
 /*************RELATIONSHIP_LIST****************/
    @POST("contact_types")
    @Headers("Content-Type: application/x-www-form-urlencode","Accept: application/json")
    fun relationshipList(
        @Header("Authorization") token:String
    ): Call<RelationShipListModel>

    /*************DATA_COLLECTION_DETAIL****************/
    @POST("data_collection_details")
    @Headers("Content-Type: application/x-www-form-urlencode","Accept: application/json")
    fun dataCollectionDetail(
        @Header("Authorization") token:String
    ): Call<DataCollectionModel>
    /*************Contact Us****************/
    @POST("contact_us")
    fun contactus(
        @Header("Authorization") token:String
    ): Call<ContactusModel>

    /*************Attendance List****************/
    @POST("attendance_record")
    @Headers("Content-Type: application/json")
    fun attendanceList(
        @Body attendanceListModel: AttendanceApiModel,
        @Header("Authorization") token:String
    ): Call<AttendanceListModel>

    /*************Report List****************/
    @POST("progressreport")
    @Headers("Content-Type: application/json")
    fun reportList(
        @Body reportListModel: ReportApiModel,
        @Header("Authorization") token:String
    ): Call<ReportListModel>
    /*Leave Request*/
    @POST("submit_datacollection")
    @Headers("Content-Type: application/json")
    fun dataCollectionSubmittion(
        @Body  dataLeave: DataCollectionSubmissionModel,
        @Header("Authorization") token:String
    ): Call<ResponseBody>
 @POST("trigger_user")
    @Headers("Content-Type: application/json")
    fun triggerUser(
        @Body  trigger: TriggerUSer,
        @Header("Authorization") token:String
    ): Call<ResponseBody>
    /*************Report List****************/
    @POST("curriculm_guides")
    @Headers("Content-Type: application/json")
    fun curriculumList(
        @Body reportListModel: CurriculumStudentApiModel,
        @Header("Authorization") token:String
    ): Call<CuriculumListModel>

    /*************FORMS_LIST****************/
    @POST("forms")
    @Headers("Content-Type: application/json")
    fun formslist(
        @Body  messageListApiModel: MessageListApiModel,
        @Header("Authorization") token:String
    ): Call<FormsResponseModel>


    /*************LOGIN****************/
    @POST("device_registration")
    @Headers("Content-Type: application/json")
    fun deviceregistration(
        @Body  messageListApiModel: DeviceRegModel,
        @Header("Authorization") token:String
    ): Call<ResponseBody>


    /*************LETTER LIST****************/
    @POST("letters")
    @Headers("Content-Type: application/json")
    fun letterList(
        @Body  messageListApiModel: MessageListApiModel,
        @Header("Authorization") token:String
    ): Call<LetterResponseModel>

    /*************LETTER LIST****************/
    @POST("magazines")
    @Headers("Content-Type: application/json")
    fun magazineList(
        @Body  messageListApiModel: MessageListApiModel,
        @Header("Authorization") token:String
    ): Call<MagazineResponseModel>

    /*************EARLY PICKUP REQUEST****************/
    @POST("request_early_pickup")
    @Headers("Content-Type: application/json")
    fun pickupRequest(
        @Body  requestPickupApiModel: RequestPickupApiModel,
        @Header("Authorization") token:String
    ): Call<EarlyPickupModel>

    /*************EARLY PICKUP LIST****************/
    @POST("list_early_pickup")
    @Headers("Content-Type: application/json")
    fun pickupList(
        @Body  pickupListApiModel: PickupListApiModel,
        @Header("Authorization") token:String
    ): Call<PickupModel>

    /*************PERMISSION SLIP RESPONSE****************/
    @POST("permissionslip/consent")
    @Headers("Content-Type: application/json")
    fun permsnlistResponse(
        @Body  pickupListApiModel: PermissionResApiModel,
        @Header("Authorization") token:String
    ): Call<PermissionResponseModel>

    /*************PERMISSIONSLIP LIST****************/
    @POST("permissionslip/list")
    @Headers("Content-Type: application/json")
    fun permissnslipList(
        @Body  permissionSlipListModel: PermissionSlipListApiModel,
        @Header("Authorization") token:String
    ): Call<PermissionSlipModel>

    /*************Get Re Enrollment****************/
    @POST("get_enrollment_form")
    @Headers("Content-Type: application/json")
    fun getreenrollmentForm(
        @Body  getreenrollmentModel: GetreenrollmentApiModel,
        @Header("Authorization") token:String
    ): Call<GetreenrollmentModel>

   /************Submit Re Enrollment***************/
    @POST("save_re_enrollment")
    @Headers("Content-Type: application/json")
    fun savereenrollmentForm(
       @Body  savereenrollmentModel: SaveReenrollmentApiModel,
       @Header("Authorization") token:String
    ): Call<SavereenrollmentModel>

    /***********PTA Alloted dates**************/
    @POST("pta_allotted_dates")
    @Headers("Content-Type: application/json")
    fun pta_allotted_dates(
        @Body  ptaAllottedDatesModel: PtaAllottedDatesApiModel,
        @Header("Authorization") token:String
    ): Call<PtaAllottedDatesModel>

    /***********PTA listing**************/
    @POST("listing_pta")
    @Headers("Content-Type: application/json")
    fun listing_pta(
        @Body  ptaListingModel: PtaListingApiModel,
        @Header("Authorization") token:String
    ): Call<PtaListingModel>
    /***********PTA insert**************/
    @POST("insert_pta")
    @Headers("Content-Type: application/json")
    fun insert_pta(
        @Body  ptaInsertModel: PtaInsertApiModel,
        @Header("Authorization") token:String
    ): Call<PtaInsertModel>
    /***********PTA review**************/
    @POST("pta_review_list")
    @Headers("Content-Type: application/json")
    fun pta_review_list(
        @Header("Authorization") token:String
    ): Call<PtaReviewListModel>
    /***********PTA confirm**************/
    @POST("pta_confirmation")
    @Headers("Content-Type: application/json")
    fun pta_confirmation(
        @Body  ptaConfirmationModel: PtaConfirmationApiModel,
        @Header("Authorization") token:String
    ): Call<PtaConfirmationModel>
    /***********PTA cancel**************/
    @POST("pta_cancel")
    @Headers("Content-Type: application/json")
    fun pta_cancel(
        @Body  ptaCancelModel: PtaCancelApiModel,
        @Header("Authorization") token:String
    ): Call<PtaCancelModel>

    /***********staff directory**************/
    @POST("teaching_staff_list")
    @Headers("Content-Type: application/json")
    fun teaching_staff_list(
        @Body  teachingStaffListModel: TeachingStaffListApiModel,
        @Header("Authorization") token:String
    ): Call<TeachingStaffListModel>

    @POST("send_email_to_staff")
    @Headers("Content-Type: application/json")
    fun send_email_to_staff(
        @Body  sendEmailMail: SendStaffEmailApiModel,
        @Header("Authorization") token:String
    ): Call<SendStaffEmailModel>

    @POST("delete_account")
    @Headers("Content-Type: application/json")
    fun delete_account(
        @Header("Authorization") token:String
    ): Call<DeleteAccountModel>
    @GET("get_enrollment_status")
    @Headers("Content-Type: application/json")
    fun get_enrollment_status(
        //@Body  ptaConfirmationModel: PtaConfirmationApiModel,
        @Header("Authorization") token:String
    ): Call<EnrollmentStatusModel>

    @POST("re_enrollment_help")
    @Headers("Content-Type: application/json")
    fun re_enrollment_mailhelp(
        @Body  reEnrollMail: ReEnrollEmailApiModel,
        @Header("Authorization") token:String
    ): Call<ReEnrollEmailModel>

    //payment category
    @POST("get_payment_categories")
    @Headers("Content-Type: application/json")
    fun payment_categories(
        @Body  paymentCategories: PaymentCategoriesApiModel,
        @Header("Authorization") token:String
    ): Call<PayCategoryModel>

    //payment token
    @POST("network_payment_gateway_access_token")
    @Headers("Content-Type: application/json")
    fun payment_token(
        @Body  paymentCategories: PaymentTokenApiModel,
        @Header("Authorization") token:String
    ): Call<PaymentTokenModel>

    //payment gateway
    @POST("network_payment_gateway_creating_an_order")
    @Headers("Content-Type: application/json")
    fun payment_gateway(
        @Body  paymentGateway: PaymentGatewayApiModel,
        @Header("Authorization") token:String
    ): Call<PaymentGatewayModel>

    //payment success
    @POST("submit_payment")
    @Headers("Content-Type: application/json")
    fun submit_payment(
        @Body  paymentGateway: PaymentSubmitApiModel,
        @Header("Authorization") token:String
    ): Call<PaymentSubmitModel>

    @GET("get_canteen_categories")
    @Headers("Content-Type: application/json")
    fun get_canteen_categories(
        //@Body  ptaConfirmationModel: PtaConfirmationApiModel,
        @Header("Authorization") token:String
    ): Call<CatListModel>

    //canteen items
    @POST("get_canteen_items")
    @Headers("Content-Type: application/json")
    fun get_canteen_items(
        @Body  canteenItems: CanteenItemsApiModel,
        @Header("Authorization") token:String
    ): Call<ItemsListModel>

    @POST("add_to_canteen_cart")
    @Headers("Content-Type: application/json")
    fun add_to_canteen_cart(
        @Body  addToCartCanteen: AddToCartCanteenApiModel,
        @Header("Authorization") token:String
    ): Call<AddToCartCanteenModel>

    @POST("get_canteen_cart")
    @Headers("Content-Type: application/json")
    fun get_canteen_cart(
        @Body  canteenCart: CanteenCartApiModel,
        @Header("Authorization") token:String
    ): Call<CanteenCartModel>

    @POST("update_canteen_cart")
    @Headers("Content-Type: application/json")
    fun update_canteen_cart(
        @Body  updatecanteenCart: CanteenCartUpdateApiModel,
        @Header("Authorization") token:String
    ): Call<CanteenCartUpdateModel>

    @POST("remove_canteen_cart")
    @Headers("Content-Type: application/json")
    fun remove_canteen_cart(
        @Body  removecanteenCart: CanteenCartRemoveApiModel,
        @Header("Authorization") token:String
    ): Call<CanteenCartRemoveModel>

    @POST("get_wallet_balance")
    @Headers("Content-Type: application/json")
    fun get_wallet_balance(
        @Body  walletbalance: WalletBalanceApiModel,
        @Header("Authorization") token:String
    ): Call<WalletBalanceModel>

    @GET("time_exceed_status")
    @Headers("Content-Type: application/json")
    fun time_exceed_status(
        @Header("Authorization") token:String
    ): Call<TimeExceedModel>

    @POST("canteen_preorder")
    @Headers("Content-Type: application/json")
    fun canteen_preorder(
        @Body  canteenpreorder: CanteenPreorderApiModel,
        @Header("Authorization") token:String
    ): Call<CanteenPreorderModel>

    @POST("wallet_topup")
    @Headers("Content-Type: application/json")
    fun wallet_topup(
        @Body  walletamount: WalletAmountApiModel,
        @Header("Authorization") token:String
    ): Call<WalletAmountModel>

    @POST("get_wallet_history")
    @Headers("Content-Type: application/json")
    fun get_wallet_history(
        @Body  wallethistory: WalletHistoryApiModel,
        @Header("Authorization") token:String
    ): Call<WalletHistoryModel>


    // Aparna 17/02/2023

    @GET("canteen_banner")
    @Headers("Content-Type: application/json")
    fun get_canteen_banner(
        //@Body  ptaConfirmationModel: PtaConfirmationApiModel,
        @Header("Authorization") token:String
    ): Call<CanteenBannerResponseModel>

    @GET("payment_banner")
    @Headers("Content-Type: application/json")
    fun get_payment_banner(
        //@Body  ptaConfirmationModel: PtaConfirmationApiModel,
        @Header("Authorization") token:String
    ): Call<PaymentBannerResponseModel>


    @POST("get_canteen_preorder_history")
    @Headers("Content-Type: application/json")
    fun canteen_order_history(
        @Body  orderHistory: OrderHistoryApiModel,
        @Header("Authorization") token:String
    ): Call<OrderHistoryResponseModel>

    @POST("get_canteen_preorder")
    @Headers("Content-Type: application/json")
    fun canteen_myorder_history(
        @Body  orderHistory: OrderHistoryApiModel,
        @Header("Authorization") token:String
    ): Call<PreOrdersModel>

    @POST("cancel_canteen_preorder")
    @Headers("Content-Type: application/json")
    fun cancelCanteenPreOrder(
        @Body  orderHistory: CancelCanteenPreOrderApiModel,
        @Header("Authorization") token:String
    ): Call<CanteenPreorderModel>

    @POST("cancel_canteen_preorder_item")
    @Headers("Content-Type: application/json")
    fun cancelCanteenPreOrderItem(
        @Body  orderHistory: CancelCanteenPreorderItemId,
        @Header("Authorization") token:String
    ): Call<CanteenPreorderModel>

   @POST("edit_canteen_preorder_item")
    @Headers("Content-Type: application/json")
    fun updateCanteenPreOrderItem(
        @Body  orderHistory: UpdateCanteenPreorderItemApiModel,
        @Header("Authorization") token:String
    ): Call<CanteenPreorderModel>



    @GET("canteen_information")
    @Headers("Content-Type: application/json")
    fun getCanteenInformation(
        //@Body  ptaConfirmationModel: PtaConfirmationApiModel,
        @Header("Authorization") token:String
    ): Call<InfoCanteenModel>

    @GET("canteen_information")
    @Headers("Content-Type: application/json")
    fun getPaymentInformation(
        //@Body  ptaConfirmationModel: PtaConfirmationApiModel,
        @Header("Authorization") token:String
    ): Call<InfoCanteenModel>

    @GET("payment_information")
    @Headers("Content-Type: application/json")
    fun getPaymentInfo(
        //@Body  ptaConfirmationModel: PtaConfirmationApiModel,
        @Header("Authorization") token:String
    ): Call<InfoPaymentModel>


    @POST("send_support_email")
    @Headers("Content-Type: application/json")
    fun sendEmailCanteen(
        @Body  sendEmailMail: CanteenSendEmailApiModel,
        @Header("Authorization") token:String
    ): Call<SendStaffEmailModel>

    //Trips module

    @GET("trip_banner")
    @Headers("Content-Type: application/json")
    fun tripsBanner(
        @Header("Authorization") token:String
    ): Call<TripBannerResponse>

    @GET("trip_categories")
    @Headers("Content-Type: application/json")
    fun tripCategories(@Header("Authorization") token: String): Call<TripCategoriesResponseModel>

    @POST("trip_items")
    @Headers("Content-Type: application/json")
    fun tripList(
        @Header("Authorization") token: String?,
        @Body json: JsonObject?
    ): Call<TripListResponseModel>

    @POST("trip_items_detail")
    @Headers("Content-Type: application/json")
    fun tripDetail(
        @Header("Authorization") token: String?,
        @Body json: JsonObject?
    ): Call<TripDetailsResponseModel>

    @POST("trip_intention_submit")
    @Headers("Content-Type: application/json")
    fun tripIntentSubmit(
        @Header("Authorization") token: String?,
        @Body json: JsonObject?
    ): Call<GeneralSubmitResponseModel>

    @Multipart
    @POST("trip_document_submit")
    fun uploadDocuments(
        @Header("Authorization") token: String?,
        @Part("action") action: RequestBody?,
        @Part("trip_item_id") tripItemId: RequestBody?,
        @Part("student_id") studentId: RequestBody?,
        @Part("card_number") cardNumber: RequestBody?,
        @Part image:MultipartBody.Part?,
        @Part image2:MultipartBody.Part?
    ): Call<TripDocumentSubmitResponseModel>



    @Multipart
    @POST("trip_document_submit")
    fun uploadSingleDocument(
        @Header("Authorization") token: String?,
        @Part("action") action: RequestBody?,
        @Part("trip_item_id") tripItemId: RequestBody?,
        @Part("student_id") studentId: RequestBody?,
        @Part("card_number") cardNumber: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Call<TripDocumentSubmitResponseModel>

    @Multipart
    @POST("trip_document_submit")
    fun uploadPermissionSlip(
        @Header("Authorization") token: String?,
        @Part("action") action: RequestBody?,
        @Part("trip_item_id") tripItemId: RequestBody?,
        @Part("student_id") studentId: RequestBody?,
        @Part("card_number") cardNumber: RequestBody?,
        @Part image: MultipartBody.Part?
    ): Call<TripDocumentSubmitResponseModel>

    @POST("trip_history")
    @Headers("Content-Type: application/json")
    fun tripHistory(
        @Header("Authorization") token: String?,
        @Body json: JsonObject?
    ): Call<TripHistoryResponseModel>

    @POST("get_trip_consent")
    @Headers("Content-Type: application/json")
    fun tripConsent(
        @Header("Authorization") token: String?,
        @Body json: JsonObject?
    ): Call<TripConsentResponseModel>

    @POST("trip_max_students_count_check")
    @Headers("Content-Type: application/json")
    fun tripCountCheck(
        @Header("Authorization") token: String?,
        @Body json: JsonObject?
    ): Call<TripCountResponseModel>
    @POST("trip_informations")
    @Headers("Content-Type: application/json")
    fun tripInformation(
        @Header("Authorization") token: String
    ): Call<InfoPaymentModel>

    @POST("network_payment_gateway_access_token")
    @Headers("Content-Type: application/json")
    fun payment_token_trip(
        @Body  paymentCategories: PaymentTokenApiModel,
        @Header("Authorization") token:String
    ): Call<PaymentTokenModel>

    //payment gateway
    @POST("network_payment_gateway_creating_an_order")
    @Headers("Content-Type: application/json")
    fun payment_gateway_trip(
        @Body  paymentGateway: PaymentGatewayApiModelTrip,
        @Header("Authorization") token:String
    ): Call<TripPaymentInitiateResponseModel>

    @POST("trip_payment_submit")
    @Headers("Content-Type: application/json")
    fun paymentSubmit(
        @Header("Authorization") token: String?,
        @Body json: JsonObject?
    ): Call<TripPaymentSubmitModel>
    @POST("trip_document_submit")
    fun uploadmedicalDocuments(
        @Header("Authorization") token: String?,
        @Body json: JsonObject?
    ): Call<SubmitDocResponseModel>
    @POST("generate_trip_receipt")
    @Headers("Content-Type: application/json")
    fun tripReciept(
        @Header("Authorization") token: String?,
        @Body json: JsonObject?
    ): Call<TripInvoiceResponseModel>

    @POST("trip_choices_count")
    @Headers("Content-Type: application/json")
    fun tripChoicePreference(@Header("Authorization") token: String?): Call<TripChoicePreferenceResponseModel>
}