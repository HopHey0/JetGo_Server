package com.hophey.service

import com.hophey.dto.FlightDto
import com.hophey.dto.toDto
import com.hophey.repository.FavouriteRepository
import com.hophey.repository.UserRepository

class FavouriteService(
    private val favouriteRepository: FavouriteRepository,
    private val userRepository: UserRepository
) {
    fun getFavourites(email: String): List<FlightDto>? {
        val user = userRepository.findByEmail(email) ?: return null
        return favouriteRepository.getFavourites(user.id).map { it.toDto() }
    }

    fun addFavourite(email: String, flightId: Long): Boolean? {
        val user = userRepository.findByEmail(email) ?: return null
        return favouriteRepository.addFavourite(user.id, flightId)
    }

    fun removeFavourite(email: String, flightId: Long): Boolean? {
        val user = userRepository.findByEmail(email) ?: return null
        return favouriteRepository.removeFavourite(user.id, flightId)
    }
}