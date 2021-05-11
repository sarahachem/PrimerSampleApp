package com.example.primerapplication.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import com.example.primerapplication.R
import com.primer.Card
import com.primer.PaymentInstrument
import com.primer.Paypal
import java.util.regex.Pattern

class CardInfoFormState {
    companion object {
        private val DATE_PATTERN: Pattern = Pattern.compile("^\\d{2}/\\d{4}$")
    }

    val isFormValid: Boolean
        get() {
            return cardHolderName?.isNotBlank() == true
                    && cardNumber?.toString()?.isNotBlank() == true
                    && cardHolderName?.isNotBlank() == true
                    && cvv?.toString()?.isNotBlank() == true
                    && cardHolderNameErrorText == null
                    && cardNumberErrorText == null
                    && expirationErrorText == null
                    && cvvErrorText == null
        }

    fun toPaymentInstrument(): PaymentInstrument {
        return PaymentInstrument(
            Card(
                cardNumber.toString(),
                cvv.toString(),
                expirationDate?.split("/")?.get(0) ?: "",
                expirationDate?.split("/")?.get(1) ?: "",
                cardHolderName.toString()
            )
        )
    }

    var cardHolderName: String? by mutableStateOf(null)
    var expirationDate: String? by mutableStateOf(null)
    var cardNumber: Long? by mutableStateOf(null)
    var cvv: Int? by mutableStateOf(null)

    val cardHolderNameErrorText: Int?
        get() = when {
            cardHolderName?.isEmpty() ?: false -> R.string.CARD_HOLDER_NAME_EMPTY
            else -> null
        }

    var cardNumberErrorText: Int? = null
        get() = when {
            (cardNumber?.toString()?.isNotBlank() ?: false) && cardNumber?.toString()?.toCharArray()
                ?.count() != 16 -> R.string.WRONG_CARD_NUMBER
            else -> null
        }

    var cvvErrorText: Int? = null
        get() = when {
            (cvv?.toString()?.isNotBlank() ?: false) && cvv?.toString()?.toCharArray()
                ?.count() != 3 -> R.string.WRONG_CVV
            else -> null
        }
    var expirationErrorText: Int? = null
        get() = when {
            expirationDate?.isEmpty() ?: false
                    || (expirationDate?.isNotBlank() ?: false)
                    && DATE_PATTERN.matcher(expirationDate).matches().not()
            -> R.string.WRONG_EXPIRATION_DATE
            else -> null
        }
}

class PaypalInfoFormState {

    val isFormValid: Boolean = true

    fun toPaymentInstrument(): PaymentInstrument {
        return PaymentInstrument(
            Paypal(paypalOrderId = "1")
        )
    }

    //to use if we had an actual order id
    var orderId: Int? by mutableStateOf(null)
}

@Composable
fun rememberCardPaymentFormState(): CardInfoFormState {
    return remember { CardInfoFormState() }
}

@Composable
fun rememberPaypalPaymentFormState(): PaypalInfoFormState {
    return remember { PaypalInfoFormState() }
}

