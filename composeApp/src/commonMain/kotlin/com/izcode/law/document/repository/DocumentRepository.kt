package com.izcode.law.home.document.repository

import com.izcode.law.document.model.*

object DocumentRepository {
    val documents = listOf(
        Document(
            title = "Property Purchase Agreement",
            clientName = "John Smith",
            nextHearingDate = "2024-04-15",
            lastHearingDate = "2024-01-15",
            hearingHistory = listOf("2024-01-15", "2024-02-01", "2024-03-15"),
            location = "Kavali",
            juryNames = listOf("Jury P", "Jury P1", "Jury P11"),
            attachments = listOf(
                Attachment("Agreement.pdf", FileType.PDF, "2.5 MB"),
                Attachment("Property_Details.docx", FileType.WORD, "1.2 MB")
            )
        ),
        Document(
            title = "Divorce Settlement",
            clientName = "Sarah Johnson",
            nextHearingDate = "2024-04-20",
            lastHearingDate = "2024-01-15",
            hearingHistory = emptyList(),
            location = "Nellore",
            juryNames = listOf("Jury 1", "Jury 1", "Jury 1"),
            attachments = emptyList()
        ),
        Document(
            title = "Business Contract",
            clientName = "Tech Corp Ltd",
            nextHearingDate = "2024-04-25",
            lastHearingDate = "2024-01-15",
            hearingHistory = listOf("item1", "item2", "item3"),
            location = "Bangalore",
            juryNames = listOf("Jury 1", "Jury 1", "Jury 1"),
            attachments = emptyList()
        ),
        Document(
            title = "Will and Testament",
            clientName = "Robert Brown",
            nextHearingDate = "2024-05-01",
            lastHearingDate = "2024-01-15",
            hearingHistory = listOf("item1", "item2", "item3"),
            location = "Nellore",
            juryNames = listOf("Jury 1", "Jury 1", "Jury 1"),
            attachments = emptyList()
        ),
        Document(
            title = "Employment Agreement",
            clientName = "Mary Williams",
            nextHearingDate = "2024-05-05",
            lastHearingDate = "2024-01-15",
            hearingHistory = listOf("item1", "item2", "item3"),
            location = "Bangalore",
            juryNames = listOf("Jury 1", "Jury 1", "Jury 1"),
            attachments = emptyList()
        )
    )
}