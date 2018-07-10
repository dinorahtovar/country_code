package code.country.minttcode.phonecode

import android.content.Context
import android.util.Log
import code.country.minttcode.phonecode.CCPCountryGroup
import code.country.minttcode.phonecode.PickerCountryCode
import code.country.minttcode.phonecode.R

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory

import java.io.IOException
import java.text.Collator
import java.util.ArrayList
import java.util.Collections
import java.util.Locale


/**
 * Created by hbb20 on 11/1/16.
 */
class CodesCountryList : Comparable<CodesCountryList> {

    var nameCode: String? = null
    var phoneCode: String? = null
    var name: String? = null
    var englishName: String? = null
    internal var flagResID = DEFAULT_FLAG_RES

    val flagID: Int
        get() {
            if (flagResID == -99)
                flagResID = getFlagMasterResID(this)
            return flagResID
        }

    constructor() {

    }

    constructor(nameCode: String, phoneCode: String, name: String, flagResID: Int) {
        this.nameCode = nameCode
        this.phoneCode = phoneCode
        this.name = name
        this.flagResID = flagResID
    }

    fun log() {
        try {
            Log.d(TAG, "Country->$nameCode:$phoneCode:$name")
        } catch (ex: NullPointerException) {
            Log.d(TAG, "Null")
        }

    }

    internal fun logString(): String {
        return nameCode?.toUpperCase() + " +" + phoneCode + "(" + name + ")"
    }

    /**
     * If country have query word in name or name code or phone code, this will return true.
     *
     * @param query
     * @return
     */
    internal fun isEligibleForQuery(query: String): Boolean {
        var query = query
        query = query.toLowerCase()
        return name?.toLowerCase()?.contains(query)!! || nameCode?.toLowerCase()?.contains(query)!! || phoneCode?.toLowerCase()?.contains(query)!! || englishName?.toLowerCase()?.contains(query)!!
    }

    override fun compareTo(o: CodesCountryList): Int {
        return Collator.getInstance().compare(name, o.name)
    }

