package com.nereidaro.parksnro

import java.io.Serializable

data class Park(
    var name: String,
    var desc: String?,
    var phone: String?,
    var webSite: String?,
    var openingTime: String?,
    var closingTime: String?,
    var sports: Boolean?,
    var children: Boolean?,
    var bar: Boolean?,
    var pets: Boolean?,
): Serializable
