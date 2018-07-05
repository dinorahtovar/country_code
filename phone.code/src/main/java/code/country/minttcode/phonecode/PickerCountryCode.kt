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
    private var constraintLayout: ConstraintLayout? = null
    private var mInflater: LayoutInflater? = null

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
    enum class Language private constructor(var code: String) {
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

}
