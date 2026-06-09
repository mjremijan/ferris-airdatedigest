package org.ferris.add.main;

import java.io.InputStream;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) throws Exception {

        TvMazeClient client = new TvMazeClient();
        TvMazeParser parser = new TvMazeParser();

        try (InputStream in = client.downloadSchedule()) {
            AtomicInteger i = new AtomicInteger(1);
            parser.streamUpcomingEpisodes(in)
                    .sorted(Comparator.comparing(EpisodeInfo::airDate))
                    .forEach(ep ->
                            //1485
                            System.out.printf(
                                    "%d %s S%02dE%02d %-30s %-15s %s%n",
                                    i.getAndIncrement(),
                                    ep.showName(),
                                    ep.season(),
                                    ep.episodeNumber(),
                                    ep.episodeName(),
                                    ep.channelName(),
                                    ep.airDate()));
        }
    }
}
