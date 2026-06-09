package org.ferris.add.main;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.stream.Stream;

public class TvMazeParser {

    private final ObjectMapper mapper = new ObjectMapper();

    public Stream<EpisodeInfo> streamUpcomingEpisodes(InputStream jsonStream) throws Exception {

        LocalDate today = LocalDate.now();
        LocalDate cutoff = today.plusWeeks(2);

        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser(jsonStream);

        if (parser.nextToken() != JsonToken.START_ARRAY) {
            throw new IllegalStateException("Expected JSON array");
        }

        Stream.Builder<EpisodeInfo> builder = Stream.builder();

        while (parser.nextToken() == JsonToken.START_OBJECT) {

            JsonNode episode = mapper.readTree(parser);

            String airDateText = episode.path("airdate").asText(null);

            if (airDateText == null || airDateText.isBlank()) {
                continue;
            }

            LocalDate airDate = LocalDate.parse(airDateText);

            if (airDate.isBefore(today) || airDate.isAfter(cutoff)) {
                continue;
            }

            JsonNode show = episode.path("_embedded").path("show");

            String channelName = null;

            JsonNode network = show.path("network");
            if (!network.isNull()) {
                channelName = network.path("name").asText(null);
                
                JsonNode country = network.path("country");
                if (!country.isNull()) {
                    String countryCode = country.path("code").asText("");
                    if (!"US".equalsIgnoreCase(countryCode)) {
                        continue;
                    }
                }
            }

            if (channelName == null || channelName.isBlank()) {
                JsonNode webChannel = show.path("webChannel");
                if (!webChannel.isNull()) {
                    channelName = webChannel.path("name").asText(null);
                }
            }

            builder.add(new EpisodeInfo(
                    show.path("name").asText(),
                    episode.path("name").asText(),
                    episode.path("season").asInt(),
                    episode.path("number").asInt(),
                    channelName,
                    airDate));
        }

        return builder.build();
    }
}
