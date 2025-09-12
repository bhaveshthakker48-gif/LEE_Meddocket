package org.impactindiafoundation.iifllemeddocket.Utils


import android.content.Context
import android.net.ConnectivityManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import org.impactindiafoundation.iifllemeddocket.R

import java.util.regex.Pattern


/**
 * Created by SIR.WilliamRamsay on 03-Dec-15.
 */
object MyValidator {
    private const val EMAIL_REGEX =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)(\\.[A-Za-z]{2,})$"
    private const val PHONE_REGEX = "\\d{3}-\\d{7}"
    private const val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    private const val REQUIRED_MSG = "Field required"
    private const val AADHAR_REGEX ="^[2-9]{1}[0-9]{3}[0-9]{4}[0-9]{4}\$"
    private const val PINCODE_REGEX="^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$"
    private const val PANCARD_REGEX="[A-Z]{5}[0-9]{4}[A-Z]{1}"
    private const val GST_REGEX= ("^[0-9]{2}[A-Z]{5}[0-9]{4}"
            + "[A-Z]{1}[1-9A-Z]{1}"
            + "Z[0-9A-Z]{1}$")
    private const val IFSC_REGEX="^[A-Z]{4}0[A-Z0-9]{6}$"
    private const val  DRIVING_LICENSE_NUMBER_REGEX= ("^(([A-Z]{2}[0-9]{2})"
            + "( )|([A-Z]{2}-[0-9]"
            + "{2}))((19|20)[0-9]"
            + "[0-9])[0-9]{7}$")
    private const val PASSPORT_REGEX= ("^[A-PR-WYa-pr-wy][1-9]\\d"
            + "\\s?\\d{4}[1-9]$")

    var view:View?=null

    private const val LINKEDIN_REGEX= "(https?:\\\\/\\\\/(www.)?linkedin.com\\\\/(mwlite\\\\/|m\\\\/)?in\\\\/[a-zA-Z0-9_.-]+\\\\/?)"
    private const val LINKED_REGEX="^https:\\\\/\\\\/[a-z]{2,3}\\\\.linkedin\\\\.com\\\\/.*\$"
    //^https?://((www|\w\w)\.)?linkedin.com/((in/[^/]+/?)|(pub/[^/]+/((\w|\d)+/?){3}))$
    private const val FACEBOOK_REGEX="/(?:https?:\\/\\/)?(?:www\\.)?(?:facebook|fb|m\\.facebook)\\.(?:com|me)\\/(?:(?:\\w)#!\\/)?(?:pages\\/)?(?:[\\w\\-]\\/)*([\\w\\-\\.]+)(?:\\/)?/i"
    private const val TWITTER_REGEX="/(?:http:\\/\\/)?(?:www\\.)?twitter\\.com\\/(?:(?:\\w)#!\\/)?(?:pages\\/)?(?:[\\w\\-]\\/)([\\w\\-])/"
    private const val DRIVE_LINK_REGEX="https://drive\\\\.google\\\\.com/file/d/(.?)/.?\\\\?usp=sharing\", \"https://drive.google.com/uc?export=download&id=\$1"
    private const val WEBSITE_URL="^((ftp|http|https):\\/\\/)?(www.)?(?!.(ftp|http|https|www.))[a-zA-Z0-9_-]+(\\.[a-zA-Z]+)+((\\/)[\\w#]+)(\\/\\w+\\?[a-zA-Z0-9_]+=\\w+(&[a-zA-Z0-9_]+=\\w+)*)?\$"


    // validating email id
    fun isValidEmail(editText: EditText): Boolean {
        Log.d(ConstantsApp.TAG, "validate method call")
        val email = editText.text.toString().trim { it <= ' ' }
        val pattern = Pattern.compile(emailRegex)
        val matcher = pattern.matcher(email)
        if (!matcher.matches()) {
            editText.error = "Enter valid Email"
            return false
        }
        editText.error = null
        return true
    }
    fun isValidWebsite(editText: EditText): Boolean {
        Log.d(ConstantsApp.TAG, "validate method call")
        val email = editText.text.toString().trim { it <= ' ' }
        val pattern = Pattern.compile(WEBSITE_URL)
        val matcher = pattern.matcher(email)
        if (!matcher.matches()) {
            editText.error = "Enter valid Website URL"
            return false
        }
        editText.error = null
        return true
    }
    fun isValidAadhar(editText: EditText): Boolean {
        Log.d(ConstantsApp.TAG, "validate method call")
        val email = editText.text.toString().trim { it <= ' ' }
        val pattern = Pattern.compile(AADHAR_REGEX)
        val matcher = pattern.matcher(email)
        if (!matcher.matches()) {
            editText.error = "Enter valid Aadhar Number"
            return false
        }
        editText.error = null
        return true
    }
    fun isValidPancard(editText: EditText): Boolean {
        Log.d(ConstantsApp.TAG, "validate method call")
        val email = editText.text.toString().trim { it <= ' ' }
        val pattern = Pattern.compile(PANCARD_REGEX)
        val matcher = pattern.matcher(email)
        if (!matcher.matches()) {
            editText.error = "Enter valid Pancard Number"
            return false
        }
        editText.error = null
        return true
    }
    fun isValidPinCode(editText: EditText): Boolean {
        Log.d(ConstantsApp.TAG, "validate method call")
        val email = editText.text.toString().trim { it <= ' ' }
        val pattern = Pattern.compile(PINCODE_REGEX)
        val matcher = pattern.matcher(email)
        if (!matcher.matches()) {
            editText.error = "Enter valid Pincode Number"
            return false
        }
        editText.error = null
        return true
    }
    fun isValidGST(editText: EditText): Boolean {
        Log.d(ConstantsApp.TAG, "validate method call")
        val email = editText.text.toString().trim { it <= ' ' }
        val pattern = Pattern.compile(GST_REGEX)
        val matcher = pattern.matcher(email)
        if (!matcher.matches()) {
            editText.error = "Enter valid GST Number"
            return false
        }
        editText.error = null
        return true
    }
    fun isValidIFSC(editText: EditText): Boolean {
        Log.d(ConstantsApp.TAG, "validate method call")
        val email = editText.text.toString().trim { it <= ' ' }
        val pattern = Pattern.compile(IFSC_REGEX)
        val matcher = pattern.matcher(email)
        if (!matcher.matches()) {
            editText.error = "Enter valid IFSC Number"
            return false
        }
        editText.error = null
        return true
    }
    fun isValidLinkedIn(editText: EditText): Boolean {
        Log.d(ConstantsApp.TAG, "validate method call")
        val email = editText.text.toString().trim { it <= ' ' }
        val pattern = Pattern.compile(LINKEDIN_REGEX)
        val matcher = pattern.matcher(email)
        if (!matcher.matches()) {
            editText.error = "Enter valid LinkedIn URL"
            return false
        }
        editText.error = null
        return true
    }
    fun isEqual(editText:EditText,editText1:EditText):Boolean
    {
        val data=editText.text.toString()
        val data1=editText1.text.toString()
        if (!data.toString().equals(data1))
        {
            editText1.error = "Enter Valid Password"
            return false
        }
        return true
    }
    fun isNotEqual(editText:EditText,editText1:EditText):Boolean
    {
        val data=editText.text.toString()
        val data1=editText1.text.toString()
        if (data.toString().equals(data1))
        {
            editText1.error = "Enter Valid Name"
            return false
        }
        return true
    }
    fun isValidDriving(editText: EditText): Boolean {
        Log.d(ConstantsApp.TAG, "validate method call")
        val email = editText.text.toString().trim { it <= ' ' }
        val pattern = Pattern.compile(DRIVING_LICENSE_NUMBER_REGEX)
        val matcher = pattern.matcher(email)
        if (!matcher.matches()) {
            editText.error = "Enter Valid Driving license Number"
            return false
        }
        editText.error = null
        return true
    }
    fun isValidPassport(editText: EditText): Boolean {
        Log.d(ConstantsApp.TAG, "validate method call")
        val email = editText.text.toString().trim { it <= ' ' }
        val pattern = Pattern.compile(PASSPORT_REGEX)
        val matcher = pattern.matcher(email)
        if (!matcher.matches()) {
            editText.error = "Enter Valid Passport Number"
            return false
        }
        editText.error = null
        return true
    }
    fun isValidEmailPhone(editText: EditText):Boolean
    {
        Log.d(ConstantsApp.TAG, "validate method call isValidEmailPhone")
        val isDigits = TextUtils.isDigitsOnly(editText.getText().toString())
        if(isDigits==false)
        {
            val email = editText.text.toString().trim { it <= ' ' }
            val pattern = Pattern.compile(emailRegex)

            val matcher = pattern.matcher(email)
            if (!matcher.matches() )
            {
                editText.error = "Enter Valid Email"
                return false
            }

        }
        else
        {
            val mob = editText.text.toString().trim { it <= ' ' }
            if (mob != null && mob.length == 10) {
                editText.error = null
                return true
            }
            editText.error = "Enter Valid Phone No"
            return false
        }

        /* else if (mob != null && mob.length> 10)
         {
             editText.error = null
             return true
         }
         editText.error = REQUIRED_MSG + " Enter 10 digits"*/
        return true
    }
    // validating password
    fun isValidPassword(editText: EditText): Boolean {
        val pass = editText.text.toString().trim { it <= ' ' }
        if (pass != null && pass.length > 3) {
            editText.error = null
            return true
        }
        editText.error = "Enter Valid Password"
        return false
    }
    fun isValidRefralCode(editText: EditText): Boolean {
        val refral_code = editText.text.toString().trim { it <= ' ' }
        if (refral_code.length==0 ||(refral_code.length>=6 && refral_code.length < 8)) {
            editText.error = null
            return true
        }
        editText.error = "Enter Valid Refral Code"
        return false
    }

    fun isValidField(editText: EditText): Boolean {
        val txtValue = editText.text.toString().trim { it <= ' ' }
        if (txtValue.length == 0) {
            editText.error = REQUIRED_MSG
            return false
        }
        editText.error = null
        return true
    }

    fun isValidTextView(editText: TextView): Boolean {
        val txtValue = editText.text.toString().trim { it <= ' ' }
        if (txtValue.length == 0) {
            editText.error = REQUIRED_MSG
            return false
        }
        editText.error = null
        return true
    }

    fun isValidFieldAutoCompleteTextView(autoCompleteTextView: AutoCompleteTextView): Boolean {
        val txtValue = autoCompleteTextView.text.toString().trim()
        if (txtValue.isEmpty()) {
            autoCompleteTextView.error = REQUIRED_MSG
            return false
        }
        autoCompleteTextView.error = null
        return true
    }




    fun isValidField1(editText: EditText): Boolean {
        val txtValue = editText.text.toString().trim()

        if (txtValue.isEmpty()) {
            // Set error message
            editText.error = REQUIRED_MSG
            // Set error icon (optional)
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_battery_alert_24, 0)
            // Request focus to the EditText for better visibility (optional)
            editText.requestFocus()
            return false
        }

        // Clear error and error icon if the field is valid
        editText.error = null
        editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)

        return true
    }


    /* fun isValidSpinner(spinner: Spinner): Boolean {
         val view = spinner.selectedView
         val textView = view as TextView
         if (spinner.selectedItemPosition == 0) {
             textView.error = "None selected"
             return false
         }
         textView.error = null
         return true
     }*/

    /*fun isValidSpinner(spinner: Spinner): Boolean {
        val view = spinner.selectedView

        // Check if the selected view is an instance of TextView
        if (view is TextView) {
            val textView = view as TextView
            if (spinner.selectedItemPosition == 0) {
                textView.error = "None selected"
                return false
            }
            textView.error = null
            return true
        } else {
            // Handle the case where the selected view is not a TextView
            // You might want to log a message or take appropriate action
            return false
        }
    }*/

   /* fun isValidSpinner(spinner: Spinner): Boolean {
        val view = spinner.selectedView

        // Check if the selected view is an instance of TextView
        if (view is TextView) {
            val textView = view as TextView
            if (spinner.selectedItemPosition == 0) {
                textView.error = "None selected"
                return false
            }
            textView.error = null
            return true
        }

        // Handle the case where the selected view is not a TextView
        // You might want to log a message or take appropriate action
        return true
    }*/

    fun isValidSpinner3(spinner: Spinner): Boolean {
        val selectedPosition = spinner.selectedItemPosition

        if (selectedPosition == 0) {
            // No item selected
            // Display an error message or take appropriate action
            val errorText = "Please select an item"
            val errorView = spinner.selectedView as TextView
            errorView.error = errorText
            return false
        } else {
            // Valid item selected
            // Clear any previous error message
            val errorView = spinner.selectedView as TextView
            errorView.error = null
            return true
        }
    }

    fun isValidSpinner(spinner: Spinner, errorMessage: String): Boolean {
        val selectedPosition = spinner.selectedItemPosition

        if (selectedPosition == 0) {
            // No item selected
            // Display an error message or take appropriate action
            //val errorText = "Please select an item"
            val errorText = errorMessage

            // Check if the selected view is an instance of TextView
            val selectedView = spinner.selectedView
            if (selectedView is TextView) {
                val errorView = selectedView as TextView
                errorView.error = errorText
            } else {
                // Handle the case where the selected view is not a TextView
                // For example, log a message or take appropriate action
            }

            return false
        } else {
            // Valid item selected
            // Clear any previous error message
            val errorView = spinner.selectedView as? TextView
            errorView?.error = null
            return true
        }
    }

    fun isValidSpinnerr(spinner: Spinner, errorMessage: String): Boolean {
        val selectedPosition = spinner.selectedItemPosition

        if (selectedPosition == 0) {
            // No item selected
            // Display an error message or take appropriate action
            val errorText = errorMessage

            // Check if the selected view is an instance of TextView
            val selectedView = spinner.selectedView as? TextView
            selectedView?.error = errorText

            return false
        } else {
            // Valid item selected
            // Clear any previous error message
            val errorView = spinner.selectedView as? TextView
            errorView?.error = null
            return true
        }
    }


    fun isValidSpinner1(spinner: Spinner, errorMessage: String): Boolean {
        val selectedPosition = spinner.selectedItemPosition

        if (selectedPosition == 0) {
            // No item selected
            // Display an error message or take appropriate action
            val errorText = "$errorMessage: ${spinner.id}" // Include the spinner ID in the error message
            val errorView = spinner.selectedView as? TextView
            errorView?.error = errorText
            return false
        } else {
            // Valid item selected
            // Clear any previous error message
            val errorView = spinner.selectedView as? TextView
            errorView?.error = null
            return true
        }
    }






    fun isValidMobile(editText: EditText): Boolean {
        val mob = editText.text.toString().trim { it <= ' ' }
        if (mob != null && mob.length == 10) {
            editText.error = null
            return true
        }
        editText.error = REQUIRED_MSG + " Enter 10 digits"
        return false
    }

    fun isValidFieldTextView(textView: TextView): Boolean {
        val txtValue = textView.text.toString().trim { it <= ' ' }
        if (txtValue.length == 0) {
            textView.error = REQUIRED_MSG
            return false
        }
        textView.error = null
        return true
    }

   /* fun isValidFieldImageView(imageView: ImageView):Boolean
    {
        val imageViewValue=imageView.getDrawable().toString().trim()
        if (imageViewValue==null)
        {
            imageView.setBackgroundResource(R.drawable.ic_baseline_person_outline_24)
            return false
        }
        return true
    }*/

    fun isValidcheckBox(checkBox: CheckBox):Boolean
    {
        val checkBox=checkBox.isChecked
        if(checkBox==true)
        {
            return true
        }

        return false
    }

    fun isValidRadioGroup(radioGroup: RadioGroup):Boolean
    {
        val selectValue=radioGroup.checkedRadioButtonId
        if (selectValue==-1)
        {
            return false

        }
        return true
    }
    fun isValidRadioGroupChecked(radioGroup: RadioGroup):Boolean
    {

        val textView = view as TextView
        val selectValue=radioGroup.checkedRadioButtonId
        if (selectValue==-1)
        {
            textView.error = "None selected"
            return false

        }
        textView.error = null
        return true
    }



    fun checkConnection(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connMgr != null) {
            val activeNetworkInfo = connMgr.activeNetworkInfo
            if (activeNetworkInfo != null) { // connected to the internet
                // connected to the mobile provider's data plan
                return if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    true
                } else activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE
            }
        }
        return false
    }
    fun isDigits(number: String?): Boolean {
        return if (!TextUtils.isEmpty(number)) {
            TextUtils.isDigitsOnly(number)
        } else {
            false
        }
    }

    fun validateTextView(textView: TextView): Boolean {
        val inputText = textView.text.toString()

        return if (inputText.isEmpty()) {
            textView.error = "This field is required"
            false
        } else {
            // Perform additional validation as needed
            // You can check for specific conditions or patterns using regular expressions or other checks
            true
        }
    }

    fun validateSpinner(spinner: Spinner): Boolean {
        return if (spinner.selectedItem == null) {
            // No item is selected, display an error message or handle it accordingly
            // For example, you can show a Toast message
            spinner.rootView.context?.let { context ->
                Toast.makeText(context, "Please select an item", Toast.LENGTH_SHORT).show()
            }
            false
        } else {
            // Item is selected, continue with the logic
            val selectedValue = spinner.selectedItem.toString()
            // Perform additional actions based on the selected item
            true
        }
    }

    fun isValidSpinner17(spinner: Spinner, errorMessage: String,context: Context): Boolean {
       /* val selectedItemPosition = spinner.selectedItemPosition
        if (selectedItemPosition == AdapterView.INVALID_POSITION) {
            // No item selected, show error
            showToast(errorMessage,context)
            return false
        }
        return true*/

        val selectedItemPosition = spinner.selectedItemPosition
        if (selectedItemPosition == 0) {
            // First position selected, show a specific error message
          //  showToast("Please select something other than the default option", context)
            showToast(errorMessage, context)

            return false
        } else if (selectedItemPosition == AdapterView.INVALID_POSITION) {
            // No item selected, show the provided error message
            showToast(errorMessage, context)
            return false
        }
        return true
    }

    fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }





}