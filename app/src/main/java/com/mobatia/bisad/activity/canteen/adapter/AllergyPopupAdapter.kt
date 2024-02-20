package com.mobatia.bisad.activity.canteen.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.canteen.model.AllergyContentModel

class AllergyPopupAdapter (
    var allergycontentlist: ArrayList<AllergyContentModel>,
    var mcontext: Context
) :
    RecyclerView.Adapter<AllergyPopupAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.allergy_contents_popup_adapter, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.content_name.text=allergycontentlist[position].name
        val circleDrawable =
            ContextCompat.getDrawable(mcontext, R.drawable.shape_circle)
        if (circleDrawable != null) {
            circleDrawable.setColorFilter(Color.parseColor(allergycontentlist[position].color_code), PorterDuff.Mode.SRC)
            holder.color_shape.setBackground(circleDrawable)
        }

       // holder.color_shape.setBackgroundColor(Color.parseColor(allergycontentlist[position].color_code))

    }

    override fun getItemCount(): Int {
        return allergycontentlist.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var color_shape: ImageView
        lateinit var content_name: TextView

        init {

            content_name=itemView.findViewById(R.id.content_name)
            color_shape=itemView.findViewById(R.id.color_code)
        }
    }
}