package org.ferris.tadd.main;

public record ShowInfo(
          int id
        , String name
        , String language
        , String summary
) {
    @Override
    public String toString() {
        return "%s [%d]".formatted(name, id);
    }
}
