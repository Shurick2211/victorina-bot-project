package com.nimko.bot.services

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service


@Service
class SchedulingServiceImpl @Autowired constructor(
    private val victorinaServices: VictorinaServices,
    private val prizeServices: PrizeServices
):SchedulingService {


    private val log = LoggerFactory.getLogger("SCHEDULER")


    @Scheduled(cron = "\${checked.time.end}")
    override fun checkEndedVictorinas() {
        val listEndedVictorinasMarcAsActive = victorinaServices.getEndedVictorinsMarcAsActive()
        if(listEndedVictorinasMarcAsActive.isNotEmpty()) {
            log.info(listEndedVictorinasMarcAsActive.stream().map { it.name }.reduce{ n, m -> n + ", " + m}.get())
            prizeServices.playPrize(listEndedVictorinasMarcAsActive)
        }
    }

    @Scheduled(cron = "\${checked.time.start}")
    override fun checkStartChanelViqtorinas() {
        val listToStart = victorinaServices.startVictorinsAndIsActiveTrueAllVictorinas()
        val listStartChanellVictorinas = listToStart.filter { it.hasPrize && it.channel != null }
        if(listStartChanellVictorinas.isNotEmpty()) {
            log.info(listStartChanellVictorinas.stream().map { it.name }.reduce{ n, m -> n + ", " + m}.get())
            prizeServices.startVictorinasForPlayPrize(listStartChanellVictorinas)
        }
        val listPersonVictorins = listToStart.minus(listStartChanellVictorinas)
        if(listPersonVictorins.isNotEmpty())
            prizeServices.startVictorinasForPlayPrize(listPersonVictorins)
    }


}