package ru.pnzgu.devmobile.ui.test

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.pnzgu.devmobile.model.SignGateUserRole
import ru.pnzgu.devmobile.ui.dashboard.UsernameHint

@Composable
fun TestEnvRoleSwitch(
    userRole: SignGateUserRole, onRoleSwitch: () -> Unit, modifier: Modifier = Modifier
) {
    ExtendedFloatingActionButton(
        onClick = onRoleSwitch,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        shape = MaterialTheme.shapes.large,
        icon = {
            Icon(
                imageVector = when (userRole) {
                    SignGateUserRole.ADMIN -> Icons.Default.Badge
                    SignGateUserRole.SIGNER -> Icons.Default.Person
                }, contentDescription = null
            )
        },
        text = { UsernameHint(userRole) })
}