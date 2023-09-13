package com.mobatia.bisad.activity.staff_directory

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.activity.staff_directory.model.StaffDetailsModel

class StaffDirectoryActivity:AppCompatActivity() {
    lateinit var context:Context
    lateinit var titletext:TextView
    lateinit var cat_title_rec:RecyclerView
    var cat_id:String=""
    var cat_name:String=""
    var dept_name:String=""
    lateinit var searchbtn:ImageView
    lateinit var searchtxt:EditText
    lateinit var staffdetails:ArrayList<StaffDetailsModel>
    lateinit var back: RelativeLayout
    lateinit var home_icon:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff_directory)
        init()
    }

    private fun init(){
        context=this
        staffdetails= ArrayList()
        cat_id=intent.getStringExtra("cat_id").toString()
        cat_name=intent.getStringExtra("cat_name").toString()
        searchbtn = findViewById(R.id.btnImgsearch)
        searchtxt = findViewById(R.id.searchEditText)
        titletext=findViewById(R.id.heading)
        titletext.text = cat_name
        dept_name= titletext.text.toString()
        cat_title_rec=findViewById(R.id.mStaffListView)
        back=findViewById(R.id.backRelative)
        home_icon=findViewById(R.id.logoClickImgView)
        back.setOnClickListener {
            finish()
        }
        home_icon.setOnClickListener {
            val intent = Intent(context, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

call_catlist()
    }
    private fun call_catlist(){
        var model1=StaffDetailsModel("TestTeacher1","11","64742467854","abcd@gmail.com","","Test teacher for english","ajjdneu43875ghjj")
     staffdetails.add(0,model1)
        var model2=StaffDetailsModel("TestTeacher2","12","3698521475","","","This is test 2",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
staffdetails.add(1,model2)
      /*  cat_title_rec.layoutManager=LinearLayoutManager(context)
        var categorytitle_adapter= CategoryListAdapter(context,staffdetails,searchbtn,searchtxt,dept_name)
        cat_title_rec.adapter=categorytitle_adapter*/
    }

}