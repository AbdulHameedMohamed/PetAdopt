package com.example.sharing.presentation.model.mapper

import com.example.common.domain.model.animal.Animal
import com.example.common.presentation.model.mappers.UiMapper
import com.example.sharing.presentation.model.UIAnimalToShare
import javax.inject.Inject

class UiAnimalToShareMapper @Inject constructor(): UiMapper<Animal, UIAnimalToShare> {

  override fun mapToView(input: Animal): UIAnimalToShare {
    val message = createMessage(input)

    return UIAnimalToShare(input.media.getFirstSmallestAvailablePhoto(), message)
  }

  private fun createMessage(input: Animal): String {
    val contact = input.organizationContact
    val formattedAddress = contact?.formattedAddress
    val formattedContactInfo = contact?.formattedContactInfo

    return "${input.description}\n\nOrganization info:\n$formattedAddress\n\n$formattedContactInfo"
  }
}