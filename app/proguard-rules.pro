# Keep JetBrains annotations
-keep class org.jetbrains.annotations.** { *; }

# Keep OkHttp classes (if still facing issues)
-keep class okhttp3.** { *; }
-keep class com.squareup.okhttp.** { *; }

# Keep Picasso classes (if needed)
-keep class com.squareup.picasso.** { *; }

# Keep Retrofit API interfaces
-keep interface * { *; }

# Keep Retrofit and OkHttp3 classes
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }

# Keep generic type arguments for Retrofit
-keepattributes Signature
-keepattributes *Annotation*

# Prevent obfuscation of class names for Gson serialization/deserialization
-keep class com.google.gson.** { *; }

# Keep fields of model classes
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Firebase
-dontwarn com.google.firebase.**
-keep class com.google.firebase.** { *; }
-keepattributes *Annotation*
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

# Play Services
-dontwarn com.google.android.gms.**
-keep class com.google.android.gms.** { *; }

# Glide
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep class com.bumptech.glide.** { *; }
-dontwarn com.bumptech.glide.**

# AndroidX Security Crypto
-keep class androidx.security.** { *; }
-dontwarn androidx.security.**

# BouncyCastle Security Libraries
-keep class org.bouncycastle.** { *; }
-dontwarn org.bouncycastle.**

# Conscrypt
-keep class org.conscrypt.** { *; }
-dontwarn org.conscrypt.**

# OpenJSSE
-keep class org.openjsse.** { *; }
-dontwarn org.openjsse.**

# Material Calendar View
-keep class com.applandeo.** { *; }
-dontwarn com.applandeo.**

# Android PDF Viewer
-keep class com.github.barteksc.pdfviewer.** { *; }
-dontwarn com.github.barteksc.pdfviewer.**

# PRDownloader
-keep class com.mindorks.android.prdownloader.** { *; }
-dontwarn com.mindorks.android.prdownloader.**

# ShortcutBadger
-keep class me.leolin.shortcutbadger.** { *; }
-dontwarn me.leolin.shortcutbadger.**

# Elegant Number Button
-keep class com.cepheuen.elegantnumberbutton.** { *; }
-dontwarn com.cepheuen.elegantnumberbutton.**

# RootBeer Library
-keep class com.scottyab.rootbeer.** { *; }
-dontwarn com.scottyab.rootbeer.**

# Tooltips Library
-keep class com.ryanharter.android.tooltips.** { *; }
-dontwarn com.ryanharter.android.tooltips.**

# Signature Pad
-keep class com.github.gcacace.signaturepad.** { *; }
-dontwarn com.github.gcacace.signaturepad.**

# Payment SDK (Network International)
-keep class com.network.** { *; }
-dontwarn com.network.**

# Samsung Payment SDK
-keep class com.network.samsungpay.** { *; }
-dontwarn com.network.samsungpay.**

# Mockito (For Testing)
-dontwarn org.mockito.**
-keep class org.mockito.** { *; }

# General Keep Rules
-keepattributes *Annotation*
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keepclassmembers class * {
    @androidx.annotation.Keep <methods>;
}

