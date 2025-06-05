package com.mobatia.bisad.fragment.vouchers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.mobatia.bisad.R

class VouchersFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    
    // Sample data for demonstration
    private val ticketItems = listOf(
        TicketItem(
            "Concert Night",
            "2024-06-15",
            "8:00 PM",
            "City Arena",
            "$79.99"
        ),
        TicketItem(
            "Theater Play",
            "2024-06-20",
            "7:30 PM",
            "Grand Theater",
            "$59.99"
        ),
        TicketItem(
            "Comedy Show",
            "2024-06-25",
            "9:00 PM",
            "Laugh Factory",
            "$39.99"
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vouchers, container, false)
        viewPager = view.findViewById(R.id.viewPager)
        
        // Set up the ViewPager2 with an adapter
        val adapter = TicketPagerAdapter()
        viewPager.adapter = adapter
        
        // Optional: Add page transformer for better visual effect
        viewPager.setPageTransformer { page, position ->
            val absPos = Math.abs(position)
            page.apply {
                scaleY = 0.85f + (1 - absPos) * 0.15f
                scaleX = 0.85f + (1 - absPos) * 0.15f
                alpha = 0.5f + (1 - absPos)
            }
        }
        
        // Optional: Add padding between pages
        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
        val offsetPx = resources.getDimensionPixelOffset(R.dimen.offset)
        viewPager.setPageTransformer { page, position ->
            val viewPager = page.parent.parent as ViewPager2
            val offset = position * -(2 * offsetPx + pageMarginPx)
            if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.translationX = -offset
                } else {
                    page.translationX = offset
                }
            } else {
                page.translationY = offset
            }
        }
        
        return view
    }
    
    // Data class for ticket items
    data class TicketItem(
        val title: String,
        val date: String,
        val time: String,
        val location: String,
        val price: String
    )
    
    // ViewHolder for ticket items
    private inner class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleText: TextView = itemView.findViewById(R.id.titleText)
        private val dateText: TextView = itemView.findViewById(R.id.dateText)
        private val timeText: TextView = itemView.findViewById(R.id.timeText)
        private val locationText: TextView = itemView.findViewById(R.id.locationText)
        private val priceText: TextView = itemView.findViewById(R.id.priceText)
        
        fun bind(ticketItem: TicketItem) {
            titleText.text = ticketItem.title
            dateText.text = "Date: ${ticketItem.date}"
            timeText.text = "Time: ${ticketItem.time}"
            locationText.text = "Location: ${ticketItem.location}"
            priceText.text = ticketItem.price
        }
    }
    
    // Adapter for ViewPager2
    private inner class TicketPagerAdapter : RecyclerView.Adapter<TicketViewHolder>() {
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ticket, parent, false)
            return TicketViewHolder(view)
        }
        
        override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
            holder.bind(ticketItems[position])
        }
        
        override fun getItemCount(): Int = ticketItems.size
    }
}