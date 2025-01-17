package com.github.emlano.sportsview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.emlano.sportsview.logic.SportsDatabase
import com.github.emlano.sportsview.logic.api.fetchAndStoreLeagues
import com.github.emlano.sportsview.logic.parseJsonLeagues
import com.github.emlano.sportsview.ui.theme.SportsViewTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SportsViewTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeMenu(modifier = Modifier, this)
                }
            }
        }
    }
}


@Composable
fun HomeMenu(modifier: Modifier = Modifier, context: Context) {
    val scope = rememberCoroutineScope()
    var showDialog by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.title),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = modifier.padding(26.dp))
        Button(
            onClick = {
                // Gets leagues from the API and stores them in the database
                scope.launch {
                    val json = fetchAndStoreLeagues()
                    val leagues = parseJsonLeagues(json)
                    val leagueDAO = SportsDatabase.getInstance(context).leagueDao()

                    for (i in leagues) {
                        leagueDAO.addLeague(i)
                    }

                    showDialog = !showDialog
                }
            }
        ) {
            Text(text = stringResource(id = R.string.add_to_db))
        }
        Spacer(modifier = modifier.padding(bottom = 18.dp))
        Button(onClick = {
            val intent = Intent(context, SearchClubByLeagueActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(text = stringResource(id = R.string.search_clubs_by_league))
        }
        Spacer(modifier = modifier.padding(bottom = 18.dp))
        Button(onClick = {
            val intent = Intent(context, SearchClubsActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(text = stringResource(id = R.string.search_clubs))
        }
        Spacer(modifier = modifier.padding(bottom = 18.dp))
        Button(onClick = {
            val intent = Intent(context, GetTeamJerseyActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(text = stringResource(id = R.string.get_team_jersey))
        }

        // To show user an alert once the Database has been updated with the fetched JSON
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = !showDialog },
                confirmButton = {
                    Button(onClick = { showDialog = !showDialog }) {
                        Text(text = stringResource(id = R.string.ok))
                } },
                title = { Text(text = stringResource(id = R.string.search_league_alert_header)) },
                text = { Text(text = stringResource(id = R.string.search_league_alert_desc)) }
            )
        }
    }
}