-keep class com.mobatia.bisad.activity.absence.model.EarlyPickupListModel { *; }
-keep class com.mobatia.bisad.activity.absence.model.EarlyPickupModel { *; }
-keep class com.mobatia.bisad.activity.absence.model.PickupListApiModel { *; }
-keep class com.mobatia.bisad.activity.absence.model.RequestAbsenceApiModel { *; }
-keep class com.mobatia.bisad.activity.absence.model.RequestPickupApiModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.AllergyContentModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.DateModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.TimeExceedModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.TimeExceedResModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.add_orders.CanteenItemsApiModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.add_orders.CategoryListModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.add_orders.CatItemsListModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.add_orders.CatListModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.add_orders.CatResponseModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.add_orders.ItemImageModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.add_orders.ItemsListModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.add_orders.ItemsResponseModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.add_to_cart.AddToCartCanteenApiModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.add_to_cart.AddToCartCanteenModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.add_to_cart.CanteenCartRemoveApiModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.add_to_cart.CanteenCartRemoveModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.add_to_cart.CanteenCartUpdateApiModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.add_to_cart.CanteenCartUpdateModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.canteen_cart.CanteenCartApiModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.canteen_cart.CanteenCartListmodel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.canteen_cart.CanteenCartModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.canteen_cart.CanteenCartResModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.canteen_cart.CanteenCartResponseModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.canteen_cart.CartItemsListModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.canteen_cart.ItemsModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.canteen_cart.OrdersModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.information.InfoCanteenModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.information.InfoListModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.information.InfoResponseModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.myorders.CancelCanteenPreOrderApiModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.myorders.CancelCanteenPreorderItemId { *; }
-keep class com.mobatia.bisad.activity.canteen.model.myorders.PreorderitemImage_list { *; }
-keep class com.mobatia.bisad.activity.canteen.model.myorders.Preorderitems_list { *; }
-keep class com.mobatia.bisad.activity.canteen.model.myorders.PreOrdersListModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.myorders.PreOrdersModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.myorders.PreOrdersResponseModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.myorders.UpdateCanteenPreorderItemApiModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.order_history.OrderCanteenPreOrderItems { *; }
-keep class com.mobatia.bisad.activity.canteen.model.order_history.OrderHistoryApiModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.order_history.OrderHistoryDataModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.order_history.OrderHistoryResponseModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.order_history.OrderHstoryResponseArrayModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.payment_history.FullPaymentListModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.payment_history.InstallmentListModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.payment_history.PayListModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.payment_history.PaymentHisModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.payment_history.PaymentHisResponseModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.payment_history.PaymentHistoryListModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.preorder.CanteenPreorderApiModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.preorder.CanteenPreorderModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.topup.WalletAmountApiModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.topup.WalletAmountModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.topup.WalletTopResModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.wallet.WalletBalanceApiModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.wallet.WalletBalanceModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.wallet.WalletResModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.wallethistory.CreditHisListModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.wallethistory.WalletHisResModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.wallethistory.WalletHistoryApiModel { *; }
-keep class com.mobatia.bisad.activity.canteen.model.wallethistory.WalletHistoryModel { *; }
-keep class com.mobatia.bisad.activity.common.model.DeviceRegModel { *; }
-keep class com.mobatia.bisad.activity.common.model.ForgetPasswordModel { *; }
-keep class com.mobatia.bisad.activity.common.model.LoginModel { *; }
-keep class com.mobatia.bisad.activity.common.model.SignUpModel { *; }
-keep class com.mobatia.bisad.activity.communication.letter.model.LetterResponseListModel { *; }
-keep class com.mobatia.bisad.activity.communication.letter.model.LetterResponseModel { *; }
-keep class com.mobatia.bisad.activity.communication.magazine.model.MagazineResponseListModel { *; }
-keep class com.mobatia.bisad.activity.communication.magazine.model.MagazineResponseModel { *; }
-keep class com.mobatia.bisad.activity.communication.newsletter.model.NewLetterListDetailModel { *; }
-keep class com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterDetailApiModel { *; }
-keep class com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterDetailModel { *; }
-keep class com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterDetailResponseModel { *; }
-keep class com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterListAPiModel { *; }
-keep class com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterListModel { *; }
-keep class com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterResponseModel { *; }
-keep class com.mobatia.bisad.activity.home.model.DataCollectionSubmissionModel { *; }
-keep class com.mobatia.bisad.activity.home.model.HealthInsuranceDetailAPIModel { *; }
-keep class com.mobatia.bisad.activity.message.model.MessageDetailApiModel { *; }
-keep class com.mobatia.bisad.activity.message.model.MessageDetailModel { *; }
-keep class com.mobatia.bisad.activity.message.model.MessageDetailNotificationModel { *; }
-keep class com.mobatia.bisad.activity.message.model.MessageDetailResponseModel { *; }
-keep class com.mobatia.bisad.activity.parent_meetings.model.cancel.PtaCancelApiModel { *; }
-keep class com.mobatia.bisad.activity.parent_meetings.model.cancel.PtaCancelModel { *; }
-keep class com.mobatia.bisad.activity.parent_meetings.model.confirm_pta.PtaConfirmationApiModel { *; }
-keep class com.mobatia.bisad.activity.parent_meetings.model.confirm_pta.PtaConfirmationModel { *; }
-keep class com.mobatia.bisad.activity.parent_meetings.model.insert_pta.PtaInsertApiModel { *; }
-keep class com.mobatia.bisad.activity.parent_meetings.model.insert_pta.PtaInsertModel { *; }
-keep class com.mobatia.bisad.activity.parent_meetings.model.insert_pta.ResponseInsertPta { *; }
-keep class com.mobatia.bisad.activity.parent_meetings.model.listing_pta.PtaListingApiModel { *; }
-keep class com.mobatia.bisad.activity.parent_meetings.model.listing_pta.PtaListingModel { *; }
-keep class com.mobatia.bisad.activity.parent_meetings.model.listing_pta.PtaListingResponse { *; }
-keep class com.mobatia.bisad.activity.parent_meetings.model.listing_pta.PtaTimeSlotList { *; }
-keep class com.mobatia.bisad.activity.parent_meetings.model.review_list.PtaReviewListModel { *; }
-keep class com.mobatia.bisad.activity.parent_meetings.model.review_list.ResponseReviewPta { *; }
-keep class com.mobatia.bisad.activity.parent_meetings.model.review_list.ReviewListModel { *; }
-keep class com.mobatia.bisad.activity.parent_meetings.model.AllotedDatesResponseModel { *; }
-keep class com.mobatia.bisad.activity.parent_meetings.model.PtaAllottedDatesApiModel { *; }
-keep class com.mobatia.bisad.activity.parent_meetings.model.PtaAllottedDatesModel { *; }
-keep class com.mobatia.bisad.activity.payment.model.InfoPayListModel { *; }
-keep class com.mobatia.bisad.activity.payment.model.InfoPaymentModel { *; }
-keep class com.mobatia.bisad.activity.payment.model.InfoPayResponseModel { *; }
-keep class com.mobatia.bisad.activity.payment.model.PayCatDataList { *; }
-keep class com.mobatia.bisad.activity.payment.model.PayCategoryModel { *; }
-keep class com.mobatia.bisad.activity.payment.model.PayCatListResModel { *; }
-keep class com.mobatia.bisad.activity.payment.model.PaymentCategoriesApiModel { *; }
-keep class com.mobatia.bisad.activity.payment.model.PaymentCategoryListModel { *; }
-keep class com.mobatia.bisad.activity.payment.model.payment_gateway.PayGateResModel { *; }
-keep class com.mobatia.bisad.activity.payment.model.payment_gateway.PayGatewayData { *; }
-keep class com.mobatia.bisad.activity.payment.model.payment_gateway.PaymentGatewayApiModel { *; }
-keep class com.mobatia.bisad.activity.payment.model.payment_gateway.PaymentGatewayModel { *; }
-keep class com.mobatia.bisad.activity.payment.model.payment_submit.PaymentSubmitApiModel { *; }
-keep class com.mobatia.bisad.activity.payment.model.payment_submit.PaymentSubmitModel { *; }
-keep class com.mobatia.bisad.activity.payment.model.payment_submit.PaySuccDetailResModel { *; }
-keep class com.mobatia.bisad.activity.payment.model.payment_token.PaymentTokenApiModel { *; }
-keep class com.mobatia.bisad.activity.payment.model.payment_token.PaymentTokenModel { *; }
-keep class com.mobatia.bisad.activity.payment.model.payment_token.PayTokenResModel { *; }
-keep class com.mobatia.bisad.activity.permission_slip.model.PermissionResApiModel { *; }
-keep class com.mobatia.bisad.activity.permission_slip.model.PermissionResponseModel { *; }
-keep class com.mobatia.bisad.activity.school_trips.model.ChoicePreferenceModel { *; }
-keep class com.mobatia.bisad.activity.school_trips.model.PaymentGatewayApiModelTrip { *; }
-keep class com.mobatia.bisad.activity.school_trips.model.SubmitDocResponseModel { *; }
-keep class com.mobatia.bisad.activity.school_trips.model.TripChoicePreferenceResponseModel { *; }
-keep class com.mobatia.bisad.activity.school_trips.model.TripConsentResponseModel { *; }
-keep class com.mobatia.bisad.activity.school_trips.model.TripCountResponseModel { *; }
-keep class com.mobatia.bisad.activity.school_trips.model.TripDetailsResponseModel { *; }
-keep class com.mobatia.bisad.activity.school_trips.model.TripDocumentSubmitResponseModel { *; }
-keep class com.mobatia.bisad.activity.school_trips.model.TripHistoryResponseModel { *; }
-keep class com.mobatia.bisad.activity.school_trips.model.TripInvoiceResponseModel { *; }
-keep class com.mobatia.bisad.activity.school_trips.model.TripListResponseModel { *; }
-keep class com.mobatia.bisad.activity.school_trips.model.TripPaymentInitiateResponseModel { *; }
-keep class com.mobatia.bisad.activity.school_trips.model.TripPaymentSubmitModel { *; }
-keep class com.mobatia.bisad.activity.settings.re_enrollment.model.EnrollmentStatusModel { *; }
-keep class com.mobatia.bisad.activity.settings.re_enrollment.model.ResponseArrayStatus { *; }
-keep class com.mobatia.bisad.activity.settings.re_enrollment.model.StudentsEnrollList { *; }
-keep class com.mobatia.bisad.activity.settings.termsofservice.model.TermsOfServiceDetailModel { *; }
-keep class com.mobatia.bisad.activity.settings.termsofservice.model.TermsOfServiceModel { *; }
-keep class com.mobatia.bisad.activity.settings.termsofservice.model.TermsResponseArray { *; }
-keep class com.mobatia.bisad.activity.staff_directory.model.StaffDetailsModel { *; }
-keep class com.mobatia.bisad.activity.term_dates.model.TermDateDetailsResponseArray { *; }
-keep class com.mobatia.bisad.activity.term_dates.model.TermDatesDetailApiModel { *; }
-keep class com.mobatia.bisad.activity.term_dates.model.TermDatesDetailArrayList { *; }
-keep class com.mobatia.bisad.activity.term_dates.model.TermDatesDetailModel { *; }
-keep class com.mobatia.bisad.fragment.apps.model.AppsApiModel { *; }
-keep class com.mobatia.bisad.fragment.apps.model.AppsListDetailModel { *; }
-keep class com.mobatia.bisad.fragment.apps.model.AppsListModel { *; }
-keep class com.mobatia.bisad.fragment.apps.model.AppsResponseArrayModel { *; }
-keep class com.mobatia.bisad.fragment.attendance.model.AttendanceApiModel { *; }
-keep class com.mobatia.bisad.fragment.attendance.model.AttendanceListDetailModel { *; }
-keep class com.mobatia.bisad.fragment.attendance.model.AttendanceResponseArray { *; }
-keep class com.mobatia.bisad.fragment.attendance.model.Dates { *; }
-keep class com.mobatia.bisad.fragment.calendar_new.model.Cal { *; }
-keep class com.mobatia.bisad.fragment.calendar_new.model.CalendarCalModel { *; }
-keep class com.mobatia.bisad.fragment.calendar_new.model.CalendarDateModel { *; }
-keep class com.mobatia.bisad.fragment.calendar_new.model.CalendarDetailModel{ *; }
-keep class com.mobatia.bisad.fragment.calendar_new.model.CategoryModel { *; }
-keep class com.mobatia.bisad.fragment.calendar_new.model.CalendarModel { *; }
-keep class com.mobatia.bisad.fragment.calendar_new.model.CalendarResponseArray { *; }
-keep class com.mobatia.bisad.fragment.calendar_new.model.PrimaryModel { *; }
-keep class com.mobatia.bisad.fragment.calendar_new.model.VEVENT { *; }
-keep class com.mobatia.bisad.fragment.canteen.model.CanteenBannerDataModel { *; }
-keep class com.mobatia.bisad.fragment.canteen.model.CanteenBannerResponseArrayModel { *; }
-keep class com.mobatia.bisad.fragment.canteen.model.CanteenBannerResponseModel { *; }
-keep class com.mobatia.bisad.fragment.canteen.model.CanteenSendEmailApiModel { *; }
-keep class com.mobatia.bisad.fragment.contact_us.model.ContactsListDetailModel { *; }
-keep class com.mobatia.bisad.fragment.contact_us.model.ContactusModel { *; }
-keep class com.mobatia.bisad.fragment.contact_us.model.ContactusResponseArray { *; }
-keep class com.mobatia.bisad.fragment.curriculum.model.CuriculumListModel { *; }
-keep class com.mobatia.bisad.fragment.curriculum.model.CuriculumResponseArray { *; }
-keep class com.mobatia.bisad.fragment.curriculum.model.CurriculumStudentApiModel { *; }
-keep class com.mobatia.bisad.fragment.forms.model.FormsResponseArrayDetail { *; }
-keep class com.mobatia.bisad.fragment.forms.model.FormsResponseModel { *; }

