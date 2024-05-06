package com.github.emlano.sportsview

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.emlano.sportsview.logic.entity.Team
import com.github.emlano.sportsview.logic.getAllTeams
import com.github.emlano.sportsview.ui.theme.SportsViewTheme
import kotlinx.coroutines.launch

class GetTeamJerseyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SportsViewTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GetTeamJersysScreen(modifier = Modifier.padding(innerPadding), context = this)
                }
            }
        }
    }
}

@Composable
fun GetTeamJersysScreen(modifier: Modifier, context: Context) {
    var searchStr by rememberSaveable { mutableStateOf("") }
    var searchedTeams by rememberSaveable { mutableStateOf(listOf<Team>()) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.get_team_jersey),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.padding(15.dp))
        TextField(
            value = searchStr,
            onValueChange = { searchStr = it },
            label = { Text(text = stringResource(id = R.string.enter_team_name)) }
        )
        Spacer(modifier = Modifier.padding(15.dp))
        Button(onClick = {
            if (searchStr.isEmpty()) return@Button
            var teams = listOf<Team>()

            scope.launch {
                teams = getAllTeams()
            }

            searchedTeams = teams.filter { it.name.uppercase().contains(searchStr, ignoreCase = true) }.toList()



        }) {
            Text(text = stringResource(id = R.string.search))
        }
        Spacer(modifier = Modifier.padding(15.dp))
        Box(modifier = modifier.border(width = 2.dp, color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(15.dp))) {
            LazyColumn {
                items(searchedTeams.size) {
                    Text(text = searchedTeams[it].name)
                }
            }
        }
    }
}