package app.hankdev.protodatastoredemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import app.hankdev.protodatastoredemo.proto.User
import app.hankdev.protodatastoredemo.ui.theme.ProtoDataStoreDemoTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var text by mutableStateOf("")
    private var flowText by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProtoDataStoreDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        ActionButton("save user") {
                            lifecycleScope.launch {
                                saveUser("Hank", 35)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        ActionButton("read user") {
                            lifecycleScope.launch {
                                val user = readUser()
                                text = "from function: name: ${user.name}, age: ${user.age}"
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        MessageLabel(text)
                        MessageLabel(flowText)
                    }
                }
            }
        }

        lifecycleScope.launch {
            dataStore.data.collect { user ->
                flowText = "from flow: name: ${user.name}, age: ${user.age}"
            }
        }
    }

    suspend fun readUser(): User {
        return dataStore.data.first()
    }

    suspend fun saveUser(name: String, age: Int) {
        dataStore.updateData { user ->
            user.toBuilder()
                .setName(name)
                .setAge(age)
                .build()
        }
    }
}

@Composable
fun ActionButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onClick) {
            Text(text)
        }
    }
}

@Composable
fun MessageLabel(text: String) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = text
        )
    }
}