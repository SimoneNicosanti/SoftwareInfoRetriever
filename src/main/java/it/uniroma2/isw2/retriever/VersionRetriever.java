package it.uniroma2.isw2.retriever;

import it.uniroma2.isw2.model.VersionInfo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class VersionRetriever {

    String projectName ;

    public VersionRetriever(String projectName) {
        this.projectName = projectName ;
    }

    public List<VersionInfo> retrieveVersions() throws URISyntaxException, IOException {

        String urlString = "https://issues.apache.org/jira/rest/api/2/project/" + projectName.toUpperCase();
        URI uri = new URI(urlString);
        URL url = uri.toURL();

        JSONRetriever jsonReader = new JSONRetriever() ;
        String jsonString = jsonReader.getJsonString(url);
        JSONObject jsonObject = new JSONObject((jsonString));
        JSONArray jsonVersionArray = jsonObject.getJSONArray("versions");

        List<VersionInfo> versionInfoList = parseVersionArray(jsonVersionArray) ;
        versionInfoList.sort(Comparator.comparing(VersionInfo::getVersionDate));

        for (int i = 0 ; i < versionInfoList.size() ; i++) {
            versionInfoList.get(i).setReleaseNumber(i);
        }

        logVersionRetrieve(versionInfoList);

        return versionInfoList ;
    }

    private List<VersionInfo> parseVersionArray(JSONArray versionJsonArray) {
        List<VersionInfo> versionInfoList = new ArrayList<>() ;
        for (int i = 0; i < versionJsonArray.length(); i++) {
            VersionInfo versionInfo = parseVersionInfo(versionJsonArray.getJSONObject(i)) ;
            if (versionInfo != null) {
                versionInfoList.add(versionInfo) ;
            }
        }
        return versionInfoList ;
    }


    // TODO CI SONO VERSIONI CHE SONO RILASCIATE NELLA STESSA DATA!! CHE DOBBIAMO FARE ??
    private VersionInfo parseVersionInfo(JSONObject vesionJsonObject) {
        if (vesionJsonObject.has("releaseDate") && vesionJsonObject.has("name") && vesionJsonObject.has("id")) {
            String versionName = vesionJsonObject.get("name").toString();
            String dateString = vesionJsonObject.get("releaseDate").toString();
            String versionId = vesionJsonObject.get("id").toString() ;

            LocalDate versionDate = LocalDate.parse(dateString) ;

            return new VersionInfo(versionName, versionDate, versionId);
        }
        return null ;
    }

    public void logVersionRetrieve(List<VersionInfo> versionInfoList) {
        StringBuilder logString = new StringBuilder() ;
        logString.append("\nProject Versions\n") ;
        for (VersionInfo versionInfo : versionInfoList) {
            logString.append(versionInfo.toString()).append("\n") ;
        }
        Logger.getGlobal().log(Level.INFO, "{0}", logString);
    }
}
