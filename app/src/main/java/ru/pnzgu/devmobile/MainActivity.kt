package ru.pnzgu.devmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.pnzgu.devmobile.model.SignGateUserRole
import ru.pnzgu.devmobile.ui.dashboard.SignGateDashboardScreen
import ru.pnzgu.devmobile.ui.test.TestEnvRoleSwitch
import ru.pnzgu.devmobile.ui.theme.DevMobileApplicationTheme
import ru.pnzgu.devmobile.ui.theme.LocalPlaceholderNotifier
import ru.pnzgu.devmobile.ui.theme.PlaceholderNotifier
import ru.pnzgu.devmobile.ui.theme.showNotImplemented
import ru.pnzgu.devmobile.ui.theme.snackbar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { DevMobileApplicationTheme { DevMobileApplication() } }
    }
}

@Composable
fun DevMobileApplication() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val notImplementedMessage = stringResource(R.string.notice_not_implemented)

    val placeholderNotifier = remember(notImplementedMessage, snackbarHostState) {
        PlaceholderNotifier {
            scope.showNotImplemented(snackbarHostState, notImplementedMessage)
        }
    }

    var userRole by rememberSaveable { mutableStateOf(SignGateUserRole.SIGNER) }
    val onRoleSwitch = remember { { userRole = userRole.switch() } }

    CompositionLocalProvider(LocalPlaceholderNotifier provides placeholderNotifier) {

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            floatingActionButton = {
                TestEnvRoleSwitch(userRole, onRoleSwitch)
            }) { innerPadding ->
            ScreenHolder(paddingValues = innerPadding, userRole)
        }

    }
}

@Composable
fun ScreenHolder(paddingValues: PaddingValues, userRole: SignGateUserRole) {
    var selectedNode by rememberSaveable { mutableIntStateOf(0) }

    /// TODO: В качестве тестового режима Узел №2 отвалился
    val gatewayIsFaulty = rememberSaveable(selectedNode) { selectedNode == 1 }

    SignGateDashboardScreen(
        modifier = Modifier.padding(paddingValues),
        userRole = userRole,
        gatewayIsFaulty = gatewayIsFaulty,
        selectedNode = selectedNode,
        onNodeSelected = { selectedNode = it },
        onExportLogs = MaterialTheme.snackbar::notifyPlaceholder
    )
}

@Preview(showSystemUi = true, locale = "ru")
@Composable
fun RussianApplicationPreview() {
    DevMobileApplicationTheme {
        DevMobileApplication()
    }
}

@Preview(showSystemUi = true, locale = "en")
@Composable
fun GlobalApplicationPreview() {
    DevMobileApplicationTheme {
        DevMobileApplication()
    }
}
