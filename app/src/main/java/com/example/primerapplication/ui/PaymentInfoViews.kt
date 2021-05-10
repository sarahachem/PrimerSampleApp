package com.example.primerapplication.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import com.compose.component.input.FormInputField
import com.compose.component.input.InputType
import com.compose.theme.FourDp
import com.compose.theme.PrimerTheme
import com.compose.theme.TwentyFourDp
import com.example.primerapplication.R

@ExperimentalComposeUiApi
@Composable
fun CardInfoViews(
    formState: CardInfoFormState,
    onDone: () -> Unit,
    onChange: ((CardInfoFormState) -> Unit)? = null
) {
    val numberFocusRequester = remember { FocusRequester() }
    val cvvFocusRequester = remember { FocusRequester() }
    val expirationDateFocusRequester = remember { FocusRequester() }

    Text(
        style = PrimerTheme.types.body1,
        color = MaterialTheme.colors.onSurface,
        text = stringResource(id = R.string.CARD_HOLDER_NAME)
    )
    Spacer(Modifier.size(FourDp))
    FormInputField(
        modifier = Modifier.fillMaxWidth(),
        type = InputType.Text,
        label = stringResource(id = R.string.CARD_HOLDER_NAME),
        text = formState.cardHolderName ?: "",
        isError = formState.cardHolderNameErrorText != null,
        hintText = formState.cardHolderNameErrorText?.let { AnnotatedString(stringResource(id = it)) },
        placeHolder = "Jane Doe",
        onImeAction = { numberFocusRequester.requestFocus() },
        onValueChange = {
            formState.cardHolderName = it
            onChange?.invoke(formState)
        })

    Spacer(Modifier.size(TwentyFourDp))
    Text(
        style = PrimerTheme.types.body1,
        color = MaterialTheme.colors.onSurface,
        text = stringResource(id = R.string.CARD_NUMBER)
    )
    Spacer(Modifier.size(FourDp))
    FormInputField(
        modifier = Modifier.fillMaxWidth(),
        type = InputType.Number,
        label = stringResource(id = R.string.CARD_NUMBER),
        text = formState.cardNumber?.toString() ?: "",
        isError = formState.cardNumberErrorText != null,
        hintText = formState.cardNumberErrorText?.let { AnnotatedString(stringResource(id = it)) },
        placeHolder = "411111111111111",
        focusRequester = numberFocusRequester,
        imeAction = ImeAction.Next,
        onImeAction = {
            cvvFocusRequester.requestFocus()
        },
        onValueChange = {
            formState.cardNumber = it?.takeIf { it.isNotEmpty() }?.toLong()
            onChange?.invoke(formState)
        })

    Spacer(Modifier.size(TwentyFourDp))

    Text(
        style = PrimerTheme.types.body1,
        color = MaterialTheme.colors.onSurface,
        text = stringResource(id = R.string.CVV)
    )
    Spacer(Modifier.size(FourDp))

    FormInputField(
        modifier = Modifier.fillMaxWidth(),
        type = InputType.Number,
        label = stringResource(id = R.string.CVV),
        text = formState.cvv?.toString() ?: "",
        isError = formState.cvvErrorText != null,
        hintText = formState.cvvErrorText?.let { AnnotatedString(stringResource(id = it)) },
        placeHolder = "123",
        focusRequester = cvvFocusRequester,
        imeAction = ImeAction.Next,
        onImeAction = {
            expirationDateFocusRequester.requestFocus()
        },
        onValueChange = {
            formState.cvv = it?.takeIf { it.isNotEmpty() }?.toInt()
            onChange?.invoke(formState)
        })

    Spacer(Modifier.size(TwentyFourDp))
    Text(
        style = PrimerTheme.types.body1,
        color = MaterialTheme.colors.onSurface,
        text = stringResource(id = R.string.EXPIRATION_DATE)
    )
    Spacer(Modifier.size(FourDp))
    FormInputField(
        modifier = Modifier.fillMaxWidth(),
        type = InputType.Text,
        label = stringResource(id = R.string.EXPIRATION_DATE),
        text = formState.expirationDate ?: "",
        isError = formState.expirationErrorText != null,
        hintText = formState.expirationErrorText?.let { AnnotatedString(stringResource(id = it)) },
        placeHolder = "12/2030",
        focusRequester = expirationDateFocusRequester,
        imeAction = ImeAction.Done,
        onImeAction = {
            onDone.invoke()
        },
        onValueChange = {
            formState.expirationDate = it?.takeIf { it.isNotEmpty() }
            onChange?.invoke(formState)
        })
}

@ExperimentalComposeUiApi
@Composable
fun PaypalInfoViews() {
    Text(
        modifier = Modifier.fillMaxWidth(),
        style = PrimerTheme.types.body1,
        color = MaterialTheme.colors.onSurface,
        text = "Thank you for paying with paypal, please click pay to finalize"
    )
}
