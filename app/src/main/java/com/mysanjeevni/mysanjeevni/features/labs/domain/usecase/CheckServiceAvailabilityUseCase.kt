package com.mysanjeevni.mysanjeevni.features.labs.domain.usecase

import com.mysanjeevni.mysanjeevni.features.labs.domain.model.Serviceability
import com.mysanjeevni.mysanjeevni.features.labs.domain.repository.LabsRepository
import javax.inject.Inject

class CheckServiceabilityUseCase @Inject constructor(
    private val repository: LabsRepository
) {
    suspend operator fun invoke(testId: String, pincode: String): Result<Serviceability> {
        if (pincode.length != 6 || !pincode.all { it.isDigit() }) {
            return Result.failure(IllegalArgumentException("Please enter a valid 6-digit pincode"))
        }
        if (testId.isBlank()) {
            return Result.failure(IllegalArgumentException("Test ID is required"))
        }
        return repository.checkServiceability(testId, pincode)
    }
}