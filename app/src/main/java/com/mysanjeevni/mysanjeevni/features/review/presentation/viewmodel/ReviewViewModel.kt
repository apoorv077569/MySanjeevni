package com.mysanjeevni.mysanjeevni.features.review.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.review.domain.usecase.AddReviewUseCase
import com.mysanjeevni.mysanjeevni.features.review.domain.usecase.GetReviewsUseCase
import com.mysanjeevni.mysanjeevni.features.review.domain.usecase.UpdateReviewUseCase
import com.mysanjeevni.mysanjeevni.features.review.presentation.state.ReviewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val getReviewsUseCase: GetReviewsUseCase,
    private val addReviewUseCase: AddReviewUseCase,
    private val updateReviewUseCase: UpdateReviewUseCase
) : ViewModel() {

    private val _state =
        MutableStateFlow(ReviewState())

    val state = _state.asStateFlow()

    fun loadReviews(
        productId: String
    ) {

        viewModelScope.launch {

            _state.update {
                it.copy(isLoading = true)
            }

            getReviewsUseCase(productId)
                .onSuccess { reviews ->

                    _state.update {
                        it.copy(
                            isLoading = false,
                            reviews = reviews
                        )
                    }
                }
                .onFailure { error ->

                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
        }
    }

    fun addReview(userId:String, productId: String, rating:Int, title:String, comment:String, userName: String){
        viewModelScope.launch {
            addReviewUseCase(userId,productId,rating,title,comment,userName)
                .onSuccess{
                    loadReviews(productId)
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
        }
    }

    fun updateReview(
        reviewId: String,
        userId: String,
        productId: String,
        rating: Int,
        title: String,
        comment: String
    ){
        viewModelScope.launch {
            updateReviewUseCase(reviewId,userId,rating,title,comment)
                .onSuccess{
                    loadReviews(productId)
                }
                .onFailure {error ->
                    _state.update {
                        it.copy(
                            error = error.message
                        )
                    }
                }
        }
    }
}