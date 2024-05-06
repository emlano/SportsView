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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.emlano.sportsview.logic.SportsDatabase
import com.github.emlano.sportsview.logic.api.fetchTeamsFromLeague
import com.github.emlano.sportsview.logic.parseJsonTeams
import com.github.emlano.sportsview.logic.parseJsonToOutputString
import com.github.emlano.sportsview.ui.theme.SportsViewTheme
import kotlinx.coroutines.launch

class SearchClubByLeagueActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SportsViewTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SearchClubsByLeagueScreen(this, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun SearchClubsByLeagueScreen(context: Context, modifier: Modifier = Modifier) {
    var searchStr by rememberSaveable { mutableStateOf("") }
    var outputStr by rememberSaveable { mutableStateOf("No data") }
    var fetchedTeams by rememberSaveable { mutableStateOf("") }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scroll = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scroll),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = modifier.padding(8.dp),
            text = stringResource(id = R.string.search_clubs_by_league),
            color = MaterialTheme.colorScheme.primary,
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        )
        TextField(
            value = searchStr,
            onValueChange = { searchStr = it},
            label = { Text(text = stringResource(id = R.string.enter_league_name)) }
        )
        Spacer(modifier = Modifier.padding(15.dp))
        Button(onClick = {
            scope.launch {
                // Fetches teams from the API and holds it in fetchedTeams String
                fetchedTeams = fetchTeamsFromLeague(searchStr)
                // outputStr is separate since it is the display string which needs to be
                // formatted.
                outputStr = parseJsonToOutputString(fetchedTeams)
            }
        }) {
            Text(text = stringResource(id = R.string.retrieve_clubs))
        }
        Button(onClick = {
            // The fetchedTeams which contains the unedited JSON is then used to create Team objs
            // and stored in the DB
            val teamList = parseJsonTeams(fetchedTeams)
            if (teamList.isEmpty()) return@Button

            scope.launch {
                val teamDao = SportsDatabase.getInstance(context).teamDao()

                for (i in teamList) {
                    teamDao.addTeam(i)
                }

                showDialog = !showDialog
            }
        }) {
            Text(text = stringResource(id = R.string.store_clubs))
        }
        Box(
            modifier = modifier
                .padding(15.dp)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(15.dp)
                )
                .width(400.dp)
                .height(300.dp)
        ) {
            LazyColumn {
                item {
                    Text(
                        modifier = modifier.padding(5.dp),
                        text = outputStr
                    )
                }
            }
        }

        // Shows an alert when the database is updated
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = !showDialog },
                confirmButton = {
                    Button(onClick = { showDialog = !showDialog }) {
                        Text(text = stringResource(id = R.string.ok))
                    } },
                title = { Text(text = stringResource(id = R.string.search_club_alert_header)) },
                text = { Text(text = stringResource(id = R.string.search_clubs_alert_desc)) }
            )
        }
    }
}