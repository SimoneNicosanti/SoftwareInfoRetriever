package it.uniroma2.isw2.computer;

import it.uniroma2.isw2.model.TicketInfo;
import it.uniroma2.isw2.model.VersionInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class InjectedVersionsComputer {
    public void setInjectedVersionsForTickets(List<TicketInfo> ticketInfoList, List<VersionInfo> versionInfoList, Float proportion) {

        Logger.getGlobal().log(Level.INFO, "Computing Injected Versions With Proportion {0}",  proportion);

        for (TicketInfo ticketInfo : ticketInfoList) {
            if (ticketInfo.getInjectedVersion() == null) {
                VersionInfo injectedVersion = computeInjectedVersion(ticketInfo, versionInfoList, proportion) ;
                ticketInfo.setInjectedVersion(injectedVersion);
            }
        }

        for (TicketInfo ticketInfo : ticketInfoList) {
            List<VersionInfo> affectedVersionList = computeAffectedVersions(ticketInfo, versionInfoList) ;
            ticketInfo.setAffectedVersionList(affectedVersionList);
        }

        StringBuilder stringBuilder = new StringBuilder("Computed Injected Versions\n") ;
        for (TicketInfo ticketInfo : ticketInfoList) {
            stringBuilder.append(ticketInfo.toString()).append("\n") ;
        }
        Logger.getGlobal().log(Level.INFO, "{0}", stringBuilder);

    }


    private List<VersionInfo> computeAffectedVersions(TicketInfo ticketInfo, List<VersionInfo> versionInfoList) {
        List<VersionInfo> affectedVersionList = new ArrayList<>() ;
        for (VersionInfo versionInfo : versionInfoList) {
            int injectedNumber = ticketInfo.getInjectedVersion().getReleaseNumber() ;
            int fixNumber =  ticketInfo.getFixVersion().getReleaseNumber() ;

            if ( (injectedNumber <= versionInfo.getReleaseNumber()) && (versionInfo.getReleaseNumber() < fixNumber)) {
                affectedVersionList.add(versionInfo) ;
            }
        }
        return affectedVersionList ;
    }


    private VersionInfo computeInjectedVersion(TicketInfo ticketInfo, List<VersionInfo> versionInfoList, Float proportion) {
        int fixReleaseNumber = ticketInfo.getFixVersion().getReleaseNumber() ;
        int openingReleaseNumber = ticketInfo.getOpeningVersion().getReleaseNumber() ;

        Integer proportionInjectedVersion ;
        if (fixReleaseNumber == openingReleaseNumber) {
            proportionInjectedVersion = (int) (fixReleaseNumber - proportion);
        }
        else {
            proportionInjectedVersion = (int) (fixReleaseNumber - (fixReleaseNumber - openingReleaseNumber) * proportion);
        }
        Integer injectedVersionIndex = Integer.max(0, proportionInjectedVersion) ;

        return versionInfoList.get(injectedVersionIndex) ;
    }
}
