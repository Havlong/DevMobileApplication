package ru.pnzgu.devmobile.ui.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.MiscellaneousServices
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.pnzgu.devmobile.R
import ru.pnzgu.devmobile.model.GatewayServiceStatus
import ru.pnzgu.devmobile.model.SignGateSegment
import ru.pnzgu.devmobile.ui.theme.spacing

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NodeSelector(
    selectedNode: Int, onNodeSelected: (Int) -> Unit, modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.node_selector_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(MaterialTheme.spacing.small))

        val nodeNames = stringArrayResource(R.array.list_nodes)

        /// Modifier.fillMaxWidth() убивает этот компонент на текущей версии 1.5.0-alpha28
        ButtonGroup(
            overflowIndicator = { menuState ->
                IconButton(onClick = {
                    if (menuState.isShowing) menuState.dismiss() else menuState.show()
                }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.hint_more_servers)
                    )
                }
            },
        ) {
            nodeNames.forEachIndexed { index, nodeName ->
                toggleableItem(
                    checked = selectedNode == index,
                    label = nodeName,
                    onCheckedChange = { isChecked ->
                        if (isChecked) onNodeSelected(index)
                    },
                    icon = { Icon(Icons.Default.MiscellaneousServices, null) })
            }
        }
    }
}

@Composable
fun AdminStatusList(
    selectedSegment: SignGateSegment,
    gatewayIsFaulty: Boolean,
    onExportLogs: () -> Unit,
    onChipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        CaServicesSection(if (gatewayIsFaulty) GatewayServiceStatus.faultyStatuses else GatewayServiceStatus.sampleStatuses)

        HorizontalDivider(
            modifier = Modifier.padding(vertical = MaterialTheme.spacing.medium),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        CryptoProvidersSection(selectedSegment, gatewayIsFaulty, onChipClick)

        AnimatedVisibility(
            gatewayIsFaulty,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),

            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.spacing.medium)
        ) {
            SecurityLogsExportButton(onExportLogs)
        }

    }
}

@Composable
fun CaServicesSection(
    statuses: List<GatewayServiceStatus>, modifier: Modifier = Modifier
) {
    /// TODO: Хардкод иконок для статусов сервисов УЦ
    val iconMapping = statuses.map(GatewayServiceStatus::shortTitle).zip(
        listOf(
            Icons.Default.NetworkCheck,
            Icons.Default.HourglassTop,
            Icons.Default.Checklist,
            Icons.Default.Badge
        )
    ).toMap()

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.ca_services_availability),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        statuses.forEachIndexed { index, status ->
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                leadingContent = {
                    Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = iconMapping.getOrDefault(
                                status.shortTitle,
                                Icons.Default.CloudDownload,
                            ), contentDescription = null
                        )
                    }
                },
                supportingContent = {
                    Text(status.shortInfo, style = MaterialTheme.typography.bodyMedium)
                },
                trailingContent = {
                    Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                        if (status.isAvailable) Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        ) else Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                content = {
                    Text(
                        text = status.shortTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
            )
            if (index < statuses.lastIndex) {
                HorizontalDivider(
                    Modifier.padding(MaterialTheme.spacing.small),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}

@Composable
fun CryptoProvidersSection(
    selectedSegment: SignGateSegment,
    gatewayIsFaulty: Boolean,
    onChipClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cryptoProviders = stringArrayResource(R.array.list_crypto_providers)
    /// NOTE: Симуляция, логики отказа нет
    val brokenIndex = when (selectedSegment) {
        SignGateSegment.INTERNAL -> 0
        SignGateSegment.EXTERNAL -> 1
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.status_crypto),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
        ) {
            cryptoProviders.forEachIndexed { index, cryptoProvider ->

                /// NOTE: Симуляция, логики отказа нет
                val isProviderFaulty =
                    gatewayIsFaulty && (index == brokenIndex || cryptoProvider.contains("ECDSA"))

                AssistChip(
                    onClick = onChipClick,
                    label = {
                        Text(
                            text = cryptoProvider, style = MaterialTheme.typography.labelSmall
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = if (isProviderFaulty) Icons.Default.Cancel else Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(MaterialTheme.spacing.medium),
                            tint = if (isProviderFaulty) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (isProviderFaulty) {
                            MaterialTheme.colorScheme.errorContainer.copy(alpha = .5f)
                        } else {
                            MaterialTheme.colorScheme.surfaceContainerLow
                        },
                        labelColor = if (isProviderFaulty) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                    ),
                    border = AssistChipDefaults.assistChipBorder(
                        enabled = true,
                        borderColor = if (isProviderFaulty) {
                            MaterialTheme.colorScheme.error
                        } else {
                            MaterialTheme.colorScheme.outlineVariant
                        },
                        borderWidth = 1.dp,
                    ),
                    shape = MaterialTheme.shapes.small,
                )
            }
        }
    }
}

@Composable
fun SecurityLogsExportButton(
    onExportLogs: () -> Unit, modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onExportLogs,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError
        ),
        shape = MaterialTheme.shapes.small
    ) {
        Icon(Icons.Default.FileDownload, contentDescription = null)
        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
        Text(text = stringResource(R.string.export_journals))
    }
}