package com.example.recyclerfromfirebase

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recyclerfromfirebase.ui.theme.RecyclerfromFirebaseTheme
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : ComponentActivity() {
    private val databaseReference = FirebaseDatabase.getInstance().reference.child("item")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Text(text = "ID: ", color = Color.Black)
            
            MyApp(databaseReference)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MyApp(databaseReference: DatabaseReference) {
    var data by remember { mutableStateOf(emptyList<YourDataType>()) }

    DisposableEffect(databaseReference) {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newData = snapshot.children.mapNotNull { it.getValue(YourDataType::class.java) }
                data = newData
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        }

        databaseReference.addValueEventListener(valueEventListener)

        // Cleanup the listener when the composable is disposed
        onDispose {
            databaseReference.removeEventListener(valueEventListener)
        }
    }

    //    Scaffold(
    //        topBar = {
    //            TopAppBar(
    //                title = { Text("Firebase RecyclerView") },
    ////                backgroundColor = Color.Blue
    //            )
    //        },
    //        content = {
                FirebaseRecyclerView(data, modifier = Modifier.fillMaxSize())
    //        }
    //    )
}

@Composable
fun FirebaseRecyclerView(
    data: List<YourDataType>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        items(data) { item ->

             YourItemView(item)
        }
    }
}

// Your data class
data class YourDataType(
    val id: String, // Adjust based on your data structure
    val name: String,
    // Add other properties as needed
)


@Composable
fun YourItemView(item: YourDataType) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "ID: ${item.id}", color = Color.Black)
            Text(text = "Name: ${item.name}", color = Color.Black)


            Log.d("TAG", "ID: ${item.id}")
            // Add other properties as needed
        }
    }
}