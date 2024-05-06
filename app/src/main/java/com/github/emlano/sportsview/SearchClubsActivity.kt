package com.github.emlano.sportsview

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.emlano.sportsview.ui.theme.SportsViewTheme

class SearchClubsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SportsViewTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SearchClubsScreen(modifier = Modifier.padding(innerPadding), this)
                }
            }
        }
    }
}

@Composable
fun SearchClubsScreen(modifier: Modifier = Modifier, context: Context) {
    var searchStr by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.search_clubs),
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.padding(26.dp))
        TextField(
            value = searchStr, 
            onValueChange = { searchStr = it },
            label = { Text(text = stringResource(id = R.string.enter_team_or_league)) }
        )
        Spacer(modifier = Modifier.padding(18.dp))
        Button(onClick = { /*TODO*/ }) {
            Text(text = stringResource(id = R.string.search))
        }
    }
}