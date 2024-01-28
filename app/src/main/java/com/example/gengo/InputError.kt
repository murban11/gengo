package com.example.gengo

import androidx.annotation.StringRes

enum class InputError(@StringRes val message: Int) {
    OK(message = R.string.empty),
    Empty(message = R.string.input_field_is_empty),
}