-keep class com.mobatia.bisad.fragment.home.model.datacollection.DataCollectionModel { *; }
-keep class com.mobatia.bisad.fragment.home.model.datacollection.DataCollectionResponseArray { *; }
-keep class com.mobatia.bisad.fragment.home.model.datacollection.HealthInsuranceDetailModel { *; }
-keep class com.mobatia.bisad.fragment.home.model.datacollection.KinDetailApiModel { *; }
-keep class com.mobatia.bisad.fragment.home.model.datacollection.KinDetailsModel { *; }
-keep class com.mobatia.bisad.fragment.home.model.datacollection.OwnContactModel { *; }
-keep class com.mobatia.bisad.fragment.home.model.datacollection.OwnDetailsModel { *; }
-keep class com.mobatia.bisad.fragment.home.model.datacollection.PassportApiModel { *; }
-keep class com.mobatia.bisad.fragment.home.model.datacollection.PassportDetailModel { *; }
-keep class com.mobatia.bisad.fragment.home.model.reenrollment.GetreenrollmentApiModel { *; }
-keep class com.mobatia.bisad.fragment.home.model.reenrollment.GetreenrollmentModel { *; }
-keep class com.mobatia.bisad.fragment.home.model.reenrollment.ReEnrolldetail { *; }
-keep class com.mobatia.bisad.fragment.home.model.reenrollment.ReEnrollEmailApiModel { *; }
-keep class com.mobatia.bisad.fragment.home.model.reenrollment.ReEnrollEmailModel { *; }
-keep class com.mobatia.bisad.fragment.home.model.reenrollment.ReEnrollOptionList { *; }
-keep class com.mobatia.bisad.fragment.home.model.reenrollment.ReEnrollSaveModel { *; }
-keep class com.mobatia.bisad.fragment.home.model.reenrollment.ReEnrollSubModel { *; }
-keep class com.mobatia.bisad.fragment.home.model.reenrollment.ResponseArray { *; }
-keep class com.mobatia.bisad.fragment.home.model.reenrollment.SaveReenrollmentApiModel { *; }
-keep class com.mobatia.bisad.fragment.home.model.reenrollment.SavereenrollmentModel { *; }
-keep class com.mobatia.bisad.fragment.home.model.reenrollment.SettingsData { *; }
-keep class com.mobatia.bisad.fragment.home.model.reenrollment.StudentReEnrollList { *; }
-keep class com.mobatia.bisad.fragment.home.model.BannerModel { *; }
-keep class com.mobatia.bisad.fragment.home.model.CountryiesDetailModel { *; }
-keep class com.mobatia.bisad.fragment.home.model.CountryListModel { *; }
-keep class com.mobatia.bisad.fragment.home.model.CountryResponseArray { *; }
-keep class com.mobatia.bisad.fragment.home.model.ReEnrollDropDown { *; }
-keep class com.mobatia.bisad.fragment.home.model.RelationShipDetailModel { *; }
-keep class com.mobatia.bisad.fragment.home.model.RelationShipResponseArray { *; }
-keep class com.mobatia.bisad.fragment.home.model.StudentListDataCollection { *; }
-keep class com.mobatia.bisad.fragment.home.model.TilesListModel { *; }
-keep class com.mobatia.bisad.fragment.home.model.TilesResponseArray { *; }
-keep class com.mobatia.bisad.fragment.home.model.TitlesArrayList { *; }
-keep class com.mobatia.bisad.fragment.home.model.RelationShipListModel { *; }
-keep class com.mobatia.bisad.fragment.messages.model.MessageListApiModel { *; }
-keep class com.mobatia.bisad.fragment.messages.model.MessageListDetailModel { *; }
-keep class com.mobatia.bisad.fragment.messages.model.MessageListModel { *; }
-keep class com.mobatia.bisad.fragment.messages.model.MessageResponseArrayModel { *; }
-keep class com.mobatia.bisad.fragment.payment.model.PaymentBannerDataModel { *; }
-keep class com.mobatia.bisad.fragment.payment.model.PaymentBannerResponseArrayModel { *; }
-keep class com.mobatia.bisad.fragment.payment.model.PaymentBannerResponseModel { *; }
-keep class com.mobatia.bisad.fragment.permission_slip.model.PermissionSlipListApiModel { *; }
-keep class com.mobatia.bisad.fragment.permission_slip.model.PermissionSlipListModel { *; }
-keep class com.mobatia.bisad.fragment.permission_slip.model.PermissionSlipModel { *; }
-keep class com.mobatia.bisad.fragment.permission_slip.model.PermissionSlipResponseArray { *; }
-keep class com.mobatia.bisad.fragment.report_absence.model.AbsenceLeaveApiModel { *; }
-keep class com.mobatia.bisad.fragment.report_absence.model.AbsenceListModel { *; }
-keep class com.mobatia.bisad.fragment.report_absence.model.AbsenceRequestListDetailModel { *; }
-keep class com.mobatia.bisad.fragment.report_absence.model.AbsenceResponseArray { *; }
-keep class com.mobatia.bisad.fragment.report_absence.model.EarlyPickupListSortModel { *; }
-keep class com.mobatia.bisad.fragment.report_absence.model.EarlyPickupResponseArray { *; }
-keep class com.mobatia.bisad.fragment.report_absence.model.PickupModel { *; }
-keep class com.mobatia.bisad.fragment.reports.model.ReportApiModel { *; }
-keep class com.mobatia.bisad.fragment.reports.model.ReportListDetailModel { *; }
-keep class com.mobatia.bisad.fragment.reports.model.ReportListModel { *; }
-keep class com.mobatia.bisad.fragment.reports.model.ReportResponseArray { *; }
-keep class com.mobatia.bisad.fragment.school_trips.model.TripBannerResponse { *; }
-keep class com.mobatia.bisad.fragment.school_trips.model.TripCategoriesResponseModel { *; }
-keep class com.mobatia.bisad.fragment.school_trips.model.TripsBannerResponseModel { *; }
-keep class com.mobatia.bisad.fragment.settings.model.ChangePasswordApiModel { *; }
-keep class com.mobatia.bisad.fragment.settings.model.DeleteAccountModel { *; }
-keep class com.mobatia.bisad.fragment.settings.model.TriggerDataModel { *; }
-keep class com.mobatia.bisad.fragment.settings.model.TriggerUSer { *; }
-keep class com.mobatia.bisad.fragment.socialmedia.model.SocialMediaDetailModel { *; }
-keep class com.mobatia.bisad.fragment.socialmedia.model.SocialMediaListModel { *; }
-keep class com.mobatia.bisad.fragment.socialmedia.model.SocialMediaResponseArray { *; }
-keep class com.mobatia.bisad.fragment.staff_directory.model.Cat_model { *; }
-keep class com.mobatia.bisad.fragment.staff_directory.model.ResponseTeacherList { *; }
-keep class com.mobatia.bisad.fragment.staff_directory.model.SendStaffEmailApiModel { *; }
-keep class com.mobatia.bisad.fragment.staff_directory.model.SendStaffEmailModel { *; }
-keep class com.mobatia.bisad.fragment.staff_directory.model.StaffCatList { *; }
-keep class com.mobatia.bisad.fragment.staff_directory.model.StaffDetailList { *; }
-keep class com.mobatia.bisad.fragment.staff_directory.model.TeachingStaffListApiModel { *; }
-keep class com.mobatia.bisad.fragment.staff_directory.model.TeachingStaffListModel { *; }
-keep class com.mobatia.bisad.fragment.student_information.model.ResponseArray { *; }
-keep class com.mobatia.bisad.fragment.student_information.model.StudentInfoApiModel { *; }
-keep class com.mobatia.bisad.fragment.student_information.model.StudentInfoDetail { *; }
-keep class com.mobatia.bisad.fragment.student_information.model.StudentInfoModel { *; }
-keep class com.mobatia.bisad.fragment.student_information.model.StudentInfoResponseArray { *; }
-keep class com.mobatia.bisad.fragment.student_information.model.StudentList { *; }
-keep class com.mobatia.bisad.fragment.student_information.model.StudentListModel { *; }
-keep class com.mobatia.bisad.fragment.teacher_contact.model.SendStaffMailApiModel { *; }
-keep class com.mobatia.bisad.fragment.teacher_contact.model.StaffInfoDetail { *; }
-keep class com.mobatia.bisad.fragment.teacher_contact.model.StaffListModel { *; }
-keep class com.mobatia.bisad.fragment.teacher_contact.model.StaffListApiModel { *; }
-keep class com.mobatia.bisad.fragment.teacher_contact.model.StaffResponseArray { *; }
-keep class com.mobatia.bisad.fragment.termdates.model.TermDateResponseArray { *; }
-keep class com.mobatia.bisad.fragment.termdates.model.TermDatesApiModel { *; }
-keep class com.mobatia.bisad.fragment.termdates.model.TermDatesListDetailModel { *; }
-keep class com.mobatia.bisad.fragment.termdates.model.TermDatesListModel { *; }
-keep class com.mobatia.bisad.fragment.time_table.model.apimodel.FieldApiListModel { *; }
-keep class com.mobatia.bisad.fragment.time_table.model.apimodel.RangeApiModel { *; }
-keep class com.mobatia.bisad.fragment.time_table.model.apimodel.TimeTableApiDataModel { *; }
-keep class com.mobatia.bisad.fragment.time_table.model.apimodel.TimeTableApiListModel { *; }
-keep class com.mobatia.bisad.fragment.time_table.model.apimodel.TimeTableApiModel { *; }
-keep class com.mobatia.bisad.fragment.time_table.model.apimodel.TimeTableResponseArray { *; }
-keep class com.mobatia.bisad.fragment.time_table.model.usagemodel.DayModel { *; }
-keep class com.mobatia.bisad.fragment.time_table.model.usagemodel.FieldModel { *; }
-keep class com.mobatia.bisad.fragment.time_table.model.usagemodel.PeriodModel { *; }
-keep class com.mobatia.bisad.fragment.time_table.model.usagemodel.RangeModel { *; }
-keep class com.mobatia.bisad.fragment.time_table.model.usagemodel.TimeTableModel { *; }
-keep class com.mobatia.bisad.fragment.time_table.model.usagemodel.WeekModel { *; }





































































































































































































































































