package com.example.myapplication

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity(){
    lateinit var dbHelper: DBHelper
    private val READ_CONTACTS_PERMISSION_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Check if the READ_CONTACTS permission is granted
        // Initialize DBHelper with the context (this)
        dbHelper = DBHelper.getInstance(applicationContext)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is granted, proceed with your logic
            //fetchContactNames()
        } else {
            // Permission is not granted, request it
            requestPermission()
        }
        setContent {
            MyApplicationTheme {
                val myApplication = application as MyApplication
                MainPage(
                    this,
                    myApplication.contactViewModel
                )
            }
        }
    }

    private fun requestPermission() {
        // Request the READ_CONTACTS permission
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_CONTACTS),
            READ_CONTACTS_PERMISSION_CODE
        )
    }

    // Handle the result of the permission request
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_CONTACTS_PERMISSION_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission granted, proceed with your logic
                    //fetchContactNames()
                } else {
                    // Permission denied, handle accordingly (show a message, etc.)
                    finish()
                }
                return
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(
    mainActivity: MainActivity,
    viewModel: ContactViewModel
)
{
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    )
    {
        // Top App Bar
        //val contactList by viewModel.contactList.collectAsState()
        TopAppBar(
            title = { Text("Contacts App") },
            actions = {
                // Delete Contacts Button
                IconButton(onClick = {
                    // Create an explicit intent to start SecondActivity
                    val intent = Intent(mainActivity, DeleteContactsActivity::class.java)

                    // Add any extra data you want to pass to SecondActivity
                    //intent.putExtra("key", "Hello from MainActivity!")

                    // Start SecondActivity
                    mainActivity.startActivity(intent)
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }
                // Add Contacts Button
                IconButton(onClick = {
                    // Create an explicit intent to start SecondActivity
                    val intent = Intent(mainActivity, AddContactsActivity::class.java)

                    // Add any extra data you want to pass to SecondActivity
                    //intent.putExtra("key", "Hello from MainActivity!")

                    // Start SecondActivity
                    mainActivity.startActivity(intent)
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }
        )

        val contactList by viewModel.contactList.observeAsState(emptyList())

        // Contacts List
        LazyColumn (
            modifier = Modifier
                .padding(8.dp)
        ){
            items(contactList) { contact ->
                ContactListItem(contact = contact) {
                    // Handle item click and navigate to ContactPage
                    // Create an explicit intent to start SecondActivity
                    val intent = Intent(mainActivity, ContactViewActivity::class.java)

                    // Add any extra data you want to pass to SecondActivity
                    intent.putExtra("key", contact.id)

                    // Start SecondActivity
                    mainActivity.startActivity(intent)
                }
                Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
}

@Composable
fun ContactListItem(contact: Contact, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        // Image Holder
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        ) {
            // You can load the contact image here
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Contact Name
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = contact.name,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        // Person Icon
        Icon(imageVector = Icons.Default.Person, contentDescription = null)
    }
}

// Function to generate a Toast
public fun mToast(context: Context, message: String){
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

@Preview(showBackground = true)
@Composable
fun ContactsAppPreview() {

    MyApplicationTheme {

    }
}