package org.ferris.add.main;

import java.time.LocalDate;

public record EpisodeInfo(
        String showName,
        String episodeName,
        int season,
        int episodeNumber,
        String channelName,
        LocalDate airDate
) {}
