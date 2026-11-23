package com.example.myapplication

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ParsedPayment(
    val merchant: String,
    val amount: Int,
    val time: String
)

object PaymentStore {
    private val _payments = MutableStateFlow<List<ParsedPayment>>(emptyList())
    val payments: StateFlow<List<ParsedPayment>> = _payments.asStateFlow()

    fun addPayment(item: ParsedPayment) {
        _payments.value = listOf(item) + _payments.value
    }
}