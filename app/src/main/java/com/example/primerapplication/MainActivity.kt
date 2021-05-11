package com.example.primerapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.compose.component.ButtonWithIconCell
import com.compose.component.FilledButton
import com.compose.templates.MainLayoutWithBottomSheet
import com.compose.theme.PrimerTheme
import com.compose.theme.TwentyFourDp
import com.example.primerapplication.payment.PaymentViewModel
import com.example.primerapplication.payment.PaymentViewModelFactory
import kotlinx.coroutines.CoroutineScope
import com.example.primerapplication.ui.CardInfoViews
import com.example.primerapplication.ui.PaymentInstrumentOptionBottomSheet
import com.example.primerapplication.ui.PaypalInfoViews
import com.example.primerapplication.ui.rememberCardPaymentFormState
import com.example.primerapplication.ui.rememberPaypalPaymentFormState

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: PaymentViewModel

    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        setContent {
            PrimerTheme {
                PaymentInfoScreen()
            }
        }
    }

    private fun initViewModel() {
        val factory = PaymentViewModelFactory(application)
        viewModel = ViewModelProvider(this, factory).get(PaymentViewModel::class.java)
        viewModel.errorLiveData.observe(this) {
            it?.takeIf { it.isNotEmpty() }?.let {
                Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
            }
        }
        viewModel.tokenLiveData.observe(this) {
            it?.takeIf { it.isNotEmpty() }?.let {
                Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    @Composable
    fun PaymentInfoScreen() {
        val scope = rememberCoroutineScope()
        val cardFormState = rememberCardPaymentFormState()
        val paypalFormState = rememberPaypalPaymentFormState()
        val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Expanded)
        val keyboardController = LocalSoftwareKeyboardController.current
        val localFocusManager = LocalFocusManager.current
        val shouldShowCardDetails by viewModel.shouldShowCardDetailsLiveData.observeAsState()
        val shouldShowPaypalDetails by viewModel.shouldShowPaypalLiveData.observeAsState()
        val isLoading by viewModel.isLoadingLiveData.observeAsState()
        val iconResLiveData by viewModel.iconResLiveData.observeAsState()

        MainLayoutWithBottomSheet(
            scope = scope,
            onBackPressedDispatcherOwner = this@MainActivity,
            onBackClicked = { finish() },
            sheetState = bottomSheetState,
            sheetContent = {
                PaymentsBottomSheetContent(
                    scope = scope,
                    bottomSheetState = bottomSheetState
                )
            },
            bodyContentPadding = PaddingValues(TwentyFourDp),
            mainButton = {
                ButtonWithIconCell(
                    iconResId = iconResLiveData ?: null,
                    button = {
                        FilledButton(
                            modifier = Modifier
                                .height(height = 48.dp)
                                .fillMaxWidth(),
                            text = stringResource(id = R.string.PAY),
                            enabled = when {
                                shouldShowCardDetails == true -> cardFormState.isFormValid && (isLoading?.not()
                                    ?: false)
                                shouldShowPaypalDetails == true -> paypalFormState.isFormValid && (isLoading?.not()
                                    ?: false)
                                else -> false
                            },
                            isLoading = isLoading ?: false,
                            onClick = {
                                localFocusManager.clearFocus()
                                val paymentInstrument =
                                    when (shouldShowCardDetails) {
                                        true -> cardFormState.toPaymentInstrument()
                                        else -> paypalFormState.toPaymentInstrument()
                                    }
                                viewModel.onPayClicked(paymentInstrument)
                            })
                    }
                )
            },
        ) {
            if (shouldShowCardDetails == true) {
                CardInfoViews(
                    formState = cardFormState,
                    onDone = {
                        localFocusManager.clearFocus()
                        keyboardController?.hide()
                    })
            } else if (shouldShowPaypalDetails == true) {
                PaypalInfoViews()
            }
        }
    }

    @ExperimentalMaterialApi
    @Composable
    private fun PaymentsBottomSheetContent(
        scope: CoroutineScope,
        bottomSheetState: ModalBottomSheetState,
    ) {
        PaymentInstrumentOptionBottomSheet(
            scope = scope,
            bottomSheetState = bottomSheetState,
            onPaymentOptionClicked = {
                viewModel.updatePaymentType(it)
            }
        )
    }
}
