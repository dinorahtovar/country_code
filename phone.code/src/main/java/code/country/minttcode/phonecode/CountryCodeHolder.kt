package code.country.minttcode.phonecode

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.country_list_header.view.*

/**
 * Created by Dinorah Tovar on 7/10/18
 * Holder to fill Adapter of the flags
 */

class CountryCodeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    /**
     * Setting country code list on the holder
     * making view available for everyone
     */
    fun setCountry(CCPCountry: CodesCountryList?) {
        itemView.country_list_divider.visibility = View.GONE
        itemView.country_list_name.text = CCPCountry?.name
        itemView.country_list_flag.setImageResource(CCPCountry?.flagID!!)
    }

}