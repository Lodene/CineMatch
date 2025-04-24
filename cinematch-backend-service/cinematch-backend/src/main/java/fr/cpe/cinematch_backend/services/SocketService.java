package fr.cpe.cinematch_backend.services;

import fr.cpe.cinematch_backend.dtos.MovieDto;
import fr.cpe.cinematch_backend.dtos.requests.SocketRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class SocketService {

    @Autowired
    private Environment env;

    RestTemplate restTemplate = new RestTemplate();

    public String sendMoviesInfoToSocket(SocketRequest socketRequest) {
        String nodejsUrl = env.getProperty("nodejs.url");
        if (nodejsUrl == null) {
            // bad config
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SocketRequest> requestEntity = new HttpEntity<>(socketRequest, headers);
        String result = restTemplate.postForObject(nodejsUrl.concat("recommended-film"), requestEntity, String.class);
        if (result == null) {
            // retry until succes
            // fixme: add max retry count with static var
            this.sendMoviesInfoToSocket(socketRequest);
        }
        return "Success";
    }
}
