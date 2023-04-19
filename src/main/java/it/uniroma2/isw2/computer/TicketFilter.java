package it.uniroma2.isw2.computer;

import it.uniroma2.isw2.Main;
import it.uniroma2.isw2.model.TicketInfo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TicketFilter {

    public List<TicketInfo> filterTicket(List<TicketInfo> ticketInfoList, LocalDate firstVersionDate) {

        List<TicketInfo> filteredList = new ArrayList<>() ;
        for (TicketInfo ticketInfo : ticketInfoList) {
            boolean isValid = isValidTicket(ticketInfo, firstVersionDate) ;
            if (isValid) {
                filteredList.add(ticketInfo) ;
            }
        }

        StringBuilder stringBuilder = new StringBuilder() ;
        stringBuilder.append("Numero Ticket Filtrati Per ").append(Main.PROJECT_NAME.toUpperCase()).append(" >> ").append(filteredList.size()).append("\n") ;
        Logger.getGlobal().log(Level.INFO, "{0}", stringBuilder);

        return filteredList ;
    }


    private Boolean isValidTicket(TicketInfo ticketInfo, LocalDate firstVersionDate) {

        if (ticketInfo.getCreateDate().isBefore(firstVersionDate)) {
            return false ;
        }

        if (ticketInfo.getOpeningVersion() == null || ticketInfo.getFixVersion() == null) {
            return false ;
        }

        Integer openingRelease = ticketInfo.getOpeningVersion().getReleaseNumber() ;
        Integer fixRelease = ticketInfo.getFixVersion().getReleaseNumber() ;
        if (openingRelease > fixRelease) {
            return false ;
        }

        if (ticketInfo.getInjectedVersion() != null) {
            Integer injectedRelease = ticketInfo.getInjectedVersion().getReleaseNumber() ;
            if (injectedRelease >= openingRelease) {
                return false ;
            }
        }

        return true ;
    }
}
