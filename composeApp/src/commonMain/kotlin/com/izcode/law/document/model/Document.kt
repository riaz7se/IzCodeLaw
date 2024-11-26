package com.izcode.law.document.model

data class Document(
    val title: String,
    val clientName: String,
    val lastHearingDate: String?,
    val nextHearingDate: String?,
    val hearingHistory: List<String>?,
    val location: String,
    val juryNames: List<String>?,
    val attachments: List<Attachment>?
) 