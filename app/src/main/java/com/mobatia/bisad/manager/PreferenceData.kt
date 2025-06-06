package com.mobatia.bisad.manager

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.model.HealthInsuranceDetailAPIModel
import com.mobatia.bisad.fragment.home.model.CountryiesDetailModel
import com.mobatia.bisad.fragment.home.model.RelationShipDetailModel
import com.mobatia.bisad.fragment.home.model.StudentListDataCollection
import com.mobatia.bisad.fragment.home.model.TitlesArrayList
import com.mobatia.bisad.fragment.home.model.datacollection.KinDetailApiModel
import com.mobatia.bisad.fragment.home.model.datacollection.OwnContactModel
import com.mobatia.bisad.fragment.home.model.datacollection.PassportApiModel
import java.io.IOException
import java.lang.reflect.Type
import java.security.GeneralSecurityException


@Suppress("DEPRECATION")
class PreferenceData {
    private val PREFSNAME = "encrypted_bisad_prefs"


    /**
     * SET ACCESS TOKEN
     */

    private fun getEncryptedSharedPreferences(context: Context): SharedPreferences? {
        try {
            val masterKeyAlias: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

            return EncryptedSharedPreferences.create(
                PreferenceData().PREFSNAME,
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: GeneralSecurityException) {
            throw RuntimeException("Failed to create encrypted shared preferences", e)
        } catch (e: IOException) {
            throw RuntimeException("Failed to create encrypted shared preferences", e)
        }
    }

    fun setaccesstoken(context: Context, id: String?) {
        val prefs: SharedPreferences = PreferenceData().getEncryptedSharedPreferences(context)!!
        val editor = prefs.edit()
        editor.putString("access_token", id)
        editor.apply()
    }


    /**
     * GET ACCESS TOKEN
     */

    fun getaccesstoken(context: Context): String? {
        val prefs: SharedPreferences = PreferenceData().getEncryptedSharedPreferences(context)!!

        return prefs.getString("access_token", "")
    }

    fun setreenrollvalue(context:Context,value:String){
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("value", value)
        editor.apply()
    }
    fun getreenrollvalue(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("value", "")
    }
    /**
     * SET BUTTON ONE TAB ID
     */

    fun setbuttononetabid(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_onetabid", id)
        editor.apply()
    }

    /**
     * GET BUTTON ONE TAB ID
     */

    fun getbuttononetabid(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_onetabid", "1")
    }

    fun setbuttontwotabid(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_twotabid", id)
        editor.apply()
    }

    /**
     * GET BUTTON TWO TAB ID
     */

    fun getbuttontwotabid(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_twotabid", "4")
    }

    fun setbuttonthreetabid(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_threetabid", id)
        editor.apply()
    }

    /**
     * GET BUTTON THREE TAB ID
     */

    fun getbuttonthreetabid(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_threetabid", "14")
    }

    fun setbuttonfourtabid(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_fourtabid", id)
        editor.apply()
    }

    /**
     * GET BUTTON FOUR TAB ID
     */

    fun getbuttonfourtabid(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_fourtabid", "15")
    }

    fun setbuttonfivetabid(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_fivetabid", id)
        editor.apply()
    }

    /**
     * GET BUTTON FIVE TAB ID
     */

    fun getbuttonfivetabid(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_fivetabid", "2")
    }

    fun setbuttonsixtabid(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_sixtabid", id)
        editor.apply()
    }

    /**
     * GET BUTTON SIX TAB ID
     */

    fun getbuttonsixtabid(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_sixtabid", "3")
    }

    fun setbuttonseventabid(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_seventabid", id)
        editor.apply()
    }

    /**
     * GET BUTTON SEVEN TAB ID
     */

    fun getbuttonseventabid(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_seventabid", "8")
    }

    fun setbuttoneighttabid(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_eighttabid", id)
        editor.apply()
    }

    /**
     * GET BUTTON EIGHT TAB ID
     */

    fun getbuttoneighttabid(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_eighttabid", "6")
    }

    fun setbuttonninetabid(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_ninetabid", id)
        editor.apply()
    }

    /**
     * GET BUTTON NINE TAB ID
     */

    fun getbuttonninetabid(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_ninetabid", "16")
    }


    /**
     * SET BUTTON ONE TEXT IMAGE
     */

    fun setbuttononetextimage(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_onetextimage", id)
        editor.apply()
    }

    /**
     * GET BUTTON ONE TEXT IMAGE
     */

    fun getbuttononetextimage(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_onetextimage", "1")
    }

    fun setbuttontwotextimage(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_twotextimage", id)
        editor.apply()
    }

    /**
     * GET BUTTON TWO TEXT IMAGE
     */

    fun getbuttontwotextimage(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_twotextimage", "4")
    }

    fun setbuttonthreetextimage(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_threetextimage", id)
        editor.apply()
    }

    /**
     * GET BUTTON THREE TEXT IMAGE
     */

    fun getbuttonthreetextimage(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_threetextimage", "14")
    }

    fun setbuttonfourtextimage(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_fourtextimage", id)
        editor.apply()
    }

    /**
     * GET BUTTON FOUR TEXT IMAGE
     */

    fun getbuttonfourtextimage(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_fourtextimage", "15")
    }

    fun setbuttonfivetextimage(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_fivetextimage", id)
        editor.apply()
    }

    /**
     * GET BUTTON FIVE TEXT IMAGE
     */

    fun getbuttonfivetextimage(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_fivetextimage", "2")
    }

    fun setbuttonsixtextimage(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_sixtextimage", id)
        editor.apply()
    }

    /**
     * GET BUTTON SIX TEXT IMAGE
     */

    fun getbuttonsixtextimage(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_sixtextimage", "3")
    }

    fun setbuttonseventextimage(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_seventextimage", id)
        editor.apply()
    }

    /**
     * GET BUTTON SEVEN TEXT IMAGE
     */

    fun getbuttonseventextimage(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_seventextimage", "8")
    }

    fun setbuttoneighttextimage(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_eighttextimage", id)
        editor.apply()
    }

    /**
     * GET BUTTON EIGHT TEXT IMAGE
     */

    fun getbuttoneighttextimage(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_eighttextimage", "6")
    }

    fun setbuttonninetextimage(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("button_ninetextimage", id)
        editor.apply()
    }

    /**
     * GET BUTTON NINE TEXT IMAGE
     */

    fun getbuttonninetextimage(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("button_ninetextimage", "16")
    }

    fun setButtonOneGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("buttononeguestbg", color)
        editor.apply()
    }

