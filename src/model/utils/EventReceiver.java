package model.utils;

public interface EventReceiver {
    void update(EventSource source, Object arg);
}
