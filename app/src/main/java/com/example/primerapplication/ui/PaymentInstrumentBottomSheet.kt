package com.example.primerapplication.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.compose.component.LabelIconCell
import com.example.primerapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class PaymentOption(value: Int) {
    Card(1), PayPal(2)
}

@ExperimentalMaterialApi
@Composable
fun PaymentInstrumentOptionBottomSheet(
    scope: CoroutineScope,
    bottomSheetState: ModalBottomSheetState,
    onPaymentOptionClicked: (PaymentOption) -> Unit
) {
    Column {
        LabelIconCell(
            modifier = Modifier
                .clickable {
                    onPaymentOptionClicked.invoke(PaymentOption.Card)
                    scope.launch { bottomSheetState.hide() }
                }
                .padding(horizontal = 24.dp),
            labelText = stringResource(id = R.string.CARD),
            showArrow = true
        )
        LabelIconCell(
            modifier = Modifier
                .clickable {
                    scope.launch { bottomSheetState.hide() }
                    onPaymentOptionClicked.invoke(PaymentOption.PayPal)
                }
                .padding(horizontal = 24.dp),
            labelText = stringResource(id = R.string.PAYPAL),
            topDivider = true,
            showArrow = true
        )
        Spacer(modifier = Modifier.padding(8.dp))
    }
}
