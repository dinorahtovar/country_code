package code.country.minttcode.phonecode

import android.app.Dialog

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
            val context = codePicker.getContext()
            val dialog = Dialog(context)
            //codePicker.refreshCustomMasterList()
            //codePicker.refreshPreferredCountries()
            val masterCountries = CCPCountry.getCustomMasterCountryList(context, codePicker)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setContentView(R.layout.layout_picker_dialog)

            //keyboard
            if (codePicker.isSearchAllowed() && codePicker.isDialogKeyboardAutoPopup()) {
                dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            } else {
                dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            }


            //dialog views
            val recyclerView_countryDialog = dialog.findViewById(R.id.recycler_countryDialog) as RecyclerView
            val textViewTitle = dialog.findViewById(R.id.textView_title) as TextView
            val rlQueryHolder = dialog.findViewById(R.id.rl_query_holder) as RelativeLayout
            val imgClearQuery = dialog.findViewById(R.id.img_clear_query) as ImageView
            val editText_search = dialog.findViewById(R.id.editText_search) as EditText
            val textView_noResult = dialog.findViewById(R.id.textView_noresult) as TextView
            val rlHolder = dialog.findViewById(R.id.rl_holder) as RelativeLayout
            val imgDismiss = dialog.findViewById(R.id.img_dismiss) as ImageView

            // type faces
            //set type faces
            try {
                if (codePicker.getDialogTypeFace() != null) {
                    if (codePicker.getDialogTypeFaceStyle() !== CountryCodePicker.DEFAULT_UNSET) {
                        textView_noResult.setTypeface(codePicker.getDialogTypeFace(), codePicker.getDialogTypeFaceStyle())
                        editText_search.setTypeface(codePicker.getDialogTypeFace(), codePicker.getDialogTypeFaceStyle())
                        textViewTitle.setTypeface(codePicker.getDialogTypeFace(), codePicker.getDialogTypeFaceStyle())
                    } else {
                        textView_noResult.typeface = codePicker.getDialogTypeFace()
                        editText_search.typeface = codePicker.getDialogTypeFace()
                        textViewTitle.typeface = codePicker.getDialogTypeFace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            //dialog background color
            if (codePicker.getDialogBackgroundColor() !== 0) {
                rlHolder.setBackgroundColor(codePicker.getDialogBackgroundColor())
            }

            //close button visibility
            if (codePicker.isShowCloseIcon()) {
                imgDismiss.visibility = View.VISIBLE
                imgDismiss.setOnClickListener { dialog.dismiss() }
            } else {
                imgDismiss.visibility = View.GONE
            }

            //title
            if (!codePicker.getCcpDialogShowTitle()) {
                textViewTitle.visibility = View.GONE
            }

            //clear button color and title color
            if (codePicker.getDialogTextColor() !== 0) {
                val textColor = codePicker.getDialogTextColor()
                imgClearQuery.setColorFilter(textColor)
                imgDismiss.setColorFilter(textColor)
                textViewTitle.setTextColor(textColor)
                textView_noResult.setTextColor(textColor)
                editText_search.setTextColor(textColor)
                editText_search.setHintTextColor(Color.argb(100, Color.red(textColor), Color.green(textColor), Color.blue(textColor)))
            }


            //editText tint
            if (codePicker.getDialogSearchEditTextTintColor() !== 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    editText_search.backgroundTintList = ColorStateList.valueOf(codePicker.getDialogSearchEditTextTintColor())
                    setCursorColor(editText_search, codePicker.getDialogSearchEditTextTintColor())
                }
            }


            //add messages to views
            textViewTitle.setText(codePicker.getDialogTitle())
            editText_search.setHint(codePicker.getSearchHintText())
            textView_noResult.setText(codePicker.getNoResultFoundText())

            //this will make dialog compact
            if (!codePicker.isSearchAllowed()) {
                val params = recyclerView_countryDialog.layoutParams as RelativeLayout.LayoutParams
                params.height = RecyclerView.LayoutParams.WRAP_CONTENT
                recyclerView_countryDialog.layoutParams = params
            }

            val cca = CountryCodeAdapter(context, masterCountries, codePicker, rlQueryHolder, editText_search, textView_noResult, dialog, imgClearQuery)
            recyclerView_countryDialog.layoutManager = LinearLayoutManager(context)
            recyclerView_countryDialog.adapter = cca

            //fast scroller
            val fastScroller = dialog.findViewById(R.id.fastscroll) as FastScroller
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