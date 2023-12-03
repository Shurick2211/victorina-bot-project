package com.nimko.bot.services

import com.nimko.bot.models.VictorinaDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class SchedulingServiceImpl @Autowired constructor(
    val victorinaServices: VictorinaServices
):SchedulingService {

    lateinit var listEndedVictorinasMarcAsActive:List<VictorinaDto>

    val log = LoggerFactory.getLogger("SCHEDULER")

    @Scheduled(cron = "\${checked.time.end}")
    override fun checkEndedVictorinas() {
        listEndedVictorinasMarcAsActive = victorinaServices.getEndedVictorinsMarcAsActive()
        log.info(listEndedVictorinasMarcAsActive.stream().map { it.name }.reduce{ n, m -> n + ", " + m}.get())

    }




}