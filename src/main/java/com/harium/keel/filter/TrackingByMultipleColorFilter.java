package com.harium.keel.filter;

import com.harium.keel.core.source.ImageSource;
import com.harium.keel.feature.Component;
import com.harium.keel.filter.color.MultipleColorStrategy;
import com.harium.keel.filter.search.flood.FloodFillSearch;
import com.harium.etyl.commons.graphics.Color;

import java.util.List;

public class TrackingByMultipleColorFilter extends TrackingFilter {

    private static int DEFAULT_TOLERANCE = 0x40;
    private MultipleColorStrategy colorStrategy;

    public TrackingByMultipleColorFilter(int w, int h) {
        super(new FloodFillSearch(w, h));

        colorStrategy = new MultipleColorStrategy();

        this.searchStrategy.setPixelStrategy(colorStrategy);
    }

    public TrackingByMultipleColorFilter(int w, int h, Color color, int tolerance) {
        this(w, h);

        colorStrategy.addColor(color, tolerance);
    }

    public TrackingByMultipleColorFilter(int w, int h, Color color) {
        this(w, h, color, DEFAULT_TOLERANCE);
    }

    @Override
    public List<Component> filter(ImageSource bimg, Component component) {
        //Setup happens on filter
        //searchStrategy.setup();
        return searchStrategy.filter(bimg, component);
    }

    @Override
    public Component filterFirst(ImageSource bimg, Component component) {
        //Setup happens on filter
        //searchStrategy.setup();
        return searchStrategy.filterFirst(bimg, component);
    }

    public void addColor(Color color, int tolerance) {
        colorStrategy.addColor(color, tolerance);
    }

    public void addColor(Color color, int redTolerance, int greenTolerance, int blueTolerance) {
        colorStrategy.addColor(color, redTolerance, greenTolerance, blueTolerance);
    }

    public void addColor(Color color, int maxTolerance, int minTolerance) {
        colorStrategy.addColor(color, maxTolerance, minTolerance);
    }

}