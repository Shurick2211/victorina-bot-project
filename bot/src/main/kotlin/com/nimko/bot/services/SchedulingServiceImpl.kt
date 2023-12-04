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
        log.info(listEndedVictorinasMarcAsActive.stream().map { it.name }.reduce{ n, m -> n + ", " + m}.get())
        if(listEndedVictorinasMarcAsActive.isNotEmpty()) prizeServices.playPrize(listEndedVictorinasMarcAsActive)
    }


}