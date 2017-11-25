package youtube.src.main.java;

import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import com.google.gdata.data.youtube.VideoEntry;

public class TitleExtractor {

  public static String getTitleQuietly(String youtubeUrl) {
    try {
      if (youtubeUrl != null) {
        URL embededURL = new URL(
            "http://www.youtube.com/oembed?url=" + youtubeUrl + "&format=json");

        return new JSONObject(IOUtils.toString(embededURL)).getString("title");
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  
  public static void getLocation() {
    VideoEntry video = new VideoEntry("wHkPb68dxEw");
    System.out.println(video.getTitle());
  }
  
  public static void main(String args[]) {
    System.out.println(getTitleQuietly("https://www.youtube.com/watch?v=wHkPb68dxEw"));
    getLocation();
  }

}
