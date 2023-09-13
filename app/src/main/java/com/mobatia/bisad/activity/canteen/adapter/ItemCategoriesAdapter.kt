package com.mobatia.bisad.activity.canteen.adapter

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.canteen.model.add_orders.CategoryListModel

class ItemCategoriesAdapter (val category_list: ArrayList<CategoryListModel>, var mcontext: Context) :
    RecyclerView.Adapter<ItemCategoriesAdapter.ViewHolder>() {
    var isCatSelected:Boolean=false
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.itemcategory_adapter, viewGroup, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.category_name.text = category_list[position].category_name
        viewHolder.selected.setBackgroundResource(R.drawable.date_selected)

var url:String?=""
        url=category_list[position].category_image

        if (url.equals("")) {
            viewHolder.image.setBackgroundResource(R.drawable.default_cat)

        }
        else
        {

            mcontext.let {
                Glide.with(it)
                    .load(url)
                    .into(viewHolder.image)
            }
        }
        if (category_list.get(position).isItemSelected)
        {
            viewHolder.bglinear.setBackgroundResource(R.drawable.date_selected)
            viewHolder.linear.setBackgroundResource(R.color.white)

        }
        else
        {
            viewHolder.linear.setBackgroundResource(R.drawable.date)
            viewHolder.linear.setBackgroundResource(R.color.white)

        }
        for (i in 0..0) {
            category_list.get(0).isItemSelected
            if (category_list.get(position).isItemSelected)
            {
                viewHolder.bglinear.setBackgroundResource(R.drawable.date_selected)
                viewHolder.linear.setBackgroundResource(R.color.white)

            }
            //viewHolder.linear.setBackgroundResource(R.drawable.date_selected)
        }
    }

    override fun getItemCount(): Int {
        return category_list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var category_name: TextView
        var image: ImageView
        var linear: LinearLayout
        var bglinear: LinearLayout
        var selected: ImageView
        init {
            category_name = itemView.findViewById(R.id.categoryTitle)
            image=itemView.findViewById(R.id.categoryImg)
            linear=itemView.findViewById(R.id.mainLinear)
            bglinear=itemView.findViewById(R.id.bgLinear)
            selected=itemView.findViewById(R.id.itemSelectedImg)

        }
    }

}