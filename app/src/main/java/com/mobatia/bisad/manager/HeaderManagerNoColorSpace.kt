package com.mobatia.bisad.manager

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.RelativeLayout.LayoutParams
import android.widget.TextView
import com.mobatia.bisad.R
import java.io.Serializable


class HeaderManagerNoColorSpace(tripCategoriesActivity: Activity, s: String) : Serializable{

    lateinit var mcontext: Activity
    lateinit var  inflator: LayoutInflater
    lateinit var  headerView: View
    lateinit var  mHeading1: TextView
    lateinit var  mRightText: TextView
    lateinit var  mLeftText: TextView
    lateinit var  relativeParams: LayoutParams
    lateinit var  heading1: String
    lateinit var  edtText: EditText
    lateinit var  mLeftImage: ImageView
    lateinit var  mRight: ImageView
    lateinit var  mLeft: ImageView
    lateinit var  logoClickImgView: ImageView
    lateinit var  btn_history:ImageView

    fun HeaderManagerNoColorSpace(context:Activity,heading1 :String)
    {
       this.mcontext=context
        inflator= LayoutInflater.from(context)
        this.heading1=heading1
    }

    fun setVisible(v:View)
    {
      v.visibility=View.VISIBLE
    }
    fun setInvisible(v:View)
    {
      v.visibility=View.INVISIBLE
    }
    fun getHeader(headerHolder: RelativeLayout,type:Int) :Int
    {
        initializeUI(type)
        relativeParams = LayoutParams(
            LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT
        )
        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        headerHolder.addView(headerView, relativeParams)
        return headerView.id
    }
    fun initializeUI(type:Int)
    {
        inflator = LayoutInflater.from(mcontext)
        headerView = inflator.inflate(R.layout.common_header_single_withviewline, null)
        val logoHeader = headerView.findViewById<View>(R.id.relative_logo_header) as RelativeLayout
        if (type == 0) {
            logoHeader.setBackgroundResource(R.drawable.titlebar) // two
            // buttons
        } else if (type == 1) {
            logoHeader.setBackgroundResource(R.drawable.titlebar) // left
            // button
        }
        mHeading1 = headerView.findViewById<View>(R.id.heading) as TextView
        mHeading1.visibility = View.GONE
        mHeading1.text = heading1
        mRight = headerView.findViewById<View>(R.id.btn_right) as ImageView
        mLeft = headerView.findViewById<View>(R.id.btn_left) as ImageView
        btn_history = headerView
            .findViewById<View>(R.id.btn_history) as ImageView
    }

    fun initializeUI(getHeading :Boolean,type:Int)
    {
        inflator = LayoutInflater.from(mcontext)
        headerView = inflator.inflate(R.layout.common_header_single_withviewline, null)
        val logoHeader = headerView
            .findViewById<View>(R.id.relative_logo_header) as RelativeLayout
        if (type == 0) {
            logoHeader.setBackgroundResource(R.drawable.titlebar)
        } else if (type == 1) {
            logoHeader.setBackgroundResource(R.drawable.titlebar)
            mHeading1 = headerView.findViewById<View>(R.id.heading) as TextView
            mHeading1.visibility = View.GONE
        }
        logoClickImgView = headerView.findViewById<View>(R.id.logoClickImgView) as ImageView

        mHeading1 = headerView.findViewById<View>(R.id.heading) as TextView
        mHeading1.visibility = View.GONE
        mHeading1.text = heading1
        mRight =
            headerView.findViewById<View>(R.id.btn_right) as ImageView
        mLeft =
            headerView.findViewById<View>(R.id.btn_left) as ImageView
    }

   fun setTitle(title:String)
   {
       mHeading1.text = title
   }
    fun getLogoButton(): ImageView? {
        return logoClickImgView
    }
  fun getLeftButton():ImageView{
      return mLeft
  }
  fun setButtonLeftSelector(normalStateResID :Int,pressedStateResID:Int)
  {
      mLeft.setImageDrawable(
          getButtonDrawableByScreenCathegory(
              normalStateResID, pressedStateResID
          )
      )
      setVisible(mLeft)
  }
    @SuppressLint("UseCompatLoadingForDrawables")
    fun getButtonDrawableByScreenCathegory(normalStateResID :Int, pressedStateResID:Int) :Drawable

    {
        val state_normal = mcontext.resources
            .getDrawable(normalStateResID).mutate()
        val state_pressed = mcontext.resources
            .getDrawable(pressedStateResID).mutate()
        val drawable = StateListDrawable()
        drawable.addState(intArrayOf(android.R.attr.state_pressed), state_pressed)
        drawable.addState(intArrayOf(android.R.attr.state_enabled), state_normal)
        return drawable
    }

    // public void setCancelButton()
    // {
    // mRight.setBackgroundResource(R.drawable.close_button_selector);
    // setVisible(mRight);
    // }
    fun setContext(context: Activity?) {
        this.mcontext = context!!
    }

    /**
     * Gets the context.
     *
     * @return the context
     */
    fun getContext(): Activity? {
        return mcontext
    }
}