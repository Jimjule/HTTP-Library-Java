package route;

import java.util.ArrayList;
import java.util.List;

public interface Route {
    String getBody();

    ArrayList<String> getHeaders();

    String formatAllow();

    List<String> getAllow();
}
