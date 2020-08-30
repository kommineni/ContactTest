package vkommineni.com.contacttest

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Telephony
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi


class MySmsAdapter(
    context: Context,
    private val resource: Int,
    private val contactsInfoList: ArrayList<SmsInfo>
) : ArrayAdapter<SmsInfo>(context,resource,contactsInfoList) {

    private inner class ViewHolder {
        var displayName: TextView? = null
        var phoneNumber: TextView? = null
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
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
        holder!!.displayName!!.text = contactsInfo.address
        holder.phoneNumber!!.text = contactsInfo.smsBody
        convertView.setOnClickListener(View.OnClickListener {

// The following block of code working fine for other than Samsung Devices.


            val popup = Intent(Intent.ACTION_SENDTO)

            // Should *NOT* be using FLAG_ACTIVITY_MULTIPLE_TASK however something
            // is broken on
            // a few popular devices that received recent Froyo upgrades that means
            // this is required
            // in order to refresh the system compose message UI

            // Should *NOT* be using FLAG_ACTIVITY_MULTIPLE_TASK however something
            // is broken on
            // a few popular devices that received recent Froyo upgrades that means
            // this is required
            // in order to refresh the system compose message UI
            val flags = Intent.FLAG_ACTIVITY_NEW_TASK or  // Intent.FLAG_ACTIVITY_SINGLE_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
            // Intent.FLAG_ACTIVITY_MULTIPLE_TASK;

            // Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
            popup.flags = flags


                // Log.v("^^Found threadId (" + threadId +
                // "), sending to Sms intent");
                popup.data = Uri.parse("smsto:" + Uri.encode(contactsInfo.address))

            popup.putExtra(Intent.EXTRA_REFERRER_NAME, contactsInfo.smsBody)
            popup.putExtra("position", contactsInfo.smsId)
           context.startActivity(popup)
//            Telephony

  /*          val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.fromParts(
                    "sms",
                    contactsInfo.threadId,
                    null
                )
            )

//            val uri = ContentUris.withAppendedId(
//                Uri.parse("content://sms/conversations/"),
//                mId.toLong()
//            )
            println("intent ::$intent")
            intent.setPackage(Telephony.Sms.getDefaultSmsPackage(context))
            intent.putExtra("intent_extra_data_key", contactsInfo.smsBody)
            context.startActivity(intent)*/




/*

            val defineIntent = Intent(Intent.ACTION_VIEW)
            var uri1 = Telephony.Sms.Conversations.CONTENT_URI
                .buildUpon()
                .appendPath(contactsInfo.threadId)
                .build()
            val uri =
                Uri.parse("sms:"+ contactsInfo.address)
            defineIntent.putExtra("sms_id", contactsInfo.smsId)

            defineIntent.data = uri

//            defineIntent.data = Uri.parse(Telephony.Sms._ID +"/"+contactsInfo.threadId)

            defineIntent.setPackage(Telephony.Sms.getDefaultSmsPackage(context))
            context.startActivity(defineIntent)*/

          /*  val intent = Intent(Intent.ACTION_QUICK_VIEW)
            val uri = Uri.withAppendedPath(
                Telephony.MmsSms.CONTENT_CONVERSATIONS_URI,
                contactsInfo.threadId + "/" + contactsInfo.smsId
            )
           intent.data = uri
            intent.setPackage(Telephony.Sms.getDefaultSmsPackage(context))
            context.startActivity(intent)*/



//            println("intent ::$intent")

            println(" address: ${contactsInfo.address}  threadId : ${contactsInfo.threadId} mesgId : ${contactsInfo.smsId}" )


            /*val intent = Intent(Intent.ACTION_MAIN)
            intent.data = Uri.parse("content://mms-sms/conversations/${contactsInfo.address}")
            context.startActivity(intent)*/
/*
// This block of coding working fine for Samsung devices
               val defineIntent = Intent(Intent.ACTION_VIEW)
            defineIntent.data = Uri.parse("content://mms-sms/conversations/${contactsInfo.threadId}")
            context.startActivity(defineIntent)
            */


        })
        return convertView!!

    }
}
