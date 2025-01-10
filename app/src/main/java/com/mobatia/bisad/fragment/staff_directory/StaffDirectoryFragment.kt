package com.mobatia.bisad.fragment.staff_directory

import android.app.Activity
import android.content.Context
import android.content.Context.*
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.constants.CommonFunctions
import com.mobatia.bisad.fragment.staff_directory.adapter.CategoryListAdapter
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.fragment.staff_directory.model.*
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class StaffDirectoryFragment: Fragment() {
    lateinit var progressDialog: RelativeLayout
    lateinit var searchtxt: EditText
    lateinit var searchbtn: ImageView
    lateinit var titleTextView: TextView
    lateinit var mContext: Context
    lateinit var sharedprefs: PreferenceData
    lateinit var staff_list: ArrayList<StaffDetailList>
    lateinit var filtered: ArrayList<StaffDetailList>
    lateinit var cat_recycler: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_staffdirectory, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedprefs = PreferenceData()
        mContext = requireContext()
        initializeUI()
        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
        if (internetCheck) {
            callCategoryListApi()
        } else {
            InternetCheckClass.showSuccessInternetAlert(mContext)
        }

        searchbtn.setOnClickListener(View.OnClickListener {
            if (staff_list.size > 0) {
                filtered = ArrayList<StaffDetailList>()
                for (i in staff_list.indices) {
                    if (staff_list.get(i).name
                            .lowercase(Locale.getDefault())
                            .contains(searchtxt.text.toString().lowercase(Locale.getDefault())) || staff_list.get(i).department
                            .lowercase(Locale.getDefault())
                            .contains(searchtxt.text.toString().lowercase(Locale.getDefault()))
                    ) {
                        filtered.add(staff_list.get(i))
                    }
                }
                cat_recycler.layoutManager = LinearLayoutManager(mContext)
                var cat_adapter = CategoryListAdapter(mContext,filtered,searchbtn,searchtxt)
                cat_recycler.adapter = cat_adapter

            }
           // AppUtils.hideKeyBoard(mContext)
        })
        searchtxt.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                // TODO Auto-generated method stub
                if (staff_list.size > 0) {
                    filtered = ArrayList<StaffDetailList>()
                    for (i in staff_list.indices) {
                        if ((staff_list.get(i).name
                                .lowercase(Locale.getDefault()).contains(s.toString().lowercase(Locale.getDefault()))
                                    )  || (staff_list.get(i).department
                                .lowercase(Locale.getDefault()).contains(s.toString().lowercase(Locale.getDefault()))
                                    ))
                        {
                            filtered.add(staff_list.get(i))
                        }
                    }
                    cat_recycler.layoutManager = LinearLayoutManager(mContext)
                    var cat_adapter = CategoryListAdapter(mContext,filtered,searchbtn,searchtxt)
                    cat_recycler.adapter = cat_adapter

                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }
        })
    }

    private fun initializeUI() {
        filtered = ArrayList()
        staff_list = ArrayList()
        searchbtn = requireView().findViewById(R.id.btnImgsearch)
        searchtxt = requireView().findViewById(R.id.searchEditText)
        cat_recycler = requireView().findViewById(R.id.mStaffDirectoryListView)
        titleTextView = requireView().findViewById(R.id.titleTextView) as TextView
        titleTextView.text = "Staff Directory"
        progressDialog = requireView().findViewById(R.id.progressDialog)

    }

    private fun callCategoryListApi() {
        progressDialog.visibility = View.VISIBLE

        val token = sharedprefs.getaccesstoken(mContext)
        val list_teachers= TeachingStaffListApiModel()
        val call: Call<TeachingStaffListModel> = ApiClient(mContext).getClient.teaching_staff_list(list_teachers,"Bearer "+token)
        call.enqueue(object : Callback<TeachingStaffListModel> {
            override fun onFailure(call: Call<TeachingStaffListModel>, t: Throwable) {
                progressDialog.visibility = View.GONE
                CommonFunctions.faliurepopup(mContext)
            }
            override fun onResponse(call: Call<TeachingStaffListModel>, response: Response<TeachingStaffListModel>) {
                progressDialog.visibility = View.GONE
                if (response.body()!!.status==100)
                {

                   staff_list.addAll(response.body()!!.responseArray.staff_list)
                    cat_recycler.layoutManager = LinearLayoutManager(mContext)
                    var cat_adapter = CategoryListAdapter(mContext,staff_list,searchbtn,searchtxt)
                    cat_recycler.adapter = cat_adapter

                }else if(response.body()!!.status==116)
                {
                    var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
                    if (internetCheck)
                    {
                        AccessTokenClass.getAccessToken(mContext)
                        callCategoryListApi()
                    }
                    else{
                        InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                    }

                }
                else
                {
                    InternetCheckClass.checkApiStatusError(response.body()!!.status,mContext)
                }


            }

        })



    }

}