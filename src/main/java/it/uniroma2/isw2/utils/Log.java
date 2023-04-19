package it.uniroma2.isw2.utils;

import it.uniroma2.isw2.model.TicketInfo;
import it.uniroma2.isw2.model.VersionInfo;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {

    private Log() {}

    public static void logTicketList(List<TicketInfo> ticketInfoList, String phase) {
        StringBuilder stringBuilder = new StringBuilder() ;
        stringBuilder.append("\n").append("Lista Ticket: ").append(phase).append("\n");
        stringBuilder.append("Dimensione Lista: ").append(ticketInfoList.size()).append("\n") ;
        for (TicketInfo ticketInfo : ticketInfoList) {
            stringBuilder.append(ticketInfo).append("\n") ;
        }

        Logger.getGlobal().log(Level.INFO, "{0}", stringBuilder);
    }

    public static void logVersionList(List<VersionInfo> versionInfoList) {
        StringBuilder stringBuilder = new StringBuilder() ;
        stringBuilder.append("\n").append("Lista Versioni").append("\n");
        stringBuilder.append("Dimensione Lista: ").append(versionInfoList.size()) ;
        for (VersionInfo versionInfo : versionInfoList) {
            stringBuilder.append(versionInfo).append("\n") ;
        }

        Logger.getGlobal().log(Level.INFO, "{0}", stringBuilder);
    }
}
