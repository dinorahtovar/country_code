package code.country.minttcode.phonecode

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by Dinorah Tovar on 7/10/18
 * Country code adapter
 */

internal class CountryCodeAdapter() : RecyclerView.Adapter<CountryCodeHolder>() {

    var countries: List<CodesCountryList?>? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CountryCodeHolder {
        return CountryCodeHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.country_list_header, viewGroup, false))
    }

    override fun onBindViewHolder(countryCodeViewHolder: CountryCodeHolder, i: Int) {
        countryCodeViewHolder.setCountry(countries!![i])
    }

    override fun getItemCount(): Int {
        return if (countries != null)
            countries!!.size
        else
            0
    }

}
