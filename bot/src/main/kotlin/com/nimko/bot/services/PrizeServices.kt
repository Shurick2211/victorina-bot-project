package com.nimko.bot.services

import com.nimko.bot.models.VictorinaDto

interface PrizeServices {
    fun playPrize(victorinas:List<VictorinaDto>)

    fun startVictorinasForPlayPrize(victorinas:List<VictorinaDto>)
}