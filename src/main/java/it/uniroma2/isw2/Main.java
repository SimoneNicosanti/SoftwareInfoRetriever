package it.uniroma2.isw2;

import it.uniroma2.isw2.computer.BuggyClassesComputer;
import it.uniroma2.isw2.computer.FixAndAffectedVersionsComputer;
import it.uniroma2.isw2.computer.TicketFilter;
import it.uniroma2.isw2.exception.ProportionException;
import it.uniroma2.isw2.model.TicketInfo;
import it.uniroma2.isw2.model.VersionInfo;
import it.uniroma2.isw2.retriever.ClassesRetriever;
import it.uniroma2.isw2.retriever.CommitRetriever;
import it.uniroma2.isw2.retriever.TicketRetriever;
import it.uniroma2.isw2.retriever.VersionRetriever;
import it.uniroma2.isw2.writer.CsvWriter;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;


public class Main {

    public static final String PROJECT_NAME = "bookkeeper" ;
    private static final String PROJECT_PATH = "/home/simone/Scrivania/University/ISW2/Projects/" ;
    public static void main(String[] args) throws IOException, URISyntaxException, GitAPIException, ProportionException {

        VersionRetriever versionRetriever = new VersionRetriever(PROJECT_NAME) ;
        List<VersionInfo> versionInfoList = versionRetriever.retrieveVersions() ;

        VersionInfo firstVersion = versionInfoList.get(0) ;
        VersionInfo lastVersion = versionInfoList.get(versionInfoList.size() - 1) ;

        TicketRetriever ticketRetriever = new TicketRetriever(PROJECT_NAME) ;
        List<TicketInfo> ticketInfoList = ticketRetriever.retrieveBugTicket(versionInfoList) ;

        TicketFilter filter = new TicketFilter(PROJECT_NAME) ;
        List<TicketInfo> filteredList = filter.filterTicketByVersions(ticketInfoList, firstVersion.getVersionDate());

        FixAndAffectedVersionsComputer versionsComputer = new FixAndAffectedVersionsComputer() ;
        versionsComputer.setInjectedAndAffectedVersionForAllTickets(filteredList, versionInfoList);

        CommitRetriever commitRetriever = new CommitRetriever(PROJECT_PATH, PROJECT_NAME, lastVersion.getVersionDate()) ;
        commitRetriever.retrieveCommitListForAllVersions(versionInfoList) ;

        List<TicketInfo> completeTicketList = commitRetriever.retrieveFixCommitListForAllTickets(filteredList, firstVersion, lastVersion) ;

        ClassesRetriever classesRetriever = new ClassesRetriever(PROJECT_NAME, PROJECT_PATH) ;
        classesRetriever.retrieveClassesForAllVersions(versionInfoList);

        BuggyClassesComputer buggyClassesComputer = new BuggyClassesComputer(PROJECT_NAME, PROJECT_PATH) ;
        buggyClassesComputer.computeBuggyClassesForAllVersions(completeTicketList, versionInfoList);

        CsvWriter csvWriter = new CsvWriter(PROJECT_NAME) ;
        csvWriter.writeAllVersionInfo(versionInfoList);
    }
}