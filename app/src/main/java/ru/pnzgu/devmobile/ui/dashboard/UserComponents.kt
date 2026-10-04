package ru.pnzgu.devmobile.ui.dashboard

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import ru.pnzgu.devmobile.R
import ru.pnzgu.devmobile.model.GatewayServiceStatus
import ru.pnzgu.devmobile.ui.theme.spacing

@Composable
fun UserStatusList(statuses: List<GatewayServiceStatus>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        statuses.forEachIndexed { index, status ->
            ListItem(
                leadingContent = {
                    if (status.isAvailable) Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    ) else Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                overlineContent = {
                    Text(status.techInfo, style = MaterialTheme.typography.labelSmall)
                },
                supportingContent = {
                    Text(status.description, style = MaterialTheme.typography.bodyMedium)
                },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                content = {
                    Text(
                        text = status.title,
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

const val SECONDS_ESTIMATED = 4

@Composable
fun UserStatusCard(
    gatewayIsFaulty: Boolean, modifier: Modifier = Modifier
) {
    val containerColor by animateColorAsState(
        targetValue = if (gatewayIsFaulty) {
            MaterialTheme.colorScheme.errorContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }, label = stringResource(R.string.gateway_status_color_animation)
    )
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = containerColor),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = MaterialTheme.spacing.extraSmall,
            pressedElevation = MaterialTheme.spacing.small
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                Icon(
                    imageVector = if (gatewayIsFaulty) Icons.Default.Cancel else Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (gatewayIsFaulty) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(MaterialTheme.spacing.large)
                )

                Text(
                    text = if (gatewayIsFaulty) stringResource(R.string.gateway_unavailable) else stringResource(
                        R.string.gateway_ready
                    ),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (gatewayIsFaulty) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                modifier = Modifier.padding(start = MaterialTheme.spacing.extraSmall)
            ) {
                Icon(
                    imageVector = Icons.Default.Speed,
                    contentDescription = null,
                    tint = if (gatewayIsFaulty) MaterialTheme.colorScheme.onErrorContainer.copy(
                        alpha = 0.7f
                    ) else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(MaterialTheme.spacing.large)
                )
                Text(
                    text = if (gatewayIsFaulty) stringResource(R.string.gateway_connection_timeout) else pluralStringResource(
                        R.plurals.estimated_time, SECONDS_ESTIMATED, SECONDS_ESTIMATED
                    ),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    color = if (gatewayIsFaulty) MaterialTheme.colorScheme.onErrorContainer.copy(
                        alpha = 0.7f
                    ) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}