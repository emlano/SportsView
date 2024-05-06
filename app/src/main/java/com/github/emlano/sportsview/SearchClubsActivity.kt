package com.github.emlano.sportsview

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.emlano.sportsview.logic.ImgList
import com.github.emlano.sportsview.logic.SportsDatabase
import com.github.emlano.sportsview.logic.api.fetchImgFromUrl
import com.github.emlano.sportsview.logic.entity.Team
import com.github.emlano.sportsview.ui.theme.SportsViewTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

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
    var teamList by rememberSaveable { mutableStateOf(listOf<Team>()) }
    val scroll = rememberScrollState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scroll),
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
        Button(onClick = {
            ImgList.destroySelf()
            if (searchStr.isEmpty()) return@Button

            scope.launch {
                val teamDao = SportsDatabase.getInstance(context).teamDao()
                teamList = teamDao.getTeamsSimilarTo("%${searchStr.uppercase()}%")
            }
        }) {
            Text(text = stringResource(id = R.string.search))
        }
        Spacer(modifier = Modifier.padding(18.dp))
        Box(
            modifier = modifier
                .height(300.dp)
                .width(400.dp)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(15.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (teamList.isEmpty()) {
                Text(text = "No data")
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(12.dp),
                ) {
                    items(teamList.size) {
                        val team = teamList[it]

                        Row(
                            modifier = modifier
                                .fillMaxWidth()
                                .height(125.dp)
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(15.dp)
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            AsyncImage(modifier = modifier, url = team.teamLogo, contentDesc = "${team.name} logo", key = it)
                            Text(
                                text = team.name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AsyncImage(modifier: Modifier, url: String, contentDesc: String, key: Int) {
    var bitmapState by remember { mutableStateOf<ImageBitmap?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = bitmapState) {
        scope.launch(Dispatchers.IO) {
            bitmapState = fetchImgFromUrl(url)
        }
    }

    if (ImgList.hasImage(key)) {
        Image(
            modifier = modifier.padding(5.dp),
            bitmap = ImgList.getImage(key),
            contentDescription = contentDesc
        )
    } else if (bitmapState == null) {
        Icon(imageVector = Icons.Outlined.Refresh, contentDescription = "loading")
    } else {
        ImgList.addImage(key, bitmapState!!)
        Image(
            modifier = modifier
                .padding(5.dp),

            bitmap = bitmapState!!,
            contentDescription = contentDesc,
        )
    }
}
