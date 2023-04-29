package it.uniroma2.isw2.flows;

import it.uniroma2.isw2.computer.BuggyClassesComputer;
import it.uniroma2.isw2.computer.VersionsFixer;
import it.uniroma2.isw2.computer.MetricsComputer;
import it.uniroma2.isw2.model.TicketInfo;
import it.uniroma2.isw2.model.VersionInfo;
import it.uniroma2.isw2.retriever.ClassesRetriever;
import it.uniroma2.isw2.retriever.CommitRetriever;
import it.uniroma2.isw2.retriever.TicketRetriever;
import it.uniroma2.isw2.retriever.VersionRetriever;
import it.uniroma2.isw2.writer.ARFWriter;
import it.uniroma2.isw2.writer.CSVWriter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ExecutionFlow {

    private ExecutionFlow() {}

    public static void execute(String projectPath, String projectName) throws Exception {
        int maxIndex = RetrievingFlow.retrieve(projectPath, projectName);

        WekaFlow.weka(projectName, maxIndex) ;
    }


}
