package ecs189.querying.github;

import ecs189.querying.Util;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Vincent on 10/1/2017.
 */
public class GithubQuerier {

    private static final String BASE_URL = "https://api.github.com/users/";

    public static String eventsAsHTML(String user) throws IOException, ParseException {
        List<JSONObject> response = getEvents(user);
        StringBuilder sb = new StringBuilder();
        sb.append("<div>");
        for (int i = 0; i < response.size(); i++) {
            JSONObject event = response.get(i);
            // Get event type
            String type = event.getString("type");
            // Get created_at date, and format it in a more pleasant style
            String creationDate = event.getString("created_at");
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
            SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM, yyyy");
            Date date = inFormat.parse(creationDate);
            String formatted = outFormat.format(date);
            // Get list of all its commits with their SHA and each commit's commit message
            JSONObject payload = event.getJSONObject("payload");
            JSONArray commitsList = payload.getJSONArray("commits");
            List<String> shaList = new ArrayList<String>();
            List<String> messageList = new ArrayList<String>();
            for(int k = 0; k < commitsList.length(); k++) {
                JSONObject commit = (JSONObject) commitsList.get(k);
                String sha = (commit.getString("sha")).substring(0, 8);
                shaList.add(sha);
                String message = commit.getString("message");
                messageList.add(message);
            }

            // Add type of event as header
            sb.append("<h3 class=\"type\">");
            sb.append(type);
            sb.append("</h3>");
            // Add formatted date
            sb.append(" on ");
            sb.append(formatted);
            sb.append("<br />");
            // Add commits
            sb.append("<h4>");
            sb.append(" Commits ");
            sb.append("</h4>");
            for(int m = 0; m < messageList.size(); m++) {
                sb.append(m+1);
                sb.append(". ");
                sb.append(shaList.get(m));
                sb.append(": ");
                sb.append(messageList.get(m));
                sb.append("<br />");
            }
            // Add collapsible JSON textbox (don't worry about this for the homework; it's just a nice CSS thing I like)
            sb.append("<a data-toggle=\"collapse\" href=\"#event-" + i + "\">JSON</a>");
            sb.append("<div id=event-" + i + " class=\"collapse\" style=\"height: auto;\"> <pre>");
            sb.append(event.toString());
            sb.append("</pre> </div>");
        }
        sb.append("</div>");
        return sb.toString();
    }

    private static List<JSONObject> getEvents(String user) throws IOException {
        List<JSONObject> eventList = new ArrayList<JSONObject>();
        String url = BASE_URL + user + "/events";
        System.out.println(url);
        JSONObject json = Util.queryAPI(new URL(url));
        System.out.println(json);

        JSONObject object;
        String eventType;
        JSONArray events = json.getJSONArray("root");
        for (int i = 0; i < events.length(); i++) {

            object = events.getJSONObject(i);
            eventType = object.getString("type");

            if(eventType.equals("PushEvent"))
                eventList.add(events.getJSONObject(i));

            if(eventList.size() == 10) break;

        }
        return eventList;
    }
}