    fun getButtonOneGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(
            "buttononeguestbg", context.resources
                .getColor(R.color.transparent)
        )
    }

    fun setButtontwoGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("buttontwoguestbg", color)
        editor.apply()
    }

    fun getButtontwoGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(
            "buttontwoguestbg", context.resources
                .getColor(R.color.transparent)
        )
    }

    fun setButtonthreeGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("buttonthreeguestbg", color)
        editor.apply()
    }

    fun getButtonthreeGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(
            "buttonthreeguestbg", context.resources
                .getColor(R.color.transparent)
        )
    }

    fun setButtonfourGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("buttonfourguestbg", color)
        editor.apply()
    }

    fun getButtonfourGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(
            "buttonfourguestbg", context.resources
                .getColor(R.color.transparent)
        )
    }

    fun setButtonfiveGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("buttonfiveguestbg", color)
        editor.apply()
    }

    fun getButtonfiveGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(
            "buttonfiveguestbg", context.resources
                .getColor(R.color.transparent)
        )
    }

    fun setButtonsixGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("buttonsixguestbg", color)
        editor.apply()
    }

    fun getButtonsixGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(
            "buttonsixguestbg", context.resources
                .getColor(R.color.transparent)
        )
    }

    fun setButtonsevenGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("buttonsevenguestbg", color)
        editor.apply()
    }

    fun getButtonsevenGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(
            "buttonsevenguestbg", context.resources
                .getColor(R.color.transparent)
        )
    }

    fun setButtoneightGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("buttoneightguestbg", color)
        editor.apply()
    }

    fun getButtoneightGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(
            "buttoneightguestbg", context.resources
                .getColor(R.color.transparent)
        )
    }

    fun setButtonnineGuestBg(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("buttonnineguestbg", color)
        editor.apply()
    }

    fun getButtonnineGuestBg(context: Context): Int {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(
            "buttonnineguestbg", context.resources
                .getColor(R.color.transparent)
        )
    }

    //Login user check

    /**
     * SET ACCESS TOKEN
     */

    fun setUserID(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("user_id", id)
        editor.apply()
    }


    fun getUserID(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("user_id", "")
    }
    fun setUserEmail(context: Context, email: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("email", email)
        editor.apply()
    }


    fun getUserEmail(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("email", "")
    }

    /*SET USER CODE*/
    fun setUserCode(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("user_code", id)
        editor.apply()
    }

    /*GET USER CODE*/
    fun getUserCode(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("user_code", "")
    }

    /*SET FIRE BASE ID*/
    fun setFcmID(context: Context, id: String) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("firebase id", id)
        editor.apply()
    }

    /*GET FIREBASE ID*/
    fun getFcmID(context: Context): String {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("firebase id", "").toString()
    }

    /*SET STUDENT_ID*/
    fun setStudentID(context: Context, id: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("student_id", id)
        editor.apply()
    }

    /*GET STUDENT_ID*/
    fun getStudentID(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("student_id", "")
    }
    /*SET STUDENT_NAME*/
    fun setStudentName(context: Context, name: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("student_name", name)
        editor.apply()
    }

    /*GET STUDENT_NAME*/
    fun getStudentName(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("student_name", "")
    }
    /*SET STUDENT_PHOTO*/
    fun setStudentPhoto(context: Context, photo: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("student_img", photo)
        editor.apply()
    }

    /*GET STUDENT_NAME*/
    fun getStudentPhoto(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("student_img", "")
    }
    /*SET STUDENT_PHOTO*/
    fun setStudentClass(context: Context, studClass: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("student_class", studClass)
        editor.apply()
    }

    /*GET STUDENT_NAME*/
    fun getStudentClass(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("student_class", "")
    }


    /*SET APP VERSION*/
    fun setAppVersion(context: Context, appVersion: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("app_version", appVersion)
        editor.apply()
    }

    /*GET APP VERSION*/
    fun getAppVersion(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("app_version", "")
    }

    /*SET DATA COLLECTION*/
    fun setDataCollection(context: Context, dataCollection: Int) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("data_collection", dataCollection)
        editor.apply()
    }

    /*GET DATA COLLECTION*/
    fun getDataCollection(context: Context): Int {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt("data_collection", 0)
    }

    /*SET DATA COLLECTION*/
    fun setDataCollectionShown(context: Context, dataCollection: Int) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("data_collection_shown", dataCollection)
        editor.apply()
    }

    /*GET DATA COLLECTION*/
    fun getDataCollectionShown(context: Context): Int {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt("data_collection_shown", 0)
    }

    /*SET TRIGGER TYPE*/
    fun setTriggerType(context: Context, triggerType: Int) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("trigger_type", triggerType)
        editor.apply()
    }

    /*GET TRIGGER TYPE*/
    fun getTriggerType(context: Context): Int {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt("trigger_type", 0)
    }

    /*SET ALREADY TRIGGERED*/
    fun setAlreadyTriggered(context: Context, alreadyTrigger: Int) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("already_triggered", alreadyTrigger)
        editor.apply()
    }

    /*GET ALREADY TRIGGERED*/
    fun getAlreadyTriggered(context: Context): Int {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt("already_triggered", 0)
    }

    /*SET ALREADY TRIGGERED*/
    fun setCountryArrayList(context: Context, countryArrayList: ArrayList<CountryiesDetailModel>) {
        val prefs = context.getSharedPreferences(PREFSNAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(countryArrayList)
        editor.putString("country_array", json)
        editor.apply()
    }
    /*GET ALREADY TRIGGERED*/
    fun getCountryArrayList(context: Context): ArrayList<CountryiesDetailModel> {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        val json = prefs.getString("country_array", null)
        val type = object : TypeToken<java.util.ArrayList<CountryiesDetailModel?>?>() {}.type
        return  gson.fromJson(json,type)
    }

    /*SET ALREADY TRIGGERED*/
    fun setRelationShipArrayList(context: Context, countryArrayList: ArrayList<RelationShipDetailModel>) {
        val prefs = context.getSharedPreferences(PREFSNAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(countryArrayList)
        editor.putString("relationship", json)
        editor.apply()
    }
    /*GET ALREADY TRIGGERED*/
    fun getRelationShipArrayList(context: Context): ArrayList<RelationShipDetailModel> {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        val json = prefs.getString("relationship", null)
        val type = object : TypeToken<java.util.ArrayList<RelationShipDetailModel?>?>() {}.type
        return  gson.fromJson(json,type)
    }
    /*SET ALREADY TRIGGERED*/
    fun setTitleArrayList(context: Context, titleArrayList: ArrayList<TitlesArrayList>) {
        val prefs = context.getSharedPreferences(PREFSNAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(titleArrayList)
        editor.putString("title_array", json)
        editor.apply()
    }
    /*GET ALREADY TRIGGERED*/
    fun getTitleArrayList(context: Context): ArrayList<TitlesArrayList> {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        val json = prefs.getString("title_array", "")
        val type = object : TypeToken<java.util.ArrayList<TitlesArrayList?>?>() {}.type
        return gson.fromJson(json,type)
    }
    /*SET ALREADY TRIGGERED*/
    fun setStudentArrayList(context: Context, studentArrayList: ArrayList<StudentListDataCollection>) {
        val prefs = context.getSharedPreferences(PREFSNAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(studentArrayList)
        editor.putString("student_array", json)
        editor.apply()
    }
    /*GET ALREADY TRIGGERED*/
    fun getStudentArrayList(context: Context): ArrayList<StudentListDataCollection> {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        val json = prefs.getString("student_array", null)
        val type = object : TypeToken<java.util.ArrayList<StudentListDataCollection?>?>() {}.type
        return gson.fromJson(json,type)
    }

    /*SET OWN DETAIL ARRAYLIST*/
    fun setOwnContactDetailArrayList(context: Context,ownContactDetailArrayList: ArrayList<OwnContactModel>) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(ownContactDetailArrayList)
        editor.putString("own_contact", json)
        editor.apply()
    }


    fun getOwnContactDetailArrayList(context: Context): ArrayList<OwnContactModel>? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = Gson()
        val json = prefs.getString("own_contact", null)
        val type: Type = object : TypeToken<ArrayList<OwnContactModel>?>() {}.type
        return gson.fromJson(json, type)
    }
    /*SET OWN DETAIL ARRAYLIST*/
    fun setKinDetailArrayList(context: Context,ownContactDetailArrayList: ArrayList<KinDetailApiModel>) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(ownContactDetailArrayList)
        editor.putString("kin_detail", json)
        editor.apply()
    }


    fun getKinDetailArrayList(context: Context): ArrayList<KinDetailApiModel>? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = Gson()
        val json = prefs.getString("kin_detail", null)
        val type: Type = object : TypeToken<ArrayList<KinDetailApiModel>?>() {}.type
        return gson.fromJson(json, type)
    }
    /*SET OWN DETAIL ARRAYLIST*/
    fun setKinDetailPassArrayList(context: Context,ownContactDetailArrayList: ArrayList<KinDetailApiModel>) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(ownContactDetailArrayList)
        editor.putString("kin_detail_pass", json)
        editor.apply()
    }


    fun getKinDetailPassArrayList(context: Context): ArrayList<KinDetailApiModel>? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = Gson()
        val json = prefs.getString("kin_detail_pass", null)
        val type: Type = object : TypeToken<ArrayList<KinDetailApiModel>?>() {}.type
        return gson.fromJson(json, type)
    }
    /*SET OWN DETAIL ARRAYLIST*/
    fun setHealthDetailArrayList(context: Context,ownContactDetailArrayList: ArrayList<HealthInsuranceDetailAPIModel>) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(ownContactDetailArrayList)
        editor.putString("health_detail", json)
        editor.apply()
    }


    fun getHealthDetailArrayList(context: Context): ArrayList<HealthInsuranceDetailAPIModel>? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = Gson()
        val json = prefs.getString("health_detail", null)
        val type: Type = object : TypeToken<ArrayList<HealthInsuranceDetailAPIModel>?>() {}.type
        return gson.fromJson(json, type)
    }

    /*SET OWN DETAIL ARRAYLIST*/
    fun setPassportDetailArrayList(context: Context,ownContactDetailArrayList: ArrayList<PassportApiModel>) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(ownContactDetailArrayList)
        editor.putString("passport_detail", json)
        editor.apply()
    }


    fun getPassportDetailArrayList(context: Context): ArrayList<PassportApiModel>? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = Gson()
        val json = prefs.getString("passport_detail", null)
        val type: Type = object : TypeToken<ArrayList<PassportApiModel>?>() {}.type
        return gson.fromJson(json, type)
    }
    /*SET TRIGGER TYPE*/
    fun setIsAlreadyEnteredOwn(context: Context, alreadyEntered: Boolean) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putBoolean("already_entered_own", alreadyEntered)
        editor.apply()
    }

    /*GET TRIGGER TYPE*/
    fun getIsAlreadyEnteredOwn(context: Context): Boolean {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getBoolean("already_entered_own", false)
    }
    /*SET TRIGGER TYPE*/
    fun setIsAlreadyEnteredKin(context: Context, alreadyEntered: Boolean) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putBoolean("already_entered_kin", alreadyEntered)
        editor.apply()
    }

    /*GET TRIGGER TYPE*/
    fun getIsAlreadyEnteredKin(context: Context): Boolean {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getBoolean("already_entered_kin", false)
    }

    /*SET STUDENT_ID*/
    fun setSuspendTrigger(context: Context, suspend: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("SuspendTrigger", suspend)
        editor.apply()
    }

    /*GET STUDENT_ID*/
    fun getSuspendTrigger(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("SuspendTrigger", "0")
    }
    /*SET STUDENT_ID*/
    fun setDisplayMessage(context: Context, suspend: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("display_message", suspend)
        editor.apply()
    }

    /*GET STUDENT_ID*/
    fun getDisplayMessage(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("display_message", "")
    }

    fun setIsFirstTimeInPE(context: Context, result: Boolean) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putBoolean("is_first_pe", result)
        editor.apply()
    }
    fun getIsFirstTimeInPE(context: Context): Boolean {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getBoolean("is_first_pe", true)
    }
    fun setTrnNo(context: Context, trn_no: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("trn_no", trn_no)
        editor.apply()
    }

    fun getTrnNo(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("trn_no", "")
    }
    fun setTrnPayment(context: Context, trn_pay: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("trn_pay", trn_pay)
        editor.apply()
    }

    fun getTrnPayment(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("trn_pay", "")
    }

    fun setCanteenTopUpLimit(context: Context, topUpLimit: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("topUpLimit", topUpLimit)
        editor.apply()
    }

    fun getCanteenTopUpLimit(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("topUpLimit", "")
    }

    fun setcartamounttotal(context: Context, result: Int) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("cartamounttotal", result)
        editor.apply()
    }

    fun getcartamounttotal(context: Context): Int {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(
            "cartamounttotal", context.resources
                .getColor(R.color.transparent)
        )
    }
    fun setwalletAmout(context: Context, color: Int) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putInt("walletamount", color)
        editor.apply()
    }

    fun getWalletAmount(context: Context): Int {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(
            "walletamount", context.resources
                .getColor(R.color.transparent)
        )
    }
    fun setPaymentMessage(context: Context, appVersion: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("payment_message", appVersion)
        editor.apply()
    }

    /*GET APP VERSION*/
    fun getPaymentMessage(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("payment_message", "")
    }
    fun setfee_payment(context: Context, appVersion: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("fee_payment", appVersion)
        editor.apply()
    }

    /*GET APP VERSION*/
    fun getfee_payment(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("fee_payment", "")
    }
    fun setwallet_payment(context: Context, appVersion: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("wallet_payment", appVersion)
        editor.apply()
    }

    /*GET APP VERSION*/
    fun getwallet_payment(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("wallet_payment", "")
    }
    fun settrip_payment(context: Context, appVersion: String?) {
        val prefs = context.getSharedPreferences(
            PREFSNAME, Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString("trip_payment", appVersion)
        editor.apply()
    }

    /*GET APP VERSION*/
    fun gettrip_payment(context: Context): String? {
        val prefs = context.getSharedPreferences(
            PREFSNAME,
            Context.MODE_PRIVATE
        )
        return prefs.getString("trip_payment", "")
    }
}