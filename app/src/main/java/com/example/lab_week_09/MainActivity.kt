package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lab_week_09.ui.theme.OnBackgroundItemText
import com.example.lab_week_09.ui.theme.OnBackgroundTitleText
import com.example.lab_week_09.ui.theme.PrimaryTextButton
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.net.URLDecoder

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
                    val navController = rememberNavController()
                    App(navController = navController)
                }
            }
        }
    }
}

data class Student(
    var name: String
)

@Composable
fun App(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            Home { json ->
                navController.navigate("resultContent/$json")
            }
        }

        composable(
            "resultContent/{listData}",
            arguments = listOf(navArgument("listData") {
                type = NavType.StringType
            })
        ) {
            val json = it.arguments?.getString("listData").orEmpty()
            ResultContent(
                json
            )
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
fun Home(
    navigateFromHomeToResult: (String) -> Unit
) {
    /*
    * Kita membuat mutable state list of students
    * Kita menggunakan remember supaya membuat list nya
    * mengingat value nya
    *
    * Supaya list ga bakal di recreate ketika composable nya
    * recomposes */

    // intinya, ini kek make useState di react
    val listData = remember { mutableStateListOf(
        Student("Tanu"),
        Student("Tina"),
        Student("Tono")
    ) }

    /* Disini, kita buat mutable state of Student
    * supaya kita bisa dpt value dari input field */
    var inputField = remember { mutableStateOf(Student("")) }

    /* Disini kita panggil HomeContent composable
    * Kita pass:
    * 1. listData untuk menampilkan lsit of items dalam HomeContent
    * 2. inputField =untuk menam[pilkan input vield value dalam HomeContent
    * 3. lambda function untuk update value inputField
    * 4. lambda functrion untuk menambah inputfield ke listData
    */
    HomeContent(
        listData,
        inputField.value,
        { input -> inputField.value = inputField.value.copy(input)},
        {
            if (inputField.value.name.isNotBlank()) {
                listData.add(inputField.value)
                inputField.value = Student("")
            }
        },
        {
            val moshi = Moshi.Builder()
                .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
                .build()
            val type = Types.newParameterizedType(List::class.java, Student::class.java)
            val adapter = moshi.adapter<List<Student>>(type)
            val json = adapter.toJson(listData)

            val encodedJson = java.net.URLEncoder.encode(json, "UTF-8")

            navigateFromHomeToResult(encodedJson)
        }
    )
}

/*untuk display content dari Home Composable*/
@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    navigateFromHomeToResult: () -> Unit
) {
    // lazy column untuk display items lazily
    LazyColumn {
        item {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // panggil OnBackgroundTitleText UI Element
                OnBackgroundTitleText(
                    text = stringResource(id = R.string.enter_item)
                )

                TextField(
                    value = inputField.name,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    onValueChange = {
                        // panggil lambda onInputValueCahnge
                        onInputValueChange(it)
                    }
                )

                Row {
                    PrimaryTextButton(text = stringResource(id = R.string.button_click)) {
                        onButtonClick()
                    }

                    PrimaryTextButton(text = stringResource(id = R.string.button_navigate)) {
                        navigateFromHomeToResult()
                    }
                }


            }
        }

        items(listData) { item ->
            Column(
                modifier = Modifier.padding(vertical = 4.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnBackgroundItemText(text = item.name)
            }
        }
    }
}

// membuat preview function dari home composable
// func ini hanya untuk dev purposes dan secara spesifik hanya menampilkan pewview home composable
//@Preview(showBackground = true)
//@Composable
//fun PreviewHome() {
//    Home()
//}

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

@Composable
fun ResultContent(listData: String) {
    // Decode the JSON from URL-safe format
    val decodedJson = URLDecoder.decode(listData, "UTF-8")

    // Parse JSON to List<Student> using Moshi
    val moshi = Moshi.Builder()
        .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
        .build()
    val type = Types.newParameterizedType(List::class.java, Student::class.java)
    val adapter = moshi.adapter<List<Student>>(type)
    val students = adapter.fromJson(decodedJson) ?: emptyList()

    // Display using LazyColumn
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(students) { student ->
            OnBackgroundItemText(
                text = student.name
            )
        }
    }
}

