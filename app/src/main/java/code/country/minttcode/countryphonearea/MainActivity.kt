package code.country.minttcode.countryphonearea

import android.app.Activity
import android.os.Bundle

/**
 * Created by Dinorah Tovar on 7/4/18
 * Main testing activity show the ability to start the view
 */

class MainActivity: Activity() {

    /**
     * Starting Activity from WalletIntegrator
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

}