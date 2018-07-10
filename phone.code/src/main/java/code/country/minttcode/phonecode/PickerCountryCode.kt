package code.country.minttcode.phonecode

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.layout_picker_country.view.*

/**
 * Created by Dinorah Tovar on 7/4/18
 * Picker code
 */

class PickerCountryCode @JvmOverloads
/**
 * Constructor
 * @param context Context
 * @param attrs Attribute Set for view
 * @param defStyleAttr Int style from attr
 */
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var holderView: View? = null
    private var xmlWidth: String? = "notSet"
    private var mInflater: LayoutInflater? = null
    private val customMasterCountriesParam: String? = null
    var excludedCountriesParam: String? = null
    var languageToApply: Language? = Language.ENGLISH
    internal var customMasterCountriesList: List<CodesCountryList>? = null


    /**
     * Companion Object
     */
    companion object {
        const val ANDROID_NAME_SPACE = "http://schemas.android.com/apk/res/android"
    }

    /**
     * Update every time new language is supported #languageSupport
     */
    //add an entry for your language in attrs.xml's <attr name="language" format="enum"> enum.
    //add here so that language can be set programmatically
    enum class Language(var code: String) {
        ENGLISH("en"),
        SPANISH("es")
    }

    /**
     * Init constructors
     */
    init {
        init(attrs)
    }

    /**
     * Init function call the TextInputLayout class and the secondary internal class CollapsingTextHelper
     * @see ConstraintLayout
     */
    private fun init(attrs: AttributeSet?) {
        mInflater = LayoutInflater.from(context)
        xmlWidth = attrs?.getAttributeValue(ANDROID_NAME_SPACE, "layout_width")
        removeAllViewsInLayout()
        holderView = mInflater?.inflate(R.layout.layout_picker_country, this, true)

        setListener()
    }

    private fun setListener() {
        picker_country.setOnClickListener { PhoneCodeDialog.openCountryCodeDialog(this) }
    }

    /**
     * this will load preferredCountries based on countryPreference
     */
    internal fun refreshCustomMasterList() {
        //if no custom list specified then check for exclude list
        if (customMasterCountriesParam == null || customMasterCountriesParam.isEmpty()) {
            //if excluded param is also blank, then do nothing
            if (excludedCountriesParam != null && excludedCountriesParam!!.isNotEmpty()) {
                excludedCountriesParam = excludedCountriesParam!!.toLowerCase()
                val libraryMasterList = CodesCountryList.getLibraryMasterCountryList(context, languageToApply)
                val localCCPCountryList = mutableListOf<CodesCountryList>()
                for (ccpCountry in libraryMasterList!!) {
                    //if the country name code is in the excluded list, avoid it.
                    if (!excludedCountriesParam!!.contains(ccpCountry.name?.toLowerCase()!!))
                        localCCPCountryList.add(ccpCountry)
                }
                customMasterCountriesList = if (localCCPCountryList.size > 0)
                    localCCPCountryList
                else
                    null
            } else {
                customMasterCountriesList = null
            }
        } else {
            //else add custom list
            val localCCPCountryList = mutableListOf<CodesCountryList>()
            for (nameCode in customMasterCountriesParam.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                val ccpCountry = CodesCountryList.getCountryForNameCodeFromLibraryMasterList(context, languageToApply, nameCode)
                if (ccpCountry != null) {
                    if (!isAlreadyInList(ccpCountry, localCCPCountryList))//to avoid duplicate entry of country
                        localCCPCountryList.add(ccpCountry)
                }
            }

            customMasterCountriesList = if (localCCPCountryList.size == 0)
                null
            else
                localCCPCountryList
        }

        if (customMasterCountriesList != null) {
            for (ccpCountry in customMasterCountriesList!!) {
                ccpCountry.log()
            }
        }
    }

    /**
     * This will match name code of all countries of list against the country's name code.
     * @param CCPCountry
     * @param CCPCountryList list of countries against which country will be checked.
     * @return if country name code is found in list, returns true else return false
     */
    private fun isAlreadyInList(CCPCountry: CodesCountryList?, CCPCountryList: List<CodesCountryList>?): Boolean {
        if (CCPCountry != null && CCPCountryList != null) {
            for (iterationCCPCountry in CCPCountryList) {
                if (iterationCCPCountry.nameCode.equals(CCPCountry.nameCode, true))
                    return true
            }
        }
        return false
    }

}
