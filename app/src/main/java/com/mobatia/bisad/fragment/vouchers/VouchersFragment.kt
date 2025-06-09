package com.mobatia.bisad.fragment.vouchers

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.mobatia.bisad.R
import com.tuyenmonkey.mkloader.type.Worm
import com.zhpan.indicator.IndicatorView
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle

private lateinit var viewPager: ViewPager2

class VouchersFragment : Fragment() {

    private val ticketData = listOf(
        TicketData(
            "50% OFF",
            "Use this exclusive coupon to get 50% off on your next purchase across all categories. This offer is valid for a limited time only and applicable to one-time use per user. Make sure to check the terms and conditions before redeeming.",
            "BISAD50",
            "31 DEC 2024",
            R.color.rel_one
        ),
        TicketData(
            "30% OFF",
            "Get a flat 30% discount on every product sitewide. Whether you’re shopping for yourself or gifting someone special, use this code at checkout. Offer valid till stocks last or until the expiry date.",
            "BISAD30",
            "31 JAN 2025",
            R.color.rel_two
        ),
        TicketData(
            "20% OFF",
            "Apply this code to receive a 20% discount on specially marked items in our catalog. Items in this category are updated weekly, so check back regularly for new deals.",
            "BISAD20",
            "28 FEB 2025",
            R.color.rel_three
        ),
        TicketData(
            "50% OFF",
            "Redeem 50% off on your next checkout with no minimum order value required. Valid only for registered users and limited to one use per user account.",
            "BISAD50",
            "31 DEC 2024",
            R.color.rel_four
        ),
        TicketData(
            "30% OFF",
            "Avail a 30% discount instantly at checkout using this promo code. Valid across all categories and compatible with COD, wallet, and card payments.",
            "BISAD30",
            "31 JAN 2025",
            R.color.rel_five
        )
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vouchers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.viewPager)
        val adapter = TicketPagerAdapter()
        viewPager.adapter = adapter

        // Add page transformer for card effect
        viewPager.setPageTransformer(StackCardViewPagerTransformer())

        // Set offscreen page limit for better performance
        viewPager.offscreenPageLimit = 3

        val indicatorView = view.findViewById<IndicatorView>(R.id.indicator_view)
        indicatorView.apply {
            setSliderColor(resources.getColor(R.color.rel_one), resources.getColor(R.color.rel_two))
            setSliderWidth(resources.getDimension(R.dimen.dp_10))
            setSliderHeight(resources.getDimension(R.dimen.dp_5))
            setSlideMode(IndicatorSlideMode.WORM)
            setIndicatorStyle(IndicatorStyle.CIRCLE)
            setupWithViewPager(viewPager)
        }
    }

    private inner class TicketPagerAdapter : androidx.viewpager2.adapter.FragmentStateAdapter(this) {
        override fun getItemCount(): Int = ticketData.size

        override fun createFragment(position: Int): Fragment {
            return TicketItemFragment.newInstance(ticketData[position])
        }
    }

    class TicketItemFragment : Fragment() {
        private lateinit var ticketData: TicketData
        private lateinit var amountText: TextView
        private lateinit var descriptionText: TextView
        private lateinit var readMoreText: TextView
        private lateinit var codeText: TextView
        private lateinit var validityText: TextView
        private lateinit var redeemButton: ConstraintLayout
        private lateinit var headerLayout: RelativeLayout
        private var isExpanded = false

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.item_ticket, container, false)

            // Initialize views
            amountText = view.findViewById(R.id.amountText)
            descriptionText = view.findViewById(R.id.descriptionText)
            readMoreText = view.findViewById(R.id.readMoreText)
            codeText = view.findViewById(R.id.codeText)
            validityText = view.findViewById(R.id.validityText)
            redeemButton = view.findViewById(R.id.redeemButton)
            headerLayout = view.findViewById(R.id.headerLayout)

            return view
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            ticketData = arguments?.getParcelable(ARG_TICKET) ?: return

            // Set data to views
            amountText.text = ticketData.amount
            descriptionText.text = ticketData.description
            codeText.text = ticketData.code
            validityText.text = ticketData.validity

            // Check if we need to show "Read More"
            descriptionText.post {
                val lineCount = descriptionText.layout?.lineCount ?: 0
                if (lineCount > 8) {
                    readMoreText.visibility = View.VISIBLE
                    descriptionText.maxLines = 8
                    readMoreText.text = "Read More"
                    readMoreText.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, 
                        R.drawable.ic_arrow_down, 0
                    )
                    
                    // Make sure the drawables are tinted white
                    readMoreText.compoundDrawables.forEach { drawable ->
                        drawable?.setTint(android.graphics.Color.WHITE)
                    }
                } else {
                    readMoreText.visibility = View.GONE
                }

                // Set click listener for read more/less
                readMoreText.setOnClickListener {
                    isExpanded = !isExpanded
                    if (isExpanded) {
                        descriptionText.maxLines = Integer.MAX_VALUE
                        readMoreText.text = "Read Less"
                        readMoreText.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0,
                            R.drawable.ic_arrow_up, 0
                        )
                    } else {
                        descriptionText.maxLines = 8
                        readMoreText.text = "Read More"
                        readMoreText.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0,
                            R.drawable.ic_arrow_down, 0
                        )
                    }
                    // Tint the drawables after setting them
                    readMoreText.compoundDrawables.forEach { drawable ->
                        drawable?.setTint(android.graphics.Color.WHITE)
                    }
                }
            }


            // Apply dynamic colors
            val color = ResourcesCompat.getColor(resources, ticketData.color, null)