    companion object {
        internal var DEFAULT_FLAG_RES = -99
        internal var TAG = "Class Country"
        internal var loadedLibraryMasterListLanguage: PickerCountryCode.Language? = null
        internal var dialogTitle: String? = null
        internal var searchHintMessage: String? = null
        internal var noResultFoundAckMessage: String? = null
        var loadedLibraryMaterList: List<CodesCountryList>? = null
            internal set
        //countries with +1
        private val ANTIGUA_AND_BARBUDA_AREA_CODES = "268"
        private val ANGUILLA_AREA_CODES = "264"
        private val BARBADOS_AREA_CODES = "246"
        private val BERMUDA_AREA_CODES = "441"
        private val BAHAMAS_AREA_CODES = "242"
        private val CANADA_AREA_CODES = "204/226/236/249/250/289/306/343/365/403/416/418/431/437/438/450/506/514/519/579/581/587/600/601/604/613/639/647/705/709/769/778/780/782/807/819/825/867/873/902/905/"
        private val DOMINICA_AREA_CODES = "767"
        private val DOMINICAN_REPUBLIC_AREA_CODES = "809/829/849"
        private val GRENADA_AREA_CODES = "473"
        private val JAMAICA_AREA_CODES = "876"
        private val SAINT_KITTS_AND_NEVIS_AREA_CODES = "869"
        private val CAYMAN_ISLANDS_AREA_CODES = "345"
        private val SAINT_LUCIA_AREA_CODES = "758"
        private val MONTSERRAT_AREA_CODES = "664"
        private val PUERTO_RICO_AREA_CODES = "787"
        private val SINT_MAARTEN_AREA_CODES = "721"
        private val TURKS_AND_CAICOS_ISLANDS_AREA_CODES = "649"
        private val TRINIDAD_AND_TOBAGO_AREA_CODES = "868"
        private val SAINT_VINCENT_AND_THE_GRENADINES_AREA_CODES = "784"
        private val BRITISH_VIRGIN_ISLANDS_AREA_CODES = "284"
        private val US_VIRGIN_ISLANDS_AREA_CODES = "340"

        //countries with +44
        private val ISLE_OF_MAN = "1624"

        /**
         * This function parses the raw/countries.xml file, and get list of all the countries.
         *
         * @param context: required to access application resources (where country.xml is).
         * @return List of all the countries available in xml file.
         */
        internal fun loadDataFromXML(context: Context, language: PickerCountryCode.Language?) {
            var countries: MutableList<CodesCountryList> = ArrayList()
            var tempDialogTitle = ""
            var tempSearchHint = ""
            var tempNoResultAck = ""
            try {
                val xmlFactoryObject = XmlPullParserFactory.newInstance()
                val xmlPullParser = xmlFactoryObject.newPullParser()
                val ins = context.resources.openRawResource(context.resources.getIdentifier(language
                        .toString().toLowerCase(Locale.ROOT), "raw", context.packageName))
                xmlPullParser.setInput(ins, "UTF-8")
                var event = xmlPullParser.eventType
                while (event != XmlPullParser.END_DOCUMENT) {
                    val name = xmlPullParser.name
                    when (event) {
                        XmlPullParser.START_TAG -> {
                        }
                        XmlPullParser.END_TAG -> if (name == "country") {
                            val ccpCountry = CodesCountryList()
                            ccpCountry.nameCode = xmlPullParser.getAttributeValue(null, "name_code").toUpperCase()
                            ccpCountry.phoneCode = xmlPullParser.getAttributeValue(null, "phone_code")
                            ccpCountry.englishName = xmlPullParser.getAttributeValue(null, "english_name")
                            ccpCountry.name = xmlPullParser.getAttributeValue(null, "name")
                            countries.add(ccpCountry)
                        } else if (name == "ccp_dialog_title") {
                            tempDialogTitle = xmlPullParser.getAttributeValue(null, "translation")
                        } else if (name == "ccp_dialog_search_hint_message") {
                            tempSearchHint = xmlPullParser.getAttributeValue(null, "translation")
                        } else if (name == "ccp_dialog_no_result_ack_message") {
                            tempNoResultAck = xmlPullParser.getAttributeValue(null, "translation")
                        }
                    }
                    event = xmlPullParser.next()
                }
                loadedLibraryMasterListLanguage = language
            } catch (e: XmlPullParserException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {

            }

            //if anything went wrong, countries will be loaded for english language
            if (countries.size == 0) {
                loadedLibraryMasterListLanguage = PickerCountryCode.Language.ENGLISH
                countries = libraryMasterCountriesEnglish
            }

            dialogTitle = if (tempDialogTitle.length > 0) tempDialogTitle else "Select a country"
            searchHintMessage = if (tempSearchHint.length > 0) tempSearchHint else "Search..."
            noResultFoundAckMessage = if (tempNoResultAck.length > 0) tempNoResultAck else "Results not found"
            loadedLibraryMaterList = countries

            // sort list
            Collections.sort(loadedLibraryMaterList!!)
        }

        fun getDialogTitle(context: Context, language: PickerCountryCode.Language): String? {
            if (loadedLibraryMasterListLanguage == null || loadedLibraryMasterListLanguage !== language || dialogTitle == null || dialogTitle!!.length == 0) {
                loadDataFromXML(context, language)
            }
            return dialogTitle
        }

        fun getSearchHintMessage(context: Context, language: PickerCountryCode.Language): String? {
            if (loadedLibraryMasterListLanguage == null || loadedLibraryMasterListLanguage !== language || searchHintMessage == null || searchHintMessage!!.length == 0) {
                loadDataFromXML(context, language)
            }
            return searchHintMessage
        }

        fun getNoResultFoundAckMessage(context: Context, language: PickerCountryCode.Language): String? {
            if (loadedLibraryMasterListLanguage == null || loadedLibraryMasterListLanguage !== language || noResultFoundAckMessage == null || noResultFoundAckMessage!!.length == 0) {
                loadDataFromXML(context, language)
            }
            return noResultFoundAckMessage
        }

        fun setDialogTitle(dialogTitle: String) {
            CodesCountryList.dialogTitle = dialogTitle
        }

        fun setSearchHintMessage(searchHintMessage: String) {
            CodesCountryList.searchHintMessage = searchHintMessage
        }

        fun setNoResultFoundAckMessage(noResultFoundAckMessage: String) {
            CodesCountryList.noResultFoundAckMessage = noResultFoundAckMessage
        }

        /**
         * Search a country which matches @param code.
         *
         * @param context
         * @param preferredCountries is list of preference countries.
         * @param code               phone code. i.e "91" or "1"
         * @return Country that has phone code as @param code.
         * or returns null if no country matches given code.
         * if same code (e.g. +1) available for more than one country ( US, canada) , this function will return preferred country.
         */
        private fun getCountryForCode(context: Context, language: PickerCountryCode.Language, preferredCountries: List<CodesCountryList>?, code: String): CodesCountryList? {

            /**
             * check in preferred countries
             */
            if (preferredCountries != null && !preferredCountries.isEmpty()) {
                for (CCPCountry in preferredCountries) {
                    if (CCPCountry.phoneCode == code) {
                        return CCPCountry
                    }
                }
            }

            for (CCPCountry in getLibraryMasterCountryList(context, language)!!) {
                if (CCPCountry.phoneCode == code) {
                    return CCPCountry
                }
            }
            return null
        }

        /**
         * @param code phone code. i.e "91" or "1"
         * @return Country that has phone code as @param code.
         * or returns null if no country matches given code.
         * if same code (e.g. +1) available for more than one country ( US, canada) , this function will return preferred country.
         * @avoid Search a country which matches @param code. This method is just to support correct preview
         */
        internal fun getCountryForCodeFromEnglishList(code: String): CodesCountryList? {
            val countries: List<CodesCountryList>
            countries = libraryMasterCountriesEnglish
            for (ccpCountry in countries) {
                if (ccpCountry.phoneCode == code)
                    return ccpCountry
            }
            return null
        }

        internal fun getCustomMasterCountryList(context: Context, codePicker: PickerCountryCode): List<CodesCountryList>? {
            codePicker.refreshCustomMasterList()
            return if (codePicker.customMasterCountriesList != null && codePicker.customMasterCountriesList!!.isNotEmpty()) {
                codePicker.customMasterCountriesList
            } else {
                getLibraryMasterCountryList(context, codePicker.languageToApply)
            }
        }

        /**
         * Search a country which matches @param nameCode.
         *
         * @param context
         * @param customMasterCountriesList
         * @param nameCode                  country name code. i.e US or us or Au. See countries.xml for all code names.  @return Country that has phone code as @param code.
         */
        internal fun getCountryForNameCodeFromCustomMasterList(context: Context, customMasterCountriesList: List<CodesCountryList>?, language: PickerCountryCode.Language, nameCode: String): CodesCountryList? {
            if (customMasterCountriesList == null || customMasterCountriesList.isEmpty()) {
                return getCountryForNameCodeFromLibraryMasterList(context, language, nameCode)
            } else {
                for (ccpCountry in customMasterCountriesList) {
                    if (ccpCountry.nameCode.equals(nameCode, ignoreCase = true))
                        return ccpCountry
                }
            }
            return null
        }

        /**
         * Search a country which matches @param nameCode.
         *
         * @param context
         * @param nameCode country name code. i.e US or us or Au. See countries.xml for all code names.
         * @return Country that has phone code as @param code.
         * or returns null if no country matches given code.
         */
        fun getCountryForNameCodeFromLibraryMasterList(context: Context, language: PickerCountryCode.Language?, nameCode: String): CodesCountryList? {
            val countries: List<CodesCountryList>? = CodesCountryList.getLibraryMasterCountryList(context, language)
            for (ccpCountry in countries!!) {
                if (ccpCountry.nameCode.equals(nameCode, ignoreCase = true))
                    return ccpCountry
            }
            return null
        }

        /**
         * Search a country which matches @param nameCode.
         * This searches through local english name list. This should be used only for the preview purpose.
         *
         * @param nameCode country name code. i.e US or us or Au. See countries.xml for all code names.
         * @return Country that has phone code as @param code.
         * or returns null if no country matches given code.
         */
        internal fun getCountryForNameCodeFromEnglishList(nameCode: String): CodesCountryList? {
            val countries: List<CodesCountryList>
            countries = libraryMasterCountriesEnglish
            for (CCPCountry in countries) {
                if (CCPCountry.nameCode.equals(nameCode, ignoreCase = true))
                    return CCPCountry
            }
            return null
        }

        /**
         * Search a country which matches @param code.
         *
         * @param context
         * @param preferredCountries list of country with priority,
         * @param code               phone code. i.e 91 or 1
         * @return Country that has phone code as @param code.
         * or returns null if no country matches given code.
         */
        internal fun getCountryForCode(context: Context, language: PickerCountryCode.Language, preferredCountries: List<CodesCountryList>, code: Int): CodesCountryList? {
            return getCountryForCode(context, language, preferredCountries, code.toString() + "")
        }

        /**
         * Finds country code by matching substring from left to right from full number.
         * For example. if full number is +819017901357
         * function will ignore "+" and try to find match for first character "8"
         * if any country found for code "8", will return that country. If not, then it will
         * try to find country for "81". and so on till first 3 characters ( maximum number of characters in country code is 3).
         *
         * @param context
         * @param preferredCountries countries of preference
         * @param fullNumber         full number ( "+" (optional)+ country code + carrier number) i.e. +819017901357 / 819017901357 / 918866667722
         * @return Country JP +81(Japan) for +819017901357 or 819017901357
         * Country IN +91(India) for  918866667722
         * null for 2956635321 ( as neither of "2", "29" and "295" matches any country code)
         */
        internal fun getCountryForNumber(context: Context, language: PickerCountryCode.Language, preferredCountries: List<CodesCountryList>?, fullNumber: String): CodesCountryList? {
            val firstDigit: Int
            //String plainNumber = PhoneNumberUtil.getInstance().normalizeDigitsOnly(fullNumber);
            if (fullNumber.isNotEmpty()) {
                if (fullNumber[0] == '+') {
                    firstDigit = 1
                } else {
                    firstDigit = 0
                }
                var ccpCountry: CodesCountryList? = null
                for (i in firstDigit..fullNumber.length) {
                    val code = fullNumber.substring(firstDigit, i)
                    var countryGroup: CCPCountryGroup? = null
                    try {
                        countryGroup = CCPCountryGroup.getCountryGroupForPhoneCode(Integer.parseInt(code))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    if (countryGroup != null) {
                        val areaCodeStartsAt = firstDigit + code.length
                        //when phone number covers area code too.
                        if (fullNumber.length >= areaCodeStartsAt + countryGroup.areaCodeLength) {
                            val areaCode = fullNumber.substring(areaCodeStartsAt, areaCodeStartsAt + countryGroup.areaCodeLength)
                            return countryGroup.getCountryForAreaCode(context, language, areaCode)
                        } else {
                            return getCountryForNameCodeFromLibraryMasterList(context, language, countryGroup.defaultNameCode)
                        }
                    } else {
                        ccpCountry = CodesCountryList.getCountryForCode(context, language, preferredCountries, code)
                        if (ccpCountry != null) {
                            return ccpCountry
                        }
                    }
                }
            }
            //it reaches here means, phone number has some problem.
            return null
        }

        /**
         * Finds country code by matching substring from left to right from full number.
         * For example. if full number is +819017901357
         * function will ignore "+" and try to find match for first character "8"
         * if any country found for code "8", will return that country. If not, then it will
         * try to find country for "81". and so on till first 3 characters ( maximum number of characters in country code is 3).
         *
         * @param context
         * @param fullNumber full number ( "+" (optional)+ country code + carrier number) i.e. +819017901357 / 819017901357 / 918866667722
         * @return Country JP +81(Japan) for +819017901357 or 819017901357
         * Country IN +91(India) for  918866667722
         * null for 2956635321 ( as neither of "2", "29" and "295" matches any country code)
         */
        fun getCountryForNumber(context: Context, language: PickerCountryCode.Language, fullNumber: String): CodesCountryList? {
            return getCountryForNumber(context, language, null, fullNumber)
        }

        /**
         * Returns image res based on country name code
         *
         * @param CCPCountry
         * @return
         */
        internal fun getFlagMasterResID(CCPCountry: CodesCountryList): Int {
            when (CCPCountry.nameCode?.toLowerCase()) {
            //this should be sorted based on country name code.
                "ad" //andorra
                -> return R.drawable.ad
                "ae" //united arab emirates
                -> return R.drawable.ae
                "af" //afghanistan
                -> return R.drawable.af
                "ag" //antigua & barbuda
                -> return R.drawable.ag
                "ai" //anguilla // Caribbean Islands
                -> return R.drawable.ai
                "al" //albania
                -> return R.drawable.al
                "am" //armenia
                -> return R.drawable.am
                "ao" //angola
                -> return R.drawable.ao
                "aq" //antarctica // custom
                -> return R.drawable.aq
                "ar" //argentina
                -> return R.drawable.ar
                "as" //American Samoa
                -> return R.drawable.`as`
                "at" //austria
                -> return R.drawable.at
                "au" //australia
                -> return R.drawable.au
                "aw" //aruba
                -> return R.drawable.aw
                "ax" //alan islands
                -> return R.drawable.ax
                "az" //azerbaijan
                -> return R.drawable.az
                "ba" //bosnia and herzegovina
                -> return R.drawable.ba
                "bb" //barbados
                -> return R.drawable.bb
                "bd" //bangladesh
                -> return R.drawable.bd
                "be" //belgium
                -> return R.drawable.be
                "bf" //burkina faso
                -> return R.drawable.bf
                "bg" //bulgaria
                -> return R.drawable.bg
                "bh" //bahrain
                -> return R.drawable.bh
                "bi" //burundi
                -> return R.drawable.bi
                "bj" //benin
                -> return R.drawable.bj
                "bl" //saint barthélemy
                -> return R.drawable.bl// custom
                "bm" //bermuda
                -> return R.drawable.bm
                "bn" //brunei darussalam // custom
                -> return R.drawable.bn
                "bo" //bolivia, plurinational state of
                -> return R.drawable.bo
                "br" //brazil
                -> return R.drawable.br
                "bs" //bahamas
                -> return R.drawable.bs
                "bt" //bhutan
                -> return R.drawable.bt
                "bw" //botswana
                -> return R.drawable.bw
                "by" //belarus
                -> return R.drawable.by
                "bz" //belize
                -> return R.drawable.bz
                "ca" //canada
                -> return R.drawable.ca
                "cc" //cocos (keeling) islands
                -> return R.drawable.cc// custom
                "cd" //congo, the democratic republic of the
                -> return R.drawable.cd
                "cf" //central african republic
                -> return R.drawable.cf
                "cg" //congo
                -> return R.drawable.cg
                "ch" //switzerland
                -> return R.drawable.ch
                "ci" //côte d\'ivoire
                -> return R.drawable.ci
                "ck" //cook islands
                -> return R.drawable.ck
                "cl" //chile
                -> return R.drawable.cl
                "cm" //cameroon
                -> return R.drawable.cm
                "cn" //china
                -> return R.drawable.cn
                "co" //colombia
                -> return R.drawable.co
                "cr" //costa rica
                -> return R.drawable.cr
                "cu" //cuba
                -> return R.drawable.cu
                "cv" //cape verde
                -> return R.drawable.cv
                "cx" //christmas island
                -> return R.drawable.cx
                "cy" //cyprus
                -> return R.drawable.cy
                "cz" //czech republic
                -> return R.drawable.cz
                "de" //germany
                -> return R.drawable.de
                "dj" //djibouti
                -> return R.drawable.dj
                "dk" //denmark
                -> return R.drawable.dk
                "dm" //dominica
                -> return R.drawable.dm
                "do" //dominican republic
                -> return R.drawable.dom
                "dz" //algeria
                -> return R.drawable.dz
                "ec" //ecuador
                -> return R.drawable.ec
                "ee" //estonia
                -> return R.drawable.ee
                "eg" //egypt
                -> return R.drawable.eg
                "er" //eritrea
                -> return R.drawable.er
                "es" //spain
                -> return R.drawable.es
                "et" //ethiopia
                -> return R.drawable.et
                "fi" //finland
                -> return R.drawable.fi
                "fj" //fiji
                -> return R.drawable.fj
                "fk" //falkland islands (malvinas)
                -> return R.drawable.fk
                "fm" //micronesia, federated states of
                -> return R.drawable.fm
                "fo" //faroe islands
                -> return R.drawable.fo
                "fr" //france
                -> return R.drawable.fr
                "ga" //gabon
                -> return R.drawable.ga
                "gb" //united kingdom
                -> return R.drawable.gb
                "gd" //grenada
                -> return R.drawable.gd
                "ge" //georgia
                -> return R.drawable.ge
                "gf" //guyane
                -> return R.drawable.gf
                "gg" //Guernsey
                -> return R.drawable.gg
                "gh" //ghana
                -> return R.drawable.gh
                "gi" //gibraltar
                -> return R.drawable.gi
                "gl" //greenland
                -> return R.drawable.gl
                "gm" //gambia
                -> return R.drawable.gm
                "gn" //guinea
                -> return R.drawable.gn
                "gp" //guadeloupe
                -> return R.drawable.gp
                "gq" //equatorial guinea
                -> return R.drawable.gq
                "gr" //greece
                -> return R.drawable.gr
                "gt" //guatemala
                -> return R.drawable.gt
                "gu" //Guam
                -> return R.drawable.gu
                "gw" //guinea-bissau
                -> return R.drawable.gw
                "gy" //guyana
                -> return R.drawable.gy
                "hk" //hong kong
                -> return R.drawable.hk
                "hn" //honduras
                -> return R.drawable.hn
                "hr" //croatia
                -> return R.drawable.hr
                "ht" //haiti
                -> return R.drawable.ht
                "hu" //hungary
                -> return R.drawable.hu
                "id" //indonesia
                -> return R.drawable.id
                "ie" //ireland
                -> return R.drawable.ie
                "il" //israel
                -> return R.drawable.il
                "im" //isle of man
                -> return R.drawable.im // custom
                "is" //Iceland
                -> return R.drawable.`is`
                "in" //india
                -> return R.drawable.`in`
                "io" //British indian ocean territory
                -> return R.drawable.io
                "iq" //iraq
                -> return R.drawable.iq
                "ir" //iran, islamic republic of
                -> return R.drawable.ir
                "it" //italy
                -> return R.drawable.it
                "je" //Jersey
                -> return R.drawable.je
                "jm" //jamaica
                -> return R.drawable.jm
                "jo" //jordan
                -> return R.drawable.jo
                "jp" //japan
                -> return R.drawable.jp
                "ke" //kenya
                -> return R.drawable.ke
                "kg" //kyrgyzstan
                -> return R.drawable.ky
                "kh" //cambodia
                -> return R.drawable.kh
                "ki" //kiribati
                -> return R.drawable.ki
                "km" //comoros
                -> return R.drawable.km
                "kn" //st kitts & nevis
                -> return R.drawable.kn
                "kp" //north korea
                -> return R.drawable.kp
                "kr" //south korea
                -> return R.drawable.kr
                "kw" //kuwait
                -> return R.drawable.kw
                "ky" //Cayman_Islands
                -> return R.drawable.ky
                "kz" //kazakhstan
                -> return R.drawable.kz
                "la" //lao people\'s democratic republic
                -> return R.drawable.la
                "lb" //lebanon
                -> return R.drawable.lb
                "lc" //st lucia
                -> return R.drawable.lc
                "li" //liechtenstein
                -> return R.drawable.li
                "lk" //sri lanka
                -> return R.drawable.lk
                "lr" //liberia
                -> return R.drawable.lr
                "ls" //lesotho
                -> return R.drawable.ls
                "lt" //lithuania
                -> return R.drawable.lt
                "lu" //luxembourg
                -> return R.drawable.lu
                "lv" //latvia
                -> return R.drawable.lv
                "ly" //libya
                -> return R.drawable.ly
                "ma" //morocco
                -> return R.drawable.ma
                "mc" //monaco
                -> return R.drawable.mc
                "md" //moldova, republic of
                -> return R.drawable.md
                "me" //montenegro
                -> return R.drawable.me// custom
                "mf"
                -> return R.drawable.mf
                "mg" //madagascar
                -> return R.drawable.mg
                "mh" //marshall islands
                -> return R.drawable.mh
                "mk" //macedonia, the former yugoslav republic of
                -> return R.drawable.mk
                "ml" //mali
                -> return R.drawable.ml
                "mm" //myanmar
                -> return R.drawable.mm
                "mn" //mongolia
                -> return R.drawable.mn
                "mo" //macao
                -> return R.drawable.mo
                "mp" // Northern mariana islands
                -> return R.drawable.mp
                "mq" //martinique
                -> return R.drawable.mq
                "mr" //mauritania
                -> return R.drawable.mr
                "ms" //montserrat
                -> return R.drawable.ms
                "mt" //malta
                -> return R.drawable.mt
                "mu" //mauritius
                -> return R.drawable.mu
                "mv" //maldives
                -> return R.drawable.mv
                "mw" //malawi
                -> return R.drawable.mw
                "mx" //mexico
                -> return R.drawable.mx
                "my" //malaysia
                -> return R.drawable.my
                "mz" //mozambique
                -> return R.drawable.mz
                "na" //namibia
                -> return R.drawable.na
                "nc" //new caledonia
                -> return R.drawable.nc// custom
                "ne" //niger
                -> return R.drawable.ne
                "nf" //Norfolk
                -> return R.drawable.nf
                "ng" //nigeria
                -> return R.drawable.ng
                "ni" //nicaragua
                -> return R.drawable.ni
                "nl" //netherlands
                -> return R.drawable.nl
                "no" //norway
                -> return R.drawable.no
                "np" //nepal
                -> return R.drawable.np
                "nr" //nauru
                -> return R.drawable.nr
                "nu" //niue
                -> return R.drawable.nu
                "nz" //new zealand
                -> return R.drawable.nz
                "om" //oman
                -> return R.drawable.om
                "pa" //panama
                -> return R.drawable.pa
                "pe" //peru
                -> return R.drawable.pe
                "pf" //french polynesia
                -> return R.drawable.pf
                "pg" //papua new guinea
                -> return R.drawable.pg
                "ph" //philippines
                -> return R.drawable.ph
                "pk" //pakistan
                -> return R.drawable.pk
                "pl" //poland
                -> return R.drawable.pl
                "pm" //saint pierre and miquelon
                -> return R.drawable.pm
                "pn" //pitcairn
                -> return R.drawable.pn
                "pr" //puerto rico
                -> return R.drawable.pr
                "ps" //palestine
                -> return R.drawable.ps
                "pt" //portugal
                -> return R.drawable.pt
                "pw" //palau
                -> return R.drawable.pw
                "py" //paraguay
                -> return R.drawable.py
                "qa" //qatar
                -> return R.drawable.qa
                "re" //la reunion
                -> return R.drawable.re // no exact flag found
                "ro" //romania
                -> return R.drawable.ro
                "rs" //serbia
                -> return R.drawable.rs // custom
                "ru" //russian federation
                -> return R.drawable.ru
                "rw" //rwanda
                -> return R.drawable.rw
                "sa" //saudi arabia
                -> return R.drawable.sa
                "sb" //solomon islands
                -> return R.drawable.sb
                "sc" //seychelles
                -> return R.drawable.sc
                "sd" //sudan
                -> return R.drawable.sd
                "se" //sweden
                -> return R.drawable.se
                "sg" //singapore
                -> return R.drawable.sg
                "sh" //saint helena, ascension and tristan da cunha
                -> return R.drawable.sh // custom
                "si" //slovenia
                -> return R.drawable.si
                "sk" //slovakia
                -> return R.drawable.sk
                "sl" //sierra leone
                -> return R.drawable.sl
                "sm" //san marino
                -> return R.drawable.sm
                "sn" //senegal
                -> return R.drawable.sn
                "so" //somalia
                -> return R.drawable.so
                "sr" //suriname
                -> return R.drawable.sr
                "st" //sao tome and principe
                -> return R.drawable.st
                "sv" //el salvador
                -> return R.drawable.sv
                "sx" //sint maarten
                -> return R.drawable.sx
                "sy" //syrian arab republic
                -> return R.drawable.sy
                "sz" //swaziland
                -> return R.drawable.sz
                "tc" //turks & caicos islands
                -> return R.drawable.tc
                "td" //chad
                -> return R.drawable.td
                "tg" //togo
                -> return R.drawable.tg
                "th" //thailand
                -> return R.drawable.th
                "tj" //tajikistan
                -> return R.drawable.tj
                "tk" //tokelau
                -> return R.drawable.tk // custom
                "tl" //timor-leste
                -> return R.drawable.tl
                "tm" //turkmenistan
                -> return R.drawable.tm
                "tn" //tunisia
                -> return R.drawable.tn
                "to" //tonga
                -> return R.drawable.to
                "tr" //turkey
                -> return R.drawable.tr
                "tt" //trinidad & tobago
                -> return R.drawable.tt
                "tv" //tuvalu
                -> return R.drawable.tv
                "tw" //taiwan, province of china
                -> return R.drawable.tw
                "tz" //tanzania, united republic of
                -> return R.drawable.tz
                "ua" //ukraine
                -> return R.drawable.ua
                "ug" //uganda
                -> return R.drawable.ug
                "us" //united states
                -> return R.drawable.us
                "uy" //uruguay
                -> return R.drawable.uy
                "uz" //uzbekistan
                -> return R.drawable.uz
                "va" //holy see (vatican city state)
                -> return R.drawable.va
                "vc" //st vincent & the grenadines
                -> return R.drawable.vc
                "ve" //venezuela, bolivarian republic of
                -> return R.drawable.ve
                "vg" //british virgin islands
                -> return R.drawable.vg
                "vi" //us virgin islands
                -> return R.drawable.vi
                "vn" //vietnam
                -> return R.drawable.vn
                "vu" //vanuatu
                -> return R.drawable.vu
                "wf" //wallis and futuna
                -> return R.drawable.wf
                "ws" //samoa
                -> return R.drawable.ws
                "xk" //kosovo
                -> return R.drawable.xk
                "ye" //yemen
                -> return R.drawable.ye
                "yt" //mayotte
                -> return R.drawable.yt // no exact flag found
                "za" //south africa
                -> return R.drawable.za
                "zm" //zambia
                -> return R.drawable.zm
                "zw" //zimbabwe
                -> return R.drawable.zw
                else -> return R.drawable.mx
            }
        }

        /**
         * This will return all the countries. No preference is manages.
         * Anytime new country need to be added, add it
         *
         * @return
         */
        fun getLibraryMasterCountryList(context: Context, language: PickerCountryCode.Language?): List<CodesCountryList>? {
            if (loadedLibraryMasterListLanguage == null || language !== loadedLibraryMasterListLanguage || loadedLibraryMaterList == null || loadedLibraryMaterList!!.size == 0) { //when it is required to load country in country list
                loadDataFromXML(context, language)
            }
            return loadedLibraryMaterList
        }

        val libraryMasterCountriesEnglish: MutableList<CodesCountryList>
            get() {
                val countries = ArrayList<CodesCountryList>()
                countries.add(CodesCountryList("ad", "376", "Andorra", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ae", "971", "United Arab Emirates (UAE)", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("af", "93", "Afghanistan", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ag", "1", "Antigua and Barbuda", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ai", "1", "Anguilla", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("al", "355", "Albania", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("am", "374", "Armenia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ao", "244", "Angola", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("aq", "672", "Antarctica", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ar", "54", "Argentina", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("as", "1", "American Samoa", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("at", "43", "Austria", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("au", "61", "Australia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("aw", "297", "Aruba", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("az", "358", "Aland Islands", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("az", "994", "Azerbaijan", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ba", "387", "Bosnia And Herzegovina", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("bb", "1", "Barbados", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("bd", "880", "Bangladesh", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("be", "32", "Belgium", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("bf", "226", "Burkina Faso", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("bg", "359", "Bulgaria", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("bh", "973", "Bahrain", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("bi", "257", "Burundi", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("bj", "229", "Benin", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("bl", "590", "Saint Barthélemy", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("bm", "1", "Bermuda", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("bn", "673", "Brunei Darussalam", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("bo", "591", "Bolivia, Plurinational State Of", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("br", "55", "Brazil", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("bs", "1", "Bahamas", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("bt", "975", "Bhutan", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("bw", "267", "Botswana", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("by", "375", "Belarus", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("bz", "501", "Belize", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ca", "1", "Canada", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("cc", "61", "Cocos (keeling) Islands", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("cd", "243", "Congo, The Democratic Republic Of The", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("cf", "236", "Central African Republic", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("cg", "242", "Congo", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ch", "41", "Switzerland", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ci", "225", "Côte D'ivoire", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ck", "682", "Cook Islands", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("cl", "56", "Chile", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("cm", "237", "Cameroon", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("cn", "86", "China", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("co", "57", "Colombia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("cr", "506", "Costa Rica", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("cu", "53", "Cuba", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("cv", "238", "Cape Verde", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("cx", "61", "Christmas Island", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("cy", "357", "Cyprus", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("cz", "420", "Czech Republic", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("de", "49", "Germany", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("dj", "253", "Djibouti", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("dk", "45", "Denmark", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("dm", "1", "Dominica", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("do", "1", "Dominican Republic", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("dz", "213", "Algeria", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ec", "593", "Ecuador", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ee", "372", "Estonia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("eg", "20", "Egypt", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("er", "291", "Eritrea", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("es", "34", "Spain", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("et", "251", "Ethiopia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("fi", "358", "Finland", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("fj", "679", "Fiji", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("fk", "500", "Falkland Islands (malvinas)", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("fm", "691", "Micronesia, Federated States Of", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("fo", "298", "Faroe Islands", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("fr", "33", "France", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ga", "241", "Gabon", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("gb", "44", "United Kingdom", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("gd", "1", "Grenada", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ge", "995", "Georgia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("gf", "594", "French Guyana", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("gh", "233", "Ghana", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("gi", "350", "Gibraltar", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("gl", "299", "Greenland", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("gm", "220", "Gambia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("gn", "224", "Guinea", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("gp", "450", "Guadeloupe", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("gq", "240", "Equatorial Guinea", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("gr", "30", "Greece", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("gt", "502", "Guatemala", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("gu", "1", "Guam", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("gw", "245", "Guinea-bissau", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("gy", "592", "Guyana", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("hk", "852", "Hong Kong", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("hn", "504", "Honduras", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("hr", "385", "Croatia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ht", "509", "Haiti", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("hu", "36", "Hungary", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("id", "62", "Indonesia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ie", "353", "Ireland", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("il", "972", "Israel", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("im", "44", "Isle Of Man", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("is", "354", "Iceland", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("in", "91", "India", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("io", "246", "British Indian Ocean Territory", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("iq", "964", "Iraq", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ir", "98", "Iran, Islamic Republic Of", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("it", "39", "Italy", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("je", "44", "Jersey ", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("jm", "1", "Jamaica", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("jo", "962", "Jordan", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("jp", "81", "Japan", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ke", "254", "Kenya", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("kg", "996", "Kyrgyzstan", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("kh", "855", "Cambodia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ki", "686", "Kiribati", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("km", "269", "Comoros", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("kn", "1", "Saint Kitts and Nevis", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("kp", "850", "North Korea", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("kr", "82", "South Korea", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("kw", "965", "Kuwait", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ky", "1", "Cayman Islands", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("kz", "7", "Kazakhstan", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("la", "856", "Lao People's Democratic Republic", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("lb", "961", "Lebanon", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("lc", "1", "Saint Lucia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("li", "423", "Liechtenstein", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("lk", "94", "Sri Lanka", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("lr", "231", "Liberia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ls", "266", "Lesotho", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("lt", "370", "Lithuania", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("lu", "352", "Luxembourg", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("lv", "371", "Latvia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ly", "218", "Libya", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ma", "212", "Morocco", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("mc", "377", "Monaco", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("md", "373", "Moldova, Republic Of", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("me", "382", "Montenegro", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("mf", "590", "Saint Martin", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("mg", "261", "Madagascar", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("mh", "692", "Marshall Islands", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("mk", "389", "Macedonia, The Former Yugoslav Republic Of", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ml", "223", "Mali", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("mm", "95", "Myanmar", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("mn", "976", "Mongolia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("mo", "853", "Macao", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("mp", "1", "Northern Mariana Islands", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("mq", "596", "Martinique", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("mr", "222", "Mauritania", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ms", "1", "Montserrat", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("mt", "356", "Malta", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("mu", "230", "Mauritius", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("mv", "960", "Maldives", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("mw", "265", "Malawi", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("mx", "52", "Mexico", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("my", "60", "Malaysia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("mz", "258", "Mozambique", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("na", "264", "Namibia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("nc", "687", "New Caledonia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ne", "227", "Niger", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("nf", "672", "Norfolk Islands", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ng", "234", "Nigeria", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ni", "505", "Nicaragua", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("nl", "31", "Netherlands", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("no", "47", "Norway", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("np", "977", "Nepal", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("nr", "674", "Nauru", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("nu", "683", "Niue", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("nz", "64", "New Zealand", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("om", "968", "Oman", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("pa", "507", "Panama", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("pe", "51", "Peru", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("pf", "689", "French Polynesia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("pg", "675", "Papua New Guinea", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ph", "63", "Philippines", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("pk", "92", "Pakistan", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("pl", "48", "Poland", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("pm", "508", "Saint Pierre And Miquelon", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("pn", "870", "Pitcairn", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("pr", "1", "Puerto Rico", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ps", "970", "Palestine", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("pt", "351", "Portugal", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("pw", "680", "Palau", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("py", "595", "Paraguay", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("qa", "974", "Qatar", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("re", "262", "Réunion", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ro", "40", "Romania", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("rs", "381", "Serbia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ru", "7", "Russian Federation", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("rw", "250", "Rwanda", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("sa", "966", "Saudi Arabia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("sb", "677", "Solomon Islands", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("sc", "248", "Seychelles", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("sd", "249", "Sudan", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("se", "46", "Sweden", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("sg", "65", "Singapore", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("sh", "290", "Saint Helena, Ascension And Tristan Da Cunha", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("si", "386", "Slovenia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("sk", "421", "Slovakia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("sl", "232", "Sierra Leone", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("sm", "378", "San Marino", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("sn", "221", "Senegal", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("so", "252", "Somalia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("sr", "597", "Suriname", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("st", "239", "Sao Tome And Principe", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("sv", "503", "El Salvador", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("sx", "1", "Sint Maarten", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("sy", "963", "Syrian Arab Republic", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("sz", "268", "Swaziland", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("tc", "1", "Turks and Caicos Islands", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("td", "235", "Chad", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("tg", "228", "Togo", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("th", "66", "Thailand", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("tj", "992", "Tajikistan", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("tk", "690", "Tokelau", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("tl", "670", "Timor-leste", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("tm", "993", "Turkmenistan", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("tn", "216", "Tunisia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("to", "676", "Tonga", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("tr", "90", "Turkey", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("tt", "1", "Trinidad &amp; Tobago", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("tv", "688", "Tuvalu", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("tw", "886", "Taiwan", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("tz", "255", "Tanzania, United Republic Of", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ua", "380", "Ukraine", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ug", "256", "Uganda", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("us", "1", "United States", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("uy", "598", "Uruguay", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("uz", "998", "Uzbekistan", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("va", "379", "Holy See (vatican City State)", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("vc", "1", "Saint Vincent &amp; The Grenadines", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ve", "58", "Venezuela, Bolivarian Republic Of", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("vg", "1", "British Virgin Islands", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("vi", "1", "US Virgin Islands", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("vn", "84", "Viet Nam", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("vu", "678", "Vanuatu", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("wf", "681", "Wallis And Futuna", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ws", "685", "Samoa", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("xk", "383", "Kosovo", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("ye", "967", "Yemen", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("yt", "262", "Mayotte", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("za", "27", "South Africa", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("zm", "260", "Zambia", DEFAULT_FLAG_RES))
                countries.add(CodesCountryList("zw", "263", "Zimbabwe", DEFAULT_FLAG_RES))
                return countries
            }
    }
}
