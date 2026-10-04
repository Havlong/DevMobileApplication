package ru.pnzgu.devmobile.model

data class GatewayServiceStatus(
    val title: String,
    val description: String,
    val techInfo: String,
    val isAvailable: Boolean,
    val shortTitle: String,
    val shortInfo: String
) {
    companion object {
        val sampleStatuses = listOf(
            GatewayServiceStatus(
                title = "OCSP-сервис Удостоверяющего центра",
                description = "Сервис ПАСС доступен",
                techInfo = "Сертификат сервиса ПАСС действителен",
                isAvailable = true,
                shortTitle = "OCSP-сервис",
                shortInfo = "Сертификат действителен"
            ),
            GatewayServiceStatus(
                title = "TSA-сервис Удостоверяющего центра",
                description = "Сервис штампов времени доступен",
                techInfo = "Сертификат сервиса штампов действителен",
                isAvailable = true,
                shortTitle = "TSA-сервис",
                shortInfo = "Сертификат действителен"
            ),
            GatewayServiceStatus(
                title = "CRL-сервис Удостоверяющего центра",
                description = "Загрузка СОС доступна",
                techInfo = "Проверка действительности СОС успешна",
                isAvailable = true,
                shortTitle = "CRL-сервис",
                shortInfo = "СОС действительна"
            ),
            GatewayServiceStatus(
                title = "Сертификат подписи SignGate",
                description = "Алгоритм ГОСТ Р 34.10-2012",
                techInfo = "Сертификат шлюза ЭДО действителен",
                isAvailable = true,
                shortTitle = "Сертификат подписи DVCS",
                shortInfo = "Сертификат действителен"
            ),
        )
        val faultyStatuses = listOf(
            GatewayServiceStatus(
                title = "OCSP-сервис Удостоверяющего центра",
                description = "Сервис ПАСС недоступен",
                techInfo = "Сертификат сервиса отсутствует",
                isAvailable = false,
                shortTitle = "OCSP-сервис",
                shortInfo = "Онлайн-статус отзыва недоступен"
            ),
            GatewayServiceStatus(
                title = "TSA-сервис Удостоверяющего центра",
                description = "Сервис штампов времени доступен",
                techInfo = "Сертификат сервиса штампов действителен",
                isAvailable = true,
                shortTitle = "TSA-сервис",
                shortInfo = "Сертификат действителен"
            ),
            GatewayServiceStatus(
                title = "CRL-сервис Удостоверяющего центра",
                description = "Загрузка СОС недоступна",
                techInfo = "Актуальный Список Отозванных Сертификатов отсутствует",
                isAvailable = false,
                shortTitle = "CRL-сервис",
                shortInfo = "СОС истекла"
            ),
            GatewayServiceStatus(
                title = "Сертификат подписи SignGate",
                description = "Алгоритм ГОСТ Р 34.10-2012",
                techInfo = "Отзыв сертификата шлюза ЭДО не был проверен",
                isAvailable = false,
                shortTitle = "Сертификат подписи DVCS",
                shortInfo = "Сертификат не мог быть проверен"
            ),
        )
    }
}
