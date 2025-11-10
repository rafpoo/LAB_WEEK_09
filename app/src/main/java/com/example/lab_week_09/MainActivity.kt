package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme
import androidx.compose.foundation.lazy.items


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LAB_WEEK_09Theme {
                // Surface container untuk menggunakan background color dari theme
                Surface(
                    // modifier.fillMaxSize untuk membuat surface mengisi seluruh layar
                    modifier = Modifier.fillMaxSize(),
                    // MaterialTheme.colorScheme.background untuk get background colornya
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Panggil Home Composable
                    val list = listOf("Tanu", "Tina", "Tono")
                    Home(list)
                }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

/* *
* Disini, kita menghapus tag preview
* Karena, ketika compiler mencoba build preview nya ,
* dia tidak tahu apa yang harus di pass ke dalam
* composable.
*
* Jadi, kita akan membuat composable function baru
* beernama PreviewHome dan kita pass list nya
* sebagai parameter
* */

/* @Composable digunakan untuk memberitahu compiler bahwa ini adalah
* composable function. */
@Composable
fun Home(items: List<String>) {
    /*
    * Disini, kita menggunakan LazyColumn untuk display list of items secara
    * horizontal. Tapi dia hanya render items yang visible saja. jadi lebih
    * efisien.
    */

    LazyColumn {
        // display item di dalam LazyColumn
        item {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(
                    id = R.string.enter_item
                ))

                // text field itu kek input field gitu lho
                TextField(
                    value = "", // value ya berarti value awalnya
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    onValueChange = {  } // apa yg terjadi ketika value berubah
                )

                Button(onClick = {  }) {
                    Text(text = stringResource(id = R.string.button_click))
                }

            }

        }
        items(items) { item ->
            Column(
                modifier = Modifier.padding(vertical = 4.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = item)
            }
        }
    }
}

// membuat preview function dari home composable
// func ini hanya untuk dev purposes dan secara spesifik hanya menampilkan pewview home composable
@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    Home(listOf("Tanu", "Tina", "Tono"))
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LAB_WEEK_09Theme {
        Greeting("Android")
    }
}