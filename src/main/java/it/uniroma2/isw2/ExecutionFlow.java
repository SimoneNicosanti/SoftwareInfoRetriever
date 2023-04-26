package it.uniroma2.isw2;

import it.uniroma2.isw2.computer.BuggyClassesComputer;
import it.uniroma2.isw2.computer.VersionsFixer;
import it.uniroma2.isw2.computer.MetricsComputer;
import it.uniroma2.isw2.computer.TicketFilter;
import it.uniroma2.isw2.model.TicketInfo;
import it.uniroma2.isw2.model.VersionInfo;
import it.uniroma2.isw2.retriever.ClassesRetriever;
import it.uniroma2.isw2.retriever.CommitRetriever;
import it.uniroma2.isw2.retriever.TicketRetriever;
import it.uniroma2.isw2.retriever.VersionRetriever;
import it.uniroma2.isw2.writer.CSVWriter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class ExecutionFlow {

    private ExecutionFlow() {}

    public static void execute(String projectPath, String projectName) throws IOException, URISyntaxException, GitAPIException {
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();

        Repository repo = repositoryBuilder.setGitDir(new File(projectPath + projectName + "/.git")).build();
        Git git = new Git(repo) ;

        VersionRetriever versionRetriever = new VersionRetriever(projectName) ;
        List<VersionInfo> versionInfoList = versionRetriever.retrieveVersions() ;

        VersionInfo firstVersion = versionInfoList.get(0) ;
        VersionInfo lastVersion = versionInfoList.get(versionInfoList.size() - 1) ;

        TicketRetriever ticketRetriever = new TicketRetriever(projectName) ;
        List<TicketInfo> ticketInfoList = ticketRetriever.retrieveBugTicket(versionInfoList) ;

        TicketFilter filter = new TicketFilter(projectName) ;
        List<TicketInfo> filteredList = filter.filterTicketByVersions(ticketInfoList, firstVersion.getVersionDate());

        VersionsFixer versionsFixer = new VersionsFixer() ;
        versionsFixer.fixInjectedAndAffectedVersions(filteredList, versionInfoList);

        CommitRetriever commitRetriever = new CommitRetriever(projectName, git, lastVersion.getVersionDate()) ;
        commitRetriever.retrieveCommitListForAllVersions(versionInfoList) ;

        List<TicketInfo> completeTicketList = commitRetriever.retrieveFixCommitListForAllTickets(filteredList, firstVersion, lastVersion) ;

        ClassesRetriever classesRetriever = new ClassesRetriever(projectName, repo) ;
        classesRetriever.retrieveClassesForAllVersions(versionInfoList);

        BuggyClassesComputer buggyClassesComputer = new BuggyClassesComputer(projectName, repo, git) ;
        buggyClassesComputer.computeBuggyClassesForAllVersions(completeTicketList, versionInfoList);

        MetricsComputer metricsComputer = new MetricsComputer(projectName, repo, git) ;
        metricsComputer.computeMetrics(versionInfoList, completeTicketList);

        git.close();
        repo.close();

        CSVWriter csvWriter = new CSVWriter(projectName) ;
        csvWriter.writeAllVersionInfo(versionInfoList);

    }
}
