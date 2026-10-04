package ru.pnzgu.devmobile.ui.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.pnzgu.devmobile.R
import ru.pnzgu.devmobile.model.GatewayServiceStatus
import ru.pnzgu.devmobile.model.SignGateSegment
import ru.pnzgu.devmobile.model.SignGateUserRole
import ru.pnzgu.devmobile.ui.theme.snackbar
import ru.pnzgu.devmobile.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignGateDashboardScreen(
    userRole: SignGateUserRole,
    gatewayIsFaulty: Boolean,
    selectedNode: Int,
    onNodeSelected: (Int) -> Unit,
    onExportLogs: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        R.string.internal_segment to Icons.Default.Security,
        R.string.external_segment to Icons.Default.Hub
    )
    val pagerState = rememberPagerState(pageCount = tabs::size)
    val coroutineScope = rememberCoroutineScope()

    val titleText = if (userRole.infraInfoAvailable) {
        stringResource(R.string.admin_dashboard_title)
    } else {
        stringResource(R.string.user_dashboard_title)
    }

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        TopAppBar(
            title = { Text(titleText) },
            /// NOTE: Самая интересная история, TopAppBar внутри внешнего Scaffold не сбрасывает отступы
            windowInsets = WindowInsets(0, 0, 0, 0),
            subtitle = { UsernameHint(userRole) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            )
        )
    }) { screenPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = screenPadding.calculateTopPadding()),
            horizontalAlignment = Alignment.Start
        ) {
            AnimatedVisibility(
                userRole.infraInfoAvailable,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
                modifier = Modifier.fillMaxWidth()
            ) {
                NodeSelector(
                    selectedNode = selectedNode,
                    onNodeSelected = onNodeSelected,
                    modifier = Modifier.padding(
                        horizontal = MaterialTheme.spacing.medium,
                        vertical = MaterialTheme.spacing.small
                    )
                )
            }

            PrimaryTabRow(
                selectedTabIndex = pagerState.currentPage,
            ) {
                tabs.forEachIndexed { index, (tabName, tabIcon) ->
                    val selected = index == pagerState.currentPage
                    val contentColor by animateColorAsState(
                        targetValue = if (selected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                    Tab(
                        selected = selected,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(stringResource(tabName), color = contentColor) },
                        icon = { Icon(tabIcon, null, tint = contentColor) })
                }
            }

            HorizontalPager(
                state = pagerState, modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { pageIndex ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(
                            horizontal = MaterialTheme.spacing.medium,
                            vertical = MaterialTheme.spacing.small
                        )
                ) {
                    DashboardContent(
                        userRole, SignGateSegment.entries[pageIndex], gatewayIsFaulty, onExportLogs
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardContent(
    userRole: SignGateUserRole,
    selectedSegment: SignGateSegment,
    gatewayIsFaulty: Boolean,
    onExportLogs: () -> Unit
) {
    val cardColor by animateColorAsState(
        targetValue = if (gatewayIsFaulty) {
            MaterialTheme.colorScheme.errorContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }, label = stringResource(R.string.gateway_status_color_animation)
    )
    val borderColor by animateColorAsState(
        targetValue = if (gatewayIsFaulty) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.outlineVariant
        }, label = stringResource(R.string.gateway_status_color_animation)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        AnimatedVisibility(
            !userRole.infraInfoAvailable && selectedSegment == SignGateSegment.INTERNAL,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxWidth()
        ) {
            UserStatusCard(gatewayIsFaulty)
        }
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.outlinedCardColors(containerColor = cardColor),
            border = BorderStroke(width = 1.dp, color = borderColor),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(modifier = Modifier.padding(MaterialTheme.spacing.medium)) {
                if (userRole.infraInfoAvailable) {
                    AdminStatusList(
                        selectedSegment,
                        gatewayIsFaulty,
                        onExportLogs,
                        MaterialTheme.snackbar::notifyPlaceholder
                    )
                } else {
                    UserStatusList(
                        if (gatewayIsFaulty) GatewayServiceStatus.faultyStatuses else GatewayServiceStatus.sampleStatuses,
                    )
                }
            }
        }
    }
}

@Composable
fun UsernameHint(userRole: SignGateUserRole) {
    Text(
        text = when (userRole) {
            SignGateUserRole.ADMIN -> stringResource(R.string.role_admin)
            SignGateUserRole.SIGNER -> stringResource(R.string.role_user)
        }
    )
}
