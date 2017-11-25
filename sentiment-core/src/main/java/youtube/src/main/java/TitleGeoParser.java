package youtube.src.main.java;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.util.Joiner;
//import com.google.api.services.samples.youtube.cmdline.Auth;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.GeoPoint;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class TitleGeoParser {

  /**
   * Define a global variable that identifies the name of a file that contains
   * the developer's API key.
   */
  private static final String PROPERTIES_FILENAME = "./youtube.properties";

  // private static final long NUMBER_OF_VIDEOS_RETURNED = 25;

  /**
   * Define a global instance of a Youtube object, which will be used to make
   * YouTube Data API requests.
   */
  private static YouTube youtube;

  /**
   * Initialize a YouTube object to search for videos on YouTube. Then display
   * the name and thumbnail image of each video in the result set.
   *
   * @param args
   *          command line args.
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    // Read the developer key from the properties file.
    // List<String> videoIds = new ArrayList<String>();
    Properties properties = new Properties();
    try {
      InputStream in = TitleGeoParser.class
          .getResourceAsStream(PROPERTIES_FILENAME);
      properties.load(in);

    } catch (IOException e) {
      System.err.println("There was an error reading " + PROPERTIES_FILENAME
          + ": " + e.getCause() + " : " + e.getMessage());
      System.exit(1);
    }

    youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY,
        new HttpRequestInitializer() {
          @Override
          public void initialize(HttpRequest request) throws IOException {
          }
        }).setApplicationName("youtube-cmdline-geolocationsearch-sample")
            .build();
    
    YouTube.Search.List search = youtube.search().list("id,snippet");

    // Set your developer key from the {{ Google Cloud Console }} for
    // non-authenticated requests. See:
    // {{ https://cloud.google.com/console }}
    String apiKey = properties.getProperty("youtube.apikey");
    search.setKey(apiKey);

    // Restrict the search results to only include videos. See:
    // https://developers.google.com/youtube/v3/docs/search/list#type
    search.setType("video");

    // Merge video IDs
    // videoIds.add("qV6wAJVCVus");
    Joiner stringJoiner = Joiner.on(',');
    String videoId = "wHkPb68dxEw"; // stringJoiner.join(videoIds);

    // Call the YouTube Data API's youtube.videos.list method to
    // retrieve the resources that represent the specified videos.
    YouTube.Videos.List listVideosRequest = youtube.videos()
        .list("snippet, recordingDetails").setId(videoId);
    VideoListResponse listResponse = listVideosRequest.execute();

    List<Video> videoList = listResponse.getItems();

    if (videoList != null) {
      prettyPrint(videoList.iterator());
    }

  }

  /*
   * Prints out all results in the Iterator. For each result, print the title,
   * video ID, location, and thumbnail.
   *
   * @param iteratorVideoResults Iterator of Videos to print
   *
   * @param query Search query (String)
   */
  private static void prettyPrint(Iterator<Video> iteratorVideoResults) {

    System.out.println(
        "\n=============================================================");
    System.out.println(
        "=============================================================\n");

    while (iteratorVideoResults.hasNext()) {

      Video singleVideo = iteratorVideoResults.next();

      Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails()
          .getDefault();
      GeoPoint location = singleVideo.getRecordingDetails().getLocation();

      System.out.println(" Video Id" + singleVideo.getId());
      System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
      System.out.println(" Location: " + location.getLatitude() + ", "
          + location.getLongitude());
      System.out.println(" Thumbnail: " + thumbnail.getUrl());
      System.out.println(
          "\n-------------------------------------------------------------\n");
    }
  }

}
