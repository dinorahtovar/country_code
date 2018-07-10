package code.country.minttcode.phonecode

import android.content.Context
import android.util.SparseArray

import java.util.HashMap

/**
 * This all those countries that shares the same country code but can be differentiated based on area code
 */
class CCPCountryGroup private constructor(internal var defaultNameCode: String, internal var areaCodeLength: Int, private val nameCodeToAreaCodesMap: HashMap<String, String>) {

    /**
     * Go though nameCodeToAreaCodesMap entries to find name code of country.
     *
     * @param context
     * @param language
     * @param areaCode for which we are looking for country
     * @return country that matches areaCode. If no country matched, returns default country.
     */
    fun getCountryForAreaCode(context: Context, language: PickerCountryCode.Language, areaCode: String): CodesCountryList? {
        var nameCode = defaultNameCode
        for ((key, value) in nameCodeToAreaCodesMap) {
            if (value.contains(areaCode)) {
                nameCode = key
            }
        }
        return CodesCountryList.getCountryForNameCodeFromLibraryMasterList(context, language, nameCode)
    }

    companion object {
        private var countryGroups: SparseArray<CCPCountryGroup>? = null

        private fun initializeGroups() {

            countryGroups = SparseArray()

            //create group for +1
            addGroupForPhoneCode1()

            //create group for +44
            addGroupForPhoneCode44()

            //create group for +358
            addGroupForPhoneCode358()
        }

        private fun addGroupForPhoneCode358() {
            val nameCodeToAreaCodes = HashMap<String, String>()
            nameCodeToAreaCodes["ax"] = "18" //Aland Islands
            countryGroups!!.put(358, CCPCountryGroup("fi", 2, nameCodeToAreaCodes)) // Finland
        }

        /**
         * +44 group
         */
        private fun addGroupForPhoneCode44() {
            val nameCodeToAreaCodes = HashMap<String, String>()
            nameCodeToAreaCodes["gg"] = "1481" //Guernsey
            nameCodeToAreaCodes["im"] = "1624" //ISLE_OF_MAN
            nameCodeToAreaCodes["je"] = "1534" //Jersey
            countryGroups!!.put(44, CCPCountryGroup("gb", 4, nameCodeToAreaCodes)) // UK
        }


        /**
         * NANP countries (+1)
         */
        private fun addGroupForPhoneCode1() {
            val nameCodeToAreaCodes = HashMap<String, String>()
            nameCodeToAreaCodes["ag"] = "268" //ANTIGUA_AND_BARBUDA_AREA_CODES
            nameCodeToAreaCodes["ai"] = "264" //ANGUILLA_AREA_CODES
            nameCodeToAreaCodes["as"] = "684" //American Samoa
            nameCodeToAreaCodes["bb"] = "246" //BARBADOS_AREA_CODES
            nameCodeToAreaCodes["bm"] = "441" //BERMUDA_AREA_CODES
            nameCodeToAreaCodes["bs"] = "242" //BAHAMAS_AREA_CODES
            nameCodeToAreaCodes["ca"] = "204/226/236/249/250/289/306/343/365/403/416/418/431/437/438/450/506/514/519/579/581/587/600/601/604/613/639/647/705/709/769/778/780/782/807/819/825/867/873/902/905/" //CANADA_AREA_CODES
            nameCodeToAreaCodes["dm"] = "767" //DOMINICA_AREA_CODES
            nameCodeToAreaCodes["do"] = "809/829/849" //DOMINICAN_REPUBLIC_AREA_CODES
            nameCodeToAreaCodes["gd"] = "473" //GRENADA_AREA_CODES
            nameCodeToAreaCodes["gu"] = "671" //Guam
            nameCodeToAreaCodes["jm"] = "876" //JAMAICA_AREA_CODES
            nameCodeToAreaCodes["kn"] = "869" //SAINT_KITTS_AND_NEVIS_AREA_CODES
            nameCodeToAreaCodes["ky"] = "345" //CAYMAN_ISLANDS_AREA_CODES
            nameCodeToAreaCodes["lc"] = "758" //SAINT_LUCIA_AREA_CODES
            nameCodeToAreaCodes["mp"] = "670" //Northern Mariana Islands
            nameCodeToAreaCodes["ms"] = "664" //MONTSERRAT_AREA_CODES
            nameCodeToAreaCodes["pr"] = "787" //PUERTO_RICO_AREA_CODES
            nameCodeToAreaCodes["sx"] = "721" //SINT_MAARTEN_AREA_CODES
            nameCodeToAreaCodes["tc"] = "649" //TURKS_AND_CAICOS_ISLANDS_AREA_CODES
            nameCodeToAreaCodes["tt"] = "868" //TRINIDAD_AND_TOBAGO_AREA_CODES
            nameCodeToAreaCodes["vc"] = "784" //SAINT_VINCENT_AND_THE_GRENADINES_AREA_CODES
            nameCodeToAreaCodes["vg"] = "284" //BRITISH_VIRGIN_ISLANDS_AREA_CODES
            nameCodeToAreaCodes["vi"] = "340" //US_VIRGIN_ISLANDS_AREA_CODES
            countryGroups!!.put(1, CCPCountryGroup("us", 3, nameCodeToAreaCodes)) // USA
        }

        fun getCountryGroupForPhoneCode(countryCode: Int): CCPCountryGroup {
            if (countryGroups == null) {
                initializeGroups()
            }
            return countryGroups!!.get(countryCode)
        }
    }
}
