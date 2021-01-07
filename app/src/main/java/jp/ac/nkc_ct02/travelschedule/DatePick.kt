package jp.ac.nkc_ct02.travelschedule

import android.os.Bundle
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import androidx.fragment.app.DialogFragment;
import java.util.Calendar

class DatePick : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            this.context as Context,
            activity as MainActivity?,
            year,
            month,
            day)
    }

    override fun onDateSet(view: android.widget.DatePicker, year: Int,
                           monthOfYear: Int, dayOfMonth: Int) {

    }

}