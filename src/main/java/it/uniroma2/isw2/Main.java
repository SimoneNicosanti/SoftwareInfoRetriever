package it.uniroma2.isw2;

import it.uniroma2.isw2.enums.ProjectEnum;
import it.uniroma2.isw2.exception.ProportionException;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.net.URISyntaxException;


public class Main {

    private static final String PROJECT_PATH = "/home/simone/Scrivania/University/ISW2/Projects/" ;

    public static void main(String[] args) throws IOException, URISyntaxException, GitAPIException, ProportionException {

        for (ProjectEnum project : ProjectEnum.values()) {
            ExecutionFlow.execute(PROJECT_PATH, project.name().toLowerCase());
        }

    }
}