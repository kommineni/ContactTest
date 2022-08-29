package vkommineni.com.contacttest

import android.Manifest
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Telephony
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.w3c.dom.Text


class MainActivity : AppCompatActivity() {
    var dataAdapter: MyCustomAdapter? = null
    var smsAdapter: MySmsAdapter? = null
    var listView: ListView? = null
    var btnGetContacts: Button? = null
    var btnGetSms: Button? = null
    var listTitle:TextView? = null
    var edt_search_key:EditText? = null
    var contactsInfoList: ArrayList<ContactsInfo>? = null
    var smsInfoList: ArrayList<SmsInfo>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnGetContacts = findViewById<View>(R.id.btnGetContacts) as Button
        btnGetSms = findViewById<View>(R.id.btnGetSMS) as Button
        listTitle = findViewById<View>(R.id.list_title) as TextView
        edt_search_key = findViewById<EditText>(R.id.edt_search_key) as EditText

        listView = findViewById<View>(R.id.lstContacts) as ListView
        listView!!.adapter = dataAdapter

        btnGetContacts!!.setOnClickListener {
            contactsInfoList?.clear()
            smsInfoList?.clear()
            listTitle?.setText("Contacts")
            requestContactPermission()
        }

        btnGetSms!!.setOnClickListener {
            smsInfoList?.clear()
            contactsInfoList?.clear()
            listTitle?.setText("Sms")
            requestSmsPermission()
        }

        println("Test Print for Git commit ")
    }

    private val contacts: Unit
        private get() {
            val contentResolver = contentResolver
            var contactId: String? = null
            var displayName: String? = null
            val uri: Uri = Uri.withAppendedPath(
                ContactsContract.Contacts.CONTENT_FILTER_URI,
                Uri.encode(edt_search_key?.text.toString())
            )
            var selection:String = ContactsContract.Contacts.DISPLAY_NAME +" like'%" + Uri.encode(edt_search_key?.text.toString()) +"%'"

            contactsInfoList = ArrayList<ContactsInfo>()
            val cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, selection, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC")
            if (cursor!!.count > 0) {
                while (cursor.moveToNext()) {
                    var phoneNumber:String = "nothing"
                    val hasPhoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)).toInt()
                    if (hasPhoneNumber > 0) {
                        contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))
                        displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                        val phoneCursor = getContentResolver().query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY + " = ?", arrayOf<String?>(contactId),
                                null)
                        if (phoneCursor!!.moveToNext()) {
                             phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                        }
                        phoneCursor.close()
                        contactsInfoList?.add(ContactsInfo(contactId,displayName,phoneNumber))
                    }
                }
            }
            cursor.close()
            dataAdapter = MyCustomAdapter(this, R.layout.contact_info, contactsInfoList!!)
            listView!!.adapter = dataAdapter
            dataAdapter?.notifyDataSetChanged()
        }

    private fun requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.READ_CONTACTS)) {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Read contacts access needed")
                    builder.setPositiveButton(android.R.string.ok, null)
                    builder.setMessage("Please enable access to contacts.")
                    builder.setOnDismissListener { requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), PERMISSIONS_REQUEST_READ_CONTACTS) }
                    builder.show()
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS),
                            PERMISSIONS_REQUEST_READ_CONTACTS)
                }
            } else {
                contacts
            }
        } else {
            contacts
        }
    }


    private fun requestSmsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_SMS)) {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Read contacts access needed")
                    builder.setPositiveButton(android.R.string.ok, null)
                    builder.setMessage("Please enable access to contacts.")
                    builder.setOnDismissListener { requestPermissions(arrayOf(Manifest.permission.READ_SMS), PERMISSIONS_REQUEST_READ_SMS) }
                    builder.show()
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_SMS),
                        PERMISSIONS_REQUEST_READ_SMS)
                }
            } else {
                sms
            }
        } else {
            sms
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    contacts
                } else {
                    Toast.makeText(this, "You have disabled a contacts permission", Toast.LENGTH_LONG).show()
                }
                return
            }
            PERMISSIONS_REQUEST_READ_SMS -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sms
                } else {
                    Toast.makeText(this, "You have disabled a SMS permission", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    companion object {
        const val PERMISSIONS_REQUEST_READ_CONTACTS = 1
        const val PERMISSIONS_REQUEST_READ_SMS = 2
    }


    private val CURSOR_PROJECTION: Array<out String> = arrayOf(
        Telephony.Sms.Inbox._ID,
        Telephony.Sms.Inbox.ADDRESS,
        Telephony.Sms.Inbox.BODY
    )

    private val sms: Unit
        private get() {

            val message = Uri.parse("content://sms/")
            val cr: ContentResolver = this.contentResolver


            val selection = Telephony.Sms.Inbox.BODY + " LIKE ?"
            val selectionArgs = arrayOf("%${edt_search_key?.text.toString()}%")

           /* val c = contentResolver.query(
                Telephony.Sms.Inbox.CONTENT_URI,
                CURSOR_PROJECTION,
                selection,
                selectionArgs,
                null
            )*/

        /*    val messageId = String(rawMessageId)
            val selection = ("(" + Telephony.Mms.MESSAGE_ID + " = ? AND "
                    + Telephony.Mms.MESSAGE_TYPE + " = ?)")
            val selectionArgs = arrayOf<String>(
                messageId,
                java.lang.String.valueOf(PduHeaders.MESSAGE_TYPE_RETRIEVE_CONF)
            )

            val cursor: Cursor = SqliteWrapper.query(
                context, context.contentResolver,
                Telephony.Mms.CONTENT_URI, arrayOf(Telephony.Mms._ID, Telephony.Mms.SUBJECT, Telephony.Mms.SUBJECT_CHARSET),
                selection, selectionArgs, null
            )*/

            val c: Cursor? = cr.query(message, null, selection, selectionArgs, null)

            smsInfoList = ArrayList<SmsInfo>()

            println("Items count ::" + c?.count)

            if(c?.count!! > 0 ){
                while (c?.moveToNext()) {

                    var folder = if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                        "inbox"
                    } else {
                        "sent"
                    }
                   val address = (c.getString(c.getColumnIndexOrThrow("address"))).replace("[^0-9]".toRegex(), "")
                    var objSms = SmsInfo(
                        c.getString(c.getColumnIndexOrThrow("_id")),
                        address,
                        c.getString(c.getColumnIndexOrThrow("body")),
                        c.getString(c.getColumnIndexOrThrow("date")),
                        c.getString(c.getColumnIndexOrThrow("thread_id")),
                        folder
                    )
                    smsInfoList?.add(objSms)
            }
        }

            c.close()

            println("Items count ::" + smsInfoList?.size)
            smsAdapter = MySmsAdapter(this, R.layout.contact_info, smsInfoList!!)
            listView!!.adapter = smsAdapter
            smsAdapter?.notifyDataSetChanged()

        }





/*
    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
    smsIntent.setType("vnd.android-dir/mms-sms"); or intent.setData(Uri.parse("sms:"))
    smsIntent.putExtra("address", "12125551212");
    smsIntent.putExtra("sms_body","Body of Message");
    startActivity(smsIntent);*/


}