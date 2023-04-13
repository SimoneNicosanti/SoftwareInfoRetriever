package it.uniroma2.isw2;

import it.uniroma2.isw2.computer.InjectedVersionsComputer;
import it.uniroma2.isw2.computer.ProportionComputer;
import it.uniroma2.isw2.computer.TicketFilter;
import it.uniroma2.isw2.exception.ProportionException;
import it.uniroma2.isw2.model.TicketInfo;
import it.uniroma2.isw2.model.VersionInfo;
import it.uniroma2.isw2.retriever.CommitRetriever;
import it.uniroma2.isw2.retriever.TicketRetriever;
import it.uniroma2.isw2.retriever.VersionRetriever;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class Main {

    private static final String PROJECT_NAME = "bookkeeper" ;
    private static final String PROJECT_PATH = "/home/simone/Scrivania/University/ISW2/Projects/" ;
    public static void main(String[] args) throws IOException, URISyntaxException, GitAPIException, ProportionException {

        VersionRetriever versionRetriever = new VersionRetriever(PROJECT_NAME) ;
        List<VersionInfo> versionInfoList = versionRetriever.retrieveVersions() ;

        TicketRetriever ticketRetriever = new TicketRetriever(PROJECT_NAME) ;
        List<TicketInfo> ticketInfoList = ticketRetriever.retrieveBugTicket(versionInfoList) ;

        TicketFilter filter = new TicketFilter() ;
        List<TicketInfo> filteredList = filter.filterTicket(ticketInfoList, versionInfoList.get(0).getVersionDate());

        ProportionComputer proportionComputer = new ProportionComputer() ;
        Float proportion = proportionComputer.computeProportion(filteredList);

        InjectedVersionsComputer injectedVersionsComputer = new InjectedVersionsComputer() ;
        injectedVersionsComputer.setInjectedVersionsForTickets(filteredList, versionInfoList, proportion);

    }
}