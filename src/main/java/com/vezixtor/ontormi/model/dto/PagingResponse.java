package com.vezixtor.ontormi.model.dto;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class PagingResponse<T extends PagingResponse<T>> {

    private List<Object> data;
    private Paging paging;

    protected abstract List<?> getSubclassData();

    protected abstract String getDeclaredFieldAtSubclassData();

    public T withPaging() {
        return withPaging(null);
    }

    public T withPaging(String url) {
        data = new ArrayList<>();
        data.addAll(getSubclassData());
        paging = new Paging(new Cursor(getBeforeRecordId(), getAfterRecordId()), null, null);
        if (hasUrl(url)) {
            withUrl(url);
        }
        return (T) this;
    }

    private boolean hasUrl(String url) {
        return url != null && !url.trim().isEmpty();
    }

    private void withUrl(String url) {
        String previous = url + "?previous=" + paging.getCursors().getBefore();
        String next = url + "?next=" + paging.getCursors().getAfter();
        paging.setPrevious(previous);
        paging.setNext(next);
    }

    private String getBeforeRecordId() {
        Object firstRecord = data.get(0);
        return getObjectId(firstRecord);
    }

    private String getAfterRecordId() {
        Object lastRecord = data.get(data.size() - 1);
        return getObjectId(lastRecord);
    }

    private String getObjectId(Object object) {
        String objectIdValue = "";
        try {
            Field field = object.getClass().getDeclaredField(getDeclaredFieldAtSubclassData());
            field.setAccessible(true);
            objectIdValue = String.valueOf(field.get(object));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return objectIdValue;
    }

    public class Paging {
        Cursor cursors;
        String previous;
        String next;

        Paging(PagingResponse<T>.Cursor cursors, String previous, String next) {
            this.cursors = cursors;
            this.previous = previous;
            this.next = next;
        }

        public Cursor getCursors() {
            return cursors;
        }

        public void setCursors(Cursor cursors) {
            this.cursors = cursors;
        }

        public String getPrevious() {
            return previous;
        }

        public void setPrevious(String previous) {
            this.previous = previous;
        }

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }
    }

    public class Cursor {
        String after;
        String before;

        Cursor(String before, String after) {
            this.before = before;
            this.after = after;
        }

        public String getAfter() {
            return after;
        }

        public void setAfter(String after) {
            this.after = after;
        }

        public String getBefore() {
            return before;
        }

        public void setBefore(String before) {
            this.before = before;
        }
    }

    public Paging getPaging() {
        return paging;
    }

    public List<?> getData() {
        return data;
    }
}