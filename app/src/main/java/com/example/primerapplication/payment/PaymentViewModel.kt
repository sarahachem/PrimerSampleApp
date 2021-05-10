package com.example.primerapplication.payment

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.primerapplication.ApiBuilders
import com.example.primerapplication.R
import com.example.primerapplication.ui.PaymentOption
import com.primer.PaymentInstrument
import com.primer.checkSuccessful
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

class PaymentViewModel(
    val app: Application,
) : AndroidViewModel(app) {

    var errorLiveData = mutableLiveDataOf("")
    var tokenLiveData = mutableLiveDataOf("")
    var shouldShowCardDetailsLiveData = mutableLiveDataOf(false)
    var shouldShowPaypalLiveData = mutableLiveDataOf(false)
    var isLoadingLiveData = mutableLiveDataOf(false)
    var iconResLiveData = mutableLiveDataOf<Int?>(null)

    fun onPayClicked(cardInstrument: PaymentInstrument) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoadingLiveData.postValue(true)
            kotlin.runCatching {
                ApiBuilders.getPrimerApi(app.applicationContext)
                    .addPaymentInstrument(cardInstrument)
                    .execute().checkSuccessful()
            }.onFailure {
                if (it is IllegalStateException) {
                    errorLiveData.postValue(it.message)
                } else {
                    Log.e("viewModel", it.message ?: "error")
                }
                isLoadingLiveData.postValue(false)
            }.onSuccess {
                isLoadingLiveData.postValue(false)
                Log.d("viewModel", it.toString())
                tokenLiveData.postValue("Payment instrument added successfully with token ${it.body()?.token}")

            }
        }
    }

    fun updatePaymentType(paymentOption: PaymentOption) = when (paymentOption) {
        PaymentOption.Card -> {
            iconResLiveData.postValue(null)
            shouldShowPaypalLiveData.postValue(false)
            shouldShowCardDetailsLiveData.postValue(true)
        }
        PaymentOption.PayPal -> {
            iconResLiveData.postValue(R.drawable.ic_paypal)
            shouldShowCardDetailsLiveData.postValue(false)
            shouldShowPaypalLiveData.postValue(true)
        }
    }
}

class PaymentViewModelFactory(
    val app: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return (PaymentViewModel(app) as T)
    }
}

fun <T> mutableLiveDataOf(value: T): MutableLiveData<T> =
    MutableLiveData<T>().apply { this.value = value }
