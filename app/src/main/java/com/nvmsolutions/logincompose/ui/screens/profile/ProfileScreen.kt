package com.nvmsolutions.logincompose.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nvmsolutions.logincompose.data.model.User
import androidx.compose.foundation.layout.Arrangement

@Composable
fun ProfileScreen(user: User) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Profile",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("ID: ${user.id}")
                Text("Name: ${user.name}")
                Text("Email: ${user.email}")
                Text("Groups: ${user.groups.joinToString()}")
                Text("Friends: ${user.friends.joinToString()}")
            }
        }
    }
}