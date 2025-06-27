package com.example.petadopt.animals.domain

import java.io.IOException

class NetworkUnavailableException(message: String = "No network available :(") :
    IOException(message)

class NetworkException(message: String) : Exception(message)

class NoMoreAnimalsException(message: String) : Exception(message)