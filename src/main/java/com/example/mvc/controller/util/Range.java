package com.example.mvc.controller.util;

/**
 * Represents a range of data.
 * 
 * @author Darrin Collins
 */
public class Range {

    private static final Range DEFAULT_RANGE = new Range(0, 100);
    
    private int start;
    private int end;
    
    public Range(int start, int end) {
        this.start = start;
        this.end = end;
    }
    
    /**
     * Returns the page size that was requested by this range.
     * @return the page size that was requested by this range.
     */
    public int getPageSize() {
        return end - start + 1;
    }
    
    /**
     * Returns the page number requested by this range.
     * @return the page number requested by this range.
     */
    public int getPage() {
        if (start == 0) {
            return 0;
        }
        return start / getPageSize();
    }

    /**
     * Parses a range HTTP header.
     * @param range the Range HTTP header to parse.
     * @return a <code>Range</code> object.
     */
    public static Range parseRange(String range) {
        if (range == null) {
            return DEFAULT_RANGE;
        }
        String[] parsed = range.substring("range=".length()).split("-");
        
        if (parsed.length != 2) {
            return DEFAULT_RANGE;
        }
        
        int start = Integer.parseInt(parsed[0]);
        int end = Integer.parseInt(parsed[1]);
        
        return new Range(start, end);
    }

    /**
     * @return the start
     */
    public int getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * @return the pageSize
     */
    public int getEnd() {
        return end;
    }

    /**
     * @param pageSize the pageSize to set
     */
    public void setEnd(int end) {
        this.end = end;
    }
    
    
}
