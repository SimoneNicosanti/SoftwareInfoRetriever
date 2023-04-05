package it.uniroma2.isw2;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final String PROJECT_NAME = "bookkeeper" ;
    public static void main(String[] args) throws IOException, URISyntaxException, GitAPIException {
        String repoPath = "/home/simone/Scrivania/University/ISW2/Projects/" ;

        JiraRetriever retriever = new JiraRetriever() ;
        List<String> list = retriever.retrieveBugTicket(PROJECT_NAME) ;
        retriever.retrieveVersions(PROJECT_NAME) ;
        Logger.getGlobal().log(Level.INFO, list.toString());
        CommitRetriever commitRetriever = new CommitRetriever() ;

        List<RevCommit> commitList = commitRetriever.retrieveAllCommitsInfo(repoPath + PROJECT_NAME);


    }
}