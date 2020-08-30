package vkommineni.com.contacttest

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import java.lang.String


class MyCustomAdapter(
    context: Context,
     private val resource: Int,
     private val contactsInfoList: ArrayList<ContactsInfo>
) : ArrayAdapter<ContactsInfo>(context,resource,contactsInfoList) {

    private inner class ViewHolder {
        var displayName: TextView? = null
        var phoneNumber: TextView? = null
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        var holder: ViewHolder? = null
        if (convertView == null) {
            val vi =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = vi.inflate(R.layout.contact_info, null)
            holder = ViewHolder()
            holder.displayName = convertView!!.findViewById<View>(R.id.displayName) as TextView
            holder.phoneNumber =
                convertView.findViewById<View>(R.id.phoneNumber) as TextView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val contactsInfo = contactsInfoList[position]
        holder!!.displayName!!.text = contactsInfo.displayName
        holder.phoneNumber!!.text = contactsInfo.phoneNumber
        convertView.setOnClickListener(View.OnClickListener {

            val intent = Intent(Intent.ACTION_VIEW)
            val uri = Uri.withAppendedPath(
                ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                String.valueOf(contactsInfo.contactId)
            )
            intent.data = uri
            context.startActivity(intent)
          /*  val intent = Intent(Intent.ACTION_VIEW)
            intent.type = ContactsContract.Contacts.

// Just two examples of information you can send to pre-fill out data for the
// user.  See android.provider.ContactsContract.Intents.Insert for the complete
// list.

// Just two examples of information you can send to pre-fill out data for the
// user.  See android.provider.ContactsContract.Intents.Insert for the complete
// list.

            intent.putExtra(ContactsContract.Intents.Insert.NAME, holder.displayName?.text)
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, holder.phoneNumber?.text)

            intent.putExtra(
                ContactsContract.Intents.Insert.FULL_MODE,
                true
            ) //skips the dialog box that asks the user to confirm creation of contacts

            context.startActivity(intent)
            */
        })
        return convertView!!
    }



}