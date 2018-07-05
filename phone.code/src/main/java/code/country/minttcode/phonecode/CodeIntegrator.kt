package code.country.minttcode.phonecode

import android.app.Activity
import android.content.Intent
import java.io.Serializable
import java.util.HashMap

/**
 * Created by Dinorah Tovar on 6/28/18
 * Country Code Integrator
 */

class CodeIntegrator {

    private var activity: Activity? = null
    private var loginActivity: Class<*>? = null
    private val ATTRIBUTES_EXTRA = "attributes"
    private val moreExtras = HashMap<String, Any>(3)

    protected fun getDefaultActivity(): Class<*> {
        return PhoneCodeDialog::class.java
    }

    fun getLoginActivity(): Class<*> {
        if (loginActivity == null) {
            loginActivity = getDefaultActivity()
        }
        return loginActivity as Class<*>
    }

    /**
     * @param activity [Activity] invoking the integration
     */
    fun CodeIntegrator(activity: Activity?) {
        this.activity = activity
    }

    /**
     * Initiates the library
     */
    fun initiateCountryPhone() {
        startActivityForResult(createIntent(), 0)
    }

    /**
     * Create a Login intent with the specified options.
     * @return
     */
    private fun createIntent(): Intent {
        val intentScan = Intent(activity, getLoginActivity())
        attachMoreExtras(intentScan)
        return intentScan
    }

    /**
     * Start an activity. This method is defined to allow different methods of activity starting for
     * newer versions of Android and for compatibility library.
     *
     * @param intent Intent to start.
     * @param code   Request code for the activity
     * @see android.app.Activity.startActivityForResult
     */
    private fun startActivityForResult(intent: Intent, code: Int) {
        activity?.startActivityForResult(intent, code)
    }

    /**
     * Use the specified camera ID.
     *
     * @param attributes AttributesResources for custome view
     * @return this
     */
    fun setAttributes(attributes: ResourcesPhone?): CodeIntegrator {
        if (attributes != null)
            addExtra(ATTRIBUTES_EXTRA, attributes)
        return this
    }

    /**
     * Adding extras to the Intent
     * @param key Intent key
     * @param value Intent value key
     * @return
     */
    private fun addExtra(key: String, value: Any): CodeIntegrator {
        moreExtras[key] = value
        return this
    }

    private fun attachMoreExtras(intent: Intent) {
        for ((key, value) in moreExtras) {
            if (value is ResourcesPhone)
                intent.putExtra(key, value as Serializable)
        }
    }
}