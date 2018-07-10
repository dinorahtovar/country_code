package code.country.minttcode.phonecode

import android.app.Dialog
import android.support.v7.widget.LinearLayoutManager
import android.view.Window

/**
 * Created by Dinorah Tovar on 6/28/18
 */

class PhoneCodeDialog {

    /**
     * Open dialog for country flags
     * @param codePicker PickerCountryCode
     */
    companion object {
        fun openCountryCodeDialog(codePicker: PickerCountryCode) {
            val context = codePicker.context
            val dialog = Dialog(context)
            //codePicker.refreshCustomMasterList()
            //codePicker.refreshPreferredCountries()
            val masterCountries = CodesCountryList.getCustomMasterCountryList(context, codePicker)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setContentView(R.layout.country_phone_list)

            //dialog views
            /*val recyclerView_countryDialog = dialog.findViewById(R.id.recycler_countryDialog) as RecyclerView
            val textViewTitle = dialog.findViewById(R.id.textView_title) as TextView
            val rlQueryHolder = dialog.findViewById(R.id.rl_query_holder) as RelativeLayout
            val imgClearQuery = dialog.findViewById(R.id.img_clear_query) as ImageView
            val editText_search = dialog.findViewById(R.id.editText_search) as EditText
            val textView_noResult = dialog.findViewById(R.id.textView_noresult) as TextView
            val rlHolder = dialog.findViewById(R.id.rl_holder) as RelativeLayout
            val imgDismiss = dialog.findViewById(R.id.img_dismiss) as ImageView*/

            val cca = CountryCodeAdapter()
            country_recycler_view.layoutManager = LinearLayoutManager(context)
            country_recycler_view.adapter = cca
            cca.countries = masterCountries
            cca.notifyDataSetChanged()

            //fast scroller
            /*val fastScroller = dialog.findViewById(R.id.fastscroll) as FastScroller
            fastScroller.setRecyclerView(recyclerView_countryDialog)
            if (codePicker.isShowFastScroller()) {
                if (codePicker.getFastScrollerBubbleColor() !== 0) {
                    fastScroller.setBubbleColor(codePicker.getFastScrollerBubbleColor())
                }

                if (codePicker.getFastScrollerHandleColor() !== 0) {
                    fastScroller.setHandleColor(codePicker.getFastScrollerHandleColor())
                }

                if (codePicker.getFastScrollerBubbleTextAppearance() !== 0) {
                    try {
                        fastScroller.setBubbleTextAppearance(codePicker.getFastScrollerBubbleTextAppearance())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

            } else {
                fastScroller.setVisibility(View.GONE)
            }

            dialog.setOnDismissListener { dialogInterface ->
                hideKeyboard(context)
                if (codePicker.getDialogEventsListener() != null) {
                    codePicker.getDialogEventsListener().onCcpDialogDismiss(dialogInterface)
                }
            }

            dialog.setOnCancelListener { dialogInterface ->
                hideKeyboard(context)
                if (codePicker.getDialogEventsListener() != null) {
                    codePicker.getDialogEventsListener().onCcpDialogCancel(dialogInterface)
                }
            }

            dialog.show()
            if (codePicker.getDialogEventsListener() != null) {
                codePicker.getDialogEventsListener().onCcpDialogOpen(dialog)
            }*/
        }
    }

}