//            // Set header background color
//            headerLayout.setBackgroundColor(color)
//
//            // Set code text color
//            codeText.setTextColor(color)
//
//            // Set button text and border color
//            redeemButton.setTextColor(color)

            // Create a custom drawable for the button background
//            val buttonDrawable = ResourcesCompat.getDrawable(resources, R.drawable.button_border, null)?.mutate()
//            buttonDrawable?.setTint(color)

            // Create a ColorStateList for the button text
            val states = arrayOf(
                intArrayOf(android.R.attr.state_pressed),
                intArrayOf(-android.R.attr.state_pressed)
            )
            val colors = intArrayOf(
                android.graphics.Color.argb(26, android.graphics.Color.red(color), android.graphics.Color.green(color), android.graphics.Color.blue(color)), // Slightly transparent version for pressed state
                color
            )
            val colorStateList = android.content.res.ColorStateList(states, colors)

            // Apply the color state list to the button
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                redeemButton.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.TRANSPARENT)
            }

            // Set the background with our custom drawable
//            redeemButton.background = buttonDrawable

            // Set the text color state list
//            redeemButton.setTextColor(colorStateList)

            // Set click listener for redeem button
            redeemButton.setOnClickListener {
                // Handle redeem button click
            }
        }

        companion object {
            private const val ARG_TICKET = "ticket"

            fun newInstance(ticketData: TicketData): TicketItemFragment {
                return TicketItemFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_TICKET, ticketData)
                    }
                }
            }
        }
    }


    // Add this class for card stack effect
    private inner class StackCardViewPagerTransformer : ViewPager2.PageTransformer {
        private val MIN_SCALE = 0.85f
        private val MIN_ALPHA = 0.5f

        override fun transformPage(page: View, position: Float) {
            page.apply {
                val pageWidth = width
                val pageHeight = height

                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left
                        alpha = 0f
                    }
                    position <= 1 -> { // [-1,1]
                        // Modify the default slide transition to shrink the page as well
                        val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2

                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            -horzMargin + vertMargin / 2
                        }

                        // Scale the page down (between MIN_SCALE and 1)
                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        // Fade the page relative to its size
                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }
                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right
                        alpha = 0f
                    }
                }
            }
        }
    }
}

data class TicketData(
    val amount: String,
    val description: String,
    val code: String,
    val validity: String,
    val color: Int // Color resource ID (e.g., R.color.red)
) : android.os.Parcelable {
    constructor(parcel: android.os.Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
        parcel.writeString(amount)
        parcel.writeString(description)
        parcel.writeString(code)
        parcel.writeString(validity)
        parcel.writeInt(color)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : android.os.Parcelable.Creator<TicketData> {
        override fun createFromParcel(parcel: android.os.Parcel): TicketData {
            return TicketData(parcel)
        }

        override fun newArray(size: Int): Array<TicketData?> {
            return arrayOfNulls(size)
        }
    }
}
